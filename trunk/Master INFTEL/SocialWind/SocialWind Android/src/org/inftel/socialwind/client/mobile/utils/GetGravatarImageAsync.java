package org.inftel.socialwind.client.mobile.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.inftel.socialwind.client.mobile.R;
import org.inftel.socialwind.client.mobile.activities.ProfileActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class GetGravatarImageAsync extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = "Async";
    private ProfileActivity activity;
    private ProgressBar loadingCircle;
    private ImageView profileImg;

    /**
     * Constructor
     * 
     * @param activity
     */
    public GetGravatarImageAsync(ProfileActivity activity) {
        this.activity = activity;
        loadingCircle = (ProgressBar) activity.findViewById(R.id.imageProgressBar);
        profileImg = (ImageView) activity.findViewById(R.id.profileImageView);
    }

    @Override
    protected void onPreExecute() {
        loadingCircle.setVisibility(View.VISIBLE);
    }

    @Override
    protected Bitmap doInBackground(String... hash) {

        String fileUrl = "http://www.gravatar.com/avatar/" + hash[0] + "?s=100";
        Bitmap bmImg = null;
        URL myFileUrl = null;
        try {
            myFileUrl = new URL(fileUrl);
        } catch (MalformedURLException e) {
            Log.d(TAG, "La URL de acceso a la imagen de gravatar no es correcta");
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bmImg = BitmapFactory.decodeStream(is);

        } catch (IOException e) {
            Log.d(TAG, "Error al obtener la imagen de gravatar");
            e.printStackTrace();
        }

        return bmImg;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        if (image == null) {
            Log.d(TAG, "Obtenida imagen NULL");
        } else {
            loadingCircle.setVisibility(View.INVISIBLE);
        }

        // se establece la imagen
        profileImg.setImageBitmap(image);
    }

}
