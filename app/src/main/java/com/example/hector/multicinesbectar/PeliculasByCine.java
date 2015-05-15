package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hector on 15/05/2015.
 */
public class PeliculasByCine extends Activity{
    private DBController controller;
    private ArrayList<HashMap<String, String>> PeliculasByCineList;
    private ListView lstPeliculasByCine;
    private SimpleAdapter adaptadorPeliculasByCine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Intent objIntent = getIntent();

        String IdCine = objIntent.getStringExtra("IdCine");

        lstPeliculasByCine = (ListView) findViewById(R.id.LstPeliculas);

        controller = new DBController(this);

        PeliculasByCineList=controller.getAllPeliculasByCineId(IdCine);

        adaptadorPeliculasByCine=new SimpleAdapter(this,PeliculasByCineList, R.layout.peliculas_layout, new String[] { "IdPelicula" ,"ImgPelicula","Titulo","Genero","Duracion"}, new int[] {R.id.IDPelicula,R.id.ivContactImagePelicula,R.id.lblTituloPelicula, R.id.lblGenero,R.id.lblDuracion});

        lstPeliculasByCine.setAdapter(adaptadorPeliculasByCine);
    }
}
