/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pasosServer.servlet;

import com.frame.JavaBeans.Frame;
import com.sun.grizzly.comet.CometContext;
import com.sun.grizzly.comet.CometEngine;
import com.sun.grizzly.comet.CometEvent;
import com.sun.grizzly.comet.CometHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pasosServer.ejb.AlarmaFacadeRemote;
import pasosServer.ejb.MaltratadorFacadeRemote;
import pasosServer.ejb.OperadorFacadeRemote;
import pasosServer.ejb.ProtegidoFacade;
import pasosServer.ejb.ProtegidoFacadeRemote;
import pasosServer.model.Alarma;
import pasosServer.model.Contacto;
import pasosServer.model.Maltratador;
import pasosServer.model.Operador;
import pasosServer.model.Protegido;

/**
 *
 * @author Jesus Ruiz Oliva
 */
public class CometServlet extends HttpServlet {
    
    @EJB
    private ProtegidoFacadeRemote protegidoFacade;
    @EJB
    private OperadorFacadeRemote operadorFacade;
    @EJB
    private MaltratadorFacadeRemote maltratadorFacade;
    @EJB
    private AlarmaFacadeRemote alarmaFacade;
    private final static String JUNK = "<!-- Comet is a programming technique that enables web " +
            "servers to send data to the client without having any need " +
            "for the client to request it. -->\n";
    private String contextPath;
    private ArrayList<ClientInfo> clientInfos = new ArrayList<ClientInfo>();
    static int firstServlet = -1;
    private boolean isPendingMessage = false;
    private ArrayList<Frame> listAlarmFrame = new ArrayList<Frame>();

    public CometServlet() {
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        contextPath = config.getServletContext().getContextPath() + "/comet";
        CometEngine cometEngine = CometEngine.getEngine();
        CometContext context = cometEngine.register(contextPath);
        context.setExpirationDelay(-1);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
                 
             
        String action = request.getParameter("action");
        Frame trama = (Frame)request.getAttribute("trama");
        CometEngine cometEngine = CometEngine.getEngine();
        CometContext cometContext = cometEngine.getCometContext(contextPath);
        System.out.println("COMET: "+action);
        if (action != null) {
            if ("login".equals(action)) {
                System.out.println("LOGINNNNN");
                String username = request.getParameter("username");
                request.getSession(true).setAttribute("username", username);                
                response.sendRedirect("main.jsp");
                return;
            } 
            else if ("suscribe".equals(action)) {
                // For IE, Safari and Chrome, we must output some junk to enable
                // streaming
                for (int i = 0; i < 10; i++) {
                    response.getWriter().write(JUNK);
                }
                response.getWriter().flush();
                response.setContentType("text/html");
                BigDecimal id = (BigDecimal) request.getSession().getAttribute("id");
                String username = (String) request.getSession(true).getAttribute("username"); 
                CometRequestHandler handler = new CometRequestHandler();
                handler.clientIP = request.getRemoteAddr();
                handler.attach(response.getWriter());
                //Guardamos el manejador
                ClientInfo clientInfo= new ClientInfo(Boolean.FALSE,id, username,(CometHandler) handler);                
                clientInfos.add(clientInfo);             
                cometContext.addCometHandler(handler);
                System.out.println(isPendingMessage);
                if (isPendingMessage){
                    Operador operador = operadorFacade.find(clientInfo.getId());
                    String info = saveAlarm(listAlarmFrame.get(0), operador);
                    String LT = "LT="+listAlarmFrame.get(0).getLT()+";";
                    String LN = "LN="+listAlarmFrame.get(0).getLN()+";";                
                    String codigo = "<script languaje='Javascript'>"+info+LT+LN+"alarma();</script>";                    
                    clientInfo.setHandlerState(Boolean.TRUE);
                    cometContext.notify(codigo,CometEvent.NOTIFY,clientInfo.getCometHandler());
                    listAlarmFrame.remove(0);
                    isPendingMessage = !listAlarmFrame.isEmpty();
                }

                return;
            } else if ("atendido".equals(action)){
                String username = (String) request.getSession(true).getAttribute("username");
                
                for (ClientInfo clientInfo : clientInfos){
                    if (clientInfo.getUsuario().equals(username)){
                        String codigo = "<script languaje='Javascript'>parent.$('#iframe').hide();$('#alarma').empty();</script>";    
                        cometContext.notify(codigo,CometEvent.NOTIFY,clientInfo.getCometHandler());
                        if(isPendingMessage){
                            Operador operador = operadorFacade.find(clientInfo.getId());
                            String info = saveAlarm(listAlarmFrame.get(0), operador);
                            String LT = "LT="+listAlarmFrame.get(0).getLT()+";";
                            String LN = "LN="+listAlarmFrame.get(0).getLN()+";";                
                            codigo = "<script languaje='Javascript'>"+info+LT+LN+"alarma();</script>";
                            clientInfo.setHandlerState(Boolean.TRUE);
                            cometContext.notify(codigo,CometEvent.NOTIFY,clientInfo.getCometHandler());
                            listAlarmFrame.remove(0);
                            isPendingMessage = !listAlarmFrame.isEmpty();
                        }else{                    
                            clientInfo.setHandlerState(Boolean.FALSE);
                        }
                     }
                }
               
                return;
            } else if ("logout".equals(action)){
                String username = (String) request.getSession(true).getAttribute("username");
                for (ClientInfo clientInfo : clientInfos){                    
                    if (clientInfo.getUsuario().equals(username)){
                        clientInfos.remove(clientInfo);
                    }                
                }
            }

            }
            if (trama!=null){
                System.out.println("entra");
                String LT = "LT="+trama.getLT()+";";
                String LN = "LN="+trama.getLN()+";";
                System.out.println("LT: "+ LT);
                System.out.println("LN: "+ LN);
                ClientInfo firstClientNotBusy = null;
                for(ClientInfo clientInfo : clientInfos){
                    if (firstClientNotBusy==null && clientInfo.getHandlerState().equals(false)){
                        firstClientNotBusy = clientInfo;
                    }
                }
                if (firstClientNotBusy!=null){
                    Operador operador = operadorFacade.find(firstClientNotBusy.getId());
                    String info = saveAlarm(trama, operador);
                    System.out.println(info);
                    String codigo = "<script languaje='Javascript'>"+info+LT+LN+"alarma();</script>";
                    firstClientNotBusy.setHandlerState(Boolean.TRUE);
                    cometContext.notify(codigo,CometEvent.NOTIFY,firstClientNotBusy.getCometHandler());                    

                }
                else {
                    isPendingMessage = true;
                    listAlarmFrame.add(trama);
                }
                return; 
            }           
    }

