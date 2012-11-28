package org.inftel.socialwind.client.desktop.requestfactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class StreamUtils {

    public static String readStreamAsString(InputStream in) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int count;
            do {
                count = in.read(buffer);
                if (count > 0) {
                    out.write(buffer, 0, count);
                }
            } while (count >= 0);
            return out.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("La maquina JVM no soporta codificación UTF-8", e);
        } catch (IOException e) {
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException ignored) {
            }
        }
    }

}
