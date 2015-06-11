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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class LogInActivity extends Activity {

    private EditText EditPassword;
    private EditText txtEmailUserName;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView link_to_register;
    private Button ShowPassword;
    private boolean sePuedeLogear = false;
    private Hash h;
    private SessionManager session;// Session Manager Class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);



        session = new SessionManager(getApplicationContext()); // Session Manager

        h = new Hash();

        btnLogin=(Button)findViewById(R.id.btnLogin);
        txtEmailUserName=(EditText)findViewById(R.id.txtEmailUserName);
        txtPassword=(EditText)findViewById(R.id.txtPassword);
        EditPassword=(EditText)findViewById(R.id.txtPassword);
        ShowPassword=(Button)findViewById(R.id.ShowPassword);
        link_to_register=(TextView)findViewById(R.id.link_to_register);

        Bundle b;
        b = getIntent().getExtras();//recogemos el intent con la informacion asociada.

        if(b != null) {
            String username = b.getString("username");
            if (username.equals("")) {
                txtEmailUserName.setText("");
            } else {
                txtEmailUserName.setText(username);
            }
        }

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
    /*Tarea asincrona encargada de comprobar la existencia del usuario que se va a logear, si existe permite el logeo.*/
    private class TareaWSLogIn extends AsyncTask<String,Integer,Boolean> {

        Usuarios usuario = new Usuarios();

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://bectar.ddns.net/Api/Usuarios/Usuario/"+params[0].toString());

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
                    usuario.setUserName(respJSON.get("UserName").toString());
                    usuario.setPass(respJSON.get("Pass").toString());
                    usuario.setDNI(respJSON.get("DNI").toString());
                    usuario.setEmail(respJSON.get("Email").toString());
                    usuario.setNombre(respJSON.get("Nombre").toString());
                    usuario.setApellidos(respJSON.get("Apellidos").toString());
                    usuario.setT_Credito(respJSON.get("T_Credito").toString());

                    String UserPassHasheado;

                    UserPassHasheado=h.computeSHAHash(params[0].toString(),params[1].toString());

                    if(UserPassHasheado.equals(usuario.getPass())){
                        sePuedeLogear=true;
                        //Creamos el login de la sesion
                        session.createLoginSession(usuario.getUserName(), usuario.getEmail(), usuario.getDNI(),
                                usuario.getNombre(), usuario.getApellidos(), usuario.getPass(), usuario.getT_Credito());
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
                MyCustomToast t =  new MyCustomToast(getString(R.string.SesionIniciada));
                t.ShowToast(LogInActivity.this);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
            else{
                MyCustomToast t =  new MyCustomToast(getString(R.string.PasswordIncorrect));
                t.ShowToast(LogInActivity.this);
            }
        }
    }
}