    // --------------------------------------------------------- Async Hook ---/
    public class CometRequestHandler implements CometHandler<PrintWriter> {

        private PrintWriter printWriter;
        public String clientIP;

        @Override
        public void attach(PrintWriter printWriter) {
            this.printWriter = printWriter;
        }

        @Override
        public void onEvent(CometEvent event) throws IOException {
            try {

               if (event.getType() != CometEvent.READ) {
                    printWriter.println(event.attachment());
                    printWriter.flush();
                }
            } catch (Throwable t) {
            }
        }

        @Override
        public void onInitialize(CometEvent event) throws IOException {
            printWriter.println("<!doctype html public \"-//w3c//dtd html 4.0 transitional//en\">");
            printWriter.println("<html><head><title>Chat</title><script type='text/javascript' src='js/funciones.js'></script>"
                    +"<script type='text/javascript' src='http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js'></script>"
                    + 
                    "</head><body bgcolor=\"#FFFFFF\"><div id='alarma'></div>");
            printWriter.flush();
        }

        @Override
        public void onTerminate(CometEvent event) throws IOException {
            onInterrupt(event);
        }

        @Override
        public void onInterrupt(CometEvent event) throws IOException {

            /*if (this.hashCode() == firstServlet) {
                firstServlet = -1;
            }*/

            printWriter.println("Chat closed<br/>");
            printWriter.println("</body></html>");
            printWriter.flush();
            printWriter.close();
        }
    }
    public class ClientInfo{        
        private Boolean handlerState;
        private String usuario;
        private BigDecimal id;

        public BigDecimal getId() {
            return id;
        }

