package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Hector on 16/06/2015.
 */
public class Valoracion extends Activity {

    private RatingBar ratingBar;
    private Button btnSubmit;
    private EditText txtComentario;
    private String textoComentario;
    private Float valorRatingBar;
    private SessionManager session;// Session Manager Class
    private String idPelicula;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valoracion_activity);
        session = new SessionManager(getApplicationContext()); // Session Manager
        addListenerOnRatingBar();
        addListenerOnButton();

        Intent objIntent = getIntent();
        idPelicula = objIntent.getStringExtra("IdPelicula");

        txtComentario=(EditText)findViewById(R.id.txtComentario);
    }
    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                valorRatingBar=rating;
                MyCustomToast t =  new MyCustomToast(valorRatingBar.toString());
                t.ShowToast(Valoracion.this);

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareaWSEnviarValoracion valoracion = new TareaWSEnviarValoracion();
                valoracion.execute(session.getUserDetails().get("IdUsuario"),
                        idPelicula.toString(),txtComentario.getText().toString(),
                        valorRatingBar.toString());
            }

        });
    }

    private class TareaWSEnviarValoracion extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;
            //le enviamos el nickname al web service
            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://bectar.ddns.net/Api/Valoraciones/Valoracion");
            post.setHeader("content-type", "application/json");

            try {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                dato.put("IdUsuario", params[0]);
                dato.put("IdPelicula", params[1]);
                dato.put("TextoValoracion", params[2]);
                dato.put("ValorRatingBar", params[3]);

                StringEntity entity = new StringEntity(dato.toString());
                post.setEntity(entity);

                HttpResponse resp1 = httpClient.execute(post);
                String respStr1 = EntityUtils.toString(resp1.getEntity());

                if (!respStr1.equals("true"))
                    resul = false;
            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                MyCustomToast t =  new MyCustomToast(getString(R.string.ValoracionEnviada));
                t.ShowToast(Valoracion.this);
            }
        }
    }
}
