package com.example.yener.login;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity_Lista_Usuarios extends AppCompatActivity {

    private static final String NOMBRE_CARPETA_APP = "com.example.yener.reportespdf";
    private static final String GENERADOS = "reportesGenerados";

    final String NAMESPACE = "http://tempuri.org/";
    final String URL = "http://bastruk-001-site1.gtempurl.com/ServicioAgenda.asmx";

    ListView list;
    EditText edt_id_usuario;
    ItemAdapter_Listado_Usuarios adapter;
    private ArrayList<Item_List_Usuarios> datos = new ArrayList<Item_List_Usuarios>();

    Item_List_Usuarios[] listaUsuarios;
    Bitmap a, b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_lista_usuarios);

        list = (ListView) findViewById(R.id.lst_usuarios);

        int res = getIntent().getIntExtra("res", 1);

        if (res == 1) {
            TareaWSConsulta tarea = new TareaWSConsulta();
            tarea.execute();
        }

    }

    public class TareaWSConsulta extends AsyncTask<String, Integer, Boolean> {

       // private Item_List_Usuarios[] listaUsuarios;


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            final String METHOD_NAME = "listadoUsuarios";
            final String SOAP_ACTION = "http://tempuri.org/listadoUsuarios";

            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

            SoapSerializationEnvelope envelope =
                    new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);

            HttpTransportSE transporte = new HttpTransportSE(URL);

            try {
                transporte.call(SOAP_ACTION, envelope);

                SoapObject resSoap = (SoapObject) envelope.getResponse();

                listaUsuarios = new Item_List_Usuarios[resSoap.getPropertyCount()];

                for (int i = 0; i < listaUsuarios.length; i++) {
                    SoapObject ic = (SoapObject) resSoap.getProperty(i);

                    Item_List_Usuarios usu = new Item_List_Usuarios();

                    usu.setIdentificacion(ic.getProperty(0).toString());
                    String ima = ic.getProperty(1).toString();
                    a = StringToBitMap(ima);
                    usu.setImagen(a);
                    usu.setNombre(ic.getProperty(2).toString());
                    usu.setCorreo(ic.getProperty(3).toString());
                    usu.setContrasena(ic.getProperty(4).toString());
                    usu.setGenero(ic.getProperty(5).toString());

                    String fir = ic.getProperty(6).toString();
                    b = StringToBitMap(fir);
                    usu.setFirma(b);


                    listaUsuarios[i] = usu;
                    datos.add(usu);
                }
            } catch (Exception e) {
                Log.i("Errores", e.toString());
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {


            if (result) {
                //Rellenamos la lista de canciones

                for (int i = 0; i < listaUsuarios.length; i++) {
                }
                adapter = new ItemAdapter_Listado_Usuarios(getApplicationContext(), R.layout.activity_item_list_usuarios, datos);
                list.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public void generarPDFOnclic (View view){

        Document document = new Document (PageSize.LETTER);
        String NOMBRE_ARCHIVO = "Estudiantes.pdf";
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        File pdfDir = new File(tarjetaSD + File.separator + NOMBRE_CARPETA_APP);

        if (!pdfDir.exists()){
            pdfDir.mkdir();
        }
        File pdfSubDir = new File(pdfDir.getPath() + File.separator + GENERADOS);
        if (!pdfSubDir.exists()){
            pdfSubDir.mkdir();
        }

        String nombre_completo = Environment.getExternalStorageDirectory()
                + File.separator + NOMBRE_CARPETA_APP +
                File.separator + GENERADOS + File.separator + NOMBRE_ARCHIVO;
        File outputfile = new File(nombre_completo);
        if (!outputfile.exists()){
            outputfile.delete();
        }
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document , new FileOutputStream(nombre_completo));

            document.open();
            document.addAuthor("REPORTE DE EJEMPLO");
            document.addCreator("PDF");
            document.addSubject("GENRACION DE REPORTES PDF");
            document.addCreationDate();
            document.addTitle("ESTUDIANTES POR CURSO");

            StringBuffer aux = new StringBuffer();

            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

            aux .append("<html><head></head><body><h1>ESTUDIANTES INSCRITOS</h1><p>Estos son los Estudiantes Inscritos en Primero</p>");

            aux.append("<table border=1 cellpadding=4 cellspacing=0 >");
            aux.append("<tr>\n" +
                    "<td><strong>identificacion</strong></td>\n" +
                    "<td><strong>Nombres</strong></td>\n" +
                    "<td><strong>Email</strong></td>\n" +
                    "<td><strong>Contraseña</strong></td>\n" +
                    "<td><strong>Genero</strong></td>\n" +
                    "<td><strong>Firma</strong></td>\n" +
                    "</tr>");

            //BitmapFactory.decodeResource(getResources(), R.menu.menureporte);

            for (int i = 0; i < listaUsuarios.length; i++) {


                aux.append("<tr>\n" +
                        "<td>"+listaUsuarios[i].getIdentificacion()+"</td>\n" +
                        "<td>"+listaUsuarios[i].getNombre()+"</td>\n" +
                        "<td>"+listaUsuarios[i].getCorreo()+"</td>\n" +
                        "<td>"+listaUsuarios[i].getContrasena()+"</td>\n" +
                        "<td>"+listaUsuarios[i].getGenero()+"</td>\n" +
                        "<td><img"+listaUsuarios[i].getFirma()+ "\" width=\"100\" heigh=\"100\"/></td>\n" +
                        "</tr>");
            }


            aux.append("</table>");
            aux.append("<br></br>");
            aux.append("<br></br>");
            aux.append("<br></br>");
            aux.append("<br></br>");
            aux.append("<br></br>");


            aux.append("<p>Pruebas2016 - Todos los Derechos Reservados</p>");
            aux.append("</body></html>");


            worker.parseXHtml(pdfWriter, document, new StringReader(aux.toString()));
            document.close();

            Toast.makeText(this, "REPORTE GENERADO", Toast.LENGTH_SHORT).show();
            muestraPDF(nombre_completo, this);

        }catch (DocumentException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    public void muestraPDF (String archivo, Context context){
        Toast.makeText(this, "Leyendo El Archivo", Toast.LENGTH_SHORT).show();
        File file = new File(archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, "No tiene App para abrir este Archivo", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menureporte, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View mostrar = null;
        int id = item.getItemId();

        if (id == R.id.menu_reporte) {
            generarPDFOnclic(mostrar);
        }
        return true;
    }

    private String guardarFirma (Context context, String nombre, Bitmap firma){
        ContextWrapper cw = new ContextWrapper(context);
        File dirFir = cw.getDir("Firmas", Context.MODE_PRIVATE);
        File myPath = new  File(dirFir,nombre + ".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            firma.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myPath.getAbsolutePath();
    }
    //convertir bitmap a string ** baztrvk
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}
