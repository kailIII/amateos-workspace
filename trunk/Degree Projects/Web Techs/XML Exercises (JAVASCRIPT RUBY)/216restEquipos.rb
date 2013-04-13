#!/usr/bin/ruby
require 'rubygems'
require 'uri'
require 'nokogiri'
require 'RestClient'
require 'XmlSimple'

########### Creación del XML que contiene los equipos
builder = Nokogiri::XML::Builder.new do |xml|
    xml.equipos {
            xml.equipo(:id => '1', :nombre => 'Real Madrid'){
                xml.jugador(:type => 'portero'){xml.nombre 'Iker Casillas'} 
                xml.jugador(:type => 'defensa'){xml.nombre 'Marcelo'}
                xml.jugador(:type => 'defensa'){xml.nombre 'Pepe'}
                xml.jugador(:type => 'centrocampista'){xml.nombre 'Ozil'}
                xml.jugador(:type => 'centrocampista'){xml.nombre 'Xabi Alonso'}
                xml.jugador(:type => 'delantero'){xml.nombre 'Ronaldo'}
                xml.jugador(:type => 'delantero'){xml.nombre 'Adebayor'}
            }
            xml.equipo(:id => '2', :nombre => 'Barcelona'){
                xml.jugador(:type => 'portero'){xml.nombre 'Victor Valdés'}
                xml.jugador(:type => 'defensa'){xml.nombre 'Piqué'}
                xml.jugador(:type => 'defensa'){xml.nombre 'Puyol'}
                xml.jugador(:type => 'centrocampista'){xml.nombre 'Xavi'}
                xml.jugador(:type => 'centrocampista'){xml.nombre 'Iniesta'}
                xml.jugador(:type => 'delantero'){xml.nombre 'Messi'}
                xml.jugador(:type => 'delantero'){xml.nombre 'Pedro'}
            }
            xml.equipo(:id => '3', :nombre => 'Granada'){
                xml.jugador(:type => 'portero'){xml.nombre 'Roberto'}
                xml.jugador(:type => 'defensa'){xml.nombre 'Nyom'}
                xml.jugador(:type => 'defensa'){xml.nombre 'Mainz'}
                xml.jugador(:type => 'centrocampista'){xml.nombre 'Orellana'}
                xml.jugador(:type => 'centrocampista'){xml.nombre 'Dani Benitez'}
                xml.jugador(:type => 'delantero'){xml.nombre 'Geijo'}
            }   
    }
end
# Guardado del builder en formato XML
xml = builder.to_xml
##################### FIN CREACIÓN XML #########################

######## Agregamos el XML a la colección Equipos
RestClient.put URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/Equipos/equipos.xml"), xml, {'Content-Type' => 'application/xml'}


######## Petición para mostrar sólo los PORTEROS
resultado = RestClient.get URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/Equipos?_query=//jugador[@type='portero']")

#Convierte XML recibido a formato XmlSimple -> se guarda en formato hash
porteros = XmlSimple.xml_in(resultado)

puts '**** PORTEROS ****'
i=0
while(i<porteros['exist:count'].to_i)
    puts 'Portero #'<<i.to_s<<' -> '<<porteros['jugador'][i]['nombre'].to_s
    i += 1
end
puts '*************************'

######## Petición para mostrar sólo los DELANTEROS
resultado = RestClient.get URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/Equipos?_query=//jugador[@type='delantero']")

#Convierte XML recibido a formato XmlSimple -> se guarda en formato hash
delanteros = XmlSimple.xml_in(resultado)

puts '**** DELANTEROS ****'
i=0
while(i<delanteros['exist:count'].to_i)
    puts 'Delantero #'<<i.to_s<<' -> '<<delanteros['jugador'][i]['nombre'].to_s
    i += 1
end
puts '*************************'

######## Petición para mostrar sólo PRIMER JUGADOR
resultado = RestClient.get URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/Equipos?_query=//equipo[@nombre='Real Madrid']/jugador&_howmany=1")
#Convierte XML recibido a formato XmlSimple -> se guarda en formato hash
primerJugadorMadrid = XmlSimple.xml_in(resultado)
resultado = RestClient.get URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/Equipos?_query=//equipo[@nombre='Barcelona']/jugador&_howmany=1")
primerJugadorBarcelona = XmlSimple.xml_in(resultado)
resultado = RestClient.get URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/Equipos?_query=//equipo[@nombre='Granada']/jugador&_howmany=1")
primerJugadorGranada = XmlSimple.xml_in(resultado)

puts '**** PRIMEROS JUGADORES ****'
puts 'Primer Jugador Real Madrid -> '<<primerJugadorMadrid['jugador'][0]['nombre'].to_s
puts 'Primer Jugador Barcelona -> '<<primerJugadorBarcelona['jugador'][0]['nombre'].to_s
puts 'Primer Jugador Granada -> '<<primerJugadorGranada['jugador'][0]['nombre'].to_s
puts '*************************'