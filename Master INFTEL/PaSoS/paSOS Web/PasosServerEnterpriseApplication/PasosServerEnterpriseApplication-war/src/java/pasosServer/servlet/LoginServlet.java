/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pasosServer.ejb.OperadorFacade;
import pasosServer.ejb.OperadorFacadeRemote;
import pasosServer.model.Operador;

/**
 *
 * @author albertomateos
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    @EJB
    private OperadorFacadeRemote operadorFacade;

    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("username");
        String password = request.getParameter("password");        
        System.out.println("LOGIN: "+user+" - "+password+" - "+request.getParameter("action"));
       
        
        try{
            //hacer comprobación en la base de datos
            Operador operador = this.operadorFacade.findByLoginAndPassword(user,password);
            BigDecimal idOperador = operador.getIdOperador();
            request.getSession(true).setAttribute("id", idOperador);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/comet");
            dispatcher.forward(request,response);
        }catch(javax.ejb.EJBException e){
            //se hace forward al jsp incluyendo reescritura de URL
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/loginError.jsp"));  
        }catch(javax.persistence.NoResultException e){
            //se hace forward al jsp incluyendo reescritura de URL
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/loginError.jsp"));  
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
