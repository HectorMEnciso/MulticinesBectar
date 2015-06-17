package com.example.hector.multicinesbectar;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{

    private DBController controller;
    private TabHost tabs;
    private ViewPager pager;
    private GestureDetectorCompat gDetector; // global in fragment
    private int x;
    private TextView IDPelicula, IDCine;

    private ListView lstCines;
    private ListView lstPeliculas;
    private ListView lstIrYa;

    private ArrayList<HashMap<String, String>> CinesList;
    private ArrayList<HashMap<String, String>> PeliculasList;
    private ArrayList<HashMap<String, String>> IrYaList;

    private CinesAdapter adaptadorCines;
    private PeliculasAdapter adaptadorPeliculas;
    private IrYaAdapter adaptadorIrYa;

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
        pager = (ViewPager) getActivity().findViewById(R.id.pager);
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

        //tabs.setCurrentTab(0);

        pager.setAdapter(new MyPagerAdapter(getActivity()));
        pager.setOnPageChangeListener(this);
        tabs.setOnTabChangedListener(this);




        /*tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
            }
        });*/

        CinesList = controller.getAllCines();

        adaptadorCines= new CinesAdapter(getActivity(),CinesList);
        lstCines.setAdapter(adaptadorCines);
        lstCines.setTextFilterEnabled(true);

        PeliculasList = controller.getAllPeliculas();

        adaptadorPeliculas=new PeliculasAdapter(getActivity(),PeliculasList);
        lstPeliculas.setAdapter(adaptadorPeliculas);
        lstPeliculas.setTextFilterEnabled(true);

        IrYaList = controller.getIrYa();

        adaptadorIrYa= new IrYaAdapter(getActivity(),IrYaList);
        lstIrYa.setAdapter(adaptadorIrYa);
        lstIrYa.setTextFilterEnabled(true);

        // #######################################	VISTA DETALLE !!!!!!  #####################################################

        if (PeliculasList.size() != 0) {
            lstPeliculas.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
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
            lstCines.setOnItemClickListener(new OnItemClickListener() {
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
            lstIrYa.setOnItemClickListener(new OnItemClickListener() {
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

        gDetector = new GestureDetectorCompat(getActivity(), new GestureDetector.OnGestureListener() {


            public boolean onDown(MotionEvent e) {
                return true;
            }


            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                Log.i("motion", "onFling has been called!");
                final int SWIPE_MIN_DISTANCE = 120;
                final int SWIPE_MAX_OFF_PATH = 250;
                final int SWIPE_THRESHOLD_VELOCITY = 200;
                try {
                    if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                        return false;
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                        Log.i("motion", "Right to Left");
                        switchTabs(false);
                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                        Log.i("motion", "Left to Right");
                        switchTabs(true);

                    }
                } catch (Exception e) {
                    // nothing
                }
                return false;
            }


            public void onLongPress(MotionEvent e) {

            }


            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                return false;
            }


            public void onShowPress(MotionEvent e) {

            }


            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }
        });

        tabs.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void onTabChanged(String tabId){
        int pageNumber = 0;
        if(tabId.equals("mitab1")){
            pageNumber = 0;
        } else if(tabId.equals("mitab2")){
            pageNumber = 1;
        } else{
            pageNumber = 2;
        }
        pager.setCurrentItem(pageNumber);
    }



    public void switchTabs(boolean direction) {

        Log.w("switch Tabs", "idemo direction");
        if (direction) // true = move left
        {
            if (tabs.getCurrentTab() != 0)
                tabs.setCurrentTab(tabs.getCurrentTab() - 1);
        } else
        // move right
        {
            if (tabs.getCurrentTab() != (tabs.getTabWidget()
                    .getTabCount() - 1))
                tabs.setCurrentTab(tabs.getCurrentTab() + 1);

        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int pageNumber) {
        tabs.setCurrentTab(pageNumber);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}