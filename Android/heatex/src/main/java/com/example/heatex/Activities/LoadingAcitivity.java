package com.example.heatex.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.R;

import static java.lang.Thread.interrupted;

public class LoadingAcitivity extends AppCompatActivity {

    public ProgressBar progressBar;
    public Button interromperButton;
    private TextView numeroDeProjetosTextView;
    int idStringTipoDeProjeto;
    Runnable updater;
    Handler temporal = new Handler();
    Thread thread = new Thread();
    //Thread calculando;
    Runnable calculando;
    boolean jaAcabou = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_acitivity);


        //Recebe o intent
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getIntExtra("tipoDeProjeto", 0) != 0) {
                idStringTipoDeProjeto = intent.getIntExtra("tipoDeProjeto", 0);
            }
        }

        //Passando as ids
        progressBar = findViewById(R.id.loadingProjetosProgressBar);
        interromperButton = findViewById(R.id.interromperProjetosButton);
        numeroDeProjetosTextView = findViewById(R.id.contagemItensProjetadosTextView);


        //Prepara para o início do processo
        ArmazenamentoDeDados.keepGoing=true;
        //Coloca o listener no botão para interromper
        if(interromperButton != null){
            interromperButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pararThreads();
                    finish();

                }
            });
        }

        //Pegando as possibilidades do modelo de projeto selecionado:
        int numeroPossibilidades = ArmazenamentoDeDados.getListaFiltradaAtual().size()*ArmazenamentoDeDados.getListaFiltradaAtual().size();
        System.out.println("DEBUG MAXIMO DE PROJETOS = " + numeroPossibilidades);
        if(progressBar!=null){
            progressBar.setMax(numeroPossibilidades);
        }


        //Rodando a função em background:
        /*calculando = new Thread(new Runnable() {
            @Override
            public void run() {
                if(interrupted() || jaAcabou){
                    return;
                }

                ArmazenamentoDeDados.projetarMultiplosTrocs(getApplicationContext());
            }
        });

         */


        calculando = new Runnable() {
            @Override
            public void run() {
                if (interrupted()) {
                    ArmazenamentoDeDados.keepGoing=false;
                    return;
                }else{
                    ArmazenamentoDeDados.keepGoing=true;
                    ArmazenamentoDeDados.projetarMultiplosTrocs(getApplicationContext());
                    }
                }
        };
        thread = new Thread(calculando);

        //TODO mudei o código de atualização, resta conferir se a thread agora se ajeitou...
        //calculando.start();



    }


    @Override
    protected void onResume(){

        if(!jaAcabou){
            updateValores();
            thread.start();
        }


        super.onResume();


    }


    @Override
    protected void onPause(){
        pararThreads();

        super.onPause();
    }


    @Override
    protected void onDestroy(){
        pararThreads();
        super.onDestroy();

    }

    public void pararThreads(){
        temporal.removeCallbacks(updater);
        temporal = new Handler();//cria novo pra esvaziar!
        thread.interrupt();
        thread = new Thread();
        ArmazenamentoDeDados.keepGoing=false;
    }

    void updateValores(){
        jaAcabou=false;
        updater = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(interrupted()){
                            return;
                        }
                        int numProjetos = ArmazenamentoDeDados.quantidadeDeProjetos;
                        int progresso = ArmazenamentoDeDados.etapa;
                        //Atualiza as views
                        progressBar.setProgress(progresso);
                        numeroDeProjetosTextView.setText(""+ArmazenamentoDeDados.quantidadeDeProjetos);
                        //Se prepara pra encerrar
                        if(progresso>=progressBar.getMax()){
                            jaAcabou = true;
                            Intent intent = new Intent(getApplicationContext(), listagemProjetosActivity.class);
                            intent.putExtra("tipoDeProjeto", R.string.trocador__de_calor);
                            startActivity(intent);
                        }

                    }
                });
                if(!jaAcabou)
                    temporal.postDelayed(updater, 10);
                else {
                    //Executa animação de saída ...
                    temporal.postDelayed(updater, 1000);
                    temporal.removeCallbacks(updater);

                }

            }
        };
        temporal.post(updater);
    }
}
