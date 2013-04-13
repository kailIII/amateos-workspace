#!/usr/bin/ruby
# encoding: UTF-8

require 'rubygems'
require 'firewatir'
require 'nokogiri'
require 'uri'
require 'RestClient'
require 'rexml/document' 
require 'tree'
include REXML 


#####################################################################################
########################### DEFINICIÓN DE FUNCIONES #################################
#####################################################################################

### Devuelve el esquema del menú de navegación de la web en forma de árbol a partir del archivo rutasMenu.xml
### Para acceder a los elementos se puede hacer asi: menu[i][j][k].content (se puede navegar fácilmente por los hijos)
def getMenu()
	xml = Document.new(File.new("rutasMenu.xml"))
	menu = Tree::TreeNode.new("Menu", "Menu Principal")

	indicePrincipal = 0
	indiceSecundario = 0
	indiceTerciario = 0

	#Recorrer menu principal
	xml.elements.each("menuPrincipal/itemMenuPrincipal"){ |itemPrincipal| 
		menu << Tree::TreeNode.new(indicePrincipal,itemPrincipal.attribute("name").to_s)
	
		#Recorrer menu secundario
		itemPrincipal.elements.each("itemSecundario"){ |itemSecundario|
		padre = menu[indicePrincipal]
		item = itemSecundario.attribute("name").to_s	  
		padre << Tree::TreeNode.new(indiceSecundario, item)
	
		#Recorrer menu terciario
		itemSecundario.elements.each{ |itemTerciario|
			abuelo = menu[indicePrincipal]
			padre = abuelo[indiceSecundario]
			item = itemTerciario.text	    
			padre << Tree::TreeNode.new(indiceTerciario, item)
	    
			indiceTerciario += 1
		}
		indiceSecundario += 1
		indiceTerciario = 0
		}
	indicePrincipal += 1
	indiceSecundario = 0
	}

	return menu
end

### Obtiene las marcas que tienen más de una palabra junto con el número de palabras adicionales que componen el nombre de la marca
### a partir del archivo excepcionesMarcas.xml De esta forma se podrá obtener el nombre completo de una marca al hacer el scraping
def getMarcasConflictivas()
  	xml = Document.new(File.new("excepcionesMarcas.xml"))
	return xml
end

### Obtiene la marca del producto a partir de su descripcion. Para ello tiene en cuenta aquellas marcas cuyo nombre de marca
### conste de más de una palabra a través del XML excepcionesMarcas
def getMarcaYDescripcion(excepcionesMarcas, tipo, desc)
  aux = desc.scan(/\w+/) #separa las palabras de la descripcion
  palabrasAdicionales = comprobarMarca(excepcionesMarcas,tipo,aux[0])
  i = 0
  marca = ""
  descripcion = ""
  while(i<aux.length)
    # Obtiene la marca completa
    if i<=(palabrasAdicionales)
      if i==0
        marca << aux[i].to_s
      else
        marca << " " << aux[i].to_s 
      end
      
    # Las demás palabras pertenecen a la descripción
    else
      descripcion << aux[i].to_s << " "
    end
    i += 1
  end
  return marca, descripcion
end

### Verifica si la marca obtenida tiene más de una palabra, devolviendo el número de palabras adicionales que hay que obtener para
### formar el nombre completo de la marca
def comprobarMarca(excepcionesMarcas,tipo,marca)
	palabrasAdicionales = 0
	
	# Se recorre el XML excepcionesMarcas para comprobar si la marca en cuestión tiene más de una palabra
	excepcionesMarcas.elements.each("excepciones"){|excepcion|
	    excepcion.elements.each("categoria"){|categoria|
  	        if tipo == categoria.attribute("name").to_s #si es la categoría que coincide con el producto
          	    categoria.elements.each("excepcion"){|excepcion|
              		if marca == excepcion.attribute("marca").to_s
              		    #se obtiene el número de palabras adicionales de que consta el nombre de la marca
              			palabrasAdicionales = excepcion.text.to_i
              		end
              	}
            end
      	}
	}
	return palabrasAdicionales
