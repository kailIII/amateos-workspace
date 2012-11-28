/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.inftel.pasos.vos.ContactoEnvio;
import pasosServer.ejb.ContactoFacadeRemote;
import pasosServer.ejb.ProtegidoFacadeRemote;
import pasosServer.model.Contacto;
import pasosServer.model.Protegido;
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author juan_antonio
 */
@WebServlet(name = "ContactosServlet", urlPatterns = {"/ContactosServlet"})
public class ContactosServlet extends HttpServlet {
    @EJB
    private ProtegidoFacadeRemote protegidoFacade;
    

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml");
        String imei = request.getParameter("imei");
        
        XStream xstream = new XStream();
        xstream.alias("contacto", ContactoEnvio.class);
        String xml="";
        
        ArrayList<ContactoEnvio> lcontactosEnvio = new ArrayList<ContactoEnvio>();
        for(int i=0; i<4; i++){
            ContactoEnvio ce = new ContactoEnvio();
            ce.setIdContacto(BigDecimal.valueOf(i));
            ce.setNombre("contacto "+i);
            String telefono="99999999"+i;          
            ce.setTelefonoContacto(BigInteger.valueOf(Long.parseLong(telefono)));
            ce.setEmail("contacto"+i+"@gmail.com");
            xml+=xstream.toXML(ce);
        }
        System.out.println(xml);
        PrintWriter out = response.getWriter();
        out.write(xml);
        
        /*Protegido protegido= this.protegidoFacade.findByimei(imei);
        ArrayList<Contacto> lcontactos= (ArrayList<Contacto>)protegido.getContactoCollection();
        ArrayList<ContactoEnvio> lcontactosEnvio = new ArrayList<ContactoEnvio>();
        for(Contacto c: lcontactos){
            ContactoEnvio contactoEnvio= new ContactoEnvio();
            contactoEnvio.setIdContacto(c.getIdContacto());
            contactoEnvio.setNombre(c.getNombre());
            contactoEnvio.setTelefonoContacto(c.getTelefonoContacto());
            contactoEnvio.setEmail(c.getEmail());
            xml+=xstream.toXML(contactoEnvio);
        }
        PrintWriter out = response.getWriter();
        out.write(xml);*/
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
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
     * Handles the HTTP
     * <code>POST</code> method.
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
