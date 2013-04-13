xquery version "1.0";
declare option exist:serialize "method=xml media-type=application/xml";

(: obtención de parámetros desde la URL :)
let $q := request:get-parameter('q', '')
let $super := request:get-parameter('super', '')

let $coleccion := 
	if ($super='Hipercor') then '/db/marketscraper/supermercados/Hipercor'
	else if ($super='Carrefour') then '/db/marketscraper/supermercados/Carrefour'
	else if ($super='Mercadona') then '/db/marketscraper/supermercados/Mercadona'
	else if ($super='CorteIngles') then '/db/marketscraper/supermercados/CorteIngles'
	else ''

	return
	<busqueda>{ 
		for $producto in collection($coleccion) //producto[matches(descripcion,lower-case($q))]
		return
		<producto><marca>{data($producto/marca)}</marca>
		<descripcion>{data($producto/descripcion)}</descripcion>
		<precio>{data($producto/precio)}</precio>
		<precio_relativo>{data($producto/precio_relativo)}</precio_relativo>
		<imagen>{data($producto/imagen)}</imagen>
		</producto>
	}</busqueda>