#!/usr/bin/ruby
require 'rss/1.0' 
require 'rss/2.0'

require 'soap/rpc/driver'
proxy = SOAP::RPC::Driver.new("http://localhost:55555","http://aap-rolimat.es/buscaRSS")
proxy.add_method('buscar','tema')

rssSoap = proxy.buscar(ARGV[0])

content = ""
open(rssSoap[2]) do |s| 
	content = s.read 
end

rss = RSS::Parser.parse(content, false)

puts "RSS titulo: "<< rss.channel.title.to_s
puts "RSS link: "<< rss.channel.link.to_s
puts "RSS descripcion: "<< rss.channel.description.to_s