package com.example.hector.multicinesbectar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private Button EditarInfo;
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

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // Username
        usuario.setUserName(user.get(SessionManager.KEY_USERNAME));
        // email
        usuario.setEmail(user.get(SessionManager.KEY_EMAIL));

        usuario.setDNI(user.get(SessionManager.KEY_DNI));

        usuario.setNombre(user.get(SessionManager.KEY_Nombre));

        usuario.setApellidos(user.get(SessionManager.KEY_Apellidos));

        //usuario.setPass(user.get(SessionManager.KEY_Pass));

        // usuario.setT_Credito(user.get(SessionManager.KEY_T_Credito));


        //imageUser=(ImageView)  getActivity().findViewById(R.id.imageUser);
        NombreUserLogin = (TextView) getActivity().findViewById(R.id.NombreUserLogin);
        ApellidosUserLogin = (TextView) getActivity().findViewById(R.id.ApellidosUserLogin);
        //PassUserLogin=(TextView) getActivity().findViewById(R.id.PassUserLogin);
        DNIUserLogin = (TextView) getActivity().findViewById(R.id.DNIUserLogin);
        EMAILUserLogin = (TextView) getActivity().findViewById(R.id.EMAILUserLogin);
        USERNAMELogin = (TextView) getActivity().findViewById(R.id.USERNAMELogin);
        // CreditCard=(TextView) getActivity().findViewById(R.id.CreditCard);
        EditarInfo = (Button) getActivity().findViewById(R.id.btnEditarUserInfo);

        // imageUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_user));

        USERNAMELogin.setText("Nombre de usuario\n" + usuario.getUserName());

        EMAILUserLogin.setText("Email\n" + usuario.getEmail());

        NombreUserLogin.setText("Nombre\n" + usuario.getNombre());

        ApellidosUserLogin.setText("Apellidos\n" + usuario.getApellidos());

        DNIUserLogin.setText("DNI\n" + usuario.getDNI());

//            PassUserLogin.setText("Contraseña\n"+usuario.getPass());

        //CreditCard.setText("Tarjeta credito\n"+usuario.getT_Credito());

        EditarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent datos = new Intent(getActivity(),EditUserLoginInfo.class);
                datos.putExtra("Nombre",usuario.getNombre());
                datos.putExtra("Apellidos",usuario.getApellidos());
                datos.putExtra("DNI",usuario.getDNI());
                datos.putExtra("Email",usuario.getEmail());
                datos.putExtra("NickName",usuario.getUserName());
                getActivity().startActivity(datos);
            }

        });
    }
}