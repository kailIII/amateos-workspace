#! /Applications/jruby-1.6.3/bin/jruby
# encoding: UTF-8

require 'java'

include Java

#############################################################################################################
###### CLASE THREADMENU: define las acciones a realizar por la hebra para el scraping de menú
#############################################################################################################
class ThreadMenu
	
	### Función run: define el código a ejecutar por la hebra
    ########################################################################################
	def run
	    $gui.agregarTextoConsola("Inicio de scraping de menú.")
	    $scraperMenu.scrapMenu
	    $gui.finObtenerMenu
	    $gui.agregarTextoConsola("Scraping de menú finalizado.")
	end
end


#############################################################################################################
###### CLASE THREADCATEGORIAS: define las acciones a realizar por la hebra para el scraping de categorías
#############################################################################################################
class ThreadCategorias

    ### Función run: define el código a ejecutar por la hebra
    ########################################################################################
	def run     
	    #se recorre el vector de categoriasSeleccionadas para saber qué categorías hay que obtener
        i=0
        while(i<$categoriasSeleccionadas.size)

            if($categoriasSeleccionadas[i]==true)
                $gui.agregarTextoConsola("Inicio de scraping de la categoría: #{$categorias[i]}.")
                
                # No hay categoría ofertas
                $scraperCategorias.scrapCategoria(i+2)
                # Si hay categoría ofertas
        	    $gui.agregarTextoConsola("Scraping de categoría #{$categorias[i]} finalizado.")
            end
            i += 1

        end
	    $gui.finObtenerCategorias
	end
end