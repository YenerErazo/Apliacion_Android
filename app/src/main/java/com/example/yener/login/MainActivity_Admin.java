package com.example.yener.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity_Admin extends AppCompatActivity {

    ImageButton btnListaUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_admin);

        btnListaUsu = (ImageButton)findViewById(R.id.btn_usuarios);

        btnListaUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity_Admin.this, MainActivity_Lista_Usuarios.class);
                i.putExtra("res",btnListaUsu+"");

                startActivity(i);

            }
        });


    }
}
