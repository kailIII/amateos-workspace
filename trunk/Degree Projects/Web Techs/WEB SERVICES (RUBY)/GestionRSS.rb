class GestionRSS

$coleccion_rss = []

  def initialize()
    $coleccion_rss.push(["Ugr","Universidad","http://secretariageneral.ugr.es/pages/tablon/*/rss/noticias-canal-ugr"])
    $coleccion_rss.push(["Marca","Deportes","http://marca.feedsportal.com/rss/portada.xml"])
    $coleccion_rss.push(["El Pais","Noticias","http://www.elpais.com/rss/feed.html?feedId=1022"])
  end

  def add_rss(nombre,tema,url) 
    $coleccion_rss.push([nombre, tema,url])
  end

  def buscar( tema )
      i=0
      while(i<3)
          if ($coleccion_rss[i][1] == tema)
              return $coleccion_rss[i]
          end 
      i += 1
	  end
  end
end
