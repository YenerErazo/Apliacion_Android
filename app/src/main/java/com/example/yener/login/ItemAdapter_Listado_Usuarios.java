package com.example.yener.login;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LIDA on 08/09/2016.
 */
public class ItemAdapter_Listado_Usuarios  extends ArrayAdapter<Item_List_Usuarios> {
    Context context;

    public ItemAdapter_Listado_Usuarios(Context context, int textViewResourceId, List<Item_List_Usuarios> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }
    public View getView(int position, View converView, ViewGroup parent){
        View v = converView;
        ImageView iv;
        TextView tV;

        if (v == null){
            v = View.inflate(context, R.layout.activity_item_list_usuarios, null);
        }

        Item_List_Usuarios item = getItem(position);

        iv =(ImageView) v.findViewById(R.id.img_user);
        iv.setImageBitmap(item.getImagen());

        tV = (TextView) v.findViewById(R.id.txt_nombre_usuario);
        tV.setText(item.getNombre());

        tV = (TextView) v.findViewById(R.id.txt_identificacion);
        tV.setText(item.getIdentificacion());

        tV = (TextView) v.findViewById(R.id.txt_correo_usuario);
        tV.setText(item.getCorreo());

        tV = (TextView) v.findViewById(R.id.txt_genero_usuario);
        tV.setText(item.getGenero());

        tV = (TextView) v.findViewById(R.id.txt_contrasena_usuario);
        tV.setText(item.getContrasena());

        iv =(ImageView) v.findViewById(R.id.img_firma);
        iv.setImageBitmap(item.getFirma());



        return  v;

    }

}
