package com.renanmendes.eqat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.heatex.Adapters.SumarioDeLicoesAdapter;
import com.example.heatex.Adapters.trocCalExibicaoAdapter;
import com.example.heatex.Classes.ProjetoDeBase;
import com.example.heatex.Classes.TrocadorBitubularHeatex;

import java.util.ArrayList;
import java.util.List;

public class SumarioDaLicaoActivity extends AppCompatActivity {

    String titutloDaLicao;
    TextView tituloMateriaLicaoTextView;
    int qualOProjeto=0, qualTipo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Coloca o layout do sumário
        setContentView(R.layout.licao_sumario_layout);
        RecyclerView recyclerView = findViewById(R.id.licaoSumarioRecyclerView);
        tituloMateriaLicaoTextView = findViewById(R.id.tituloMateriaLicaoTextView);
        ImageButton voltarImageButton = findViewById(R.id.voltarImageButton);
        Button vamosLaButton = findViewById(R.id.vamosLaButton);


        //TODO colocar aqui as configuração do texto de topo, com base no intent
        Intent intent = getIntent();

        if (intent != null) {
                qualOProjeto = intent.getIntExtra("qualProjeto", 0);
                qualTipo = intent.getIntExtra("qualTipo", 0);
            }


        List<String> listaTexto = gerarListaDeSumario(); //As verificações já são feitas por dentro

        carregarDadosNoRecyler(recyclerView, listaTexto);


        //OnClick dos botões:
        voltarImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SumarioDaLicaoActivity.super.onBackPressed();
            }
        });

        vamosLaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LicaoActivity.class);
                intent.putExtra("qualProjeto", qualOProjeto);
                intent.putExtra("qualTipo", qualTipo);
                intent.putExtra("veioDoSumario", true);
                startActivity(intent);
            }
        });

    }



    public void carregarDadosNoRecyler(RecyclerView recyclerView, List<String> listaTextosa){
        //Carrega dados da seção no recycler view:
        SumarioDeLicoesAdapter adapter = new SumarioDeLicoesAdapter(getApplicationContext(), listaTextosa);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    public List<String> gerarListaDeSumario(){

        List<String> listaTxt = new ArrayList<>();
        String qualLicao="";

        if(qualOProjeto==ProjetoDeBase.TROCADOR_DE_CALOR){

            switch(qualTipo){
                case TrocadorBitubularHeatex.BITUBULAR:
                    tituloMateriaLicaoTextView.setText(R.string.trocador_bitubular);
                    qualLicao=ObterLicaoSpecialClass.bitubular;
                    break;
                default:
                    tituloMateriaLicaoTextView.setText(R.string.erro_alerta);
                    listaTxt.add("Tem nada não");
                    listaTxt.add("EU DISSE NADA");
                    break;
            }

            listaTxt = ObterLicaoSpecialClass.getSumarioDaLicao(qualLicao, getApplicationContext());
        }

        return listaTxt;
    }

}
