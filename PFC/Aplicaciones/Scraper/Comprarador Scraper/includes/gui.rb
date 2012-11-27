#! /Applications/jruby-1.6.3/bin/jruby
# encoding: UTF-8

require 'rubygems'
require 'profligacy/swing'
require 'profligacy/lel'
require 'rufus/scheduler'
require 'includes/scraper'
require 'includes/threads'
require 'java'



#############################################################################################################
###### CLASE GUI: engloba todos los elementos de la interfaz y ejecuta las funciones necesarias para 
######            el funcionamiento del programa
#############################################################################################################

class Gui
    include_package 'javax.swing'
    include_package 'java.awt'
    include Profligacy


    ### Función initialize: define el código a ejecutar cuando se crea un objeto de la clase
    ########################################################################################
    def initialize
        
        puts "Obteniendo categorías para crear gui"
        $categorias = $scraperCategorias.obtenerCategorias
        #$categorias = ["Animales", "Aperitivos y frutos secos", "Arroz, pastas, harinas y legumbres", "Bazar", "Bebe", "Caldos, sopas y pur\303\251s", "Bebidas", "Carnicer\303\255a y pescader\303\255a", "Charcuter\303\255a", "Congelados y helados", "Conservas, aceites y condimentos", "Desayuno, dulces, pan, boller\303\255a y pasteler\303\255a", "Diet\303\251ticos", "Droguer\303\255a y limpieza", "Frutas y verduras", "Internacional", "L\303\241cteos y huevos", "Parafarmacia", "Perfumer\303\255a e higiene", "Platos preparados", "Quesos"]
        
        #debido a las reestricciones del framework utilizado para programar la Gui del programa es necesario definir todos los elementos
        #uno a uno, por lo que es obligatorio conocer el número de elementos de antemano. Es por ello que se ha asumido que el número de
        #categorias será de 21. En caso contrario (la ejecución provocará un error), se muestra mensaje de error indicando que es necesario
        #modificar la interfaz.
                
        if($categorias.size == 21)
            
            ## variables de estado que controlan si se está en proceso de ejecución del scraping o no
            @scrapingIniciadoCategorias = false
            @scrapingIniciadoMenu = false

            #se inicializa el array que almacenará el estado de los checkboxes con todas las posiciones a false
            $categoriasSeleccionadas = Array.new
            $categorias.size.times do
                $categoriasSeleccionadas.push false
            end
            
            #se inicia variable que indica que se está realizando un scraping para el modo "obtener ahora"
            #se comparte el objeto sobre el que se realiza el scraping para ahorrar tiempo de inicio
            # por tanto, no se puede ejecutar el scraping de menú y de categorías a la vez
            $scrapingIniciado = false
            
            #se inicializa el array asociativo que almacenará el estado de los comboboxes utilizados para la programación del scraping
            #inician todos los elementos al valor -1
            $combosProgramacion = {"diasCategorias" => 2, "horaCategorias" => 0, "diasMenu" => 2, "horaMenu" => 0}

            #se crea el JFrame que contendrá todos los elementos
            @ui = Swing::Build.new JFrame, :panel do |c,i|
                c.panel = crearLayoutPrincipal
            end

            @frame = @ui.build("Comprarador Scraper") 
            @frame.set_resizable false
            
            #se inicializa el objeto para el scheduler
            $scheduler = Rufus::Scheduler.start_new
            
        else
            puts "Error al iniciar la GUI: el número de categorias obtenido (#{$categorias.size}) no se corresponde con el diseño de la interfaz"
        end
    end


    #############################################################
    ###### CREACIÓN DE ELEMENTOS DE LA INTERFAZ
    #############################################################
    
    ### Función crearLayoutPrincipal: define el layout principal de la interfaz
    ########################################################################################
    def crearLayoutPrincipal
        
        # se define el layout
        layout="
            [controles]
            [consola]
            [<estado]
            "
            
        # se crea el layout    
        layoutPrincipal = Swing::LEL.new(JPanel,layout) do |c,i|
            c.controles = crearLayoutControles
            c.controles.setBorder BorderFactory.createEtchedBorder
            c.consola = crearLayoutConsola
            c.consola.setBorder BorderFactory.createEtchedBorder
            c.estado = crearLayoutEstado
            c.estado.setBorder BorderFactory.createEtchedBorder
        end
        layoutPrincipal.build
    end
    
    ### Función crearLayoutPrincipal: define el layout de la sección de consola
    ########################################################################################
    def crearLayoutConsola
        
        # se define el layout
        layout="
            [labelConsola]
            [(400,100)consola]
            "
            
        # se crea el layout    
        layoutConsola = Swing::LEL.new(JPanel,layout) do |c,i|
            c.labelConsola = JLabel.new("CONSOLA")
            c.labelConsola.setFont Font.new "Arial Rounded MT Bold", Font::BOLD, 18
            c.labelConsola.setForeground Color.new 65, 60, 110
            
            @textoConsola = JTextArea.new
            @textoConsola.set_editable false
            c.consola = JScrollPane.new(@textoConsola)
        end
        layoutConsola.build
    end
    
    ### Función crearLayoutEstado: define el layout de la sección de estado
    ########################################################################################
    def crearLayoutEstado
        
        # se define el layout
        layout="
            [labelEstado]
            [elementosEstado]
            "
        
        # se crea el layout
        layoutEstado = Swing::LEL.new(JPanel,layout) do |c,i|
            c.labelEstado = JLabel.new("ESTADO")
            c.labelEstado.setFont Font.new "Arial Rounded MT Bold", Font::BOLD, 18
            c.labelEstado.setForeground Color.new 65, 60, 110
            
            # se hace la llamada para crear los elementos que habrá en la sección estado
            c.elementosEstado = crearElementosEstado
        end
        layoutEstado.build
    end
    
    ### Función crearElementosEstado: crea todos los elementos que se inclueyen en la sección estado
    ########################################################################################
    def crearElementosEstado
        
        # se define el layout
        layout="
                [(30)<labelEstadoCategorias|<*textoEstadoCategorias|(30)<labelEstadoMenu|<*textoEstadoMenu]
                "
        # se crea el layout
        layoutEstado = Swing::LEL.new(JPanel,layout) do |c,i|
            c.labelEstadoCategorias = JLabel.new("Categorías: ")
            c.labelEstadoCategorias.setFont Font.new "Arial", Font::BOLD, 14
            c.labelEstadoCategorias.setForeground Color.new 200, 100, 100
            
            c.textoEstadoCategorias = JLabel.new("detenido.")  
            @estadoCategorias = c.textoEstadoCategorias
            
            c.labelEstadoMenu = JLabel.new("Menú: ")
            c.labelEstadoMenu.setFont Font.new "Arial", Font::BOLD, 14
            c.labelEstadoMenu.setForeground Color.new 200, 100, 100
            
            c.textoEstadoMenu = JLabel.new("detenido.")  
            @estadoMenu = c.textoEstadoMenu
            
        end
        layoutEstado.build
        
    end

    ### Función crearLayoutControles: define el layout de la sección de controles
    #######################################################################################
    def crearLayoutControles

        # se define el layout
        layout="
            [labelConfiguracion]
            [<labelCategorias]
            [<checkboxes]
            [layoutProgramacionCategorias]
            [<labelMenu]
            [layoutProgramacionMenu]
            "
        # se crea el layout
        layoutControles = Swing::LEL.new(JPanel,layout) do |c,i|

            c.labelConfiguracion = JLabel.new("SCRAPER")
            c.labelConfiguracion.setFont Font.new "Arial Rounded MT Bold", Font::BOLD, 18
            c.labelConfiguracion.setForeground Color.new 65, 60, 110
            
            c.labelCategorias = JLabel.new("CATEGORÍAS")
            c.labelCategorias.setFont Font.new "Arial Rounded MT Bold", Font::BOLD, 14
            c.labelCategorias.setForeground Color.new 200, 100, 100
            
            c.checkboxes = crearCheckboxes
            #se hace la llamada para crear la sección de programación de categorías
            c.layoutProgramacionCategorias = crearLayoutProgramacionCategorias
            
            c.labelMenu = JLabel.new("MENÚ")
            c.labelMenu.setFont Font.new "Arial Rounded MT Bold", Font::BOLD, 14
            c.labelMenu.setForeground Color.new 200, 100, 100
            #se hace la llamada para crear la sección de programación de menú
            c.layoutProgramacionMenu = crearLayoutProgramacionMenu

        end
        

        layoutControles.build
    end
    
    ### Función crearLayoutProgramacionCategorias: define el layout de la sección de 
    ### programación del scheduler para las categorias
    ########################################################################################
    def crearLayoutProgramacionCategorias
        
        # se define el layout
        layout="
          [programacion]
          [botonesCategorias]
          "    
        # se crea el layout  
        layoutProgramacionCategorias = Swing::LEL.new(JPanel, layout) do |c,i|
            
            #se hace la llamada para crear los botones de categorías y los elementos de
            #la sección de programación
            c.botonesCategorias = crearBotonesCategorias
            c.programacion = elementosProgramacionCategorias
            
            
        end
        layoutProgramacionCategorias.build
    end
    
    ### Función crearLayoutProgramacionMenu: define el layout de la sección de programación 
    ### del scheduler para el menú
    ########################################################################################
    def crearLayoutProgramacionMenu
        # se define el layout
        layout="
            [programacion]
            [botonesMenu]
            "
        # se crea el layout    
        layoutProgramacionCategorias = Swing::LEL.new(JPanel, layout) do |c,i|
            #se hace la llamada para crear los botones de menú y los elementos de
            #la sección de programación
            c.botonesMenu = crearBotonesMenu
            c.programacion = elementosProgramacionMenu
            
            
        end
        layoutProgramacionCategorias.build
    end
    
    ### Función elementosProgramacionCategorias: agrega los comboboxes y labels a la sección 
    ### de programación de categorias
    ######################################################################################## 
    def elementosProgramacionCategorias
        # se define el layout
        layout="
        [>etiqueta1|<comboDias|<*etiqueta2|>etiqueta3|<*comboHora]
        " 
        # se define el layout
        elementos = Swing::LEL.new(JPanel, layout) do |c,i|
            #Etiquetas
            c.etiqueta1    = JLabel.new "Cada" 
            c.etiqueta2    = JLabel.new "días"
            c.etiqueta3    = JLabel.new "Hora de inicio" 

            #ComboDias -> Frecuencia en días del scraping de menú
            c.comboDias    = JComboBox.new
            2.upto(10) { |i| c.comboDias.add_item i.to_s }
            
            #se agrega el listener
            c.comboDias.add_item_listener do |e|
                $combosProgramacion["diasCategorias"] = c.comboDias.get_selected_index+2
            end            
            @comboDiasCategorias = c.comboDias
            
            #ComboHora -> Hora de inicio del scraping de menú
            c.comboHora    = JComboBox.new
            24.times { |i| c.comboHora.add_item i.to_s<<":00"}
            
            #se agrega el listener
            c.comboHora.add_item_listener do |e|
                $combosProgramacion["horaCategorias"] = c.comboHora.get_selected_index
            end
            @comboHoraCategorias = c.comboHora
            

        end
        elementos.build
    end
    
    ### Función elementosProgramacionMenu: agrega los comboboxes y labels a la sección de 
    ### programación del menú
    ########################################################################################
    def elementosProgramacionMenu
        # se define el layout
        layout="
        [>etiqueta1|<comboDias|<*etiqueta2|>etiqueta3|<*comboHora]
        " 
        # se crea el layout
        elementos = Swing::LEL.new(JPanel, layout) do |c,i|
            #Etiquetas
            c.etiqueta1    = JLabel.new "Cada" 
            c.etiqueta2    = JLabel.new "días"
            c.etiqueta3    = JLabel.new "Hora de inicio" 

            #ComboDias -> Frecuencia en días del scraping de menú
            c.comboDias    = JComboBox.new
            2.upto(10) { |i| c.comboDias.add_item i.to_s }
            #se agrega el listener
            c.comboDias.add_item_listener do |e|
                $combosProgramacion["diasMenu"] = c.comboDias.get_selected_index+2
            end
            @comboDiasMenu = c.comboDias
            
            #ComboHora -> Hora de inicio del scraping de menú
            c.comboHora    = JComboBox.new
            24.times { |i| c.comboHora.add_item i.to_s<<":00"}
            #se agrega el listener
            c.comboHora.add_item_listener do |e|
                $combosProgramacion["horaMenu"] = c.comboHora.get_selected_index
            end
            @comboHoraMenu = c.comboHora

        end
        elementos.build
    end

    ### Función crearCheckBoxes: Crea el JPanel que contiene los checkboxes para seleccionar 
    ### las categorias
    ########################################################################################
    def crearCheckboxes
        
        # se define el layout
        layoutCheckBoxes = "
                [<box0|<box1|<box2]
                [<box3|<box4|<box5]
                [<box6|<box7|<box8]
                [<box9|<box10|<box11]
                [<box12|<box13|<box14]
                [<box15|<box16|<box17]
                [<box18|<box19|<box20]
                "
        # se crea el layout
        checkBoxes = Swing::LEL.new(JPanel, layoutCheckBoxes) do |c,i|
            c.box0 = JCheckBox.new($categorias[0],false)
            c.box1 = JCheckBox.new($categorias[1],false)
            c.box2 = JCheckBox.new($categorias[2],false)
            c.box3 = JCheckBox.new($categorias[3],false)
            c.box4 = JCheckBox.new($categorias[4],false)
            c.box5 = JCheckBox.new($categorias[5],false)
            c.box6 = JCheckBox.new($categorias[6],false)
            c.box7 = JCheckBox.new($categorias[7],false)
            c.box8 = JCheckBox.new($categorias[8],false)
            c.box9 = JCheckBox.new($categorias[9],false)
            c.box10 = JCheckBox.new($categorias[10],false)
            c.box11 = JCheckBox.new($categorias[11],false)
            c.box12 = JCheckBox.new($categorias[12],false)
            c.box13 = JCheckBox.new($categorias[13],false)
            c.box14 = JCheckBox.new($categorias[14],false)
            c.box15 = JCheckBox.new($categorias[15],false)
            c.box16 = JCheckBox.new($categorias[16],false)
            c.box17 = JCheckBox.new($categorias[17],false)
            c.box18 = JCheckBox.new($categorias[18],false)
            c.box19 = JCheckBox.new($categorias[19],false)
            c.box20 = JCheckBox.new($categorias[20],false)
            
            @checkBoxes=Array.new
            

            #se agrega el listener a cada uno de los checkboxes. En el array $categoriasSeleccionadas
            #se guarda true o false en función de si el checkbox correspondiente ha sido seleccionado o no
            c.each do |p|
                
                #se almacenan todos los checkboxes en un array para poder habilitarlos y deshabilitarlos
                @checkBoxes.push p
                
                #se agrega el listener
                p.add_action_listener do |e| 
                    
                    #se obtiene el indice de la categoria que ha sido seleccionada/deseleccionada
                    index = $categorias.index(e.get_source.text)

                    if(p.is_selected)
                        $categoriasSeleccionadas[index]=true
                    else
                        $categoriasSeleccionadas[index]=false
                    end
                end
            end

        end

        checkBoxes.build   
        
    end

    ### Función crearBotonesMenu: Crea el JPanel donde se encuentran los botones relativos al menú
    ########################################################################################
    def crearBotonesMenu
        
        #se crea el panel con los botones y se asignan los listeners para los eventos de los botones
        botones = Swing::LEL.new(JPanel, "[programar|obtener]") do |c,i|
            c.programar = JButton.new "Programar"
            @botonProgramarMenu = c.programar
            c.obtener = JButton.new "Obtener ahora"
            @botonObtenerMenu = c.obtener
            i.programar = { :action => method(:eventoProgramarMenu) }
            i.obtener = { :action => method(:eventoObtenerMenu) } 
        end

        botones.build(:auto_create_container_gaps => false)
    end
    
    ### Función crearBotonesCategorias: Crea el JPanel donde se encuentran los botones 
    ### relativos a las categorias
    ########################################################################################
    def crearBotonesCategorias
        
        #se crea el panel con los botones y se asignan los listeners para los eventos de los botones
        botones = Swing::LEL.new(JPanel, "[programar|obtener]") do |c,i|
            c.programar = JButton.new "Programar"
            @botonProgramarCategorias = c.programar
            c.obtener = JButton.new "Obtener ahora"
            @botonObtenerCategorias = c.obtener
            i.programar = { :action => method(:eventoProgramarCategorias) }
            i.obtener = { :action => method(:eventoObtenerCategorias) } 
        end

        botones.build(:auto_create_container_gaps => false)
    end

    
    #############################################################
    ###### OTRAS FUNCIONES GUI
    #############################################################

    ### Función deshabilitarCheckBoxes: deshabilita los checkboxes de la gui para que no
    ### puedan ser modificados
    ########################################################################################
    def deshabilitarCheckBoxes
        
       @checkBoxes.each do |c|
           c.set_enabled false 
        end
    end
    
    ### Función habilitarCheckBoxes: habilita los checkboxes de la gui
    ########################################################################################
    def habilitarCheckBoxes
        
       @checkBoxes.each do |c|
           c.set_enabled true 
        end
    end
    
    ### Función agregarTextoConsola: muestra un texto en la consola de la gui
    ########################################################################################
    def agregarTextoConsola(texto)
        @textoConsola.append Time.new.ctime<<": "<<texto<<"\n"
    end
    
    ### Función textoEstadoCategorias: modifica el texto que se muestra en el estado para las categorias
    ########################################################################################
    def textoEstadoCategorias(texto)
        @estadoCategorias.set_text texto
    end
    
    ### Función textoEstadoMenu: modifica el texto que se muestra en el estado para el menu
    ########################################################################################
    def textoEstadoMenu(texto)
        @estadoMenu.set_text texto
    end

    ### Función iniciarObtenerMenu: modifica todos los elementos de la GUI para indicar que se 
    ### ha iniciado el scraping de menú e inicia la hebra de ejecución del scraping de menú
    ########################################################################################
    def iniciarObtenerMenu
        
        #se modifica la apariencia de los elementos de la GUI
        $scrapingIniciado = true
        @botonObtenerMenu.set_enabled false
        @botonProgramarMenu.set_enabled false
        @comboDiasMenu.set_enabled false
        @comboHoraMenu.set_enabled false
        @scrapingIniciadoMenu = true
        textoEstadoMenu("iniciado.")
        
        #se lanza la hebra de scraping de menú
        java.lang.Thread.new(ThreadMenu.new).start
  
    end
    
    ### Función finObtenerMenu: modifica todos los elementos de la GUI para indicar que se ha 
    ### finalizado el scraping de menú
    ########################################################################################
    def finObtenerMenu
        
        #la apariencia de los elementos de la GUI se devuelve al estado inicial 
        $scrapingIniciado = false
        @botonObtenerMenu.set_enabled true
        @botonProgramarMenu.set_enabled true
        @comboDiasMenu.set_enabled true
        @comboHoraMenu.set_enabled true
        @scrapingIniciadoMenu = false
        textoEstadoMenu("detenido.")
    end
    
    ### Función iniciarObtenerCategorias: modifica todos los elementos de la GUI para indicar 
    ### que se ha iniciado el scraping de categorías
    ########################################################################################
    def iniciarObtenerCategorias
        
        #se modifica la apariencia de los elementos de la GUI
        deshabilitarCheckBoxes
        @botonObtenerCategorias.set_text "Detener scraping"
        @botonProgramarCategorias.set_enabled false
        @comboDiasCategorias.set_enabled false
        @comboHoraCategorias.set_enabled false
        @scrapingIniciadoCategorias = true
        textoEstadoCategorias("iniciado.")

        # se lanza la hebra de ejecución del scraping de categorías
        java.lang.Thread.new(ThreadCategorias.new).start

    end

    ### Función finObtenerCategorias: modifica todos los elementos de la GUI para indicar 
    ### que se ha finalizado el scraping de categorías
    ########################################################################################
   def finObtenerCategorias

       #se modifica la variable que controla la ejecución del scraping de categorías
       $scrapingEnabled=false
       $gui.agregarTextoConsola("El scraping de categorías está siendo detenido.")
       
       #la apariencia de los elementos de la GUI se devuelve al estado inicial 
       habilitarCheckBoxes
       @botonObtenerCategorias.set_text "Obtener ahora"
       @botonProgramarCategorias.set_enabled true
       @comboDiasCategorias.set_enabled true
       @comboHoraCategorias.set_enabled true
       @scrapingIniciadoCategorias = false
       textoEstadoCategorias("detenido.")        
   end
   
    #############################################################
    ###### MANEJO DE EVENTOS DE BOTONES
    #############################################################

    ### Función eventoProgramarMenu: maneja el evento del botón "programar" de la sección menú
    ########################################################################################
    def eventoProgramarMenu(type, event)
        ## El Scraping del Menú está detenido -> PROGRAMAR SCRAPING MENÚ
        if(@scrapingIniciadoMenu == false)
            
            iniciarProgramacionMenu
        else
        ## El Scraping del Menú está iniciado -> DETENER PROGRAMACIÓN SCRAPING MENÚ  
            
            finProgramacionMenu
        end
  
            
    end
    
    ### Función eventoObtenerMenu: maneja el evento del botón "obtener ahora" de la sección menú
    ########################################################################################
    def eventoObtenerMenu(type, event)
        ## El Scraping del Menú está detenido -> INICIAR SCRAPING MENÚ
        if(@scrapingIniciadoMenu == false)
            iniciarObtenerMenu
        end
    end
    
    ### Función eventoProgramarCategorias: maneja el evento del botón "programar" de la 
    ### sección categorias
    ########################################################################################
    def eventoProgramarCategorias(type, event)
        
        ## El Scraping de Categorias está detenido -> PROGRAMAR SCRAPING CATEGORIAS
        if(@scrapingIniciadoCategorias == false)

            iniciarProgramacionCategorias
            
        else
        ## El Scraping de Categorias está iniciado -> DETENER PROGRAMACIÓN SCRAPING CATEGORIAS  
            
            finProgramacionCategorias
            
        end

    end
    
    ### Función eventoObtenerCategorias: maneja el evento del botón "obtener ahora" de la 
    ### sección categorias
    ########################################################################################
    def eventoObtenerCategorias(type, event)
        
        ## El Scraping de Categorias está detenido -> INICIAR SCRAPING CATEGORIAS
        if(@scrapingIniciadoCategorias == false)

            iniciarObtenerCategorias            
                    
        else
        ## El Scraping de Categorias está iniciado -> DETENER SCRAPING CATEGORIAS  
            
            finObtenerCategorias        
        end
    end
    
    
    #############################################################
    ###### FUNCIONES SCHEDULER
    #############################################################
    
    ### Función calcularFecha: calcula el día de siguiente ejecución del scheduler
    ########################################################################################
    def calcularFecha(tiempoInicio,dias)
         fecha = tiempoInicio + (dias * 60 * 60 * 24)
         return fecha
    end
    
    ### Función iniciarProgramacionMenu: realiza las operaciones necesarias para programar 
    ### el scraping del menú
    ########################################################################################
    def iniciarProgramacionMenu
        
        ## Se modifican los elementos de la GUI
        @botonProgramarMenu.set_text "Detener programación"
        @botonObtenerMenu.set_enabled false
        @comboDiasMenu.set_enabled false
        @comboHoraMenu.set_enabled false
        @scrapingIniciadoMenu = true
        textoEstadoMenu("programado.")
        
        ## se configura el scheduler para que se ejecute a la hora mencionada todos los días (sólo la primera vez)
        patronMenu = "0 #{$combosProgramacion['horaMenu']} * * *"
        $trabajoMenu = $scheduler.cron patronMenu do

            $tiempoInicioScrapingMenu = Time.now

            # Se configura el scheduler para la siguiente ejecución
            configurarSchedulerMenu($tiempoInicioScrapingMenu)           

            #se lanza la hebra de scraping del menu
            java.lang.Thread.new(ThreadMenu.new).start
            

        end
        
    end
    
    ### Función configurarSchedulerMenu: realizar la configuración para la siguiente ejecución
    ### del scheduler. Se ejecuta todas las veces menos la primera.
    ########################################################################################
    def configurarSchedulerMenu(tiempoInicio)    
        
        # se calcula la fecha de la siguiente ejecución
        fechaMenu = calcularFecha(tiempoInicio,$combosProgramacion["diasMenu"])
        #se confecciona el nuevo patrón cron
        patronMenu = "0 #{$combosProgramacion["diasMenu"]} #{fechaMenu.day} #{fechaMenu.month} *"
        
        $trabajoMenu = $scheduler.cron patronMenu do

            # Se configura el scheduler (recursivamente) para la siguiente ejecución
            configurarSchedulerMenu($tiempoInicioScrapingMenu)

            #se lanza la hebra de scraping del menu
            java.lang.Thread.new(ThreadMenu.new).start
            
        end
    end

    ### Función finProgramacionMenu: realiza las operaciones necesarias para desprogramar 
    ### el scraping del menú
    ########################################################################################
    def finProgramacionMenu
        
        # los elementos de la gui vuelven al estado inicial
        @botonProgramarMenu.set_text "Programar"
        @botonObtenerMenu.set_enabled true
        @comboDiasMenu.set_enabled true
        @comboHoraMenu.set_enabled true
        @scrapingIniciadoMenu = false
        textoEstadoMenu("detenido.")
        
        # se elimina el trabajo del scheduler
        $trabajoMenu.unschedule

    end
    
    ### Función finProgramacionCategorías: realiza las operaciones necesarias para desprogramar 
    ### el scraping de las categorías
    ########################################################################################
    def finProgramacionCategorias
        
        # los elementos de la gui vuelven al estado inicial
        habilitarCheckBoxes
        @botonProgramarCategorias.set_text "Programar"
        @botonObtenerCategorias.set_enabled true
        @comboDiasCategorias.set_enabled true
        @comboHoraCategorias.set_enabled true
        @scrapingIniciadoCategorias = false
        textoEstadoCategorias("detenido.")
        
        #se elimina el trabajo del scheduler
        $trabajoCategorias.unschedule
        
    end
    
    ### Función iniciarProgramacionCategorias: realiza las operaciones necesarias para programar 
    ### el scraping de las categorías
    ########################################################################################
    def iniciarProgramacionCategorias
        
        ## Se modifican los elementos de la GUI
        deshabilitarCheckBoxes
        @botonProgramarCategorias.set_text "Detener programación"
        @botonObtenerCategorias.set_enabled false
        @comboDiasCategorias.set_enabled false
        @comboHoraCategorias.set_enabled false
        @scrapingIniciadoCategorias = true
        textoEstadoCategorias("programado.")
        
        ## se configura el scheduler para que se ejecute a la hora mencionada todos los días (sólo la primera vez)
       patronCategorias = "0 #{$combosProgramacion['horaCategorias']} * * *"
       puts patronCategorias
       $trabajoCategorias = $scheduler.cron patronCategorias do

           $tiempoInicioScrapingCategorias = Time.now

           # Se configura el scheduler para la siguiente ejecución
           configurarSchedulerCategorias($tiempoInicioScrapingCategorias)

           # se lanza la hebra de ejecución del scraping de categorías
           java.lang.Thread.new(ThreadCategorias.new).start

       end
    
    end
    
    ### Función configurarSchedulerCategorias: realizar la configuración para la siguiente 
    ### ejecución del scheduler. Se ejecuta todas las veces menos la primera.
    ########################################################################################    
    def configurarSchedulerCategorias(tiempoInicio)  

        # se calcula la fecha de la siguiente ejecución
        fechaCategorias = calcularFecha(tiempoInicio,$combosProgramacion["diasCategorias"])
        
        # se crea el nuevo patrón
        patronCategorias = "0 #{$combosProgramacion["horaCategorias"]} #{fechaCategorias.day} #{fechaCategorias.month} *"

        # configuracion scheduler categorias    
        $trabajoCategorias = $scheduler.cron patronCategorias do

            # Se configura el scheduler para la siguiente ejecución
            configurarSchedulerCategorias($tiempoInicioScrapingCategorias)

            # se lanza la hebra de ejecución del scraping de categorías
            java.lang.Thread.new(ThreadCategorias.new).start
        end  
    end
    

end
