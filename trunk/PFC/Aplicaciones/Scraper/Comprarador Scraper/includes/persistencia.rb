require "rubygems"
require "java"
require "net/http"
require "uri"
require "nokogiri"
require "includes/Producto"
require "logger"
require "json"

#################################################################################
### FUNCIONES DE LLAMADAS A BASE DE DATOS
#################################################################################

### Función guardarProducto: Realiza la petición al servidor para almacenar el producto 
### en la base de datos
########################################################################################
def guardarProducto(p)

  case $tipoServidor 
  when 0 then ### SERVIDOR TIPO -> REST  

    servidor = "#{$urlServidorREST}/productos/"

    begin
      # se codifica la petición a formato URI
      url = URI.parse(URI.encode(servidor))
    rescue Exception => e
      manejarExcepcion(e,"Error al crear URL para guardado de producto")
    end

    begin
      # Petición POST
      http = Net::HTTP.new(url.host, url.port)
      request = Net::HTTP::Post.new(url.path)
      request.content_type = 'application/xml'
      request.body = productoToXml(p)
      response = http.request(request)            
      $logCategorias.debug "------ #{p.descripcion} -> #{response.body}"
    rescue Exception => e
      manejarExcepcion(e,"Error al realizar petición para guardado de producto")
    end

  when 1 then ### SERVIDOR TIPO -> WEB
    # ruta base de la petición
    ruta = "#{$urlServidorWeb}/guardarProducto.php"

    begin
      # parámetros de la petición
      parametros = "?m="<<p.marca<<"&d="<<p.descripcion<<"&f="<<p.formato<<"&im="<<p.imagen
      parametros = parametros<<"&cat="<<p.categoria.to_s<<"&subcat="<<p.subcategoria.to_s<<"&subsubcat="<<p.subsubcategoria.to_s
      parametros = parametros<<"&merc="<<p.precio_mercadona.to_s<<"&carr="<<p.precio_carrefour.to_s
      parametros = parametros<<"&hip="<<p.precio_hipercor.to_s<<"&cort="<<p.precio_corteIngles.to_s
      parametros = parametros<<"&merc_rel="<<p.precio_relativo_mercadona.to_s
      parametros = parametros<<"&carr_rel="<<p.precio_relativo_carrefour.to_s
      parametros = parametros<<"&hip_rel="<<p.precio_relativo_hipercor.to_s
      parametros = parametros<<"&cort_rel="<<p.precio_relativo_corteIngles.to_s
      parametros = parametros<<"&of_merc="<<p.oferta_mercadona.to_s
      parametros = parametros<<"&of_carr="<<p.oferta_carrefour.to_s
      parametros = parametros<<"&of_hip="<<p.oferta_hipercor.to_s
      parametros = parametros<<"&of_cort="<<p.oferta_corteIngles.to_s
    rescue Exception => e
      manejarExcepcion(e,"Error al obtener los parámetros del producto para formar la URL.")
    end



    begin
      # se codifica la petición a formato URI
      uri = URI.parse(URI.encode(ruta<<parametros))
    rescue Exception => e
      manejarExcepcion(e,"Error al crear URL para guardado de producto")
    end

    begin
      # Petición GET
      respuesta = Net::HTTP.get_response(uri)
      $logCategorias.debug "------ #{p.descripcion} -> #{respuesta.body}"
    rescue Exception => e
      manejarExcepcion(e,"Error al realizar petición para guardado de producto")
    end

  end
end

### Función guardarCategoria: Realiza la petición al servidor para almacenar una categoria 
### en la base de datos
########################################################################################
def guardarCategoria(nombre, posicion)

  case $tipoServidor 
  when 0 then ### SERVIDOR TIPO -> REST

    servidor = "#{$urlServidorREST}/categorias/"

    begin
      # se codifica la petición a formato URI
      url = URI.parse(URI.encode(servidor))
    rescue Exception => e
      manejarExcepcion(e,"Error al crear URL para guardado de producto")
    end

    begin
      #Se genera el XML
      builder = Nokogiri::XML::Builder.new do
        categorias {
          nombre nombre
          posicion posicion
        }
      end

      # Petición POST
      http = Net::HTTP.new(url.host, url.port)
      request = Net::HTTP::Post.new(url.path)
      request.content_type = 'application/xml'
      request.body = builder.to_xml
      response = http.request(request)            
      $logMenu.debug "Categ: #{nombre} -> #{response.body}"
      $logMenu.debug "#"        
    rescue Exception => e
      manejarExcepcion(e,"Error al realizar petición para guardado de categoria")
    end

  when 1 then ### SERVIDOR TIPO -> WEB

    # ruta base de la petición
    ruta = "#{$urlServidorWEB}/guardarCategoria.php"

    begin
      # parámetros de la petición
      parametros = "?nombre="<<nombre<<"&posicion="<<posicion
    rescue Exception => e
      manejarExcepcion(e,"Error al formar URL para guardado de categoría.")
    end

    begin
      # se codifica la petición a formato URI
      uri = URI.parse(URI.encode(ruta<<parametros))
    rescue Exception => e
      manejarExcepcion(e,"Error al crear URL para guardado de categoria")
    end

    begin
      # Petición GET
      respuesta = Net::HTTP.get_response(uri)
      $logMenu.debug "Categ: #{nombre} -> #{respuesta.body}"
      $logMenu.debug "#"
    rescue Exception => e
      manejarExcepcion(e,"Error al realizar petición para guardado de categoria")
    end
  end
end

