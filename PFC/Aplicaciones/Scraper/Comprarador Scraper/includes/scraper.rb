#! /Applications/jruby-1.6.3/bin/jruby -J-Xms512m -J-Xmx2048m
# encoding: UTF-8

require "rubygems"
require "celerity"
require "java"
require "net/http"
require "uri"
require "nokogiri"
require "includes/Producto"
require "logger"
require "json"
require "includes/Persistencia"

#Clase Java requerida para excepciones
include_class 'java.lang.NullPointerException'

#############################################################################################################
###### CLASE SCRAPER: se encarga de establecer las funciones que permitirán obtener la información de los
######                productos de la web www.carritus.com
#############################################################################################################

class Scraper


    ### Función initialize: define el código a ejecutar cuando se crea un objeto de la clase
    ########################################################################################
    def initialize

        #################################################################################
        ### DECLARACI√ìN DE VARIABLES
        #################################################################################

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
        $scrapingEnabled = false

        ## se inicia el navegador
        init()

    end

   
    ### Función init: Inicializa el navegador y lo redirije hacia la web de carritus para 
    ### comenzar el scraping
    ########################################################################################
    def init()

        #Carga la página de carritus.com
        begin
            @browser.goto("http://www.carritus.com/")
        rescue Exception => e
          manejarExcepcion(e,"Error dirigir el navegador hacia la web de carritus")
        end
    
        #Se realiza el login en la página
        begin
            @browser.link(:class,"linkLoginPopUp registerPopupAccess usuario").fire_event("onclick")
            @browser.text_field(:id,"ulogin_email").value = "XXXX"
            @browser.text_field(:id,"ulogin_password").value = "XXXX"
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
    
    ### Función scrapCategoria: Obtiene la información relativa a todos los productos de una 
    ### categoría principal y los almacena en la base de datos
    ########################################################################################
    def scrapCategoria(iteradorCategorias)

        #se crea el Log para las categorias
        begin
            $logCategorias = Logger.new('logs/logCategorias.txt','weekly')
            $logCategorias.debug "###########################################################"
            $logCategorias.debug "#################### COMIENZO SCRAPING ####################"
            $logCategorias.debug "###########################################################"
        rescue Exception => e
            puts "El archivo de log de categorías no ha podido ser creado correctamente."
            puts e.inspect
        end
        
        
        $scrapingEnabled = true
        
        ## SE REALIZA EL SCRAPING
        
        begin
        
          pathCategorias = "html/body/ul/li[#{iteradorCategorias}]/a"
          @productos = []


          if(existe?(@paginaCategorias.xpath(pathCategorias).inner_text) && $scrapingEnabled)

            puts "-- Obteniendo categoria: #{parseCategoria(@paginaCategorias.xpath(pathCategorias).inner_text)}"
            ## SE ITERAN LAS SUBCATEGORIAS
            iteradorSubcategorias = 1
            pathSubcategorias = "html/body/ul/li[#{iteradorCategorias}]/ul/li[#{iteradorSubcategorias}]/a"
            while(existe?(@paginaCategorias.xpath(pathSubcategorias).inner_text) && $scrapingEnabled)

              puts "----- Obteniendo subcategoria: #{parseSubcategoria(@paginaCategorias.xpath(pathSubcategorias).inner_text)}"

              ## SE ITERAN LAS SUBSUBCATEGORIAS
              iteradorSubsubcategorias = 1
              pathSubsubcategorias = "html/body/ul/li[#{iteradorCategorias}]/ul/li[#{iteradorSubcategorias}]/ul/li[#{iteradorSubsubcategorias}]/a"
              if(existe?(@paginaCategorias.xpath(pathSubsubcategorias).inner_text) && $scrapingEnabled)
                while(existe?(@paginaCategorias.xpath(pathSubsubcategorias).inner_text) && $scrapingEnabled)

                  puts "-------- Obteniendo subsubcategoria: #{parseSubsubcategoria(@paginaCategorias.xpath(pathSubsubcategorias).inner_text)}"

                  url = @paginaCategorias.xpath(pathSubsubcategorias)[0].attributes['href'].text

                  #se obtiene el id de la subsubcategoria
                  idSubsubcategoria = obtenerIdSubsubcategoria(url)

                  #se obtienen los productos
                  scrapPaginaProducto(idSubsubcategoria,iteradorCategorias,iteradorSubcategorias,iteradorSubsubcategorias)
                  if($scrapingEnabled)
                      almacenarProductos()  
                  end

                  iteradorSubsubcategorias += 1
                  pathSubsubcategorias = "html/body/ul/li[#{iteradorCategorias}]/ul/li[#{iteradorSubcategorias}]/ul/li[#{iteradorSubsubcategorias}]/a"
                end
              else #NO HAY SUBSUBCATEGORIAS -> NAVEGAR A SUBCATEGORIA
                puts "No existen subsubcategorias para la subcategoria"
                iteradorSubsubcategorias = 0
                url = @paginaCategorias.xpath("html/body/ul/li[#{iteradorCategorias}]/ul/li[#{iteradorSubcategorias}]/a")[0].attributes['href'].value

                #se obtiene el id de la subsubcategoria
                idSubcategoria = obtenerIdSubcategoria(url)

                #se obtienen los productos
                scrapPaginaProducto(idSubcategoria,iteradorCategorias,iteradorSubcategorias,iteradorSubsubcategorias)
                
                if($scrapingEnabled)
                    almacenarProductos()  
                end
              end
                
              iteradorSubcategorias += 1
              pathSubcategorias = "html/body/ul/li[#{iteradorCategorias}]/ul/li[#{iteradorSubcategorias}]/a/b"
            end
          end
        
        
        rescue Exception => e
            puts "Error al obtener categoria #{iteradorCategorias-1}"
            puts e.inspect
        end
        $logCategorias.debug "###########################################################"

    end 
    ### fin función scrapCategoria
    
     ### Funcion scrapPaginaProducto: obtiene la página de productos de una subcategoria/subsubcategoria
      ### y recorre todos los productos para realizar el scraping
      ########################################################################################
      def scrapPaginaProducto(idCategoria,iteradorCategorias,iteradorSubcategorias,iteradorSubsubcategorias)

        # SE ITERAN LOS SUPERMERCADOS
        # IDS SUPERMERCADOS: Mercadona -> 4, Corte Ingles -> 5, Carrefour -> 6, Hipercor -> 7
        iteradorSupermercado = 4
        while(iteradorSupermercado <= 7 && $scrapingEnabled)

          puts "Supermercado -> #{$supermercadosNombres[iteradorSupermercado.to_s]}"

          numeroPagina = 1
          urlPaginaProductos = "http://www.carritus.com/carrito/ajaxFrame?cm_id=#{idCategoria}&page=#{numeroPagina}&super_id=#{iteradorSupermercado}"
                    
          #se realiza la petición para obtener la página con los productos de la subsubcategoria
          @browser.goto(urlPaginaProductos)
          @browser.wait

          #SE ITERAN LOS PRODUCTOS
          iteradorProductos = 2
          while(producto?(iteradorProductos) && $scrapingEnabled)
            # se obtienen los datos del producto correspondiente                    
            scrapProducto(iteradorProductos,iteradorSupermercado,iteradorCategorias,iteradorSubcategorias,iteradorSubsubcategorias)

            # el producto scrapeado es el último de la página
            if(not producto?(iteradorProductos+2) && $scrapingEnabled)

              # se pasa a la siguiente página
              numeroPagina += 1
              urlPaginaProductos = "http://www.carritus.com/carrito/ajaxFrame?cm_id=#{idCategoria}&page=#{numeroPagina}&super_id=#{iteradorSupermercado}"
              #se realiza la petición para obtener la página con los productos de la subsubcategoria
              @browser.goto(urlPaginaProductos)
              @browser.wait
              iteradorProductos = 2
            else
              # si no es el último se aumenta el iterador de productos
              iteradorProductos += 2
            end

          end
          
          iteradorSupermercado += 1
        end

      end



      ### Función scrapProducto: Obtiene los datos acerca de un producto e introduce 
      ### (o actualiza) el objeto que simboliza el producto en el array productos
      ########################################################################################
      def scrapProducto(index,supermercado,categoria,subcategoria,subsubcategoria)   
          #El index va por números pares -> 1 y 2 es el mismo producto!!

          #puts "Escrapeando producto #{index}"

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
                      #puts "Precio -> #{precio}"
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
              #puts "Descripcion -> #{descripcion}"
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
              puts "  - Obtenido producto ya scrapeado -> agregando precio"
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
              puts "  - Obtenido producto no scrapeado -> crear producto"
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

    
    
    
    ### Función scrapMenu: Obtiene el árbol de menús utilizado por Carritus
    ########################################################################################
    def scrapMenu()
        
        $categorias = []
        
        #se crea el Log para el menú
        begin
            $logMenu = Logger.new('logs/logMenu.txt','weekly')
            $logMenu.debug "###########################################################"
            $logMenu.debug "#################### COMIENZO SCRAPING ####################"
            $logMenu.debug "###########################################################"
        rescue Exception => e
            puts "El archivo de log de menú no ha podido ser creado correctamente."
            puts e.inspect
        end
        
        ## SE ITERAN LAS CATEGORIAS
        iteradorCategorias = 2 # se empieza en 2 para no tener en cuenta la categoría "Cestas"
        pathCategorias = "html/body/ul/li[#{iteradorCategorias}]/a"

        obtenerCategorias()
        
        while(existe?(@paginaCategorias.xpath(pathCategorias).inner_text))

          categoriaActual = parseCategoria(@paginaCategorias.xpath(pathCategorias).inner_text)
          guardarCategoria(categoriaActual,(iteradorCategorias-1).to_s)
          puts "Obtenida categoria -> #{categoriaActual}"
          
          ## SE ITERAN LAS SUBCATEGORIAS
          iteradorSubcategorias = 1
          pathSubcategorias = "html/body/ul/li[#{iteradorCategorias}]/ul/li[#{iteradorSubcategorias}]/a/b"
          while(existe?(@paginaCategorias.xpath(pathSubcategorias).inner_text))
                      
                subcategoriaActual = parseSubcategoriaMenu(@paginaCategorias.xpath(pathSubcategorias).inner_text)
                guardarSubcategoria(subcategoriaActual,iteradorSubcategorias.to_s,(iteradorCategorias-1).to_s)
                puts "---- Obtenida subcategoria -> #{subcategoriaActual}"
                      
                ## SE ITERAN LAS SUBSUBCATEGORIAS
                iteradorSubsubcategorias = 1
                pathSubsubcategorias = "html/body/ul/li[#{iteradorCategorias}]/ul/li[#{iteradorSubcategorias}]/ul/li[#{iteradorSubsubcategorias}]/a"
                while(existe?(@paginaCategorias.xpath(pathSubsubcategorias).inner_text))
                  subsubcategoriaActual = parseSubsubcategoriaMenu(@paginaCategorias.xpath(pathSubsubcategorias).inner_text)
                  guardarSubsubcategoria(subsubcategoriaActual,iteradorSubsubcategorias.to_s,(iteradorCategorias-1).to_s,iteradorSubcategorias.to_s)
                  puts "------------ Obtenida subsubcategoria -> #{subsubcategoriaActual}"
                  iteradorSubsubcategorias += 1
                  pathSubsubcategorias = "html/body/ul/li[#{iteradorCategorias}]/ul/li[#{iteradorSubcategorias}]/ul/li[#{iteradorSubsubcategorias}]/a"
                end
            
            
            iteradorSubcategorias += 1
            pathSubcategorias = "html/body/ul/li[#{iteradorCategorias}]/ul/li[#{iteradorSubcategorias}]/a/b"
          end
          
          iteradorCategorias += 1
          pathCategorias = "html/body/ul/li[#{iteradorCategorias}]/a"
        end      
    end


    #################################################################################
    ### FUNCIONES COMPLEMENTARIAS SCRAPING
    #################################################################################

    ### Función producto?: Verifica si existe un producto en el navegador en la posición indicada
    ########################################################################################
    def producto?(index)
        return @browser.link(:class => "detalle_ofertas", :index=> index).exists
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
    
    ### Función parseSubcategoria: parsea el texto del nombre de una subcategoría
    ########################################################################################
    def parseSubcategoria(subcat)
      subcategoria = parseCategoria(subcat)
      return subcategoria[17,subcategoria.size].split("\n")[0]
    end
    
    ### Función parseSubcategoriaMenu: parsea el texto del nombre de una subcategoría
    ########################################################################################
    def parseSubcategoriaMenu(subcat)
      subcategoria = parseCategoria(subcat)
      return subcategoria[6,subcategoria.size].split("\n")[0]
    end
    
    ### Función parseSubsubcategoria: parsea el texto del nombre de una subsubcategoría
    ########################################################################################
    def parseSubsubcategoria(subsubcat)
      subsubcategoria = parseCategoria(subsubcat)
      return subsubcategoria[8,subsubcategoria.size-1].split("\n")[0]
    end

    ### Función parseSubsubcategoriaMenu: parsea el texto del nombre de una subsubcategoría
    ########################################################################################
    def parseSubsubcategoriaMenu(subsubcat)
      subsubcategoria = parseCategoria(subsubcat)
      return subsubcategoria[1,subsubcategoria.size-1].split("\n")[0]
    end
    
    ### Función almacenarProductos: recorre los productos del array @productos para almacenarlos en la BBDD
    ########################################################################################
    def almacenarProductos ()
        numeroProductos = @productos.size
        puts "++++++ GUARDANDO PRODUCTOS EN BASE DE DATOS ++++++++"
        puts "Obtenidos #{numeroProductos} productos"
        $logCategorias.debug "++++++ GUARDANDO PRODUCTOS EN BASE DE DATOS ++++++++"
        i=0
        while(i<numeroProductos && $scrapingEnabled)

           guardarProducto(@productos[i])
            puts "Guardando producto #{i}"
           # se aumenta el iterador 
            i += 1
        end
        @productos=[]

    end
   
    ### Función obtenerIdSubsubcategoria: obtiene el identificador de una subsubcategoria
    ########################################################################################
    def obtenerIdSubsubcategoria(url)
      partes = url.split("/")
      id = partes[partes.size-1].split("-")[0]
      return id
    end

    ### Función obtenerIdSubcategoria: obtiene el identificador de una subcategoria
    ########################################################################################
    def obtenerIdSubcategoria(url)
      partes = url.split("/")
      id = partes[partes.size-1].split("-")[0]
      return id
    end

    ### Función obtenerIdProducto: obtiene el identificador de un producto
    ########################################################################################
    def obtenerIdProducto(url)
      partes = url.split("-")
      id = partes[partes.size-1].split(".")[0]
      return id
    end
    
    #################################################################################
    ### OTRAS FUNCIONES SECUNDARIAS
    #################################################################################

    ### Función manejarExcepcion: se llama cuando ocurre una excepción
    ########################################################################################
    def manejarExcepcion(excepcion,texto)   
        #puts texto<<"-> "<<excepcion.message
    
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


    ### Función mostrarProducto: muestra las características de un producto del array productos
    ########################################################################################
    def mostrarProducto(producto)
    
        puts "---- Descripción: #{producto.descripcion}"
        puts "---- Imagen: #{producto.imagen}"
        puts "---- Precio Mercadona: #{producto.precio_mercadona}"
        puts "---- Precio Carrefour: #{producto.precio_carrefour}"
        puts "---- Precio Hipercor: #{producto.precio_hipercor}"
        puts "---- Precio Corte Inglés: #{producto.precio_corteIngles}"
    
    end


#FINAL DE LA CLASE
end
