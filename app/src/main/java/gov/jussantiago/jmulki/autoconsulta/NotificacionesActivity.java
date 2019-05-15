package gov.jussantiago.jmulki.autoconsulta;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificacionesActivity extends AppCompatActivity {

    TextView txtAbogado;
    ProgressBar progressBar;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    private Integer casillero;
    private Integer codigo;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificaciones);

        this.setTitle(getString(R.string.app_name) + " - Notificaciones");

        txtAbogado = findViewById(R.id.txtAbogado);

        // Obtener el Recycler
        recycler = findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String nombre = sharedPref.getString("nombre",null);
        casillero = sharedPref.getInt("casillero",0);
        codigo = sharedPref.getInt("codigo",0);
        token = sharedPref.getString("token",null);

        txtAbogado.setText(nombre + " (Cas. N° "+ casillero.toString() +")");

        progressBar = findViewById(R.id.progressBar);
        agregarItems();
    }

    private void agregarItems() {
        progressBar.setVisibility(View.VISIBLE);
        final List items =  new ArrayList();

        Map<String, String> params = new HashMap<String, String>();
        params.put("codigo", codigo.toString());
        params.put("casillero", casillero.toString());

        ObjetoVolley volley = new ObjetoVolley(
                NotificacionesActivity.this,
                getString(R.string.url_notificaciones),
                params,
                Request.Method.POST,
                token
        );

        volley.execute(new ObjetoVolley.VolleyCallback(){
            @Override
            public void onSuccessResponse(JSONObject objeto){
                if (objeto!=null) {
                    try {
                        JSONArray arreglo = objeto.getJSONArray("notificaciones");

                        for (int i=0; i<arreglo.length(); i++) {
                            JSONObject notificacion = arreglo.getJSONObject(i);
                            Integer numero = notificacion.getInt("numero");
                            String remitente = notificacion.getString("remitente");
                            String expediente = notificacion.getString("expediente");
                            String fuero = notificacion.getString("fuero");
                            String fecha = notificacion.getString("fecha");
                            String archivo = notificacion.getString("archivo");

                            items.add(new Notificacion(numero,remitente,expediente,fuero,fecha,archivo));
                        }
                        // Crear un nuevo adaptador
                        adapter = new NotificacionAdapter(items);
                        recycler.setAdapter(adapter);

                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Toast.makeText(NotificacionesActivity.this, getString(R.string.error) + " (Cód: Not01)", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(NotificacionesActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}
