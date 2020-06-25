package com.example.heatex.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.heatex.Adapters.propriedadesExibicaoSimplesAdapter;
import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.ProjetoDeBase;
import com.example.heatex.Classes.PropriedadeBasica;
import com.example.heatex.Classes.PropriedadeBasicaExibicao;
import com.example.heatex.Classes.TrocadorBitubularHeatex;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.renanmendes.eqat.DrawerActivity;
import com.renanmendes.eqat.R;
import com.renanmendes.eqat.SumarioDaLicaoActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.NestedScrollingChild;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import static java.lang.Thread.interrupted;

public class exibirDadosProjetoUnicoActivity extends AppCompatActivity {

    propriedadesExibicaoSimplesAdapter adapter;
    List<PropriedadeBasicaExibicao> listonildaFluido1, listonildaFluido2, listonildaMetal, listonildaProjeto;
    LinearLayout linLay;
    Button estudarButton, salvarButton;
    ProjetoDeBase projetoExibido = new ProjetoDeBase();
    Handler temporal = new Handler();
    ProgressBar progressBar;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;

    int idTipoDeProjeto = R.string.app_name;
    String nomeDoProjeto = "";
    private Boolean sumirComBotaoSalvar=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exibir_dados_projeto_unico);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarLayout = findViewById(R.id.toolbar_layout);

        setSupportActionBar(toolbar);

        //Botão de "voltar"
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        estudarButton = findViewById(R.id.estudarButton);
        salvarButton = findViewById(R.id.salvarButton);
        progressBar = findViewById(R.id.progressoCricularProgressBar);
        linLay = findViewById(R.id.exibicaoDadosLinearLayout);


        //AppBar:
        final AppBarLayout mAppBarLayout = findViewById(R.id.app_bar);

        //DEFININDO O TÍTULO DA PÁGINA!!!!
        //Obtendo o intent e informações dele!
        Intent intent = getIntent();
        getSupportActionBar().setTitle(R.string.projeto_indefinido);
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
            //Tentando descobrir se devemos esconder o botão de salvar ou não
            try{
                sumirComBotaoSalvar = intent.getBooleanExtra("naoExibirSalvar", false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(sumirComBotaoSalvar){ salvarButton.setVisibility(View.GONE); }

        if((idTipoDeProjeto == R.string.trocador__de_calor)){
            //Se for um trocador....
            projetoExibido = ArmazenamentoDeDados.projetoDaVez;
        }


        //Coloca os botões pra fazer alguma coisa
        estudarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean projetoEstudavel = true;
                //Vamos determinar exatamente qual o projeto, e em seguida decidir o que fazer
                //Qual o projeto:
                if(ProjetoDeBase.TROCADOR_DE_CALOR == projetoExibido.getTipoDeProjeto()){
                    if(TrocadorBitubularHeatex.BITUBULAR == projetoExibido.obterProjetoTrocador().getIdDoTipoDeTrocador()){
                        Intent intent = new Intent(getApplicationContext(), SumarioDaLicaoActivity.class);
                        intent.putExtra("qualProjeto", ProjetoDeBase.TROCADOR_DE_CALOR);
                        intent.putExtra("qualTipo", TrocadorBitubularHeatex.BITUBULAR);
                        startActivity(intent);
                    }else{projetoEstudavel = false;}
                }

                if(!projetoEstudavel){ //Se não for um projeto "estudável"...
                    Snackbar.make(v, getResources().getString(R.string.esse_recurso_ainda_nao_disponivel), Snackbar.LENGTH_LONG)
                            .show();
                }


            }
        });

        salvarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarAlertaInserirNomeProjeto();
            }
        });

        carregarImagem();


        //Carregando a tela em tempo real:
        Runnable carregoso = new Runnable() {
            @Override
            public void run() {
                if (interrupted()) {
                    return;
                }else{
                    carregarTela();
                }
            }
        };
        //Thread thread = new Thread(carregoso);
        temporal.postDelayed(carregoso, 1200);

    }


    @Override
    public void onResume(){
        super.onResume();

    }

    public void carregarTela(){


        //Carregamento para cada tipo de projeto:
        if((idTipoDeProjeto==R.string.trocador__de_calor) && ArmazenamentoDeDados.projetoDaVez.getTipoDeProjeto()==ProjetoDeBase.TROCADOR_DE_CALOR) {
            //Declarando o trocador que vamos usar aqui
            TrocadorBitubularHeatex troquinho = ArmazenamentoDeDados.projetoDaVez.obterProjetoTrocador();

            //Listas de propriedades:
            listonildaFluido1 = gerarListaPropsFluido(1, troquinho);
            listonildaFluido2 = gerarListaPropsFluido(2, troquinho);
            listonildaMetal = gerarListaPropsMetalBitubular(troquinho);
            listonildaProjeto = gerarListaPropsProjetoBitubular(troquinho);


            //Variáveis usadas para o preenchimento:
            String titulo;
            LinearLayout miniLay;

            //Preenchendo Trocador - básico
            titulo = getResources().getString(R.string.trocador__de_calor);
            miniLay = criarMiniLinLay(titulo, listonildaProjeto, getApplicationContext());
            miniLay.setOrientation(LinearLayout.VERTICAL);
            miniLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linLay.addView(miniLay);

            //Preenchendo Metal
            titulo = getResources().getString(R.string.material_do_trocador);
            miniLay = criarMiniLinLay(titulo, listonildaMetal, getApplicationContext());
            miniLay.setOrientation(LinearLayout.VERTICAL);
            miniLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linLay.addView(miniLay);

            //Preenchendo fluido 1
            titulo = getResources().getString(R.string.fluido1);
            miniLay = criarMiniLinLay(titulo, listonildaFluido1, getApplicationContext());
            miniLay.setOrientation(LinearLayout.VERTICAL);
            miniLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linLay.addView(miniLay);

            //Preenchendo fluido 2
            titulo = getResources().getString(R.string.fluido2);
            miniLay = criarMiniLinLay(titulo, listonildaFluido2, getApplicationContext());
            miniLay.setOrientation(LinearLayout.VERTICAL);
            miniLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linLay.addView(miniLay);

        }


        //Só remove depois que carregar!
        linLay.removeView(progressBar);
    }

    public void carregarImagem(){
        if(idTipoDeProjeto==R.string.trocador__de_calor){
            //TODO Carrega a imagem do trocador...
            //toolbarLayout.setBackground();
        }

    }


    //A função abaixo geral uma lista de propriedades para determinado fluido.
    public List<PropriedadeBasicaExibicao> gerarListaPropsFluido_ver0(int num){
        List<PropriedadeBasicaExibicao> listosa = new ArrayList<>();
        PropriedadeBasicaExibicao miniProp;
        //Botar isso dentro do for:
        int[] idDasSiglas = ArmazenamentoDeDados.idDasPropriedades;
        int[] idDasProps = ArmazenamentoDeDados.idDasPropsNomeFluido;
        int[] idDasUnidades = ArmazenamentoDeDados.idDasUnidadesFluido;
        String sigla, nomeProp, unidade, valor="";
        double[] vetorValores;

        if(num == 1){
             vetorValores = ArmazenamentoDeDados.valoresFluido1;
        }else{
             vetorValores = ArmazenamentoDeDados.valoresFluido2;
        }
        for(int i=0; i < idDasProps.length; i++){
            sigla = getResources().getString(idDasSiglas[i]);
            nomeProp = getResources().getString(idDasProps[i]);
            unidade = getResources().getString(idDasUnidades[i]);
            valor = ArmazenamentoDeDados.formatarValorNumerico(vetorValores[i], null);

            miniProp = new PropriedadeBasicaExibicao(sigla, nomeProp, unidade, valor);
            listosa.add(miniProp);
        }

        return listosa;
    }

    public List<PropriedadeBasicaExibicao> gerarListaPropsFluido(int num, TrocadorBitubularHeatex troquinho){
        List<PropriedadeBasicaExibicao> listosa = new ArrayList<>();
        PropriedadeBasicaExibicao miniProp;
        //Botar isso dentro do for:
        String sigla, nomeProp, unidade, valor="";
        String nomeFluido = troquinho.getNomeFluido(num);

        int[] idDasSiglas = troquinho.getCoisativos(0, 1);
        int[] idDasProps = troquinho.getCoisativos(1, 1);
        int[] idDasUnidades = troquinho.getCoisativos(2, 1);
        double[] vetorValores = troquinho.getListaPropsFluidos(num);

        //Antes de tudo o nome do fluido:
        miniProp = new PropriedadeBasicaExibicao("", getResources().getString(R.string.nome_do_fluido), "", nomeFluido);
        listosa.add(miniProp);

        //Alocação:
        miniProp = new PropriedadeBasicaExibicao("", getResources().getString(R.string.alocacao), "", getResources().getString(troquinho.getIdAlocacao(num)));
        listosa.add(miniProp);

        for(int i=0; i < idDasProps.length; i++){
            sigla = getResources().getString(idDasSiglas[i]);
            nomeProp = getResources().getString(idDasProps[i]);
            unidade = getResources().getString(idDasUnidades[i]);

            //Correção para unidades que foram transformadas!!!
            if(idDasUnidades[i] == R.string.viscosidade_unidade_si || idDasUnidades[i] == R.string.rugosidade_unidade_si){ vetorValores[i] = vetorValores[i]*1000;}
            valor = ArmazenamentoDeDados.formatarValorNumerico(vetorValores[i], null);

            //Se for o tipo, temos que fazer algo a mais:
            if(idDasProps[i] == R.string.tipo_fluido_texto){
                unidade="";
                sigla="";
                //Determinar o "valor" dependendo do tipo:
                if(vetorValores[i] == troquinho.TIPOLIQ){
                    valor= "" + getResources().getString(R.string.liquido);
                }else if(vetorValores[i] == troquinho.TIPOGAS){
                    valor = "" + getResources().getString(R.string.gas);
                }else if(vetorValores[i] == troquinho.TIPOMETLIQ){
                    valor = "" + getResources().getString(R.string.metal_liquido);
                }

            }

            miniProp = new PropriedadeBasicaExibicao(sigla, nomeProp, unidade, valor);
            listosa.add(miniProp);
        }

        return listosa;
    }


    //A função abaixo gera as propriedades do metal
    public List<PropriedadeBasicaExibicao> gerarListaPropsMetalBitubular(TrocadorBitubularHeatex troquinho){
        PropriedadeBasicaExibicao miniProp;
        List<PropriedadeBasicaExibicao> listosa = new ArrayList<>();
        //Pega as propriedades do troquinho...

        String sigla, nomeProp, unidade, valor="";



        int[] idDasSiglas = troquinho.getCoisativos(0, 2);
        int[] idDasProps = troquinho.getCoisativos(1, 2);
        int[] idDasUnidades = troquinho.getCoisativos(2, 2);
        double[] vetorValores = troquinho.getListaPropsMetal();


        for(int i=0; i < idDasProps.length; i++){
            sigla = getResources().getString(idDasSiglas[i]);
            nomeProp = getResources().getString(idDasProps[i]);
            unidade = getResources().getString(idDasUnidades[i]);

            //No caso de diâmetros ou coisas que são mm multiplica por mil...
            if(idDasUnidades[i] == R.string.diametro_unidade_si || idDasUnidades[i] == R.string.rugosidade_unidade_si){ vetorValores[i] = vetorValores[i]*1000;}

            valor = ArmazenamentoDeDados.formatarValorNumerico(vetorValores[i], null);


            miniProp = new PropriedadeBasicaExibicao(sigla, nomeProp, unidade, valor);
            listosa.add(miniProp);
        }

        //e bota na lista...
        return listosa;
    }

    //A função abaixo gera as de projeto - se é CC ou Paralelo, deltaP, etc
    public List<PropriedadeBasicaExibicao> gerarListaPropsProjetoBitubular(TrocadorBitubularHeatex troquinho){
        String orientosa, retornosa;
        PropriedadeBasicaExibicao miniProp;
        List<PropriedadeBasicaExibicao> listosa = new ArrayList<>();

        String sigla, nomeProp, unidade, valor="", hi_pre_determinado="", ho_pre_determinado="";
        if(troquinho.isH_PreDeterminado(1)){ hi_pre_determinado = getResources().getString(R.string.sim); }
        else{hi_pre_determinado = getResources().getString(R.string.nao);}
        if(troquinho.isH_PreDeterminado(2)){ ho_pre_determinado = getResources().getString(R.string.sim); }
        else{ho_pre_determinado = getResources().getString(R.string.nao);}

        //Adiciona o tipo de trocador
        int tipoDeTrocador = troquinho.getIdDoTipoDeTrocador();
        //Agora transforma essa id na que é do texto, não na do trocador!!
        switch (tipoDeTrocador){
            case TrocadorBitubularHeatex.BITUBULAR:
                tipoDeTrocador=R.string.trocador_bitubular;
                break;
            case TrocadorBitubularHeatex.MULTITUBULAR:
                tipoDeTrocador=R.string.trocador_de_calor_multitubular_hairpin;
                break;
            case TrocadorBitubularHeatex.CASCOETUBOS:
                tipoDeTrocador=R.string.trocador_casco_e_tubos;
                break;
            default:
                tipoDeTrocador=R.string.erro_alerta;
                break;
        }



        //Agora bota isso no texto
        miniProp = new PropriedadeBasicaExibicao("", getResources().getString(R.string.tipo_de_trocador), "", getResources().getString(tipoDeTrocador));
        listosa.add(miniProp);

        //Adiciona o comprimento total do trocador:
        miniProp = new PropriedadeBasicaExibicao(getString(R.string.comprimento_L_sigla), getResources().getString(R.string.comprimento_acumulado), getString(R.string.comprimento_maximo_unidade_si), ""+troquinho.getComprimentoTotal());
        listosa.add(miniProp);

        //Adiciona a orientação!
        if(troquinho.getOrientacao() == troquinho.ORIENTACAOCONTRACORRENTE){ orientosa = getResources().getString(R.string.contra_corrente); }else{orientosa = getResources().getString(R.string.paralelo);}
        miniProp = new PropriedadeBasicaExibicao("", getResources().getString(R.string.orientacao_texto), "", orientosa);
        listosa.add(miniProp);

        //Adiciona o tipo de retorno
        if(troquinho.getTipoReturn() == troquinho.BONNET_TYPE_RETURN){ retornosa = getResources().getString(R.string.bonnet_type_return_texto); }else{retornosa = getResources().getString(R.string.straight_type_return_texto);}
        miniProp = new PropriedadeBasicaExibicao("", getResources().getString(R.string.tipo_retorno_anulo_texto), "", retornosa);
        listosa.add(miniProp);


        int[] idDasSiglas = troquinho.getCoisativos(0, 3);
        int[] idDasProps = troquinho.getCoisativos(1, 3);
        int[] idDasUnidades = troquinho.getCoisativos(2, 3);
        System.out.println(idDasProps.length);
        System.out.println(idDasSiglas.length);
        System.out.println(idDasUnidades.length);
        double[] vetorValores = troquinho.getListaPropriedadesTrocador();


        for(int i=0; i < idDasProps.length; i++){
            sigla = getResources().getString(idDasSiglas[i]);
            nomeProp = getResources().getString(idDasProps[i]);
            unidade = getResources().getString(idDasUnidades[i]);
            valor = ArmazenamentoDeDados.formatarValorNumerico(vetorValores[i], null);


            //No caso de diâmetros ou coisas que são mm multiplica por mil...
            if(idDasUnidades[i] == R.string.diametro_unidade_si || idDasUnidades[i] == R.string.rugosidade_unidade_si){ vetorValores[i] = vetorValores[i]*1000;}


            miniProp = new PropriedadeBasicaExibicao(sigla, nomeProp, unidade, valor);
            listosa.add(miniProp);

            //Se tiver adicionado o hi ou ho, coloca logo informações sobre eles!!!!!!
            if(idDasSiglas[i] == R.string.hi_sigla){
                if(troquinho.isH_PreDeterminado(1)){ //Só bota se não for uma não né que n tem nem sentido zz
                    miniProp = new PropriedadeBasicaExibicao("", getResources().getString(R.string.hi_pre_determinada), "", hi_pre_determinado);
                    listosa.add(miniProp);
                }
                //Exibe a correlação empregada
                Boolean deciso = troquinho.forcarCorrelacaoTubo;
                if(deciso){
                    String textineo = getResources().getString(R.string.sim);
                    //Diz se a correlação foi forçada ou não
                    miniProp = new PropriedadeBasicaExibicao("", getResources().getString(R.string.correlacao_forcada), "", textineo);
                    listosa.add(miniProp);
                }
                //Diz a correlação empregada
                String textineo = troquinho.getCorrelacaoNome(troquinho.TUBO);
                miniProp = new PropriedadeBasicaExibicao("", getResources().getString(R.string.correlacao_empregada_tubo), "", textineo);
                listosa.add(miniProp);

            }
            if(idDasSiglas[i] == R.string.ho_sigla){
                if(troquinho.isH_PreDeterminado(2)){
                    miniProp = new PropriedadeBasicaExibicao("", getResources().getString(R.string.ho_predeterminada), "", ho_pre_determinado);
                    listosa.add(miniProp);
                }

                //Exibe a correlação empregada
                Boolean deciso = troquinho.forcarCorrelacaoAnulo;
                if(deciso){
                    String textineo = getResources().getString(R.string.sim);
                    //Diz se a correlação foi forçada ou não
                    miniProp = new PropriedadeBasicaExibicao("", getResources().getString(R.string.correlacao_forcada), "", textineo);
                    listosa.add(miniProp);
                }

                //Diz a correlação empregada
                String textineo = troquinho.getCorrelacaoNome(troquinho.ANULO);
                miniProp = new PropriedadeBasicaExibicao("", getResources().getString(R.string.correlacao_empregada_anulo), "", textineo);
                listosa.add(miniProp);

            }

        }


        return listosa;
    }


    public LinearLayout criarMiniLinLay(String titulo, List<PropriedadeBasicaExibicao> listonilda, Context context){

        //Roubei da internet essa conversão de pixel pra dp
        int paddingDesejado = 16; //8 dp de padding!
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (paddingDesejado*scale + 0.5f );

        //Linear layout que armazena a view:
        LinearLayout linearLayoutInterno = new LinearLayout(context);
        linearLayoutInterno.setOrientation(LinearLayout.VERTICAL);

        //TextView de título:
        TextView textView = new TextView(context);
        textView.setText(titulo);
        textView.setTextAppearance(context, R.style.TextoGrangeTitulo);
        textView.setPadding(dpAsPixels, ((int) 1*dpAsPixels),  dpAsPixels, dpAsPixels);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //RecyclerView que exibe as coisas:
        RecyclerView recyclerView = new RecyclerView(context);
        adapter = new propriedadesExibicaoSimplesAdapter(getApplicationContext(), listonilda);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        linearLayoutInterno.addView(textView);
        linearLayoutInterno.addView(recyclerView);

        return linearLayoutInterno;
    }


    public void criarAlertaInserirNomeProjeto(){
        final ScrollView scrollView = new ScrollView(getApplicationContext());
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //Criando o linear layout
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.add_info_layout, null);

        //Coloca um onClick que muda o nome do projeto!
        final EditText editText = linearLayout.findViewById(R.id.nomearEditText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nome = s.toString();
                projetoExibido.setNome(nome);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        scrollView.addView(linearLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(exibirDadosProjetoUnicoActivity.this);
        //builder.setMessage(R.string.mensagem_erro_calores_errados_trocador)
        builder.setView(scrollView)
                .setTitle(R.string.nome_do_projeto)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        projetoExibido.setNome(editText.getText().toString());
                        ArmazenamentoDeDados.adicionarItemListaDeProjetos(getApplicationContext(), projetoExibido);
                        salvarButton.setClickable(false); //deixa de ser clicável pra não add mais de uma vez!
                        salvarButton.setAlpha((float)0.2);
                        //salvarButton.setBackgroundColor(getResources().getColor(R.color.colorCinzaClaro));
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

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
        return true;
    }

}
