package com.ta.messenger.cliente;

import java.util.Arrays;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Principal extends Activity {
	
	private String[] listaContactos;
	private String[] listaApodos;
	private String[] listaContactosConectados; 
	private String[] listaApodosConectados;

	private Boolean vistaConectados;
    
    String nombreUsuario;
    String apodoUsuario;
	
	ListView listaView;
	Spinner spinner;
	TextView textoContactos;
	TextView textoApodo;
	TextView textoConversacion;
	EditText entradaTexto;

	
	Peticiones peticiones;
	PeticionesConversacion peticionesConversacion;
	Vector <Conversacion> conversaciones;
	String[] listaConversaciones;
	int conversacionActiva;
	

    
////////////////////////////////////////////////////////////////////////////////
///////////////////////////////// ONCREATE /////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtención de datos usuario desde activity de login
	    Bundle bundle = this.getIntent().getExtras();
        nombreUsuario = bundle.getString("Usuario");
        apodoUsuario = bundle.getString("Apodo");
        
	    //Se crea objeto hebra para peticiones TCP con servidor	    
	    peticiones = new Peticiones();
	    
        // Inicialización elementos interfaz usuario
        setContentView(R.layout.principal);

        /*Texto contactos conectados-todos los contactos y apodo*/
		textoApodo = (TextView) this.findViewById(R.id.texto_apodo);
		textoApodo.setText(apodoUsuario);
        textoContactos = (TextView) this.findViewById(R.id.texto_contactos_conectados);
		textoContactos.setText("Contactos conectados");
		textoConversacion = (TextView) this.findViewById(R.id.texto_conversacion);
		entradaTexto = (EditText) findViewById(R.id.texto_a_enviar_conversacion);
		
        /*Lista de contactos*/
		listaView = (ListView)findViewById(R.id.view_lista);
		registerForContextMenu(listaView);

	    // Inicialización vectores contactos y conversaciones
		actualizarListasContactos();
		vistaConectados = true;
		actualizarVistaLista();
		
		// INICIALIZACION CONVERSACIONES
        conversaciones = new Vector <Conversacion>();
        obtenerListaConversaciones();
		
		/*Lista expandible conversaciones*/
	    spinner = (Spinner) findViewById(R.id.spinner);
	    spinner.setOnItemSelectedListener(new spinnerListener());
	    actualizarSpinner();
	    
	    // Se obtiene la lista de contactos (incluyendo conectados y no conectados)
	    listaContactos = peticiones.obtenerListaContactosDeMensaje(peticiones.solicitarListaContactos (nombreUsuario));
        Arrays.sort(listaContactos);
        listaApodos = peticiones.solicitarApodosContactos(listaContactos);
        
        

    }// FIN ON CREATE
    

	
	
////////////////////////////////////////////////////////////////////////////////	
/////////////////////////// OTROS //////////////////////////////////////////////    
////////////////////////////////////////////////////////////////////////////////
    /*********************************************************************/
    /******************** ACTUALIZARLISTACONTACTOS ***********************/
    /*********************************************************************/
    private void actualizarListasContactos(){
    	// Obtención de la lista de contactos
    	listaContactos = peticiones.obtenerListaContactosDeMensaje(peticiones.solicitarListaContactos (nombreUsuario));
        Arrays.sort(listaContactos);
        listaApodos = peticiones.solicitarApodosContactos(listaContactos);

        // Obtención de la lista de contactos conectados
    	listaContactosConectados = peticiones.obtenerListaContactosConectadosDeMensaje(peticiones.solicitarListaContactosConectados (nombreUsuario));
        Arrays.sort(listaContactosConectados);
        listaApodosConectados = peticiones.solicitarApodosContactos(listaContactosConectados);
    	
    }
    
    /*********************************************************************/
    /******************** OBTENERLISTACONVERSACIONES *********************/
    /*********************************************************************/
    private void obtenerListaConversaciones(){
    	if(conversaciones.size()>0){ //Hay conversaciones
	    	String aux=conversaciones.elementAt(0).apodo +";";
	    	for (int i=1; i<conversaciones.size(); i++){
	    		aux+=conversaciones.elementAt(i).apodo +";";
	    	}
	    	listaConversaciones = aux.split(";");
    	}else{
    		listaConversaciones = new String[1];
    		listaConversaciones [0] = "Conversaciones";
    	}
    	
    }
    