### Función guardarSubcategoria: Realiza la petición al servidor para almacenar una 
### subcategoria en la base de datos
########################################################################################
def guardarSubcategoria(nombre, posicion, categoria)


  case $tipoServidor 
  when 0 then ### SERVIDOR TIPO -> REST

    servidor = "#{$urlServidorREST}/subcategorias/"

    begin
      # se codifica la petición a formato URI
      url = URI.parse(URI.encode(servidor))
    rescue Exception => e
      manejarExcepcion(e,"Error al crear URL para guardado de producto")
    end

    begin
      #Se genera el XML
      builder = Nokogiri::XML::Builder.new do
        subcategorias {
          nombre nombre
          posicion posicion
          categoria categoria
        }
      end

      # Petición POST
      http = Net::HTTP.new(url.host, url.port)
      request = Net::HTTP::Post.new(url.path)
      request.content_type = 'application/xml'
      request.body = builder.to_xml
      response = http.request(request)            
      $logMenu.debug "#  Subcateg: #{nombre} -> #{response.body}"
      $logMenu.debug "#  *"
    rescue Exception => e
      manejarExcepcion(e,"Error al realizar petición para guardado de subcategoria")
    end


  when 1 then ### SERVIDOR TIPO -> WEB

    # ruta base de la petición
    ruta = "#{$urlServidorWEB}/guardarSubcategoria.php"

    begin
      # parámetros de la petición
      parametros = "?nombre="<<nombre<<"&posicion="<<posicion<<"&categoria="<<categoria
    rescue Exception => e
      manejarExcepcion(e,"Error al formar URL para guardado de subcategoría.")
    end

    begin
      # se codifica la petición a formato URI
      uri = URI.parse(URI.encode(ruta<<parametros))
    rescue Exception => e
      manejarExcepcion(e,"Error al crear URL para guardado de subcategoria")
    end

    begin
      # Petición GET
      respuesta = Net::HTTP.get_response(uri)
      $logMenu.debug "#  Subcateg: #{nombre} -> #{respuesta.body}"
      $logMenu.debug "#  *"
    rescue Exception => e
      manejarExcepcion(e,"Error al realizar petición para guardado de subcategoria")
    end
  end
end

### Función guardarSubsubcategoria: Realiza la petición al servidor para almacenar una 
### subsubcategoria en la base de datos
########################################################################################
def guardarSubsubcategoria(nombre, posicion, categoria, subcategoria)


  case $tipoServidor 
  when 0 then ### SERVIDOR TIPO -> REST

    servidor = "#{$urlServidorREST}/subsubcategorias/"

    begin
      # se codifica la petición a formato URI
      url = URI.parse(URI.encode(servidor))
    rescue Exception => e
      manejarExcepcion(e,"Error al crear URL para guardado de producto")
    end

    begin

      #Se genera el XML
      builder = Nokogiri::XML::Builder.new do
        subsubcategorias {
          nombre nombre
          posicion posicion
          categoria categoria
          subcategoria subcategoria
        }
      end

      # Petición POST
      http = Net::HTTP.new(url.host, url.port)
      request = Net::HTTP::Post.new(url.path)
      request.content_type = 'application/xml'
      request.body = builder.to_xml
      response = http.request(request)            
      $logMenu.debug "#  *  Subsubcateg: "<<nombre<<" -> "<<response.body
    rescue Exception => e
      manejarExcepcion(e,"Error al realizar petición para guardado de subsubcategoria")
    end


  when 1 then ### SERVIDOR TIPO -> WEB
    # ruta base de la petición
    ruta = "#{$urlServidorWEB}/guardarSubsubcategoria.php"

    begin
      # parámetros de la petición
      parametros = "?nombre="<<nombre<<"&posicion="<<posicion<<"&categoria="<<categoria<<"&subcategoria="<<subcategoria
    rescue Exception => e
      manejarExcepcion(e,"Error al formar URL para guardado de subsubcategoría.")
    end


    begin
      # se codifica la petición a formato URI
      uri = URI.parse(URI.encode(ruta<<parametros))
    rescue Exception => e
      manejarExcepcion(e,"Error al crear URL para guardado de subsubcategoria")
    end

    begin
      # Petición GET
      respuesta = Net::HTTP.get_response(uri)
      $logMenu.debug "#  *  Subsubcateg: "<<nombre<<" -> "<<respuesta.body
    rescue Exception => e
      manejarExcepcion(e,"Error al realizar petición para guardado de subsubcategoria")
    end

  end 
end

### Función productoToXml: genera un documento XML que representa un producto
########################################################################################
def productoToXml(p)
  builder = Nokogiri::XML::Builder.new do
    productos {
      categoria p.categoria
      descripcion p.descripcion
      formato p.formato
      imagenSrc p.imagen
      marca p.marca
      ofertaCarrefour p.oferta_carrefour
      ofertaHipercor p.oferta_hipercor
      ofertaMercadona p.oferta_mercadona
      ofertacorteIngles p.oferta_corteIngles
      precioCarrefour p.precio_carrefour
      precioHipercor p.precio_hipercor
      precioMercadona p.precio_mercadona
      precioRelativoCarrefour p.precio_relativo_carrefour
      precioRelativoHipercor p.precio_relativo_hipercor
      precioRelativoMercadona p.precio_relativo_mercadona
      preciocorteIngles p.precio_corteIngles
      preciorelativocorteIngles p.precio_relativo_corteIngles
      subcategoria p.subcategoria
      subsubcategoria p.subsubcategoria
    }
  end

  return builder.to_xml
end