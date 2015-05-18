package com.example.hector.multicinesbectar;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	private static final String PREF_NAME = "BectarPref";
	
	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";
	
	// User name (make variable public to access from outside)
	public static final String KEY_USERNAME = "UserName";
	
	// Email address (make variable public to access from outside)

	public static final String KEY_EMAIL = "Email";

	public static final String KEY_DNI = "DNI";

	public static final String KEY_Nombre = "Nombre";

	public static final String KEY_Apellidos = "Apellidos";

	//public static final String KEY_Pass = "Pass";

	//public static final String KEY_T_Credito = "T_Credito";
	
	// Constructor
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String UserName, String Email,
		 String DNI,String Nombre,String Apellidos,String Pass,String T_Credito){
		// Storing login value as TRUE
		editor.putBoolean(IS_LOGIN, true);
		
		// Storing name in pref
		editor.putString(KEY_USERNAME, UserName);
		
		// Storing email in pref
		editor.putString(KEY_EMAIL, Email);

		editor.putString(KEY_Nombre, Nombre);

		editor.putString(KEY_DNI, DNI);

		editor.putString(KEY_Apellidos, Apellidos);

		//editor.putString(KEY_Pass, Pass);

		//editor.putString(KEY_T_Credito, T_Credito);

		
		// commit changes
		editor.commit();
	}	
	
	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public void checkLogin(){
		// Check login status
		if(!this.isLoggedIn()){
			// user is not logged in redirect him to Login Activity
			//Intent i = new Intent(_context, LogInActivity.class);
			// Closing all the Activities
			//i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			// Add new Flag to start new Activity
			//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			// Staring Login Activity
			//_context.startActivity(i);

		}
		
	}
	
	
	
	/**
	 * Get stored session data
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();
		// user name
		user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
		
		// user email id
		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

		user.put(KEY_DNI, pref.getString(KEY_DNI, null));

		user.put(KEY_Nombre, pref.getString(KEY_Nombre, null));

		user.put(KEY_Apellidos, pref.getString(KEY_Apellidos, null));

		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

		//user.put(KEY_Pass, pref.getString(KEY_Pass, null));

		//user.put(KEY_T_Credito, pref.getString(KEY_T_Credito, null));
		
		// return user
		return user;
	}
	
	/**
	 * Clear session details
	 * */
	public void logoutUser(){
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();

		Toast.makeText(_context,"Ha abandonado la sesión",Toast.LENGTH_SHORT).show();
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, MainActivity.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		// Staring Login Activity
		_context.startActivity(i);
	}
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		return pref.getBoolean(IS_LOGIN, false);//false, valor a devolver si
		//la preferencia no existe.
	}
}
