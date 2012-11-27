package org.inftel.anmap.vos;

import org.inftel.anmap.util.RedUtil;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class LocalHost {

	private final static String TAG = "LocalHost";
	private static LocalHost instancia = null;
	
	private String ssid;
	private String bssid;
	private String ip;
	private String mac;
	private String fabricante;
	private int speed;
	
	private String gatewayIp;
	private String netmaskIp;
	private int rssi;
	

	private LocalHost(Context ctx) {
		
		WifiManager wifi = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            ip = RedUtil.getIpFromIntSigned(info.getIpAddress());
            speed = info.getLinkSpeed();
            rssi = info.getRssi();

            ssid = info.getSSID();
            bssid = info.getBSSID();
            mac = info.getMacAddress();
            gatewayIp = RedUtil.getIpFromIntSigned(wifi.getDhcpInfo().gateway);
            netmaskIp = RedUtil.getIpFromIntSigned(wifi.getDhcpInfo().netmask);
            
            Log.d(TAG,"OBTENIDOS DATOS WIFI");
            Log.d(TAG,"speed -> "+speed);
            Log.d(TAG,"rssi -> "+rssi);

            Log.d(TAG,"ssid -> "+ssid);
            Log.d(TAG,"bssid -> "+bssid);
            Log.d(TAG,"ip -> "+ip);
            Log.d(TAG,"mac -> "+mac);
            Log.d(TAG,"gateway -> "+gatewayIp);
            Log.d(TAG,"netmask -> "+netmaskIp);
            
        }
		
	}

	public static synchronized LocalHost getInstance(Context ctx) {
		if (instancia == null) {
			instancia = new LocalHost(ctx);
		}
		return instancia;
	}

	public static LocalHost getInstancia() {
		return instancia;
	}

	public static void setInstancia(LocalHost instancia) {
		LocalHost.instancia = instancia;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public String getBssid() {
		return bssid;
	}

	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String getGatewayIp() {
		return gatewayIp;
	}

	public void setGatewayIp(String gatewayIp) {
		this.gatewayIp = gatewayIp;
	}

	public String getNetmaskIp() {
		return netmaskIp;
	}

	public void setNetmaskIp(String netmaskIp) {
		this.netmaskIp = netmaskIp;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public static String getTag() {
		return TAG;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}
	
	
	
}