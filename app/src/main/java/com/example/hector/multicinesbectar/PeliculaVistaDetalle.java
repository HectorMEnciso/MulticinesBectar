package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hector.multicinesbectar.imageutils.ImageLoader;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hector on 09/05/2015.
 */
public class PeliculaVistaDetalle extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{

    private static final int RECOVERY_DIALOG_REQUEST = 1;


    private YouTubePlayerView youTubeView; // YouTube player view
    private SessionManager session;
    private Spinner compras;
    private String opnSpinner;
    private ImageView imageViewPelicula;
    private ImageLoader imgLoader;
    private TextView TituloPeliculaDetalle;
    private TextView DirectorDetalle;
    private TextView InterpretesDetalle;
    private TextView GeneroDetalle;
    private TextView DuracionDetalle;
    private TextView AnyoDetalle;
    private TextView lblHorario;
    private TextView txtSinopsis;
    private TextView vUsu,Valoraciones;
    private Button btnComprar,btnAnadirValoracion;
    private Context context;
    private Uri imageUri;
    private String TituloPelicula,Director,Interpretes,Genero,Duracion,Anyo,Trailer,Sinopsis;
    private DBController controller = new DBController(this);
    private ArrayList<HashMap<String, String>> PeliculasList,ValoracionesList;
    private String id,valora;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        session = new SessionManager(this);
        imgLoader= new ImageLoader((Activity) context);
        context=this;
        session.checkLogin();

