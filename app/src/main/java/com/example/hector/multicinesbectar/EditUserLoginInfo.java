package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
    private Spinner spinner;
    private Button updateUserLoginInfo;
    private static int NO_OPTIONS=0;
    private boolean SePuedeModificar=true;
    private final String[] languages=new String[]{"English","Spanish"};
    private Bundle b;
    private SessionManager session;// Session Manager Class
    private String opnSpinner;
    private int posLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_login_info);

        session = new SessionManager(getApplicationContext());// Session Manager

        nombre = (TextView) findViewById(R.id.txtName);
        apellidos = (TextView) findViewById(R.id.txtSurname);
        DNI = (TextView) findViewById(R.id.txtDNI);
        Email = (TextView) findViewById(R.id.txtEmail);
        updateUserLoginInfo = (Button) findViewById(R.id.btnEditarUserInfo);
        spinner = (Spinner) findViewById(R.id.selectLanguage);

        ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,languages);
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adap);


        b = getIntent().getExtras();//Recogermos el intent con la informacion asociada.

        nombre.setText(b.getString("Nombre"));
        apellidos.setText(b.getString("Apellidos"));
        DNI.setText(b.getString("DNI"));
        Email.setText(b.getString("Email"));
        opnSpinner=b.getString("Idioma");

        switch (opnSpinner){
            case "English":
                posLanguage=0;
                break;
            case "Spanish":
                posLanguage=1;
                break;
        }
        spinner.setSelection(posLanguage);


        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {

                        opnSpinner = languages[position].toString();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


        updateUserLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaWSModificarUser t = new TareaWSModificarUser();
                t.execute(nombre.getText().toString(),
                        apellidos.getText().toString(),
                        Email.getText().toString(),
                        DNI.getText().toString(),
                        b.getString("NickName").toString(),opnSpinner);
            }
            });
    }
//Tarea asincrona encargada modificar la informacion del usuario.
    private class TareaWSModificarUser extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPut put = new HttpPut("http://bectar.ddns.net/Api/Usuarios/Usuario");
            put.setHeader("content-type", "application/json");

            try {

                JSONObject dato = new JSONObject();//Construimos el objeto cliente en formato JSON
                /*
                * Datos a enviar al WS
                * */
                dato.put("Nombre", params[0]);
                dato.put("Apellidos", params[1]);
                dato.put("Email", params[2]);
                dato.put("DNI", params[3]);
                dato.put("UserName",params[4]);
                dato.put("Idioma",params[5]);

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
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(EditUserLoginInfo.this);
                    builder1.setMessage(getString(R.string.txtDialog));
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    session.logoutUser();
                                    MyCustomToast t =  new MyCustomToast(getString(R.string.ModifyUserDataOk));
                                    t.ShowToast(EditUserLoginInfo.this);
                                    Intent d = new Intent(getApplicationContext(),LogInActivity.class);
                                    d.putExtra("username", b.getString("NickName").toString());
                                    startActivity(d);
                                }
                            });
                    builder1.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        }
    }

}
