package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hector on 19/05/2015.
 */
public class SplashScreen extends Activity {

    private static int SPLASH_TIME_OUT = 3000;// Splash screen timer
    private DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        deleteDatabase("DBMulticines.db");

        TareaWSListarCines tareaListarCines = new TareaWSListarCines();
        tareaListarCines.execute();

        TareaWSListarPeliculas tareaListasPeliculas = new TareaWSListarPeliculas();
        tareaListasPeliculas.execute();

        TareaWSListarSalas tareaListasProyecionesSalas = new TareaWSListarSalas();
        tareaListasProyecionesSalas.execute();

        TareaWSListarProyeciones tareaListasProyeciones = new TareaWSListarProyeciones();
        tareaListasProyeciones.execute();/*Tarea asincrona con la que nos descargamos la informacion de todos los cines y lo almacenamos en la SQLITE*/



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                finish();// close this activity
            }
        }, SPLASH_TIME_OUT);

        TextView title = (TextView) findViewById(R.id.titleBectar1);
        Typeface font = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/Roboto-BlackItalic.ttf");
        title.setTypeface(font);

        TextView title2 = (TextView) findViewById(R.id.titleBectar2);
        Typeface font2 = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/Roboto-BlackItalic.ttf");
        title2.setTypeface(font2);
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
                //DialogActualizar("Peliculas");
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