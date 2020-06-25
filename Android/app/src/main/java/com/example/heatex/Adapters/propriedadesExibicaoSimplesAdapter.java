package com.example.heatex.Adapters;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.PropriedadeBasica;
import com.example.heatex.Classes.PropriedadeBasicaExibicao;
import com.renanmendes.eqat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class propriedadesExibicaoSimplesAdapter extends RecyclerView.Adapter<propriedadesExibicaoSimplesAdapter.MyViewHolder> {

    Context context;
    String nome, sigla, valor, unidade;
    List<PropriedadeBasicaExibicao> listaProps;


    public propriedadesExibicaoSimplesAdapter(Context context, List<PropriedadeBasicaExibicao> listaProps) {
        this.context = context;
        this.listaProps = listaProps;

    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exibir_propriedade_adapter, parent, false);

        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        //holder.sigla.setText( sigla );
        //holder.nomePropriedade.setText( nome );
        //holder.unidade.setText( unidade );
        //holder.valorzineo.setText(valor);
        //Obtendo os valores:
        String textoSigla, textoNome, textoUnidade, textoValorzineo;

        textoSigla = listaProps.get(holder.getAdapterPosition()).getSiglaPropriedade();
        textoNome = listaProps.get(holder.getAdapterPosition()).getNomePropriedade();
        textoUnidade = listaProps.get(holder.getAdapterPosition()).getUnidadeSI();
        textoValorzineo = listaProps.get(holder.getAdapterPosition()).getValor();

        holder.nomePropriedade.setText(textoNome);
        //tenta converter o valorzineio para double e, e der, pega a forma em HTML
        android.text.Spanned valorHtml = null;
        try{
            double valornumerico = Double.parseDouble(""+textoValorzineo); //pra dar erro se tiver texto com os números
            String txt="";
            if(valornumerico>=1000){
                txt = ArmazenamentoDeDados.formatarValorNumerico(valornumerico, "e");
                valorHtml = ArmazenamentoDeDados.transformarEmNotacaoCientifica(txt);
            }else txt = ArmazenamentoDeDados.formatarValorNumerico(valornumerico, "f");


            if(valorHtml!=null){
                holder.valorzineo.setText(valorHtml);
            }else holder.valorzineo.setText(txt);

        }catch(Exception e){
            holder.valorzineo.setText(textoValorzineo);
        }


        if(textoSigla=="" || textoSigla == null){ ((ViewGroup) holder.sigla.getParent()).removeView(holder.sigla); ((ViewGroup) holder.textoIgual.getParent()).removeView(holder.textoIgual); }
        else{holder.sigla.setText(textoSigla); }

        if(textoUnidade =="" || textoUnidade == null){ ((ViewGroup) holder.unidade.getParent()).removeView(holder.unidade); }
        else{holder.unidade.setText(textoUnidade);}
    }


    @Override
    public int getItemCount() {
        return listaProps.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sigla, nomePropriedade, unidade, valorzineo, textoIgual;


        public MyViewHolder(View itemView) {
            super(itemView);

            sigla = itemView.findViewById(R.id.propSligaaTextView);
            nomePropriedade = itemView.findViewById(R.id.propNameTextView);
            unidade = itemView.findViewById(R.id.unidadeeeeTextView);
            valorzineo = itemView.findViewById(R.id.valorTextView);
            textoIgual = itemView.findViewById(R.id.IgualSiglaTextView);

            /*
            textoSigla = listaProps.get(getAdapterPosition()+1).getSiglaPropriedade();
            textoNome = listaProps.get(getAdapterPosition()+1).getNomePropriedade();
            textoUnidade = listaProps.get(getAdapterPosition()+1).getUnidadeSI();
            textoValorzineo = listaProps.get(getAdapterPosition()+1).getValor();

             */

            /*
            nomePropriedade.setText(textoNome);
            nomePropriedade.setText("aaaaaaaaaaaAAAAAAAAAAAAAAAA");
            valorzineo.setText(textoValorzineo);

            if(textoSigla=="" || textoSigla == null){ ((ViewGroup) sigla.getParent()).removeView(sigla); ((ViewGroup) sigla.getParent()).removeView(textoIgual); }
            else{sigla.setText(textoSigla); }

            if(textoUnidade =="" || textoUnidade == null){ ((ViewGroup) unidade.getParent()).removeView(unidade); }
            else{unidade.setText(textoUnidade);}
            */

        }
    }


}
