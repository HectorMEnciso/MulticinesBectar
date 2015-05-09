package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
    Uri imageUri;
    String TituloPelicula,Director,Interpretes,Genero,Duracion,Anyo;
    DBController controller = new DBController(this);
    HashMap<String, String> PeliculasList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_detalle_pelicula);
        Intent objIntent = getIntent();
        String id = objIntent.getStringExtra("id");
        PeliculasList = controller.getPeliculainfo(id);

        imageViewPelicula= (ImageView)findViewById(R.id.imageViewPelicula);
        TituloPeliculaDetalle = (TextView)findViewById(R.id.lblTituloPelicula);
        DirectorDetalle = (TextView)findViewById(R.id.DirectorDetalle);
        InterpretesDetalle = (TextView)findViewById(R.id.InterpretesDetalle);
        GeneroDetalle = (TextView)findViewById(R.id.Genero);
        DuracionDetalle = (TextView)findViewById(R.id.Duracion);
        AnyoDetalle = (TextView)findViewById(R.id.Anyo);

    }
    public void onResume(){
        super.onResume();

        TituloPelicula=PeliculasList.get("Titulo");
        Director=PeliculasList.get("Director");
        Interpretes=PeliculasList.get("Interpretes");
        Genero=PeliculasList.get("Genero");
        Duracion=PeliculasList.get("Duracion");
        Anyo=PeliculasList.get("Anyo");
        imageUri = Uri.parse(PeliculasList.get("ImgPelicula"));

        imageViewPelicula.setImageURI(imageUri);
        TituloPeliculaDetalle.setText(TituloPelicula);
        DirectorDetalle.setText(Director);
        InterpretesDetalle.setText(Interpretes);
        GeneroDetalle.setText(Genero);
        DuracionDetalle.setText(Duracion);
        AnyoDetalle.setText(Anyo);

    }
}
