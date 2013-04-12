#!/usr/bin/ruby
require 'rexml/document'
require 'rexml/streamlistener'
include REXML

file=ARGV[0]

class Escucha
  include StreamListener
  @@tags = { 'micasa' => 'home',
			 'mueble' => 'furniture',
			 'casa' => 'house',
			 'puerta' => 'door',
			 'habitacion' => 'room'}
	
  muebles = {'Silla' => 'Chair',
			   'Mesa' => 'Table',
			   'Cama' => 'Bed',
			   'Armario' => 'Wardrove'}


  def tag_start(name, attributes)
    puts "<#{@@tags[name]}>"
  end

  def tag_end(name)
    puts "</#{@@tags[name]}>"
  end

  def text(text)
    puts text
  end

end

listener = Escucha.new
parser = Parsers::StreamParser.new(File.new(file), listener)
parser.parse