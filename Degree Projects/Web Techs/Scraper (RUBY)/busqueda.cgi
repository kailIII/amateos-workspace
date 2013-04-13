#!/usr/bin/ruby
require 'rubygems'
require 'cgi'
require "uri"
require "RestClient"

cgi = CGI.new

#se realiza la búsqueda mediante el xquery correspondiente

xml = RestClient.get URI.escape("http://admin:barrizal@localhost:8080/exist/rest/db/marketscraper/supermercados/search"<<cgi['tipo'].to_s<<".xq?q="<<cgi['q'].to_s<<"&super="<<cgi['super'].to_s)
puts cgi.header('type' => 'application/xml')
puts xml
