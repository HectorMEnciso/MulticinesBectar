package com.example.hector.multicinesbectar;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;

/*  Fragment para seccion perfil */ 
public class ProfileFragment extends Fragment {
    // Session Manager Class
    private SessionManager session;
    private ImageView imageUser;
    private TextView PassUserLogin;
    private TextView NombreUserLogin;
    private TextView ApellidosUserLogin;
    private TextView DNIUserLogin;
    private TextView EMAILUserLogin;
    private TextView USERNAMELogin;
    private TextView CreditCard;
    private Button EditarInfo,DarDeBaja;
    private Usuarios usuario = null;

    public ProfileFragment() {
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        session = new SessionManager(getActivity());

        session.checkLogin();

        if (session.isLoggedIn()) {
            rootView = inflater.inflate(R.layout.profile_user_login, container, false);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        usuario = new Usuarios();


        HashMap<String, String> user = session.getUserDetails();// get user data from session

        usuario.setUserName(user.get(SessionManager.KEY_USERNAME));

        usuario.setEmail(user.get(SessionManager.KEY_EMAIL));

        usuario.setDNI(user.get(SessionManager.KEY_DNI));

        usuario.setNombre(user.get(SessionManager.KEY_Nombre));

        usuario.setApellidos(user.get(SessionManager.KEY_Apellidos));

        NombreUserLogin = (TextView) getActivity().findViewById(R.id.NombreUserLogin);
        ApellidosUserLogin = (TextView) getActivity().findViewById(R.id.ApellidosUserLogin);
        DNIUserLogin = (TextView) getActivity().findViewById(R.id.DNIUserLogin);
        EMAILUserLogin = (TextView) getActivity().findViewById(R.id.EMAILUserLogin);
        USERNAMELogin = (TextView) getActivity().findViewById(R.id.USERNAMELogin);
        EditarInfo = (Button) getActivity().findViewById(R.id.btnEditarUserInfo);
        DarDeBaja=(Button) getActivity().findViewById(R.id.btnDarDeBaja);


        Typeface nombre = Typeface.createFromAsset(
                getActivity().getAssets(),
                "fonts/RobotoCondensed-BoldItalic.ttf");
        NombreUserLogin.setTypeface(nombre);

        Typeface apellidos = Typeface.createFromAsset(
                getActivity().getAssets(),
                "fonts/RobotoCondensed-BoldItalic.ttf");
        ApellidosUserLogin.setTypeface(apellidos);

        Typeface DNI = Typeface.createFromAsset(
                getActivity().getAssets(),
                "fonts/RobotoCondensed-BoldItalic.ttf");
        DNIUserLogin.setTypeface(DNI);

        Typeface email = Typeface.createFromAsset(
                getActivity().getAssets(),
                "fonts/RobotoCondensed-BoldItalic.ttf");
        EMAILUserLogin.setTypeface(email);

        Typeface username = Typeface.createFromAsset(
                getActivity().getAssets(),
                "fonts/RobotoCondensed-BoldItalic.ttf");
        USERNAMELogin.setTypeface(username);

        USERNAMELogin.setText("Nombre de usuario: " + usuario.getUserName());

        EMAILUserLogin.setText("Email: " + usuario.getEmail());

        NombreUserLogin.setText("Nombre: " + usuario.getNombre());

        ApellidosUserLogin.setText("Apellidos: " + usuario.getApellidos());

        DNIUserLogin.setText("DNI: " + usuario.getDNI());


        EditarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datos = new Intent(getActivity(), EditUserLoginInfo.class);
                datos.putExtra("Nombre", usuario.getNombre());
                datos.putExtra("Apellidos", usuario.getApellidos());
                datos.putExtra("DNI", usuario.getDNI());
                datos.putExtra("Email", usuario.getEmail());
                datos.putExtra("NickName", usuario.getUserName());
                getActivity().startActivity(datos);
            }

        });
        Typeface edit = Typeface.createFromAsset(
                getActivity().getAssets(),
                "fonts/RobotoCondensed-Bold.ttf");
        EditarInfo.setTypeface(edit);


        DarDeBaja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage(getString(R.string.txtDialogBaja));
                builder1.setCancelable(true);
                builder1.setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                TareaWEliminarUsuario tareaEliminar = new TareaWEliminarUsuario();
                                tareaEliminar.execute(usuario.getUserName().toString());
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
        Typeface baja = Typeface.createFromAsset(
                getActivity().getAssets(),
                "fonts/RobotoCondensed-Bold.ttf");
        DarDeBaja.setTypeface(baja);
    }

    private class TareaWEliminarUsuario extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpDelete del = new HttpDelete("http://bectar.ddns.net/Api/Usuarios/Usuario/"+params[0].toString());

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest", "Error!", ex);
            }

            return resul;
        }
        protected void onPostExecute(Boolean result) {
            if (result) {
                session.logoutUser();
                Toast.makeText(getActivity(), "Usuario dado de baja correctamente", Toast.LENGTH_SHORT).show();
                Intent d = new Intent(getActivity(),MainActivity.class);
                getActivity().startActivity(d);
            }
        }
    }
}
