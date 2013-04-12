import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.*;

public class xml {
  
	  /** Método que imprime el DOM de un documento XML */
	  public static void imprimirDOM(Node node) {
	    
		int type = node.getNodeType();
	    
		
		switch (type) {
		
		    // Impresión del elemento DOCUMENTO
		    case Node.DOCUMENT_NODE: {
		      System.out.println("&lt;?xml version=\"1.0\" ?>");
		      imprimirDOM(((Document)node).getDocumentElement());
		      break;
		    } //FIN case Node.DOCUMENT_NODE:
	
		      // Impresión del elemento con Atributos
		    case Node.ELEMENT_NODE: {
		      System.out.print("<");
		      System.out.print(node.getNodeName());
		      NamedNodeMap attrs = node.getAttributes();
		      for (int i = 0; i < attrs.getLength(); i++) {
		        Node attr = attrs.item(i);
		        System.out.print(" " + attr.getNodeName().trim() +
		                         "=\"" + attr.getNodeValue().trim() +
		                         "\"");
		      }
		      System.out.println(">");
	
		      NodeList children = node.getChildNodes();
		      if (children != null) {
		        int len = children.getLength();
		        for (int i = 0; i < len; i++)
		          imprimirDOM(children.item(i));
		      }
	
		      break;
		    }// FIN case Node.ELEMENT_NODE
	
		    // Gestión nodos referencia
		  case Node.ENTITY_REFERENCE_NODE: {
		    System.out.print("&");
		    System.out.print(node.getNodeName().trim());
		    System.out.print(";");
		    break;
		  }// FIN case Node.ENTITY_REFERENCE_NODE
		  	
		  //Impresión nodos CDATA
		  case Node.CDATA_SECTION_NODE: {
			System.out.print("<![CDATA[");
			System.out.print(node.getNodeValue().trim());
			System.out.print("]]>");
			break;
		  }// FIN case Node.CDATA_SECTION_NODE
		
			// Impresión texto
		  case Node.TEXT_NODE: {
			System.out.print(node.getNodeValue().trim());
			break;
		  }// FIN case Node.TEXT_NODE
		
		  // Impresión instrucción procesamiento
		  case Node.PROCESSING_INSTRUCTION_NODE: {
			System.out.print("<?");
			System.out.print(node.getNodeName().trim());
			String data = node.getNodeValue().trim(); {
			  System.out.print(" ");
			  System.out.print(data);
			}
			System.out.print("?>");
			break;
		  }// FIN case Node.PROCESSING_INSTRUCTION_NODE
	  }// FIN SWITCH

	    if (type == Node.ELEMENT_NODE) {
	      System.out.println();
	      System.out.print("</");
	      System.out.print(node.getNodeName().trim());
	      System.out.print('>');
	    }
	  }// FIN IMPRIMIR DOM
	  

	  
	public static void main(String[] args) {
		Document xml=null;
		
		  if(args.length != 1)
		  {
			 System.err.println("Introduce como parámetro la URL del XML");
		  	return;
		  }
		  
		  // Se comprueba que el XML está bien formado
		  try
		  {
			  DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			  DocumentBuilder domBuilder = domFactory.newDocumentBuilder();
			  xml = domBuilder.parse(args[0]);
			  System.out.println("El XML está bien formado.");
		  }
		  catch(org.xml.sax.SAXException exp)
		  {
			  System.out.println("El XML no está bien formado!!");
		  }
		  catch(FactoryConfigurationError exp)
		  {
			  System.err.println(exp.toString());
		  }
		  catch(ParserConfigurationException exp)
		  {
			  System.err.println(exp.toString());
		  }
		  catch(Exception exp)
		  {
			  System.err.println(exp.toString());
		  }
		  
		  //Impresión del DOM
		  System.out.println("***** IMPRESIÓN DEL DOM *****");
		  imprimirDOM(xml);
	}// FIN MAIN
		

} // FIN CLASE
