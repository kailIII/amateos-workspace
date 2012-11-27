#! /Applications/jruby-1.6.3/bin/jruby
# encoding: UTF-8
require "rubygems"
require "includes/funcionesScraperTest"
require "celerity"
require "java"
require "net/http"
require "uri"
require "nokogiri"
require "includes/Producto"
require "logger"
require "json"
require "includes/Persistencia"
# se sincroniza la consola para que esté siempre limpia cuando se introducen valores
STDOUT.sync = true

# se crea el archivo de log del test
log = Logger.new("logs/logTest.txt","weekly")
log.debug "********************** INICIO DEL TEST **********************"

# se inicia la variable que contendrá el resultado del test
testTextos = ["1. INICIALIZACIÓN DEL SCRAPER","2. OBTENCIÓN DE CATEGORÍAS", "3. PÁGINA DE PRODUCTOS", "4. CAMBIO DE SUPERMERCADO", "5. PÁGINA SIGUIENTE", "6. INDEXADO DE PRODUCTOS"]
testResult = []

### Test iniciación scraper
puts "**********************************************"
puts "Test: #{testTextos[0]}"
puts "**********************************************"
begin
  puts "________________________________________________________________________________________________________________________________"
  puts "ORDEN: "
  puts "Navegue hasta www.carritus.com/carrito, elija Mercadona como supermercado habitual y seleccione 18004 como código postal."
  puts "--------------------------------------------------------------------------------------------------------------------------------"

  ## Inicialización del navegador y login
  init()
  testResult[0] = "OK"
  log.debug "Test: #{testTextos[0]} - OK"
rescue Exception => e
  testResult[0] = "Falló"
  log.error "Test: #{testTextos[0]} - Falló - #{e.message}"
end


### Test de navegación: obtener categorias
puts "**********************************************"
puts "*** TEST: #{testTextos[1]}"
puts "**********************************************"
begin
  categorias = obtenerCategorias() 

  i=0
  while(i<categorias.size)
    puts "#{categorias[i]}"
    i += 1
  end

  puts "________________________________________________________________________________________________________________________________"
  puts "ACCIÓN: Introduzca 1 (OK) o 0 (Falló) si las categorías mostradas son las mismas que las del menú de Carritus"
  puts "--------------------------------------------------------------------------------------------------------------------------------"

  respuesta = gets.chomp

  if(respuesta.to_i == 1)  
    testResult[1] = "OK"
    log.debug "Test: #{testTextos[3]} - OK"
  else
    testResult[1] = "Falló"
    log.error "Test: #{testTextos[3]} - Falló - No coincide con la realidad"
  end  
rescue Exception => e
  testResult[1] = "Falló"
  log.error "Test: #{testTextos[1]} - Falló - #{e.message}"
end


### Test de navegación: obtener producto categoria
puts "**********************************************"
puts "*** TEST: #{testTextos[2]}"
puts "**********************************************"
begin
  puts "________________________________________________________________________________________________________________________________"
  puts "ORDEN: "
  puts "Navegue a la categoría BEBIDAS - REFRESCOS - COLA"
  puts "--------------------------------------------------------------------------------------------------------------------------------"
  
  urlPaginaProductos = "http://www.carritus.com/carrito/ajaxFrame?cm_id=1731&page=1&super_id=4"
  #se realiza la petición para obtener la página con los productos de la subsubcategoria
  @browser.goto(urlPaginaProductos)
  @browser.wait

  scrapProducto(2,4,7,10,2)
  mostrarProducto(@productos[0])

  puts "________________________________________________________________________________________________________________________________"
  puts "ACCIÓN: Introduzca 1 (OK) o 0 (Falló) si el producto mostrado es el primero de la categoría para MERCADONA"
  puts "--------------------------------------------------------------------------------------------------------------------------------"

  respuesta = gets.chomp

  if(respuesta.to_i == 1)  
    testResult[2] = "OK"
    log.debug "Test: #{testTextos[2]} - OK"
  else
    testResult[2] = "Falló"
    log.error "Test: #{testTextos[2]} - Falló - No coincide con la realidad"
  end
rescue Exception => e
  testResult[2] = "Falló"
  log.error "Test: #{testTextos[2]} - Falló - #{e.message}"
end


