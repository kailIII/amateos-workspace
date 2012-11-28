/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.jsf;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import org.apache.poi.util.IOUtils;
import org.inftel.scrum.ejb.ProyectoFacade;
import org.inftel.scrum.ejb.UsuarioFacade;
import org.inftel.scrum.model.Proyecto;
import org.inftel.scrum.model.Tarea;
import org.inftel.scrum.model.Usuario;

/**
 *
 * @author empollica
 */
@ManagedBean
@RequestScoped
public class InformesBean implements Serializable {

    @EJB
    private ProyectoFacade proyectoFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    private final static Logger LOGGER = Logger.getLogger(InformesBean.class.getName());
    private Usuario usuario;
    private Proyecto proyecto;
    private List<Usuario> usuarios;
    private JasperPrint jasperPrint;
    private Boolean rendered = false;
    private UsuarioController usuarioController;

    public InformesBean() {
    }

    @PostConstruct
    public void init() {
        usuarioController = (UsuarioController) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioController");
        usuario = usuarioFacade.findByMail(usuarioController.getEmail());
        if (usuario.getRol() != null && usuario.getRol().equalsIgnoreCase("scrum master")) {
            rendered = true;
            try {
                proyecto = usuario.getGrupoId().getProyectoId();
            } catch (Exception e) {
            }
            usuarios = usuarioFacade.findByProyecto(proyecto);
        }
    }

    public UsuarioController getUsuarioController() {
        return usuarioController;
    }

    public void setUsuarioController(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }

    public Boolean getRendered() {
        return rendered;
    }

