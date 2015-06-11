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

/**
 * Created by Hector on 11/06/2015.
 */
public class CinesAdapter extends BaseAdapter {

    Activity context;
    ArrayList<Cines> cines;
    private ImageLoader imgLoader;


    CinesAdapter(Activity context, ArrayList<Cines> cines) {
        this.context = context;
        this.cines = cines;
    }

    public void UpdateAdaptador(ArrayList<Cines> c){
        this.cines.clear();
        this.cines.addAll(c);
        this.notifyDataSetChanged();
    }


    private class ViewHolder {

        public TextView id;
        public ImageView image;
        public TextView NombreCine;

    }

    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cines_layout, parent, false);
            imgLoader= new ImageLoader((Activity) context);
            holder = new ViewHolder();

            holder.id=(TextView) convertView.findViewById(R.id.IDCine);
            holder.image = (ImageView) convertView.findViewById(R.id.ivContactImage);
            holder.NombreCine = (TextView) convertView.findViewById(R.id.NombreCine);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.id.setText(cines.get(position).getIdCine());
        holder.NombreCine.setText(cines.get(position).getNombreCine());
        imgLoader.DisplayImage(cines.get(position).getImgCine(), holder.image);

        return convertView;
    }

    @Override
    public int getCount() {
        return cines.size();
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