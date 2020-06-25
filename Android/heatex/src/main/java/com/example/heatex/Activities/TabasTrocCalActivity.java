package com.example.heatex.Activities;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.ErroPadrao;
import com.example.heatex.Fragments.InserirDadosTrocadorBitubularFragment;
import com.example.heatex.Fragments.fluidoDadosTrocCalFragment;
import com.example.heatex.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.heatex.ui.main.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabasTrocCalActivity extends AppCompatActivity {


    //TODO deve ser implementado melhor no futuro, tá muito bagunçado/perdido
    // Nome das abas que serão criadas:
    int[] TabNames = new int[]{R.string.fluido1, R.string.fluido2, R.string.design_e_material};
    Fragment[] listaFragmentosInflados = {new fluidoDadosTrocCalFragment(1), new fluidoDadosTrocCalFragment(2), new InserirDadosTrocadorBitubularFragment()};
    //Por padrão vamos deixar no app name!
    int idNovoTitulo = R.string.app_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();

        //Inicializando as abas:
        setContentView(R.layout.activity_tabas_troc_cal);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), TabNames, listaFragmentosInflados);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //Substitui o nome de topo da aba por aqui que se está projetando
        //Só substitui caso o intent tenha mandado algo!
        if (intent != null) {
            if (intent.getIntExtra("tipoDeProjeto", 0) != 0) {
                idNovoTitulo = intent.getIntExtra("tipoDeProjeto", 0);
                TextView tabTitle = findViewById(R.id.title);
                tabTitle.setText(idNovoTitulo);
            }
        }


        //ABRINDO A ATIVIDADE QUE EXIBE UM ÚNICO PROJETO!!!
        //Floating action button:
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (idNovoTitulo == R.string.trocador__de_calor) {

                    if(contemErros(R.string.trocador__de_calor)){
                        criarAlertaDeErros(R.string.trocador__de_calor);
                    }else{

                        //Se for um diâmetro único...
                        if (!ArmazenamentoDeDados.testarTodosOsDiametros) {
                            Intent saidaIntent = new Intent(getApplicationContext(), exibirDadosProjetoUnicoActivity.class);
                            saidaIntent.putExtra("tipoDeProjeto", R.string.trocador__de_calor);
                            ArmazenamentoDeDados.criarTrocadorDaVez();

                            /*
                            final View sharedView = findViewById(R.id.fab);
                            String transitionName = getString(R.string.botao_aba_troc_transition_name);
                            ActivityOptions transitionActivityOptions = null;
                            if(Build.VERSION.SDK_INT>=21){
                                try{
                                    transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(TabasTrocCalActivity.this, sharedView, transitionName);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }


                             */

                            saidaIntent.putExtra("tipoDeProjeto", R.string.trocador__de_calor);
                            /*if(Build.VERSION.SDK_INT>=17){
                                if(transitionActivityOptions!=null){
                                    startActivity(saidaIntent, transitionActivityOptions.toBundle());
                                }else startActivity(saidaIntent);
                            }else startActivity(saidaIntent);*/

                            startActivity(saidaIntent);
                        }


                        //Se for para testar todos os diâmetros...
                        else {
                            Intent saidaIntent = new Intent(getApplicationContext(), LoadingAcitivity.class);
                            /*
                            final View sharedView = findViewById(R.id.fab);
                            String transitionName = getString(R.string.botao_aba_troc_transition_name);
                            ActivityOptions transitionActivityOptions = null;
                            if(Build.VERSION.SDK_INT>=21){
                                try{
                                    transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(TabasTrocCalActivity.this, sharedView, transitionName);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                             */


                            saidaIntent.putExtra("tipoDeProjeto", R.string.trocador__de_calor);
                            /*
                            if(Build.VERSION.SDK_INT>=17){
                                if(transitionActivityOptions!=null){
                                    startActivity(saidaIntent, transitionActivityOptions.toBundle());
                                }else startActivity(saidaIntent);
                            }else*/ startActivity(saidaIntent);

                        }

                    }

                }
            }
        });


    }


    public LinearLayout criarUmaMiniViewDeErro(int arquivoDeImagem, String texto, int corDoTexto){
        LinearLayout linLay = (LinearLayout) getLayoutInflater().inflate(R.layout.erro_padrao_layout,null);
        //linLay.setOrientation(LinearLayout.VERTICAL);
        //linLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ImageView imageView = linLay.findViewById(R.id.iconeDoLayoutErroImageView);
        TextView erroTextView = linLay.findViewById(R.id.erroMiniLayoutTextView);
        //Muda a imagem, o texto e a descrição

        //Manda 0 pro que não for ser alterado
        if(arquivoDeImagem!=0){
            imageView.setImageResource(arquivoDeImagem);
        }
        if(corDoTexto!=0){
            erroTextView.setTextColor(getResources().getColor(corDoTexto));
            DrawableCompat.setTint(
                    DrawableCompat.wrap(imageView.getDrawable()),
                    ContextCompat.getColor(getApplicationContext(), corDoTexto)
            );
            imageView.setAlpha((float) 0.3);
        }
        if(texto!=null){
            //Passa pro html pra justificar
            //texto = "<p align=\"justify>\" " + texto + "</p>";
            erroTextView.setText(Html.fromHtml(texto));
        }


        return linLay;
    }

    public LinearLayout criarUmaViewCompletaDeErro(String tipoDeErro, String descricaoErro, String ondeFicaOErro){

        //Empilha todos os erros:
        LinearLayout linLay = new LinearLayout(getApplicationContext());
        linLay.setOrientation(LinearLayout.VERTICAL);
        linLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //-------------TÍTULO---------------------
        //Cria a linearLayout do título do erro:
        //Gerar o texto:
        LinearLayout miniLay = criarUmaMiniViewDeErro(R.drawable.erro_exclamacao_escuro, tipoDeErro, R.color.colorREDERROR);
        linLay.addView(miniLay);

        //Gera a descrição do erro:
        LinearLayout miniLay2 = criarUmaMiniViewDeErro(R.drawable.short_text_escuro, descricaoErro, R.color.colorCinzaEscuro);
        miniLay2.findViewById(R.id.dividerErroneo).setVisibility(View.GONE);
        TextView textineo = (TextView) miniLay2.findViewById(R.id.erroMiniLayoutTextView);
        textineo.setAllCaps(false);
        linLay.addView(miniLay2);

        //Gera o arquivo de localização do erro
        LinearLayout miniLay3 = criarUmaMiniViewDeErro(R.drawable.interrogacao_escuro, ondeFicaOErro, R.color.secondaryDarkColor);
        miniLay3.findViewById(R.id.dividerErroneo).setVisibility(View.GONE);
        miniLay3.findViewById(R.id.erroMiniDivider).setVisibility(View.GONE);
        linLay.addView(miniLay3);

        return linLay;
    }

    public LinearLayout criarTodosOsErros(List<ErroPadrao> listaDeErros){

        String tipoDeErro="", descricaoDoErro="", ondeFicaOErro="";
        //int corDoTexto = R.color.colorCinzaEscuro;

        //Roda todos os erros...
        //Empilha todos os erros:
        LinearLayout linLay = new LinearLayout(getApplicationContext());
        linLay.setOrientation(LinearLayout.VERTICAL);
        linLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        //dentro do for:
        //Primeiro bota tudo que for zerado aqui, numa coisa só:
        List<String> localizacaoDosErros  = new ArrayList<String>();
        boolean jaFoiUmaVez=false;

        for(int i=0; i<listaDeErros.size(); i++) {
            ErroPadrao erroPadrao=listaDeErros.get(i);
            if (erroPadrao.tipo_de_erro == ArmazenamentoDeDados.ALGUM_DADO_ZERADO) {
                //So pra pegar as besteiras de todos que são iguais
                if(!jaFoiUmaVez){
                    jaFoiUmaVez=true;
                    tipoDeErro=ArmazenamentoDeDados.listaErrosTrocsStringoso[erroPadrao.tipo_de_erro];
                    descricaoDoErro+= getResources().getString(erroPadrao.descricaoDoErroTexto) + " ";
                }if(!descricaoDoErro.equals("")){
                    //Já foi preenchido... hora de botar as variáveis
                    descricaoDoErro+=" ( " + getResources().getString(erroPadrao.idPropRelacionada) + " ) ";

                }

                //Onde o erro foi encontrado, se já não tiver sido mencionado:
                if(!localizacaoDosErros.contains(erroPadrao.ondeFicaPropZeradaIdDoTituloDaAba)){
                    localizacaoDosErros.add(getResources().getString(erroPadrao.ondeFicaPropZeradaIdDoTituloDaAba));
                }
            }
        }
        //Preenche a lista de localizacao com os erros
        ondeFicaOErro = "";
        for(String local:localizacaoDosErros){ ondeFicaOErro+=local + " "; }

        //Aí cria uma view só com tudo o que é zerado
        //E adiciona à principal:
        //mas só se houver algo a ser adicionado...
        if(jaFoiUmaVez){
            linLay.addView(criarUmaViewCompletaDeErro(tipoDeErro, descricaoDoErro, ondeFicaOErro));
        }



        //Zerando pra poder utilizar as mesmas variáveis novamente
        tipoDeErro = "";
        descricaoDoErro="";
        ondeFicaOErro="";

        //Aí roda todos os outros e faz um por um
        for(int i=0; i<listaDeErros.size(); i++) {
            ErroPadrao erroPadrao=listaDeErros.get(i);
            if(erroPadrao.tipo_de_erro!=ArmazenamentoDeDados.ALGUM_DADO_ZERADO){
                tipoDeErro=ArmazenamentoDeDados.listaErrosTrocsStringoso[erroPadrao.tipo_de_erro];
                //Caso seja um erro de calor, adicionar os valores também !!!
                descricaoDoErro = getResources().getString(erroPadrao.descricaoDoErroTexto);
                if(erroPadrao.tipo_de_erro==ArmazenamentoDeDados.CALOR_NAO_CONFERE){
                    descricaoDoErro += "//   q1 =  " + ArmazenamentoDeDados.formatarValorNumerico(ArmazenamentoDeDados.q1,"e") + " " + getResources().getString(R.string.calor_q_unidade);
                    descricaoDoErro += "//   q2 =  " + ArmazenamentoDeDados.formatarValorNumerico(ArmazenamentoDeDados.q2,"e") + " " + getResources().getString(R.string.calor_q_unidade);
                }

                ondeFicaOErro = getResources().getString(erroPadrao.ondeFicaPropZeradaIdDoTituloDaAba);
                linLay.addView(criarUmaViewCompletaDeErro(tipoDeErro, descricaoDoErro, ondeFicaOErro));

            }

        }




        return linLay;
    }

    public boolean contemErros(int tipoDeProjeto){
        //Checa se o projeto contém erros
        if(tipoDeProjeto==R.string.trocador__de_calor){
            ArmazenamentoDeDados.gerarErrosTrocadores();
            if(ArmazenamentoDeDados.listaDeErros.size()>=1){
                return true;
            }else return false;
        }
        return false; //por padrão retorna falso
    }

    public void criarAlertaDeErros(int tipoDeProjeto){
        if(tipoDeProjeto == R.string.trocador__de_calor){

            LinearLayout linearLayout = criarTodosOsErros(ArmazenamentoDeDados.listaDeErros);

            ScrollView scrollView = new ScrollView(getApplicationContext());
            scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            scrollView.addView(linearLayout);

            AlertDialog.Builder builder = new AlertDialog.Builder(TabasTrocCalActivity.this);
            //builder.setMessage(R.string.mensagem_erro_calores_errados_trocador)
            builder.setView(scrollView)
                    .setTitle(R.string.erro_alerta)
                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();

        }

    }


}