package com.example.hector.multicinesbectar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    private boolean NewPassEqualOldPass = false;
    private boolean correctPass = false;
    private boolean NewPassEqualRepeatNewPass = false;
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage(getString(R.string.txtDialogSecurity));
                builder1.setCancelable(true);
                builder1.setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                TareaWSModificarSecurityUser securityUser = new TareaWSModificarSecurityUser();
                                securityUser.execute(session.getUserDetails().get("UserName").toString(),OldPass.getText().toString());
                            }
                        });
                builder1.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
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

            try {
                    HttpResponse resp = httpClient.execute(del);
                    String respStr = EntityUtils.toString(resp.getEntity());

                    JSONObject respJSON = new JSONObject(respStr);
                    usuario.setUserName(respJSON.get("UserName").toString());
                    usuario.setPass(respJSON.get("Pass").toString());
                    usuario.setT_Credito(respJSON.get("T_Credito").toString());

                    String UserPassHasheado;

                    UserPassHasheado = h.computeSHAHash(params[0].toString(), params[1].toString());

                    if (UserPassHasheado.equals(usuario.getPass())) {
                            correctPass = true;
                                if (OldPass.getText().toString().equals(NewPass.getText().toString())) {
                                    NewPassEqualOldPass = true;
                                 }
                                if (NewPass.getText().toString().equals(RepeatNewPass.getText().toString())) {
                                     NewPassEqualRepeatNewPass = true;
                                }
                    }
                 }

            catch(Exception ex) {
              Log.e("ServicioRest", "Error!", ex);
              resul = false;
            }
                    if(correctPass && NewPassEqualOldPass==false && NewPassEqualRepeatNewPass){

                    HttpClient httpClient1 = new DefaultHttpClient();
                    HttpPut put = new HttpPut("http://bectar.ddns.net/Api/Usuarios/Usuario");
                    put.setHeader("content-type", "application/json");

                    try {
                        //Construimos el objeto cliente en formato JSON
                        JSONObject dato = new JSONObject();

                        dato.put("Pass",h.computeSHAHash(usuario.getUserName().toString().toString(),NewPass.getText().toString()));
                        dato.put("T_Credito", h.computeSHAHash(CreditCard.getText().toString()));
                        dato.put("UserName",usuario.getUserName().toString());

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
            return resul;
        }
        protected void onPostExecute(Boolean result) {
            if (result)
            {   if(correctPass && NewPassEqualOldPass==false && NewPassEqualRepeatNewPass){
                    session.logoutUser();
                    MyCustomToast t =  new MyCustomToast(getString(R.string.ModifyUserDataOk));
                    t.ShowToast(getActivity());
                    Intent d = new Intent(getActivity(),LogInActivity.class);
                    d.putExtra("username",(usuario.getUserName().toString()));
                    getActivity().startActivity(d);
                }
                else if(!correctPass){
                MyCustomToast t =  new MyCustomToast(getString(R.string.PasswordIncorrect2));
                t.ShowToast(getActivity());
                }
                else if(NewPassEqualOldPass){
                MyCustomToast t =  new MyCustomToast(getString(R.string.OldPassEqualNewPass));
                t.ShowToast(getActivity());

                }
                else if(NewPassEqualRepeatNewPass==false){
                MyCustomToast t =  new MyCustomToast(getString(R.string.NewPassEqualRepeatNewPass));
                t.ShowToast(getActivity());
                }
            }
        }
    }
}
