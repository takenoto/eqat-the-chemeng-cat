package com.example.heatex.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heatex.Activities.TabasTrocCalActivity;
import com.example.heatex.Adapters.minilayoutDeProjetoAdapter;
import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.ProjetoDeBase;
import com.example.heatex.Classes.RecyclerItemTouchHelper;
import com.renanmendes.eqat.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class exibirProjetosAtividadePrincipalFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    LinearLayout linLay;
    View rootView;
    List<ProjetoDeBase> listaDeProjetos;
    minilayoutDeProjetoAdapter adapter;
    private ConstraintLayout constraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle SavedInstance){

        //Infla o layout
        rootView = inflater.inflate(R.layout.fragment_armazem_layout, containter, false);

        //Toolbar myToolbar = rootView.findViewById(R.id.toolbar);
        //myToolbar.setVisibility(View.GONE);

        //Pegando as views por id
        constraintLayout = rootView.findViewById(R.id.constraintLayout);
        recyclerView = rootView.findViewById(R.id.listaProjetosRecyclerView);
        linLay = rootView.findViewById(R.id.linLayInternoListaProjetosLinearLayout);




        return rootView;
    }

    @Override
    public void onResume(){
        //Rodar todos os projetos e adicionar um por um:
        listaDeProjetos = ArmazenamentoDeDados.obterListaDeProjetosSalva(rootView.getContext());
        //linLay.removeAllViews();
        if(false && (listaDeProjetos!=null)){ //Para evitar de quebrar quando a lista não tiver nada dentro
            int contador=-1;
            //Infla layout padrão que diz o tipo de projeto, e preenche
            for(ProjetoDeBase projeto:listaDeProjetos){
                contador+=1;
                String nomeDoProjeto = projeto.getNome();


                int tipoDeProjeto = projeto.getTipoDeProjeto();
                int idDaString=0, idDaImagem=0;
                String tipoDeProjetoString="", dataDoProjeto="";
                //Preenche de acordo com o tipo de projeto:
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
                tipoDeProjetoString = getResources().getString(idDaString);

                LinearLayout miniLay = gerarLayoutDeUmProjeto(idDaImagem, nomeDoProjeto, tipoDeProjetoString, dataDoProjeto);

                /*
                //Remover os divisores desnecessários:
                if(contador==0){ //Remove o de cima do primeiro
                    miniLay.findViewById(R.id.projetosDividerTopo).setVisibility(View.GONE);
                }
                 */


                linLay.addView(miniLay);
            }
        }

        //Carrega o recycler view:
        adapter = new minilayoutDeProjetoAdapter(getContext(), listaDeProjetos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        super.onResume();
    }


    //TODO remover isso pq nem é pra ser usado mais...
    public LinearLayout gerarLayoutDeUmProjeto(int idImagem, String nomeDoProjeto, String tipoDeProjeto, String dataDoProjeto){
        LinearLayout miniLay = (LinearLayout) getLayoutInflater().inflate(R.layout.minilayout_de_projeto_exibicao, null);

        //Pega as views
        FrameLayout frameLayout = miniLay.findViewById(R.id.miniLayProjetoFrameLayout);
        ImageView icone = miniLay.findViewById(R.id.tipoDeProjetoSimboloImageView);
        TextView nomeDoProjetoTextView = miniLay.findViewById(R.id.nomeDoProjetoTextView);
        TextView tipoDeProjetoTextView = miniLay.findViewById(R.id.tipoDeProjetoTextView);
        TextView dataProjetoTextView = miniLay.findViewById(R.id.dataProjetoTextView);


        //Substitui a imagem:
        if(idImagem!=0){
            try{//Tenta colocar a imagem como a passada pelo usuário
                icone.setImageResource(idImagem);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //TODO Coloca o onClick do card:
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Preenche com os dados fornecidos:
        nomeDoProjetoTextView.setText(nomeDoProjeto);
        tipoDeProjetoTextView.setText(tipoDeProjeto);
        dataProjetoTextView.setText(dataDoProjeto);

        return miniLay;
    }



    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof minilayoutDeProjetoAdapter.MyViewHolder){
            String nomeDoRemovido = listaDeProjetos.get(position).getNome();

            //Salva o item que foi removido pra caso o usuário queira desfazer!
            final ProjetoDeBase projetoRemovido = listaDeProjetos.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            //remoção
            adapter.removeItem(deletedIndex);
            //Atualiza o banco de dados!
            ArmazenamentoDeDados.atualizarListaDeProjetos(getContext(), adapter.getListaDoAdapter());

            //opção de desfazer:
            String textoDeRemocao = getResources().getString(R.string.zzz_foi_removido);
            textoDeRemocao = textoDeRemocao.replace("zzzzz", nomeDoRemovido);
            Snackbar.make(constraintLayout, textoDeRemocao, Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.desfazer), new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            adapter.restoreItem(projetoRemovido, deletedIndex);
                            ArmazenamentoDeDados.atualizarListaDeProjetos(getContext(), adapter.getListaDoAdapter());
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.colorREDERROR))
                    .show();}
    }


}
