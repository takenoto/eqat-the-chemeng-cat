package com.example.heatex.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.example.heatex.R;
import com.example.heatex.Activities.TabasTrocCalActivity;

public class listaDeDesignsFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle SavedInstance){

        //Infla o layout
        View rootView = inflater.inflate(R.layout.lista_design_disponiveis, containter, false);

        //Instanciando o botão do trocador de calor
        Button buttonTrocadores = (Button) rootView.findViewById(R.id.trocCalButton);
        buttonTrocadores.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Toast.makeText(getActivity(),"pause",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TabasTrocCalActivity.class);
                intent.putExtra("tipoDeProjeto", R.string.trocador__de_calor);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

}
