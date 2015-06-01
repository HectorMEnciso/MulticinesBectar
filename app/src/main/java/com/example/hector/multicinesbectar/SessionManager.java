package com.example.hector.multicinesbectar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import java.util.HashMap;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context _context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "BectarPref";//Nombre del ficheo de las shared preferences.

	private static final String IS_LOGIN = "IsLoggedIn";//Valor para saber si esta logeado.
	
	// Datos del usuario a guardar...
	public static final String KEY_USERNAME = "UserName";

	public static final String KEY_EMAIL = "Email";

	public static final String KEY_DNI = "DNI";

	public static final String KEY_Nombre = "Nombre";

	public static final String KEY_Apellidos = "Apellidos";
	
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

		editor.putBoolean(IS_LOGIN, true);//guardamos como que se ha logeado a true
		
		// Guardos estos datos en el xml de la preferencias.
		editor.putString(KEY_USERNAME, UserName);

		editor.putString(KEY_EMAIL, Email);

		editor.putString(KEY_Nombre, Nombre);

		editor.putString(KEY_DNI, DNI);

		editor.putString(KEY_Apellidos, Apellidos);

		editor.commit();//aplicamos cambios
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

		user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

		user.put(KEY_DNI, pref.getString(KEY_DNI, null));

		user.put(KEY_Nombre, pref.getString(KEY_Nombre, null));

		user.put(KEY_Apellidos, pref.getString(KEY_Apellidos, null));

		user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
		
		// return user
		return user;//Devuevo los datos de usuario guardamos en las preferencias.
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
		return pref.getBoolean(IS_LOGIN, false);
	}
}
