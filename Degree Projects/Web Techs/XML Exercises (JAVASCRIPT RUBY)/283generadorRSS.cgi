#!/usr/bin/perl

use CGI qw(:standard);

print header( -type => 'application/xml+rss' );

print<<EOC;
<?xml version="1.0" encoding="iso-8859-1"?>
<rss version="2.0" xmlns:myns="http://aap.alberto.com/ns/">
<channel>
    <title><![CDATA[RSS de Alberto Mateos]]></title>
    <link><![CDATA[http://geneura.ugr.es/~jmerelo/asignaturas/AAP]]></link>
    <description><![CDATA[RSS de prueba]]></description>
    <lastBuildDate>Mon, 11 Apr 2011 13:34:25 +0200</lastBuildDate>
    <language>es-es</language>
    <copyright><![CDATA[Ninguno]]></copyright>
    <item>
        <title><![CDATA[Este es el título del item 1]]></title>
        <link><![CDATA[http://www.google.com/]]></link>
        <description><![CDATA[Descripción del item 1]]></description>
        <author><![CDATA[Alberto Mateos]]></author>
        <pubDate><![CDATA[Mon, 15 Apr 2011 11:30:00 +0200]]></pubDate>
    </item>
    <item>
        <title><![CDATA[Este es el título del item 2]]></title>
        <link><![CDATA[http://www.google.com/]]></link>
        <description><![CDATA[Descripción del item 2]]></description>
        <author><![CDATA[Alberto Mateos]]></author>
        <pubDate><![CDATA[Mon, 15 Apr 2011 11:31:00 +0200]]></pubDate>
    </item>
</channel>
EOC

