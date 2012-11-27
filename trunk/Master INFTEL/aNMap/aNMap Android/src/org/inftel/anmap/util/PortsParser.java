package org.inftel.anmap.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.inftel.anmap.R;



import android.content.Context;
import android.util.Log;

public class PortsParser {
	
	private static String PORT = "(^[a-z][a-z0-9-]*[ ]+[0-9]+[ ]+(tcp|udp)).*";
	
	
	public static void GetDataPorts (Context context, HashMap<String, String> tcpPorts,HashMap<String, String> udpPorts){
		
		
		InputStream in = context.getResources().openRawResource(
				R.raw.service_names_port_numbers);
		
		String s = null;
		BufferedReader entrada = null;
		
		
		try {
			entrada = new BufferedReader(new InputStreamReader(in));

			Pattern pattern = Pattern.compile(PORT);
			Matcher matcher;
			while ((s = entrada.readLine()) != null) {
				matcher = pattern.matcher(s);
				if (matcher.matches()) {

					String port[] = new String[3];
					port = matcher.group(1).split("\\s+");
					
					if(port[2].equals("tcp")){
						
						if(tcpPorts.containsKey(port[1])==false){
							tcpPorts.put(port[1], port[0]);
						}
					}else if(port[2].equals("udp")){
						if(udpPorts.containsKey(port[1])==false){
							udpPorts.put(port[1], port[0]);
						}
						
					}

				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
		

		try {
			entrada.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
