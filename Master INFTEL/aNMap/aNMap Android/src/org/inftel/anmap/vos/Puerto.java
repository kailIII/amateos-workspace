package org.inftel.anmap.vos;

import java.io.Serializable;

public class Puerto implements Serializable{
	
	private static final long serialVersionUID = -3121334502472331170L;
	public final static int READ = 0;
	public final static int WRITE = 1;
	public final static int READ_WRITE = 2;
	public final static String TCP = "tcp";
	public final static String UDP = "udp";
	
	private int numero;
	private String tipo; // tcp o udp
	private String protocolo; // Especifica el protocolo que hace uso del puerto
	private int rw; // indica si el puerto es de lectura, escritura o ambos
	
	public Puerto(int numero, String protocolo, String tipo, int rw){
		this.numero = numero;
		this.protocolo = protocolo;
		this.tipo = tipo;
		this.rw = rw;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getRw() {
		return rw;
	}

	public void setRw(int rw) {
		this.rw = rw;
	}
}
