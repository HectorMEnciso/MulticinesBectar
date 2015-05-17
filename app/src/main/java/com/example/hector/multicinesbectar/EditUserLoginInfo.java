package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Hector on 17/05/2015.
 */
public class EditUserLoginInfo  extends Activity {

    private TextView nombre;
    private TextView apellidos;
    private TextView DNI;
    private TextView Email;
    //private TextView NickName;
   /* private TextView Pass;
    private TextView repeatPass;
    private TextView creditCard;*/
    private Button updateUserLoginInfo;
    private static int NO_OPTIONS=0;
   /* private String SHAHash;*/
    private boolean SePuedeModificar=true;
  /*  private Hash h;*/
  private Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_login_info);

        nombre = (TextView) findViewById(R.id.txtName);
        apellidos = (TextView) findViewById(R.id.txtSurname);
        DNI = (TextView) findViewById(R.id.txtDNI);
        Email = (TextView) findViewById(R.id.txtEmail);
        updateUserLoginInfo = (Button) findViewById(R.id.btnEditarUserInfo);
        //NickName= (TextView) findViewById(R.id.txtNickName);


        b = getIntent().getExtras();

        nombre.setText(b.getString("Nombre"));
        apellidos.setText(b.getString("Apellidos"));
        DNI.setText(b.getString("DNI"));
        Email.setText(b.getString("Email"));
        //NickName.setText(b.getString("NickName"));

        updateUserLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaWSModificarUser t = new TareaWSModificarUser();
                t.execute(nombre.getText().toString(),
                        apellidos.getText().toString(),
                        Email.getText().toString(),
                        DNI.getText().toString(),
                        b.getString("NickName").toString());
            }
            });
    }

    private class TareaWSModificarUser extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            //le enviamos el nickname al web service
            HttpClient httpClient = new DefaultHttpClient();
            //HttpPut put = new HttpPut("http://localhost:49461/Api/Usuarios/Usuarios");
            HttpPut put = new HttpPut("http://bectar.ddns.net/Api/Usuarios/Usuario");
            put.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                //dato.put("Id", Integer.parseInt(txtId.getText().toString()));

                //dato.put("ImgUsuario", params[1]);
                dato.put("Nombre", params[0]);
                dato.put("Apellidos", params[1]);
                dato.put("Email", params[2]);
                dato.put("DNI", params[3]);
                dato.put("UserName",params[4]);
                //dato.put("UserName", params[4]);
                //dato.put("Pass", params[5]);
                // dato.put("T_Credito", params[6]);

                StringEntity entity = new StringEntity(dato.toString());
                put.setEntity(entity);

                HttpResponse resp = httpClient.execute(put);
                String respStr = EntityUtils.toString(resp.getEntity());

                if (!respStr.equals("true"))
                resul = false;

            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }
        protected void onPostExecute(Boolean result) {

            if (result)
            {
                if(SePuedeModificar){
                    Toast.makeText(getApplicationContext(), "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
                   /* Intent d = new Intent(getApplicationContext(),LogInActivity.class);
                    d.putExtra("username",NickName.getText().toString());
                    startActivity(d);*/
                }
                else{
                    Toast.makeText(getApplicationContext(),"Ya existe un usuario con ese nombre de usuario\nPor favor elija otro",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