    public void setRendered(Boolean rendered) {
        this.rendered = rendered;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public void obtenerProyecto() {
        usuario = usuarioFacade.findByMail(usuarioController.getEmail());
        if (usuario.getRol() != null && usuario.getRol().equalsIgnoreCase("scrum master")) {
            rendered = true;
            try {
                proyecto = usuario.getGrupoId().getProyectoId();
            } catch (Exception e) {
            }
            usuarios = usuarioFacade.findByProyecto(proyecto);
        }
    }
    InitialContext initialContext;

    public void initEJB() {
        try {
            initialContext = new InitialContext();
            java.lang.Object ejbHome = initialContext.lookup("java:global/ProyectoScrum/ProyectoScrum-ejb/UsuarioFacade");
            this.usuarioFacade = (UsuarioFacade) javax.rmi.PortableRemoteObject.narrow(ejbHome, UsuarioFacade.class);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public byte[] informeUsuarios(int formato) {
        byte[] documento = null;
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(usuarios);
        String reportPath = "";
        try {
            reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/jasper/usuarios.jasper");
            Map parameters = new HashMap();
            parameters.put("proyecto", proyecto.getNombre());
            jasperPrint = JasperFillManager.fillReport(reportPath, parameters, dataSource);
        } catch (Exception ex) {
            try {
                initEJB();
                obtenerProyecto();
                Map parameters = new HashMap();
                parameters.put("proyecto", proyecto.getNombre());
                jasperPrint = JasperFillManager.fillReport("/Users/inftel/usuarios.jasper", parameters, dataSource);
            } catch (JRException ex1) {
                LOGGER.info("EXCEPCION FILL MANAGER -> " + ex.getMessage());
            }
        }
        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        } catch (Exception e) {
        }
        // Exportar según formato
        switch (formato) {
            case 0: // PDF
                try {
                    try {
                        try {
                            JasperExportManager.exportReportToPdfStream(jasperPrint, httpServletResponse.getOutputStream());
                            JasperExportManager.exportReportToPdfFile(jasperPrint, "InformeUsuario1.pdf");
                        } catch (Exception e) {
                        }
                        JasperExportManager.exportReportToPdfFile(jasperPrint, "/Users/inftel/InformeUsuario.pdf");
                        File file = new File("/Users/inftel/InformeUsuario.pdf");
                        documento = IOUtils.toByteArray(new FileInputStream(file));
                        file.delete();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JRE -> " + ex.getMessage());
                    }
                } catch (Exception ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;
            case 1: //EXCELL
                httpServletResponse.addHeader("Content-disposition", "attachment;filename=empleados.xlsx");
                try {

                    JRXlsExporter exporter = new JRXlsExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());
                    try {
                        exporter.exportReport();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JR -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;

            case 2: //POWERPOINT
                httpServletResponse.addHeader("Content-disposition", "attachment;filename=empleados.pptx");
                try {

                    JRPptxExporter exporter = new JRPptxExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());
                    try {
                        exporter.exportReport();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JR -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;
        }
        try {
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        return documento;
    }

    public byte[] informeProyecto(int formato) {
        byte[] documento = null;
        List<Proyecto> listaProyecto = new ArrayList<Proyecto>();
        listaProyecto.add(proyecto);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listaProyecto);
        String reportPath = "";
        try {
            reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/jasper/proyecto.jasper");
            int totales = proyecto.getTareaList().size();
            int realizadas = calcularTareasRealizadasProyecto();
            double progreso = (double) (realizadas * 100 / totales);
            LOGGER.info("TOTALES -> " + totales);
            LOGGER.info("REALIZADAS -> " + realizadas);
            LOGGER.info("PROGRESO -> " + progreso);
            Map parameters = new HashMap();
            parameters.put("tareasTotales", totales);
            parameters.put("tareasRealizadas", realizadas);
            parameters.put("progreso", progreso);
            parameters.put("sprints", proyecto.getSprintList().size());
            jasperPrint = JasperFillManager.fillReport(reportPath, parameters, dataSource);
        } catch (Exception ex) {
            try {
                initEJB();
                obtenerProyecto();
                int totales = proyecto.getTareaList().size();
                int realizadas = calcularTareasRealizadasProyecto();
                double progreso = (double) (realizadas * 100 / totales);
                Map parameters = new HashMap();
                parameters.put("tareasTotales", totales);
                parameters.put("tareasRealizadas", realizadas);
                parameters.put("progreso", progreso);
                parameters.put("sprints", proyecto.getSprintList().size());
                parameters.put("proyecto", proyecto.getNombre());
                jasperPrint = JasperFillManager.fillReport("/Users/inftel/proyecto.jasper", parameters, dataSource);
            } catch (JRException ex1) {
                LOGGER.info("EXCEPCION FILL MANAGER -> " + ex.getMessage());
            }
        }
        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        } catch (Exception e) {
        }
        // Exportar según formato
        switch (formato) {
            case 0: // PDF
                try {
                    try {
                        try {
                            JasperExportManager.exportReportToPdfStream(jasperPrint, httpServletResponse.getOutputStream());
                            JasperExportManager.exportReportToPdfFile(jasperPrint, "InformeProyecto1.pdf");
                        } catch (Exception e) {
                        }
                        System.out.println(System.getProperty("user.dir"));
                        JasperExportManager.exportReportToPdfFile(jasperPrint, "/Users/inftel/InformeProyecto.pdf");
//                        System.out.println(System.getProperty("user.dir"));
                        File file = new File("/Users/inftel/InformeProyecto.pdf");
                        documento = IOUtils.toByteArray(new FileInputStream(file));
                        file.delete();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JRE -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;
            case 1: //EXCELL
                httpServletResponse.addHeader("Content-disposition", "attachment;filename=proyecto.xlsx");
                try {

                    JRXlsExporter exporter = new JRXlsExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());
                    try {
                        exporter.exportReport();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JR -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;

            case 2: //POWERPOINT
                httpServletResponse.addHeader("Content-disposition", "attachment;filename=proyecto.pptx");
                try {

                    JRPptxExporter exporter = new JRPptxExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());
                    try {
                        exporter.exportReport();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JR -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;
        }
        try {
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        return documento;
    }

    public byte[] informeBacklog(int formato) {
        byte[] documento = null;

        String reportPath = "";
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(proyecto.getTareaList());
            reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/jasper/backlog.jasper");
            Map parameters = new HashMap();
            parameters.put("proyecto", proyecto.getNombre());
            jasperPrint = JasperFillManager.fillReport(reportPath, parameters, dataSource);
        } catch (Exception ex) {
            try {
                initEJB();
                obtenerProyecto();
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(proyecto.getTareaList());
                Map parameters = new HashMap();
                parameters.put("proyecto", proyecto.getNombre());
                jasperPrint = JasperFillManager.fillReport("/Users/inftel/backlog.jasper", parameters, dataSource);
            } catch (JRException ex1) {
                LOGGER.info("EXCEPCION FILL MANAGER -> " + ex.getMessage());
            }
        }
        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        } catch (Exception e) {
        }
        // Exportar según formato
        switch (formato) {
            case 0: // PDF
                try {
                    try {
                        try {
                            JasperExportManager.exportReportToPdfStream(jasperPrint, httpServletResponse.getOutputStream());
                            JasperExportManager.exportReportToPdfFile(jasperPrint, "InformeBackLog1.pdf");
                        } catch (Exception e) {
                        }
                        JasperExportManager.exportReportToPdfFile(jasperPrint, "/Users/inftel/InformeBackLog.pdf");
                        File file = new File("/Users/inftel/InformeBacklog.pdf");
                        file.setReadOnly();
                        documento = IOUtils.toByteArray(new FileInputStream(file));
                        file.delete();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JRE -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;
            case 1: //EXCELL
                httpServletResponse.addHeader("Content-disposition", "attachment;filename=backlog.xlsx");
                try {

                    JRXlsExporter exporter = new JRXlsExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());
                    try {
                        exporter.exportReport();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JR -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;

            case 2: //POWERPOINT
                httpServletResponse.addHeader("Content-disposition", "attachment;filename=backlog.pptx");
                try {

                    JRPptxExporter exporter = new JRPptxExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());
                    try {
                        exporter.exportReport();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JR -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;
        }
        try {
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        return documento;
    }

    public byte[] informeSprints(int formato) {
        byte[] documento = null;

        String reportPath = "";
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(proyecto.getSprintList());
            reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/jasper/sprints.jasper");
            Map parameters = new HashMap();
            parameters.put("proyecto", proyecto.getNombre());
            jasperPrint = JasperFillManager.fillReport(reportPath, parameters, dataSource);
        } catch (Exception ex) {
            try {
                initEJB();
                obtenerProyecto();
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(proyecto.getSprintList());
                Map parameters = new HashMap();
                parameters.put("proyecto", proyecto.getNombre());
                jasperPrint = JasperFillManager.fillReport("/Users/inftel/sprints.jasper", parameters, dataSource);
            } catch (JRException ex1) {
                LOGGER.info("EXCEPCION FILL MANAGER -> " + ex.getMessage());
            }
        }
        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        } catch (Exception e) {
        }
        // Exportar según formato
        switch (formato) {
            case 0: // PDF
                try {
                    try {
                        try {
                            JasperExportManager.exportReportToPdfStream(jasperPrint, httpServletResponse.getOutputStream());
                            JasperExportManager.exportReportToPdfFile(jasperPrint, "Sprints1.pdf");
                        } catch (Exception e) {
                        }
                        JasperExportManager.exportReportToPdfFile(jasperPrint, "/Users/inftel/Sprints.pdf");
                        File file = new File("/Users/inftel/Sprints.pdf");
                        documento = IOUtils.toByteArray(new FileInputStream(file));
                        file.delete();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JRE -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;
            case 1: //EXCELL
                httpServletResponse.addHeader("Content-disposition", "attachment;filename=sprints.xlsx");
                try {

                    JRXlsExporter exporter = new JRXlsExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());
                    try {
                        exporter.exportReport();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JR -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;

            case 2: //POWERPOINT
                httpServletResponse.addHeader("Content-disposition", "attachment;filename=sprints.pptx");
                try {

                    JRPptxExporter exporter = new JRPptxExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());
                    try {
                        exporter.exportReport();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JR -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;
        }
        try {
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        return documento;
    }

    public byte[] informeLogs(int formato) {
        byte[] documento = null;

        String reportPath = "";
        try {
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(proyecto.getLogList());
            reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/jasper/logs.jasper");
            Map parameters = new HashMap();
            parameters.put("proyecto", proyecto.getNombre());
            jasperPrint = JasperFillManager.fillReport(reportPath, parameters, dataSource);
        } catch (Exception ex) {
            try {
                initEJB();
                obtenerProyecto();
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(proyecto.getLogList());
                Map parameters = new HashMap();
                parameters.put("proyecto", proyecto.getNombre());
                jasperPrint = JasperFillManager.fillReport("/Users/inftel/logs.jasper", parameters, dataSource);
            } catch (JRException ex1) {
                LOGGER.info("EXCEPCION FILL MANAGER -> " + ex.getMessage());
            }
        }
        HttpServletResponse httpServletResponse = null;
        try {
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        } catch (Exception e) {
        }
        // Exportar según formato
        switch (formato) {
            case 0: // PDF
                try {
                    try {
                        try {
                            JasperExportManager.exportReportToPdfStream(jasperPrint, httpServletResponse.getOutputStream());
                            JasperExportManager.exportReportToPdfFile(jasperPrint, "Logs1.pdf");
                        } catch (Exception e) {
                        }

                        JasperExportManager.exportReportToPdfFile(jasperPrint, "/Users/inftel/Logs.pdf");
                        File file = new File("/Users/inftel/Logs.pdf");
                        documento = IOUtils.toByteArray(new FileInputStream(file));
                        file.delete();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JRE -> " + ex.getStackTrace());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;
            case 1: //EXCELL
                httpServletResponse.addHeader("Content-disposition", "attachment;filename=logs.xlsx");
                try {

                    JRXlsExporter exporter = new JRXlsExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());
                    try {
                        exporter.exportReport();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JR -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break;

            case 2: //POWERPOINT
                httpServletResponse.addHeader("Content-disposition", "attachment;filename=logs.pptx");
                try {

                    JRPptxExporter exporter = new JRPptxExporter();
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, httpServletResponse.getOutputStream());
                    try {
                        exporter.exportReport();
                    } catch (JRException ex) {
                        LOGGER.info("EXCEPCION JR -> " + ex.getMessage());
                    }
                } catch (IOException ex) {
                    LOGGER.info("EXCEPCION IO -> " + ex.getMessage());
                }

                break; 
                
        }
        try {
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
        }
        return documento;
    }

    private int calcularTareasRealizadasProyecto() {
        List<Tarea> tareas = proyecto.getTareaList();
        int realizadas = 0;
        char finalizada = '2';
        for (Tarea t : tareas) {
            if (t.getDone() == finalizada) {
                realizadas++;
            }
        }
        return realizadas;
    }
}
