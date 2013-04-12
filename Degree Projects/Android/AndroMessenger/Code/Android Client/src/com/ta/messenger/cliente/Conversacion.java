package com.ta.messenger.cliente;

public class Conversacion {
	
	String contacto;
	String apodo;
	String ip;
	String conversacion;
	
	//Constructor
	Conversacion(String contacto_, String apodo_, String ip_){
		this.contacto = contacto_;
		this.apodo = apodo_;
		this.ip = ip_;
		this.conversacion = "";
	}

	public void agregarTextoAConversacion(String apodo, String texto){
		this.conversacion += "\n" + apodo + " dice: " + texto;
	}
}
