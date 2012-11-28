/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
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
@WebServlet(name = "EstadisticasServlet", urlPatterns = {"/EstadisticasServlet"})
public class EstadisticasServlet extends HttpServlet {
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
//        List list;
//        if(anio.equals("todos")){
//            list = this.alarmaFacade.findAlarmasGroupByMonth();
//            
//        }
//        else{
//            list = this.alarmaFacade.findAlarmasGroupByMonth(anio);
//            
//        }
//        JFreeChart chart = this.crearGraficoAlarmasPorMes(list);
           //sesion.setAttribute("grafico", chart);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/grafico.jsp?anio=" + anio);
        dispatcher.forward(request, response);
                //response.setContentType("image/jpeg");
        //OutputStream salida = response.getOutputStream();
        //List list = this.alarmaFacade.findAlarmasGroupByMonth();
        /*Object[] o1=new Object[2];
        Object[] o2=new Object[2];
        Object[] o3=new Object[2];
        Object[] o4=new Object[2];
        o1[0]=3;
        o2[0]=1;
        o3[0]=1;
        o4[0]=1;
        o1[1]=1;
        o2[1]=3;
        o3[1]=4;
        o4[1]=5;
        List<Object[]> list=new ArrayList<Object[]>();
        list.add(o1);
        list.add(o2);
        list.add(o3);
        list.add(o4);*/
        
        //JFreeChart chart = this.crearGraficoAlarmasPorMes(list);
        //ChartUtilities.writeChartAsJPEG(salida,chart,500,500);
        //salida.close();    
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