        public void setId(BigDecimal id) {
            this.id = id;
        }
        private CometHandler cometHandler;
       
        public ClientInfo(Boolean handlerState,BigDecimal id,String usuario,CometHandler cometHandler){
            this.handlerState = handlerState;
            this.usuario = usuario;            
            this.cometHandler = cometHandler;
            this.id=id;
        }
        public Boolean getHandlerState() {
            return handlerState;
        }

        public void setHandlerState(Boolean handlerState) {
            this.handlerState = handlerState;
        }

        public String getUsuario() {
            return usuario;
        }

        public void setUsuario(String usuario) {
            this.usuario = usuario;
        }

        public CometHandler getCometHandler() {
            return cometHandler;
        }

        public void setCometHandler(CometHandler cometHandler) {
            this.cometHandler = cometHandler;
        }
        
        
    }
        public String saveAlarm(Frame frame, Operador operador){
                Alarma alarma = new Alarma();
                Maltratador maltratador;
                Protegido protegido;
                String info="";
                if(frame.getType().equals("ZN")){
                    info= info + "<p>Tipo de alarma: "+frame.getType()+"</p>";
                    maltratador = maltratadorFacade.findByimei(frame.getRD());
                    info= info + "<p>Maltratador: "+maltratador.getNombre() + " " +maltratador.getApellidos()+"</p>";
                    info= info + "<p>Posicion: "+"LN: "+frame.getLN()+" LT: "+frame.getLT()+"</p>";
                    info= info + "<p>Protegida: "+maltratador.getIdProtegido().getNombre() + " " +maltratador.getIdProtegido().getApellidos()+"</p>";
                    info= info + "<p>Movil: "+maltratador.getIdProtegido().getTelefonoMovil()+"</p>";
                    Collection<Contacto> contactoCollection = maltratador.getIdProtegido().getContactoCollection();
                    for (Contacto c: contactoCollection){
                        info= info + "<p>Contactos: "+c.getNombre()+" "+c.getTelefonoContacto()+"</p>";
                    }
                    alarma.setIdMaltratador(maltratador);
                    alarma.setIdProtegido(maltratador.getIdProtegido());
                }else{
                    info= info + "<p>Tipo de alarma: "+frame.getType()+"</p>";
                    protegido = protegidoFacade.findByimei(frame.getRD());
                    info= info + "<p>Protegido: "+protegido.getNombre() + " " +protegido.getApellidos()+"</p>";
                    info= info + "<p>Posicion: "+"LN: "+frame.getLN()+" LT: "+frame.getLT()+"</p>";
                    info= info + "<p>Movil: "+protegido.getTelefonoMovil()+"</p>";
                    Collection<Maltratador> maltratadors = protegido.getMaltratadorCollection();
                    for (Maltratador m: maltratadors){
                        info= info + "<p>Maltratador: "+m.getNombre()+" "+m.getApellidos()+"</p>";
                    }                
                   
                    Collection<Contacto> contactoCollection = protegido.getContactoCollection();
                    for (Contacto c: contactoCollection){
                        info= info + "<p>Contactos: "+c.getNombre()+" "+c.getTelefonoContacto()+"</p>";
                    }
                    
                    protegido = protegidoFacade.findByimei(frame.getRD());
                    alarma.setIdProtegido(protegido);
                    Collection<Maltratador> maltratadorCollection = protegido.getMaltratadorCollection();
                    for (Maltratador m: maltratadorCollection){
                        alarma.setIdMaltratador(m);
                    }                    
                }
                
                Calendar calendar = Calendar.getInstance();                
                calendar.setTime(new Date());
                Date date = calendar.getTime();
                alarma.setIdOperador(operador);
                alarma.setFechaHora(date);
                if (frame.getType().equals("ZN")){
                    alarma.setTipo(BigInteger.ZERO);
                }else{
                    alarma.setTipo(BigInteger.ONE);
                }
                alarmaFacade.create(alarma);
                info = "info= '"+info+"';";                
                return info;
        }

    private Message createJMSMessageForjmsQueue(Session session, Object messageData) throws JMSException {
        // TODO create and populate message to send
        TextMessage tm = session.createTextMessage();
        tm.setText(messageData.toString());
        return tm;
    }

   
        
}
