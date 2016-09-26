package com.example.yener.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    Button inicio_sesion, registrarse, admin;
    EditText user, pass;
    String docUser, passUser;

    final String NAMESPACE = "http://tempuri.org/";
    final String URL = "http://bastruk-001-site1.gtempurl.com/ServicioAgenda.asmx";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        user = (EditText) findViewById(R.id.edt_nombre_usuario);
        pass = (EditText) findViewById(R.id.edt_contraseña);

        passUser = pass.getText().toString();
        docUser = user.getText().toString();

        inicio_sesion = (Button) findViewById(R.id.btn_login);
        registrarse = (Button) findViewById(R.id.btn_registarse);

        admin = (Button) findViewById(R.id.btn_admin);


        inicio_sesion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TareaWSBuscar tarea = new TareaWSBuscar();
                tarea.execute();
            }
        });

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity_Registro.class));
            }
        });


        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity_Admin.class));
            }
        });
    }


    //Busca usuario en la DB ** baztrvk
    private class TareaWSBuscar extends AsyncTask<String, Integer, Boolean> {
        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            final String METHOD_NAME = "buscarUsuario";
            final String SOAP_ACTION = "http://tempuri.org/buscarUsuario";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            //Deben coincidir los nombres de las claves con los nombres en los servicios. Ojo.
            request.addProperty("documento", docUser);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
                String res = resultado_xml.toString();
                if (!res.equals("1"))
                    resul = false;
            } catch (Exception e) {
                Log.i("Errores", e.toString());
                resul = false;
            }
            return resul;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            docUser = user.getText().toString();
        }

        protected void onPostExecute(Boolean result) {

            if (result) {

                Toast.makeText(getApplicationContext(), "Usuario válido", Toast.LENGTH_SHORT).show();
                TareaWSBuscarContrasenia tarea1 = new TareaWSBuscarContrasenia();
                tarea1.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Busca la contraseña del usuario ** baztrvk
    private class TareaWSBuscarContrasenia extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {
            boolean resul = true;
            final String METHOD_NAME = "buscarContrasenia";
            final String SOAP_ACTION = "http://tempuri.org/buscarContrasenia";
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            //Deben coincidir los nombres de las claves con los nombres en los servicios. Ojo.
            request.addProperty("contrasena", passUser);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(URL);
            try {
                transporte.call(SOAP_ACTION, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
                String res = resultado_xml.toString();
                if (!res.equals("1"))
                    resul = false;
            } catch (Exception e) {
                Log.i("Errores", e.toString());
                resul = false;
            }
            return resul;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            passUser = pass.getText().toString();
        }
        protected void onPostExecute(Boolean result) {

            if (result) {

                Toast.makeText(getApplicationContext(), "Contraseña correcta", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Login OK", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getApplicationContext(), "Error Contrasenia", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
