package com.example.heatex.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.heatex.Activities.exibirDadosProjetoUnicoActivity;
import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.ProjetoDeBase;
import com.example.heatex.Classes.TrocadorBitubularHeatex;
import com.example.heatex.R;

import java.util.List;

public class minilayoutDeProjetoAdapter extends  RecyclerView.Adapter<minilayoutDeProjetoAdapter.MyViewHolder> {

    Context context;
    List<ProjetoDeBase> listaProjetos;

    public minilayoutDeProjetoAdapter(Context context, List<ProjetoDeBase> listaProjetos) {
        this.context = context;
        this.listaProjetos = listaProjetos;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.minilayout_de_projeto_exibicao, parent, false);

        return new MyViewHolder(itemLista);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout frameLayout;
        ImageView icone;
        TextView nomeDoProjetoTextView, tipoDeProjetoTextView, dataProjetoTextView;
        public LinearLayout viewForeground, viewBackground;
        //View divider;

        public MyViewHolder(View itemView) {
            super(itemView);

            frameLayout = itemView.findViewById(R.id.miniLayProjetoFrameLayout);
            icone = itemView.findViewById(R.id.tipoDeProjetoSimboloImageView);
            nomeDoProjetoTextView = itemView.findViewById(R.id.nomeDoProjetoTextView);
            tipoDeProjetoTextView = itemView.findViewById(R.id.tipoDeProjetoTextView);
            dataProjetoTextView = itemView.findViewById(R.id.dataProjetoTextView);
            viewForeground = itemView.findViewById(R.id.frenteLinearLayout);
            viewBackground = itemView.findViewById(R.id.versoLinearLayout);
            //divider = itemView.findViewById(R.id.projetosDividerTopo);
        }
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int i) {
        //Preenche os dados da view atual com os da posição:
        final ProjetoDeBase projeto = listaProjetos.get(i);
        final String nomeDoProjeto = projeto.getNome();
        final int tipoDeProjeto = projeto.getTipoDeProjeto();
        final int position = i;
        int idDaString=0, idDaImagem=0;
        String tipoDeProjetoString="", dataDoProjeto="";

        //Preenche de acordo com o tipo de projeto:
        //TODO implementar as alterações por imagem
        switch (tipoDeProjeto){
            case ProjetoDeBase.TROCADOR_DE_CALOR:
                idDaString = R.string.trocador__de_calor;
                break;
            case ProjetoDeBase.REATOR:
                idDaString=R.string.reator;
                break;
            default:
                idDaString=R.string.projeto_indefinido;
                break;
        }
        tipoDeProjetoString = context.getResources().getString(idDaString);




    //Substitui a imagem:
    if(idDaImagem!=0){
        try{//Tenta colocar a imagem como a passada pelo usuário
            holder.icone.setImageResource(idDaImagem);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //TODO Coloca o onClick do card:
    holder.frameLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, exibirDadosProjetoUnicoActivity.class);
            intent.putExtra("tipoDeProjeto", R.string.trocador__de_calor);
            intent.putExtra("nome", nomeDoProjeto);
            intent.putExtra("naoExibirSalvar",true);
            //Cria o trocador né bobão
            if(tipoDeProjeto == ProjetoDeBase.TROCADOR_DE_CALOR){
                ArmazenamentoDeDados.projetoDaVez = projeto;
            }

            context.startActivity(intent);
        }
    });

    //Preenche com os dados fornecidos:
    holder.nomeDoProjetoTextView.setText(nomeDoProjeto);
    holder.tipoDeProjetoTextView.setText(tipoDeProjetoString);
    holder.dataProjetoTextView.setText(dataDoProjeto);




    }


    @Override
    public int getItemCount() {
        return listaProjetos.size();
    }


    public void removeItem(int position){
        listaProjetos.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ProjetoDeBase projeto, int position){
        listaProjetos.add(position, projeto);
        notifyItemInserted(position);
    }

    public List<ProjetoDeBase> getListaDoAdapter(){
        return listaProjetos;
    }

}
