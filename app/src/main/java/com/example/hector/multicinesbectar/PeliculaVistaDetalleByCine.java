package com.example.hector.multicinesbectar;

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
public class PeliculaVistaDetalleByCine extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener{

    private static final int RECOVERY_DIALOG_REQUEST = 1;

    // YouTube player view
    private YouTubePlayerView youTubeView;

    private Context context;
    private Button btnComprar;
    private SessionManager session;
    private Spinner compras;
    private String opnSpinner;
    ImageView imageViewPelicula;
    TextView TituloPeliculaDetalle;
    TextView DirectorDetalle;
    TextView InterpretesDetalle;
    TextView GeneroDetalle;
    TextView DuracionDetalle;
    TextView AnyoDetalle;
    TextView lblHorario;
    TextView txtSinopsis;
    Uri imageUri;
    String TituloPelicula,Director,Interpretes,Genero,Duracion,Anyo,Trailer,Sinopsis;
    DBController controller = new DBController(this);
    ArrayList<HashMap<String, String>> PeliculasList;
    String NombreCine;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        session = new SessionManager(this);
        context=this;
        session.checkLogin();

        if (session.isLoggedIn()) {//si esta logeado
            setContentView(R.layout.vista_detalle_pelicula);
        }
        else{
            setContentView(R.layout.vista_detalle_pelicula_no_login);
        }

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

        // Initializing video player with developer key
        youTubeView.initialize(Config.DEVELOPER_KEY, this);

        Intent objIntent = getIntent();
        String id = objIntent.getStringExtra("IdPelicula");
        NombreCine = objIntent.getStringExtra("NombreCine");
        PeliculasList = controller.getPeliculainfo(id);
        txtSinopsis= (TextView)findViewById(R.id.Sinopsis);
        lblHorario= (TextView)findViewById(R.id.lblHoraPelicula);

        Typeface horario = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/RobotoCondensed-Bold.ttf");
        lblHorario.setTypeface(horario);

        if (session.isLoggedIn()) {//si esta logeado
            compras=(Spinner)findViewById(R.id.spncompras);
        }
        btnComprar=(Button)findViewById(R.id.btnComprar);
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

        txtSinopsis= (TextView)findViewById(R.id.Sinopsis);

        Typeface font = Typeface.createFromAsset(
                this.getAssets(),
                "fonts/RobotoCondensed-Italic.ttf");
        txtSinopsis.setTypeface(font);


    }
    public void onResume(){
        super.onResume();
        String horario="";
        int tamaño=PeliculasList.size();
        for(int k = 0; k < PeliculasList.size(); k++){
            if(PeliculasList.get(k).get("NombreCine").equals(NombreCine)){
                String horaLimpia=PeliculasList.get(k).get("Hora").substring(0,5);
                String fechaSinAño=PeliculasList.get(k).get("Dia").substring(5, 10);
                String cambioOrden=fechaSinAño.substring(3,5)+"/" +fechaSinAño.substring(0,2) ;
                horario=horario + cambioOrden+" "+horaLimpia +" Sala: "+ PeliculasList.get(k).get("NumeroSala")+"\n\n";
            }

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
        imageViewPelicula.setImageURI(imageUri);
        TituloPeliculaDetalle.setText(TituloPelicula);
        DirectorDetalle.setText("Director: " + Director);
        InterpretesDetalle.setText("Interpretes: " + Interpretes);
        GeneroDetalle.setText("Genero: " + Genero);
        DuracionDetalle.setText("Duracion: " + Duracion);
        AnyoDetalle.setText("Año: " + Anyo);

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
                                    Toast.makeText(getApplicationContext(), "Compra realizada con éxito", Toast.LENGTH_SHORT).show();
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
            player.loadVideo(Trailer);

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
