/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.servlet;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException; //agregado para la fecha
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pasosServer.ejb.ContactoFacadeRemote;
import pasosServer.ejb.MaltratadorFacadeRemote;
import pasosServer.ejb.ProtegidoFacadeRemote;
import pasosServer.model.Contacto;
import pasosServer.model.Maltratador;
import pasosServer.model.Protegido;

/**
 *
 * @author Gonzalo
 */
@WebServlet(name = "CreateUserServlet", urlPatterns = {"/CreateUserServlet"})
public class CreateUserServlet extends HttpServlet {

    @EJB
    private ContactoFacadeRemote contactoFacade;
    @EJB
    private MaltratadorFacadeRemote maltratadorFacade;
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
            throws ServletException, IOException, ParseException {

        String tipo = request.getParameter("tipo");
        if (tipo.equals("1")) { // GUARDAR PROTEGIDO
            //Protegido
            String nombreP = request.getParameter("nombreP");
            String apellidosP = request.getParameter("apellidosP");
            String fecha = request.getParameter("fechanacP");
            System.out.println("OBTENIDO: " + nombreP + " " + apellidosP + " - " + fecha);
            SimpleDateFormat fecha2 = new SimpleDateFormat("dd-MM-yyyy");
            Date fechanacP = fecha2.parse(fecha);

            String aux = request.getParameter("telefonoP");
            Integer aux1 = Integer.parseInt(aux);
            BigInteger telefonoP = BigInteger.valueOf(aux1);
            String imeiP = request.getParameter("imeiP");
            // Creación del objeto protegido                
            Protegido protegido = new Protegido();
            protegido.setNombre(nombreP);
            protegido.setApellidos(apellidosP);
            protegido.setFechaNacimiento(fechanacP);
            protegido.setTelefonoMovil(telefonoP);
            protegido.setImei(imeiP);
            System.out.println("Se va a guardar el protegido: " + protegido.getNombre() + " - " + protegido.getApellidos());
            this.protegidoFacade.create(protegido);

        } else if (tipo.equals("2")) { // GUARDAR MALTRATADOR

            //Maltratador
            String nombreA = request.getParameter("nombreA");
            String apellidosA = request.getParameter("apellidosA");
            String aux6 = request.getParameter("dispositivoA");
            Integer aux7 = Integer.parseInt(aux6);
            BigInteger dispositivoA = BigInteger.valueOf(aux7);
            String aux8 = request.getParameter("distanciaA");
            Integer aux9 = Integer.parseInt(aux8);
            BigInteger distanciaA = BigInteger.valueOf(aux9);
            String imeiA = request.getParameter("imeiA");
            
            System.out.println("OBTENIDO: " + nombreA + " " + apellidosA);


            Maltratador maltratador = new Maltratador();
            maltratador.setNombre(nombreA);
            maltratador.setApellidos(apellidosA);
            maltratador.setDispositivo(dispositivoA);
            maltratador.setDistanciaAlejamiento(distanciaA);
            maltratador.setImei(imeiA);
            
            Protegido p = this.protegidoFacade.findProtegidoByNombreAndApellidos("pepe", "perez");
            this.maltratadorFacade.createMaltratador(maltratador, p.getIdProtegido());
            /*maltratador.setIdProtegido(p);
            System.out.println("Se va a guardar el protegido: " + maltratador.getNombre() + " - " + maltratador.getApellidos());
            this.maltratadorFacade.create(maltratador);*/

        }

        //Contacto 1
                /*String nombreC1 = request.getParameter("nombreC1");
        String aux2 = request.getParameter("movilC1");
        Integer aux3 = Integer.parseInt(aux2);
        BigInteger movilC1 = BigInteger.valueOf(aux3);
        String emailC1 = request.getParameter("emailC1");
        
        //Contacto 2
        String nombreC2 = request.getParameter("nombreC2");
        String aux4 = request.getParameter("movilC2");
        Integer aux5 = Integer.parseInt(aux4);
        BigInteger movilC2 = BigInteger.valueOf(aux5);
        String emailC2 = request.getParameter("emailC2");*/







        /*Maltratador maltratador = new Maltratador();
        maltratador.setNombre(nombreA);
        maltratador.setApellidos(apellidosA);
        maltratador.setDispositivo(dispositivoA);
        maltratador.setDistanciaAlejamiento(distanciaA);
        maltratador.setFoto(imagenA); //imagen*/

        //this.maltratadorFacade.create(maltratador);
        //maltratador.setIdProtegido(protegido);
        //this.maltratadorFacade.create(maltratador);
        //protegido.addMaltratador(maltratador);
        //this.protegidoFacade.create(protegido);


        /*maltratador.setIdProtegido(protegido);
        this.maltratadorFacade.create(maltratador);*
        
        
        
        // Creación de los objetos contactos
        /*Contacto contacto1 = new Contacto();
        contacto1.setNombre(nombreC1);
        contacto1.setTelefonoContacto(movilC1);
        contacto1.setEmail(emailC1);
        
        Contacto contacto2 = new Contacto();
        contacto2.setNombre(nombreC2);
        contacto2.setTelefonoContacto(movilC2);
        contacto2.setEmail(emailC2);*/

        //protegido.setContactoCollection(null);

        // Guardado de protegido y contactos en la base de datos

        //this.contactoFacade.create(contacto1);
        //this.contactoFacade.create(contacto2);


        // Guardado del maltratado (si se ha introducido)
                /*if(!nombreA.isEmpty() && !apellidosA.isEmpty() && dispositivoA != null && distanciaA != null){
        Maltratador maltratador = new Maltratador();
        maltratador.setNombre(nombreA);
        maltratador.setApellidos(apellidosA);
        maltratador.setDispositivo(dispositivoA);
        maltratador.setDistanciaAlejamiento(distanciaA);
        maltratador.setFoto(imagenA); //imagen
        //maltratador.setIdProtegido(protegido);
        
        this.maltratadorFacade.create(maltratador);
        }*/

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/respuestaCreateUser.jsp");
        dispatcher.forward(request, response);

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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(CreateUserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(CreateUserServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
