package org.inftel.socialwind.client.web.mvp.ui;

import org.inftel.socialwind.shared.domain.SessionProxy;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SesionUI extends Composite {

    private static SesionUIUiBinder uiBinder = GWT.create(SesionUIUiBinder.class);

    interface SesionUIUiBinder extends UiBinder<Widget, SesionUI> {
    }

    @UiField
    Label namep;

    @UiField
    Label hini;

    @UiField
    Label hfin;

    @UiField
    Label htotal;
    
    @UiField
    Label tt;

    @UiField(provided = true)
    SpotUI spotp;

    private SessionProxy sesion;

    public @UiConstructor
    SesionUI(SessionProxy sesion) {
        this.sesion = sesion;
        spotp = new SpotUI(sesion.getSpot());
        initWidget(uiBinder.createAndBindUi(this));
        setup();
    }

    private void setup() {
        spotp.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                // TODO Auto-generated method stub
                // presenter.onPlayaSeleccionada(playa);
            }
        });

        try {
            // Formateador de Fecha Hora
            DateTimeFormat sdf = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");

            namep.setText(sesion.getSpot().getName());
            hini.setText(sdf.format(sesion.getStart()));
            if (sesion.getEnd() == null) {
                hfin.setText("Surfeando!.");
                tt.setText("");
            } else {
                hfin.setText(sdf.format(sesion.getEnd()));

                // milisegundos con la resta de la fecha de fin y la de inicio
                long resta = sesion.getEnd().getTime() - sesion.getStart().getTime();

                // horas y minutos que estuvo el surfero en la playa
                int horas = (int) (resta / 1000 / 60 / 60);
                int minutos = (int) (resta / 1000 / 60);
                
                htotal.setText(horas + " horas y " + minutos + " minutos");
            }
            
        } catch (NullPointerException e) {
            System.out.println("Puntero Nulo: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