        if (session.isLoggedIn()) {//si esta logeado
            setContentView(R.layout.vista_detalle_pelicula);
        }
        else{
            setContentView(R.layout.vista_detalle_pelicula_no_login);
        }

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);


        youTubeView.initialize(Config.DEVELOPER_KEY, this);// Inicializa el reproducto de youtube a partir de la clave generada.

        Intent objIntent = getIntent();
        id = objIntent.getStringExtra("IdPelicula");
        PeliculasList = controller.getPeliculainfo(id);
        txtSinopsis= (TextView)findViewById(R.id.Sinopsis);

        vUsu= (TextView)findViewById(R.id.vUsuarios);

        Valoraciones= (TextView)findViewById(R.id.Valoraciones);

        ValoracionesList=controller.getValoracionesByIdPelicula(id);
        /*
        * Cambiamos el tipo de la fuente a partir de la libreria roboto importada previamente.
        * */
        Typeface font = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/RobotoCondensed-Italic.ttf");
        txtSinopsis.setTypeface(font);

        lblHorario= (TextView)findViewById(R.id.lblHoraPelicula);

        Typeface horario = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/RobotoCondensed-Bold.ttf");
        lblHorario.setTypeface(horario);

        if (session.isLoggedIn()) {//si esta logeado
            compras=(Spinner)findViewById(R.id.spncompras);
        }

        btnComprar=(Button)findViewById(R.id.btnComprar);

        btnAnadirValoracion=(Button)findViewById(R.id.btnAnadirValoracion);

        imageViewPelicula= (ImageView)findViewById(R.id.imageViewPelicula);

        TituloPeliculaDetalle = (TextView)findViewById(R.id.TituloPeliculaDetalle);

        Typeface fontTitulo = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/RobotoCondensed-BoldItalic.ttf");
        TituloPeliculaDetalle.setTypeface(fontTitulo);

        DirectorDetalle = (TextView)findViewById(R.id.DirectorDetalle);

        Typeface directorDetalle = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/RobotoCondensed-Italic.ttf");
        DirectorDetalle.setTypeface(directorDetalle);

        InterpretesDetalle = (TextView)findViewById(R.id.InterpretesDetalle);

        Typeface interpretesDetalle = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/RobotoCondensed-Italic.ttf");
        InterpretesDetalle.setTypeface(interpretesDetalle);

        GeneroDetalle = (TextView)findViewById(R.id.Genero);

        Typeface generoDetalle = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/RobotoCondensed-Italic.ttf");
        GeneroDetalle.setTypeface(generoDetalle);


        DuracionDetalle = (TextView)findViewById(R.id.Duracion);

        Typeface duracionDetalle = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/RobotoCondensed-Italic.ttf");
        DuracionDetalle.setTypeface(duracionDetalle);


        AnyoDetalle = (TextView)findViewById(R.id.Anyo);

        Typeface añoDetalle = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/RobotoCondensed-Italic.ttf");
        AnyoDetalle.setTypeface(añoDetalle);


    }
    public void onResume(){
        super.onResume();
        String horario="";
        int tamaño=PeliculasList.size();
        for(int k = 0; k < PeliculasList.size(); k++){
            String horaLimpia=PeliculasList.get(k).get("Hora").substring(0,5);
            String fechaSinAño=PeliculasList.get(k).get("Dia").substring(5, 10);
            String cambioOrden=fechaSinAño.substring(3,5)+"/" +fechaSinAño.substring(0,2) ;
            horario=horario+cambioOrden+" "+PeliculasList.get(k).get("NombreCine")+": "+horaLimpia +" Sala: "+ PeliculasList.get(k).get("NumeroSala")+"\n\n";
            TituloPelicula=PeliculasList.get(k).get("Titulo");
            Director=PeliculasList.get(k).get("Director");
            Interpretes=PeliculasList.get(k).get("Interpretes");
            Genero=PeliculasList.get(k).get("Genero");
            Duracion=PeliculasList.get(k).get("Duracion");
            Anyo=PeliculasList.get(k).get("Anyo");
            imageUri = Uri.parse(PeliculasList.get(k).get("ImgPelicula"));
            Trailer=PeliculasList.get(k).get("Trailer");
            Sinopsis=PeliculasList.get(k).get("Sinopsis");
        }
        if(ValoracionesList.size()>0){
            Valoraciones.setVisibility(TextView.VISIBLE);
            valora="";
            for(int h=0; h < ValoracionesList.size();h++){
                valora=valora+ValoracionesList.get(h).get("UserName")+": "+ ValoracionesList.get(h).get("TextoValoracion")+"  Val: "+ValoracionesList.get(h).get("ValorRatingBar")+"\n\n";
            }


        }



        imgLoader.DisplayImage(imageUri.toString(), imageViewPelicula);
        TituloPeliculaDetalle.setText(TituloPelicula);
        DirectorDetalle.setText("Director: " + Director);
        InterpretesDetalle.setText("Interpretes: " + Interpretes);
        GeneroDetalle.setText("Genero: " + Genero);
        DuracionDetalle.setText("Duracion: " + Duracion);
        AnyoDetalle.setText("Año: " + Anyo);

        vUsu.setText(valora);

        String[] sesiones = new String[]{};

        if (session.isLoggedIn()) {//si esta logeado

            for(int i=0;i<tamaño;i++){
                sesiones= horario.split("\n\n");
            }
            ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,sesiones);
            adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            compras.setAdapter(adap);

            final String[] finalSesiones = sesiones;
            compras.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent,
                                                   android.view.View v, int position, long id) {

                            opnSpinner = finalSesiones[position].toString();
                        }

                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
            btnComprar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Ha seleccionado: " + opnSpinner + ", ¿Está seguro?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton("Si",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    MyCustomToast t = new MyCustomToast(getString(R.string.BuySuccess));
                                    t.ShowToast(PeliculaVistaDetalle.this);
                                }
                            });
                    builder1.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            });


            btnAnadirValoracion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Intent i = new Intent(getApplicationContext(),Valoracion.class);
                    i.putExtra("IdPelicula",id);
                    startActivity(i);
                }
            });

        }


        lblHorario.setText(horario);
        txtSinopsis.setText(Sinopsis);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {

            // loadVideo() will auto play video
            // Use cueVideo() method, if you don't want to play it automatically
            //player.loadVideo(Config.YOUTUBE_VIDEO_CODE);
           // player.loadVideo(Trailer);
            player.cueVideo(Trailer);

            // Hiding player controls
            player.setPlayerStyle(PlayerStyle.DEFAULT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.DEVELOPER_KEY, this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }
}
