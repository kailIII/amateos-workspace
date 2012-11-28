
package org.inftel.scrum.activities;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.lists.TareasAdapter;
import org.inftel.scrum.modelXML.Proyecto;
import org.inftel.scrum.modelXML.Sprint;
import org.inftel.scrum.modelXML.Tarea;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.utils.Constantes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TareasActivity extends SherlockListActivity {

    private TareasAdapter adapter;
    private ArrayList<Tarea> tareas;
    private Sprint sprint;
    private Proyecto proyecto;
    private String jSessionId;
    ProgressDialog d;

    private static final String TAG = TareasActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tareas);

        getSupportActionBar().setTitle(R.string.tareas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Se obtiene el jSessionId
        this.jSessionId = obtenerSessionId();

        // Se obtienen el sprint y el proyecto del fragment anterior

        Bundle b = getIntent().getExtras();
        sprint = (Sprint) b.getSerializable("sprint");
        proyecto = (Proyecto) b.getSerializable("proyecto");
        Log.d(TAG, "SPRINT OBTENIDO -> " + sprint);
        Log.d(TAG, "PROYECTO OBTENIDO -> " + proyecto);

    }

    @Override
    protected void onStart() {
        // Se obtienen las tareas del servidor
        tareas = obtenerTareas(sprint);
        Log.d(TAG, "TAREAS OBTENIDAS -> " + tareas);

        if (tareas == null) {
            tareas = new ArrayList<Tarea>();
        }

        // Se pinta la lista
        adapter = new TareasAdapter(this, tareas);
        setListAdapter(adapter);
        super.onStart();
    }

    /**
     * Muestra un dialog para crear una nueva tarea
     */
    public void mostrarDialogNuevaTarea() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_tarea_nueva);

        dialog.setTitle(this.getString(R.string.dialog_tarea_nueva_titulo));

        Button boton = (Button) dialog
                .findViewById(R.id.dialog_tarea_nueva_aceptar);
        final EditText descripcion = (EditText) dialog
                .findViewById(R.id.dialog_tarea_nueva_descripcion_texto);
        final RadioGroup prioridad = (RadioGroup) dialog
                .findViewById(R.id.dialog_tarea_nueva_prioridad_radioGroup);
        boton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                Tarea t = new Tarea();
                t.setSprintId(sprint);
                t.setProyectoId(proyecto);

                // Descripción
                t.setDescripcion(descripcion.getText().toString());

                // Prioridad
                switch (prioridad.getCheckedRadioButtonId()) {
                    case R.id.dialog_tarea_nueva_prioridad_alta:
                        t.setPrioridad(BigInteger.ZERO);
                        break;
                    case R.id.dialog_tarea_nueva_prioridad_baja:
                        t.setPrioridad(BigInteger.ONE);
                        break;
                    default:
                        t.setPrioridad(BigInteger.ZERO);
                        break;
                }

                // Estado -> por defecto TO DO
                t.setDone('0');

                BigDecimal id = enviarTarea(t);
                if (id != null) {
                    t.setId(id);
                    tareas.add(t);
                    adapter.updateList(tareas);
                    Toast.makeText(getBaseContext(),
                            getString(R.string.dialog_tarea_nueva_exito),
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getBaseContext(),
                            getString(R.string.dialog_tarea_nueva_error),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();
    }

    /**
     * Obtiene el SessionId del sharedpreferences
     * 
     * @return
     */
    private String obtenerSessionId() {
        SharedPreferences prefs = this.getSharedPreferences("loginPreferences",
                Context.MODE_PRIVATE);
        return prefs.getString("jSessionId", "default_value");
    }

    /**
     * Obtiene la lista de tareas de un sprint del servidor
     * 
     * @param sprint
     * @return
     */
    private ArrayList<Tarea> obtenerTareas(Sprint sprint) {
        ArrayList<Tarea> lista = new ArrayList<Tarea>();
        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("accion",
                Constantes.BUSCAR_TAREAS_SPRINTS));
        parameters.add(new BasicNameValuePair("Sprint", sprint.getId()
                .toString()));
        String datos = "";
        try {
            d = ProgressDialog.show(this.getApplicationContext(),
                    getResources().getString(R.string.espere), "", true, true);
            datos = ServerRequest.send(jSessionId, parameters,
                    "BUSCAR_TAREAS_SPRINTS");
            d.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Type listOfTestObject = new TypeToken<List<Tarea>>() {
        }.getType();
        lista = new Gson().fromJson(datos, listOfTestObject);
        return lista;
    }

    /**
     * Almacena una tarea en el servidor
     * 
     * @param t
     * @return ID de la tarea
     */
    private BigDecimal enviarTarea(Tarea t) {

        BigDecimal id = null;

        String jsonTarea = new Gson().toJson(t);
        String jsonProyecto = new Gson().toJson(proyecto);
        String jsonSprint = new Gson().toJson(sprint);

        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters
                .add(new BasicNameValuePair("accion", Constantes.CREAR_TAREA));
        parameters.add(new BasicNameValuePair("Tarea", jsonTarea));
        parameters.add(new BasicNameValuePair("Proyecto", jsonProyecto));
        parameters.add(new BasicNameValuePair("Sprint", jsonSprint));

        String idTarea = "";
        try {
            d = ProgressDialog.show(this.getApplicationContext(),
                    getResources().getString(R.string.espere), "", true, true);
            idTarea = ServerRequest.send(jSessionId, parameters, "CREAR_TAREA");
            d.dismiss();
            id = new BigDecimal(idTarea);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /*
     * public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
     * menu.add(this.getString(R.string.nuevo)).setIcon(R.drawable.ic_mas)
     * .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
     * menu.add(this.getString(R.string.Tablon)).setIcon(R.drawable.ic_mas)
     * .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS); }
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(this.getString(R.string.nuevo)).setIcon(R.drawable.ic_mas)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        Bundle b = new Bundle();
        b.putSerializable("tarea", tareas.get(position));
        b.putSerializable("sprint", sprint);
        b.putSerializable("proyecto", proyecto);

        Intent i = new Intent(this, TareaActivity.class);
        i.putExtras(b);
        startActivity(i);

        super.onListItemClick(l, v, position, id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d(TAG, "Pulsado item -> " + item.toString());

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getTitle().equals(this.getString(R.string.nuevo))) {
            // PULSADO NUEVO
            mostrarDialogNuevaTarea();
        }
        return super.onOptionsItemSelected(item);
    }

}
