
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

public class OUIparser {

    private static InputStream in;
    private static String s;
    private static BufferedReader entrada;
    private static InputStreamReader inputStreamReader;
    private static HashMap<String, String> macManufacturer;

    private static Pattern pattern;
    private static Matcher matcher;

    private final static String MAC_PATTERN = "^([0-9a-fA-F][0-9a-fA-F]-){2}([0-9a-fA-F][0-9a-fA-F]).*";
    private final static String SPLIT_PATTERN = "(\\s)+(\\(hex)(\\))(\\s)+";

    public static void initialize(Context context) {

        in = context.getResources().openRawResource(R.raw.oui);
        s = null;
        entrada = null;
        inputStreamReader = null;

        macManufacturer = new HashMap<String, String>();

        try {
            inputStreamReader = new InputStreamReader(in);
            entrada = new BufferedReader(inputStreamReader);

            String[] trozos = null;

            pattern = Pattern.compile(MAC_PATTERN);

            while ((s = entrada.readLine()) != null) {
                matcher = pattern.matcher(s);
                if (matcher.matches()) {
                    trozos = s.split(SPLIT_PATTERN);
                    macManufacturer.put(trozos[0].replace("-", ":"), trozos[1]);
                }
            }

            entrada.close();
            inputStreamReader.close();
            in.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }

    }

    public static String getManufacturer(String MAC) {
        return macManufacturer.get(MAC.substring(0, 8).toUpperCase());
    }

}
