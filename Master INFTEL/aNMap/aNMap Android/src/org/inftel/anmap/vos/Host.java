package org.inftel.anmap.vos;

import java.io.Serializable;
import java.util.ArrayList;

public class Host implements Serializable {
	
	private static final long serialVersionUID = -5735572240214688960L;
	private Boolean router;
	private String ip;
	private String hostname;
	private String mac;
	private String fabricante;
	private ArrayList<Puerto> puertos;

	public ArrayList<Puerto> getPuertos() {
		return puertos;
	}

	public void setPuertos(ArrayList<Puerto> puertos) {
		this.puertos = puertos;
	}

	public Host() {
	}

	public Host(String ip) {
		this.ip = ip;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public Boolean getRouter() {
		return router;
	}

	public void setRouter(Boolean router) {
		this.router = router;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
