package org.inftel.pasos.controlador;

import org.inftel.pasos.modelo.Modelo;

public class Controlador implements IControlador{

	private Modelo modelo;
	
	public Controlador(Modelo modelo){
		this.modelo = modelo;
	}

	public void setNotifVibrador(Boolean b) {
		this.modelo.setNotifVibracion(b);
	}

	public void setNotifVoz(Boolean b) {
		this.modelo.setNotifVoz(b);
	}

	public void setTema(int t) {
		this.modelo.setTema(t);
	}

	public void setTamTexto(float t) {
		modelo.setTamTexto(t);
	}

	public void aumentarTexto() {
		float t = modelo.getTamTexto();
		modelo.setTamTexto(t+2);
	}

	public void disminuirTexto() {
		float t = modelo.getTamTexto();
		modelo.setTamTexto(t-2);		
	}

	public Boolean getNotifVibracion() {
		return this.modelo.getNotifVibracion();
	}

	public Boolean getNotifVoz() {
		return this.modelo.getNotifVoz();
	}

	public int getTema() {
		return this.modelo.getTema();
	}

	public float getTamTexto() {
		return this.modelo.getTamTexto();
	}
	

}
