#!/usr/bin/ruby
require 'rubygems'
require 'simple-rss'
require 'open-uri'

feed = SimpleRSS.parse open('http://recetasdelamiamama.blogspot.com/feeds/posts/default')

feed.channel.items.each do |i| 
	puts "- " << i.title 
end 