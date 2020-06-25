package com.example.heatex.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.heatex.Adapters.trocCalExibicaoAdapter;
import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.Database;
import com.example.heatex.Classes.SpecHairpinMultitub;
import com.example.heatex.Classes.TrocadorBitubularHeatex;
import com.example.heatex.Classes.TuboHairpin;
import com.renanmendes.eqat.DrawerActivity;
import com.renanmendes.eqat.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class listagemProjetosActivity extends AppCompatActivity {

    trocCalExibicaoAdapter adapter;
    RecyclerView recyclerView;
    List<TrocadorBitubularHeatex> listaTrocs;

    String nomeDoProjeto = "";
    int idTipoDeProjeto = R.string.app_name;
    Spinner ordenarProjetosSpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_projetos);

        //Arruma  toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        //Botão de "voltar"
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.listaProjetosRecyclerView);

        TextView textView = findViewById(R.id.listaDeProjetosTextView);
        textView.setVisibility(View.GONE);
        try{getSupportActionBar().setTitle(R.string.lista_de_projetos);}catch (Exception e){e.printStackTrace();}

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getIntExtra("tipoDeProjeto", 0) != 0) {
                idTipoDeProjeto = intent.getIntExtra("tipoDeProjeto", 0);
                //Bota o texto do tipo de projeto:
                //getSupportActionBar().setTitle(idTipoDeProjeto);
            }
            try{//Tentando colocar o topo como o nome que o usuário deu:
                nomeDoProjeto = intent.getStringExtra("nome");
                //getSupportActionBar().setTitle(nomeDoProjeto);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //Descobre o tipo de projeto
        //Se for um troc bitubular...
        if(idTipoDeProjeto == R.string.trocador_bitubular){

        }


        //Criando o adapter da exibição
        listaTrocs = ArmazenamentoDeDados.listaTrocadores;
        if((listaTrocs!=null) && (listaTrocs!=new ArrayList<TrocadorBitubularHeatex>())){
            adapter = new trocCalExibicaoAdapter(getApplicationContext(), listaTrocs);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else{recyclerView.setVisibility(View.GONE);}


        //Spinner para ordenar a recycler view:
        ordenarProjetosSpinner = findViewById(R.id.ordenarProjetosSpinner);
        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.ordenar_trocadores_array, android.R.layout.simple_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ordenarProjetosSpinner.setAdapter(spinnerArrayAdapter);

        ordenarProjetosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reordenarSpinner(ordenarProjetosSpinner, position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


    }

    @Override
    public void onResume(){
        super.onResume();
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        ordenarProjetosSpinner.setSelection(0);
    }


    //Inflando o menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fora_da_main, menu);
        return true;
    }

    //Lidando com itens de clique:
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.action_home:
                startActivity(new Intent(getApplicationContext(), DrawerActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //Podendo voltar atrás:
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        ArmazenamentoDeDados.listaTrocadores = listaTrocs;
        return true;
    }


    public void reordenarSpinner(Spinner spinner, int textoSelecionado){
        //Criando o vetor de valores de acordo com o texto selecionado:
        List<Double> valores = new ArrayList<>();

        double valorDaVez=0;
        int pegarOQue=0;
        pegarOQue=textoSelecionado;
        final int AREA=0, PRESSAO=1, COMPRIMENTOTOTAL=2;
        boolean ehInvertido=false; //Se for true vai inverter na hora de botar!!!

        switch (textoSelecionado){
            case 0:
                pegarOQue = AREA;
                ehInvertido=false;
                break;
            case 1:
                pegarOQue = AREA;
                ehInvertido=true;
                break;
            case 2:
                pegarOQue = PRESSAO;
                ehInvertido=false;
                break;
            case 3:
                pegarOQue = PRESSAO;
                ehInvertido=true;
                break;
            case 4:
                pegarOQue = COMPRIMENTOTOTAL;
                ehInvertido=false;
                break;
            case 5:
                pegarOQue = COMPRIMENTOTOTAL;
                ehInvertido=true;
                break;
        }

        //Para a pressão do menor pro maior:
        for(TrocadorBitubularHeatex troquinho:listaTrocs){

            if(pegarOQue==AREA){
                valorDaVez = troquinho.getAreaDeTrocaTotal();
            }else if(pegarOQue==PRESSAO){
                valorDaVez = troquinho.getDP();
            }else if(pegarOQue == COMPRIMENTOTOTAL){
                valorDaVez = troquinho.getComprimentoTotal();
            }

            valores.add(valorDaVez);
        }


        //roda o recycler view e muda de acordo com o solicitado!

        //Atualizando a lista, com uma zerada!!
        listaTrocs = novaOrdem(valores, listaTrocs);

        //Invertendo os valores! tem esse
        if(!ehInvertido){ Collections.reverse(listaTrocs); }

        //trocCalExibicaoAdapter novoAdapter = new trocCalExibicaoAdapter(getApplicationContext(), novaLista);
        adapter = new trocCalExibicaoAdapter(getApplicationContext(), listaTrocs);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.invalidate();

    }

    public List<TrocadorBitubularHeatex> novaOrdem(List<Double> valores, List<TrocadorBitubularHeatex> listosa){
        //Reordena. Recebe 2 vetores, 1 com as posições anteriores e outro com o valor.
        //Ordena do menor pro maior.
        //Encontra o menor valor:
        //double menorValor = Double.MI;
        List<TrocadorBitubularHeatex> listaAntiga = listosa; // copia pra n mexer na original

        //Nova lista de trocadores
        List<TrocadorBitubularHeatex> novaLista = new ArrayList<>();

        //Primeiro vamos descobrir as posições que devemos adicionar da lista antiga, na ordem certa
        //Roda a lista anterior
        final int tamanho = valores.size();
        for(int i=0; i<tamanho; i++){
            double valorminimo = Collections.min(valores);
            //Qual a posição dele?
            int posicao = valores.indexOf(valorminimo);
            //Agora adiciona esse valor máximo na nova lista!
            novaLista.add(listaAntiga.get(posicao));
            //E em seguida remove ele da antiga
            listaAntiga.remove(posicao);
            valores.remove(posicao);

            //System.out.println("DEBUG VALOR ATUAL =   " + valorminimo);

        }


        return novaLista;

    }

}

