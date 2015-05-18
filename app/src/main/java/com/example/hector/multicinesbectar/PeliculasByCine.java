package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
    private int x;
    private TextView IDPelicula;
    private TextView NombreCine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Intent objIntent = getIntent();

        String IdCine = objIntent.getStringExtra("IdCine");

        lstPeliculasByCine = (ListView) findViewById(R.id.LstPeliculas);

        controller = new DBController(this);

        PeliculasByCineList=controller.getAllPeliculasByCineId(IdCine);

        adaptadorPeliculasByCine=new SimpleAdapter(this,PeliculasByCineList, R.layout.peliculas_layout, new String[] { "IdPelicula" ,"ImgPelicula","Titulo","Genero","Duracion","NombreCine"}, new int[] {R.id.IDPelicula,R.id.ivContactImagePelicula,R.id.lblTituloPelicula, R.id.lblGenero,R.id.lblDuracion,R.id.NombreCine});

        lstPeliculasByCine.setAdapter(adaptadorPeliculasByCine);

        // #######################################	VISTA DETALLE !!!!!!  #####################################################

        if(PeliculasByCineList.size()!=0) {
            lstPeliculasByCine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    IDPelicula = (TextView) v.findViewById(R.id.IDPelicula);
                    NombreCine= (TextView) v.findViewById(R.id.NombreCine);
                    Intent data = new Intent(getApplicationContext(), PeliculaVistaDetalleByCine.class);//Intent explicito a editActivity
                    TextView ti = (TextView) v.findViewById(R.id.lblTituloPelicula);//Obtenemos la referencia al listView TextView
                    String m = ti.getText().toString();//Almacenamos el texto
                    Log.e("m ", m);
                    for (int k = 0; k < PeliculasByCineList.size(); k++) {//Recorremos el ArrayList<Motos> datos
                        if (PeliculasByCineList.get(k).get("Titulo").toString().equalsIgnoreCase(m)) {//Para cada elemento comparamos cada matricula
                            x = k;//Guardamos aquella posicion cuyo elemento coincida.
                        }
                    }
                    data.putExtra("IdPelicula", PeliculasByCineList.get(x).get("IdPelicula"));
                    data.putExtra("NombreCine", PeliculasByCineList.get(x).get("NombreCine"));
                    startActivity(data);
                }
            });
        }
        // ############################################################################################################################
    }
}