end

### Hace click en los menús hasta llegar a la subcategoría 3 (ahí es donde ya se ven los productos)
### Los parámetros contienen el texto que se muestra en cada uno de los 3 menús donde hay que realizar los clicks
def navegacion(navegador,menu1, menu2, menu3)
	submenu1 = navegador.span(:text,menu1)
	submenu1.fire_event("onclick")
	submenu2 = navegador.span(:text,menu2)
	submenu2.fire_event("onclick")
	submenu3 = navegador.span(:text,menu3)
	submenu3.fire_event("onclick")
	
	sleep(1)## Espera 1 segundos para que la página se cargue completamente antes de realizar el scraping
  
end

### Devuelve el objeto Nokogiri que contiene la página de la que extraer los datos de los productos.
def cambiarSupermercado (navegador,supermercado)
	
	divSupermercado = navegador.div(:id,"supermercadoSeleccionado")
	divSupermercado.fire_event("onclick")
	link = navegador.link(:super_id,supermercado)
	link.fire_event("onclick")
	sleep(0.5)
	
	link2 = navegador.div(:super_id,supermercado)
	link2.fire_event("onclick")
	
	sleep(1)## Espera 1 segundos para que la página se cargue completamente antes de realizar el scraping
	
	pagina = Nokogiri::HTML.parse(navegador.html)
	return pagina
end

### Método que obtiene los datos de los productos
def scrap (pagina,tipoProducto,excepcionesMarcas)
  datos = []
  fila = 1
	while fila <6 #Solo hay 5 filas de productos por página como máximo
		columna = 1
		
		## Para tipo de datos 1 -> Ejemplo LATAS DE CERVEZA
		loop do
			descripcionInicio = pagina.xpath(".//*[@id='lista_cms']/div/div[2]/div/div[#{fila}]/div[#{columna}]/div[2]/div[3]/a").inner_text
			break unless not descripcionInicio.empty? # Si la descripción obtenida está vacía -> no hay más productos
			precio_entero = pagina.xpath(".//*[@id='lista_cms']/div/div[2]/div/div[#{fila}]/div[#{columna}]/div[2]/div[1]/span[1]").inner_text
			precio_decimal = pagina.xpath(".//*[@id='lista_cms']/div/div[2]/div/div[#{fila}]/div[#{columna}]/div[2]/div[1]/span[2]").inner_text
			precio_relativo = pagina.xpath(".//*[@id='lista_cms']/div/div[2]/div/div[#{fila}]/div[#{columna}]/div[2]/div[2]/span").inner_text
			imagen = pagina.xpath(".//*[@id='lista_cms']/div/div[2]/div[1]/div[#{fila}]/div[#{columna}]/div[1]/a/img")
			imagen = obtenerUrlImagen(imagen.to_s)
			#Se eliminan los saltos de linea y tabulaciones que aparecen al inicio de cada descripción
			aux = descripcionInicio.partition("\t\t\t")
			marca, descripcion = getMarcaYDescripcion(excepcionesMarcas,tipoProducto,aux[2])
			datos.push([marca,descripcion, precio_entero << precio_decimal,precio_relativo,imagen])
			columna += 1
		end
		
		columna = 1
		## Para tipo de datos 2 -> Ejemplo BOTELLINES DE CERVEZA
		loop do
			descripcionInicio = pagina.xpath(".//*[@id='lista_cms']/div/div[3]/div/div[#{fila}]/div[#{columna}]/div[2]/div[3]/a").inner_text
			break unless not descripcionInicio.empty?
			precio_entero = pagina.xpath(".//*[@id='lista_cms']/div/div[3]/div/div[#{fila}]/div[#{columna}]/div[2]/div[1]/span[1]").inner_text
			precio_decimal = pagina.xpath(".//*[@id='lista_cms']/div/div[3]/div/div[#{fila}]/div[#{columna}]/div[2]/div[1]/span[2]").inner_text
			precio_relativo = pagina.xpath(".//*[@id='lista_cms']/div/div[3]/div/div[#{fila}]/div[#{columna}]/div[2]/div[2]/span").inner_text
			imagen = pagina.xpath(".//*[@id='lista_cms']/div/div[3]/div[1]/div[#{fila}]/div[#{columna}]/div[1]/a/img")
			imagen = obtenerUrlImagen(imagen.to_s)
			#Se eliminan los saltos de linea y tabulaciones que aparecen al inicio de cada descripción
			aux = descripcionInicio.partition("\t\t\t")
			marca, descripcion = getMarcaYDescripcion(excepcionesMarcas,tipoProducto,aux[2])
			datos.push([marca,descripcion, precio_entero << precio_decimal,precio_relativo,imagen])
			columna += 1
		end
	fila += 1
	end
  return datos
