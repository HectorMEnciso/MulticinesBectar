package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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
    private static int NO_OPTIONS=0;
    private String SHAHash;
    private ArrayList<Usuarios> usuarios= new ArrayList<Usuarios>();
    private boolean SePuedeinsertar=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);

        nombre = (TextView) findViewById(R.id.txtName);
        apellidos = (TextView) findViewById(R.id.txtSurname);
        dni = (TextView) findViewById(R.id.txtDNI);
        email = (TextView) findViewById(R.id.txtEmail);
        NickName = (TextView) findViewById(R.id.txtNickName);
        Pass = (TextView) findViewById(R.id.txtPasswordSign);
        repeatPass = (TextView) findViewById(R.id.txtRepeatPassword);
        creditCard = (TextView) findViewById(R.id.txtCreditCard);
        signIn = (Button) findViewById(R.id.btnSignIn);



        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*TareaWSObtenerUsuarios obtenerUser = new TareaWSObtenerUsuarios();
                obtenerUser.execute();

                if(!usuarios.isEmpty()){
                        Toast.makeText(getApplicationContext(),"Ya existe un usuario con ese NickName\n Por favor, escoja otro.",Toast.LENGTH_SHORT).show();
                        insertar=false;
                    }

                else{
                        insertar=true;
                    }
*/
              //  if(insertar) {
                    TareaWSInsertar tarea = new TareaWSInsertar();
                    tarea.execute(
                            dni.getText().toString(),
                            nombre.getText().toString(),
                            apellidos.getText().toString(),
                            email.getText().toString(),
                            NickName.getText().toString(),
                            computeSHAHash(NickName.getText().toString(), Pass.getText().toString()),
                            creditCard.getText().toString());
              //  }
            }
        });
    }
    public String computeSHAHash(String userName,String password) {
        MessageDigest mdShaPass = null;
        String HashUserPass=userName+password;
        try {
            mdShaPass = MessageDigest.getInstance("SHA-1");//define tipo de hasheo
        } catch (NoSuchAlgorithmException e1) {
        }
        try {
            mdShaPass.update(HashUserPass.getBytes("ASCII"));
        } catch (UnsupportedEncodingException e) {
        }
        byte[] dataPass = mdShaPass.digest();
        try {
            SHAHash = convertToHex(dataPass);
        } catch (IOException e) {
        }
            return SHAHash;
    }

    private static String convertToHex(byte[] data) throws java.io.IOException
    {


        StringBuffer sb = new StringBuffer();
        String hex=null;

        hex= Base64.encodeToString(data, 0, data.length, NO_OPTIONS);

        sb.append(hex);

        return sb.toString();
    }
    //Tarea Asincrona para llamar al WS de insercion en segundo plano
    private class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {
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

                    usuario.setUserName(obj.getString("UserName"));
                    usuarios.add(usuario);
                }

                for(int x =0;x<usuarios.size();x++){
                    if(usuarios.get(x).getUserName().equals(NickName.getText().toString())){
                        //Toast.makeText(getApplicationContext(),"El user ya existe..",Toast.LENGTH_SHORT).show();
                        SePuedeinsertar=false;
                        break;
                    }
                    else{
                        SePuedeinsertar=true;
                    }
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }
        if(SePuedeinsertar){

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
        }
            return resul;
        }

        protected void onPostExecute(Boolean result) {
            if(SePuedeinsertar){
                Toast.makeText(getApplicationContext(),"Usuario registrado",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(),"Usuario ya existe",Toast.LENGTH_SHORT).show();
            }
        }
    }

   /* private class TareaWSObtenerUsuarios extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://10.0.2.2:49461/Api/Usuarios/Usuario/"+ NickName.getText().toString());

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject  respJSON = new JSONObject (respStr);//si el usuario no existe salta la excepcion


                    Usuarios usuario = new Usuarios();
                    if(respJSON.length()==0){
                        insertar=true;
                    }
                else{
                        usuario.setUserName(respJSON.getString(("UserName")));
                        usuarios.add(usuario);
                        insertar=false;
                    }


            } catch (Exception ex) {
                insertar=true;
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }
        protected void onPostExecute(Boolean result) {

            if (result)
            {
                //Toast.makeText(getApplicationContext(),"Listado de usuarios obtenido",Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}
