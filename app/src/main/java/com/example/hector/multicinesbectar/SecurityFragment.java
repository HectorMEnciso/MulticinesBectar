package com.example.hector.multicinesbectar;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Hector on 25/05/2015.
 */
public class SecurityFragment extends Fragment {
    private SessionManager session;
    private EditText OldPass;
    private EditText NewPass;
    private EditText RepeatNewPass;
    private EditText CreditCard;
    private Button EditarInfo;
    private boolean EqualPass=false;
    private boolean correctPass=false;
    private boolean EqualPass2=false;
    private Hash h;
    public SecurityFragment() {
    }
    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        session = new SessionManager(getActivity());

        session.checkLogin();

        if (session.isLoggedIn()) {
            rootView = inflater.inflate(R.layout.security_user_login, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        h = new Hash();
        OldPass=(EditText) getActivity().findViewById(R.id.OldPass);
        NewPass=(EditText) getActivity().findViewById(R.id.NewPass);
        RepeatNewPass=(EditText) getActivity().findViewById(R.id.RepeatNewPass);
        CreditCard=(EditText) getActivity().findViewById(R.id.CreditCard);
        EditarInfo = (Button) getActivity().findViewById(R.id.btnEditarUserInfo);

        EditarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaWSModificarSecurityUser securityUser = new TareaWSModificarSecurityUser();
                securityUser.execute(session.getUserDetails().get("UserName").toString(),OldPass.getText().toString());
            }

        });
    }
    private class TareaWSModificarSecurityUser extends AsyncTask<String,Integer,Boolean> {
        Usuarios usuario = new Usuarios();
        @Override
        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del = new HttpGet("http://bectar.ddns.net/Api/Usuarios/Usuario/"+params[0].toString());//username
            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONObject respJSON = new JSONObject(respStr);
                usuario.setUserName(respJSON.get("UserName").toString());
                usuario.setPass(respJSON.get("Pass").toString());
                usuario.setT_Credito(respJSON.get("T_Credito").toString());

                String UserPassHasheado;

                UserPassHasheado=h.computeSHAHash(params[0].toString(),params[1].toString());

                if(UserPassHasheado.equals(usuario.getPass())) {
                    correctPass = true;
                    if (OldPass.getText().toString().equals(NewPass.getText().toString())) {
                        EqualPass = true;
                    } else if (NewPass.getText().toString().equals(RepeatNewPass.getText().toString())) {
                        EqualPass2 = true;
                    }
                    if(correctPass && !EqualPass && EqualPass2){

                    HttpClient httpClient1 = new DefaultHttpClient();
                    HttpPut put = new HttpPut("http://bectar.ddns.net/Api/Usuarios/Usuario");
                    put.setHeader("content-type", "application/json");

                    try {
                        //Construimos el objeto cliente en formato JSON
                        JSONObject dato = new JSONObject();

                        dato.put("Pass", NewPass.getText().toString());
                        dato.put("T_Credito", h.computeSHAHash(CreditCard.getText().toString()));

                        StringEntity entity1 = new StringEntity(dato.toString());
                        put.setEntity(entity1);

                        HttpResponse resp1 = httpClient1.execute(put);
                        String respStr1 = EntityUtils.toString(resp1.getEntity());

                        if (!respStr1.equals("true"))
                            resul = false;

                    } catch (Exception ex) {
                        Log.e("ServicioRest", "Error!", ex);
                        resul = false;
                    }
                }
                }
                else{
                    correctPass=false;
                }

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
            {   if(correctPass){
                session.logoutUser();
                Toast.makeText(getActivity(), "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
                Intent d = new Intent(getActivity(),LogInActivity.class);
                d.putExtra("username",(usuario.getUserName().toString()));
                getActivity().startActivity(d);
            }

            }
        }
    }
}
