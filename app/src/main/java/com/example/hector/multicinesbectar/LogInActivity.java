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

/**
 * Created by Hector on 29/03/2015.
 */
public class LogInActivity extends Activity {
    private EditText EditPassword;
    private EditText txtEmailUserName;
    private EditText txtPassword;
    private Button btnLogin;
    private TextView link_to_register;
    private Button ShowPassword;
    private ArrayList<Usuarios> usuarios= new ArrayList<Usuarios>();
    private boolean sePuedeLogear = false;
    private Hash h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        h = new Hash();

        btnLogin=(Button)findViewById(R.id.btnLogin);

        txtEmailUserName=(EditText)findViewById(R.id.txtEmailUserName);
        txtPassword=(EditText)findViewById(R.id.txtPassword);

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
                tarea.execute();
            }
        });
    }

    private class TareaWSLogIn extends AsyncTask<String,Integer,Boolean> {
        ArrayList<Usuarios> usuarios = new ArrayList<Usuarios>();
        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://10.0.2.2:49461/Api/Usuarios/Usuario");


            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);

                    Usuarios usuario = new Usuarios();

                    usuario.setPass(obj.getString("Pass"));
                    usuarios.add(usuario);
                }

                String UserPassHasheado;

                UserPassHasheado=h.computeSHAHash(txtEmailUserName.getText().toString(),txtPassword.getText().toString());

                for(int x =0;x<usuarios.size();x++){
                    if(UserPassHasheado.equals(usuarios.get(x).getPass())){
                        //Toast.makeText(getApplicationContext(),"El user ya existe..",Toast.LENGTH_SHORT).show();
                        sePuedeLogear=true;
                        break;
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
/*            if(sePuedeLogear){

                HttpPost post = new HttpPost("http://10.0.2.2:49461/Api/Usuarios/Usuario");
                post.setHeader("content-type", "application/json");

                try
                {
                    //Construimos el objeto cliente en formato JSON
                    JSONObject dato = new JSONObject();

                    //dato.put("Id", Integer.parseInt(txtId.getText().toString()));
                    dato.put("DNI", params[0]);
                    //dato.put("ImgUsuario", params[1]);
                    dato.put("Nombre", params[1]);
                    dato.put("Apellidos", params[2]);
                    dato.put("Email", params[3]);
                    dato.put("UserName", params[4]);
                    dato.put("Pass", params[5]);
                    dato.put("T_Credito", params[6]);

                    StringEntity entity = new StringEntity(dato.toString());
                    post.setEntity(entity);

                    HttpResponse resp = httpClient.execute(post);
                    String respStr = EntityUtils.toString(resp.getEntity());

                    if(!respStr.equals("true"))
                        resul = false;
                }
                catch(Exception ex)
                {
                    Log.e("ServicioRest", "Error!", ex);
                    resul = false;
                }
            }*/
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if(sePuedeLogear){
                Toast.makeText(getApplicationContext(),"Usuario logeado",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Usuario no existe",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
