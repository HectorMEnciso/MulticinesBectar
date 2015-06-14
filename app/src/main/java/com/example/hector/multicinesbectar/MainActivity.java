package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends Activity implements SearchView.OnQueryTextListener {
    private String[] titulos;
    private DrawerLayout NavDrawerLayout;
    private ListView NavList;
    private ArrayList<Item_objct> NavItms;
    private TypedArray NavIcons;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private NavigationAdapter NavAdapter;
    private SessionManager session;// Session Manager Class
    private DBController controller = new DBController(this);
    private Locale myLocale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigationdrawer_activity);


        session = new SessionManager(getApplicationContext());    // Session class instance

        session.checkLogin();

        if(session.getUserDetails().get("Language").equals("English")){
            setLocale("en");
        }
        else{
            setLocale("es");
        }

        NavDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//Drawer Layout

        NavList = (ListView) findViewById(R.id.listaDrawer);//Lista

        View header = getLayoutInflater().inflate(R.layout.header, null);//Declaramos el header el cual sera el layout de header.xml

        NavList.addHeaderView(header);//Establecemos header

        NavIcons = getResources().obtainTypedArray(R.array.navigation_iconos);//Tomamos listado  de imgs desde drawable

        titulos = getResources().getStringArray(R.array.nav_options);//Tomamos listado  de titulos desde el string-array de los recursos @string/nav_options

        NavItms = new ArrayList<Item_objct>();//Listado de titulos de barra de navegacion


        if(!session.isLoggedIn()) {//si no esta logeado

           NavItms.add(new Item_objct(titulos[0], NavIcons.getResourceId(0, -1)));

           NavItms.add(new Item_objct(titulos[1], NavIcons.getResourceId(1, -1)));

           NavItms.add(new Item_objct(titulos[3], NavIcons.getResourceId(3, -1)));

        }
        else{

            NavItms.add(new Item_objct(titulos[0], NavIcons.getResourceId(0, -1)));

            NavItms.add(new Item_objct(titulos[1], NavIcons.getResourceId(1, -1)));

            NavItms.add(new Item_objct(titulos[2], NavIcons.getResourceId(2, -1)));

            NavItms.add(new Item_objct(titulos[3], NavIcons.getResourceId(3, -1)));

            NavItms.add(new Item_objct(titulos[4], NavIcons.getResourceId(4, -1)));
        }

        NavAdapter= new NavigationAdapter(this,NavItms);//Declaramos y seteamos nuestro adaptador al cual le pasamos el array con los titulos
        NavList.setAdapter(NavAdapter);

        mTitle = mDrawerTitle = getTitle();//Titulo a mostrar

        //Declaramos el mDrawerToggle y las imgs a utilizar
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                NavDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* Icono de navegacion*/
                R.string.app_name,  /* "open drawer" description */
                R.string.hello_world  /* "close drawer" description */
        ) {

            /** Llamado cuando el navigation drawer se cierra */
            public void onDrawerClosed(View view) {
                Log.e("Cerrado completo", "!!");
            }

            /** Llamado cuando el navigation drawer se abre */
            public void onDrawerOpened(View drawerView) {
                Log.e("Apertura completa", "!!");
            }
        };


        NavDrawerLayout.setDrawerListener(mDrawerToggle);//Establecemos que mDrawerToggle declarado anteriormente sea el DrawerListener

        getActionBar().setDisplayHomeAsUpEnabled(true); //Establecemos que el ActionBar muestre el Boton Home

        //Establecemos la accion al clickear sobre cualquier item del menu
        NavList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                if(!session.isLoggedIn()) {//si no esta logeado
                    if(position==0){
                        MostrarFragmentNoLogin(position + 2);
                    }
                    else{
                        MostrarFragmentNoLogin(position);
                    }

                }
                else{
                    if(position==0){
                        MostrarFragment(position+2);
                    }
                    else{
                        MostrarFragment(position);
                    }
                }
            }
        });
        //Cuando la aplicacion cargue por defecto mostrar la opcion Home
        if(!session.isLoggedIn()) {//si no esta logeado
            MostrarFragmentNoLogin(1);
        }
        else{
            MostrarFragment(1);
        }
    }
    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
    private void MostrarFragmentNoLogin(int position) {
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment= new MapsCinesActivity();
                break;
            case 3:
                Toast.makeText(getApplicationContext(), "Opcion " + titulos[position] + " en construcción!", Toast.LENGTH_SHORT).show();
                break;
            default:
                //si no esta la opcion mostrara un toast y nos mandara a Home
                Toast.makeText(getApplicationContext(), "Opcion " + titulos[position-1] + " no disponible!", Toast.LENGTH_SHORT).show();
                fragment = new HomeFragment();
                position=1;
                break;
        }
        //Validamos si el fragment no es nulo
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            NavList.setItemChecked(position, true);// Actualizamos el contenido segun la opcion elegida

            NavList.setSelection(position);

            setTitle(titulos[position-1]);//Cambiamos el titulo en donde decia

            NavDrawerLayout.closeDrawer(NavList);//Cerramos el menu deslizable
        } else {
            //Si el fragment es nulo mostramos un mensaje de error.
            Log.e("Error  ", "MostrarFragment"+position);
        }
    }

    private void MostrarFragment(int position) {
        // update the navigationdrawer_activity content by replacing fragments
        Fragment fragment = null;

        switch (position) {
            case 1:
                fragment = new HomeFragment();
                break;

            case 2:
                fragment= new MapsCinesActivity();
                break;
            case 3:
                fragment=new SecurityFragment();
                break;
            case 5:
                fragment = new ProfileFragment();
                break;
            default:
                //si no esta la opcion mostrara un toast y nos mandara a Home
                Toast.makeText(getApplicationContext(), "Opcion " + titulos[position-1] + " no disponible!", Toast.LENGTH_SHORT).show();
                fragment = new HomeFragment();
                position=1;
                break;
        }
        //Validamos si el fragment no es nulo
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // Actualizamos el contenido segun la opcion elegida
            NavList.setItemChecked(position, true);
            NavList.setSelection(position);
            //Cambiamos el titulo en donde decia "
            setTitle(titulos[position-1]);
            //Cerramos el menu deslizable
            NavDrawerLayout.closeDrawer(NavList);
        } else {
            //Si el fragment es nulo mostramos un mensaje de error.
            Log.e("Error  ", "MostrarFragment"+position);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.e("mDrawerToggle pushed", "x");
            return true;
        }
        int id = item.getItemId();

        if (id == R.id.LogIn) {
            Intent LogIn = new Intent(this,LogInActivity.class);
            startActivity(LogIn);
            return true;
        }
        if (id == R.id.update) {
            deleteDatabase("DBMulticines.db");

            TareaWSListarCines tareaListarCines = new TareaWSListarCines();
            tareaListarCines.execute();

            TareaWSListarPeliculas tareaListasPeliculas = new TareaWSListarPeliculas();
            tareaListasPeliculas.execute();

            TareaWSListarSalas tareaListasProyecionesSalas = new TareaWSListarSalas();
            tareaListasProyecionesSalas.execute();

            TareaWSListarProyeciones tareaListasProyeciones = new TareaWSListarProyeciones();
            tareaListasProyeciones.execute();/*Tarea asincrona con la que nos descargamos la informacion de todos los cines y lo almacenamos en la SQLITE*/

            DialogActualizar("Peliculas");


            return true;
        }

        if (id == R.id.SignIn) {
            Intent SignIn = new Intent(this,SignInActivity.class);
            startActivity(SignIn);
            return true;
        }

        if (id == R.id.LogOut) {
            session.logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void DialogActualizar(String tab) {

        ProgressDialog ringProgressDialog = null;

        if (tab.equals("Cines")) {
            ringProgressDialog = ProgressDialog.show(this, "Por favor espere....", "Actualizando lista de cines ...", true);
        } else if (tab.equals("Peliculas")) {
            ringProgressDialog = ProgressDialog.show(this, "Por favor espere....", "Actualizando lista de peliculas ...", true);
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
                runOnUiThread(new Runnable() {
                    public void run() {
                        MyCustomToast t = new MyCustomToast(getString(R.string.DataUpdatedSuccessfully));
                        t.ShowToast(MainActivity.this);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(!session.isLoggedIn()) {
            inflater.inflate(R.menu.menu_main, menu);
        }
        else{
            inflater.inflate(R.menu.menu_main_login, menu);
        }
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private class TareaWSListarCines extends AsyncTask<String, Integer, Boolean> {

        ArrayList<Cines> cines = new ArrayList<Cines>();

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://bectar.ddns.net/Api/Cines/Cine");

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);

                    Cines cine = new Cines();

                    cine.setIdCine(obj.getInt("IdCine"));
                    cine.setImgCine(obj.getString("ImgCine"));
                    cine.setDireccion(obj.getString("Direccion"));
                    cine.setNombreCine(obj.getString("NombreCine"));

                    cines.add(cine);
                }
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                for (int i = 0; i < cines.size(); i++) {
                    if (!controller.existeCine(cines.get(i).getNombreCine())) {
                        HashMap<String, String> queryValues = new HashMap<String, String>();
                        queryValues.put("IdCine", String.valueOf(cines.get(i).getIdCine()));
                        queryValues.put("ImgCine", String.valueOf(cines.get(i).getImgCine()));
                        queryValues.put("Direccion", String.valueOf(cines.get(i).getDireccion()));
                        queryValues.put("NombreCine", String.valueOf(cines.get(i).getNombreCine()));
                        controller.insertCine(queryValues);
                    }
                }
            }
        }
    }

    /*Tarea asincrona con la que nos descargamos la informacion de todas las peliculas y lo almacenamos en la SQLITE*/
    private class TareaWSListarPeliculas extends AsyncTask<String, Integer, Boolean> {

        ArrayList<Peliculas> peliculas = new ArrayList<Peliculas>();

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://bectar.ddns.net/Api/Peliculas/Pelicula");

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for (int i = 0; i < respJSON.length(); i++) {
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
                    pelicula.setTrailer(obj.getString("Trailer"));
                    pelicula.setSinopsis(obj.getString("Sinopsis"));
                    peliculas.add(pelicula);
                }
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                for (int i = 0; i < peliculas.size(); i++) {
                    if (!controller.existePelicula(peliculas.get(i).getTitulo())) {
                        HashMap<String, String> queryValues = new HashMap<String, String>();
                        queryValues.put("IdCine", String.valueOf(peliculas.get(i).getIdPelicula()));
                        queryValues.put("ImgPelicula", String.valueOf(peliculas.get(i).getImgPelicula()));
                        queryValues.put("Titulo", String.valueOf(peliculas.get(i).getTitulo()));
                        queryValues.put("Director", String.valueOf(peliculas.get(i).getDirector()));
                        queryValues.put("Interpretes", String.valueOf(peliculas.get(i).getInterpretes()));
                        queryValues.put("Genero", String.valueOf(peliculas.get(i).getGenero()));
                        queryValues.put("Duracion", String.valueOf(peliculas.get(i).getDuracion()));
                        queryValues.put("Anyo", String.valueOf(peliculas.get(i).getAnyo()));
                        queryValues.put("Trailer", String.valueOf(peliculas.get(i).getTrailer()));
                        queryValues.put("Sinopsis", String.valueOf(peliculas.get(i).getSinopsis()));
                        controller.insertPelicula(queryValues);
                    }
                }
            }
        }
    }

    /*Tarea asincrona con la que nos descargamos la informacion de todas las proyecciones y lo almacenamos en la SQLITE*/
    private class TareaWSListarProyeciones extends AsyncTask<String, Integer, Boolean> {

        ArrayList<Proyecciones> proyeciones = new ArrayList<Proyecciones>();


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://bectar.ddns.net/Api/Proyecciones/Proyeccion");

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);

                    Proyecciones proyeccion = new Proyecciones();

                    proyeccion.setIdProyeccion(obj.getInt("IdProyeccion"));
                    proyeccion.setIdCine(obj.getInt("IdCine"));
                    proyeccion.setIdPelicula(obj.getInt("IdPelicula"));
                    proyeccion.setIdButaca(obj.getInt("IdButaca"));
                    proyeccion.setIdSala(obj.getInt("IdSala"));
                    proyeccion.setHora(obj.getString("Hora"));

                    String diaReplace = "";

                    diaReplace = obj.getString("Dia").replace("0:00:00", "");


                    String str[] = diaReplace.split("/");//Vector con caracter delimitador
                    String day = str[0];
                    String month = str[1];
                    String year = str[2];

                    String fechaBuena = year + "-" + month + "-" + day;
                    String fechaBuena2 = fechaBuena.replace(" ", "");
                    proyeccion.setDia(fechaBuena2);

                    proyeccion.setButacasDisponibles(obj.getString("ButacasDisponibles"));
                    proyeccion.setIdCompra(obj.getString("IdCompra"));
                    proyeciones.add(proyeccion);
                }
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                for (int i = 0; i < proyeciones.size(); i++) {
                    if (!controller.existeProyeccion(proyeciones.get(i).getIdProyeccion())) {
                        HashMap<String, String> queryValues = new HashMap<String, String>();
                        queryValues.put("IdProyeccion", String.valueOf(proyeciones.get(i).getIdProyeccion()));
                        queryValues.put("IdCine", String.valueOf(proyeciones.get(i).getIdCine()));
                        queryValues.put("IdPelicula", String.valueOf(proyeciones.get(i).getIdPelicula()));
                        queryValues.put("IdButaca", String.valueOf(proyeciones.get(i).getIdButaca()));
                        queryValues.put("IdSala", String.valueOf(proyeciones.get(i).getIdSala()));
                        queryValues.put("Hora", String.valueOf(proyeciones.get(i).getHora()));
                        queryValues.put("Dia", String.valueOf(proyeciones.get(i).getDia()));
                        queryValues.put("ButacasDisponibles", String.valueOf(proyeciones.get(i).getButacasDisponibles()));
                        queryValues.put("IdCompra", String.valueOf(proyeciones.get(i).getIdCompra()));
                        controller.insertProyeccion(queryValues);
                    }
                }
            }
        }
    }

    /*Tarea asincrona con la que nos descargamos la informacion de todass las salas y lo almacenamos en la SQLITE*/
    private class TareaWSListarSalas extends AsyncTask<String, Integer, Boolean> {

        ArrayList<Salas> salas = new ArrayList<Salas>();

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del = new HttpGet("http://bectar.ddns.net/Api/Salas/Sala");

            del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray respJSON = new JSONArray(respStr);

                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);

                    Salas sala = new Salas();

                    sala.setIdSala(obj.getInt("IdSala"));
                    sala.setNumeroSala(obj.getInt("NumeroSala"));
                    sala.setNumeroFilas(obj.getInt("NumeroFilas"));
                    sala.setNumeroButacas(obj.getInt("NumeroButacas"));
                    salas.add(sala);
                }
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                for (int i = 0; i < salas.size(); i++) {
                    if (!controller.existeSala(salas.get(i).getIdSala())) {
                        HashMap<String, String> queryValues = new HashMap<String, String>();
                        queryValues.put("IdSala", String.valueOf(salas.get(i).getIdSala()));
                        queryValues.put("NumeroSala", String.valueOf(salas.get(i).getNumeroSala()));
                        queryValues.put("NumeroFilas", String.valueOf(salas.get(i).getNumeroFilas()));
                        queryValues.put("NumeroButacas", String.valueOf(salas.get(i).getNumeroButacas()));
                        controller.insertSala(queryValues);
                    }
                }
            }
        }
    }

}
