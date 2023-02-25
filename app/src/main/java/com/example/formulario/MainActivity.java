package com.example.formulario;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    AsyncHttpClient cliente;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    String httpURI="https://proyectoapejal.000webhostapp.com/agenda/consultaEdo.php";
    Spinner cboestados;
    String e;
    TableLayout tbtusarios;

    MaterialButton btnregresa,btnconsulta;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        cliente=new AsyncHttpClient();
        cboestados=(Spinner) findViewById(R.id.cboestado);
        llenarspinner();

        requestQueue= Volley.newRequestQueue(MainActivity.this);
        progressDialog=new ProgressDialog(MainActivity.this);

        btnregresa= (MaterialButton) findViewById(R.id.btnregresar);
        btnconsulta= (MaterialButton) findViewById(R.id.btnconsultar);

        btnregresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent regresa= new Intent(SpinnerEstado.this,Mapa.class);
                //startActivity(mapa);
            }
        });

        btnconsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conxe= new Intent(MainActivity.this,MapaEstado.class);
                conxe.putExtra("Estado",e);//envia el valor del select a el mapa
                startActivity(conxe);
            }
        });

    }

    private void CargarTabla() {

        //tabla
        tbtusarios=findViewById(R.id.tablacxe);
        tbtusarios.removeAllViews();//remueve columnas


        //------------
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, httpURI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try{
                    JSONArray result=new JSONArray(response);

                    for (int i = 0; i < result.length(); ) {

                        JSONObject jsonObject = result.getJSONObject(i);

                        View registro= LayoutInflater.from(getApplicationContext()).inflate(R.layout.table_row,null,false);

                        TextView nombre=registro.findViewById(R.id.columnaname);
                        TextView email=registro.findViewById(R.id.coemail);

                        //nombre.append(jsonObject.getString("NombreCentro"));
                        //email.append(jsonObject.getString("Domicilio"));

                        String name=jsonObject.getString("NombreCentro");
                        String emails=jsonObject.getString("Domicilio");
                        nombre.setText(name);
                        email.setText(emails);

                        tbtusarios.addView(registro);
                        i++;

                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                //Mostrar el error de Volley exacto a través de la librería
                Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String,String> getParams(){
                Map<String, String> parametros=new HashMap<>();

                parametros.put("Estado",e);
                return parametros;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void llenarspinner(){
        String url="https://proyectoapejal.000webhostapp.com/agenda/cboestados.php";
        cliente.post(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200){
                    cargarspinner(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void cargarspinner(String respuesta){
        ArrayList<estados> lista= new ArrayList<estados>();
        try{
            JSONArray jsonArreglo=new JSONArray(respuesta);
            for(int i=0; i<jsonArreglo.length(); i++){
                estados es=new estados();
                es.setHorarioDiasLaborales(jsonArreglo.getJSONObject(i).getString("Estado"));
                lista.add(es);
            }
            ArrayAdapter<CharSequence> a=new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,lista);
            cboestados.setAdapter(a);
            cboestados.setOnItemSelectedListener(this);

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        e = parent.getSelectedItem().toString();
        CargarTabla();

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }


}