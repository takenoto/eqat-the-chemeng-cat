package com.example.heatex.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.renanmendes.eqat.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.interrupted;

public class LoadingAcitivity extends AppCompatActivity {

    private ProgressBar progressBar;
    //private Button interromperButton;
    private TextView numeroDeProjetosTextView;
    private ImageButton interromperImgButton, seguirImageButton;
    ImageView imageView;

    int idStringTipoDeProjeto;
    int duracaoPadraoMS = 2000; //2 segundos
    Runnable updater, textoUpdater;
    Handler temporal = new Handler();
    Thread thread = new Thread();
    //Thread calculando;
    Runnable calculando;
    boolean jaAcabou = false;
    TextView textoCarregamentoTextView;

    LinearLayout.LayoutParams layoutParamsBig = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 4f);
    LinearLayout.LayoutParams layoutParamsSmall = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
    LinearLayout.LayoutParams layoutParamsNada = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_acitivity);

        try {
            getSupportActionBar().setTitle(R.string.projetando);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Recebe o intent
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getIntExtra("tipoDeProjeto", 0) != 0) {
                idStringTipoDeProjeto = intent.getIntExtra("tipoDeProjeto", 0);
            }
        }

        //Passando as ids
        progressBar = findViewById(R.id.loadingProjetosProgressBar);
        //interromperButton = findViewById(R.id.interromperProjetosButton);
        interromperImgButton = findViewById(R.id.interromperImageButton);
        seguirImageButton = findViewById(R.id.vamosLaImageButton);
        numeroDeProjetosTextView = findViewById(R.id.contagemItensProjetadosTextView);
        textoCarregamentoTextView = findViewById(R.id.textoCarregamentoTextView);

        imageView = findViewById(R.id.imagemMascoteCarregamento);
        //Bota pra rodar
        RotateAnimation rotateAnimation = new RotateAnimation(0,
                360,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(duracaoPadraoMS);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        imageView.startAnimation(rotateAnimation);

        //Prepara para o início do processo
        ArmazenamentoDeDados.keepGoing = true;
        //Coloca o listener no botão para interromper
        if (interromperImgButton != null) {
            interromperImgButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pararThreads();
                    finish();

                }
            });
        }
        seguirImageButton.setAlpha((float) 0.4);
        seguirImageButton.setLayoutParams(layoutParamsNada);
        interromperImgButton.setLayoutParams(layoutParamsBig);

        //Pegando as possibilidades do modelo de projeto selecionado:
        int numeroPossibilidades = 0;
        if (ArmazenamentoDeDados.tipoDeTrocador == ArmazenamentoDeDados.BITUBULAR) {
            numeroPossibilidades = ArmazenamentoDeDados.getListaFiltradaAtual().size() * ArmazenamentoDeDados.getListaFiltradaAtual().size();
        } else if (ArmazenamentoDeDados.tipoDeTrocador == ArmazenamentoDeDados.MULTITUBULAR) {
            numeroPossibilidades = ArmazenamentoDeDados.tubosMultitub.size();
        } else numeroPossibilidades = 0;


        System.out.println("DEBUG MAXIMO DE PROJETOS = " + numeroPossibilidades);
        if (progressBar != null) {
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
                    ArmazenamentoDeDados.keepGoing = false;
                    return;
                } else {
                    ArmazenamentoDeDados.keepGoing = true;
                    ArmazenamentoDeDados.projetarMultiplosTrocs(getApplicationContext());
                }
            }
        };
        thread = new Thread(calculando);

        gerarTextosDeCarregamento();


    }


    @Override
    protected void onResume() {

        if (!jaAcabou) {
            updateValores();
            thread.start();
        }


        super.onResume();


    }


    @Override
    protected void onPause() {
        pararThreads();

        super.onPause();
    }


    @Override
    protected void onDestroy() {
        pararThreads();
        super.onDestroy();

    }

    public void pararThreads() {
        temporal.removeCallbacks(updater);
        temporal.removeCallbacks(textoUpdater);
        temporal = new Handler();//cria novo pra esvaziar!
        thread.interrupt();
        thread = new Thread();

        ArmazenamentoDeDados.keepGoing = false;
    }

    void updateValores() {
        jaAcabou = false;
        updater = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (interrupted()) {
                            return;
                        }
                        int numProjetos = ArmazenamentoDeDados.quantidadeDeProjetos;
                        int progresso = ArmazenamentoDeDados.etapa;
                        //Atualiza as views
                        progressBar.setProgress(progresso);
                        numeroDeProjetosTextView.setText("" + ArmazenamentoDeDados.quantidadeDeProjetos);
                        //Se prepara pra encerrar
                        if (progresso >= progressBar.getMax()) {
                            jaAcabou = true;
                            seguirImageButton.setAlpha((float) 1.0);

                            //Ajustando feedback pro usuário
                            textoCarregamentoTextView.setText(R.string.concluido);
                            imageView.setImageResource(R.drawable.ic_check_holo_light);
                            imageView.clearAnimation();


                            seguirImageButton.setLayoutParams(layoutParamsBig);
                            interromperImgButton.setLayoutParams(layoutParamsSmall);
                            seguirImageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), listagemProjetosActivity.class);
                                    intent.putExtra("tipoDeProjeto", R.string.trocador__de_calor);
                                    startActivity(intent);
                                }
                            });

                        }

                    }
                });
                if (!jaAcabou)
                    temporal.postDelayed(updater, 10);
                else {
                    //Executa animação de saída ...
                    temporal.postDelayed(updater, 1000);
                    temporal.removeCallbacks(updater);
                    temporal.removeCallbacks(textoUpdater);

                }

            }
        };
        temporal.post(updater);
    }


    int posicao = 0;
    List<String> textos = new ArrayList<>();

    void gerarTextosDeCarregamento() {

        final List<String> textos = Arrays.asList(getResources().getStringArray(R.array.frases_carregamento));
        Collections.shuffle(textos);

        //A posição inicial é aleatória:
        posicao = new Random().nextInt(textos.size());
        textoUpdater = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (interrupted()) {
                            return; }

                        posicao+=1;
                        textoCarregamentoTextView.setText(textos.get(posicao%textos.size()));
            }


        });

                //Isso roda dentro dele mesmo
                temporal.postDelayed(textoUpdater, duracaoPadraoMS);
            }

        };

        //Tem que rodar a primeira vez
        temporal.post(textoUpdater);


    }
}