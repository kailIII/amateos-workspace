#!/usr/bin/ruby
require 'rexml/document' 
include REXML 
documento = ARGV[0] 
file = File.new(documento) 
doc = Document.new(file) 
doc.root.each_element('habitacion') { |habitacion| 
	puts habitacion.attribute("id") }