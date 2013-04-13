#!/usr/bin/ruby
require 'rubygems'
require 'cgi'
require "uri"
require "RestClient"

rutas={'11'=>'Aguas/Agua con gas','12'=>'Aguas/Agua con sabores','13'=>'Aguas/Agua sin gas','21'=>'Cervezas/Especialidades','22'=>'Cervezas/Importac','23'=>'Cervezas/Light y sin alcohol',
       '24'=>'Cervezas/Nacional','25'=>'Cervezas/Negra','31'=>'Licores/Ginebra','32'=>'Licores/Ron','33'=>'Licores/Tequila','34'=>'Licores/Vodka'}
       
cgi = CGI.new
xml = RestClient.get URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/marketscraper/Bebidas/"<<rutas[cgi['ruta'].to_s]<<"/"<<cgi['supermercado'].to_s<<".xml")
puts cgi.header('type' => 'application/xml')
puts xml
