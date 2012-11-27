### Función init: Inicializa el navegador y lo redirije hacia la web de carritus para 
### comenzar el scraping
########################################################################################
def init()

  #Creación del navegador
  begin
    @browser = Celerity::Browser.new(:browser => :firefox,:resynchronize => true)
    @browser.clear_cookies
  rescue Exception => e
    manejarExcepcion(e,"Error al crear el navegador")
  end

  ## Hash que identifica los ids de cada supermercado
  $supermercadosNombres = {"6"=>"Carrefour", "5"=>"El Corte Inglés", "4"=>"Mercadona", "7"=>"Hipercor"}
  $supermercados = {"Carrefour"=>"4", "El Corte Inglés"=>"3","Mercadona"=>"2","Hipercor"=>"5"}

  ## Variable que indica el índice del último producto obtenido
  #$idProducto = 0

  ## Array que almacena los productos de una subcategoria
  @productos = []

  ## Variable que funciona a modo de interruptor para permitir o no realizar el scraping de categorías
  ## es utilizada para poder detener el scraping.
  $scrapingEnabled = true

  #Carga la página de carritus.com
  begin
    @browser.goto("http://www.carritus.com/")
  rescue Exception => e
    manejarExcepcion(e,"Error dirigir el navegador hacia la web de carritus")
  end

  #Se realiza el login en la página
  begin
    @browser.link(:class,"linkLoginPopUp registerPopupAccess usuario").fire_event("onclick")
    @browser.text_field(:id,"ulogin_email").value = "icomprascraper@gmail.com"
    @browser.text_field(:id,"ulogin_password").value = "scraper_pfc_2012"
    @browser.div(:id,"loginRE31").fire_event("click")
  rescue Exception => e
    manejarExcepcion(e,"Error al abrir el dialog de login")
  end

  #Se navega hasta la página de productos
  begin
    @browser.goto("http://www.carritus.com/productos/")
  rescue Exception => e
    manejarExcepcion(e,"Error dirigir el navegador hacia la web de carritus")
  end
end

#################################################################################
### DEFINICIÓN DE FUNCIONES SCRAPING
#################################################################################

### Función obtenerCategorias: Obtiene el nombre de todas las categorias principales del 
### menú para mostrarlas en la GUI.
########################################################################################
def obtenerCategorias()

  categorias = []
  numCategorias = 0
  begin

    @paginaCategorias = Nokogiri::HTML.parse(@browser.div(:class, "radius_5_bottom menu-vertical").ul.html)

    i = 2 # se empieza en 2 para no tener en cuenta la categoría "Cestas"
    path = "html/body/ul/li[#{i}]/a"
    while(existe?(@paginaCategorias.xpath(path).inner_text))
      categorias.push(parseCategoria(@paginaCategorias.xpath(path).inner_text))
      i += 1
      numCategorias += 1
      path = "html/body/ul/li[#{i}]/a"
    end  

  rescue Exception => e
    numeroCategorias = 0
    manejarExcepcion(e,"Error al obtener el número de categorias")
  end

  return categorias
end