////////////////////////////////////////////////////////////////////////////////
/////////////////// INTERFAZ GRAFICA ///////////////////////////////////////// 
////////////////////////////////////////////////////////////////////////////////
    
    /*********************************************************************/
    /************************ ACTUALIZARVISTALISTA ****************************/
    /*********************************************************************/
    private void actualizarVistaLista(){
    	if(vistaConectados){
	        listaView.setAdapter(new ArrayAdapter<String>(this,R.layout.item, listaApodosConectados));
    	}else{
	        listaView.setAdapter(new ArrayAdapter<String>(this,R.layout.item, listaApodos));
    	}
    }   
    
    
    /*********************************************************************/
    /************************ ACTUALIZARSPINNER **************************/
    /*********************************************************************/
    private void actualizarSpinner(){

    	spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listaConversaciones));

    }

    /*********************************************************************/
    /************************ SPINNERLISTENER ****************************/
    /*********************************************************************/
    public class spinnerListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        	if(listaConversaciones[pos].equals("Conversaciones")){
        		textoConversacion.setText("");
        	}else{
        		conversacionActiva = pos;
        		mostrarConversacion();
        	}
        }

        public void onNothingSelected(AdapterView parent) {
          // Do nothing.
        }
    }
    
    /*********************************************************************/
    /************************ MOSTRARCONVERSACION **************************/
    /*********************************************************************/
    public void mostrarConversacion(){
		textoConversacion.setText(conversaciones.elementAt(conversacionActiva).conversacion);
    }
    
    /*********************************************************************/
    /************************ CERRARCONVERSACION **************************/
    /*********************************************************************/
    public void cerrarConversacion(){
    	
    	///////////////////////////////////////////////////////
    	////Mandar mensaje cierre conversacion al contacto ////
    	///////////////////////////////////////////////////////
    	conversaciones.remove(conversacionActiva);
		conversacionActiva = 0;
       	obtenerListaConversaciones();
    	actualizarSpinner();    	
    	if(conversaciones.size()>0){
    		textoConversacion.setText(conversaciones.elementAt(conversacionActiva).conversacion);
    	}else{
    		textoConversacion.setText("");
    	}
    }
    
    /*********************************************************************/
    /************************ ONACTIVITYRESULT ****************************/
    /*********************************************************************/
    // Método que se llama a la vuelta de otras activities
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    	
        // Se extraen los datos del intent procedente de la activity anterior
        Bundle extras = intent.getExtras();
    	String respuesta = extras.getString("Respuesta");
    	
    	if(respuesta.equals("Agregar contacto")){ // Vuelve de AGREGAR CONTACTO
    		String contactoAgregar = extras.getString("Contacto");
    		String mensajeRespuesta = peticiones.agregarContacto(contactoAgregar);
	    	actualizarListasContactos();
	    	actualizarVistaLista();
    	}
    	if(respuesta.equals("Cambiar apodo")){
    		String nuevoApodo = extras.getString("Apodo");
    		String mensajeRespuesta = peticiones.cambiarApodo(nuevoApodo);
    		textoApodo.setText(nuevoApodo);
    	}
    	
    }    
	/*********************************************************************/
    /********************* MENU ******************************************/
    /*********************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.itemAgregarContacto:
	    		Intent i = new Intent(getApplicationContext(), IntroducirDatos.class);
	    		i.putExtra("Operacion", 0);// Intencion 0 -> agregar contacto
	            startActivityForResult(i,0);
	       	   return true;
	       	   
	    case R.id.itemApodo:
    			Intent i2 = new Intent(getApplicationContext(), IntroducirDatos.class);
    			i2.putExtra("Operacion", 1);// Intencion 1 -> apodo
    			startActivityForResult(i2,1);    		
       	   return true;
       	   
	    case R.id.itemCerrarConversacion:
	    		cerrarConversacion();
	    	return true;
       	   
	    case R.id.itemListaContactos:
	    	   
			if(vistaConectados){
				vistaConectados = false;
				textoContactos.setText("Todos los contactos");
			}else{
				vistaConectados = true;
				textoContactos.setText("Contactos conectados");
			}
			actualizarVistaLista();
       	   return true;
       	   
	    case R.id.itemDesconectar:
	    		String mensajeRespuesta = peticiones.desconectar();
	       	   finish();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
    
    /*********************************************************************/
    /******************************* CONTEXTO ****************************/
    /*********************************************************************/
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	if (v.getId()==R.id.view_lista) {
    	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    		
    	    if(vistaConectados){
    	    	menu.setHeaderTitle(listaContactosConectados[info.position]);
    	    }else{
    	    	menu.setHeaderTitle(listaContactos[info.position]);
    	    }
 			menu.add(Menu.NONE, 0, 0, R.string.eliminarContacto);
 			
 			if(vistaConectados){//La opción de iniciar conversación sólo aparece en la vista de conectados
 				menu.add(Menu.NONE, 1, 0, R.string.conversacion);
 			}

    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	    
	    int menuItemIndex = item.getItemId();
	    String contacto;
	    String apodo;
	    if(vistaConectados){
	    	contacto = listaContactosConectados[info.position];
	    	apodo = listaApodosConectados[info.position];
	    }else{
	    	contacto = listaContactos[info.position];
	    	apodo = listaApodos[info.position];
	    }
	    switch(menuItemIndex){
	    
	    case 0: //Eliminar contacto
		    Log.d("CONTEXT","Seleccionado eliminar contacto: "+contacto);
	    	String mensajeRespuesta = peticiones.eliminarContacto(contacto);
	    	actualizarListasContactos();
	    	actualizarVistaLista();
	    	break;
	    	
	    case 1: //Comenzar conversación

	    	String ip = peticiones.localizarContacto(contacto);
	    	Log.d("IP", "La IP del contacto es: "+ip);

	    	//Se crea la conversaci�n
	    	Conversacion conversacion = new Conversacion(contacto,apodo,ip);
	    	
	        conversacion.agregarTextoAConversacion(apodo, "Hola EmP");
	    	
	    	//Prueba de inclusion de texto
	    	PeticionesConversacion conversacionConContacto = new PeticionesConversacion(ip,conversacion,this,entradaTexto);
	    	conversaciones.add(conversacion);
	    	conversacionActiva = conversaciones.indexOf(conversacion);
	    	obtenerListaConversaciones();
	    	actualizarSpinner();
	    	spinner.setSelection(conversacionActiva);
	    	mostrarConversacion();
	    	break;
	    
	    }// Fin switch

    	return true;
    }// FIN ONCONTEXTITEMSELECTED
    
    
}// FIN CLASE PRINCIPAL.JAVA