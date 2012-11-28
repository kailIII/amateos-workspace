/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.servlet;

import org.inftel.scrum.util.SprintComparator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.inftel.scrum.ejb.*;
import org.inftel.scrum.jsf.EstadisticaBean;
import org.inftel.scrum.jsf.InformesBean;
import org.inftel.scrum.jsf.UsuarioController;
import org.inftel.scrum.modelXML.*;
import org.inftel.scrum.util.Cast;

/**
 *
 * @author Fernando
 */
public class ServletUsuario extends HttpServlet {

    @EJB
    private MensajesFacade mensajesFacade;
    @EJB
    private ReunionesFacade reunionesFacade;
    private static final Logger log = Logger.getLogger(ServletUsuario.class.getName());
    @EJB
    private GrupoFacade grupoFacade;
    @EJB
    private SprintFacade sprintFacade;
    @EJB
    private TareaFacade tareaFacade;
    @EJB
    private ProyectoFacade proyectoFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    private Gson gson = new Gson();
    private UsuarioController uc;

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        log.log(Level.INFO, "Entrando al servlet!");
        try {
            String accion = request.getParameter("accion");
            int ac = Integer.parseInt(accion);
            log.log(Level.INFO, "La accion: {0}", ac);
            if (ac == Constantes.DO_LOGIN) {
                log.log(Level.INFO, "Logueandome");
                doLogin(request, response);
            } else if ((uc != null) && (uc.isLoggedIn())) {
                log.log(Level.INFO, "Ya estaba logueado como {0}.La accion es {1}", new Object[]{uc.getEmail(), ac});
                enviarRespuesta(request, response, ac);
            } else {
                log.log(Level.WARNING, "No se puede realizar la accion.Necesita estar logueado.");
            }
        } finally {
//            out.close();
        }
    }
    
    public void doLogin(HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession() != null) {
            log.log(Level.INFO, "Ya estaba logueado, logueandome de nuevo");
            request.getSession().invalidate();
        }
        String nombreUsuario = request.getParameter("Usuario");
        String password = request.getParameter("Password");
        uc = new UsuarioController();
        uc.setEmail(nombreUsuario);
        uc.setPass(password);
        uc.doLogin();
        if (uc.isLoggedIn()) {
            request.getSession(true);
            HttpSession s = request.getSession(true);
            response.setHeader("jsessionid", s.getId());
            request.getRequestedSessionId();
        }
    }
    
    public void enviarRespuesta(HttpServletRequest request, HttpServletResponse response, int ac) {
        switch (ac) {
            case Constantes.VER_PROYECTO: {
                try {
                    org.inftel.scrum.model.Usuario u = usuarioFacade.findByMail(uc.getEmail());
                    org.inftel.scrum.model.Grupo g = u.getGrupoId();
                    org.inftel.scrum.model.Proyecto p = g.getProyectoId();
                    Proyecto pr = Cast.castProyectoFromEntity(p);
                    String envio = gson.toJson(pr);
                    log.log(Level.INFO, "Sending VER_PROYECTO: " + envio);
                    response.setHeader("VER_PROYECTO", envio);
                } catch (Exception e) {
                    response.setHeader("VER_PROYECTO", "null");
                }
            }
            break;
            case Constantes.OBTENER_USUARIO: {
                String id = request.getParameter("OBTENER_USUARIO");
                org.inftel.scrum.model.Usuario u = usuarioFacade.findByName(id);
                Usuario user = Cast.castUsuarioFromEntity(u);
                List<Historico> lis = new ArrayList<Historico>();
                for (org.inftel.scrum.model.Historico h : u.getHistoricoList()) {
                    Historico h1 = Cast.castHistoricoFromEntity(h);
                    lis.add(h1);
                }
                user.setHistoricoList(lis);
                List<Tarea> tar = new ArrayList<Tarea>();
                for (org.inftel.scrum.model.Tarea t : u.getTareaList()) {
                    Tarea ta = Cast.castTareaFromEntity(t, false);
                    ta.setUsuarioId(user);
                    ta.setSprintId(Cast.castSprintFromEntity(t.getSprintId()));
                }
                user.setTareaList(tar);
//                
                List<Log> log = new ArrayList<Log>();
                for (org.inftel.scrum.model.Log l : u.getLogList()) {
                    Log l1 = Cast.castLogFromEntity(l);
                    l1.setUsuarioId(user);
                }
                user.setLogList(log);
                List<Mensajes> mensajes = new ArrayList<Mensajes>(); //estoy en el origen
                for (org.inftel.scrum.model.Mensajes m : u.getMensajesList()) {
                    Mensajes m1 = Cast.castMensajesFromEntity(m);
                    m1.setDestino(Cast.castUsuarioFromEntity(m.getDestino()));
                    m1.setOrigen(user);
                    mensajes.add(m1);
                }
                user.setMensajesList(mensajes);
                List<Mensajes> mensajes1 = new ArrayList<Mensajes>(); //estoy en el origen
                for (org.inftel.scrum.model.Mensajes m : u.getMensajesList1()) {
                    Mensajes m1 = Cast.castMensajesFromEntity(m);
                    m1.setOrigen(Cast.castUsuarioFromEntity(m.getOrigen()));
                    m1.setDestino(user);
                    mensajes1.add(m1);
                }
                user.setMensajesList(mensajes1);//estoy en el destino
                String userString = gson.toJson(user);
                response.setHeader("OBTENER_USUARIO", userString);
            }
            break;
            case Constantes.CONTAR_USUARIOS: {
                response.setHeader("CONTAR_USUARIOS", Integer.toString(usuarioFacade.count()));
            }
            break;
            case Constantes.CREAR_USUARIO: {
                try {
                    org.inftel.scrum.model.Usuario usuario;
                    String json = request.getParameter("CREAR_USUARIO");
                    Usuario u = gson.fromJson(json, Usuario.class);
                    usuario = Cast.castUsuarioToEntity(u);
                    usuario.setId(usuarioFacade.maxId().add(BigDecimal.ONE));
                    usuarioFacade.create(usuario);
                    String valido = "true";
                    response.setHeader("CREAR_USUARIO", gson.toJson(valido));
                } catch (Exception ex) {
                    Logger.getLogger(ServletUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case Constantes.EDITAR_USUARIO: {
            }
            break;
            case Constantes.LISTAR_USUARIOS: {
                org.inftel.scrum.model.Usuario u = usuarioFacade.findByMail(uc.getEmail());
                org.inftel.scrum.model.Grupo g = u.getGrupoId();
                org.inftel.scrum.model.Proyecto p = g.getProyectoId();
                List<Usuario> listas = new ArrayList<Usuario>();
                for (org.inftel.scrum.model.Usuario user : usuarioFacade.findByProyecto(p)) {
                    Usuario usuario = Cast.castUsuarioFromEntity(user);
                    listas.add(usuario);
                }
                String list = gson.toJson(listas);
                response.setHeader("LISTAR_USUARIOS", list);
                
            }
            break;
            
            case Constantes.BUSCAR_USUARIO_POR_NOMBRE: {
                
            }
            break;
            case Constantes.BUSCAR_USUARIO_POR_EMAIL: {
                String mail = request.getParameter("email");
                org.inftel.scrum.model.Usuario u = usuarioFacade.findByMail(mail);
                Usuario user = Cast.castUsuarioFromEntity(u);
                List<Historico> lis = new ArrayList<Historico>();
                for (org.inftel.scrum.model.Historico h : u.getHistoricoList()) {
                    Historico h1 = Cast.castHistoricoFromEntity(h);
                    lis.add(h1);
                }
                user.setHistoricoList(lis);
                List<Tarea> tar = new ArrayList<Tarea>();
                for (org.inftel.scrum.model.Tarea t : u.getTareaList()) {
                    Tarea ta = Cast.castTareaFromEntity(t, false);
                    ta.setUsuarioId(user);
                    ta.setSprintId(Cast.castSprintFromEntity(t.getSprintId()));
                }
                user.setTareaList(tar);
            
                List<Log> log = new ArrayList<Log>();
                for (org.inftel.scrum.model.Log l : u.getLogList()) {
                    Log l1 = Cast.castLogFromEntity(l);
                    l1.setUsuarioId(user);
                }
                user.setLogList(log);
                List<Mensajes> mensajes = new ArrayList<Mensajes>(); //estoy en el origen
                for (org.inftel.scrum.model.Mensajes m : u.getMensajesList()) {
                    Mensajes m1 = Cast.castMensajesFromEntity(m);
                    m1.setDestino(Cast.castUsuarioFromEntity(m.getDestino()));
                    m1.setOrigen(user);
                    mensajes.add(m1);
                }
                user.setMensajesList(mensajes);
                List<Mensajes> mensajes1 = new ArrayList<Mensajes>(); //estoy en el origen
                for (org.inftel.scrum.model.Mensajes m : u.getMensajesList1()) {
                    Mensajes m1 = Cast.castMensajesFromEntity(m);
                    m1.setOrigen(Cast.castUsuarioFromEntity(m.getOrigen()));
                    m1.setDestino(user);
                    mensajes1.add(m1);
                }
                user.setMensajesList(mensajes1);//estoy en el destino
                String userString = gson.toJson(user);
                response.setHeader("BUSCAR_USUARIO_POR_EMAIL", userString);
            }
            break;
            case Constantes.BUSCAR_USUAIRO_POR_PROYECTO: {
            }
            break;
            case Constantes.MAX_ID_USUARIO: {
            }
            break;
            case Constantes.ELIMINAR_USUARIO: {
            }
            break;
            case Constantes.CONTAR_PROYECTO: {
            }
            break;
            case Constantes.BUSCAR_PROYECTO_POR_NOMBRE: {
            }
            break;
            case Constantes.BUSCAR_PROYECTO_REPETIDO: {
            }
            break;
            case Constantes.CREAR_PROYECTO: {
            }
            break;
            case Constantes.EDITAR_PROYECTO: {
            }
            break;
            case Constantes.MAX_ID_PROYECTO: {
            }
            break;
            case Constantes.ELIMINAR_PROYECTO: {
            }
            break;
            case Constantes.CONTAR_TAREAS: {
            }
            break;
            case Constantes.CREAR_TAREA: {
                String jsonTarea = request.getParameter("Tarea");
                String jsonSprint = request.getParameter("Sprint");
                String jsonProyecto = request.getParameter("Proyecto");
                
                Tarea tarea = new Gson().fromJson(jsonTarea, Tarea.class);
                Sprint sprint = new Gson().fromJson(jsonSprint, Sprint.class);
                Proyecto proyecto = new Gson().fromJson(jsonProyecto, Proyecto.class);
                
                org.inftel.scrum.model.Tarea t = Cast.castTareaToEntity(tarea);
                t.setId(tareaFacade.maxId().add(BigDecimal.ONE));
                t.setProyectoId(Cast.castProyectoToEntity(proyecto));
                t.setSprintId(Cast.castSprintToEntity(sprint));
                
                tareaFacade.create(t);
                response.setHeader("CREAR_TAREA", t.getId().toString());
            }
            break;
            case Constantes.BUSCAR_TAREA_POR_NOMBRE: {
            }
            break;
            case Constantes.BUSCAR_TAREA_REPETIDA: {
            }
            break;
            case Constantes.BUSCAR_TAREA_POR_USUARIO: {
            }
            break;
            case Constantes.BUSCAR_TAREAS_TODO: {
            }
            break;
            case Constantes.BUSCAR_TAREAS_DOING: {
            }
            break;
            case Constantes.BUSCAR_TAREAS_DONE: {
            }
            break;
            case Constantes.MAX_ID_TAREA: {
            }
            break;
            case Constantes.ELIMINAR_TAREA: {
            }
            break;
            case Constantes.CONTAR_SPRINTS: {
            }
            break;
            case Constantes.CREAR_SPRINT: {
                String json = request.getParameter("Sprint");
                Sprint sprint = new Gson().fromJson(json, Sprint.class);
                org.inftel.scrum.model.Sprint s = Cast.castSprintToEntity(sprint);
                s.setId(sprintFacade.maxId().add(BigDecimal.ONE));
                sprintFacade.create(s);
                response.setHeader("CREAR_SPRINTS", s.getId().toString());
            }
            break;
            case Constantes.EDITAR_SPRINT: {
            }
            case Constantes.BUSCAR_SPRINTS: {
                org.inftel.scrum.model.Usuario u = usuarioFacade.findByMail(uc.getEmail());
                List<org.inftel.scrum.model.Sprint> lista = u.getGrupoId().getProyectoId().getSprintList();
                List<Sprint> sprints = new ArrayList<Sprint>();
                for (org.inftel.scrum.model.Sprint sprint : lista) {
                    Sprint s = Cast.castSprintFromEntity(sprint);
                    sprints.add(s);
                }
                Collections.sort(sprints, new SprintComparator());
                String list = gson.toJson(sprints);
                System.out.println(list);
                response.setHeader("BUSCAR_SPRINTS", list);
            }
            break;
            
            case Constantes.LISTAR_SPRINTS: {
            }
            break;
            case Constantes.MAX_ID_SPRINT: {
            }
            break;
            case Constantes.ELIMINAR_SPRINT: {
            }
            break;
            case Constantes.GET_BURNDOWN: {
                
                try {
                    EstadisticaBean estadisticaBean = new EstadisticaBean();
                    estadisticaBean.setUc(uc);
                    estadisticaBean.generarEstadisticas();
                    response.setContentType("image/jpeg");
                    response.setContentLength(estadisticaBean.getByteBurndownImage().length);
                    response.getOutputStream().write(estadisticaBean.getByteBurndownImage());
                    log.log(Level.INFO, "Sending burndown image");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            case Constantes.GET_PIE: {
                try {
                    EstadisticaBean estadisticaBean = new EstadisticaBean();
                    estadisticaBean.setUc(uc);
                    estadisticaBean.generarEstadisticas();
                    response.setContentType("image/jpeg");
                    response.setContentLength(estadisticaBean.getBytePieImage().length);
                    response.getOutputStream().write(estadisticaBean.getBytePieImage());
                    log.log(Level.INFO, "Sending piechart image");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            case Constantes.GET_BURNUP: {
                try {
                    EstadisticaBean estadisticaBean = new EstadisticaBean();
                    estadisticaBean.setUc(uc);
                    estadisticaBean.generarEstadisticas();
                    response.setContentType("image/jpeg");
                    response.setContentLength(estadisticaBean.getByteBurnupImage().length);
                    response.getOutputStream().write(estadisticaBean.getByteBurnupImage());
                    log.log(Level.INFO, "Sending burnup image");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            case Constantes.VER_BACKLOG: {
                String nombreProyecto = request.getParameter("NOMBRE_PROYECTO");
                org.inftel.scrum.model.Proyecto p = proyectoFacade.findByNombre(nombreProyecto);
                List<Tarea> lista = new ArrayList<Tarea>();
                for (org.inftel.scrum.model.Tarea t : p.getTareaList()) {
                    Tarea tarea = Cast.castTareaFromEntity(t, true);
                    lista.add(tarea);
                }
                String list = gson.toJson(lista);
                response.setHeader("VER_BACKLOG", list);
            }
            break;
            case Constantes.CREAR_REUNIONES: {
                String nombreProyecto = request.getParameter("CREAR_REUNIONES");
                Reuniones reunion = gson.fromJson(nombreProyecto, Reuniones.class);
                org.inftel.scrum.model.Reuniones r = Cast.castReunionToEntity(reunion);
                r.setUsuarioId(usuarioFacade.findByMail(uc.getEmail()));
                r.setId(reunionesFacade.maxId().add(BigDecimal.ONE));
                org.inftel.scrum.model.Grupo grupo=usuarioFacade.findByMail(uc.getEmail()).getGrupoId();
                for(org.inftel.scrum.model.Usuario usuario:grupo.getUsuarioList()){
                    org.inftel.scrum.model.Mensajes mensaje=new org.inftel.scrum.model.Mensajes();
                    mensaje.setAsunto("Reunion");
                    mensaje.setDestino(usuario);
                    mensaje.setFecha(r.getFecha());
                    mensaje.setLeido('0');
                    mensaje.setMensaje(r.getAsunto());
                    mensaje.setNotificado('0');
                    mensaje.setOrigen(usuarioFacade.findByMail(uc.getEmail()));
                    System.out.println("El id...."+mensajesFacade.maxId().add(BigDecimal.ONE));
                    mensaje.setId(mensajesFacade.maxId().add(BigDecimal.ONE));
                    mensajesFacade.create(mensaje);
                }
                reunionesFacade.create(r);
            }
            break;
            case Constantes.BUSCAR_REUNIONES: {
                List<org.inftel.scrum.model.Reuniones> listReuniones = reunionesFacade.findAll();
                List<org.inftel.scrum.model.Reuniones> reunionesUsuario = new ArrayList<org.inftel.scrum.model.Reuniones>();
                for (org.inftel.scrum.model.Reuniones r : listReuniones) {
                    if (r.getFecha().after(new Date())) {
                        reunionesUsuario.add(r);
                    }
                }
                List<Reuniones> lista = new ArrayList<Reuniones>();
                for (org.inftel.scrum.model.Reuniones r : reunionesUsuario) {
                    Reuniones reunion = Cast.castReunionFromEntity(r);
                    reunion.setUsuarioId(Cast.castUsuarioFromEntity(usuarioFacade.findByMail(uc.getEmail())));
                    lista.add(reunion);
                }
                String list = gson.toJson(lista);
                response.setHeader("BUSCAR_REUNIONES", list);
            }
            break;
            case Constantes.GET_PDF_BACKLOG: {
                try {
                    InformesBean ib = new InformesBean();
                    ib.setUsuarioController(uc);
                    byte[] informe = ib.informeBacklog(0);
                    response.setContentLength(informe.length);
                    response.getOutputStream().write(informe);
                } catch (IOException ex) {
                    Logger.getLogger(ServletUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case Constantes.GET_PDF_LOGS: {
                try {
                    InformesBean ib = new InformesBean();
                    ib.setUsuarioController(uc);
                    byte[] informe = ib.informeLogs(0);
                    response.setContentLength(informe.length);
                    response.getOutputStream().write(informe);
                } catch (IOException ex) {
                    Logger.getLogger(ServletUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            break;
            case Constantes.GET_PDF_PROYECTO: {
                try {
                    InformesBean ib = new InformesBean();
                    ib.setUsuarioController(uc);
                    byte[] informe = ib.informeProyecto(0);
                    response.setContentLength(informe.length);
                    response.getOutputStream().write(informe);
                } catch (IOException ex) {
                    Logger.getLogger(ServletUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            break;
            case Constantes.GET_PDF_SPRINTS: {
                try {
                    InformesBean ib = new InformesBean();
                    ib.setUsuarioController(uc);
                    byte[] informe = ib.informeSprints(0);
                    response.setContentLength(informe.length);
                    response.getOutputStream().write(informe);
                } catch (IOException ex) {
                    Logger.getLogger(ServletUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            break;
            case Constantes.GET_PDF_USUARIO: {
                try {
                    InformesBean ib = new InformesBean();
                    ib.setUsuarioController(uc);
                    byte[] informe = ib.informeUsuarios(0);
                    response.setContentLength(informe.length);
                    response.getOutputStream().write(informe);
                } catch (IOException ex) {
                    Logger.getLogger(ServletUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            break;
            case Constantes.GET_MENSAJES_ENVIADOS: {
                List<org.inftel.scrum.model.Mensajes> listMensajes = mensajesFacade.findEnviadosByUsuario(usuarioFacade.findByMail(uc.getEmail()));
                List<Mensajes> mensajesEnviados = new ArrayList<Mensajes>();
                for (org.inftel.scrum.model.Mensajes mensaje : listMensajes) {
                    Mensajes m = Cast.castMensajesFromEntity(mensaje);
                    m.setOrigen(Cast.castUsuarioFromEntity(mensaje.getOrigen()));
                    mensajesEnviados.add(m);
                }
                String list = gson.toJson(mensajesEnviados);


                response.setHeader("GET_MENSAJES_ENVIADOS", list);
            }
            break;
            case Constantes.GET_MENSAJES_RECIBIDOS: {
                List<org.inftel.scrum.model.Mensajes> listMensajes = mensajesFacade.findRecibidosByUsuario(usuarioFacade.findByMail(uc.getEmail()));
                List<Mensajes> mensajesRecibidos = new ArrayList<Mensajes>();
                for (org.inftel.scrum.model.Mensajes mensaje : listMensajes) {
                    Mensajes m = Cast.castMensajesFromEntity(mensaje);
                    m.setOrigen(Cast.castUsuarioFromEntity(mensaje.getOrigen()));
                    m.setDestino(Cast.castUsuarioFromEntity(mensaje.getDestino()));
                    mensajesRecibidos.add(m);                    
                }
                String list = gson.toJson(mensajesRecibidos);
                System.out.println("lista:"+list);
                response.setHeader("GET_MENSAJES_RECIBIDOS", list);
            }
            break;
            case Constantes.SET_MENSAJES_NOTIFICA: {
                String iden = request.getParameter("identificador");
                
                org.inftel.scrum.model.Mensajes mensajes = mensajesFacade.findMensajeById(iden);
                mensajes.setNotificado('1');
                mensajesFacade.edit(mensajes);
            }
            break;
            case Constantes.SET_MENSAJES_LEIDO: {
                String iden = request.getParameter("identificador");
                
                org.inftel.scrum.model.Mensajes mensajes = mensajesFacade.findMensajeById(iden);
                mensajes.setLeido('1');
                mensajesFacade.edit(mensajes);
                
            }
            break;  
            case Constantes.ELIMINAR_MENSAJE: {
                String iden = request.getParameter("identificador");
                org.inftel.scrum.model.Mensajes mensajes = mensajesFacade.findMensajeById(iden);
                mensajesFacade.remove(mensajes);               
            }
            break;  
            
            case Constantes.BUSCAR_USUARIO_GRUPO: {
                org.inftel.scrum.model.Grupo grupo = usuarioFacade.findByMail(uc.getEmail()).getGrupoId();
                List<org.inftel.scrum.model.Usuario> lista = grupo.getUsuarioList();
                List<Usuario> listaUsuarios = new ArrayList<Usuario>();
                for (org.inftel.scrum.model.Usuario usuario : lista) {
                    Usuario u = Cast.castUsuarioFromEntity(usuario);
                    listaUsuarios.add(u);
                }
                String list = gson.toJson(listaUsuarios);
                response.setHeader("BUSCAR_USUARIO_GRUPO", list);
            }
            break;
            case Constantes.DO_LOGOUT: {
                if (uc != null) {
                    request.getSession().invalidate();
                    log.log(Level.INFO, "Logout success!");
                }
            }
            break;
            case Constantes.GET_FORECAST: {
                EstadisticaBean estadisticaBean = new EstadisticaBean();
                estadisticaBean.setUc(uc);
                estadisticaBean.generarEstadisticas();
                log.log(Level.INFO, "Enviando forecast: {0}", estadisticaBean.getForecast().toString());
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                response.setHeader("GET_FORECAST", formato.format(estadisticaBean.getForecast()));
            }
            break;
            case Constantes.GET_SPEED: {
                EstadisticaBean estadisticaBean = new EstadisticaBean();
                estadisticaBean.setUc(uc);
                estadisticaBean.generarEstadisticas();
                log.log(Level.INFO, "Enviando speed: {0}", estadisticaBean.getVelocidadFormatted());
                response.setHeader("GET_SPEED", estadisticaBean.getVelocidadFormatted());
            }
            break;
            case Constantes.GET_DELAY: {
                EstadisticaBean estadisticaBean = new EstadisticaBean();
                estadisticaBean.setUc(uc);
                estadisticaBean.generarEstadisticas();
                org.inftel.scrum.model.Usuario u = usuarioFacade.findByMail(uc.getEmail());
                float daysDelay = (u.getGrupoId().getProyectoId().getEntrega().getTime() - estadisticaBean.getForecast().getTime()) / 1000 * 60 * 60 * 24;
                String delay = "0";
                if (daysDelay <= 0) {
                    delay = "0";
                } else if (daysDelay < 3) {
                    delay = "1";
                } else {
                    delay = "2";
                }
                log.log(Level.INFO, "Enviando delay: {0}", delay);
                response.setHeader("GET_DELAY", delay);
            }
            break;
            case Constantes.BUSCAR_TAREAS_SPRINTS: {
                BigDecimal sprintId = new BigDecimal(request.getParameter("Sprint"));
                org.inftel.scrum.model.Sprint sprint = sprintFacade.find(sprintId);
                List<org.inftel.scrum.model.Tarea> tareas = sprint.getTareaList();
                List<Tarea> listaTareas = new ArrayList<Tarea>();
                for (org.inftel.scrum.model.Tarea t : tareas) {
                    Tarea tarea = Cast.castTareaFromEntity(t, true);
                    tarea.setUsuarioId(Cast.castUsuarioFromEntity(uc.getUser()));
                    listaTareas.add(tarea);
                }
                String list = gson.toJson(listaTareas);
                response.setHeader("BUSCAR_TAREAS_SPRINTS", list);
            }
            break;
            case Constantes.EDITAR_TAREA: {
                String jsonTarea = request.getParameter("Tarea");
                String idProyecto = request.getParameter("Proyecto");
                String idSprint = request.getParameter("Sprint");
                String emailUsuario = request.getParameter("Usuario");
                Tarea tarea = new Gson().fromJson(jsonTarea, Tarea.class);
                org.inftel.scrum.model.Tarea t = Cast.castTareaToEntity(tarea);
                org.inftel.scrum.model.Proyecto proyecto = proyectoFacade.findProyectoById(new BigDecimal(idProyecto));
                org.inftel.scrum.model.Sprint sprint = sprintFacade.find(new BigDecimal(idSprint));
                org.inftel.scrum.model.Usuario usuario = usuarioFacade.findByMail(emailUsuario);
                t.setProyectoId(proyecto);
                t.setSprintId(sprint);
                t.setUsuarioId(usuario);
                tareaFacade.edit(t);
            }
            break;
            case Constantes.OBTENER_FECHA_INICIO_TAREA: {
                BigDecimal tareaId = new BigDecimal(request.getParameter("Tarea"));
                org.inftel.scrum.model.Tarea tarea = tareaFacade.findById(tareaId);
                Date fechaInicio = tarea.getInicio();
                String fecha = gson.toJson(fechaInicio);
                response.setHeader("OBTENER_FECHA_INICIO_TAREA", fecha);
            }
            break;
            case Constantes.OBTENER_FECHA_FIN_TAREA: {
                BigDecimal tareaId = new BigDecimal(request.getParameter("Tarea"));
                org.inftel.scrum.model.Tarea tarea = tareaFacade.findById(tareaId);
                Date fechaFin = tarea.getFechaFin();
                String fecha = gson.toJson(fechaFin);
                response.setHeader("OBTENER_FECHA_FIN_TAREA", fecha);
            }
            break;
            case Constantes.OBTENER_USUARIO_TAREA: {
                BigDecimal tareaId = new BigDecimal(request.getParameter("Tarea"));
                org.inftel.scrum.model.Tarea tarea = tareaFacade.findById(tareaId);
                Usuario usuario = Cast.castUsuarioFromEntity(tarea.getUsuarioId());
                String u = gson.toJson(usuario);
                response.setHeader("OBTENER_USUARIO_TAREA", u);
            }
            break;
            case Constantes.EDITAR_LISTA_TAREAS:{
                String jsonTarea = request.getParameter("Tareas");
                String idProyecto = request.getParameter("Proyecto");
                String idSprint = request.getParameter("Sprint");
//                String emailUsuario = request.getParameter("Usuario");
                org.inftel.scrum.model.Proyecto proyecto = proyectoFacade.findProyectoById(new BigDecimal(idProyecto));
                org.inftel.scrum.model.Sprint sprint = sprintFacade.find(new BigDecimal(idSprint));
                org.inftel.scrum.model.Usuario usuario = usuarioFacade.findByMail(uc.getEmail());
                
                Type listOfTestObject = new TypeToken<List<Tarea>>() {
		}.getType();
		ArrayList<Tarea> list = new Gson().fromJson(jsonTarea, listOfTestObject);
                for(Tarea t:list){
                    org.inftel.scrum.model.Tarea ta = Cast.castTareaToEntity(t);
                    ta.setProyectoId(proyecto);
                    ta.setSprintId(sprint);
                    ta.setUsuarioId(usuario);
                    tareaFacade.edit(ta);
                }
            }break;
            case Constantes.CREAR_MENSAJE:{
                String mensajeJson=request.getParameter("Mensaje");
                String emailDestino=request.getParameter("usuarioDestino");
                org.inftel.scrum.model.Usuario uDestino = usuarioFacade.findByMail(emailDestino);
                System.out.println("servelt usuario: "+uDestino.getId().toString());
                String emailOrigen=request.getParameter("usuarioOrigen");
                org.inftel.scrum.model.Usuario uOrigen = usuarioFacade.findByMail(emailOrigen);
                System.out.println("servelt usuario2: "+uOrigen.getId().toString());
                Mensajes mensaje=new Gson().fromJson(mensajeJson, Mensajes.class);
                org.inftel.scrum.model.Mensajes m= Cast.castMensajesToEntity(mensaje);
                m.setDestino(uDestino);
                m.setOrigen(uOrigen);
                m.setLeido('1');
                m.setNotificado('1');
                m.setFecha(new Date());
                m.setId(mensajesFacade.maxId().add(BigDecimal.ONE));
                mensajesFacade.create(m);
                
            }break;
                
        }
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
     * @param response servlet responsef
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
