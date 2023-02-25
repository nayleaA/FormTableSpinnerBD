package com.example.formulario;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class MapaEstado extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_estado);

        Bundle bundle= getIntent().getExtras();//se trae lo que se mando en el extra en el evento al consultar
        String dato= bundle.getString("Estado");//dato rescata el string del identificador(osea el valor del estado seleccionado)
        Toast.makeText(this,dato, Toast.LENGTH_SHORT).show();//imprime nomas para asegurarce
    }
}