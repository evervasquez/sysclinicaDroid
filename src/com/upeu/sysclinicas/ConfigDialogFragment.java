package com.upeu.sysclinicas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.upeu.sysclinicas.models.Clinicas;

public class ConfigDialogFragment extends DialogFragment implements
		OnItemSelectedListener {
	private static Clinicas clinica;
	private TextView nombreClinica,razonSocial,direccion,telefono;
	private TextView detalle,facebook,twitter;
	public static ConfigDialogFragment newInstance(Clinicas clinic) {
		ConfigDialogFragment frag = new ConfigDialogFragment();
		ConfigDialogFragment.clinica = clinic;
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		View view = getActivity().getLayoutInflater().inflate(
				R.layout.fragment_clinica, null);
		
		nombreClinica = (TextView) view.findViewById(R.id.txttitulo);
		detalle = (TextView) view.findViewById(R.id.txtdetalle);
		direccion = (TextView) view.findViewById(R.id.txtdireccion);
		razonSocial = (TextView) view.findViewById(R.id.txtrazonsocial);
		telefono = (TextView) view.findViewById(R.id.txttelefono);
		facebook = (TextView) view.findViewById(R.id.txtfacebook);	
		twitter = (TextView) view.findViewById(R.id.txttwitter);	
		
		
		nombreClinica.setText(ConfigDialogFragment.clinica.getNombreClinica());
		detalle.setText(ConfigDialogFragment.clinica.getResumen());
		razonSocial.setText(ConfigDialogFragment.clinica.getRazonSocial());
		direccion.setText(ConfigDialogFragment.clinica.getDireccion());
		telefono.setText(ConfigDialogFragment.clinica.getTelefono());
		facebook.setText(ConfigDialogFragment.clinica.getFacebook());
		twitter.setText(ConfigDialogFragment.clinica.getTwitter());
		
		
		alertDialogBuilder.setView(view);
		alertDialogBuilder
            .setNegativeButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    
                }
            });

		return alertDialogBuilder.create();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// outState.putFloat(KEY_SAVE_RATING_BAR_VALUE, mRatingBar.getRating());
		super.onSaveInstanceState(outState);
	}
	
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
