package gov.jussantiago.jmulki.autoconsulta.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import gov.jussantiago.jmulki.autoconsulta.classes.ObjetoVolley;
import gov.jussantiago.jmulki.autoconsulta.R;

public class MainActivity extends AppCompatActivity {

    private EditText txtUsuario;
    private EditText txtClave;
    private ProgressBar progressBar;

    private String versionName;
    private Integer versionCode;

    private TextView txtVersion;
    private Button btnLogin;

    private String usuario,clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        usuario = sharedPref.getString("usuario",null);
        clave = sharedPref.getString("clave",null);

            btnLogin = findViewById(R.id.btnLogin);
            btnLogin.setEnabled(false);

            txtVersion = findViewById(R.id.txtVersion);
            try {
                PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                versionName = pInfo.versionName;
                versionCode = pInfo.versionCode;
                txtVersion.setText(getString(R.string.version) + " " + versionName);
            } catch (PackageManager.NameNotFoundException e) {
                versionCode = 1;
            }

            txtUsuario = findViewById(R.id.txtUsuario);
            txtClave = findViewById(R.id.txtClave);
            progressBar = findViewById(R.id.progressBar);

            String numSerie = sharedPref.getString("numSerie", null);
            if (numSerie == null) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("numSerie", UUID.randomUUID().toString().replace("-", "")).commit();
                Log.i("proceso token", "numero de serie creado");
            } else
                Log.i("proceso token", "numero de serie ya existe");

            txtUsuario.setText(usuario);
            txtClave.setText(clave);

            Log.i("ingreso","entra aqui2");
            version();
    }

    private void version() {
        String string = getResources().getString(R.string.API_KEY);
        String hash = new String(Hex.encodeHex(DigestUtils.md5(string)));

        Map<String,String> params = new HashMap<String,String>();
        params.put("version",versionCode.toString());

        ObjetoVolley volley = new ObjetoVolley(
                MainActivity.this,
                getString(R.string.url_version),
                params,
                Request.Method.GET,
                hash
        );

        Log.i("íngreso","entra aqui1");
        volley.execute(new ObjetoVolley.VolleyCallback(){
            @Override
            public void onSuccessResponse(JSONObject jsonObject){
                if (jsonObject!=null) {
                    try {
                        Boolean error = jsonObject.getBoolean("error");
                        String mensaje = jsonObject.getString("message");

                        if (error) {
                            AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(MainActivity.this);

                            builder.setMessage(mensaje)
                                    .setTitle("¡Atención!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                            finish();
                                        }
                                    });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Log.i("íngreso","entra aquiiiii"+usuario);
                            if (usuario==null || clave==null)
                                btnLogin.setEnabled(true);
                            else {
                                ingresar(findViewById(android.R.id.content));
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void ingresar(View v) {
        String u = txtUsuario.getText().toString();
        String c = txtClave.getText().toString();

        if (u.isEmpty() || c.isEmpty()) {
            Toast.makeText(MainActivity.this, "Ingrese todos los datos para identificarse", Toast.LENGTH_SHORT).show();
        }
        else {

            progressBar.setVisibility(View.VISIBLE);

            String string = getResources().getString(R.string.API_KEY);
            String hash = new String(Hex.encodeHex(DigestUtils.md5(string)));

            Map<String, String> params = new HashMap<String, String>();
            params.put("usuario", txtUsuario.getText().toString());
            params.put("clave", txtClave.getText().toString());

            ObjetoVolley volley = new ObjetoVolley(
                    MainActivity.this,
                    getString(R.string.url_login),
                    params,
                    Request.Method.GET,
                    hash
            );

            volley.execute(new ObjetoVolley.VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject objeto) {
                    if (objeto != null) {
                        try {
                            Boolean error = objeto.getBoolean("error");
                            String mensaje = objeto.getString("message");
                            if (error)
                                Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                            else {
                                objeto = objeto.getJSONObject("abogado");
                                String nombre = objeto.getString("nombre");
                                Integer matricula = objeto.getInt("matricula");
                                Integer codigo = objeto.getInt("codigo");
                                Integer casillero = objeto.getInt("casillero");
                                String token = objeto.getString("token");

                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("usuario", txtUsuario.getText().toString());
                                editor.putString("clave", txtClave.getText().toString());
                                editor.putString("nombre", nombre);
                                editor.putInt("matricula", matricula);
                                editor.putInt("codigo", codigo);
                                editor.putInt("casillero", casillero);
                                editor.putString("token", token);
                                editor.commit();

                                //String refreshedToken = sharedPref.getString("refreshedToken", null);
                                //if (refreshedToken == null)
                                actualizarToken();
                                //else
                                //    Log.i("proceso token", "token ya existe");

                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(MainActivity.this, getString(R.string.bienvenido), Toast.LENGTH_SHORT).show();
                                abrirActividad();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, getString(R.string.error) + " (Cód: Main01)", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

        }
    }

    private void actualizarToken() {
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Integer codigo = sharedPref.getInt("codigo",0);
        Integer casillero = sharedPref.getInt("casillero",0);
        String token = sharedPref.getString("token",null);
        String numSerie = sharedPref.getString("numSerie",null);

        if (refreshedToken!=null && !refreshedToken.isEmpty()) {
            Log.d("Firebase", "Actual token " + refreshedToken);

            Map<String, String> params = new HashMap<String, String>();
            params.put("codigo", codigo.toString());
            params.put("casillero", casillero.toString());
            params.put("numserie", numSerie);
            params.put("refreshedToken", refreshedToken);

            ObjetoVolley volley = new ObjetoVolley(
                    MainActivity.this,
                    getString(R.string.url_acttoken),
                    params,
                    Request.Method.POST,
                    token
            );

            volley.execute(new ObjetoVolley.VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject objeto) {
                    if (objeto != null) {
                        try {
                            String mensaje = objeto.getString("message");
                            Log.i("proceso token", mensaje);

                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("refreshedToken",refreshedToken).commit();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, getString(R.string.error) + " (Cód: Token01)", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Log.i("proceso token","sin token");
        }
    }

    private void abrirActividad() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);

        finish();
    }

}

