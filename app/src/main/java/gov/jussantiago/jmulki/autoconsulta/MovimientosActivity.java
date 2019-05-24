package gov.jussantiago.jmulki.autoconsulta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MovimientosActivity extends AppCompatActivity {

    private ListView lstOpciones;
    private ArrayList<Movimiento> datos = new ArrayList<Movimiento>();
    private Integer numero;
    private String fuero;
    private ProgressBar progressBar;

    private TableLayout tblCaratula;
    private Button btnCaratula;
    private Boolean onCaratula = true;
    private TextView txtNumero;
    private TextView txtActor;
    private TextView txtDemandado;
    private TextView txtCausa;
    private TextView txtFecha;
    private TextView txtFuero;

    private Integer codigo;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimientos);

        this.setTitle(getString(R.string.app_name) + " - Movimientos");

        tblCaratula = findViewById(R.id.tblCaratula);
        btnCaratula = findViewById(R.id.btnCaratula);
        numero = Integer.parseInt(getIntent().getExtras().getString("numero"));
        fuero = getIntent().getExtras().getString("fuero");
        Log.i("Message Data: ",numero.toString());

        txtNumero = findViewById(R.id.txtNumero);
        txtActor = findViewById(R.id.txtRemitente);
        txtDemandado = findViewById(R.id.txtDemandado);
        txtCausa = findViewById(R.id.txtCausa);
        txtFecha = findViewById(R.id.txtFecha);
        txtFuero = findViewById(R.id.txtFuero);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        codigo = sharedPref.getInt("codigo",0);
        token = sharedPref.getString("token",null);

        progressBar = findViewById(R.id.progressBar);
        verCaratula();

        lstOpciones = findViewById(R.id.lstMovimientos);
        agregarItems();
    }

    private void verCaratula() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("codigo", codigo.toString());
        params.put("numero", numero.toString());

        ObjetoVolley volley = new ObjetoVolley(
                MovimientosActivity.this,
                getString(R.string.url_caratula),
                params,
                Request.Method.POST,
                token
        );

        volley.execute(new ObjetoVolley.VolleyCallback(){
            @Override
            public void onSuccessResponse(JSONObject objeto){
                if (objeto!=null) {
                    try {
                        JSONArray arreglo = objeto.getJSONArray("expediente");

                        JSONObject expediente = arreglo.getJSONObject(0);
                        Integer numero = expediente.getInt("numero");
                        String actor = expediente.getString("actor");
                        String demandado = expediente.getString("demandado");
                        String causa = expediente.getString("causa");
                        String fecha = expediente.getString("fecha");
                        String fuero = expediente.getString("fuero");

                        txtNumero.setText(numero.toString());
                        txtActor.setText(actor);
                        txtDemandado.setText(demandado);
                        txtCausa.setText(causa);
                        txtFecha.setText(fecha);
                        txtFuero.setText(fuero);
                    } catch (JSONException e) {
                        Toast.makeText(MovimientosActivity.this, getString(R.string.error) + " (Cód: Mov01)", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MovimientosActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void agregarItems() {
        progressBar.setVisibility(View.VISIBLE);
        final ArrayList<Movimiento> items = new ArrayList<>();

        Map<String, String> params = new HashMap<String, String>();
        params.put("codigo", codigo.toString());
        params.put("numero", numero.toString());
        params.put("fuero", fuero);

        ObjetoVolley volley = new ObjetoVolley(
                MovimientosActivity.this,
                getString(R.string.url_movimientos),
                params,
                Request.Method.POST,
                token
        );

        volley.execute(new ObjetoVolley.VolleyCallback(){
            @Override
            public void onSuccessResponse(JSONObject objeto){
                if (objeto!=null) {
                    try {
                        JSONArray arreglo = objeto.getJSONArray("movimientos");

                        for (int i=0; i<arreglo.length(); i++) {
                            JSONObject movimiento = arreglo.getJSONObject(i);
                            String fecha = movimiento.getString("fecha");
                            String estado = movimiento.getString("estado");
                            String observaciones = movimiento.getString("observaciones");
                            Integer cgoestado = movimiento.getInt("cgoestado");
                            Integer idvar = movimiento.getInt("idvar");
                            Integer anio = Integer.parseInt(fecha.substring(6,10));

                            datos.add(new Movimiento(numero,fecha,estado,observaciones,cgoestado,idvar,fuero,anio));
                        }

                        // Crear un nuevo adaptador
                        AdaptadorMovimientos adaptador =
                                new AdaptadorMovimientos(MovimientosActivity.this, datos);
                        lstOpciones.setAdapter(adaptador);

                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Toast.makeText(MovimientosActivity.this, getString(R.string.error) + " (Cód: Mov02)", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(MovimientosActivity.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void estadoCaratula(View v) {
        if (onCaratula) {
            tblCaratula.setVisibility(View.GONE);
            btnCaratula.setText("Mostrar Carátula");
            onCaratula = false;
        }
        else {
            tblCaratula.setVisibility(View.VISIBLE);
            btnCaratula.setText("Ocultar Carátula");
            onCaratula = true;
        }
    }

    class AdaptadorMovimientos extends ArrayAdapter<Movimiento> {

        public AdaptadorMovimientos(Context context, ArrayList<Movimiento> datos) {
            super(context, R.layout.listitem_movimiento, datos);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.listitem_movimiento, null);

            TextView lblFecha = (TextView)item.findViewById(R.id.lblFecha);
            lblFecha.setText(datos.get(position).getFecha());

            TextView lblEstado = (TextView)item.findViewById(R.id.lblEstado);
            lblEstado.setText(datos.get(position).getEstado());

            TextView lblObservaciones = (TextView)item.findViewById(R.id.lblObservaciones);
            lblObservaciones.setText(datos.get(position).getObservaciones());

            if ((datos.get(position).getCgoestado()==26) || (datos.get(position).getCgoestado()==118) || (datos.get(position).getCgoestado()==232)) {
                lblObservaciones.setPaintFlags(lblObservaciones.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                lblObservaciones.setTextColor(Color.BLUE);
            }

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((datos.get(position).getCgoestado()==26) || (datos.get(position).getCgoestado()==118)) && (datos.get(position).getIdvar()!=0))
                    {
                        Integer actual = Calendar.getInstance().get(Calendar.YEAR);
                        if (datos.get(position).getAnio()>=actual) {
                            Intent intent = new Intent(view.getContext(), DocumentoActivity.class);
                            intent.putExtra("idvar", datos.get(position).getIdvar());
                            intent.putExtra("cgodestino", datos.get(position).getCgodestino());
                            view.getContext().startActivity(intent);
                        }
                        else {
                            Toast.makeText(MovimientosActivity.this, getString(R.string.movimientos_anio), Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (datos.get(position).getCgoestado()==232) {
                        Log.i("NroExpediente",datos.get(position).getNroexpediente().toString());
                        Intent intent = new Intent(view.getContext(), CedulaCivilActivity.class);
                        intent.putExtra("nroexpediente", datos.get(position).getNroexpediente().toString());
                        intent.putExtra("observaciones", datos.get(position).getObservaciones());
                        view.getContext().startActivity(intent);
                    }
                    else {
                        Toast.makeText(MovimientosActivity.this, getString(R.string.movimientos_adjunto), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            return(item);
        }
    }

}


