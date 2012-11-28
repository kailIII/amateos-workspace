/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

   $(document).ready(function(){
                $("#enviar").click(function(){
                    
                    enviarDatos()
                    
                });
                 
                $("#tipouser").change(function(){
                    var selector = $("#tipouser").val();
                    if(selector==1){
                        $("#protegida").show();
                        $("#agresor").hide();
                    }else{
                        $("#protegida").hide();
                        $("#agresor").show();
                        
                    }
                });
            });
            
            
    

 function enviarDatos(){
     
     var opcion = $('#tipouser').val();
    
     if(opcion == 1){
     
     tipo = 1;
     
     nombre = $("#nombre").val();
     apellidos = $("#apellidos").val();
     fechanac = $("#fechanac").val();
     telefono = $("#telefono").val();
     nombrec = $("#nombrec").val();
     movil = $("#movil").val();
     email = $("#email").val();
     nombrecs = $("#nombrecs").val();
     movils = $("#movils").val();
     emails = $("#emails").val();
     
      $.ajax({
                    
                    type: 'POST',
                    url: "http://localhost:8080/PersistenciaPasos/Persistencia",
                    data:"tipo="+tipo+"&nombre="+nombre+"&apellidos="+apellidos+"&fechanac="+fechanac+"&telefono="+telefono+"&nombrec="+nombrec+"&movil="+movil+"&email="+email+"&nombrecs="+nombrecs+"&movils="+movils+"&emails="+emails,
                    success: function(respuesta) {
                        $("#content").empty().append("<p>" +respuesta.texto+ "</p>");
                        alert("Enviado"+respuesta);
                    },
                    error: function() {
                        alert("error");
                        
                    }
                    });
     
     }else{
        tipo = 2;
        nombrea = $("#nombrea").val();
        apellidosa = $("#apellidosa").val();
        dispositivo = $("#dispositivo").val();
        distancia = $("#distancia").val();
        
         $.ajax({
                    
                    type: 'POST',
                    url: "http://localhost:8080/PersistenciaPasos/Persistencia",
                    data:"tipo="+tipo+"&nombrea="+nombrea+"&apellidosa="+apellidosa+"&dispositivo="+dispositivo+"&distancia="+distancia,
                    success: function(respuesta) {
                        $("#content").empty().append("<p>" +respuesta.texto+ "</p>");
                        alert("Enviado"+respuesta);
                    },
                    error: function() {
                        alert("error");
                        
                    }
                    });
     
     }
                
               
                    
              }
              
            


