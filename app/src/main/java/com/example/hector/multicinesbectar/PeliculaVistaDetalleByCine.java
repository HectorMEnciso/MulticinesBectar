package com.example.hector.multicinesbectar;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
        setContentView(R.layout.vista_detalle_pelicula);

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
        for(int k = 0; k < PeliculasList.size(); k++){
            if(PeliculasList.get(k).get("NombreCine").equals(NombreCine)){
                String horaLimpia=PeliculasList.get(k).get("Hora").substring(0,5);
                horario=horario+horaLimpia +" Sala: "+ PeliculasList.get(k).get("NumeroSala")+"\n\n";
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
