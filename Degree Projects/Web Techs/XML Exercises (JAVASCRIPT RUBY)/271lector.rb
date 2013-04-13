#!/usr/bin/ruby
require 'rss/1.0' 
require 'rss/2.0' 
documento = ARGV[0] 
file = File.new(documento) 
rss = RSS::Parser.parse(file, false) 
rss.channel.items.each do |i| 
	puts i.title 
	puts i.pubDate
end 