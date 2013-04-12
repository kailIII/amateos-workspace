package com.ta.messenger.cliente;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class IntroducirDatos extends Activity{
	Button boton;
	EditText campoTexto;
	TextView texto;
	String contactoAgregar;
	
	int operacion;
	
	@Override	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inicialización elementos interfaz usuario
        setContentView(R.layout.introducir_datos);
        
        Bundle bundle = this.getIntent().getExtras();
        operacion = bundle.getInt("Operacion");
        
        boton = (Button) findViewById(R.id.boton_agregar_contacto);
        campoTexto = (EditText) findViewById(R.id.campo_contacto);
        texto = (TextView) findViewById(R.id.text_info_agregar_contacto);
        
        switch (operacion){
        case 0: 
        	boton.setText("Agregar Contacto");
        	texto.setText("Introduce el nombre de usuario del contacto:");
        	break;
        case 1:
        	boton.setText("Cambiar Apodo");
        	texto.setText("Introduce el nuevo apodo:");
        }
        
        
        //Listener Botón
        boton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				
				if(operacion == 0){ // Agregar contacto
					bundle.putString("Respuesta","Agregar contacto");
					bundle.putString("Contacto", campoTexto.getText().toString());
				}
				
				if(operacion == 1){ // Cambiar apodo
					bundle.putString("Respuesta","Cambiar apodo");
					bundle.putString("Apodo", campoTexto.getText().toString());
				}
				Intent i = new Intent();
	            i.putExtras(bundle);
	            setResult(RESULT_OK, i);
	            finish();			
	            }
        	
        });	
    }
}
