#!/usr/bin/ruby
require 'rubygems'
require 'nokogiri'
require 'cgi'
require 'rexml/document' 
include REXML

cgi = CGI.new

partido = cgi['partido']
resultado = cgi['resultado']

#Confección del XML
builder = Nokogiri::XML::Builder.new do |xml|
    xml.quiniela {
            xml.partido partido
            xml.resultado resultado
    }
end

#Guardado del xml en disco (en la misma carpeta donde se encuentra el CGI)
log = File.new("modificaciones.xml", "w")
log.write(builder.to_xml)
log.close

#Se devuelve el XML al cliente
puts cgi.header( 'type' => 'xml','charset' => 'UTF-8')
puts builder.to_xml