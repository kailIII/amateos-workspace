package org.inftel.scrum.activities;

import java.util.ArrayList;
import java.util.Calendar;

import org.inftel.scrum.R;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.services.ServicioMensajes;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ScroidActivity extends SherlockFragmentActivity {

	private static final String TAG = "ScroidActivity";
	private ActionBar actionBar;
	private String jSessionId;
	TabsAdapter mTabsAdapter;
	private static PendingIntent pendingIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ActivarServicioMensajes();
		SharedPreferences prefs = getSharedPreferences("loginPreferences",
				MODE_PRIVATE);
		String usuario = prefs.getString("usuario", "default_value");
		String password = prefs.getString("password", "default_value");
		Log.i(TAG, "Recuperados user y password de preferencias: " + usuario
				+ ":" + password);
		try {
			jSessionId = ServerRequest.login(usuario, password);
			prefs.edit().putString("jSessionId", jSessionId).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		initUI();
	}

	private void ActivarServicioMensajes() {

		int comprobacionIntervaloSegundos = 5;

		Intent myIntent = new Intent(ScroidActivity.this,
				ServicioMensajes.class);
		pendingIntent = PendingIntent.getService(ScroidActivity.this, 0,
				myIntent, 0);

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, 10);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(),
				comprobacionIntervaloSegundos * 1000, pendingIntent);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case (0):
			if (resultCode == Activity.RESULT_OK) {
				try {
					SharedPreferences prefs = getSharedPreferences(
							"loginPreferences", MODE_PRIVATE);
					String usuario = prefs
							.getString("usuario", "default_value");
					String password = prefs.getString("password",
							"default_value");
					jSessionId = ServerRequest.login(usuario, password);
					prefs.edit().putString("jSessionId", jSessionId).commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * Comprueba si se dispone de conexión a Internet
	 * 
	 * @return
	 */
	public boolean checkConexionInternet() {
		Boolean conex = false;
		Log.d(TAG, "COMPROBAR CONEXIÓN INTERNET");

		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr != null) {
			NetworkInfo i = conMgr.getActiveNetworkInfo();
			if (i != null) {
				if (i.isConnected() && i.isAvailable())
					conex = true;
			}

		}
		Log.d(TAG, "CONEXIÓN -> " + conex);

		return conex;
	}

	/**
	 * Inicializa la interfaz de usuario, creando las tabs y fragments y
	 * asignando los listener
	 */
	private void initUI() {
		setTheme(R.style.Theme_Sherlock_Light_DarkActionBar);

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);

		Bundle tab1Args = new Bundle(), tab2Args = new Bundle(), tab3Args = new Bundle(), tab4Args = new Bundle(), tab5Args = new Bundle(), tab6Args = new Bundle(), tab7Args = new Bundle();
		tab1Args.putString(getResources().getString(R.string.tabURL),
				getResources().getString(R.string.Proyecto));
		tab2Args.putString(getResources().getString(R.string.tabURL),
				getResources().getString(R.string.Sprints));
		tab3Args.putString(getResources().getString(R.string.tabURL),
				getResources().getString(R.string.Reunion));
		tab4Args.putString(getResources().getString(R.string.tabURL),
				getResources().getString(R.string.Estadisticas));
		tab5Args.putString(getResources().getString(R.string.tabURL),
				getResources().getString(R.string.Informes));
		tab6Args.putString(getResources().getString(R.string.tabURL),
				getResources().getString(R.string.Grupo));
		tab7Args.putString(getResources().getString(R.string.tabURL),
				getResources().getString(R.string.Mensajes));
		setContentView(R.layout.main);
		ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpagercontent);
		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(
				actionBar.newTab().setText(getString(R.string.tab_proyecto)),
				ProyectoFragment.class, tab1Args);
		mTabsAdapter.addTab(
				actionBar.newTab().setText(getString(R.string.tab_sprints)),
				SprintsFragment.class, tab2Args);
		mTabsAdapter.addTab(
				actionBar.newTab().setText(getString(R.string.tab_reunion)),
				ReunionFragment.class, tab3Args);
		mTabsAdapter.addTab(
				actionBar.newTab()
						.setText(getString(R.string.tab_estadisticas)),
				EstadisticasFragment.class, tab4Args);
		mTabsAdapter.addTab(
				actionBar.newTab().setText(getString(R.string.tab_informes)),
				InformesFragment.class, tab5Args);
		mTabsAdapter.addTab(
				actionBar.newTab().setText(getString(R.string.tab_grupos)),
				GruposFragment.class, tab6Args);
		mTabsAdapter.addTab(
				actionBar.newTab().setText(getString(R.string.tab_mensajes)),
				MensajesFragment.class, tab7Args);
	}

	/**
	 * A TabListener receives event callbacks from the action bar as tabs are
	 * deselected, selected, and reselected. A FragmentTransaction is provided
	 * to each of these callbacks; if any operations are added to it, it will be
	 * committed at the end of the full tab switch operation. This lets tab
	 * switches be atomic without the app needing to track the interactions
	 * between different tabs.
	 */
	public static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {
		private SherlockFragment mFragment = null;
		private SherlockListFragment mListFragment = null;
		final static private String tag = TabsAdapter.class.getSimpleName();
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			Log.i(tag, "TabsAdapter: constructor");

			mContext = activity;
			mActionBar = ((SherlockFragmentActivity) activity)
					.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
			Log.i(tag, "***** Exiting TabsAdapter: constructor *****");
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle string) {
			Log.i(tag, "addTab");
			TabInfo info = new TabInfo(clss, string);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
			Log.i(tag, "***** Exiting addTab *****");
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Log.i(tag, "onTabSelected: " + tab.getPosition());
			Object tabTag = tab.getTag();
			int numTabs = mTabs.size();
			for (int i = 0; i < numTabs; i++) {
				if (mTabs.get(i) == tabTag) {
					mViewPager.setCurrentItem(i);
				}
			}
			Log.i(tag, "***** onTabSelected: " + tab.getPosition() + " *****");
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageSelected(int arg0) {
			Log.i(tag, "***** onPageSelected: " + arg0 + " *****");
			mActionBar.setSelectedNavigationItem(arg0);
		}

		@Override
		public Fragment getItem(int arg0) {
			Log.i(tag, "getItem: " + arg0);
			TabInfo info = mTabs.get(arg0);
			Fragment frag = Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
			Log.i(tag, "***** Exiting getItem: " + arg0 + " *****");
			return frag;
		}

		@Override
		public int getItemPosition(Object object) {
			Log.i(tag, "getItemPosition");
			return PagerAdapter.POSITION_UNCHANGED;
		}

		@Override
		public int getCount() {
			int iCount = mTabs.size();
			return iCount;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("logout").setIcon(R.drawable.ic_logout)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		// LOGOUT
		if (item.getTitle().equals("logout")) {
			Log.d("Scroid", "LOGOUT");
			try {
				ServerRequest.logout(jSessionId);
				this.finish();
			} catch (Exception e) {
				ProgressDialog.show(this.getApplicationContext(),
						getResources().getString(R.string.Error),
						getResources().getString(R.string.Error_finalizando),
						true, true);
				e.printStackTrace();
			}
		} else if (item.getItemId() == android.R.id.home) {
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onDestroy() {
		try {
			Log.d("Scroid", "LOGOUT");
			ServerRequest.logout(jSessionId);
		} catch (Exception e) {
			ProgressDialog.show(this.getApplicationContext(), getResources()
					.getString(R.string.Error),
					getResources().getString(R.string.Error_finalizando), true,
					true);
			e.printStackTrace();
		}
		super.onDestroy();
	}

}
