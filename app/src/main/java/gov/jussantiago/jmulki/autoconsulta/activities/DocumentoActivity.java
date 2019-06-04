package gov.jussantiago.jmulki.autoconsulta.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import gov.jussantiago.jmulki.autoconsulta.classes.ObjetoVolley;
import gov.jussantiago.jmulki.autoconsulta.R;

public class DocumentoActivity extends AppCompatActivity {

    private float size = 18;
    private float size_max = 70;
    private float size_min = 12;

    private TextView txtDocumento;
    private Integer idvar;
    private String cgodestino;
    private ProgressBar progressBar;

    Integer codigo;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documento);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

        this.setTitle(getString(R.string.app_name) + " - Documento");

        txtDocumento = findViewById(R.id.txtDocumento);
        idvar = getIntent().getExtras().getInt("idvar");
        cgodestino = getIntent().getExtras().getString("cgodestino");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        codigo = sharedPref.getInt("codigo",0);
        token = sharedPref.getString("token",null);

        progressBar = findViewById(R.id.progressBar);
        verDocumento();
    }

    private void verDocumento() {
        progressBar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<String, String>();
        params.put("codigo", codigo.toString());
        params.put("idvar", idvar.toString());
        params.put("cgodestino", cgodestino);

        ObjetoVolley volley = new ObjetoVolley(
                DocumentoActivity.this,
                getString(R.string.url_texto),
                params,
                Request.Method.GET,
                token
        );

        volley.execute(new ObjetoVolley.VolleyCallback(){
            @Override
            public void onSuccessResponse(JSONObject jsonObject){
                if (jsonObject!=null) {
                    try {
                        String texto = jsonObject.getString("message");
                        txtDocumento.setText(Html.fromHtml(texto));

                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Toast.makeText(DocumentoActivity.this, getString(R.string.error) + " (Cód: Doc01)", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(DocumentoActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void letraMas(View v) {
        if (size<size_max) {
            size = size + 2;
            txtDocumento.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
        }
    }

    public void letraMenos(View v) {
        if (size>size_min) {
            size = size - 2;
            txtDocumento.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

}
