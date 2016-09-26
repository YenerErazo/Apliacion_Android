package com.example.yener.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity_Registro extends AppCompatActivity {

    final String NAMESPACE = "http://tempuri.org/";
    final String URL = "http://bastruk-001-site1.gtempurl.com/ServicioAgenda.asmx";


    String nombre, documento, contrasena, contrasena1, email,genero1, foto, firma1;

    final static int CONS = 1;
    String datoBdFoto;
    ImageButton btnFoto;
    Bitmap bmpFoto = null;
    Bitmap imagenFirma = null;
    boolean validacionCampos=false;

    public static final int SIGNATURE_ACTIVITY = 0;
    String datoBdFirma;
    ImageView firma;

    Button cancelar, registro, btnFirma;
    EditText edtNombre,edtDocumento, edtEmail, edtcontrasena, edtcontrasena1;
    Spinner sp_genero;

    String genero[] = new String[]{"Femenino", "Masculino"};
    ArrayAdapter<String> Adaptador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_registro);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        registro = (Button) findViewById(R.id.btn_registrarUsu);
        cancelar = (Button) findViewById(R.id.btn_cancel_registro);
        btnFoto = (ImageButton) findViewById(R.id.btn_tomarFoto);
        firma = (ImageView)findViewById(R.id.ima_firma);
        btnFirma = (Button) findViewById(R.id.btn_firma_registro);

        edtNombre = (EditText) findViewById(R.id.edt_registroNombre);
        edtDocumento = (EditText) findViewById(R.id.edt_registroIdentificacion);
        edtEmail = (EditText) findViewById(R.id.edt_registroCorreo);
        edtcontrasena = (EditText) findViewById(R.id.edt_registroContrasena);
        edtcontrasena1 = (EditText) findViewById(R.id.edt_confirmarContrasena);
        sp_genero = (Spinner) findViewById(R.id.sp_genero_registro);

        Adaptador = new ArrayAdapter<String>(MainActivity_Registro.this, android.R.layout.simple_expandable_list_item_1, genero);
        sp_genero.setAdapter(Adaptador);
        sp_genero.setOnItemSelectedListener(eventogenero);

        btnFirma.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MainActivity_Registro.this, Capture_signature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);
            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent goCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(goCamara, CONS);
            }
        });



    registro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    //startActivity(new Intent(MainActivity_Registro.this, MainActivity.class));

                    TareaWSInsercion tarea = new TareaWSInsercion();
                    tarea.execute();

                    //btnFoto.setImageResource(R.drawable.perfil);
                    //Toast.makeText(getApplicationContext(), ""+ email, Toast.LENGTH_SHORT).show();


            }


        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity_Registro.this, MainActivity.class));
            }
        });
    }

    AdapterView.OnItemSelectedListener eventogenero = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position == 0) {
                genero1 = "Femenino";
            }
            if (position == 1) {
                genero1 = "masculino";
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK && requestCode == CONS) {
                Bundle ext = data.getExtras();
                bmpFoto = (Bitmap) ext.get("data");
                btnFoto.setImageBitmap(bmpFoto);
                datoBdFoto = createImageFromBitmap(bmpFoto);
            }
            if (requestCode == SIGNATURE_ACTIVITY && resultCode == Capture_signature.RESULT_OK) {


                Bundle bundle = data.getExtras();
                String status = bundle.getString("status");

                if (status.equalsIgnoreCase("done")) {
                    datoBdFirma = bundle.getString("imagen");

                    try {
                        imagenFirma = BitmapFactory.decodeStream(MainActivity_Registro.this.openFileInput(datoBdFirma));
                        firma.setImageBitmap(imagenFirma);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Toast toast = Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 105, 50);
                    toast.show();
                }
            }
        } catch (Exception e) {

        }

    }

    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage";
        try{
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }
    //Tarea As�ncrona para llamar al WS de consulta en segundo plano
    private class TareaWSInsercion extends AsyncTask<String, Integer, Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;


                final String METHOD_NAME = "nuevoUsuario";
                final String SOAP_ACTION = "http://tempuri.org/nuevoUsuario";
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("documento", documento);
                request.addProperty("foto", foto);
                request.addProperty("nombre", nombre);
                request.addProperty("email", email);
                request.addProperty("contrasena", contrasena);
                request.addProperty("genero", genero1);
                request.addProperty("firma", firma1);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE transporte = new HttpTransportSE(URL);

                try {
                    transporte.call(SOAP_ACTION, envelope);


                    if (validacionCampos){
                    SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
                    String res = resultado_xml.toString();

                    if (!res.equals("1"))
                        resul = false;}
                    else{
                        Toast.makeText(getApplicationContext(), "error validación", Toast.LENGTH_SHORT).show();
                        resul=false;
                    }
                } catch (Exception e) {
                    Log.i("Errores", e.toString());
                    resul = false;
                }



                return resul;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            boolean nn,c,em,doc;


                nombre = edtNombre.getText().toString();
                contrasena = edtcontrasena.getText().toString();
                contrasena1=edtcontrasena1.getText().toString();
                email = edtEmail.getText().toString();
                genero1 = sp_genero.getSelectedItem().toString();
                documento = edtDocumento.getText().toString();
                        //convertir a string foto ** baztrvk
                        String bitfoto = BitMapToString(bmpFoto);
                        foto = bitfoto;
                        //convertir a string firma ** baztrvk
                        String bitfirma = BitMapToString(imagenFirma);
                        firma1 = bitfirma;


            nn=isNombreValid(nombre);
            c=isPassOk(contrasena,contrasena1);
            em=isEmailValid(email);
            doc=isDocValid(documento);

            if (nn && c && em && doc){
                validacionCampos = true;
             }

            }



        protected void onPostExecute(Boolean result) {

            if (result) {
                Toast.makeText(getApplicationContext(), "Insertado Correctamente", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity_Registro.this, MainActivity.class));
            }else{
                Toast.makeText(getApplicationContext(), "Error al insertar", Toast.LENGTH_SHORT).show();}
        }
    }



    //validar email ** baztrvk
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    //convertir bitmap a string ** baztrvk
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    //convertir string a bitmap ** baztrvk
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }


    public static boolean isNombreValid(String nombre) {
        boolean isValid = false;

        String expression = ("^[a-zA-Z ]+$");
        CharSequence inputStr = nombre;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;


    }

    public static boolean isDocValid(String doc) {
        boolean isValid = false;

        String expression = ("[0-9 ]*");
        CharSequence inputStr = doc;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;


    }

    public static boolean isPassOk (String pass1, String pass2){
        boolean isValid = false;

        if (pass1.equals(pass2)){
            isValid = true;
        }

        return isValid;
    }



}





