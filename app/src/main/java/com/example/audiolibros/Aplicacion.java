package com.example.audiolibros;

import android.app.Application;

import java.util.List;

/**
 * Created by JulioM on 12/01/2018.
 */

public class Aplicacion extends Application{
	private List<Libro> listaLibros;
	private AdaptadorLibros adaptador;

	@Override
	public void onCreate() {
		super.onCreate();
		listaLibros = Libro.ejemploLibros();
		adaptador = new AdaptadorLibros (this, listaLibros);
	}

	public AdaptadorLibros getAdaptador(){
		return adaptador;
	}

	public List<Libro> getListaLibros(){
		return  listaLibros;
	}
}
