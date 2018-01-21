package com.example.audiolibros.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.audiolibros.AdaptadorLibros;
import com.example.audiolibros.Aplicacion;
import com.example.audiolibros.Libro;
import com.example.audiolibros.MainActivity;
import com.example.audiolibros.R;

import java.util.List;

/**
 * Created by JulioM on 17/01/2018.
 */

public class SelectorFragment extends Fragment {
	private Activity actividad;
	private RecyclerView recyclerView;
	private AdaptadorLibros adaptador;
	private List<Libro> listaLibros;

	@Override public void onAttach(Activity actividad) {
		super.onAttach(actividad);
		this.actividad = actividad;
		Aplicacion app = (Aplicacion) actividad.getApplication();
		adaptador = app.getAdaptador();
		listaLibros = app.getListaLibros();
	}
	@Override public View onCreateView(LayoutInflater inflador, ViewGroup
			contenedor, Bundle savedInstanceState) {
		View vista = inflador.inflate(R.layout.fragment_selector,
				contenedor, false);
		recyclerView = (RecyclerView) vista.findViewById(
				R.id.recycler_view);
		recyclerView.setLayoutManager(new GridLayoutManager(actividad,2));
		recyclerView.setAdapter(adaptador);
		adaptador.setOnItemClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) actividad).mostrarDetalle(
						recyclerView.getChildAdapterPosition(v));
			}
		});

		adaptador.setOnItemLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(final View v) {
				final int id = recyclerView.getChildAdapterPosition(v);
				AlertDialog.Builder menu = new AlertDialog.Builder(actividad);
				CharSequence[] opciones = { "Compartir", "Borrar ", "Insertar" };
				menu.setItems(opciones, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int opcion) {
						switch (opcion) {
							case 0: //Compartir
								Libro libro = listaLibros.get(id);
								Intent i = new Intent(Intent.ACTION_SEND);
								i.setType("text/plain");
								i.putExtra(Intent.EXTRA_SUBJECT, libro.titulo);
								i.putExtra(Intent.EXTRA_TEXT, libro.urlAudio);
								startActivity(Intent.createChooser(i, "Compartir"));
								break;
							case 1: //Borrar
								listaLibros.remove(id);
								adaptador.notifyDataSetChanged();
								break;
							case 2: //Insertar
								listaLibros.add(listaLibros.get(id));
								adaptador.notifyDataSetChanged();
								break;
						}
					}
				});
				menu.create().show();
				return true;
			}
		});
		return vista;
	}
}