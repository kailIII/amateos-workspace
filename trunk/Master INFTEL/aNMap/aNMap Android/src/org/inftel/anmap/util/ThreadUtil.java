package org.inftel.anmap.util;

import android.util.Log;

public class ThreadUtil {

	public static int MAX_HEBRAS = 10;
	public static int hebras = 0;
	private static int numHostsEscaneados = 0;

	public static synchronized void finHebra(){
		hebras --;
	}
	
	public static synchronized void inicioHebra(){
		hebras ++;
	}
	
	public static synchronized int getHebras(){
		return hebras;
	}
	
	public static synchronized int getEscaneados(){
		return numHostsEscaneados;
	}
	
	public static synchronized void sumarEscaneados(){
		numHostsEscaneados ++;
	}
	
	public static synchronized void restarEscaneados(){
		numHostsEscaneados --;
	}
}
