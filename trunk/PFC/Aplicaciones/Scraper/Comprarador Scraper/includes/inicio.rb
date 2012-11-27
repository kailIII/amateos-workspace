#! /Applications/jruby-1.6.3/bin/jruby
# encoding: UTF-8

require 'java'

import javax.imageio.ImageIO
import javax.swing.JFrame
#import java.io.File;


#############################################################################################################
###### CLASE IMAGEPANEL: crea un JPanel y muestra la imagen que se le pasa como argumento
#############################################################################################################
class ImagePanel < javax.swing.JPanel
  def initialize(image)
    super()
    @image = image
  end

  def getPreferredSize
    java.awt.Dimension.new(@image.width, @image.height)
  end

  def paintComponent(graphics)
    graphics.draw_image(@image, 0, 0, nil)
  end
end

#############################################################################################################
###### CLASE PANTALLAINICIO: abre y muestra el logo de la aplicación en uun JPanel
#############################################################################################################
class PantallaInicio
    
    def initialize
        logo = ImageIO.read(java.io.File.new("imagenes/logo.gif"))
        @frame = JFrame.new("Comprarador Scraper")
        @frame.set_bounds 0, 0, logo.width, logo.height+25
        @frame.add(ImagePanel.new(logo))
        @frame.visible = true 
        
    end
    
    
    ### Función noMostrar: deja de mostrar el JPanel que incluye el logo
    ########################################################################################
    def noMostrar
        @frame.visible = false
    end
    
end
     
    
    
    
