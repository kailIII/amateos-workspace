package com.taller.ui.ejemplos;

public class Estudiante {
	
	public static final int TELECO = 0;
	public static final int INFORMATICA = 1;
	
	public String nombre;
	public int carrera;
	public int curso;
	
	public Estudiante (String nombre, int carrera, int curso){
		this.nombre = nombre;
		this.carrera = carrera;
		this.curso = curso;
	}

}
