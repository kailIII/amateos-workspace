/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import pasosServer.ejb.AlarmaFacadeRemote;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "GraficoServlet", urlPatterns = {"/GraficoServlet"})
public class GraficoServlet extends HttpServlet {
    @EJB
    private AlarmaFacadeRemote alarmaFacade;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String anio=request.getParameter("anio");
        //utputStream salida = response.getOutputStream();
        List list;
        if(anio.equals("todos")){
            list = this.alarmaFacade.findAlarmasGroupByMonth();
            
        }
        else{
            list = this.alarmaFacade.findAlarmasGroupByMonth(anio);
            
        }
        JFreeChart chart = this.crearGraficoAlarmasPorMes(list);
        response.setContentType("image/jpeg");
        OutputStream salida = response.getOutputStream();
        ChartUtilities.writeChartAsJPEG(salida,chart,500,500);
        salida.close();
        //sesion.removeAttribute("foto");
    }
    
    private JFreeChart crearGraficoAlarmasPorMes(List list){
        String[] months = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul",
        "Ago", "Sep", "Oct", "Nov","Dic"};
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Object[] ltuplas=new Object[12];
        for(Object o:list){
                Object[] tupla=(Object[])o;
                BigDecimal mes=(BigDecimal)tupla[1]; 
                int mesindice=mes.intValue()-1;
                ltuplas[mesindice]=tupla;
        }
        for(int i=0; i<12; i++){
            Object[] tupla=(Object[])ltuplas[i];
            if(tupla!=null){
                BigDecimal cont=(BigDecimal)tupla[0];
                BigDecimal mes=(BigDecimal)tupla[1];
                //System.out.println("Cont:"+cont+", Mes:"+mes);
                dataset.setValue(cont.intValue(), "Alarmas", months[i]);
            }
            else{
                dataset.setValue(0, "Alarmas", months[i]);
            }
        }
        /*for(Object o:tuplas){
            Object[] tupla=(Object[])o;
            
            if(o!=null){
                BigDecimal cont=(BigDecimal)tupla[0];
                BigDecimal mes=(BigDecimal)tupla[1];
                dataset.setValue(cont.intValue(), "Alarmas", months[tuplas.indexOf(o)]);
            }
            else{
                dataset.setValue(0, "Alarmas", months[tuplas.indexOf(o)]);
            }
        }*/
        JFreeChart chart= ChartFactory.createBarChart("Alarmas recibidas por mes", "Meses", "Numero de alarmas", dataset, PlotOrientation.VERTICAL, false, true, false);
        return chart;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
