package org.inftel.scrum.activities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.modelXML.Usuario;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.utils.Constantes;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GruposFragment extends SherlockFragment {
	private String jSessionId;
	private ListView usuarios;
	ProgressDialog d;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.grupos, container, false);
		usuarios = (ListView) view.findViewById(R.id.listGrupos);
		
//		d=ServerRequest.compruebaConexionServer(this.getActivity());
		buscarGrupo();

		return view;
	}

	private void buscarGrupo() {
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters.add(new BasicNameValuePair("accion", Constantes.BUSCAR_USUARIO_GRUPO));
		String datos = null;
		try {
			
			Log.d("Grupos","mostrando progressDailog...");
			d=ProgressDialog.show(this.getSherlockActivity(), getResources().getString(R.string.espere), "", true, true);
			datos = ServerRequest.send(jSessionId, parameters, "BUSCAR_USUARIO_GRUPO");
			
			if(datos!=null){
				Log.d("Grupos","ocultando progressDailog...");
				d.dismiss();
			}
			
		} catch (Exception e) {
			ProgressDialog.show(this.getActivity().getApplicationContext(), getResources().getString(R.string.Error), getResources().getString(R.string.Error_grupos), true, true);
			e.printStackTrace();
		}
		Type listOfTestObject = new TypeToken<List<Usuario>>() {
		}.getType();
		ArrayList<Usuario> list = new Gson().fromJson(datos, listOfTestObject);
		String[] nombres = getNombres(list);
		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nombres);
		usuarios.setAdapter(adaptador);
	}

	private String[] getNombres(ArrayList<Usuario> list) {
		String[] usuarios = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			usuarios[i] = list.get(i).getNombre() + " - " + list.get(i).getEmail();
		}
		return usuarios;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		menu.add("actualizar").setIcon(R.drawable.ic_actualizar).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getTitle().toString().compareTo("actualizar")==0)
			buscarGrupo();
		Log.d("Scroid", "Seleccionado menu: " + item);
		return super.onOptionsItemSelected(item);
	}

}
