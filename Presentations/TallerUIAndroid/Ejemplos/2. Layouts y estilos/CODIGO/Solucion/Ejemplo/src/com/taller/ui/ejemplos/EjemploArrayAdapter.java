package com.taller.ui.ejemplos;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EjemploArrayAdapter extends ArrayAdapter<Estudiante> {

	private final Context context;
    private final ArrayList<Estudiante> estudiantes;

    /**
     * Constructor
     * 
     * @param context
     * @param hotspots
     */
    public EjemploArrayAdapter(Context context, ArrayList<Estudiante> estudiantes) {
        super(context, R.layout.row_ejemplo, estudiantes);
        this.context = context;
        this.estudiantes = estudiantes;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_ejemplo, parent, false);

        
        ImageView imagen = (ImageView) rowView.findViewById(R.id.imagen);
        if(estudiantes.get(position).carrera == Estudiante.TELECO){
        	imagen.setImageResource(R.drawable.telefono);
        }else{
        	imagen.setImageResource(R.drawable.portatil);
        }
        
        TextView nombre = (TextView) rowView.findViewById(R.id.nombre);
        nombre.setText(estudiantes.get(position).nombre);

        TextView curso = (TextView) rowView.findViewById(R.id.curso);

        int cursoEstudiante = estudiantes.get(position).curso;
        curso.setText(String.valueOf(cursoEstudiante));
        
        if(cursoEstudiante==5){
        	curso.setTextColor(context.getResources().getColor(R.color.verde));
        }
        
        return rowView;
    }
	
}
