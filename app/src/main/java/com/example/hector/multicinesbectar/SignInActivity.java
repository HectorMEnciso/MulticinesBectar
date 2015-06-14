package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class SignInActivity extends Activity {
    private TextView nombre;
    private TextView apellidos;
    private TextView dni;
    private TextView email;
    private TextView NickName;
    private TextView Pass;
    private TextView repeatPass;
    private TextView creditCard;
    private Spinner spinner;
    private Button signIn;
    private static int NO_OPTIONS=0;
    private String SHAHash,opnSpinner;
    private boolean SePuedeinsertar=true;
    private Hash h;
    private final String[] languages=new String[]{"English","Spanish"};
    private Mail m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);

        m = new Mail("bectarmulticines@gmail.com", "Iz2zRcCh");

        h = new Hash();

        nombre = (TextView) findViewById(R.id.txtName);
        apellidos = (TextView) findViewById(R.id.txtSurname);
        dni = (TextView) findViewById(R.id.txtDNI);
        email = (TextView) findViewById(R.id.txtEmail);
        NickName = (TextView) findViewById(R.id.txtNickName);
        Pass = (TextView) findViewById(R.id.txtPasswordSign);
        repeatPass = (TextView) findViewById(R.id.txtRepeatPassword);
        creditCard = (TextView) findViewById(R.id.txtCreditCard);
        signIn = (Button) findViewById(R.id.btnSignIn);
        spinner= (Spinner) findViewById(R.id.selectLanguage);

        ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,languages);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adap);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {

                        opnSpinner=languages[position].toString();
                    }
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombre.getText().toString().trim().length()!=0 || email.getText().toString().trim().length()!=0 || apellidos.getText().toString().trim().length()!=0
                        ||  dni.getText().toString().trim().length()!=0 || NickName.getText().toString().trim().length()!=0) {

                    if (!Pass.getText().toString().equals("") && !repeatPass.getText().toString().equals("")) {


                        if ((Pass.getText().toString().equals(repeatPass.getText().toString()))) {

                            TareaWSRegistrarUser tarea = new TareaWSRegistrarUser();

                            if (creditCard.getText().toString().trim().length() == 0) {//Controlamos que el usuario no rellene el campo tarjeta de credito
                                tarea.execute(
                                        dni.getText().toString(),
                                        nombre.getText().toString(),
                                        apellidos.getText().toString(),
                                        email.getText().toString(),
                                        NickName.getText().toString(),
                                        h.computeSHAHash(NickName.getText().toString(), Pass.getText().toString()),
                                        "",opnSpinner);
                            } else {
                                tarea.execute(
                                        dni.getText().toString(),
                                        nombre.getText().toString(),
                                        apellidos.getText().toString(),
                                        email.getText().toString(),
                                        NickName.getText().toString(),
                                        h.computeSHAHash(NickName.getText().toString(), Pass.getText().toString()),
                                        h.computeSHAHash(creditCard.getText().toString()),opnSpinner);
                            }

                            TareaSendEmail i = new TareaSendEmail();
                            i.execute(email.getText().toString());
                        } else {
                            MyCustomToast t =  new MyCustomToast(getString(R.string.ContrasenasNoCoinciden));
                            t.ShowToast(SignInActivity.this);
                        }
                    } else {
                        MyCustomToast t =  new MyCustomToast(getString(R.string.ContrasenasRequerida));
                        t.ShowToast(SignInActivity.this);
                    }
                }
                else{
                    MyCustomToast t =  new MyCustomToast(getString(R.string.CamposObligartorios));
                    t.ShowToast(SignInActivity.this);
                }
            }
        });
    }

    private class TareaSendEmail extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            String[] toArr = {params[0]}; // This is an array, you can add more emails, just separate them with a coma
            m.setTo(toArr); // load array to setTo function
            m.setFrom("bectarmulticines@gmail.com"); // who is sending the email
            m.setSubject("¡Bienvenido a Bectar!");
            m.setBody("Desde el equipo directivo de Bectar esperamos que disfrute con nosotros, Gracias por registrarse :)");

            try {
                // m.addAttachment(getResources().getResourceName(R.drawable.logo_bectar).toString());  // path to file you want to attach
                m.send();


            } catch(Exception e) {
                // some other problem
                Toast.makeText(SignInActivity.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
            }


            return resul;
        }
    }

    private class TareaWSRegistrarUser extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            //le enviamos el nickname al web service
            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://bectar.ddns.net/Api/Usuarios/Usuario/"+params[4].toString());

            del.setHeader("content-type", "application/json");
            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                if(respStr.equals("")){
                        SePuedeinsertar=true;
                }
                else
                {
                    SePuedeinsertar=false;
                }

                if(SePuedeinsertar){

                    HttpPost post = new HttpPost("http://bectar.ddns.net/Api/Usuarios/Usuario");
                    post.setHeader("content-type", "application/json");

                    try
                    {
                        //Construimos el objeto cliente en formato JSON
                        JSONObject dato = new JSONObject();

                        dato.put("DNI", params[0]);
                        dato.put("Nombre", params[1]);
                        dato.put("Apellidos", params[2]);
                        dato.put("Email", params[3]);
                        dato.put("UserName", params[4]);
                        dato.put("Pass", params[5]);
                        dato.put("T_Credito", params[6]);
                        dato.put("Idioma", params[7]);

                        StringEntity entity = new StringEntity(dato.toString());
                        post.setEntity(entity);

                        HttpResponse resp1 = httpClient.execute(post);
                        String respStr1 = EntityUtils.toString(resp1.getEntity());

                        if(!respStr1.equals("true"))
                            resul = false;
                    }
                    catch(Exception ex)
                    {
                        Log.e("ServicioRest", "Error!", ex);
                        resul = false;
                    }
                }

            } catch (Exception ex) {
                SePuedeinsertar=true;
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }
        protected void onPostExecute(Boolean result) {

            if (result)
            {
                if(SePuedeinsertar){
                    MyCustomToast t =  new MyCustomToast(getString(R.string.RegisterSuccess));
                    t.ShowToast(SignInActivity.this);
                    Intent d = new Intent(getApplicationContext(),LogInActivity.class);
                    d.putExtra("username",NickName.getText().toString());
                    startActivity(d);
                }
                else{
                    MyCustomToast t =  new MyCustomToast(getString(R.string.UserAlreadyExists));
                    t.ShowToast(SignInActivity.this);
                }
            }
        }
    }
}