### Función scrapProducto: Obtiene los datos acerca de un producto e introduce 
### (o actualiza) el objeto que simboliza el producto en el array productos
########################################################################################
def scrapProducto(index,supermercado,categoria,subcategoria,subsubcategoria)   
  #El index va por números pares -> 1 y 2 es el mismo producto!!

  # obtención del precio relativo del producto (desde la página donde se muestran todos los productos)
  if (@browser.div(:class=> "cantidad precio_por_medida",:index => index/2).exists)

    begin
      precio_relativo = @browser.div(:class=> "cantidad precio_por_medida",:index => index/2).text
    rescue Exception => e
      manejarExcepcion(e,"Error al obtener precio relativo de producto")
    end

    begin
      precio_relativo = precio_relativo.delete "()"
      # se sustituye la coma del precio por un punto. Por ejemplo 1,23 -> 1.23
      precio_relativo [","]="."
    rescue Exception => e
      manejarExcepcion(e,"Error al dar formato al precio relativo de producto")
    end

  else
    precio_relativo = ""
  end


  begin
    # se navega hasta el producto para obtener los detalles
    idProducto = obtenerIdProducto(@browser.link(:class => "detalle_ofertas", :index=> index).href)
    @browser.goto("http://www.carritus.com/carrito/ajaxDetalleProducto?pm_id=#{idProducto}")
    @browser.wait
  rescue Exception => e
    manejarExcepcion(e,"Error al hacer click sobre un producto para obtener más información")
  end

  begin
    # se obtiene el texto que se muestra en la cabecera: marca + formato. Por ejemplo: Cruzcampo (1x330ml)
    cabecera = @browser.span(:class, "titulo_popup").text
  rescue Exception => e
    manejarExcepcion(e,"Error al obtener la cabecera de información de un producto")
  end

  begin
    # obtención de la marca del producto a partir de los datos de cabecera
    marca = cabecera.split(" (")[0]
  rescue Exception => e
    manejarExcepcion(e,"Error al obtener la marca de un producto")
  end

  begin
    # obtención del formato a partir de los datos de cabecera
    formato = cabecera.split(" (")[1].delete ")"
  rescue Exception => e
    manejarExcepcion(e,"Error al obtener el formato de un producto")
  end

  # se recorren todos los precios que se muestran hasta encontrar el del supermerado actual
  sup = 1
  oferta = ""
  precio = ""
  while(@browser.div(:class, "dp_derecha").div(:class => "dp_super", :index => sup.to_i).image.exists)

    if(@browser.div(:class, "dp_derecha").div(:class => "dp_super", :index => sup.to_i).image.alt==$supermercadosNombres[supermercado.to_s])

      begin
        precio = @browser.div(:class, "dp_derecha").div(:class => "dp_super", :index => sup.to_i).spans[1].text
      rescue Exception => e
        manejarExcepcion(e,"Error al obtener el precio de un producto")
      end

      begin
        # obtención del precio del producto (se elimina el símbolo € para almacenarlo como número)
        precio = precio.delete " €"
        # se sustituye la coma del precio por un punto. Por ejemplo 1,23 -> 1.23
        precio [","]="."
      rescue Exception => e
        manejarExcepcion(e,"Error al dar formato al precio de un producto")
      end

      # se verifica si el producto tiene algún tipo de oferta            
      if(@browser.div(:class, "dp_derecha").div(:class => "dp_super", :index => sup.to_i).spans[2].exists)
        begin
          oferta = @browser.div(:class, "dp_derecha").div(:class => "dp_super", :index => sup.to_i).spans[2].text   
        rescue Exception => e
          manejarExcepcion(e,"Error al obtener la oferta de un producto")
        end
      end   
    end

    sup += 1
  end

  begin
    # obtención de la descripción completa del producto
    descripcion = @browser.div(:class, "dp_centro").span.text
  rescue Exception => e
    manejarExcepcion(e,"Error al obtener la descripcion de un producto")
  end

  begin
    # obtención de la ruta de la imagen (en carritus.com)
    imagen = @browser.div(:class, "dp_izquierda").image.src
    # la imagen del producto no está disponible (en la ruta se incluye el texto "no_disponible")
    if(imagen.include? 'no_disponible')
      imagen = ""
    end
  rescue Exception => e
    manejarExcepcion(e,"Error al obtener la imagen de un producto")
  end

  begin
    # se vuelve atrás en la navegación
    @browser.back
    @browser.wait
  rescue Exception => e
    manejarExcepcion(e,"Error al cerrar la ventana emergente de información de un producto")
  end

  # comprueba si el producto ya ha sido obtenido anteriormente
  scrapeado = productoScrapeado?(descripcion, imagen)
  if (scrapeado>=0)
    begin
      # Si existe el producto se agrega el precio del supermercado correspondiente
      case supermercado
      when 4 then @productos[scrapeado].setPrecioMercadona(precio,precio_relativo,oferta)
      when 5 then @productos[scrapeado].setPrecioCorteIngles(precio,precio_relativo,oferta)                         
      when 6 then @productos[scrapeado].setPrecioCarrefour(precio,precio_relativo,oferta)
      when 7 then @productos[scrapeado].setPrecioHipercor(precio,precio_relativo,oferta)
      end
    rescue Exception => e
      manejarExcepcion(e,"Error al actualizar el precio de un producto previamente scrapeado")
    end

  else
    begin
      # si el producto no está en la lista (es la primera vez que se obtiene) se crea
      producto = Producto.new(marca,descripcion,formato,imagen,categoria-1,subcategoria,subsubcategoria)
    rescue Exception => e
      manejarExcepcion(e,"Error al crear un objeto producto")
    end

    begin
      # se agrega el precio del supermercado correspondiente        
      case supermercado
      when 4 then producto.setPrecioMercadona(precio,precio_relativo,oferta)
      when 5 then producto.setPrecioCorteIngles(precio,precio_relativo,oferta)
      when 6 then producto.setPrecioCarrefour(precio,precio_relativo,oferta)
      when 7 then producto.setPrecioHipercor(precio,precio_relativo,oferta)
      end
    rescue Exception => e
      manejarExcepcion(e,"Error al establecer el precio de un producto")
    end

    begin
      # se introduce el nuevo producto en el vector
      @productos.push(producto)
    rescue Exception => e
      manejarExcepcion(e,"Error al introducir un objeto producto en el vector @productos")
    end

  end

