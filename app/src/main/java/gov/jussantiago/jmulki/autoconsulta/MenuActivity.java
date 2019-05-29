package gov.jussantiago.jmulki.autoconsulta;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {
    TextView txtCreditos;
    ImageView imgLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setTitle(getString(R.string.app_name));
        txtCreditos = findViewById(R.id.txtCreditos);
        txtCreditos.setText(getString(R.string.creditos));

        imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(MenuActivity.this);

                builder.setMessage(getString(R.string.acerca_de))
                        .setTitle("Acerca de...")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    public void expedientes(View v) {
        abrirActividad(ExpedientesActivity.class);
    }

    public void notificaciones(View v) {
        abrirActividad(NotificacionesActivity.class);
    };

    public void datos(View v) {
        abrirActividad(DatosActivity.class);
    }

    private void abrirActividad(Class clase) {
        Intent intent = new Intent(this,clase);
        startActivity(intent);
    }

    public void cerrar(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.cerrar_sesion))
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        eliminarToken();

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove("usuario").commit();
                        editor.remove("clave").commit();
                        editor.remove("matricula").commit();
                        editor.remove("nombre").commit();
                        editor.remove("codigo").commit();
                        editor.remove("casillero").commit();
                        editor.remove("token").commit();
                        editor.remove("refreshedToken").commit();

                        abrirActividad(MainActivity.class);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private void eliminarToken() {
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Integer codigo = sharedPref.getInt("codigo",0);
        String token = sharedPref.getString("token",null);
        String numSerie = sharedPref.getString("numSerie",null);

            Map<String, String> params = new HashMap<String, String>();
            params.put("codigo", codigo.toString());
            params.put("numserie", numSerie);

            ObjetoVolley volley = new ObjetoVolley(
                    MenuActivity.this,
                    getString(R.string.url_elitoken),
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
                        } catch (Exception e) {
                            Toast.makeText(MenuActivity.this, getString(R.string.error) + " (Cód: Token02)", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MenuActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    /*public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent i = new Intent(this, Alumnos.class);
                startActivity(i);
                break;
            case R.id.item2:
                Intent j = new Intent(this, Porcentajes.class);
                startActivity(j);
                break;
            case R.id.item3:
                escribirRegistro();
                //Intent k = new Intent(this, Registro.class);
                //startActivity(j);
                break;
        }
        return true;
    }*/


}