### Test de navegación: cambio supermercado
puts "**********************************************"
puts "*** TEST: #{testTextos[3]}"
puts "**********************************************"
begin

  puts "________________________________________________________________________________________________________________________________"
  puts "ORDEN: "
  puts "Cambie de supermercado a CARREFOUR"
  puts "--------------------------------------------------------------------------------------------------------------------------------"


  urlPaginaProductos = "http://www.carritus.com/carrito/ajaxFrame?cm_id=1731&page=1&super_id=6"
  #se realiza la petición para obtener la página con los productos de la subsubcategoria
  @browser.goto(urlPaginaProductos)
  @browser.wait

  scrapProducto(2,6,7,10,2)
  mostrarProducto(@productos[1])

  puts "________________________________________________________________________________________________________________________________"
  puts "ACCIÓN: Introduzca 1 (OK) o 0 (Falló) si el producto mostrado es el PRIMERO de la categoría para CARREFOUR"
  puts "--------------------------------------------------------------------------------------------------------------------------------"

  respuesta = gets.chomp

  if(respuesta.to_i == 1)  
    testResult[3] = "OK"
    log.debug "Test: #{testTextos[3]} - OK"
  else
    testResult[3] = "Falló"
    log.error "Test: #{testTextos[3]} - Falló - No coincide con la realidad"
  end
rescue Exception => e
  testResult[3] = "Falló"
  log.error "Test: #{testTextos[3]} - Falló - #{e.message}"
end



### Test de navegación: cambio supermercado
puts "**********************************************"
puts "*** TEST: #{testTextos[4]}"
puts "**********************************************"
begin

  puts "________________________________________________________________________________________________________________________________"
  puts "ORDEN: "
  puts "Navegue a la página 2"
  puts "--------------------------------------------------------------------------------------------------------------------------------"


  urlPaginaProductos = "http://www.carritus.com/carrito/ajaxFrame?cm_id=1731&page=2&super_id=6"
  #se realiza la petición para obtener la página con los productos de la subsubcategoria
  @browser.goto(urlPaginaProductos)
  @browser.wait

  scrapProducto(2,6,7,10,2)
  mostrarProducto(@productos[2])

  puts "________________________________________________________________________________________________________________________________"
  puts "ACCIÓN: Introduzca 1 (OK) o 0 (Falló) si el producto mostrado es el PRIMERO de la categoría para CARREFOUR y la PÁGINA 2"
  puts "--------------------------------------------------------------------------------------------------------------------------------"

  respuesta = gets.chomp

  if(respuesta.to_i == 1)  
    testResult[4] = "OK"
    log.debug "Test: #{testTextos[4]} - OK"
  else
    testResult[4] = "Falló"
    log.error "Test: #{testTextos[4]} - Falló - No coincide con la realidad"
  end
rescue Exception => e
  testResult[4] = "Falló"
  log.error "Test: #{testTextos[4]} - Falló - #{e.message}"
end

### Test de navegación: indexado de productos
puts "**********************************************"
puts "*** TEST: #{testTextos[5]}"
puts "**********************************************"
begin

  urlPaginaProductos = "http://www.carritus.com/carrito/ajaxFrame?cm_id=1731&page=2&super_id=6"
  #se realiza la petición para obtener la página con los productos de la subsubcategoria
  @browser.goto(urlPaginaProductos)
  @browser.wait

  scrapProducto(6,6,7,10,2)
  mostrarProducto(@productos[3])

  puts "________________________________________________________________________________________________________________________________"
  puts "ACCIÓN: Introduzca 1 (OK) o 0 (Falló) si el producto mostrado es el TERCERO de la categoría para CARREFOUR y la PÁGINA 2"
  puts "--------------------------------------------------------------------------------------------------------------------------------"

  respuesta = gets.chomp

  if(respuesta.to_i == 1)  
    testResult[5] = "OK"
    log.debug "Test: #{testTextos[5]} - OK"
  else
    testResult[5] = "Falló"
    log.error "Test: #{testTextos[5]} - Falló - No coincide con la realidad"
  end
rescue Exception => e
  testResult[5] = "Falló"
  log.error "Test: #{testTextos[5]} - Falló - #{e.message}"
end



###################################################
### SE MUESTRAN LOS RESULTADOS DEL TEST
puts "***************************"
puts "*** RESULTADOS DEL TEST ***"
puts "***************************"

i=0
while(i<testResult.size)
  puts "#{testTextos[i]}: #{testResult[i]}"
  i += 1
end