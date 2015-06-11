package com.example.hector.multicinesbectar;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener  {

    private DBController controller;
    private TabHost tabs;
    private int x;
    private TextView IDPelicula, IDCine;
    private ArrayList<HashMap<String, String>> CinesList;
    private ListView lstCines; //Declaracion GLobal del listView lstCoches.
    private SimpleAdapter adaptadorCines;
    private ArrayList<HashMap<String, String>> PeliculasList;
    private ListView lstPeliculas; //Declaracion GLobal del listView lstCoches.
    private SimpleAdapter adaptadorPeliculas;
    private ArrayList<HashMap<String, String>> IrYaList;
    private ListView lstIrYa; //Declaracion GLobal del listView lstCoches.
    private SimpleAdapter adaptadorIrYa;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home, container, false);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lstCines = (ListView) getActivity().findViewById(R.id.LstCines);
        lstPeliculas = (ListView) getActivity().findViewById(R.id.LstPeliculas);
        lstIrYa = (ListView) getActivity().findViewById(R.id.LstIrYa);

        controller = new DBController(getActivity());

        Resources res = getResources();

        tabs = (TabHost) getActivity().findViewById(R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("CINES", res.getDrawable(android.R.drawable.ic_menu_mylocation));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("PELICULAS", res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("mitab3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("IR YA!", res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);


        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
            }
        });

        CinesList = controller.getAllCines();

        adaptadorCines = new SimpleAdapter(getActivity(), CinesList, R.layout.cines_layout, new String[]{"IdCine", "ImgCine", "NombreCine"}, new int[]{R.id.IDCine, R.id.ivContactImage, R.id.lblNombreCine});
        lstCines.setAdapter(adaptadorCines);
        lstCines.setTextFilterEnabled(true);

        PeliculasList = controller.getAllPeliculas();

        adaptadorPeliculas = new SimpleAdapter(getActivity(), PeliculasList, R.layout.peliculas_layout, new String[]{"IdPelicula", "ImgPelicula", "Titulo", "Genero", "Duracion"}, new int[]{R.id.IDPelicula, R.id.ivContactImagePelicula, R.id.lblTituloPelicula, R.id.lblGenero, R.id.lblDuracion});
        lstPeliculas.setAdapter(adaptadorPeliculas);
        lstPeliculas.setTextFilterEnabled(true);

        IrYaList = controller.getIrYa();

        adaptadorIrYa = new SimpleAdapter(getActivity(), IrYaList, R.layout.irya_layout, new String[]{"IdPelicula", "ImgPelicula", "Titulo", "Genero", "Duracion"}, new int[]{R.id.IDPelicula, R.id.ivContactImagePelicula, R.id.lblTituloPelicula, R.id.lblGenero, R.id.lblDuracion});
        lstIrYa.setAdapter(adaptadorIrYa);
        lstIrYa.setTextFilterEnabled(true);

        // #######################################	VISTA DETALLE !!!!!!  #####################################################

        if (PeliculasList.size() != 0) {
            lstPeliculas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    IDPelicula = (TextView) v.findViewById(R.id.IDPelicula);
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
                    getActivity().startActivity(data);
                }
            });

        }


        // ############################################################################################################################

        // ######################################   Listado de peliculas por cine   ###################################################

        if (CinesList.size() != 0) {
            lstCines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    IDCine = (TextView) view.findViewById(R.id.IDCine);
                    Intent data = new Intent(getActivity(), PeliculasByCine.class);//Intent explicito a editActivity
                    TextView ti = (TextView) view.findViewById(R.id.lblNombreCine);//Obtenemos la referencia al listView TextView
                    String m = ti.getText().toString();//Almacenamos el texto
                    Log.e("m ", m);
                    for (int k = 0; k < CinesList.size(); k++) {//Recorremos el ArrayList<Motos> datos
                        if (CinesList.get(k).get("NombreCine").toString().equalsIgnoreCase(m)) {//Para cada elemento comparamos cada matricula
                            x = k;//Guardamos aquella posicion cuyo elemento coincida.
                        }
                    }
                    data.putExtra("IdCine", CinesList.get(x).get("IdCine"));
                    getActivity().startActivity(data);
                }
            });
        }


        // ############################################################################################################################



        // #######################################	VISTA DETALLE IR YA !!!!!!  #####################################################

        if (IrYaList.size() != 0) {
            lstIrYa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    IDPelicula = (TextView) v.findViewById(R.id.IDPelicula);
                    Intent data = new Intent(getActivity(), PeliculaVistaDetalle.class);//Intent explicito a editActivity
                    TextView ti = (TextView) v.findViewById(R.id.lblTituloPelicula);//Obtenemos la referencia al listView TextView
                    String m = ti.getText().toString();//Almacenamos el texto
                    Log.e("m ", m);
                    for (int k = 0; k < IrYaList.size(); k++) {//Recorremos el ArrayList<Motos> datos
                        if (IrYaList.get(k).get("Titulo").toString().equalsIgnoreCase(m)) {//Para cada elemento comparamos cada matricula
                            x = k;//Guardamos aquella posicion cuyo elemento coincida.
                        }
                    }

                    //Pasamos todos los datos del elemento al vistaDetalle

                    data.putExtra("IdPelicula", IrYaList.get(x).get("IdPelicula"));
                    getActivity().startActivity(data);
                }
            });

        }


        // ############################################################################################################################




    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        // Retrieves the system search manager service
        final SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        // Retrieves the SearchView from the search menu item
        final SearchView searchView = (SearchView) searchItem.getActionView();

        // Assign searchable info to SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    lstCines.clearTextFilter();
                } else {
                    lstCines.setFilterText(newText.toString());
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }
}