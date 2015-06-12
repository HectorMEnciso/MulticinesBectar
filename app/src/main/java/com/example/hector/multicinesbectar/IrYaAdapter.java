package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hector.multicinesbectar.imageutils.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hector on 12/06/2015.
 */
public class IrYaAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<HashMap<String, String>> peliculas;
    private ImageLoader imgLoader;


    IrYaAdapter(Activity context, ArrayList<HashMap<String, String>> peliculas) {
        this.context = context;
        this.peliculas = peliculas;
    }

    public void UpdateAdaptador(ArrayList<HashMap<String, String>> c){
        this.peliculas.clear();
        this.peliculas.addAll(c);
        this.notifyDataSetChanged();
    }


    private class ViewHolder {

        public TextView IdPelicula;
        public ImageView ImgPelicula;
        public TextView Titulo;
        public TextView Genero;
        public TextView Duracion;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.irya_layout, parent, false);
            imgLoader= new ImageLoader((Activity) context);

            holder = new ViewHolder();

            holder.IdPelicula=(TextView) convertView.findViewById(R.id.IDPelicula);
            holder.ImgPelicula = (ImageView) convertView.findViewById(R.id.ivContactImagePelicula);
            holder.Titulo = (TextView) convertView.findViewById(R.id.lblTituloPelicula);
            holder.Genero = (TextView) convertView.findViewById(R.id.lblGenero);
            holder.Duracion = (TextView) convertView.findViewById(R.id.lblDuracion);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.IdPelicula.setText(peliculas.get(position).get("IdPelicula"));
        imgLoader.DisplayImage(peliculas.get(position).get("ImgPelicula"), holder.ImgPelicula);
        holder.Titulo.setText(peliculas.get(position).get("Titulo"));
        holder.Genero.setText(peliculas.get(position).get("Genero"));
        holder.Duracion.setText(peliculas.get(position).get("Duracion"));

        return convertView;
    }

    @Override
    public int getCount() {
        return peliculas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
