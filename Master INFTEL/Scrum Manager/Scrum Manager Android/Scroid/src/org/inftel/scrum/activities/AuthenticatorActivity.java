package org.inftel.scrum.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.message.BasicNameValuePair;
import org.inftel.scrum.R;
import org.inftel.scrum.modelXML.Usuario;
import org.inftel.scrum.server.ServerRequest;
import org.inftel.scrum.utils.AuthenticationConexion;
import org.inftel.scrum.utils.Constantes;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

	private static final String TAG = "AuthenticatorActivity";

	private AccountManager mAccountManager;
	private Thread mAuthThread;
	private String mAuthtoken;
	private String mAuthtokenType;

	private EditText nombre;
	private EditText apellidos;
	private DatePicker  fechaNac;
	private EditText email;
	private EditText password;

	private Boolean mConfirmCredentials = false;

	private final Handler mHandler = new Handler();
	private TextView mMessage;
	private String mPassword;
	private EditText mPasswordEdit;

	protected boolean mRequestNewAccount = false;

	private String mUsername;
	private EditText mUsernameEdit;

	public void onCreate(Bundle icicle) {
		Log.d(TAG, "onCreate(" + icicle + ")");
		super.onCreate(icicle);

		mAccountManager = AccountManager.get(this);
		Account[] cuentas = mAccountManager.getAccountsByType(Constantes.ACCOUNT_TYPE);

		Log.d(TAG, "cuentas: " + cuentas.length);

		if (cuentas.length == 0) {
			Log.d(TAG, "cargando datos desde el Intent...");
			final Intent intent = getIntent();

			mUsername = intent.getStringExtra(Constantes.PARAM_USERNAME);
			mAuthtokenType = intent.getStringExtra(Constantes.PARAM_AUTHTOKEN_TYPE);
			mRequestNewAccount = mUsername == null;
			mConfirmCredentials = intent.getBooleanExtra(Constantes.PARAM_CONFIRMCREDENTIALS, false);

			Log.d(TAG, "    request new: " + mRequestNewAccount);

			setContentView(R.layout.login);

			mMessage = (TextView) findViewById(R.id.loginMessage);
			mUsernameEdit = (EditText) findViewById(R.id.usernameEdit);
			mPasswordEdit = (EditText) findViewById(R.id.passwordEdit);

			mUsernameEdit.setText(mUsername);
		} else {

			Log.d(TAG, "Recordando cuenta ...");
			Intent intento = new Intent(AuthenticatorActivity.this, ScroidActivity.class);
			startActivity(intento);
			// setContentView(R.layout.main);
		}

	}

	private CharSequence getMessage() {

		if (TextUtils.isEmpty(mUsername)) {

			final CharSequence msg = getText(R.string.loginMessageNewAccount);
			return msg;
		}
		if (TextUtils.isEmpty(mPassword)) {

			return getText(R.string.loginMessagePasswordEmpty);
		}
		return null;
	}

	

	protected void showProgress() {
		showDialog(0);
	}

	protected void hideProgress() {
		dismissDialog(0);
	}

	public void handleLogin(View view) {
		if (mRequestNewAccount) {
			mUsername = mUsernameEdit.getText().toString();
		}
		mPassword = mPasswordEdit.getText().toString();
		if (TextUtils.isEmpty(mUsername) || TextUtils.isEmpty(mPassword)) {
			mMessage.setText(getMessage());
		} else {
			showProgress();
			// Start authenticating...
			mAuthThread = AuthenticationConexion.attemptAuth(mUsername, mPassword, mHandler, AuthenticatorActivity.this);
		}
	}

	public void handleRegistro(View view) {
		showDialog(1);
		// registro();
	}

	protected Dialog onCreateDialog(int id) {

		ProgressDialog dialog = null;

		Dialog registro = null;

		switch (id) {

			case 0 :
				dialog = new ProgressDialog(this);
				dialog.setMessage(getText(R.string.loginDialogMessage));
				dialog.setIndeterminate(true);
				dialog.setCancelable(true);
				dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						Log.d(TAG, "dialog cancel has been invoked");
						if (mAuthThread != null) {
							mAuthThread.interrupt();
							finish();
						}
					}
				});
				break;

			case 1 :

				registro = crearDialogoRegistro();
				Log.d(TAG, "creando dialogo registro..");

				break;

		}
		if (dialog != null)
			return dialog;
		else
			return registro;

	}

	public Dialog crearDialogoRegistro() {

		Log.d(TAG, "Creando dialogo registro ...");
		Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.registro);
		dialog.setTitle("Formulario de Registro");

		nombre = (EditText) dialog.findViewById(R.id.nombreEdit);
		apellidos = (EditText) dialog.findViewById(R.id.apellidosEdit);
		email = (EditText) dialog.findViewById(R.id.emailEdit);
		password = (EditText) dialog.findViewById(R.id.passwordEditRegistro);
		
		fechaNac = (DatePicker) dialog.findViewById(R.id.fechaPicker);

		Button registroButton = (Button) dialog.findViewById(R.id.enviarButtonRegistro);

		registroButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onClickRegistro(v);
			}

		});

		return dialog;
	}

	public void onClickRegistro(View view) {

		ArrayList<BasicNameValuePair> lista = new ArrayList<BasicNameValuePair>();

		lista.add(new BasicNameValuePair("accion", Constantes.CREAR_USUARIO));
		Usuario usu = new Usuario();
		usu.setApellidos(apellidos.getText().toString());
		usu.setEmail(email.getText().toString());
		
		Date fecha = new Date();
		fecha.setDate(fechaNac.getDayOfMonth());
		fecha.setMonth(fechaNac.getMonth());
		fecha.setYear(fechaNac.getYear());
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy");
		Date fechacambia = new Date(formato.format(fecha));
		
		usu.setFechanac(fechacambia);
		usu.setNombre(nombre.getText().toString());
		Log.d("info", "fechacambia: " + fechacambia.toString());
		Log.d("info", "fecha: " + fecha.toString());
		usu.setPassword(password.getText().toString());

		Gson gson = new Gson();
		String json = gson.toJson(usu);

		lista.add(new BasicNameValuePair("CREAR_USUARIO", json));
		try {
			ServerRequest.send("", lista, Constantes.CREAR_USUARIO);
		} catch (Exception e) {
			ProgressDialog.show(this.getApplicationContext(), getResources().getString(R.string.Error), getResources().getString(R.string.Error_Registro), true, true);
			e.printStackTrace();

		}
		dismissDialog(1);
	}


	protected void finishConfirmCredentials(boolean result) {
		Log.d(TAG, "finishConfirmCredentials()");
		final Account account = new Account(mUsername, Constantes.ACCOUNT_TYPE);
		mAccountManager.setPassword(account, mPassword);
		final Intent intent = new Intent();
		intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, result);
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		finish();
	}

	protected void finishLogin() {
		Log.d(TAG, "finishLogin()");
		final Account account = new Account(mUsername, Constantes.ACCOUNT_TYPE);

		if (mRequestNewAccount) {
			Log.d("info", account.toString());
			Log.d("info", mPassword.toString());
			mAccountManager.addAccountExplicitly(account, mPassword, null);
			// Set contacts sync for this account.
			ContentResolver.setSyncAutomatically(account, ContactsContract.AUTHORITY, true);
		} else {
			mAccountManager.setPassword(account, mPassword);
		}

		final Intent intent = new Intent();
		mAuthtoken = mPassword;
		intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
		intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constantes.ACCOUNT_TYPE);

		if (mAuthtokenType != null && mAuthtokenType.equals(Constantes.AUTHTOKEN_TYPE)) {
			intent.putExtra(AccountManager.KEY_AUTHTOKEN, mAuthtoken);
		}
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);

		Intent intento = new Intent(AuthenticatorActivity.this, ScroidActivity.class);
		startActivity(intento);

		finish();

	}

	public void onAuthenticationResult(boolean result, String user, String pass) {
		Log.d(TAG, "onAuthenticationResult(" + result + ")");

		hideProgress();

		if (result) {

			Editor editor = getSharedPreferences("loginPreferences", MODE_PRIVATE).edit();
			editor.putString("usuario", user);
			editor.putString("password", pass);
			editor.commit();

			if (!mConfirmCredentials) {
				finishLogin();
			} else {
				finishConfirmCredentials(true);
			}
		} else {
			Log.e(TAG, "onAuthenticationResult: failed to authenticate");
			if (mRequestNewAccount) {

				mMessage.setText(getText(R.string.loginMessageFailInvalidBoth));
			} else {

				mMessage.setText(getText(R.string.loginMessageFailInvalidPass));
			}
		}
	}

}
