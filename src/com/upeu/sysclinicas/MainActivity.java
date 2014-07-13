package com.upeu.sysclinicas;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.upeu.sysclinicas.models.Clinicas;
import com.upeu.sysclinicas.utils.CargarDatosMaps;
import com.upeu.sysclinicas.utils.ConectionUtils;

public class MainActivity extends FragmentActivity implements
		SearchView.OnQueryTextListener {

	private static final int RESULT_SETTING = 1;
	private SearchView mSearchView;
	private ArrayList<Clinicas> listClinicas;
	// private static final String ASSETS = "/assets/img/";
	private final LatLng UPV = new LatLng(-6.4824784, -76.3726891);
	private String medodo = "android/listclinicas";
	private String medodoSearch = "android/searchClinicas";
	public int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			getClinicas(getUrl() + medodo, "0");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onDestroy() {

	    FragmentManager fm = getSupportFragmentManager();
	    Fragment fragment = (fm.findFragmentById(R.id.map));

	    if (fragment.isResumed()) {
	        FragmentTransaction ft = fm.beginTransaction();
	        ft.remove(fragment);
	        ft.commit();
	    }
	    super.onDestroy();
	}
	
	public void getClinicas(String url, String arg0) throws JSONException {
		
		final GoogleMap mapa;
		mapa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		
		if (mapa != null) {

		}
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(UPV, 13));
		mapa.setMyLocationEnabled(true);
		mapa.getUiSettings().setZoomControlsEnabled(false);
		mapa.getUiSettings().setCompassEnabled(true);

		RequestParams params = new RequestParams();
		params.put("cadena", arg0);

		ConectionUtils.get(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Toast.makeText(getApplicationContext(),
						"hubo un error al descargar los datos, renicie la app",
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				CargarDatosMaps cargar = new CargarDatosMaps(MainActivity.this,
						mapa, arg2);
				listClinicas = cargar.setDatos();
				mapa.setOnMarkerClickListener(new CustomOnMarkerClickListener());
			}
		});
	}

	@Override
	// metodo para recuperar la url de preferences
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RESULT_SETTING:
			showSettings();
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showSettings() {
		SharedPreferences mSharePreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String url = mSharePreferences.getString("prefUrl", "NULL");

		mSharePreferences = getSharedPreferences("UpeuPreferences",
				MODE_PRIVATE);

		// guardamos las preferencias en un xml
		SharedPreferences.Editor editor = mSharePreferences.edit();
		editor.putString("url", url);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) searchItem.getActionView();
		mSearchView.setQueryHint("Buscar negocio...");
		mSearchView.setOnQueryTextListener(this);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
			startActivityForResult(intent, RESULT_SETTING);
			break;

		case R.id.refresh:
			try {
				count = 1;
				getClinicas(getUrl() + medodo, "0");
				Toast.makeText(getApplicationContext(),
						"los datos fueron recargados exitosamente",
						Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	// optenemos la url de sharePrefrences
	private String getUrl() {
		SharedPreferences pref = getSharedPreferences("UpeuPreferences",
				MODE_PRIVATE);
		return pref.getString("url", "nada");
	}

	public class CustomOnMarkerClickListener implements OnMarkerClickListener {

		@Override
		public boolean onMarkerClick(Marker marker) {
			Clinicas clinica = listClinicas.get(Integer.parseInt(marker.getSnippet()));

			ConfigDialogFragment newFragment = ConfigDialogFragment
					.newInstance(clinica);
			newFragment.show(getSupportFragmentManager(), "dialog");
			return true;
		}

	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		try {
			getClinicas(getUrl() + medodoSearch, arg0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
