/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.jsf;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.model.Sprint;
import org.inftel.scrum.model.Tarea;
import org.inftel.scrum.model.Usuario;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.primefaces.context.RequestContext;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.poi.util.IOUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author migueqm
 */
@ManagedBean
@ViewScoped
public class EstadisticaBean implements Serializable {

    @EJB
    private UsuarioFacade usuarioFacade;
    private CartesianChartModel modelBurndown;
    private CartesianChartModel modelBurnUp;
    private PieChartModel modelPie;
    private Usuario usuarioActual;
    private Sprint sprintActual;
    private BigInteger horasEstimadas = BigInteger.ZERO;
    private static final Logger log = Logger.getLogger(EstadisticaBean.class.getName());
    private boolean burnup = false;
    private boolean burndown = true;
    private boolean pie = false;
    //puntos quemados por dia
    private double velocidad = 0.0;
    //puntos que se dejan de quemar por reunion 
    private double costeReunion= 0.0;
    private String VelocidadFormatted;
    private Date forecast = new Date();
    private boolean sprintGood = true;
    private float progress = 0;
    private byte[] byteBurndownImage;
    private byte[] byteBurnupImage;
    private byte[] bytePieImage;
    private UsuarioController uc;

    /**
     * Creates a new instance of EstadisticaBean
     */
    public EstadisticaBean() {
    }

    @PostConstruct
    public void init() {
        RequestContext ReqContext = RequestContext.getCurrentInstance();
        if (ReqContext == null) {
            initEJB();
        }
        FacesContext context = FacesContext.getCurrentInstance();
        uc = (UsuarioController) context.getApplication().evaluateExpressionGet(context, "#{usuarioController}", UsuarioController.class);
        generarEstadisticas();
    }

    public void generarEstadisticas() {
        RequestContext ReqContext = RequestContext.getCurrentInstance();
        if (ReqContext == null) {
            initEJB();
        }
        //Inicializa burndown model
        modelBurndown = new CartesianChartModel();        
        usuarioActual = usuarioFacade.findByMail(uc.getEmail());        
        List<Sprint> sprintList = new ArrayList<Sprint>();
        if (usuarioActual.getGrupoId() != null) {
            sprintList = usuarioActual.getGrupoId().getProyectoId().getSprintList();
            log.log(Level.INFO, "SPRINTS del proyecto: {0}", sprintList);
            sprintActual = null;
            if (!sprintList.isEmpty()) {
                for (Sprint sprint : sprintList) {
                    if ((sprint.getInicio().before(new Date())) && ((sprint.getFin().after(new Date())))) {
                        sprintActual = sprint;
                        log.log(Level.INFO, "SPRINT EN CURSO: ", sprintActual.getId());
                    }
                }

                long periodoHoras = 0;
                periodoHoras = (sprintActual.getFin().getTime() - sprintActual.getInicio().getTime()) / (24 * 60 * 60 * 1000);

                for (Tarea t : sprintActual.getTareaList()) {
                    horasEstimadas = horasEstimadas.add(t.getDuracionEstimado());
                }

                //grafico ideal
                ChartSeries ideal = new ChartSeries();
                ideal.setLabel("Ideal");
                ideal.set(0, horasEstimadas);

                ideal.set(periodoHoras, 0);
                modelBurndown.addSeries(ideal);

                //grafico real
                ChartSeries real = new ChartSeries();
                real.setLabel("Real");
                real.set(0, horasEstimadas);

                BigInteger horasQuemadas = horasEstimadas;

                BigInteger duracion = BigInteger.ZERO;
                for (Tarea t : sprintActual.getTareaList()) {
                    if (t.getDone() == '2') {
                        duracion = BigInteger.valueOf((t.getFechaFin().getTime() - sprintActual.getInicio().getTime()) / (24 * 60 * 60 * 1000));
                        horasQuemadas = horasQuemadas.subtract(t.getTotalHoras());

                        real.set(duracion, horasQuemadas);
//                        log.log(Level.INFO, "DURACION: {0} HORAS: {1}", new Object[]{duracion, horasQuemadas});
                    }
                }

                this.velocidad = (horasEstimadas.subtract(horasQuemadas).doubleValue() / ((new Date().getTime() - sprintActual.getInicio().getTime()) / (24 * 60 * 60 * 1000)));
                DecimalFormat formatter = new DecimalFormat("#0.0");
                this.VelocidadFormatted = formatter.format(velocidad);

                this.costeReunion = this.velocidad / (24 * 4 );
                
                long millis = (Math.round(horasEstimadas.longValue() * 24 / velocidad) * 60 * 60 * 1000);
                forecast.setTime(millis + sprintActual.getInicio().getTime());

                if (forecast.before(sprintActual.getFin())) {
                    sprintGood = true;
                } else {
                    sprintGood = false;
                }

                float dif = (horasEstimadas.subtract(horasQuemadas)).floatValue();
                progress = 100 * dif / horasEstimadas.floatValue();
                progress = Math.round(progress);

                modelBurndown.addSeries(real);

                createBurndownImage(ideal, real);

                //Inicializa pie model
                modelPie = new PieChartModel();

                int inTime, late, before, doing, todo;
                inTime = late = before = doing = todo = 0;
                for (Tarea t : sprintActual.getTareaList()) {

                    if (t.getDone() == '2') {
                        if (t.getDuracionEstimado() == t.getTotalHoras()) {
                            inTime++;
                        } else if (t.getDuracionEstimado().compareTo(t.getTotalHoras()) == -1) {
                            late++;
                        } else {
                            before++;
                        }
                    } else if (t.getDone() == '1') {
                        doing++;
                    } else {
                        todo++;
                    }
                }

                modelPie.set("ToDO", todo);
                modelPie.set("Doing", doing);
                modelPie.set("in Time", inTime);
                modelPie.set("late", late);
                modelPie.set("before", before);

                createPieImage(todo, doing, inTime, late, before);

                //Inicializa burnUp model
                modelBurnUp = new CartesianChartModel();
                BigInteger totalHoras = BigInteger.ZERO;
                BigInteger horasSprint = BigInteger.ZERO;

                //gráfico ideal
                ideal = new ChartSeries();
                int sprintN = 0;
                ideal.setLabel("Ideal");
                ideal.set(sprintN, 0);
                for (Sprint sprint : sprintList) {
                    sprintN++;
                    for (Tarea t : sprint.getTareaList()) {
                        totalHoras = totalHoras.add(t.getDuracionEstimado());
                    }
                    ideal.set(sprintN, totalHoras);
                }

                modelBurnUp.addSeries(ideal);


                //gráfico real
                totalHoras = BigInteger.ZERO;
                horasSprint = BigInteger.ZERO;
                sprintN = 0;
                real = new ChartSeries();
                real.setLabel("Real");
                real.set(0, 0);
                for (Sprint sprint : sprintList) {
                    sprintN++;
                    for (Tarea t : sprint.getTareaList()) {
                        if (t.getDone() == '2') {
                            totalHoras = totalHoras.add(t.getDuracionEstimado());
                        }
                    }

                    real.set(sprintN, totalHoras);
                }
                modelBurnUp.addSeries(real);
                createBurnupImage(ideal, real);
            }
        }
    }

