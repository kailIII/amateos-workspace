#!/usr/bin/ruby
require 'rubygems'
require 'cgi'
require 'json/pure'
require 'rexml/document' 
include REXML

cgi = CGI.new
xmlClasificacion = Document.new(File.new('29equipos.xml'))
clasificacion = xmlClasificacion.root.elements

j = 1
clasificacionSalida= {}
while (j<=cgi['equipos'].to_i)
#while (j<=3)
	parametros = clasificacion[j].elements
	clasificacionSalida[parametros[1].text] = parametros[2].text
	j += 1	
end
  
json = JSON.generate(clasificacionSalida)
  
puts cgi.header
puts json
