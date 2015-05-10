package com.example.hector.multicinesbectar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/*  Fragment para seccion perfil */ 
public class ProfileFragment extends Fragment {
    // Session Manager Class
    private SessionManager session;
    private ImageView imageUser;
    private TextView NombreUserLogin;
    private TextView ApellidosUserLogin;
    private TextView DNIUserLogin;
    private TextView EMAILUserLogin;
    private TextView USERNAMELogin;

    public ProfileFragment(){}
    private View rootView;
     private boolean layoutLogeado=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        session = new SessionManager(getActivity());

        session.checkLogin();

        if(!session.isLoggedIn()) {//si no esta logeado
            rootView = inflater.inflate(R.layout.profile, container, false);
            layoutLogeado=false;
        }
        else{
            rootView = inflater.inflate(R.layout.profile_user_login, container, false);
            layoutLogeado=true;
        }
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(layoutLogeado){
            // get user data from session
            HashMap<String, String> user = session.getUserDetails();

            // Username
            String name = user.get(SessionManager.KEY_USERNAME);

            // email
            String email = user.get(SessionManager.KEY_EMAIL);

            imageUser=(ImageView)  getActivity().findViewById(R.id.imageUser);
            NombreUserLogin=(TextView) getActivity().findViewById(R.id.NombreUserLogin);
            ApellidosUserLogin=(TextView) getActivity().findViewById(R.id.ApellidosUserLogin);
            DNIUserLogin=(TextView) getActivity().findViewById(R.id.DNIUserLogin);
            EMAILUserLogin=(TextView) getActivity().findViewById(R.id.EMAILUserLogin);
            USERNAMELogin=(TextView) getActivity().findViewById(R.id.USERNAMELogin);

            imageUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_user));

            USERNAMELogin.setText(name);

            EMAILUserLogin.setText(email);
        }
    }
}