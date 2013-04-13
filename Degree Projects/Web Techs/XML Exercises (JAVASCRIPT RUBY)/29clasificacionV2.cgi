#!/usr/bin/ruby
require 'rubygems'
require 'nokogiri'
require 'cgi'
require 'rexml/document' 
include REXML

cgi = CGI.new
xmlClasificacion = Document.new(File.new('29equipos.xml'))
clasificacion = xmlClasificacion.root.elements

#Confección del XML
j = 1
builder = Nokogiri::XML::Builder.new do |xml|
    xml.clasificacion {
        while (j<=cgi['equipos'].to_i)
            parametros = clasificacion[j].elements
            xml.equipo {
                xml.posicion j.to_s
                xml.nombre parametros[1].text
                xml.link parametros[2].text
            }
            j += 1
        end
    }
end
  
puts cgi.header
puts builder.to_xml
