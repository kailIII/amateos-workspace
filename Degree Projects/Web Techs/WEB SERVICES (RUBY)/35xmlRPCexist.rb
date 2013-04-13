#!/usr/bin/ruby
require 'rubygems'
require 'uri'
require 'nokogiri'
require "xmlrpc/client"

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


################# LLAMADA XML-RPC #################
# Make an object to represent the XML-RPC server.
server = XMLRPC::Client.new2( "http://admin:barrizal@localhost:8080/exist/xmlrpc")

# Call the remote server and get our result
result = server.call("createCollection", "/db/coleccionRPC")
puts 'Creación colección: Respuesta -> '<<result.to_s
result = server.call("parse",xml,"/db/coleccionRPC/mixml.xml")
puts 'Inserción de XML: Respuesta -> '<<result.to_s