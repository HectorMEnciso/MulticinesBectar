package com.example.hector.multicinesbectar;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/*  Fragment para seccion perfil */ 
public class HomeFragment extends Fragment {
    private SearchView mSearchView; //Declaracion global del SearchView sSearchView
    private TabHost tabs;
    public HomeFragment(){}
    ArrayList<HashMap<String, String>> CinesList;
    private ListView lstCines; //Declaracion GLobal del listView lstCoches.
    SimpleAdapter adaptadorCines;
    Context context;
    private DBController controller;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home, container, false);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSearchView = (SearchView) getActivity().findViewById(R.id.searchView1);//Obtenemos la referencia al SearchView mSearchView
        //setupSearchView();
        lstCines = (ListView)getActivity().findViewById(R.id.LstCines);

        controller = new DBController(getActivity());


        Resources res = getResources();

        tabs=(TabHost) getActivity().findViewById(R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("CINES", res.getDrawable(android.R.drawable.ic_menu_mylocation));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("PELICULAS",res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("IR YA!",res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);


        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
            }
        });

        TareaWSListarCines tarea = new TareaWSListarCines();
        tarea.execute();

        mSearchView.setQuery("",false);
        mSearchView.clearFocus();

        CinesList=controller.getAllCines();

        adaptadorCines = new SimpleAdapter(getActivity(),CinesList, R.layout.cines_layout, new String[] { "IdCine" ,"ImgCine","NombreCine"}, new int[] {R.id.IDCine,R.id.ivContactImage, R.id.lblNombreCine});
        lstCines.setAdapter(adaptadorCines);

    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(true); //Define el estado del campo de busqueda.
        mSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) getActivity());//Define un escuchador para para las acciones dentro del searchView
        mSearchView.setSubmitButtonEnabled(true);//Habilita el boton Submit cuando no esta vacia.
        mSearchView.setQueryHint("Introduzca matricula....");//Texto a mostrar.
    }


    private class TareaWSListarCines extends AsyncTask<String,Integer,Boolean> {

        ArrayList<Cines> cines = new ArrayList<Cines>();


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://10.0.2.2:49461/Api/Cines/Cine");
           // HttpGet del = new HttpGet("http://localhost:49461/Api/Cines/Cine");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);

                    Cines cine = new Cines();

                    cine.setIdCine(obj.getInt("IdCine"));
                    cine.setImgCine(obj.getString("ImgCine"));
                    cine.setDireccion(obj.getString("Direccion"));
                    cine.setNombreCine(obj.getString("NombreCine"));

                    cines.add(cine);
                }
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                for(int i=0; i<cines.size(); i++){
                    if(!controller.existeCine(cines.get(i).getNombreCine())){
                        HashMap<String, String> queryValues = new HashMap<String, String>();
                        queryValues.put("IdCine",String.valueOf(cines.get(i).getIdCine()));
                        queryValues.put("ImgCine",String.valueOf(cines.get(i).getImgCine()));
                        queryValues.put("Direccion",String.valueOf(cines.get(i).getDireccion()));
                        queryValues.put("NombreCine",String.valueOf(cines.get(i).getNombreCine()));
                        controller.insertCine(queryValues);
                        Intent objIntent = new Intent(getActivity(), MainActivity.class);
                        startActivity(objIntent);
                    }
                }
                DialogActualizarCines();
            }
        }
    }

    public void DialogActualizarCines() {
        final ProgressDialog ringProgressDialog = ProgressDialog.show(getActivity(), "Por favor espere....","Actualizando lista de cines ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Let the progress ring for 10 seconds...
                    Thread.sleep(2000);
                } catch (Exception e) {

                }
                ringProgressDialog.dismiss();
            }
        }).start();
    }

}