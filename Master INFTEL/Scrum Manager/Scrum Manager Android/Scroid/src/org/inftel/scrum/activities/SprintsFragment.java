
package org.inftel.scrum.activities;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.lists.SprintsAdapter;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SprintsFragment extends SherlockListFragment {

    private SprintsAdapter adapter;
    private ArrayList<Sprint> sprints;
    private final static String TAG = SprintsFragment.class.getSimpleName();
    private String jSessionId;
    private Proyecto proyecto;
    ProgressDialog d;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        /*
         * TareasFragment f = new TareasFragment(); // Se pasa al Fragment el
         * sprint seleccionado Bundle b = new Bundle();
         * b.putSerializable("sprint", sprints.get(position));
         * b.putSerializable("proyecto", proyecto); f.setArguments(b);
         * FragmentTransaction transaction = getFragmentManager()
         * .beginTransaction(); transaction.replace(android.R.id.content, f);
         * transaction.addToBackStack("tareasFragment"); transaction.commit();
         */

        Bundle b = new Bundle();

        b.putSerializable("sprint", sprints.get(position));
        b.putSerializable("proyecto", proyecto);

        Intent i = new Intent(this.getSherlockActivity(), TareasActivity.class);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sprints, container, false);

        // Se obtiene el jSessionId
        this.jSessionId = obtenerSessionId();

        // Se obtienen los datos del servidor
        sprints = obtenerSprints();
        Log.d(TAG, "SPRINTS OBTENIDOS -> " + sprints);

        proyecto = obtenerProyecto();
        Log.d(TAG, "PROYECTO OBTENIDO -> " + proyecto);

        // Se pinta la vista
        adapter = new SprintsAdapter(this.getSherlockActivity()
                .getBaseContext(), sprints);
        setListAdapter(adapter);
        return view;
    }

    /**
     * Obtiene el SessionId del sharedpreferences
     * 
     * @return
     */
    private String obtenerSessionId() {
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences(
                "loginPreferences", Context.MODE_PRIVATE);
        return prefs.getString("jSessionId", "default_value");
    }

    /**
     * Obtiene los sprints del servidor
     * 
     * @return
     */
    private ArrayList<Sprint> obtenerSprints() {
        ArrayList<Sprint> sprints = new ArrayList<Sprint>();
        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("accion",
                Constantes.BUSCAR_SPRINTS));
        String datos = "";
        try {
            d = ProgressDialog.show(this.getSherlockActivity(),
                    getResources().getString(R.string.espere), "", true, true);
            datos = ServerRequest
                    .send(jSessionId, parameters, "BUSCAR_SPRINTS");
            d.dismiss();
        } catch (Exception e) {
            ProgressDialog.show(this.getActivity().getApplicationContext(), getResources()
                    .getString(R.string.Error),
                    getResources().getString(R.string.Error_obtener_sprints), true, true);
            e.printStackTrace();
        }
        Type listOfTestObject = new TypeToken<List<Sprint>>() {
        }.getType();
        sprints = new Gson().fromJson(datos, listOfTestObject);
        return sprints;
    }

    /**
     * Muestra un dialog para crear un nuevo sprints
     */
    public void mostrarDialogNuevoSprint() {

        final Dialog dialog = new Dialog(this.getSherlockActivity());
        dialog.setContentView(R.layout.dialog_sprint_nuevo);

        dialog.setTitle(this.getString(R.string.dialog_sprint_nuevo_titulo));

        Button boton = (Button) dialog
                .findViewById(R.id.dialog_sprint_nuevo_boton);

        final DatePicker dtInicio = (DatePicker) dialog
                .findViewById(R.id.dialog_sprint_nuevo_inicio_datepicker);
        final DatePicker dtFin = (DatePicker) dialog
                .findViewById(R.id.dialog_sprint_nuevo_fin_datepicker);

        // Listener botón "aceptar" dialog
        boton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Date inicio = new Date(dtInicio.getYear() - 1900, dtInicio
                        .getMonth(), dtInicio.getDayOfMonth());

                Date fin = new Date(dtFin.getYear() - 1900, dtFin.getMonth(),
                        dtFin.getDayOfMonth());

                if (comprobarFechasCorrectas(inicio, fin)) {
                    Sprint s = new Sprint();
                    s.setInicio(inicio);
                    s.setFin(fin);
                    s.setProyectoId(proyecto);
                    s.setTareaList(new ArrayList<Tarea>());

                    BigDecimal id = enviarSprint(s);

                    if (id != null) {
                        s.setId(id);
                        sprints.add(s);
                        Toast.makeText(getSherlockActivity(),
                                getString(R.string.dialog_sprint_nuevo_exito),
                                Toast.LENGTH_SHORT).show();
                        adapter.updateList(sprints);
                        dialog.dismiss();

                    } else {
                        Toast.makeText(
                                getSherlockActivity(),
                                getString(R.string.dialog_sprint_nuevo_error_servidor),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        dialog.show();

    }

    /**
     * Almacena un sprint en el servidor
     * 
     * @param s
     * @return ID del sprint creado
     */
    private BigDecimal enviarSprint(Sprint s) {

        BigDecimal id = null;
        String jsonSprint = new Gson().toJson(s);
        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("accion",
                Constantes.CREAR_SPRINTS));
        parameters.add(new BasicNameValuePair("Sprint", jsonSprint));
        String idSprint = "";
        try {
            d = ProgressDialog.show(this.getSherlockActivity(),
                    getResources().getString(R.string.espere), "", true, true);
            idSprint = ServerRequest.send(jSessionId, parameters,
                    "CREAR_SPRINTS");
            d.dismiss();
        } catch (Exception e) {
            ProgressDialog.show(this.getActivity().getApplicationContext(), getResources()
                    .getString(R.string.Error),
                    getResources().getString(R.string.Error_sprints_envio), true, true);
            e.printStackTrace();
        }
        if (!idSprint.isEmpty()) {
            id = new BigDecimal(idSprint);
        }

        return id;
    }

    /**
     * Obtiene el proyecto del servidor
     * 
     * @return
     */
    private Proyecto obtenerProyecto() {
        Proyecto proyecto = null;
        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters
                .add(new BasicNameValuePair("accion", Constantes.VER_PROYECTO));
        try {
            d.show();
            String datos = ServerRequest.send(this.jSessionId, parameters,
                    "VER_PROYECTO");
            d.dismiss();
            if (datos.compareTo("null") != 0) {
                proyecto = new Gson().fromJson(datos, Proyecto.class);

            } else {
                ProgressDialog.show(getActivity(), getResources().getString(R.string.Error),
                        getResources().getString(R.string.Error_sin_proyectos), true, true);
            }
        } catch (Exception e) {
            ProgressDialog.show(this.getActivity().getApplicationContext(), getResources()
                    .getString(R.string.Error),
                    getResources().getString(R.string.Error_sin_proyectos), true, true);
            e.printStackTrace();
        }
        return proyecto;
    }

    /**
     * Comprueba que las fechas del sprint que se va a crear son correctas
     * 
     * @param inicio
     * @param fin
     * @return
     */
    private Boolean comprobarFechasCorrectas(Date inicio, Date fin) {
        int aciertos = 0;
        Boolean correctas = false;

        Date aux = new Date();

        if (inicio.compareTo(new Date(aux.getYear(), aux.getMonth(), aux
                .getDate())) >= 0) {

            if (fin.after(inicio)) {

                for (Sprint s : sprints) {
                    if (inicio.after(s.getFin()) || fin.before(s.getInicio())) {
                        aciertos++;
                    }
                }

                if (aciertos == sprints.size()) {
                    correctas = true;
                } else {
                    correctas = false;
                    Toast.makeText(
                            this.getSherlockActivity(),
                            getString(R.string.dialog_sprint_nuevo_error_fechas_no_validas),
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(
                        this.getSherlockActivity(),
                        this.getSherlockActivity().getString(
                                R.string.dialog_sprint_nuevo_error_inicio_fin),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(
                    this.getSherlockActivity(),
                    this.getSherlockActivity().getString(
                            R.string.dialog_sprint_nuevo_error_inicio),
                    Toast.LENGTH_SHORT).show();
        }

        return correctas;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.add(this.getString(R.string.nuevo)).setIcon(R.drawable.ic_mas)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(this.getString(R.string.Tablon)).setIcon(R.drawable.ic_tablon)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals(this.getString(R.string.nuevo))) {
            // PULSADO NUEVO
            mostrarDialogNuevoSprint();
        }
        else if (item.getTitle().equals(this.getString(R.string.Tablon))) {
            Log.d(TAG, "Abrir tablon");
            Intent i = new Intent(this.getSherlockActivity(), TablonActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("proyecto", proyecto);
            i.putExtras(b);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * private ArrayList<Sprint> generarSprints() { ArrayList<Sprint> sprints =
     * new ArrayList<Sprint>(); sprints.add(generarSprint(111, 11, 1, 111, 11,
     * 20, 10)); sprints.add(generarSprint(112, 0, 1, 112, 1, 1, 10));
     * sprints.add(generarSprint(112, 2, 1, 112, 2, 25, 5));
     * sprints.add(generarSprint(112, 3, 1, 112, 3, 15, 0));
     * sprints.get(3).setTareaList(generarTareas()); return sprints; } private
     * Sprint generarSprint(int annoI, int mesI, int diaI, int annoF, int mesF,
     * int diaF, int tareasDone) { Sprint s = new Sprint(); Date inicio = new
     * Date(annoI, mesI, diaI); Date fin = new Date(annoF, mesF, diaF);
     * List<Tarea> tareas = new ArrayList<Tarea>(); for (int i = 0; i < 10; i++)
     * { if (i < tareasDone) { tareas.add(generarTarea('2')); } else {
     * tareas.add(generarTarea('0')); } } s.setTareaList(tareas);
     * s.setInicio(inicio); s.setFin(fin); return s; } private Tarea
     * generarTarea(char done) { Tarea t = new Tarea(); t.setDone(done); return
     * t; } private ArrayList<Tarea> generarTareas() { ArrayList<Tarea> tareas =
     * new ArrayList<Tarea>(); Tarea t1 = new Tarea();
     * t1.setDescripcion("Descripcion tarea 1"); t1.setDone('0');
     * t1.setPrioridad(BigInteger.ONE); Tarea t2 = new Tarea();
     * t2.setDescripcion("Descripcion tarea 2"); t2.setDone('1');
     * t2.setPrioridad(BigInteger.ZERO); Tarea t3 = new Tarea();
     * t3.setDescripcion("Descripcion tarea 3"); t3.setDone('2');
     * t3.setPrioridad(BigInteger.ONE); tareas.add(t1); tareas.add(t2);
     * tareas.add(t3); return tareas; }
     */

}
