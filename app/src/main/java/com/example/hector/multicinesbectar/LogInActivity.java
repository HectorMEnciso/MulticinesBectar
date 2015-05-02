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
    private TextView link_to_register;
    private Button ShowPassword;
    private ArrayList<Usuarios> usuarios= new ArrayList<Usuarios>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

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
    }

    private class TareaWSObtenerUsuarios extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://10.0.2.2:49461/Api/Usuarios/Usuario");

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);

                    Usuarios usuario = new Usuarios();

                    usuario.setUserName(obj.getString("UserName"));
                    usuarios.add(usuario);
                }
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }
        protected void onPostExecute(Boolean result) {

            if (result)
            {
                Toast.makeText(getApplicationContext(), "Listado de usuarios obtenido", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
