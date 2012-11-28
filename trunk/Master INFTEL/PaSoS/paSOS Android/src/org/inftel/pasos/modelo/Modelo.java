package org.inftel.pasos.modelo;

import java.io.Serializable;
import java.util.Observable;

import org.inftel.pasos.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Modelo extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;

	private Boolean notifVibracion;
	private Boolean notifVoz;
	private int tema;
	private float tamTexto;
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;

	public Modelo(Context ctx) {
		prefs = ctx.getSharedPreferences("prefs", Activity.MODE_PRIVATE);
		editor = prefs.edit();

		notifVibracion = prefs.getBoolean("vib", true);
		notifVoz = prefs.getBoolean("voz", true);
		tema = prefs.getInt("tema", R.style.tema1);
		tamTexto = prefs.getFloat("tam", 20);
	}

	public Boolean getNotifVibracion() {
		return notifVibracion;
	}

	public float getTamTexto() {
		return tamTexto;
	}

	public void setTamTexto(float tamTexto) {
		this.tamTexto = tamTexto;
		editor.putFloat("tam", tamTexto);
		if (editor.commit()) {
			setChanged();
			notifyObservers(this);
		}
	}

	public void setNotifVibracion(Boolean notifVibracion) {
		this.notifVibracion = notifVibracion;
		editor.putBoolean("vib", notifVibracion);
		if (editor.commit()) {
			setChanged();
			notifyObservers(this);
		}
	}

	public Boolean getNotifVoz() {
		return notifVoz;
	}

	public void setNotifVoz(Boolean notifVoz) {
		this.notifVoz = notifVoz;
		editor.putBoolean("voz", notifVoz);
		if (editor.commit()) {
			setChanged();
			notifyObservers(this);
		}
	}

	public int getTema() {
		return tema;
	}

	public void setTema(int tema) {
		this.tema = tema;
		editor.putInt("tema", tema);
		if (editor.commit()) {
			setChanged();
			notifyObservers(this);
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
