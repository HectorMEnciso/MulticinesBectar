package com.example.hector.multicinesbectar;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

        CinesList=controller.getAllCines();

        adaptadorCines = new SimpleAdapter(getActivity(),CinesList, R.layout.cines_layout, new String[] { "IdCine" ,"ImgCine","NombreCine"}, new int[] {R.id.IDCine,R.id.ivContactImage, R.id.lblNombreCine});
        lstCines.setAdapter(adaptadorCines);

    }

    private class TareaWSListarCines extends AsyncTask<String,Integer,Boolean> {

        ArrayList<Cines> cines = new ArrayList<Cines>();


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://10.0.2.2:49461/Api/Cines/Cine");

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
                    HashMap<String, String> queryValues = new HashMap<String, String>();
                    queryValues.put("IdCine",String.valueOf(cines.get(i).getIdCine()));
                    queryValues.put("ImgCine",String.valueOf(cines.get(i).getImgCine()));
                    queryValues.put("Direccion",String.valueOf(cines.get(i).getDireccion()));
                    queryValues.put("NombreCine",String.valueOf(cines.get(i).getNombreCine()));
                    controller.insertCine(queryValues);
                }
            }
        }
    }

}