#! /Applications/jruby-1.6.3/bin/jruby -J-Xms512m -J-Xmx2048m
# encoding: UTF-8

require 'includes/gui'
require 'includes/inicio'

################# CONFIGURACIÓN SCRAPER ################# 

## Indica el tipo de servidor que se utiliza
$tipoServidor = 0  ## REST -> 0    WEB -> 1

## url del servidor WEB
$urlServidorWEB = "http://localhost/comprarador"                  

 ## url del servidor REST
$urlServidorREST = "http://localhost:8080/comprarador/rest"
#########################################################


################# INICIACIÓN EJECUCIÓN ################# 

#se crea la pantalla de inicio
pantallaInicio = PantallaInicio.new

#se inicia el scraper para categorias
puts "Iniciando scraper"
$scraperCategorias = Scraper.new

#se inicia el scraper para menu
#$scraperMenu = Scraper.new

#se deja de mostrar la pantalla de inicio
pantallaInicio.noMostrar

#se crea la interfaz del programa
puts "Creando GUI"
$gui = Gui.new

