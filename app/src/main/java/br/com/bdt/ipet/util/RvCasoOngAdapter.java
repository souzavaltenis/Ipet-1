package br.com.bdt.ipet.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.com.bdt.ipet.R;
import br.com.bdt.ipet.data.model.Caso;
import br.com.bdt.ipet.data.model.CasoComDoacao;
import br.com.bdt.ipet.view.CriarCasoActivity;

import java.util.ArrayList;
import java.util.List;

public class RvCasoOngAdapter extends RecyclerView.Adapter<RvCasoOngAdapter.CasoViewHolder> {

    private final Context context;
    private List<CasoComDoacao> casosOng;
    private final CasoOnClickListener onClickListener;

    public interface CasoOnClickListener {
        void onConfirmDelete(int position);
    }

    public RvCasoOngAdapter(Context context, List<CasoComDoacao> casosOng, CasoOnClickListener onClickListener) {
        this.context = context;
        this.casosOng = casosOng != null ? casosOng : new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public CasoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_card_caso,
                viewGroup, false);
        return new CasoViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return casosOng != null ? casosOng.size() : 0;
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    @Override
    public void onBindViewHolder(final CasoViewHolder holder, final int position) {

        Caso caso = casosOng.get(position).getCaso();

        holder.tvTitulo.setText(caso.getTitulo());
        holder.tvDescricao.setText(caso.getDescricao());
        holder.tvAnimalData.setText(caso.getNomeAnimal() + " (" + caso.getEspecie() + ")");
        holder.tvValor.setText(GeralUtils.formatarValor(caso.getValor()));
        holder.tvValorArrecadado.setText(GeralUtils.formatarValor(caso.getArrecadado()));
        holder.tvOptionsCaso.setOnClickListener(view -> {

            PopupMenu popup = new PopupMenu(context, holder.tvOptionsCaso);
            popup.inflate(R.menu.caso_options);

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menuItemEditar: editarCaso(caso); return true;
                    case R.id.menuItemApagar: apagarCaso(position, caso); return true;
                    default: return false;
                }
            });

            popup.show();
        });

    }

    public void editarCaso(Caso caso){
        Intent intent = new Intent(context, CriarCasoActivity.class);
        intent.putExtra("casoEdit", (Parcelable) caso);
        context.startActivity(intent);
    }

    public void apagarCaso(int position, Caso caso){

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {

            if(which == DialogInterface.BUTTON_POSITIVE){
                onClickListener.onConfirmDelete(position);
            }

        };

        GeralUtils.builderDialog(
                context,
                R.drawable.ic_alerta,
                "Muita atenção agora",
                caso.getOng().getNome() + ", realmente deseja excluir o caso '" + caso.getTitulo() + "' ?",
                "Sim", dialogClickListener,
                "Não, cliquei errado", dialogClickListener
        ).show();
    }

    public static class CasoViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo;
        TextView tvDescricao;
        TextView tvValor;
        TextView tvValorArrecadado;
        TextView tvOptionsCaso;
        TextView tvAnimalData;
        View view;

        public CasoViewHolder(View view) {
            super(view);
            this.view = view;
            tvTitulo = view.findViewById(R.id.tvTitleData);
            tvDescricao = view.findViewById(R.id.tvDescricaoData);
            tvValor = view.findViewById(R.id.tvValorData);
            tvValorArrecadado = view.findViewById(R.id.tvValorArrecadado);
            tvOptionsCaso = view.findViewById(R.id.tvOptionsCaso);
            tvAnimalData = view.findViewById(R.id.tvAnimalData);
        }
    }
}