end



### Función manejarExcepcion: se llama cuando ocurre una excepción
########################################################################################
def manejarExcepcion(excepcion,texto)   
  puts texto<<"-> "<<excepcion.message

  begin
    $logErrores = Logger.new('logs/logErrores.txt','weekly')
    $logErrores.error "*******************************************************"
    $logErrores.error texto<<"-> "<<excepcion.message
    $logErrores.error excepcion.backtrace 
    $logErrores.error "*******************************************************"
  rescue Exception => e
    puts "No ha sido posible acceder al log de errores."
    puts e.inspect
  end

end


### Función existe?: comprueba la existencia de un elemento en la página
########################################################################################
def existe?(item)
  return (item != "")
end


### Función parseCategoria: parsea el texto del nombre de una categoría
########################################################################################
def parseCategoria(cat)
  categoria = cat[7, cat.size]
  s = categoria.size-5
  return categoria[0,s]
end

### Función parseCategoria: parsea el texto del nombre de una subcategoría
########################################################################################
def parseSubcategoria(subcat)
  subcategoria = parseCategoria(subcat)
  return subcategoria[17,subcategoria.size].split("\n")[0]
end

### Función parseCategoria: parsea el texto del nombre de una subsubcategoría
########################################################################################
def parseSubsubcategoria(subsubcat)
  subsubcategoria = parseCategoria(subsubcat)
  return subsubcategoria[8,subsubcategoria.size-1].split("\n")[0]
end

### Función mostrarProducto: muestra las características de un producto del array productos
########################################################################################
def mostrarProducto(producto)

  puts "PRODUCTO OBTENIDO:"
  puts "---- Descripción: #{producto.descripcion}"
  puts "---- Imagen: #{producto.imagen}"
  puts "---- Precio Mercadona: #{producto.precio_mercadona} (#{producto.precio_relativo_mercadona})"
  puts "---- Precio Carrefour: #{producto.precio_carrefour} (#{producto.precio_relativo_carrefour})"
  puts "---- Precio Hipercor: #{producto.precio_hipercor} (#{producto.precio_relativo_hipercor})"
  puts "---- Precio Corte Inglés: #{producto.precio_corteIngles} (#{producto.precio_relativo_corteIngles})"

end

### Función obtenerIdProducto: obtiene el identificador de un producto
########################################################################################
def obtenerIdProducto(url)
  partes = url.split("-")
  id = partes[partes.size-1].split(".")[0]
  return id
end

### Función obtenerIdSubsubcategoria: obtiene el identificador de una subsubcategoria
########################################################################################
def obtenerIdSubsubcategoria(url)
  partes = url.split("/")
  id = partes[partes.size-1].split("-")[0]
  return id
end

 ### Función productoScrapeado?: Verifica si el producto existe en el array de productos
  ########################################################################################
  def productoScrapeado?(descripcion, imagen)
      existe = -1
      i = 0
      while i<@productos.size
          if @productos[i].descripcion == descripcion
              existe = i
              break
          elsif imagen != "" and @productos[i].imagen == imagen
              existe = i
              break
          end
          i += 1
      end

      return existe
  end
