/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pasosServer.ejb.AlarmaFacadeRemote;
import pasosServer.ejb.ProtegidoFacadeRemote;
import pasosServer.jspbeans.ProtegidoInfoBean;
import pasosServer.model.Protegido;

/**
 *
 * @author Juan Antonio
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/SearchServlet"})
public class SearchServlet extends HttpServlet {
    @EJB
    private AlarmaFacadeRemote alarmaFacade;
    @EJB
    private ProtegidoFacadeRemote protegidoFacade;

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            String nombre=request.getParameter("nombre");
            String apellidos=request.getParameter("apellidos");
            
            /*List list=this.alarmaFacade.findAlarmasGroupByMonth();
            for(Object o:list){
                Object[] tupla=(Object[])o;
                BigDecimal cont=(BigDecimal)tupla[0];
                BigDecimal mes=(BigDecimal)tupla[1]; 
                System.out.println("Cont: "+cont+", Mes: "+mes);
            }*/
           
            Protegido protegido=this.protegidoFacade.findProtegidoByNombreAndApellidos(nombre, apellidos);
            
            if(protegido!=null){
                System.out.println(protegido.getNombre());
                ProtegidoInfoBean bean=new ProtegidoInfoBean();
                bean.setProtegido(protegido);
                //bean.setFoto(protegido.getImage());
                HttpSession sesion=request.getSession(true);
                sesion.setAttribute("foto", protegido.getFoto());
                //sesion.setAttribute("protegido", protegido);
                request.setAttribute("ProtegidoInfoBean", bean);
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/mostrarDatos.jsp");
                dispatcher.forward(request, response);
            }
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
