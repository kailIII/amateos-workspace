
package org.inftel.scrum.activities;

import java.io.File;
import java.io.FileOutputStream;

import org.inftel.scrum.R;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.utils.Constantes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;

public class InformesFragment extends SherlockFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.informes, container, false);
        ((RelativeLayout) view.findViewById(R.id.relative1))
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        descargarPdf("InformeUsuario", Constantes.GET_PDF_USUARIO);
                    }
                });
        ((RelativeLayout) view.findViewById(R.id.relative2))
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        descargarPdf("InformeProyecto", Constantes.GET_PDF_PROYECTO);
                    }
                });
        ((RelativeLayout) view.findViewById(R.id.relative3))
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        descargarPdf("InformeBackLog", Constantes.GET_PDF_BACKLOG);
                    }
                });
        ((RelativeLayout) view.findViewById(R.id.relative4))
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        descargarPdf("InformeSprints", Constantes.GET_PDF_SPRINTS);
                    }
                });
        ((RelativeLayout) view.findViewById(R.id.relative5))
                .setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        descargarPdf("InformeLogs", Constantes.GET_PDF_LOGS);
                    }
                });
        return view;
    }

    public void abrirPDF(String nombreFichero) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String category = new String("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(new File("sdcard/" + nombreFichero + ".pdf")),
                "application/pdf");
        startActivity(intent);
    }

    public void descargarPdf(String nombreFichero, String accion) {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream("sdcard/" + nombreFichero + ".pdf");
            fOut.write(ServerRequest.getPdf("", accion));
            fOut.close();
            abrirPDF(nombreFichero);
        } catch (Exception e) {
            ProgressDialog.show(this.getSherlockActivity(), getResources()
                    .getString(R.string.Error),
                    getResources().getString(R.string.Error_abriendo_archivo), true, true);
        }
    }
}
