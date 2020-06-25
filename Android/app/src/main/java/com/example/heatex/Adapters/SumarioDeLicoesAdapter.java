package com.example.heatex.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.renanmendes.eqat.R;

import java.util.List;

public class SumarioDeLicoesAdapter extends RecyclerView.Adapter<SumarioDeLicoesAdapter.MyViewHolder>{


    Context context;
    List<String> listaDeTextos;

    public SumarioDeLicoesAdapter(Context context, List<String> listaDeTextos) {
        this.context = context;
        this.listaDeTextos = listaDeTextos;
    }


    @Override
    public SumarioDeLicoesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_do_sumario_de_licoes_adapter, parent, false);

        return new SumarioDeLicoesAdapter.MyViewHolder(itemLista);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView numero, secaoDoEstudo;

        public MyViewHolder(View itemView) {
            super(itemView);

            numero = itemView.findViewById(R.id.numeroTextView);
            secaoDoEstudo = itemView.findViewById(R.id.secaoDoEstudoTextView);
        }
    }


    @Override
    public void onBindViewHolder(final SumarioDeLicoesAdapter.MyViewHolder holder, int i) {
        //Preenche os dados da view atual com os da posição:
        int numLicao = i+1;
        holder.numero.setText(""+numLicao);
        holder.secaoDoEstudo.setText(listaDeTextos.get(i));
    }


    @Override
    public int getItemCount() {
        return listaDeTextos.size();
    }



}



