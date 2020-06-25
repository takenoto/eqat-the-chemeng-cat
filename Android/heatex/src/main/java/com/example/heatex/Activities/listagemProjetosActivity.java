package com.example.heatex.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.heatex.Adapters.trocCalExibicaoAdapter;
import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.Database;
import com.example.heatex.Classes.SpecHairpinMultitub;
import com.example.heatex.Classes.TrocadorBitubularHeatex;
import com.example.heatex.Classes.TuboHairpin;
import com.example.heatex.R;

import java.util.ArrayList;
import java.util.List;

public class listagemProjetosActivity extends AppCompatActivity {

    trocCalExibicaoAdapter adapter;
    RecyclerView recyclerView;

    String nomeDoProjeto = "";
    int idTipoDeProjeto = R.string.app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_projetos);

        recyclerView = findViewById(R.id.listaProjetosRecyclerView);


        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getIntExtra("tipoDeProjeto", 0) != 0) {
                idTipoDeProjeto = intent.getIntExtra("tipoDeProjeto", 0);
                //Bota o texto do tipo de projeto:
                getSupportActionBar().setTitle(idTipoDeProjeto);
            }
            try{//Tentando colocar o topo como o nome que o usuário deu:
                nomeDoProjeto = intent.getStringExtra("nome");
                getSupportActionBar().setTitle(nomeDoProjeto);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //Descobre o tipo de projeto
        //Se for um troc bitubular...
        if(idTipoDeProjeto == R.string.trocador_bitubular){

        }


        //Criando o adapter da exibição
        List<TrocadorBitubularHeatex> listaTrocs = ArmazenamentoDeDados.listaTrocadores;
        adapter = new trocCalExibicaoAdapter(getApplicationContext(), listaTrocs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);



    }

    @Override
    public void onResume(){
        super.onResume();
    }



}

