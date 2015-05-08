package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LogInActivity extends Activity {
    private EditText EditPassword;
    private EditText txtEmailUserName;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView link_to_register;
    private Button ShowPassword;
    private boolean sePuedeLogear = false;
    private Hash h;
    // Session Manager Class
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        // Session Manager
        session = new SessionManager(getApplicationContext());
        h = new Hash();

        btnLogin=(Button)findViewById(R.id.btnLogin);

        txtEmailUserName=(EditText)findViewById(R.id.txtEmailUserName);
        txtPassword=(EditText)findViewById(R.id.txtPassword);

        Bundle b;
        b = getIntent().getExtras();

        if(b != null) {
            String username = b.getString("username");
            if (username.equals("")) {
                txtEmailUserName.setText("");
            } else {
                txtEmailUserName.setText(username);
            }
        }

        EditPassword=(EditText)findViewById(R.id.txtPassword);
        ShowPassword=(Button)findViewById(R.id.ShowPassword);
        link_to_register=(TextView)findViewById(R.id.link_to_register);

        link_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(data);
            }
        });

        ShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                EditPassword.setSelection(EditPassword.getText().length());
            }
        });

        ShowPassword.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EditPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                EditPassword.setSelection(EditPassword.getText().length());
                return true;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaWSLogIn tarea = new TareaWSLogIn();
                tarea.execute(txtEmailUserName.getText().toString(),txtPassword.getText().toString());
            }
        });
    }

    private class TareaWSLogIn extends AsyncTask<String,Integer,Boolean> {
        //ArrayList<Usuarios> usuarios = new ArrayList<Usuarios>();
        Usuarios usu= new Usuarios();
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del = new HttpGet("http://10.0.2.2:49461/Api/Usuarios/Usuario/"+params[0].toString());
            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                if(respStr.equals("")){
                    sePuedeLogear=false;
                }
                else
                {
                    JSONObject respJSON = new JSONObject(respStr);
                    String username = respJSON.get("UserName").toString();
                    String pass = respJSON.get("Pass").toString();
                    usu.setUserName(username);
                    usu.setPass(pass);

                    String UserPassHasheado;

                    UserPassHasheado=h.computeSHAHash(params[0].toString(),params[1].toString());

                    if(UserPassHasheado.equals(usu.getPass())){
                        sePuedeLogear=true;
                        // Creating user login session
                        // For testing i am stroing name, email as follow
                        // Use user real data
                        session.createLoginSession(params[0].toString(),params[1].toString());
                    }
                    else{
                        sePuedeLogear=false;
                    }
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if(sePuedeLogear){
                Toast.makeText(getApplicationContext(),"Usuario logeado",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Usuario no existe",Toast.LENGTH_SHORT).show();
            }
            // Staring MainActivity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