    public void initEJB() {
        try {
            InitialContext initialContext = new InitialContext();
            java.lang.Object ejbHome = initialContext.lookup("java:global/ProyectoScrum/ProyectoScrum-ejb/UsuarioFacade");
            this.usuarioFacade = (UsuarioFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, UsuarioFacade.class);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public CartesianChartModel getModelBurndown() {
        return modelBurndown;
    }

    public void setModelBurndown(CartesianChartModel model) {
        this.modelBurndown = model;
    }

    public CartesianChartModel getModelBurnUp() {
        return modelBurnUp;
    }

    public void setModelBurnUp(CartesianChartModel modelBurnUp) {
        this.modelBurnUp = modelBurnUp;
    }

    public PieChartModel getModelPie() {
        return modelPie;
    }

    public void setModelPie(PieChartModel modelPie) {
        this.modelPie = modelPie;
    }

    public boolean isBurndown() {
        return burndown;
    }

    public void setBurndown(boolean burndown) {
        this.burndown = burndown;
    }

    public boolean isBurnup() {
        return burnup;
    }

    public void setBurnup(boolean burnup) {
        this.burnup = burnup;
    }

    public boolean isPie() {
        return pie;
    }

    public void setPie(boolean pie) {
        this.pie = pie;
    }

    public Sprint getSprintActual() {
        return sprintActual;
    }

    public void setSprintActual(Sprint sprintActual) {
        this.sprintActual = sprintActual;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public BigInteger getHorasEstimadas() {
        return horasEstimadas;
    }

    public void setHorasEstimadas(BigInteger horasEstimadas) {
        this.horasEstimadas = horasEstimadas;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public Date getForecast() {
        return forecast;
    }

    public void setForecast(Date forecast) {
        this.forecast = forecast;
    }

    public boolean isSprintGood() {
        return sprintGood;
    }

    public void setSprintGood(boolean sprintGood) {
        this.sprintGood = sprintGood;
    }

    public String getVelocidadFormatted() {
        return VelocidadFormatted;
    }

    public void setVelocidadFormatted(String VelocidadFormatted) {
        this.VelocidadFormatted = VelocidadFormatted;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void redirigir(int estado) {
        if (estado == 1) {
            burndown = true;
            burnup = pie = false;
        } else if (estado == 2) {
            burnup = true;
            burndown = pie = false;
        } else if (estado == 3) {
            pie = true;
            burnup = burndown = false;
        }
    }

    public UsuarioController getUc() {
        return uc;
    }

    public void setUc(UsuarioController uc) {
        this.uc = uc;
    }

    public byte[] getByteBurndownImage() {
        return byteBurndownImage;
    }

    public void setByteBurndownImage(byte[] byteBurndownImage) {
        this.byteBurndownImage = byteBurndownImage;
    }

    public byte[] getByteBurnupImage() {
        return byteBurnupImage;
    }

    public void setByteBurnupImage(byte[] byteBurnupImage) {
        this.byteBurnupImage = byteBurnupImage;
    }

    public byte[] getBytePieImage() {
        return bytePieImage;
    }

    public void setBytePieImage(byte[] bytePieImage) {
        this.bytePieImage = bytePieImage;
    }

    public double getCosteReunion() {
        return costeReunion;
    }

    public void setCosteReunion(double costeReunion) {
        this.costeReunion = costeReunion;
    }

    private void createBurndownImage(ChartSeries ideal, ChartSeries real) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Ideal");
        XYSeries series2 = new XYSeries("Real");

        for (Map.Entry<Object, Number> entry : ideal.getData().entrySet()) {
            series1.add((Number) entry.getValue(), Long.parseLong(entry.getKey().toString()));
//            log.log(Level.INFO, "{0}:{1}", new Object[]{entry.getValue(), Long.parseLong(entry.getKey().toString())});
        }


        for (Map.Entry<Object, Number> entry : real.getData().entrySet()) {
            series2.add((Number) entry.getValue(), Long.parseLong(entry.getKey().toString()));
//            log.log(Level.INFO, "{0}:{1}", new Object[]{entry.getValue(), Long.parseLong(entry.getKey().toString())});
        }

        dataset.addSeries(series1);
        dataset.addSeries(series2);

        JFreeChart chart = ChartFactory.createXYLineChart("Burndown del sprint actual", "puntos", "tiempo", dataset, PlotOrientation.HORIZONTAL, true, false, false);
        try {
            File chartFile = new File("burndown.png");
            ChartUtilities.saveChartAsPNG(chartFile, chart, 375, 300);
            InputStream is = new FileInputStream(chartFile);
            this.byteBurndownImage = IOUtils.toByteArray(is);
            chartFile.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createPieImage(int todo, int doing, int inTime, int late, int before) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("todo", todo);
        dataset.setValue("doing", doing);
        dataset.setValue("in Time", inTime);
        dataset.setValue("late", late);
        dataset.setValue("before", before);

        JFreeChart chart = ChartFactory.createPieChart3D("Tareas", dataset, true, false, false);
        try {
            File chartFile = new File("pie.png");
            ChartUtilities.saveChartAsPNG(chartFile, chart, 375, 300);
            InputStream is = new FileInputStream(chartFile);
            this.bytePieImage = IOUtils.toByteArray(is);
            chartFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void createBurnupImage(ChartSeries ideal, ChartSeries real) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series1 = new XYSeries("Ideal");
        XYSeries series2 = new XYSeries("Real");

        for (Map.Entry<Object, Number> entry : ideal.getData().entrySet()) {
            series1.add((Number) entry.getValue(), Long.parseLong(entry.getKey().toString()));
//            log.log(Level.INFO, "{0}:{1}", new Object[]{entry.getValue(), Long.parseLong(entry.getKey().toString())});
        }


        for (Map.Entry<Object, Number> entry : real.getData().entrySet()) {
            series2.add((Number) entry.getValue(), Long.parseLong(entry.getKey().toString()));
//            log.log(Level.INFO, "{0}:{1}", new Object[]{entry.getValue(), Long.parseLong(entry.getKey().toString())});
        }

        dataset.addSeries(series1);
        dataset.addSeries(series2);

        JFreeChart chart = ChartFactory.createXYLineChart("Burnup del proyecto", "puntos", "tiempo", dataset, PlotOrientation.HORIZONTAL, true, false, false);
        try {
            File chartFile = new File("burnup.png");
            ChartUtilities.saveChartAsPNG(chartFile, chart, 375, 300);
            InputStream is = new FileInputStream(chartFile);
            this.byteBurnupImage = IOUtils.toByteArray(is);
            chartFile.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
