package gov.jussantiago.jmulki.autoconsulta;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CedulaActivity extends AppCompatActivity {

    private Integer numero;
    private Integer codigo;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cedula);

        this.setTitle(getString(R.string.app_name)+" - Cédula");

        numero = Integer.parseInt(getIntent().getExtras().getString("numero"));

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        codigo = sharedPref.getInt("codigo",0);
        token = sharedPref.getString("token",null);

        buscarArchivo();
    }

    private void verArchivo(String archivo) {
        String pdf = getString(R.string.url_cedulas) + archivo;
        WebView mWebView=new WebView(CedulaActivity.this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(getString(R.string.url_gview) + "&url="+pdf);
        setContentView(mWebView);
    }

    private void buscarArchivo() {
        Toast.makeText(this, getString(R.string.aguarde), Toast.LENGTH_SHORT).show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("numero", numero.toString());
        params.put("codigo", codigo.toString());

        ObjetoVolley volley = new ObjetoVolley(
                CedulaActivity.this,
                getString(R.string.url_cedula),
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
                            Toast.makeText(CedulaActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                        } else {
                            String archivo = objeto.getString("archivo");
                            verArchivo(archivo);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(CedulaActivity.this, getString(R.string.error)+" (Cód: Ced01)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CedulaActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
