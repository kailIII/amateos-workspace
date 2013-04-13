#!/usr/bin/ruby
require 'rexml/document' 
include REXML

xml = Document.new(File.new('212habitaciones.xml'))
contador=0

XPath.each(xml,'micasa/habitacion/puerta/@a'){ |puerta|
	if(puerta.to_s==ARGV[0])
		contador += 1
	end
}

puts "El número de habitaciones con puerta a "<< ARGV[0].to_s << " es -> "<< contador.to_s