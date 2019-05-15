package gov.jussantiago.jmulki.autoconsulta;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jmulki on 17/04/2019.
 */

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder> {
    private List<Notificacion> items;

    public static class NotificacionViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView txtNumero;
        public TextView txtRemitente;
        public TextView txtDatos;
        public TextView txtFuero;


        public NotificacionViewHolder(View v) {
            super(v);
            txtNumero = v.findViewById(R.id.txtNumero);
            txtRemitente = v.findViewById(R.id.txtRemitente);
            txtDatos = v.findViewById(R.id.txtDatos);
            txtFuero = v.findViewById(R.id.txtFuero);
        }
    }

    public NotificacionAdapter(List<Notificacion> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public NotificacionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notificacion_card, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView numero = view.findViewById(R.id.txtNumero);
                Intent intent = new Intent(view.getContext(), CedulaActivity.class);
                intent.putExtra("numero",numero.getText().toString());

                view.getContext().startActivity(intent);
            }
        });

        return new NotificacionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotificacionViewHolder viewHolder, int i) {
        viewHolder.txtNumero.setText(String.valueOf(items.get(i).getNumero()));
        viewHolder.txtRemitente.setText("De: " + items.get(i).getRemitente());
        viewHolder.txtDatos.setText(items.get(i).getFecha() + " - " + "Exp. N° " + items.get(i).getExpediente());
        viewHolder.txtFuero.setText(items.get(i).getFuero());
    }
}
