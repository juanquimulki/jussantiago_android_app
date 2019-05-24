package gov.jussantiago.jmulki.autoconsulta;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CedulaCivilActivity extends AppCompatActivity {

    private Integer nroexpediente;
    private String observaciones;
    private Integer codigo;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cedula_civil);

        this.setTitle(getString(R.string.app_name)+" - Cédula Civil");

        nroexpediente = Integer.parseInt(getIntent().getExtras().getString("nroexpediente"));
        observaciones = getIntent().getExtras().getString("observaciones");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        codigo = sharedPref.getInt("codigo",0);
        token = sharedPref.getString("token",null);

        buscarArchivo();
    }

    private void verArchivo(String archivo) {
        String pdf = getString(R.string.url_firmados) + archivo;
        Log.i("ruta de archivo",pdf);
        WebView mWebView=new WebView(CedulaCivilActivity.this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(getString(R.string.url_gview) + "&url="+pdf);
        setContentView(mWebView);
    }

    private void buscarArchivo() {
        Toast.makeText(this, getString(R.string.aguarde), Toast.LENGTH_SHORT).show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("codigo", codigo.toString());
        params.put("nroexpediente", nroexpediente.toString());
        params.put("observaciones", observaciones.replace(" ","%20"));

        ObjetoVolley volley = new ObjetoVolley(
                CedulaCivilActivity.this,
                getString(R.string.url_cedula_civil),
                params,
                Request.Method.GET,
                token
        );

        volley.execute(new ObjetoVolley.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject objeto) {
                if (objeto != null) {
                    try {
                        Boolean error = objeto.getBoolean("error");

                        if (error) {
                            String mensaje = objeto.getString("message");
                            Toast.makeText(CedulaCivilActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                        } else {
                            String archivo = objeto.getString("archivo");
                            verArchivo(archivo);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(CedulaCivilActivity.this, getString(R.string.error)+" (Cód: Civ01)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CedulaCivilActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
