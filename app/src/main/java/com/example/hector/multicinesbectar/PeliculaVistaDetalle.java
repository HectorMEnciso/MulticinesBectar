package com.example.hector.multicinesbectar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by Hector on 09/05/2015.
 */
public class PeliculaVistaDetalle extends YouTubeBaseActivity implements
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
    Uri imageUri;
    String TituloPelicula,Director,Interpretes,Genero,Duracion,Anyo,Trailer;
    DBController controller = new DBController(this);
    ArrayList<HashMap<String, String>> PeliculasList;
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
        PeliculasList = controller.getPeliculainfo(id);
        lblHorario= (TextView)findViewById(R.id.lblHoraPelicula);
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
        for(int k = 0; k < PeliculasList.size(); k++){
            horario=horario+PeliculasList.get(k).get("NombreCine")+": "+PeliculasList.get(k).get("Hora") +" Sala: "+ PeliculasList.get(k).get("NumeroSala")+"\n\n";
            TituloPelicula=PeliculasList.get(k).get("Titulo");
            Director=PeliculasList.get(k).get("Director");
            Interpretes=PeliculasList.get(k).get("Interpretes");
            Genero=PeliculasList.get(k).get("Genero");
            Duracion=PeliculasList.get(k).get("Duracion");
            Anyo=PeliculasList.get(k).get("Anyo");
            imageUri = Uri.parse(PeliculasList.get(k).get("ImgPelicula"));
            Trailer=PeliculasList.get(k).get("Trailer");
        }
        imageViewPelicula.setImageURI(imageUri);
        TituloPeliculaDetalle.setText(TituloPelicula);
        DirectorDetalle.setText("Director: " + Director);
        InterpretesDetalle.setText("Interpretes: " + Interpretes);
        GeneroDetalle.setText("Genero: " + Genero);
        DuracionDetalle.setText("Duracion: " + Duracion);
        AnyoDetalle.setText("Ano: " + Anyo);
        lblHorario.setText(horario);
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
