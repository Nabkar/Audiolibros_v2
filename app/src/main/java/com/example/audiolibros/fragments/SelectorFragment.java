package com.example.audiolibros.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.audiolibros.AdaptadorLibros;
import com.example.audiolibros.AdaptadorLibrosFiltro;
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
	private AdaptadorLibrosFiltro adaptador;
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

		//Con esta instrucción activamos la acción de añadir menús desde el fragment
		//Hay que implementar los métodos de los menús
		setHasOptionsMenu(true);

		recyclerView = (RecyclerView) vista.findViewById(
				R.id.recycler_view);
		recyclerView.setLayoutManager(new GridLayoutManager(actividad,2));
		recyclerView.setAdapter(adaptador);
		adaptador.setOnItemClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*((MainActivity) actividad).mostrarDetalle(
						recyclerView.getChildAdapterPosition(v));*/
				((MainActivity) actividad).mostrarDetalle(
						(int) adaptador.getItemId(
								recyclerView.getChildAdapterPosition(v)));
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
								Snackbar.make(v,"¿Estás seguro?", Snackbar.LENGTH_LONG)
									.setAction("SI", new View.OnClickListener() {
										@Override
										public void onClick(View view) {
											//listaLibros.remove(id);
											adaptador.borrar(id);
											adaptador.notifyDataSetChanged();
										}
									})
									.show();
								break;
							case 2: //Insertar
								//listaLibros.add(listaLibros.get(id));
								int posicion = recyclerView.getChildLayoutPosition(v);
								adaptador.insertar((Libro) adaptador.getItem(posicion));
								adaptador.notifyDataSetChanged();
								Snackbar.make(v,"Libro insertado", Snackbar.LENGTH_INDEFINITE)
									.setAction("OK", new View.OnClickListener() {
										@Override public void onClick(View view) { }
									})
									.show();
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
	@Override public void onResume(){
		((MainActivity) getActivity()).mostrarElementos(true);
		super.onResume();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_selector, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_ultimo) {
			((MainActivity) actividad).irUltimoVisitado();
			return true;
		} else if (id == R.id.menu_buscar) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}