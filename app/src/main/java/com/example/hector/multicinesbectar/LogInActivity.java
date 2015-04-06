package com.example.hector.multicinesbectar;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Hector on 29/03/2015.
 */
public class LogInActivity extends Activity {
    private boolean estaMostrada=false;
    private EditText EditPassword;
    private Button ShowPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        EditPassword=(EditText)findViewById(R.id.txtPassword);
        ShowPassword=(Button)findViewById(R.id.ShowPassword);

        ShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(estaMostrada){
                    EditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                estaMostrada=false;
            }
        });

        ShowPassword.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                estaMostrada=true;
                EditPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                return estaMostrada;
            }
        });
    }
}
