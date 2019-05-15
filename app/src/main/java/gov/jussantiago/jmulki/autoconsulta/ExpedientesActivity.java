package gov.jussantiago.jmulki.autoconsulta;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
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

public class ExpedientesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private TextView txtAbogado;
    private ProgressBar progressBar;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    private Integer codigo;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expedientes);

        SearchView editsearch = findViewById(R.id.searchView);
        editsearch.setOnQueryTextListener(this);
        editsearch.setQueryHint(getString(R.string.busque));
        editsearch.setBackgroundColor(Color.WHITE);

        this.setTitle(getString(R.string.app_name) + " - Expedientes");

        txtAbogado = findViewById(R.id.txtAbogado);

        // Obtener el Recycler
        recycler = findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String nombre = sharedPref.getString("nombre",null);
        Integer matricula = sharedPref.getInt("matricula",0);
        codigo = sharedPref.getInt("codigo",0);
        token = sharedPref.getString("token",null);

        txtAbogado.setText(nombre + " (MP "+ matricula.toString() +")");

        progressBar = findViewById(R.id.progressBar);
        String query = "";
        agregarItems(query);
    }

    private void agregarItems(final String query) {
        progressBar.setVisibility(View.VISIBLE);
        final List items =  new ArrayList();

        String url;
        if (query.isEmpty())
            url = getString(R.string.url_expedientes);
        else
            url = getString(R.string.url_busqueda);

        Map<String, String> params = new HashMap<String, String>();
        params.put("codigo", codigo.toString());
        params.put("query", query);

        ObjetoVolley volley = new ObjetoVolley(
                ExpedientesActivity.this,
                url,
                params,
                Request.Method.POST,
                token
        );

        volley.execute(new ObjetoVolley.VolleyCallback(){
            @Override
            public void onSuccessResponse(JSONObject jsonObject){
                if (jsonObject!=null) {
                    try {
                        JSONArray arreglo = jsonObject.getJSONArray("expedientes");

                        for (int i=0; i<arreglo.length(); i++) {
                            JSONObject expediente = arreglo.getJSONObject(i);
                            Integer numero = expediente.getInt("numero");
                            String actor = expediente.getString("actor");
                            String demandado = expediente.getString("demandado");
                            String causa = expediente.getString("causa");
                            String cgodestino = expediente.getString("cgodestino");

                            items.add(new Expediente(numero,actor,demandado,causa,cgodestino));
                        }
                        // Crear un nuevo adaptador
                        adapter = new ExpedienteAdapter(items);
                        recycler.setAdapter(adapter);

                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Toast.makeText(ExpedientesActivity.this, getString(R.string.error) + " (Cód: Exp01)", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(ExpedientesActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        recycler.setAdapter(null);
        Toast.makeText(this, getString(R.string.buscando) + " '" + query + "'", Toast.LENGTH_SHORT).show();
        agregarItems(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            recycler.setAdapter(null);
            String query = "";
            agregarItems(query);
        }
        return false;
    }

}
