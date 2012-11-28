
package org.inftel.scrum.activities;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.touchimageview.TouchImageView;
import org.inftel.scrum.utils.Constantes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class EstadisticasFragment extends SherlockFragment {

    private static final String TAG = "EstadisticasFragment";
    private String jSessionId;
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

    public void cargarEstadisticas(View view) {
        try {
            TouchImageView imgView1 = (TouchImageView) view.findViewById(R.id.imageView1);
            TouchImageView imgView2 = (TouchImageView) view.findViewById(R.id.imageView2);
            TouchImageView imgView3 = (TouchImageView) view.findViewById(R.id.imageView3);

            Bitmap bitmapBurndown = ServerRequest
                    .getChartImage(jSessionId, Constantes.GET_BURNDOWN);
            Drawable d1 = new BitmapDrawable(getResources(), bitmapBurndown);

            Bitmap bitmapBurnup = ServerRequest.getChartImage(jSessionId, Constantes.GET_BURNUP);
            Drawable d2 = new BitmapDrawable(getResources(), bitmapBurnup);

            Bitmap bitmapPie = ServerRequest.getChartImage(jSessionId, Constantes.GET_PIE);
            Drawable d3 = new BitmapDrawable(getResources(), bitmapPie);

            imgView1.setImageDrawable(d1);
            imgView2.setImageDrawable(d2);
            imgView3.setImageDrawable(d3);

            TextView forecastTextView = (TextView) view.findViewById(R.id.forecastTextView);
            TextView speedTextView = (TextView) view.findViewById(R.id.SpeedTextView);
            ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();

            parameters.add(new BasicNameValuePair("accion", Constantes.GET_FORECAST));
            d=ProgressDialog.show(this.getSherlockActivity(), getResources().getString(R.string.espere), "", true, true);
            String forecast = ServerRequest.send(jSessionId, parameters, "GET_FORECAST");
                        
            
            parameters = new ArrayList<BasicNameValuePair>();
            parameters.add(new BasicNameValuePair("accion", Constantes.GET_SPEED));
            String speed = ServerRequest.send(jSessionId, parameters, "GET_SPEED");

            parameters = new ArrayList<BasicNameValuePair>();
            parameters.add(new BasicNameValuePair("accion", Constantes.GET_DELAY));
            String delay = ServerRequest.send(jSessionId, parameters, "GET_DELAY");
            
            d.dismiss();
            ImageView delayImage = (ImageView) view.findViewById(R.id.delayImageView);
            if (delay.equals("0")) {
                delayImage
                        .setImageDrawable(getResources().getDrawable(R.drawable.termometro_verde));
            }
            else if (delay.equals("1")) {
                delayImage.setImageDrawable(getResources().getDrawable(
                        R.drawable.termometro_amarillo));
            }
            else {
                delayImage.setImageDrawable(getResources().getDrawable(R.drawable.termometro_rojo));
            }

            Log.i(TAG, "forecast: " + forecast);
            Log.i(TAG, "speed: " + speed);
            Log.i(TAG, "delay: " + delay);

            forecastTextView.setText(getResources().getString(R.string.Forecast) + ": " + forecast);
            speedTextView.setText(getResources().getString(R.string.Speed) + ": " + speed);

        } catch (Exception e) {
        	//ProgressDialog.show(this.getActivity().getApplicationContext(), getResources().getString(R.string.Error), getResources().getString(R.string.Error_Estadisticas), true, true);
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.estadisticas, container, false);
        // Cargar jSessionId desde preferencias
        Context context = getActivity().getApplicationContext();
        SharedPreferences prefs = context.getSharedPreferences("loginPreferences",
                Context.MODE_PRIVATE);
        this.jSessionId = prefs.getString("jSessionId", "default_value");
//        d=ServerRequest.compruebaConexionServer(this.getActivity());
        cargarEstadisticas(view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.add("actualizar").setIcon(R.drawable.ic_actualizar)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Scroid", "Seleccionado menu: " + item);
        if (item.getTitle().equals("actualizar")) {
            cargarEstadisticas(this.getView());
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

}
