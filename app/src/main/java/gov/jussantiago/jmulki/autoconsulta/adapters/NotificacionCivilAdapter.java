package gov.jussantiago.jmulki.autoconsulta.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gov.jussantiago.jmulki.autoconsulta.R;
import gov.jussantiago.jmulki.autoconsulta.activities.CedulaActivity;
import gov.jussantiago.jmulki.autoconsulta.activities.CedulaCivilActivity;
import gov.jussantiago.jmulki.autoconsulta.classes.NotificacionCivil;

/**
 * Created by jmulki on 17/04/2019.
 */

public class NotificacionCivilAdapter extends RecyclerView.Adapter<NotificacionCivilAdapter.NotificacionCivilViewHolder> {
    private List<NotificacionCivil> items;

    public static class NotificacionCivilViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView txtNumero;
        public TextView txtCaratula;
        public TextView txtFecha;
        public TextView txtFuero;
        public TextView txtObservaciones;


        public NotificacionCivilViewHolder(View v) {
            super(v);
            txtNumero = v.findViewById(R.id.txtNumero);
            txtCaratula = v.findViewById(R.id.txtCaratula);
            txtFecha = v.findViewById(R.id.txtFecha);
            txtFuero = v.findViewById(R.id.txtFuero);
            txtObservaciones = v.findViewById(R.id.txtObservaciones);
        }
    }

    public NotificacionCivilAdapter(List<NotificacionCivil> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public NotificacionCivilViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notificacioncivil_card, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView numero = view.findViewById(R.id.txtNumero);
                TextView observaciones = view.findViewById(R.id.txtObservaciones);
                Intent intent = new Intent(view.getContext(), CedulaCivilActivity.class);
                intent.putExtra("nroexpediente",numero.getText().toString());
                intent.putExtra("observaciones",observaciones.getText().toString());

                view.getContext().startActivity(intent);
            }
        });

        return new NotificacionCivilViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotificacionCivilViewHolder viewHolder, int i) {
        viewHolder.txtNumero.setText(String.valueOf(items.get(i).getNumero()));
        viewHolder.txtCaratula.setText(items.get(i).getCaratula());
        viewHolder.txtFecha.setText("Despacho: " + items.get(i).getFecha());
        viewHolder.txtFuero.setText(items.get(i).getCgodestino());
        viewHolder.txtObservaciones.setText(items.get(i).getObservaciones());
    }
}
