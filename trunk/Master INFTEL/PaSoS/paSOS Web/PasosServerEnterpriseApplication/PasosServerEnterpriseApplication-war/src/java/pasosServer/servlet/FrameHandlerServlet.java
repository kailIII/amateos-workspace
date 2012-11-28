/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.servlet;

import com.frame.JavaBeans.Frame;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pasosServer.ejb.AlarmaFacadeRemote;
import pasosServer.ejb.MaltratadorFacadeRemote;
import pasosServer.ejb.ProtegidoFacadeRemote;
import pasosServer.model.Alarma;
import pasosServer.model.Maltratador;
import pasosServer.model.Protegido;

/**
 *
 * @author Jesus Ruiz Oliva
 */
//@WebServlet(name = "FrameHandlerServlet", urlPatterns = {"/FrameHandlerServlet"})
public class FrameHandlerServlet extends HttpServlet {
    @EJB
    private AlarmaFacadeRemote alarmaFacade;
    @EJB
    private MaltratadorFacadeRemote maltratadorFacade;
    @EJB
    private ProtegidoFacadeRemote protegidoFacade;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        
       System.out.println("ENTRA SERVLET FRAME");
        Frame frame = new Frame();
        RequestDispatcher requestDispatcher; 
        String trama = request.getParameter("trama");
        System.out.println("OBTENIDA TRAMA "+trama);
        
        if (trama!=null && !trama.isEmpty()){
            String typeFrame = trama.substring(1,3);
            if (typeFrame.equals("ZN") || typeFrame.equals("TE")){
                frame.setType(typeFrame);
                trama = trama.substring(4);
            } else if (typeFrame.startsWith("AU")){
                frame.setType(trama.substring(1,5));
                trama = trama.substring(6);
            }            
            System.out.println("Trama para trocear: "+trama);
            String[] trozos =trama.split("&");
            
         
            System.out.println(trozos.toString()+"Tamaño: "+trozos.length);
            
            for (int i=0; i<trozos.length; i++){
                String trozo=trozos[i];
                System.out.println("TROZO "+i+": "+trozo);
               if(trozo != null && !trozo.isEmpty()){
                   if (trozo.substring(0, 2).equals("LD")){
                       frame.setLD(trozo.substring(2));
                   }
                   else if (trozo.substring(0, 2).equals("LH")){
                       frame.setLH(trozo.substring(2));
                   }
                   else if (trozo.substring(0, 2).equals("LN")){
                       frame.setLN(converterLongitud(trozo.substring(2)));                       
                   }
                   else if (trozo.substring(0, 2).equals("LT")){
                       frame.setLT(converterLatitud(trozo.substring(2)));
                   }
                   else if (trozo.substring(0, 2).equals("RD")){
                       frame.setRD(trozo.substring(2));
                   }
               }
            }    
            System.out.println("TIPO TRAMA: "+frame.getType());
            if (frame.getType().equals("TE")){
                //Almacenar en BD                
                Protegido protegido = protegidoFacade.findByimei(frame.getRD());
                protegido.setLatitud(new BigInteger(frame.getLT()));                
                protegido.setLongitud(new BigInteger(frame.getLN()));
                protegidoFacade.updateProtegido(protegido);
            }
            if (frame.getType().equals("ZN") || frame.getType().startsWith("AU")){               
                
                request.setAttribute("trama",frame);
                System.out.println("Se va a enviar trama: "+frame);
                requestDispatcher = getServletContext().getRequestDispatcher("/comet");
                requestDispatcher.forward(request, response);
            }
        }      
        
    }
    public String converterLatitud(String coordenada){
        System.out.println("coordenda: " + coordenada);
        int signo = Integer.parseInt(coordenada.substring(0,1));
        float hh = Integer.parseInt(coordenada.substring(1,3));
        float dd = Integer.parseInt(coordenada.substring(3,5));
        float nnnn = Integer.parseInt(coordenada.substring(5));
        float x = hh + dd/60 + nnnn/600000;        
        if (signo ==2){
            x= -x;
        }
        System.out.println(Float.toString(x));
        return Float.toString(x);
    }
    
    public String converterLongitud(String coordenada){
        System.out.println("coordenda: " + coordenada);
        int signo = Integer.parseInt(coordenada.substring(0,1));
        float hh = Integer.parseInt(coordenada.substring(1,4));
        float dd = Integer.parseInt(coordenada.substring(4,6));
        float nnnn = Integer.parseInt(coordenada.substring(6));
        float x = hh + dd/60 + nnnn/600000;        
        if (signo ==2){
            x= -x;
        }
        System.out.println(Float.toString(x));
        return Float.toString(x);
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
