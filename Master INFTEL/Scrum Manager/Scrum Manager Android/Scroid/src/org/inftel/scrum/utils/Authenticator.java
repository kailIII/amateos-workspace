package org.inftel.scrum.utils;

import org.inftel.scrum.R;
import org.inftel.scrum.activities.AuthenticatorActivity;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Authenticator extends AbstractAccountAuthenticator {
	
	private final Context mContext;

	public Authenticator(Context context) {
		super(context);
		mContext = context;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
	        String accountType, String authTokenType, String[] requiredFeatures,
	        Bundle options)
			throws NetworkErrorException {
		
		final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(Constantes.PARAM_AUTHTOKEN_TYPE,
            authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
            response);
        
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
       
        return bundle;
		
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		
		if (options != null && options.containsKey(AccountManager.KEY_PASSWORD)) {
            final String password =
                options.getString(AccountManager.KEY_PASSWORD);
            final boolean verified =
                onlineConfirmPassword(account.name, password);
            final Bundle result = new Bundle();
            result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, verified);
            return result;
        }
        
		// Launch AuthenticatorActivity to confirm credentials
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(Constantes.PARAM_USERNAME, account.name);
        intent.putExtra(Constantes.PARAM_CONFIRMCREDENTIALS, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
            response);
        
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
	}
	
	/**
     * Validates user's password on the server
	 * @throws Exception 
     */
    private boolean onlineConfirmPassword(String username, String password)  {
    	
    	boolean confirm = false;
        try {
			confirm = AuthenticationConexion.authenticate(username, password,
			   null/* Handler */, null/* Context */);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       return confirm; 
    	
    }

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		
		if (!authTokenType.equals(Constantes.AUTHTOKEN_TYPE)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE,
                "invalid authTokenType");
            return result;
        }
		
        final AccountManager am = AccountManager.get(mContext);
        final String password = am.getPassword(account);
        
        if (password != null) {
            final boolean verified =
                onlineConfirmPassword(account.name, password);
            if (verified) {
                final Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE,
                    Constantes.ACCOUNT_TYPE);
                result.putString(AccountManager.KEY_AUTHTOKEN, password);
                return result;
            }
        }
        
        // the password was missing or incorrect, return an Intent to an
        // Activity that will prompt the user for the password.
        
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(Constantes.PARAM_USERNAME, account.name);
        intent.putExtra(Constantes.PARAM_AUTHTOKEN_TYPE,
            authTokenType);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
            response);
        
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		
		if (authTokenType.equals(Constantes.AUTHTOKEN_TYPE)) {
            return mContext.getString(R.string.loginMessage);
        }
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		
		final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
		
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		
		final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(Constantes.PARAM_USERNAME, account.name);
        intent.putExtra(Constantes.PARAM_AUTHTOKEN_TYPE,
            authTokenType);
        intent.putExtra(Constantes.PARAM_CONFIRMCREDENTIALS, false);
        
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
		
	}

}
