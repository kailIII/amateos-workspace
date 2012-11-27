package org.inftel.anmap.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.inftel.anmap.vos.Host;
import org.inftel.anmap.vos.LocalHost;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class RedUtil {
	private static final String TAG = "RedUtil";

	public static final String NOMAC = "00:00:00:00:00:00";
	private final static String MAC_RE = "^%s\\s+0x1\\s+0x2\\s+([:0-9a-fA-F]+)\\s+\\*\\s+\\w+$";
	private static int timeout;
	private static SharedPreferences prefs;

	
	
	public static Host findHost(Context ctx, String ip) {
		String respuesta = "";
		Host host = null;

		InetAddress ia = null;
		try {
			ia = InetAddress.getByName(ip);
			Log.d(TAG, "InetAddress: " + ia);

		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}

		// Se comprueba que se ha encontrado un host
		Boolean reacheable = false;
		
		try {
			prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
			timeout = prefs.getInt("timeout_red", 500);
			Log.d("prefs", "TIME OUT RED -> "+timeout);
			reacheable = ia.isReachable(timeout);
			Log.d(TAG,"REACHEABLE -> "+reacheable);
		} catch (IOException e) {
			Log.d(TAG,"La IP no es alcanzable");
			//e.printStackTrace();
		}
		
		
		if (ia != null && reacheable) {

			LocalHost localHost = LocalHost.getInstance(ctx);

			// Se comprueba que la ip no es la del localhost
			if (!ip.equals(localHost.getIp())) {

				host = new Host(ip);

				if (host.getIp().equals(localHost.getGatewayIp())) {
					host.setRouter(true);
				} else {
					host.setRouter(false);
				}
				Log.d(TAG, "ROUTER: " + host.getRouter());

				host.setHostname(ia.getHostName());
				Log.d(TAG, "HOSTNAME: " + host.getHostname());
				host.setMac(getMAC(ip));
				Log.d(TAG, "MAC: " + host.getMac());
				host.setFabricante(OUIparser.getManufacturer(host.getMac()));
				Log.d(TAG,"FABRICANTE: "+host.getFabricante());
			}

		}

		return host;

	}

	/**
	 * Obtiene la dirección MAC de un dispositivo a partir de la dirección IP.
	 * Para ello se hace uso de la función nativa de Linux arp
	 * 
	 * @param ip
	 * @return
	 */
	public static String getMAC(String ip) {
		String mac = NOMAC;
		try {
			if (ip != null) {
				String ptrn = String.format(MAC_RE, ip.replace(".", "\\."));
				Pattern pattern = Pattern.compile(ptrn);
				BufferedReader bufferedReader = new BufferedReader(
						new FileReader("/proc/net/arp"), 8 * 1024);
				String line;
				Matcher matcher;
				while ((line = bufferedReader.readLine()) != null) {
					matcher = pattern.matcher(line);
					if (matcher.matches()) {
						mac = matcher.group(1);
						break;
					}
				}
				bufferedReader.close();
			} else {
				Log.e(TAG, "ip is null");
			}
		} catch (IOException e) {
			Log.e(TAG, "Can't open/read file ARP: " + e.getMessage());
			return mac;
		}
		return mac;
	}

	/**
	 * Obtiene la dirección IP en formato String a partir de un entero con signo
	 * 
	 * @param ip_int
	 * @return
	 */
	public static String getIpFromIntSigned(int ip_int) {
		String ip = "";
		for (int k = 0; k < 4; k++) {
			ip = ip + ((ip_int >> k * 8) & 0xFF) + ".";
		}
		return ip.substring(0, ip.length() - 1);
	}
}
