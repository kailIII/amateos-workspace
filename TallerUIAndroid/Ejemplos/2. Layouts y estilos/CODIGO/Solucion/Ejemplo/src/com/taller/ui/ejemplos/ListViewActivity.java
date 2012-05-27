package com.taller.ui.ejemplos;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewActivity extends ListActivity {
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.listview);
        
        EjemploArrayAdapter adapter = new EjemploArrayAdapter(this, generarEstudiantes());
        setListAdapter(adapter);
        
    }
    
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(getApplicationContext(), "Pulsada fila: "+position, Toast.LENGTH_SHORT).show();
	}



	public ArrayList<Estudiante> generarEstudiantes(){
    	
    	ArrayList<Estudiante> estudiantes = new ArrayList<Estudiante>();
    	
    	estudiantes.add(new Estudiante("José Martínez", Estudiante.INFORMATICA, 1));
    	estudiantes.add(new Estudiante("Antonio Jiménez", Estudiante.INFORMATICA, 4));
    	estudiantes.add(new Estudiante("María García", Estudiante.TELECO, 2));
    	estudiantes.add(new Estudiante("Matilde Hernández", Estudiante.INFORMATICA, 5));
    	estudiantes.add(new Estudiante("Miguel Martín", Estudiante.TELECO, 3));
    	estudiantes.add(new Estudiante("Ana Pozuelo", Estudiante.TELECO, 5));
    	estudiantes.add(new Estudiante("Manuel González", Estudiante.INFORMATICA, 1));

    	return estudiantes;
    }
}