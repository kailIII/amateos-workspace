package org.inftel.pasos.controlador;


public interface IControlador {
	
	void setNotifVibrador(Boolean b);
	void setNotifVoz(Boolean b);
	void setTema(int t);
	void setTamTexto(float t);
	void aumentarTexto();
	void disminuirTexto();
	Boolean getNotifVibracion();
	Boolean getNotifVoz();
	int getTema();
	float getTamTexto();
}
