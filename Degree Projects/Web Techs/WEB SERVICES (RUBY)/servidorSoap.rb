#!/usr/bin/ruby 

require 'soap/rpc/standaloneServer' 
require 'GestionRSS' 

NS = 'http://aap-rolimat.es/buscaRSS' 

class Server2 < SOAP::RPC::StandaloneServer 
    def on_init 
        coleccion = GestionRSS.new() 
        add_method(coleccion, 'buscar', 'tema') 
    end 
end 
svr = Server2.new('coleccion', NS, '0.0.0.0', 55555) 
trap('INT') { svr.shutdown }
svr.start