#!/usr/bin/ruby
require 'rubygems'
require 'rss/2.0'
require 'cgi'
require 'rexml/document' 
include REXML

cgi = CGI.new
xmlClasificacion = Document.new(File.new('29equipos.xml'))
clasificacion = xmlClasificacion.root.elements

content = RSS::Rss.new('2.0')
channel = RSS::Rss::Channel.new

channel.title = "Clasificación Primera División"
channel.link = "www.aap.es"
channel.description = "Esta es la RSS generada como prueba para AAP. Se muestran tantos equipos como se indique en el parámetro equipos que se pasa al .cgi"
#channel.item.do_sort = true "<<<=Cannot sort

j = 1
while (j<=cgi['equipos'].to_i)
    i = RSS::Rss::Channel::Item.new
        
        parametros = clasificacion[j].elements
        i.title = parametros[1].text
        i.link = parametros[2].text
        i.guid = RSS::Rss::Channel::Item::Guid.new
        i.guid.content = j.to_s
        i.guid.isPermaLink = true
        i.description = "Más información en: "<<parametros[2].text
        channel.items << i
        j += 1
end

content.channel = channel
puts cgi.header

puts content.to_s