package com.example.heatex.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.renanmendes.eqat.R;
import com.example.heatex.Activities.TabasTrocCalActivity;

public class listaDeDesignsFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle SavedInstance){

        //Infla o layout
        View rootView = inflater.inflate(R.layout.lista_design_disponiveis, containter, false);


        //Instanciando o botão do trocador de calor
        CardView cardViewTrocadores = (CardView) rootView.findViewById(R.id.trocCalCardView);
        CardView cardViewReatores = (CardView) rootView.findViewById(R.id.reatorCardView);

        //Colocando os métodos

        //TROCADOR
        cardViewTrocadores.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Toast.makeText(getActivity(),"pause",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TabasTrocCalActivity.class);
                intent.putExtra("tipoDeProjeto", R.string.trocador__de_calor);
                startActivity(intent);
            }
        });

        //REATOR
        cardViewReatores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aindaNaoDisponivel(v);
            }
        });



        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    public void aindaNaoDisponivel(View v){
        Snackbar.make(v, getResources().getString(R.string.esse_recurso_ainda_nao_disponivel), Snackbar.LENGTH_LONG)
                .show();
    }

}
