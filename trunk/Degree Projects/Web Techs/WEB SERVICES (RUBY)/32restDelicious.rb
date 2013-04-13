#!/usr/bin/ruby
require 'rubygems'
require 'RestClient'
require 'uri'
require 'xmlsimple'

url = URI.escape('https://aap_ugr:XXXX@api.del.icio.us/v1/posts/recent')
resultado = RestClient.get url

#Convierte XML recibido a formato XmlSimple -> se guarda en formato hash
xml = XmlSimple.xml_in(resultado)

puts ' ---- POSTS RECIENTES ----'
i = 1 
while (i <= xml.length + 1)
  puts 'Link -> ' << xml['post'][i]['href']
  puts 'Descripción -> ' << xml['post'][i]['description']
  puts '************************************'
  i += 1
end