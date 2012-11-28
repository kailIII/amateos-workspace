package org.inftel.pasos.list;

import java.util.ArrayList;

import org.inftel.pasos.R;
import org.inftel.pasos.activities.ContactosActivity;
import org.inftel.pasos.vos.ContactoEnvio;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactosAdapter extends ArrayAdapter<ContactoEnvio> {
	
	private static final String TAG = ContactosActivity.class.getSimpleName();
	
    private Context context;
    private ArrayList<ContactoEnvio> contactos;
    private float tamTexto;

    public ContactosAdapter(Context context, ArrayList<ContactoEnvio> contactos) {
        super(context, R.layout.contactos, contactos);
        this.context = context;
        this.contactos = contactos;
		SharedPreferences prefs = context.getSharedPreferences("prefs", Activity.MODE_PRIVATE);
		this.tamTexto = prefs.getFloat("tam", 20);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.contactos_fila, parent, false);

        TextView nombre = (TextView) rowView.findViewById(R.id.nombre);
        nombre.setText(contactos.get(position).getNombre());
        nombre.setTextSize(TypedValue.COMPLEX_UNIT_DIP, tamTexto);

        TextView telefono = (TextView) rowView.findViewById(R.id.telefono);
        telefono.setText(contactos.get(position).getTelefonoContacto().toString());
        telefono.setTextSize(TypedValue.COMPLEX_UNIT_DIP, tamTexto-5);

        TextView email = (TextView) rowView.findViewById(R.id.email);
        email.setText(contactos.get(position).getEmail());
        email.setTextSize(TypedValue.COMPLEX_UNIT_DIP, tamTexto-5);

        return rowView;
    }
    
    public void setTamTexto(float tamTexto) {
		this.tamTexto = tamTexto;
	}

	public void updateList(ArrayList<ContactoEnvio> contactos) {

        this.contactos = contactos;
        Log.d(TAG,"ADAPTER  UPDATE");
    }
}
