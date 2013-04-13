#!/usr/bin/ruby
require 'rubygems'
require "RestClient"
require "XmlSimple"
require "uri"

### Apertura de ficheros RSS
rss1 = File.open("215rss1.xml")
rss2 = File.open("215rss2.xml")
rss3 = File.open("215rss3.xml")

### Agregamos los RSS a la base de datos a la colección RSS
RestClient.put URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/RSS/216rss1.xml"), rss1, {'Content-Type' => 'application/xml'}
RestClient.put URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/RSS/216rss2.xml"), rss2, {'Content-Type' => 'application/xml'}
RestClient.put URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/RSS/216rss3.xml"), rss3, {'Content-Type' => 'application/xml'}

######## Petición RSS 1
resultado = RestClient.get URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/RSS/216rss1.xml?_query=//item&_howmany=2")
dosPrimeros = XmlSimple.xml_in(resultado)

puts '**** Archivo RSS 1 ****'
puts '***** Item #1 *****'
puts 'Título ->' << dosPrimeros['item'][0]['title'].to_s
puts 'URL ->' << dosPrimeros['item'][0]['link'].to_s
puts '***** Item #2 *****'
puts 'Título ->' << dosPrimeros['item'][1]['title'].to_s
puts 'URL ->' << dosPrimeros['item'][1]['link'].to_s
puts '*************************'

######## Petición RSS 2
resultado = RestClient.get URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/RSS/216rss2.xml?_query=//item&_howmany=2")
dosPrimeros = XmlSimple.xml_in(resultado)

puts '**** Archivo RSS 2 ****'
puts '***** Item #1 *****'
puts 'Título ->' << dosPrimeros['item'][0]['title'].to_s
puts 'URL ->' << dosPrimeros['item'][0]['link'].to_s
puts '***** Item #2 *****'
puts 'Título ->' << dosPrimeros['item'][1]['title'].to_s
puts 'URL ->' << dosPrimeros['item'][1]['link'].to_s
puts '*************************'

######## Petición RSS 3
resultado = RestClient.get URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/RSS/216rss3.xml?_query=//item&_howmany=2")
dosPrimeros = XmlSimple.xml_in(resultado)

puts '**** Archivo RSS 3 ****'
puts '***** Item #1 *****'
puts 'Título ->' << dosPrimeros['item'][0]['title'].to_s
puts 'URL ->' << dosPrimeros['item'][0]['link'].to_s
puts '***** Item #2 *****'
puts 'Título ->' << dosPrimeros['item'][1]['title'].to_s
puts 'URL ->' << dosPrimeros['item'][1]['link'].to_s
puts '*************************'