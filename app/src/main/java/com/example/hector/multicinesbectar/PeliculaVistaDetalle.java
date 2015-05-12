package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hector on 09/05/2015.
 */
public class PeliculaVistaDetalle extends Activity {

    ImageView imageViewPelicula;
    TextView TituloPeliculaDetalle;
    TextView DirectorDetalle;
    TextView InterpretesDetalle;
    TextView GeneroDetalle;
    TextView DuracionDetalle;
    TextView AnyoDetalle;
    TextView lblHorario;
    Uri imageUri;
    String TituloPelicula,Director,Interpretes,Genero,Duracion,Anyo,HoraPelicula;
    DBController controller = new DBController(this);
    ArrayList<HashMap<String, String>> PeliculasList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_detalle_pelicula);
        Intent objIntent = getIntent();
        String id = objIntent.getStringExtra("IdPelicula");
        PeliculasList = controller.getPeliculainfo(id);

        lblHorario= (TextView)findViewById(R.id.lblHorario);
        imageViewPelicula= (ImageView)findViewById(R.id.imageViewPelicula);
        TituloPeliculaDetalle = (TextView)findViewById(R.id.TituloPeliculaDetalle);
        DirectorDetalle = (TextView)findViewById(R.id.DirectorDetalle);
        InterpretesDetalle = (TextView)findViewById(R.id.InterpretesDetalle);
        GeneroDetalle = (TextView)findViewById(R.id.Genero);
        DuracionDetalle = (TextView)findViewById(R.id.Duracion);
        AnyoDetalle = (TextView)findViewById(R.id.Anyo);

    }
    public void onResume(){
        super.onResume();
        String horario="";
        for(int i =0;i<PeliculasList.size(); i++){
            HoraPelicula=PeliculasList.get(i).get("Hora");
            TituloPelicula=PeliculasList.get(i).get("Titulo");
            Director=PeliculasList.get(i).get("Director");
            Interpretes=PeliculasList.get(i).get("Interpretes");
            Genero=PeliculasList.get(i).get("Genero");
            Duracion=PeliculasList.get(i).get("Duracion");
            Anyo=PeliculasList.get(i).get("Anyo");
            imageUri = Uri.parse(PeliculasList.get(i).get("ImgPelicula"));
       }
        for(int k = 0; k < PeliculasList.size(); k++){
            horario=horario+PeliculasList.get(k).get("NombreCine")+": "+PeliculasList.get(k).get("Hora")+"\n";
        }
        imageViewPelicula.setImageURI(imageUri);
        TituloPeliculaDetalle.setText(TituloPelicula);
        DirectorDetalle.setText("Director: " + Director);
        InterpretesDetalle.setText("Interpretes: " + Interpretes);
        GeneroDetalle.setText("Genero: " + Genero);
        DuracionDetalle.setText("Duracion: " + Duracion);
        AnyoDetalle.setText("Año: " + Anyo);
        lblHorario.setText(horario);
        horario="";
    }
}
