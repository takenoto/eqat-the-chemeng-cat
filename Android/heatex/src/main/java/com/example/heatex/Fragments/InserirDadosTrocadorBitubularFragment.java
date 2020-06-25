package com.example.heatex.Fragments;

import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Fade;
import androidx.transition.TransitionManager;

import com.example.heatex.Adapters.propriedadesBasicasTrocAdapter;
import com.example.heatex.Adapters.trocCalExibicaoAdapter;
import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.PropriedadeBasica;
import com.example.heatex.Classes.SpecHairpinMultitub;
import com.example.heatex.Classes.TrocadorBitubularHeatex;
import com.example.heatex.Classes.TuboHairpin;
import com.example.heatex.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InserirDadosTrocadorBitubularFragment extends Fragment {


    //Lista a serem usada nos inúmeros Recyclers!!
    private List<PropriedadeBasica> listaMaterial;
    private List<PropriedadeBasica> listaTubo;
    private List<PropriedadeBasica> listaAnulo;
    private List<PropriedadeBasica> listaDetalhesProjeto;
    private List<PropriedadeBasica> listaMudaFaseInterna;
    private List<PropriedadeBasica> listaMudaFaseExterna;
    private List<TuboHairpin> listaTubosBitubular;

    String tipo = "metalBitub";
    AppCompatImageButton buttonTrocarAlocacao;
    TextView fluidoTuboTextView, fluidoTuboGeralTextView, fluidoAnuloTextView, fluidoAnuloGeralTextView, coefSegValorTextView;
    AppCompatSeekBar porcentagemSegurancaSeekBar;

    LinearLayout diametrosOcultarLinearLayout, bitubularPadraoProjetoLinearLayout, demaisTrocsPadraoProjetoLinearLayout;
    SwitchCompat diametrosOcultarSwitchCompat;

    //Pegando todos os recycler views que vamos ter...
    RecyclerView materialTrocRecyclerView,tuboRecyclerView, anuloRecyclerView, detalhesProjetoRecyclerView, mudancaFaseInternoRecyclerView, mudancaFaseExternoRecyclerView;
    //E os adapters:
    propriedadesBasicasTrocAdapter adapter1, adapter2, adapter3, adapter4, adapter5, adapter6;

    AppCompatCheckBox hiConstanteCheckBox, hoConstanteCheckBox;

    AppCompatRadioButton paraleloRadioButton, ccRadioButton, bitubularRadioButton, multitubularRadioButton, cascoTubosRadioButton;

    AppCompatSpinner diametrosTuboSpinner, diametrosAnuloSpinner, padraoProjetoBitubularSeletorSpinner, padraoProjetoDemaisTrocadoresSeletorSpinner, correlacaoTuboSpinner, correlacaoAnuloSpinner;

    EditText numeroDeTubosEditText;

    //Para permitir usar vetores como recurso:
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }


        @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle SavedInstance){



        //Inicializando as listas que iremos usar:
        this.listaMaterial = gerarListaMaterial();
        this.listaTubo = gerarListaTubo();
        this.listaAnulo = gerarListaAnulo();
        this.listaDetalhesProjeto = gerarDetalhesDeProjeto();
        this.listaMudaFaseInterna = gerarMudaFaseInterna();
        this.listaMudaFaseExterna = gerarMudaFaseExterna();


        //Infla o layout
        final View rootView = inflater.inflate(R.layout.fragment_trocador_bitubular_usar_na_tabbed, container, false);


        //Achando cada view nos componentes:
        //LinearLayout
        bitubularPadraoProjetoLinearLayout = rootView.findViewById(R.id.bitubularPadraoProjetoLinearLayout);
        demaisTrocsPadraoProjetoLinearLayout = rootView.findViewById(R.id.demaisTrocsPadraoProjetoLinearLayout);
        numeroDeTubosEditText = rootView.findViewById(R.id.numeroDeTubosEditText);
        //Alocação
        buttonTrocarAlocacao = rootView.findViewById(R.id.trocarAlocacaoImageButton);
        fluidoTuboTextView = rootView.findViewById(R.id.fluidoDoTuboNomeTextView);
        fluidoAnuloTextView = rootView.findViewById(R.id.fluidoDoAnuloNomeTextView);
        fluidoTuboGeralTextView = rootView.findViewById(R.id.fluidoDoTuboGERALTextView);
        fluidoAnuloGeralTextView = rootView.findViewById(R.id.fluidoDoAnuloGERALTextView);
        //Linear layout dos diâmetros
        diametrosOcultarLinearLayout = rootView.findViewById(R.id.INTERNOdadosDeDiametroTrocadorlinearLayout);
        //Switch:
        diametrosOcultarSwitchCompat = rootView.findViewById(R.id.testeDiametroSwitch);
        //Seek bar:
        porcentagemSegurancaSeekBar = rootView.findViewById(R.id.coefSegurancaSeekBar);
        coefSegValorTextView = rootView.findViewById(R.id.coefSegurancaValorTextView);
        //Checkbox para hi e ho constantes:
        hiConstanteCheckBox = rootView.findViewById(R.id.hiEhConstanteCheckBox);
        hoConstanteCheckBox = rootView.findViewById(R.id.hoEhConstanteCheckBox);
        //Radio Button paralelo e cc
        paraleloRadioButton = rootView.findViewById(R.id.paraleloRadioButton);
        ccRadioButton = rootView.findViewById(R.id.contraCorrenteRadioButton);
        //RadioButton do tipo de trocador
        bitubularRadioButton = rootView.findViewById(R.id.trocadorBitubularRadioButton);
        multitubularRadioButton = rootView.findViewById(R.id.trocadorMultitubularRadioButton);
        cascoTubosRadioButton = rootView.findViewById(R.id.trocadorCascoETubosRadioButton);
        //Spinner
        AppCompatSpinner spinnerMaterial = rootView.findViewById(R.id.materialDoTrocadorSpinner);
        diametrosTuboSpinner = rootView.findViewById(R.id.tuboDiametroSelecSpinner2);
        diametrosAnuloSpinner = rootView.findViewById(R.id.anuloDiametroSelectSpinner3);
        padraoProjetoBitubularSeletorSpinner = rootView.findViewById(R.id.padraoProjetoBitubularSeletorSpinner);
        padraoProjetoDemaisTrocadoresSeletorSpinner = rootView.findViewById(R.id.padraoProjetoDemaisTrocadoresSeletorSpinner);
        correlacaoTuboSpinner = rootView.findViewById(R.id.coefConvecInternaHiSpinner);
        correlacaoAnuloSpinner = rootView.findViewById(R.id.coefConvecExternaHoSpinner);

        //Primeiro carrega o conteúdo dinamicamente:
        fluidoTuboTextView.setText(ArmazenamentoDeDados.Fluido1);
        fluidoAnuloTextView.setText(ArmazenamentoDeDados.Fluido2);

        //Diz se o fluido 1 está no tubo, e a função se encarrega do resto
        setarFluido1(ArmazenamentoDeDados.fluido1NoTuboですか);


        //Resolvendo o botão de troca
        //Em seguida se prepara para responder às alterações do usuário:
        buttonTrocarAlocacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Primeiro descobre se quem tá no tubo é o fluido 1 ou o 2:
                if(ArmazenamentoDeDados.fluido1NoTuboですか){
                    //Vamos trocar...Afinal é pra isso que tem esse método
                    ArmazenamentoDeDados.fluido1NoTuboですか = false;
                }else{ArmazenamentoDeDados.fluido1NoTuboですか=true;}


                setarFluido1(ArmazenamentoDeDados.fluido1NoTuboですか);

            }
        });



        //Associando cada um dos recyclers
        materialTrocRecyclerView = rootView.findViewById(R.id.materialDoTrocadorRecyclerView);
        tuboRecyclerView = rootView.findViewById(R.id.tuboInternoDiametrosRecyclerView);
        anuloRecyclerView = rootView.findViewById(R.id.anuloDiametrosRecyclerView);
        detalhesProjetoRecyclerView =  rootView.findViewById(R.id.detalhesProjetoRecyclerView);
        mudancaFaseInternoRecyclerView =  rootView.findViewById(R.id.mudancaDeFaseHiRecyclerView);
        mudancaFaseExternoRecyclerView = rootView.findViewById(R.id.mudancaDeFaseHoRecyclerView);



        //Escrevendo os adapters e...
        //Colocando os adapters dentro de cada recycler
        //1 - material
        adapter1 = new propriedadesBasicasTrocAdapter(getContext(), listaMaterial, tipo, 0);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(rootView.getContext());
        materialTrocRecyclerView.setLayoutManager(layoutManager1);
        materialTrocRecyclerView.setHasFixedSize(true);
        materialTrocRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayout.VERTICAL));
        materialTrocRecyclerView.setAdapter(adapter1);
        //2 - Tubo
        adapter2 = new propriedadesBasicasTrocAdapter(getContext(),listaTubo, tipo, 0);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(rootView.getContext());
        tuboRecyclerView.setLayoutManager(layoutManager2);
        tuboRecyclerView.setHasFixedSize(true);
        tuboRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayout.VERTICAL));
        tuboRecyclerView.setAdapter(adapter2);
        //3 - Ânulo
        adapter3 = new propriedadesBasicasTrocAdapter(getContext(),listaAnulo, tipo, 0);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(rootView.getContext());
        anuloRecyclerView.setLayoutManager(layoutManager3);
        anuloRecyclerView.setHasFixedSize(true);
        anuloRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayout.VERTICAL));
        anuloRecyclerView.setAdapter(adapter3);
        //4 - Detalhes
        adapter4 = new propriedadesBasicasTrocAdapter(getContext(),listaDetalhesProjeto, tipo, 0);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(rootView.getContext());
        detalhesProjetoRecyclerView.setLayoutManager(layoutManager4);
        detalhesProjetoRecyclerView.setHasFixedSize(true);
        detalhesProjetoRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayout.VERTICAL));
        detalhesProjetoRecyclerView.setAdapter(adapter4);
        //5 - mudança fase INTERNA
        adapter5 = new propriedadesBasicasTrocAdapter(getContext(),listaMudaFaseInterna, tipo, 0);
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(rootView.getContext());
        mudancaFaseInternoRecyclerView.setLayoutManager(layoutManager5);
        mudancaFaseInternoRecyclerView.setHasFixedSize(true);
        mudancaFaseInternoRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayout.VERTICAL));
        mudancaFaseInternoRecyclerView.setAdapter(adapter5);
        //6 - mudança fase EXTERNA
        adapter6 = new propriedadesBasicasTrocAdapter(getContext(),listaMudaFaseExterna, tipo, 0);
        RecyclerView.LayoutManager layoutManager6 = new LinearLayoutManager(rootView.getContext());
        mudancaFaseExternoRecyclerView.setLayoutManager(layoutManager6);
        mudancaFaseExternoRecyclerView.setHasFixedSize(true);
        mudancaFaseExternoRecyclerView.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayout.VERTICAL));
        mudancaFaseExternoRecyclerView.setAdapter(adapter6);




        //EditText do número de tubos
        numeroDeTubosEditText.setText(""+(int) ArmazenamentoDeDados.numeroDeTubos);
        numeroDeTubosEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String string = s.toString();


                if(string.contains(".")){
                    //Se contiver um ponto, avisa o usuário criando um snackbar
                    double num = Double.parseDouble(string);
                    int numeroInteiro = (int) num;
                    string = "" + numeroInteiro;
                    //string= string.replaceAll(".","");
                    Snackbar.make(rootView, getResources().getString(R.string.numero_de_tubos_precisa_ser_inteiro), Snackbar.LENGTH_LONG)
                            .show();

                }

                if(string!="" && string!=null && string!=" "){
                    string = "0" + string; //pra ser pelo menos zero
                    ArmazenamentoDeDados.numeroDeTubos = Double.parseDouble(string);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Spinner da escolha do metal:
        ArrayAdapter<CharSequence> adapterSpinnerMaterial = ArrayAdapter.createFromResource(rootView.getContext(), R.array.materiais_troc_bitub_array, android.R.layout.simple_spinner_item);
        adapterSpinnerMaterial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaterial.setAdapter(adapterSpinnerMaterial);
        spinnerMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //k(condutividade)
                ArmazenamentoDeDados.valoresMetalBit[0] = ArmazenamentoDeDados.valoresPadraoCondutividade[i];
                //e(rugosidade)
                ArmazenamentoDeDados.valoresMetalBit[1] = ArmazenamentoDeDados.valoresPadraoRugosidade[i];
                //Refaz o adapter e o recycler!
                adapter1 = new propriedadesBasicasTrocAdapter(getContext(), listaMaterial, tipo, 0);
                materialTrocRecyclerView.setAdapter(adapter1);
                materialTrocRecyclerView.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Código so switch, que pode fazer os diâmetros aparecem e sumirem
        //Por padrão, sempre testa todos:
        diametrosOcultarSwitchCompat.setChecked(ArmazenamentoDeDados.testarTodosOsDiametros);
        //E portanto esconde a view caso seja testar todos:
        if(ArmazenamentoDeDados.testarTodosOsDiametros){diametrosOcultarLinearLayout.setVisibility(View.GONE);}

        //Carrega a última configuração.. não tem muito sentido agora né zzz
        Boolean switchState =  diametrosOcultarSwitchCompat.isChecked(); //checa como está o switch



        diametrosOcultarSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Se for true, some com o linear layout:
                if(b){
                    ArmazenamentoDeDados.testarTodosOsDiametros = true;
                    Animation animation = new TranslateAnimation(0, 900, 0, 0);
                    animation.setDuration(300);
                    animation.setFillAfter(false);
                    diametrosOcultarLinearLayout.startAnimation(animation);
                    diametrosOcultarLinearLayout.setVisibility(View.GONE);

                    //Some com as opções em relação a diâmetros
                    demaisTrocsPadraoProjetoLinearLayout.setVisibility(View.GONE);


                }
                else{ //Se for false, reaparece!
                    ArmazenamentoDeDados.testarTodosOsDiametros = false;
                    Animation animation = new TranslateAnimation(900, 0, 0, 0);
                    animation.setDuration(500);
                    animation.setFillAfter(true);
                    diametrosOcultarLinearLayout.startAnimation(animation);
                    diametrosOcultarLinearLayout.setVisibility(View.VISIBLE);

                    //Reaparece com as opções, mas apenas caso seja o tipo delas né zzz
                    if(ArmazenamentoDeDados.tipoDeTrocador==ArmazenamentoDeDados.MULTITUBULAR||ArmazenamentoDeDados.tipoDeTrocador==ArmazenamentoDeDados.CASCOETUBOS){
                        demaisTrocsPadraoProjetoLinearLayout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });



        //Código da barra da % da margem de segurança SEEKBAR COEFICIENTE DE SEGURANÇA
        //Coloca eles pra iniciar nos valores padrão:
        porcentagemSegurancaSeekBar.setProgress((int) (10*ArmazenamentoDeDados.percentualSeguranca));
        coefSegValorTextView.setText(String.format(Locale.US,"%.1f",(double) porcentagemSegurancaSeekBar.getProgress()/10));
        porcentagemSegurancaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        int corAntiga = coefSegValorTextView.getCurrentTextColor();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                double progress = (double) seekBar.getProgress();
                ArmazenamentoDeDados.percentualSeguranca = progress/10;
                coefSegValorTextView.setText(String.format(Locale.US,"%.1f", ArmazenamentoDeDados.percentualSeguranca));
                coefSegValorTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                coefSegValorTextView.setTextColor(corAntiga);
            }
        });


        /*--------------------------------------------------
        --------------------CHECKBOX------------------------
         ---------------------------------------------------*/
        //Código dos checkbox:
            //Primeiro some com as coisas se eles tiverem desligados!
            if(!hiConstanteCheckBox.isChecked()){
                mudancaFaseInternoRecyclerView.setVisibility(View.GONE);
            }
            if(!hoConstanteCheckBox.isChecked()){
                mudancaFaseExternoRecyclerView.setVisibility(View.GONE);
            }

            //Interno:
            hiConstanteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ArmazenamentoDeDados.hはConstantesですか[0] = b;
                    ArmazenamentoDeDados.hiConstante = b;
                    if(b){
                        mudancaFaseInternoRecyclerView.setVisibility(View.VISIBLE);
                    }else{
                        ArmazenamentoDeDados.hiConstante = false;
                        mudancaFaseInternoRecyclerView.setVisibility(View.GONE);
                    }
                }
            });


            //Externo:
            hoConstanteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    ArmazenamentoDeDados.hはConstantesですか[1] = b;
                    ArmazenamentoDeDados.hoConstante = b;
                    if(b){
                        mudancaFaseExternoRecyclerView.setVisibility(View.VISIBLE);
                    }else{
                        ArmazenamentoDeDados.hoConstante = false;
                        mudancaFaseExternoRecyclerView.setVisibility(View.GONE);
                    }
                }
            });

            //-------------------------------------------------
            //------------------RADIO BUTTON-------------------
            //Paralelo e CC:
            ccRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){ ArmazenamentoDeDados.testarCC = true; }
                    else{ArmazenamentoDeDados.testarCC = false; }
                    ArmazenamentoDeDados.marcarOrientacao();
                }
            });
            paraleloRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){ ArmazenamentoDeDados.testarParalelo = true; }
                    else{ArmazenamentoDeDados.testarParalelo = false; }
                    ArmazenamentoDeDados.marcarOrientacao();
                }
            });


            //Tipo de retorno:
            final RadioGroup radioGroup1 = rootView.findViewById(R.id.tipoReturnRadioGroup);
            radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    AppCompatRadioButton radioButton = radioGroup.findViewById(checkedId);
                    switch (checkedId){
                        case R.id.bonnetTypeRadioButton:{
                            ArmazenamentoDeDados.tipoReturn = TrocadorBitubularHeatex.BONNET_TYPE_RETURN;
                            break;
                        }
                        case R.id.straightTypeRadioButton:{
                            ArmazenamentoDeDados.tipoReturn = TrocadorBitubularHeatex.STRAIGHT_PIPE_RETURN;
                            break;
                        }
                    }

                }
            });

            ccRadioButton.setChecked(true); //por padrão o cc é ligado!
            ArmazenamentoDeDados.testarCC = true;

            //RadioButton do TIPO DE TROCADOR BIT MULTI CASCO
            RadioGroup radioGroup2 = rootView.findViewById(R.id.tipoDeTrocGrupoRadioButton);
            radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    AppCompatRadioButton radioButton = radioGroup1.findViewById(checkedId);
                    switch (checkedId){
                        case R.id.trocadorBitubularRadioButton:{
                            ArmazenamentoDeDados.tipoDeTrocador = ArmazenamentoDeDados.BITUBULAR;
                            diametrosTuboSpinner.setVisibility(View.VISIBLE);
                            diametrosAnuloSpinner.setVisibility(View.VISIBLE);
                            bitubularPadraoProjetoLinearLayout.setVisibility(View.VISIBLE);
                            demaisTrocsPadraoProjetoLinearLayout.setVisibility(View.GONE);
                            //Carregando os diâmetros dinamicamente caso seja um bitubular:
                            //Cria a coisa e coloca ela dentro dos spinners do Di
                            ArmazenamentoDeDados.numeroDeTubos=1; // bitubular só é 1 tubo zz
                            carregarSpinnerPadraoProjeto(); //Essa função já resolve tudo por si só!
                            //As outras duas ficam dentro dela!!!


                            break;
                        }
                        case R.id.trocadorMultitubularRadioButton:{
                            ArmazenamentoDeDados.tipoDeTrocador = ArmazenamentoDeDados.MULTITUBULAR;
                            diametrosTuboSpinner.setVisibility(View.GONE);
                            diametrosAnuloSpinner.setVisibility(View.GONE);
                            bitubularPadraoProjetoLinearLayout.setVisibility(View.GONE);
                            demaisTrocsPadraoProjetoLinearLayout.setVisibility(View.VISIBLE);
                            carregarSpinnerMultitubular();
                            break;
                        }
                        case R.id.trocadorCascoETubosRadioButton:{
                            ArmazenamentoDeDados.tipoDeTrocador = ArmazenamentoDeDados.CASCOETUBOS;
                            diametrosTuboSpinner.setVisibility(View.GONE);
                            diametrosAnuloSpinner.setVisibility(View.GONE);
                            bitubularPadraoProjetoLinearLayout.setVisibility(View.GONE);
                            demaisTrocsPadraoProjetoLinearLayout.setVisibility(View.VISIBLE);
                            //TODO implementar aqui o spinner do casco e tubos, reusa o do multitubular eu acho??
                            break;
                        }
                    }
                }
            });

            //Por padrão é o bitubular
            bitubularRadioButton.setChecked(true);
            ArmazenamentoDeDados.tipoDeTrocador = ArmazenamentoDeDados.BITUBULAR;

            //Carrega o seletor de correlações
            carregarSpinnerDeCorrelacoes();


            return rootView;

    }










    //MÉTODOS QUE GERAM AS LISTAS USADAS PARA OS ADAPTERS

    List<PropriedadeBasica> gerarListaMaterial(){

        String sigla, texto, unidade;
        PropriedadeBasica miniProp;
        List<PropriedadeBasica> listaInterna = new ArrayList<>();

        int[] idSiglas = {R.string.k_condutividade_metal_sigla, R.string.rugosidade_sigla};
        int[] idTextos = {R.string.k_condutividade_texto, R.string.rugosidade_texto};
        int[] idUnidadesSI = {R.string.k_condutividade_unidade_si, R.string.rugosidade_unidade_si};

        for(int i=0; i<idSiglas.length; i++) {
            //Roda o vetor de nomes siglas etc!
            sigla = getResources().getString(idSiglas[i]);
            texto = getResources().getString(idTextos[i]);
            unidade = getResources().getString(idUnidadesSI[i]);
            miniProp = new PropriedadeBasica(sigla, texto, unidade, idSiglas[i]);
            listaInterna.add(miniProp);
        }
        return listaInterna;
    }


    List<PropriedadeBasica> gerarListaTubo(){

        String sigla, texto, unidade;
        PropriedadeBasica miniProp;
        List<PropriedadeBasica> listaInterna = new ArrayList<>();

        int[] idSiglas = {R.string.diametro_interno_tubo_sigla, R.string.diametro_externo_tubo_sigla};
        int[] idTextos = {R.string.diametro_interno_tubo_texto, R.string.diametro_externo_tubo_texto};
        int[] idUnidadesSI = {R.string.diametro_unidade_si, R.string.diametro_unidade_si};

        for(int i=0; i<idSiglas.length; i++) {
            //Roda o vetor de nomes siglas etc!
            sigla = getResources().getString(idSiglas[i]);
            texto = getResources().getString(idTextos[i]);
            unidade = getResources().getString(idUnidadesSI[i]);
            miniProp = new PropriedadeBasica(sigla, texto, unidade, idSiglas[i]);
            listaInterna.add(miniProp);
        }
        return listaInterna;
    }


    List<PropriedadeBasica> gerarListaAnulo(){
        String sigla, texto, unidade;
        PropriedadeBasica miniProp;
        List<PropriedadeBasica> listaInterna = new ArrayList<>();

        int[] idSiglas = {R.string.diametro_interno_anulo_sigla, R.string.diametro_externo_anulo_sigla};
        int[] idTextos = {R.string.diametro_interno_anulo_texto, R.string.diametro_externo_anulo_texto};
        int[] idUnidadesSI = {R.string.diametro_unidade_si, R.string.diametro_unidade_si};

        for(int i=0; i<idSiglas.length; i++) {
            //Roda o vetor de nomes siglas etc!
            sigla = getResources().getString(idSiglas[i]);
            texto = getResources().getString(idTextos[i]);
            unidade = getResources().getString(idUnidadesSI[i]);
            miniProp = new PropriedadeBasica(sigla, texto, unidade, idSiglas[i]);
            listaInterna.add(miniProp);
        }
        return listaInterna;
    }


    List<PropriedadeBasica> gerarDetalhesDeProjeto(){
        String sigla, texto, unidade;
        PropriedadeBasica miniProp;
        List<PropriedadeBasica> listaInterna = new ArrayList<>();

        int[] idSiglas = {R.string.comprimento_maximo_sigla, R.string.fouling_interno_sigla, R.string.fouling_externo_sigla};
        int[] idTextos = {R.string.comprimento_maximo_texto, R.string.fouling_interno_texto, R.string.fouling_externo_texto};
        int[] idUnidadesSI = {R.string.comprimento_maximo_unidade_si, R.string.fouling_interno_unidade_si, R.string.fouling_externo_unidade_si};

        for(int i=0; i<idSiglas.length; i++) {
            //Roda o vetor de nomes siglas etc!
            sigla = getResources().getString(idSiglas[i]);
            texto = getResources().getString(idTextos[i]);
            unidade = getResources().getString(idUnidadesSI[i]);
            miniProp = new PropriedadeBasica(sigla, texto, unidade, idSiglas[i]);
            listaInterna.add(miniProp);
        }
        return listaInterna;
    }

    List<PropriedadeBasica> gerarMudaFaseInterna(){
        String sigla, texto, unidade;
        PropriedadeBasica miniProp;
        List<PropriedadeBasica> listaInterna = new ArrayList<>();

        int idSiglas = R.string.hi_sigla;
        int idTextos = R.string.hi_texto;
        int idUnidadesSI = R.string.hi_unidade_si;

        sigla = getResources().getString(idSiglas);
        texto = getResources().getString(idTextos);
        unidade = getResources().getString(idUnidadesSI);
        miniProp = new PropriedadeBasica(sigla, texto, unidade, idSiglas);
        listaInterna.add(miniProp);

        return listaInterna;
    }

    List<PropriedadeBasica> gerarMudaFaseExterna(){
        String sigla, texto, unidade;
        PropriedadeBasica miniProp;
        List<PropriedadeBasica> listaInterna = new ArrayList<>();

        int idSiglas = R.string.ho_sigla;
        int idTextos = R.string.ho_texto;
        int idUnidadesSI = R.string.ho_unidade_si;

        sigla = getResources().getString(idSiglas);
        texto = getResources().getString(idTextos);
        unidade = getResources().getString(idUnidadesSI);
        miniProp = new PropriedadeBasica(sigla, texto, unidade, idSiglas);
        listaInterna.add(miniProp);

        return listaInterna;
    }



    private void setarFluido1(boolean noTubo){
        if(noTubo) {
            fluidoTuboTextView.setText(ArmazenamentoDeDados.Fluido1);
            fluidoTuboGeralTextView.setText(R.string.fluido1);
            fluidoAnuloTextView.setText(ArmazenamentoDeDados.Fluido2);
            fluidoAnuloGeralTextView.setText(R.string.fluido2);
        }else{
            fluidoTuboTextView.setText(ArmazenamentoDeDados.Fluido2);
            fluidoTuboGeralTextView.setText(R.string.fluido2);
            fluidoAnuloTextView.setText(ArmazenamentoDeDados.Fluido1);
            fluidoAnuloGeralTextView.setText(R.string.fluido1);
        }

    }


    final int TUBO=0, ANULO=1;
    //Cria e popula o spinner. É mais fácil como função mesmo!
    private void carregarSpinnerDiametros(AppCompatSpinner spinner, List<TuboHairpin> tubosSsS, final int TuboOuAnulo){

        //o "tipo" diz se vai ser o spinner do Di, do Do, etc

        final List<TuboHairpin> tubos;
        tubos = tubosSsS;

        //ArrayAdapter<CharSequence> adapterSpinnerMaterial = ArrayAdapter.createFromResource(getContext(), R.array.materiais_troc_bitub_array, android.R.layout.simple_spinner_item);
        ArrayList<String> spinnerArray = new ArrayList<String>();

        for(int i=0; i<tubos.size(); i++){
            spinnerArray.add(tubos.get(i).gerarTextoParaSpinner(0));
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(TuboOuAnulo==TUBO){
                    //2 - Tubo
                    ArmazenamentoDeDados.valoresMetalBit[2] = tubos.get(i).getDi(0);
                    ArmazenamentoDeDados.valoresMetalBit[3] = tubos.get(i).getDo(0);
                    adapter2 = new propriedadesBasicasTrocAdapter(getContext(),listaTubo, tipo, 0);
                    tuboRecyclerView.setAdapter(adapter2);
                    tuboRecyclerView.invalidate();
                }

                if(TuboOuAnulo==ANULO) {
                    //3 - Ânulo
                    ArmazenamentoDeDados.valoresMetalBit[4] = tubos.get(i).getDi(0);
                    ArmazenamentoDeDados.valoresMetalBit[5] = tubos.get(i).getDo(0);
                    adapter3 = new propriedadesBasicasTrocAdapter(getContext(), listaAnulo, tipo, 0);
                    anuloRecyclerView.setAdapter(adapter3);
                    anuloRecyclerView.invalidate();
                }

                if(!ArmazenamentoDeDados.testarTodosOsDiametros){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(tubos.get(i).gerarTextoParaSpinner(0))
                            .setTitle(R.string.trocador_bitubular)
                            .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void carregarSpinnerMultitubular(){
        //padraoProjetoDemaisTrocadoresSeletorSpinner
        final List<SpecHairpinMultitub> tubos = ArmazenamentoDeDados.tubosMultitub;
        ArrayList<String> spinnerArray = new ArrayList<String>();

        //Preenche
        for(int i=0; i<tubos.size(); i++){
            spinnerArray.add(tubos.get(i).gerarTextoParaSpinner(0));
        }


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        padraoProjetoDemaisTrocadoresSeletorSpinner.setAdapter(spinnerArrayAdapter);

        padraoProjetoDemaisTrocadoresSeletorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                numeroDeTubosEditText.setText(""+ tubos.get(position).tubes);

                //Atualiza os dados na classe estática global
                ArmazenamentoDeDados.valoresMetalBit[2] = tubos.get(position).IDmm;
                ArmazenamentoDeDados.valoresMetalBit[3] = tubos.get(position).ODmm;
                ArmazenamentoDeDados.valoresMetalBit[4] = Double.parseDouble(tubos.get(position).DNmm.replace(",","."));
                ArmazenamentoDeDados.valoresMetalBit[5] = Double.parseDouble(tubos.get(position).DNmm.replace(",","."));

                //Só exibe o diálogo caso não esteja testando todos!
                if(!ArmazenamentoDeDados.testarTodosOsDiametros){

                    ArmazenamentoDeDados.numeroDeTubos = tubos.get(position).tubes;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(tubos.get(position).gerarTextoParaSpinner(0))
                            .setTitle(R.string.trocador_de_calor_multitubular_hairpin)
                            .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


                //Preenche com os dados novos
                //Tubo:
                adapter2 = new propriedadesBasicasTrocAdapter(getContext(),listaTubo, tipo, 0);
                tuboRecyclerView.setAdapter(adapter2);
                tuboRecyclerView.invalidate();
                //Ânulo:
                adapter3 = new propriedadesBasicasTrocAdapter(getContext(),listaAnulo, tipo, 0);
                anuloRecyclerView.setAdapter(adapter3);
                anuloRecyclerView.invalidate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void carregarSpinnerPadraoProjeto(){


        final ArrayList<String> spinnerArray = new ArrayList<String>();
        //preenche com os dados
        for(String string : ArmazenamentoDeDados.SCHEDULEDESIGNATIONBITUB){
            if(string==null){
                //Se for um nulo, é pq quer exibir todos
                string = getResources().getString(R.string.exibir_todos);
            }
            spinnerArray.add(string);
        }


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        padraoProjetoBitubularSeletorSpinner.setAdapter(spinnerArrayAdapter);

        //Edita as opções disponíveis dinamicamente com base no selecionado:
        padraoProjetoBitubularSeletorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArmazenamentoDeDados.padraoDeProjetoBitubular = position;
                listaTubosBitubular = ArmazenamentoDeDados.getListaFiltradaAtual();
                carregarSpinnerDiametros(diametrosTuboSpinner, listaTubosBitubular, TUBO);
                carregarSpinnerDiametros(diametrosAnuloSpinner, listaTubosBitubular, ANULO);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void carregarSpinnerCascoETubos(){

    }


    private void carregarSpinnerDeCorrelacoes(){
        //Se for bi ou multi, carrega as correlações padrão nos 2 negócios, excluindo GNIELINSK pro tubo e deixando pro anulo
        if(ArmazenamentoDeDados.tipoDeTrocador==ArmazenamentoDeDados.BITUBULAR || ArmazenamentoDeDados.tipoDeTrocador==ArmazenamentoDeDados.MULTITUBULAR){
            String[] listaCorrelacoes = ArmazenamentoDeDados.nomeCorrelacoesBitEMulti;
            ArrayList<String> descricaoCorrelacoes = new ArrayList<>();
            ArrayList<String> spinnerArray = new ArrayList<String>();

            //Primeiro adiciona a primeira com o nome pra escolher todas(posição 0)
            spinnerArray.add(getResources().getString(R.string.automatico));
            //Preenche com o nome das correlações
            for(int i=1; i<listaCorrelacoes.length; i++){
                spinnerArray.add(listaCorrelacoes[i]);
            }


            //TODO usar a descrição das correlações pra algo...
            descricaoCorrelacoes = gerarDescricaoCorrelacoes(listaCorrelacoes);

            ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
            spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            correlacaoTuboSpinner.setAdapter(spinnerArrayAdapter1);

            ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
            spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            correlacaoAnuloSpinner.setAdapter(spinnerArrayAdapter2);


            correlacaoTuboSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){
                        ArmazenamentoDeDados.forcarCorrelacaoTubo=false;
                    }else{
                        ArmazenamentoDeDados.forcarCorrelacaoTubo=true;
                        if(position!=TrocadorBitubularHeatex.GNIELISNK) {ArmazenamentoDeDados.correlacaoForcadaTubo = position;}
                        else{correlacaoTuboSpinner.setSelection(0);
                            Snackbar.make(view, getResources().getString(R.string.correlacao_nao_pode_para_tubo), Snackbar.LENGTH_LONG)
                                    .show();}

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            correlacaoAnuloSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){
                        ArmazenamentoDeDados.forcarCorrelacaoAnulo=false;
                    }else{
                        ArmazenamentoDeDados.correlacaoForcadaAnulo = position;
                        ArmazenamentoDeDados.forcarCorrelacaoAnulo = true;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    //TODO fazer a descrição das correlações
    private ArrayList<String> gerarDescricaoCorrelacoes(String[] correlacoes){
        ArrayList<String> listaDescricoes = new ArrayList<>();

        //Adiciona a descrição de cada correlação:
        for(int i=0; i<correlacoes.length; i++){
            listaDescricoes.add("DESCRICAO PADRAO 1");
        }


        return listaDescricoes;
    }
}


