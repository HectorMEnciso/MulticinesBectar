package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Hector on 29/03/2015.
 */
public class SignInActivity extends Activity {
    private TextView nombre;
    private TextView apellidos;
    private TextView dni;
    private TextView email;
    private TextView NickName;
    private TextView Pass;
    private TextView repeatPass;
    private TextView creditCard;
    private Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);
        nombre=(TextView) findViewById(R.id.txtName);
        apellidos=(TextView) findViewById(R.id.txtName);
        dni=(TextView) findViewById(R.id.txtName);
        email=(TextView) findViewById(R.id.txtName);
        NickName=(TextView) findViewById(R.id.txtName);
        Pass=(TextView) findViewById(R.id.txtName);
        repeatPass=(TextView) findViewById(R.id.txtName);
        creditCard=(TextView) findViewById(R.id.txtName);
        signIn=(Button) findViewById(R.id.btnSignIn);

        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSInsertar tarea = new TareaWSInsertar();
                tarea.execute(
                        dni.getText().toString(),
                        nombre.getText().toString(),
                        apellidos.getText().toString(),
                        email.getText().toString(),
                        NickName.getText().toString(),
                        Pass.getText().toString(),
                        creditCard.getText().toString());
            }
        });
    }
    //Tarea Asíncrona para llamar al WS de inserción en segundo plano
    private class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

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

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                Toast.makeText(getApplicationContext(),"Usuario registrado",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
