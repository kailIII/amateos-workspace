#!/usr/bin/ruby
require 'rexml/document' 
include REXML

xmlClasificacion = Document.new(File.new('212equipos.xml'))

puts "Los equipos clasificados para la Champions son:"

XPath.each(xmlClasificacion,'//posicion'){ |posicion|
	if (posicion.text.to_i <= 4)
		puts posicion.text << "-> " << posicion.parent[3].text
	end
}

