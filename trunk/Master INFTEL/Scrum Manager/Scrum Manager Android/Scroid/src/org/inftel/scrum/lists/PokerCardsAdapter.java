package org.inftel.scrum.lists;

import org.inftel.scrum.R;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class PokerCardsAdapter extends BaseAdapter {
	private Context ctx;
	private String[] cartas = { "1", "2", "3", "5", "8", "13", "20", "40",
			"100", "\u221E" };

	public PokerCardsAdapter(Context ctx) {
		this.ctx = ctx;
	}

	public int getCount() {
		return cartas.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View v, ViewGroup parent) {

		TextView carta;
		if (v == null) { // if it's not recycled, initialize some attributes
			carta = new TextView(ctx);
			carta.setLayoutParams(new GridView.LayoutParams(85, 85));
			carta.setText(cartas[position]);
			if (position == cartas.length - 2) {
				carta.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
			} else {
				carta.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
			}
			carta.setGravity(Gravity.CENTER_VERTICAL
					| Gravity.CENTER_HORIZONTAL);
			carta.setTextColor(ctx.getResources()
					.getColor(R.color.verde_oscuro));
		} else {
			carta = (TextView) v;
		}

		carta.setBackgroundResource(R.drawable.fondo_blanco);

		return carta;
	}

}