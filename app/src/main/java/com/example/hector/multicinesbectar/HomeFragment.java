package com.example.hector.multicinesbectar;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

/*  Fragment para seccion perfil */ 
public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {
    private DBController controller;

    private SearchView mSearchView; //Declaracion global del SearchView sSearchView
    private TabHost tabs;
    private int x;
    private TextView ID;
    public HomeFragment(){}

    ArrayList<HashMap<String, String>> CinesList;
    private ListView lstCines; //Declaracion GLobal del listView lstCoches.
    SimpleAdapter adaptadorCines;

    ArrayList<HashMap<String, String>> PeliculasList;
    private ListView lstPeliculas; //Declaracion GLobal del listView lstCoches.
    SimpleAdapter adaptadorPeliculas;

    ArrayList<HashMap<String, String>> IrYaList;
    private ListView lstIrYa; //Declaracion GLobal del listView lstCoches.
    SimpleAdapter adaptadorIrYa;

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
        lstPeliculas = (ListView)getActivity().findViewById(R.id.LstPeliculas);
        lstIrYa=(ListView)getActivity().findViewById(R.id.LstIrYa);

        controller = new DBController(getActivity());
       // setupSearchView();

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

        TareaWSListarCines tareaListarCines = new TareaWSListarCines();
        tareaListarCines.execute();

        TareaWSListarPeliculas tareaListasPeliculas = new TareaWSListarPeliculas();
        tareaListasPeliculas.execute();

        TareaWSListarProyeciones tareaListasProyeciones = new TareaWSListarProyeciones();
        tareaListasProyeciones.execute();

        mSearchView.setQuery("",false);
        mSearchView.clearFocus();

        CinesList=controller.getAllCines();

        adaptadorCines = new SimpleAdapter(getActivity(),CinesList, R.layout.cines_layout, new String[] { "IdCine" ,"ImgCine","NombreCine"}, new int[] {R.id.IDCine,R.id.ivContactImage, R.id.lblNombreCine});
        lstCines.setAdapter(adaptadorCines);

        PeliculasList=controller.getAllPeliculas();

        adaptadorPeliculas = new SimpleAdapter(getActivity(),PeliculasList, R.layout.peliculas_layout, new String[] { "IdPelicula" ,"ImgPelicula","Titulo","NombreCine","Hora"}, new int[] {R.id.IDPelicula,R.id.ivContactImagePelicula,R.id.lblTituloPelicula, R.id.lblCinePelicula,R.id.lblHoraPelicula});
        lstPeliculas.setAdapter(adaptadorPeliculas);

        IrYaList=controller.getIrYa();

        adaptadorIrYa = new SimpleAdapter(getActivity(),IrYaList, R.layout.irya_layout, new String[] { "IdPelicula" ,"ImgPelicula","Titulo","NombreCine","Hora"}, new int[] {R.id.IDPeliculaIrYa,R.id.ivContactImagePeliculaIrYa,R.id.lblTituloPeliculaIrYa, R.id.lblCinePeliculaIrYa,R.id.lblHoraPeliculaIrYa});
        lstIrYa.setAdapter(adaptadorIrYa);

        // #######################################	VISTA DETALLE !!!!!!  #####################################################

        if(PeliculasList.size()!=0) {
            lstPeliculas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    ID = (TextView) v.findViewById(R.id.IDPelicula);
                    Intent data = new Intent(getActivity(), PeliculaVistaDetalle.class);//Intent explicito a editActivity
                    TextView ti = (TextView) v.findViewById(R.id.lblTituloPelicula);//Obtenemos la referencia al listView TextView
                    String m = ti.getText().toString();//Almacenamos el texto
                    Log.e("m ", m);
                    for (int k = 0; k < PeliculasList.size(); k++) {//Recorremos el ArrayList<Motos> datos
                        if (PeliculasList.get(k).get("Titulo").toString().equalsIgnoreCase(m)) {//Para cada elemento comparamos cada matricula
                            x = k;//Guardamos aquella posicion cuyo elemento coincida.
                        }
                    }

                //Pasamos todos los datos del elemento al vistaDetalle

                    data.putExtra("IdPelicula", PeliculasList.get(x).get("IdPelicula"));
                    //Log.e("id= " ,  PeliculasList.get(x).get("id"));
                    getActivity().startActivity(data);
                }
            });
            //adaptadorPeliculas = new SimpleAdapter(getActivity(), PeliculasList, R.layout.vista_detalle_pelicula, new String[]{"id", "ImgPelicula", "Titulo", "Director", "Interpretes", "Genero","Duracion","Anyo"}, new int[]{R.id.IDPeliculaDetalle, R.id.imageViewPelicula, R.id.TituloPeliculaDetalle, R.id.DirectorDetalle, R.id.InterpretesDetalle, R.id.Genero,R.id.Duracion,R.id.Anyo});
           // lstPeliculas.setAdapter(adaptadorPeliculas);
        }


        // ############################################################################################################################

    }

    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(true); //Define el estado del campo de busqueda.
        mSearchView.setOnQueryTextListener((SearchView.OnQueryTextListener) getActivity());//Define un escuchador para para las acciones dentro del searchView
        mSearchView.setSubmitButtonEnabled(true);//Habilita el boton Submit cuando no esta vacia.
        mSearchView.setQueryHint("Introduzca matricula....");//Texto a mostrar.
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            lstCines.clearTextFilter();
        } else {
            lstCines.setFilterText(newText);
        }
        return true;
    }



    private class TareaWSListarCines extends AsyncTask<String,Integer,Boolean> {

        ArrayList<Cines> cines = new ArrayList<Cines>();


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://10.0.2.2:49461/Api/Cines/Cine");
           // HttpGet del = new HttpGet("http://localhost:49461/Api/Cines/Cine");

            //HttpGet del = new HttpGet("http://bectar.ddns.net/Api/Cines/Cine");

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
                //DialogActualizar("Cines");
            }
        }
    }


    private class TareaWSListarPeliculas extends AsyncTask<String,Integer,Boolean> {

        ArrayList<Peliculas> peliculas = new ArrayList<Peliculas>();


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://10.0.2.2:49461/Api/Peliculas/Pelicula");
            // HttpGet del = new HttpGet("http://localhost:49461/Api/Cines/Cine");
           // HttpGet del = new HttpGet("http://bectar.ddns.net/Api/Peliculas/Pelicula");
            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for(int i=0; i<respJSON.length(); i++)
                {
                    JSONObject obj = respJSON.getJSONObject(i);

                    Peliculas pelicula = new Peliculas();

                    pelicula.setIdPelicula(obj.getInt("IdPelicula"));
                    pelicula.setImgPelicula(obj.getString("ImgPelicula"));
                    pelicula.setTitulo(obj.getString("Titulo"));
                    pelicula.setDirector(obj.getString("Director"));
                    pelicula.setInterpretes(obj.getString("Interpretes"));
                    pelicula.setGenero(obj.getString("Genero"));
                    pelicula.setDuracion(obj.getString("Duracion"));
                    pelicula.setAnyo(obj.getString("Anyo"));
                    peliculas.add(pelicula);
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
                for(int i=0; i<peliculas.size(); i++){
                    if(!controller.existePelicula(peliculas.get(i).getTitulo())){
                        HashMap<String, String> queryValues = new HashMap<String, String>();
                        queryValues.put("IdCine",String.valueOf(peliculas.get(i).getIdPelicula()));
                        queryValues.put("ImgPelicula",String.valueOf(peliculas.get(i).getImgPelicula()));
                        queryValues.put("Titulo",String.valueOf(peliculas.get(i).getTitulo()));
                        queryValues.put("Director",String.valueOf(peliculas.get(i).getDirector()));
                        queryValues.put("Interpretes",String.valueOf(peliculas.get(i).getInterpretes()));
                        queryValues.put("Genero",String.valueOf(peliculas.get(i).getDuracion()));
                        queryValues.put("Duracion",String.valueOf(peliculas.get(i).getDuracion()));
                        queryValues.put("Anyo",String.valueOf(peliculas.get(i).getAnyo()));
                        controller.insertPelicula(queryValues);
                        Intent objIntent = new Intent(getActivity(), MainActivity.class);
                       startActivity(objIntent);
                    }
                }
//                DialogActualizar("Peliculas");
            }
        }
    }


    private class TareaWSListarProyeciones extends AsyncTask<String,Integer,Boolean> {

        ArrayList<Proyecciones> proyeciones = new ArrayList<Proyecciones>();


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://10.0.2.2:49461/Api/Proyecciones/Proyeccion");
           // HttpGet del = new HttpGet("http://bectar.ddns.net/Api/Proyecciones/Proyeccion");
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

                    Proyecciones proyeccion = new Proyecciones();

                    proyeccion.setIdProyeccion(obj.getInt("IdProyeccion"));
                    proyeccion.setIdCine(obj.getInt("IdCine"));
                    proyeccion.setIdPelicula(obj.getInt("IdPelicula"));
                    proyeccion.setIdButaca(obj.getInt("IdButaca"));
                    proyeccion.setIdSala(obj.getInt("IdSala"));
                    proyeccion.setHora(obj.getString("Hora"));

                    String diaReplace="";

                    diaReplace=obj.getString("Dia").replace("0:00:00","");


                    String str[] = diaReplace.split("/");//Vector con caracter delimitador
                    String day = str[0];
                    String month = str[1];
                    String year = str[2];

                    String fechaBuena=year+"-"+month+"-"+day;
                    String fechaBuena2=fechaBuena.replace(" ","");
                    proyeccion.setDia(fechaBuena2);

                    proyeccion.setButacasDisponibles(obj.getString("ButacasDisponibles"));
                    proyeccion.setIdCompra(obj.getString("IdCompra"));
                    proyeciones.add(proyeccion);
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
                for(int i=0; i<proyeciones.size(); i++){
                    if(!controller.existeProyeccion(proyeciones.get(i).getIdProyeccion())){
                        HashMap<String, String> queryValues = new HashMap<String, String>();
                        queryValues.put("IdProyeccion",String.valueOf(proyeciones.get(i).getIdProyeccion()));
                        queryValues.put("IdCine",String.valueOf(proyeciones.get(i).getIdCine()));
                        queryValues.put("IdPelicula",String.valueOf(proyeciones.get(i).getIdPelicula()));
                        queryValues.put("IdButaca",String.valueOf(proyeciones.get(i).getIdButaca()));
                        queryValues.put("IdSala",String.valueOf(proyeciones.get(i).getIdSala()));
                        queryValues.put("Hora",String.valueOf(proyeciones.get(i).getHora()));
                        queryValues.put("Dia", String.valueOf(proyeciones.get(i).getDia()));
                        queryValues.put("ButacasDisponibles",String.valueOf(proyeciones.get(i).getButacasDisponibles()));
                        queryValues.put("IdCompra",String.valueOf(proyeciones.get(i).getIdCompra()));
                        controller.insertProyeccion(queryValues);
                       // Intent objIntent = new Intent(getActivity(), MainActivity.class);
                        //startActivity(objIntent);
                    }
                }
                //DialogActualizar("Peliculas");
            }
        }
    }


    public void DialogActualizar(String tab) {

         ProgressDialog ringProgressDialog=null;

        if(tab.equals("Cines")){
            ringProgressDialog = ProgressDialog.show(getActivity(), "Por favor espere....","Actualizando lista de cines ...", true);
        }
        else if(tab.equals("Peliculas")){
            ringProgressDialog = ProgressDialog.show(getActivity(), "Por favor espere....","Actualizando lista de peliculas ...", true);
        }

        ringProgressDialog.setCancelable(true);
        final ProgressDialog finalRingProgressDialog = ringProgressDialog;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Let the progress ring for 10 seconds...
                    Thread.sleep(2000);
                } catch (Exception e) {

                }
                finalRingProgressDialog.dismiss();
            }
        }).start();
    }

}