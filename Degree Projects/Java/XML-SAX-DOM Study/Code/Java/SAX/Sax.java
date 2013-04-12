import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Sax extends org.xml.sax.HandlerBase {
	  
	
	  public static void main(String[] args) throws IOException, SAXException,
	      ParserConfigurationException {
	    // Crea el JAXP "parser factory" para crear parsers SAX
	    javax.xml.parsers.SAXParserFactory spf = SAXParserFactory.newInstance();

	    // Configura el parser factory para el tipo de parser requerido: sin validación
	    spf.setValidating(false); 

	    // Ahora se usa el parser factory para crear un objeto SAXParser
	    javax.xml.parsers.SAXParser sp = spf.newSAXParser();

	    // Crea un input source de SAX para el archivo que se le pasa como entrada al programa
	    org.xml.sax.InputSource input = new InputSource(new FileReader(args[0]));

	    // Se pasa al InputSource una URI absoluta del archivo
	    input.setSystemId("file://" + new File(args[0]).getAbsolutePath());

	    // Creación del objeto instancia de la clase
	    Sax handler = new Sax();

	    // Se indica al parser la entrada y el handler
	    sp.parse(input, handler);
	  }

	  // Cada vez que el parser encuentra una etiqueta de inicio de un elemento se invoca este método
	  public void startElement(String namespaceURI, String localName, String qName)throws SAXException {
	  
		  System.out.println("Etiqueta inicio: "+localName);
	  }

	  // Cada vez que el parser encuentra una etiqueta de fin de un elemento se invoca este método
	  public void endElement(String name) {
		  System.out.println("Encontrada etiqueta fin: "+name);
	  }

	}