end

### Crea un archivo XML con las etiquetas "categoría" como padre
def crearXML(categoria)
    categInic = '<'<<categoria<<'>'
    categFin = '</'<<categoria<<'>'
    xml = Nokogiri::XML::DocumentFragment.parse <<-EOXML 
#{categInic}#{categFin}
EOXML
   return xml
end

### Agrega un Item con marca, precio y descripción a un XML.
def agregarItemXML (xml, marca, descripcion, precio, precio_relativo, imagen)
    producto = Nokogiri::XML::Node.new('producto',xml)
    producto.parent = xml.child
    m = Nokogiri::XML::Node.new('marca',xml)
    m.content = marca
    m.parent = producto
    d = Nokogiri::XML::Node.new('descripcion',xml)
    d.content = descripcion
    d.parent = producto
    p = Nokogiri::XML::Node.new('precio',xml)
    p.content = precio
    p.parent = producto
    pr = Nokogiri::XML::Node.new('precio_relativo',xml)
    pr.content = precio_relativo
    pr.parent = producto
    img = Nokogiri::XML::Node.new('imagen',xml)
    img.content = imagen
    img.parent = producto
    return xml
end

### Obtiene la URL de una imagen a partir del texto resultado del scrap (img class="imagen_pm_central_carrito" alt="" src="http://URL.jpg">)
def obtenerUrlImagen(cadena)
  
   if cadena.empty?
       return 'Sin imagen'
   else
       extrac = cadena[/http*.*[^">]/] #devuelve sólo el URL de la imagen
       return extrac
   end
end

### Envía un XML a la ruta especificada de la base de datos eXist mediante los parámetros introducidos
def agregarXMLaDB(xml,menu1, menu2, menu3, supermercado)
    url = URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/marketscraper/"<<menu1.to_s<<"/"<<menu2.to_s<<"/"<<menu3.to_s<<"/"<<supermercado.to_s<<".xml")
    puts 'Guardado XML en -> .../db/marketscraper/'<<menu1.to_s<<"/"<<menu2.to_s<<"/"<<menu3.to_s<<"/"<<supermercado.to_s<<".xml"
    RestClient.put url, xml.to_xml, {'Content-Type' => 'application/xml'} 
end

### Envía un XML a la ruta especificada de la base de datos eXist mediante los parámetros introducidos
def agregarXMLaDB2(xml, supermercado)
    url = URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/marketscraper/supermercados/"<<supermercado.to_s<<"/"<<supermercado.to_s<<".xml")
    puts 'Guardado XML en -> .../db/marketscraper/supermercados/'<<supermercado.to_s<<"/"<<supermercado.to_s<<'.xml'
    RestClient.put url, xml.to_xml, {'Content-Type' => 'application/xml'} 
end

#####################################################################################
################################# EJECUCIÓN #########################################
#####################################################################################

#Almacena los supermercados junto con el ID que se utiliza para identificarlos en la web
supermercados = {"Mercadona",4,"Hipercor",7,"Carrefour",6,"CorteIngles",5}	

# Obtención de los datos de los ficheros XML
menu = getMenu()
marcasConflictivas = getMarcasConflictivas()

# Inicialización y redirección del navegador
browser = Watir::Browser.new
browser.goto("http://www.carritus.com/carrito/index")

# Creamos los XML que contendrán la lista de todos los productos de cada supermercado
productosMercadona = crearXML('Mercadona')
productosCarrefour = crearXML('Carrefour')
productosCorteIngles = crearXML('CorteIngles')
productosHipercor = crearXML('Hipercor')
xmlsSupermercados = {'Mercadona'=>productosMercadona, 'Carrefour'=>productosCarrefour, 'CorteIngles'=>productosCorteIngles, 'Hipercor'=>productosHipercor}

## BUCLE PRINCIPAL EN EL QUE SE REALIZA EL SCRAPING
## Se recorre el árbol de menúes para ir navegando por las diferentes categorías y realizando el scraping de todos los productos
cat1 = 0
while (cat1 < menu.children.length)
    opcionMenu1 = Regexp.new(menu[cat1].content)
    puts "\n\n ***** Scrap -> opción menú 1: "<<menu[cat1].content.to_s<<' *****'
    
    cat2 = 0
    while (cat2 < menu[cat1].children.length)
    opcionMenu2 = Regexp.new(menu[cat1][cat2].content)
    puts "\t^^^^^ Scrap -> opción menú 2: "<<menu[cat1][cat2].content.to_s<<' ^^^^^'
        
        cat3 = 0
        while (cat3 < menu[cat1][cat2].children.length)
            opcionMenu3 = Regexp.new(menu[cat1][cat2][cat3].content)
            puts "\t\t+++++ Scrap -> opción menú 3: "<<menu[cat1][cat2][cat3].content.to_s<< '+++++'

            #Se realiza la navegación hasta la categoría correspondiente
            navegacion(browser,opcionMenu1,opcionMenu2,opcionMenu3)

            # Se recorren los diferentes supermercados
            supermercados.each do |nombreSupermercado|
                puts "\t\t\t----- "<<nombreSupermercado[0].to_s<<" -----"
  
              # Se crea el XML donde se van a guardar los productos y sus datos
              xml = crearXML(nombreSupermercado[0].to_s)
              # Se hace el scraping de la página
              datos = scrap(cambiarSupermercado(browser,nombreSupermercado[1]),menu[cat1][cat2].content.to_s,marcasConflictivas)
              
              # Se recorren los datos obtenidos y se van guardando en los xml correspondientes
              datos.each do |item|
                xml = agregarItemXML(xml, item[0], item[1], item[2], item[3], item[4])
                xmlsSupermercados[nombreSupermercado[0]] = agregarItemXML(xmlsSupermercados[nombreSupermercado[0]],  item[0], item[1], item[2], item[3], item[4])                
              end
              
              # Se realiza el guardado del xml de la categoría en la base de datos
              agregarXMLaDB(xml,menu[cat1].content, menu[cat1][cat2].content, menu[cat1][cat2][cat3].content, nombreSupermercado[0])
            
            end
            
            cat3 += 1
        end #### FIN BUCLE CAT3
        
        cat2 += 1 
    end #### FIN BUCLE CAT2
    
    cat1 += 1
end #### FIN BUCLE CAT1

# Se guardan en la base de datos los XML que contienen todos los productos de cada supermercado
agregarXMLaDB2(productosMercadona,'Mercadona')
agregarXMLaDB2(productosCarrefour,'Carrefour')
agregarXMLaDB2(productosHipercor,'Hipercor')
agregarXMLaDB2(productosCorteIngles,'CorteIngles')