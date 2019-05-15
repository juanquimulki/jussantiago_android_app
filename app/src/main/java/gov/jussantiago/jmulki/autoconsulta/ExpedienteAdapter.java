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

public class ExpedienteAdapter extends RecyclerView.Adapter<ExpedienteAdapter.ExpedienteViewHolder> {
    private List<Expediente> items;

    public static class ExpedienteViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView txtNumero;
        public TextView txtActor;
        public TextView txtDemandado;
        public TextView txtCausa;
        public TextView txtFuero;


        public ExpedienteViewHolder(View v) {
            super(v);
            txtNumero = v.findViewById(R.id.txtNumero);
            txtActor = v.findViewById(R.id.txtRemitente);
            txtDemandado = v.findViewById(R.id.txtDemandado);
            txtCausa = v.findViewById(R.id.txtCausa);
            txtFuero = v.findViewById(R.id.txtFuero);
        }
    }

    public ExpedienteAdapter(List<Expediente> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ExpedienteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.expediente_card, viewGroup, false);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView numero = view.findViewById(R.id.txtNumero);
                TextView fuero = view.findViewById(R.id.txtFuero);
                Intent intent = new Intent(view.getContext(), MovimientosActivity.class);
                intent.putExtra("numero",numero.getText());
                intent.putExtra("fuero",fuero.getText());
                view.getContext().startActivity(intent);
            }
        });

        return new ExpedienteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExpedienteViewHolder viewHolder, int i) {
        viewHolder.txtNumero.setText(String.valueOf(items.get(i).getNumero()));
        viewHolder.txtActor.setText(items.get(i).getActor());
        viewHolder.txtDemandado.setText(items.get(i).getDemandado());
        viewHolder.txtCausa.setText(items.get(i).getCausa());
        viewHolder.txtFuero.setText(items.get(i).getCgodestino());
    }

}
