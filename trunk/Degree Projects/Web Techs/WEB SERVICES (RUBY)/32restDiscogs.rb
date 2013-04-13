#!/usr/bin/ruby
require 'rubygems'
require 'zlib'
require 'open-uri'
require 'xmlsimple'

# Se introduce como argumento el nombre del artista o grupo a buscar
nombre = ARGV[0]

# Confección de la URL para la llamada a la interfaz REST. Es necesario el uso de un API-KEY que es proporcionado al registrarse en la web
url = URI.escape("http://www.discogs.com/artist/"<<nombre<<"?f=xml&api_key=XXXXXXXXXXX")
headers = {'Accept-Encoding' => 'gzip', 'User-Agent' => 'MyDiscogsClient/1.0 +http://mydiscogsclient.org'}

# Extracción de los datos. Los datos se envían en formato XML comprimido mediante Gzip.
# El siguiente código ha sido extraído de la documentación de la web -> http://www.discogs.com/help/api
begin
    response = open(url, headers)
    begin
        data = Zlib::GzipReader.new(response)
    rescue Zlib::GzipFile::Error
        response.seek(0)
        data = response.read
    end
rescue OpenURI::HTTPError => e
    data = e.io.read
end

# Conversión de la respuesta a hash
xml = XmlSimple.xml_in(data)
discos = xml['artist'][0]['releases'][0]['release']

# Salida
puts ' ---- ÚLTIMOS 5 DISCOS DE ' << nombre << ' ----'
i = 1 
len = discos.length
while (i <= 5)
  puts 'Título -> ' << discos[len-i]['title'].to_s
  puts 'Formato -> ' << discos[len-i]['format'].to_s
  puts 'Sello -> ' << discos[len-i]['label'].to_s
  puts 'Año -> ' << discos[len-i]['year'].to_s
  puts '************************************'
  i += 1
end

