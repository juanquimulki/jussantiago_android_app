package gov.jussantiago.jmulki.autoconsulta.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gov.jussantiago.jmulki.autoconsulta.classes.ObjetoVolley;
import gov.jussantiago.jmulki.autoconsulta.R;
import gov.jussantiago.jmulki.autoconsulta.classes.Dato;

public class DatosActivity extends AppCompatActivity {

    private Integer codigo;
    private String token;

    private ProgressBar progressBar;
    private ListView lstDatos;

    private ArrayList<Dato> datos = new ArrayList<Dato>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        this.setTitle(getString(R.string.app_name) + " - Datos Personales");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        codigo = sharedPref.getInt("codigo",0);
        token = sharedPref.getString("token",null);

        progressBar = findViewById(R.id.progressBar);
        lstDatos = findViewById(R.id.lstDatos);

        agregarItems();
    }

    private void agregarItems() {
        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<String, String>();
        params.put("codigo", codigo.toString());

        ObjetoVolley volley = new ObjetoVolley(
                DatosActivity.this,
                getString(R.string.url_datos),
                params,
                Request.Method.GET,
                token
        );

        volley.execute(new ObjetoVolley.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject objeto) {
                if (objeto != null) {
                    try {
                        JSONArray arreglo = objeto.getJSONArray("datos");

                        JSONObject info = arreglo.getJSONObject(0);
                        String valor = info.getString("nombre");
                        datos.add(new Dato("Nombre",valor));
                        valor = info.getString("documento");
                        datos.add(new Dato("Documento",valor));
                        valor = info.getString("matricula");
                        datos.add(new Dato("Matrícula",valor));
                        valor = info.getString("casillero");
                        datos.add(new Dato("Casillero",valor));
                        valor = info.getString("procesal");
                        datos.add(new Dato("Domicilio Procesal",valor));
                        valor = info.getString("real");
                        datos.add(new Dato("Domicilio Real",valor));
                        valor = info.getString("correo");
                        datos.add(new Dato("Correo Electrónico",valor));
                        valor = info.getString("correo2");
                        datos.add(new Dato("Correo Alternativo",valor));
                        valor = info.getString("cuil");
                        datos.add(new Dato("CUIL",valor));

                        // Crear un nuevo adaptador
                        DatosActivity.AdaptadorDatos adaptador =
                                new DatosActivity.AdaptadorDatos(DatosActivity.this, datos);
                        lstDatos.setAdapter(adaptador);

                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Toast.makeText(DatosActivity.this, getString(R.string.error) + " (Cód: Datos01)", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(DatosActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    class AdaptadorDatos extends ArrayAdapter<Dato> {

        public AdaptadorDatos(Context context, ArrayList<Dato> datos) {
            super(context, R.layout.listitem_dato, datos);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.listitem_dato, null);

            TextView lblAtributo = item.findViewById(R.id.lblAtributo);
            lblAtributo.setText(datos.get(position).getAtributo());

            TextView lblValor = item.findViewById(R.id.lblValor);
            lblValor.setText(datos.get(position).getValor());

            return(item);
        }
    }

}
