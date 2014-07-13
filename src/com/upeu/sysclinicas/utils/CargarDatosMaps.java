package com.upeu.sysclinicas.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.upeu.sysclinicas.R;
import com.upeu.sysclinicas.models.Clinicas;

public class CargarDatosMaps {
	private ArrayList<Clinicas> listClinicas;
	private Clinicas clinicas;
	private Activity contexto;
	private GoogleMap mapa;
	private byte[] content;
	private int count = 0;
	public CargarDatosMaps(Activity context, GoogleMap map, byte[] arg2) {
		this.contexto = context;
		this.mapa = map;
		this.content = arg2;
	}

	public ArrayList<Clinicas> setDatos() {
		mapa.clear();
		String response = new String(this.content);
		// Log.v("content", response);
		JSONObject jsonResponse;
		listClinicas = new ArrayList<Clinicas>();
		try {
			jsonResponse = new JSONObject(response);
			//mapa.clear();
			// recuperamos el array
			JSONArray jsonMainNode = jsonResponse.optJSONArray("android");
			int lengthJsonArr = jsonMainNode.length();
			double latitud;
			double longitud;
			if (lengthJsonArr > 0) {
				for (int i = 0; i < lengthJsonArr; i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					latitud = Double.parseDouble(jsonChildNode.optString(
							"latitud").toString());
					longitud = Double.parseDouble(jsonChildNode.optString(
							"longitud").toString());

					LatLng ubicacion = new LatLng(latitud, longitud);

					int icono = 0;

					switch (Integer.parseInt(jsonChildNode.optString("idtipo")
							.toString())) {
					case 1:
						icono = R.drawable.clinica;
						break;
					case 2:
						icono = R.drawable.restaurant;
						break;
					case 3:
						icono = R.drawable.bus;
						break;
					default:
						icono = R.drawable.home;
					}

					// cargamos los datos
					clinicas = new Clinicas();
					clinicas.setApellidoEncargado(jsonChildNode.optString(
							"apellidos").toString());
					clinicas.setCiudad(jsonChildNode.optString("ciudad")
							.toString());
					clinicas.setDireccion(jsonChildNode.optString("direccion")
							.toString());
					clinicas.setDistrito(jsonChildNode.optString("distrito")
							.toString());
					clinicas.setFacebook(jsonChildNode.optString("facebook")
							.toString());
					clinicas.setIcono(jsonChildNode.optString("icono")
							.toString());
					clinicas.setIdclinica(jsonChildNode.optInt("id"));
					clinicas.setIdtipo(jsonChildNode.optInt("idtipo"));
					clinicas.setLatitud(jsonChildNode.optDouble("latitud"));
					clinicas.setLatitud(jsonChildNode.optDouble("longitud"));
					clinicas.setNombreClinica(jsonChildNode.optString(
							"descripcion").toString());
					clinicas.setNombreEncargado(jsonChildNode.optString(
							"nombres").toString());
					clinicas.setRazonSocial(jsonChildNode.optString(
							"razon_social").toString());
					clinicas.setResumen(jsonChildNode.optString("resumen")
							.toString());
					clinicas.setTelefono(jsonChildNode.optString("telefono")
							.toString());
					clinicas.setTipo(jsonChildNode.optString("tipo").toString());
					clinicas.setTwitter(jsonChildNode.optString("twitter")
							.toString());
					clinicas.setWeb(jsonChildNode.optString("web").toString());

					listClinicas.add(clinicas);

					mapa.addMarker(new MarkerOptions()
							.position(ubicacion)
							.title(jsonChildNode.optString("descripcion")
									.toString()).snippet(i+"")
							.icon(BitmapDescriptorFactory.fromResource(icono))
							.anchor(0.5f, 0.5f));
				}
				if (getPrimer() != 0) {
					setControl(lengthJsonArr,1);
				} else {
					setControl(0,1);
				}

			} else {
				Toast.makeText(this.contexto, "No se encontro información",
						Toast.LENGTH_SHORT).show();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listClinicas;
	}

	public void setControl(int control, int primer) {

		SharedPreferences pref = contexto.getSharedPreferences(
				"ControlPreferences", contexto.MODE_PRIVATE);

		int controla = pref.getInt("control", 0);

		SharedPreferences mSharePreferences = PreferenceManager
				.getDefaultSharedPreferences(contexto);
		mSharePreferences = contexto.getSharedPreferences("ControlPreferences",
				contexto.MODE_PRIVATE);
		
		int busqueda = control+ controla;
		// guardamos las preferencias en un xml
		SharedPreferences.Editor editor = mSharePreferences.edit();

		editor.putInt("control", busqueda);
		editor.putInt("primer", primer);
		editor.commit();
	}

	private int getControl() {
		SharedPreferences pref = contexto.getSharedPreferences(
				"ControlPreferences", contexto.MODE_PRIVATE);
		return pref.getInt("control", 0);
	}
	
	private int getPrimer() {
		SharedPreferences pref = contexto.getSharedPreferences(
				"ControlPreferences", contexto.MODE_PRIVATE);
		return pref.getInt("primer", 0);
	}
}
