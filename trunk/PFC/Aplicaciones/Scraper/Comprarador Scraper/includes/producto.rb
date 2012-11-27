#############################################################################################################
###### CLASE PRODUCTO: define todos los campos propios de un producto
#############################################################################################################

class Producto
    attr_accessor :marca
    attr_accessor :descripcion
    attr_accessor :formato
    attr_accessor :imagen
    attr_accessor :categoria
    attr_accessor :subcategoria
    attr_accessor :subsubcategoria
    attr_accessor :precio_mercadona
    attr_accessor :precio_relativo_mercadona
    attr_accessor :precio_carrefour
    attr_accessor :precio_relativo_carrefour
    attr_accessor :precio_hipercor
    attr_accessor :precio_relativo_hipercor
    attr_accessor :precio_corteIngles
    attr_accessor :precio_relativo_corteIngles
    attr_accessor :oferta_mercadona
    attr_accessor :oferta_carrefour
    attr_accessor :oferta_hipercor
    attr_accessor :oferta_corteIngles
    
    
    ### Función initialize: define el código a ejecutar cuando se crea un objeto de la clase
    ########################################################################################
    def initialize(_marca, _descripcion, _formato, _imagen,_categoria, _subcategoria, _subsubcategoria)  
      
      # atributos   
      @marca = _marca  
      @descripcion = _descripcion
      @formato = _formato
      @imagen = _imagen
      @categoria = _categoria
      @subcategoria = _subcategoria
      @subsubcategoria = _subsubcategoria
      @precio_mercadona = "0.00"
      @precio_carrefour = "0.00"
      @precio_hipercor = "0.00"
      @precio_corteIngles = "0.00"
      
    end
    
    ### Función setPrecioMercadona: asigna los valores relativos al precio de Mercadona a un
    ### producto
    ########################################################################################
    def setPrecioMercadona(precio, precio_relativo, oferta)
        @precio_mercadona = precio
        @precio_relativo_mercadona = precio_relativo
        @oferta_mercadona = oferta
    end
    
    ### Función setPrecioCarrefour: asigna los valores relativos al precio de Carrefour a un
    ### producto
    ########################################################################################
    def setPrecioCarrefour(precio, precio_relativo, oferta)
        @precio_carrefour = precio
        @precio_relativo_carrefour = precio_relativo
        @oferta_carrefour = oferta
    end
    
    ### Función setPrecioHipercor: asigna los valores relativos al precio de Hipercor a un
    ### producto
    ########################################################################################
    def setPrecioHipercor(precio, precio_relativo, oferta)
        @precio_hipercor = precio
        @precio_relativo_hipercor = precio_relativo
        @oferta_hipercor = oferta
    end
    
    ### Función setPrecioCorteIngles: asigna los valores relativos al precio de Corte Inglés 
    ### a un producto
    ########################################################################################
    def setPrecioCorteIngles(precio, precio_relativo, oferta)
        @precio_corteIngles = precio
        @precio_relativo_corteIngles = precio_relativo
        @oferta_corteIngles = oferta
    end
    
end