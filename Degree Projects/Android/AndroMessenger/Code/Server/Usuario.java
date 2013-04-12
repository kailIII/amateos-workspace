import java.util.Vector;


public class Usuario {
	
	String nombre;
	String password;
	String apodo;
	String IP;
	Boolean conectado;
	String contactos;

	//Constructor
	public Usuario (String nombre_, String password_, String apodo_, String IP_){
		this.nombre = nombre_;
		this.password = password_;
		this.apodo = apodo_;
		this.IP = IP_;
		this.conectado = false;
		this.contactos = ";";
	}
	
	public Usuario (String nombre_, String password_, String apodo_){
		this.nombre = nombre_;
		this.password = password_;
		this.apodo = apodo_;
		this.conectado = false;
		this.contactos = ";";
	}
	
	// Añade un contacto a la lista de contactos
	public void agregarContacto (String contacto){
		
		this.contactos += contacto + ";";
	}// Fin agregarContacto
	
	
	// Establece el estado de conexión del Usuario
	public void setConectado (Boolean estado){
		
		this.conectado = estado;
		
	}//Fin setConectado
	
}
