# <font color='green'>compRarador</font> #

![http://amateos-workspace.googlecode.com/svn/trunk/PFC/Imagenes/logo.png](http://amateos-workspace.googlecode.com/svn/trunk/PFC/Imagenes/logo.png)

Este proyecto se realiza como proyecto final de carrera. Se inicia en octubre de 2010 y es finalizado y presentado en septiembre de 2012, obteniendo la calificación de 10 y siendo propuesto para Matrícula de Honor Cum Laude.

La idea fundamental de la que surge este proyecto viene dada por el contexto social, económico y tecnológico del momento. Durante el periodo de realización, España está inmersa en una grave crisis económica mientras que, por otro lado, se trata de uno de los países del mundo con una mayor implantación de teléfonos móviles en general y de smartphones en concreto. Es por ello que se decide crear un sistema que permita al usuario, mediante su teléfono móvil, consultar los precios de los productos que se venden habitualmente en los supermercados del país. Asimismo, se ofrece la posibilidad de crear listas de productos para poder realizar una comparación de los precios de la lista conjunta e incluso hacer uso de esta lista como si se tratase de una lista de la compra tradicional, en la que poder ir "tachando" los productos que ya han sido adquiridos.

### Arquitectura y características de las aplicaciones ###

![http://amateos-workspace.googlecode.com/svn/trunk/PFC/Imagenes/arquitectura.png](http://amateos-workspace.googlecode.com/svn/trunk/PFC/Imagenes/arquitectura.png)

La arquitectura del sistema es compleja y consta de los siguientes elementos:

  * **Scraper**: aplicación programada utilizando el lenguaje JRuby que permite obtener toda la información necesaria acerca de productos y sus precios para los diferentes supermercados. Para ello, dicha aplicación realiza un rastreo automatizado de una conocida página web que ofrece dicha información. Entre otras cosas, permite programar la extracción de información a una hora y frecuencia impuestas por el usuario.

  * **Clientes móviles**: aplicación desarrollada a partir del uso del framework phonegap y que es posible ejecutar en la mayoría de dispositivos móviles de última generación del mercado. Entre las bondades de dicho framework destaca la necesidad de realizar un único código HTML/Javascript/CSS que será ejecutado en todas las plataformas, no siendo necesaria la programación personalizada para cada una de las plataformas. Sus funciones más destacadas se resumen a continuación:
    * Comparación del precio de una lista de productos de supermercado.
    * Mostrado de información acerca del producto: marca, descripción, formato del envase, imagen y precio en Carrefour, Mercadona, Hipercor y El Corte Inglés.
    * Búsqueda de productos mediante categorías, texto o escaneo del código de barras del producto mediante la cámara de fotos del teléfono.
    * Creación y gestión de listas de productos.
    * Marcado y gestión de productos favoritos, modo que el usuario puede acceder rápidamente a los productos que haya calificado como favoritos.
    * Agregado de productos a una lista mediante la agitación del teléfono.

  * **Servidor**: aplicación que gestiona el acceso a la base de datos MySQL. Recibe las peticiones de la aplicación scraper y de los clientes móviles para almacenar/extraer información de la base de datos. Para el desarrollo de dicha aplicación se programan dos alternativas, con objeto de realizar una posterior comparación de rendimiento y características. La primera alternativa, la más simple, está formada por un conjunto de scripts PHP que atienden las peticiones. Por otro lado, la segunda de las alternativas se realiza desarrollando una interfaz RESTful mediante la tecnología JavaEE. De esta forma, el servidor atiende peticiones en formato XML, generando las respuestas oportunas.

### Presentación ###

_Nota: para ver la presentación hacer click sobre el cuadro de debajo de este texto y utilizar las teclas ← y → para pasar las diapositivas hacia atrás y delante. Se recomienda usar Google Chrome para la correcta visualización._

&lt;wiki:gadget url="http://amateos-workspace.googlecode.com/svn/trunk/PFC/Imagenes/comprarador\_presentacion\_gadget.xml" width="1024" height="768" /&gt;

[Abrir la presentación en una pestaña nueva](http://amateos-workspace.googlecode.com/svn/trunk/PFC/Presentacion/index.html)

### Vídeos ###

**Vídeo demostrativo de las  funciones del cliente móvil:** [Ver vídeo en Vimeo](https://vimeo.com/55390903)
<wiki:gadget url="http://amateos-workspace.googlecode.com/svn/trunk/site/vimeo-gadget/gcVideo.xml" up\_video="https://vimeo.com/55390903" width="640" height="384"/>


**Todos los vídeos (scraper y clientes Android e iOS**: [Ver en Youtube](http://www.youtube.com/playlist?list=PL4PPb0PVAuVTtswsLLWFTrKux6uzD7hSK")


### Enlaces y más info ###
  * **Documentación**: [Descargar](http://code.google.com/p/amateos-workspace/downloads/detail?name=PFC_Alberto_Mateos.pdf)

  * **Código Fuente**: [Ver](http://code.google.com/p/amateos-workspace/source/browse/trunk/PFC/)