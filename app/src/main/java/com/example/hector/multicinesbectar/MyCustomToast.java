package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Hector on 11/06/2015.
 */
public class MyCustomToast {

    private String ToasText;
    private Activity context;

    public MyCustomToast(String ToasText) {
        this.ToasText=ToasText;
    }

    public void ShowToast(Activity context){

        LayoutInflater inflater = context.getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast_layout, null);

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(ToasText);

        Toast toast = Toast.makeText(context, ToasText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 5, 0);
        toast.setView(layout);
        toast.show();

    }
}
