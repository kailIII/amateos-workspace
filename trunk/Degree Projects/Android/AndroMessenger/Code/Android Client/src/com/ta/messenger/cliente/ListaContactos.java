package com.ta.messenger.cliente;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ListaContactos extends Activity{

	private String[] listaContactos;
	Button boton;
	ListView listaView;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);
        
        // Obtención de datos usuario desde activity de login
	    Bundle bundle = this.getIntent().getExtras();
	    String aux = bundle.getString("Contactos");
	    listaContactos = aux.split(";");
        
		listaView = (ListView)findViewById(R.id.view_listaContactos);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, listaContactos);
		listaView.setAdapter(adapter);
		registerForContextMenu(listaView);
		
    }
    
    private void actualizarLista(){
    	String [] nuevaLista = {"NuevoNombre1","NuevoNombre2","NuevoNombre3"};
    	listaView.setAdapter(new ArrayAdapter<String>(this, 
				R.layout.item, nuevaLista));
    }
    
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	if (v.getId()==R.id.view_listaContactos) {
    	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    		menu.setHeaderTitle(listaContactos[info.position]);
 			menu.add(Menu.NONE, 0, 0, "Opcion 1");
 			menu.add(Menu.NONE, 1, 0, "Opcion 2");

    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	    
	    int menuItemIndex = item.getItemId();
	    String contacto = listaContactos[info.position];
	    Log.d("CONTEXT","Seleccionado contacto: "+contacto);
	    Log.d("CONTEXT","Seleccionado menu numero: "+menuItemIndex);

    	return true;
    }
}