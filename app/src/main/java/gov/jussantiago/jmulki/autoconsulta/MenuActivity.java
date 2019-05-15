package gov.jussantiago.jmulki.autoconsulta;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove("usuario").commit();
                        editor.remove("clave").commit();
                        editor.remove("matricula").commit();
                        editor.remove("nombre").commit();
                        editor.remove("codigo").commit();
                        editor.remove("casillero").commit();
                        editor.remove("token").commit();
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

}
