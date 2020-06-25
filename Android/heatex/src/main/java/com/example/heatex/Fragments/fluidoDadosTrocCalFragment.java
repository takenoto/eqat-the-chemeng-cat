package com.example.heatex.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heatex.Adapters.propriedadesBasicasTrocAdapter;
import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.PropriedadeBasica;
import com.example.heatex.Classes.TrocadorBitubularHeatex;
import com.example.heatex.R;

import java.util.ArrayList;
import java.util.List;

public class fluidoDadosTrocCalFragment extends Fragment {




    private List<PropriedadeBasica> listaProps1 = new ArrayList<>();
    private List<PropriedadeBasica> listaProps2 = new ArrayList<>();
    propriedadesBasicasTrocAdapter adapter1;
    propriedadesBasicasTrocAdapter adapter2;
    RecyclerView recyclerView1, recyclerView2;
    TextView textoValorTemperatura;
    EditText nomeFluidoTextView;
    AppCompatImageButton buttonCalcTemp;

    double Tmedia;


    //A ser modificado lá na activity, não aqui!!!
    int numFluido = 1; //Por padrão 1 só pra não dar ruim!!

    //Construtor:
    public fluidoDadosTrocCalFragment(int numFluido){
        //Verificação para também evitar problemas!!!
        if(numFluido==1 || numFluido == 2){this.numFluido = numFluido;}
        else{System.out.println("Você digitou um número que não era 1 ou 2, mas só são 2 fluidos :v MAS O QUE É ISSO MAS O QUE ESTÁ ACONTECENDO");}
    }

    static {        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle SavedInstance){

        //Primeira lista de propriedades
        //Criação dos adapters que usaremos:
        this.listaProps1 = gerarListaPropsMassaTemp();
        //A segunda em diante
        this.listaProps2 = gerarListaPropsFluido();


        adapter1 = new propriedadesBasicasTrocAdapter(getContext(), listaProps1, "fluido", numFluido);
        adapter2 = new propriedadesBasicasTrocAdapter(getContext(), listaProps2, "fluido", numFluido);


        //Infla o layout
        View rootView = inflater.inflate(R.layout.fragment_prop_liquido, container, false);



        //Arrumando o texto em que o usuário digita o nome do fluido!!
        nomeFluidoTextView = rootView.findViewById(R.id.nomeDoFluidoEditText);
        //Primeiro carrega os últimos digitados
        if(numFluido==1){nomeFluidoTextView.setText(ArmazenamentoDeDados.Fluido1);}
        else{nomeFluidoTextView.setText(ArmazenamentoDeDados.Fluido2);}
        //Depois deixa o edit text pronto para alterações
        nomeFluidoTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(numFluido==1){ ArmazenamentoDeDados.Fluido1 = editable.toString();}
                else{ArmazenamentoDeDados.Fluido2 = editable.toString();}

            }
        });


        //Adicionar listeners nos buttons:
        //AppCompatRadioButton radioButtonLiq = rootView.findViewById(R.id.radioButtonLiq);
        //AppCompatRadioButton radioButtonGas = rootView.findViewById(R.id.radioButtonGas);
        //AppCompatRadioButton radioButtonMetalLiq = rootView.findViewById(R.id.radioButtonMetalLiq);
        RadioGroup radioGroup1 = rootView.findViewById(R.id.radioGroupTipoFluido);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                AppCompatRadioButton radioButton = radioGroup.findViewById(checkedId);
                switch (checkedId){
                    case R.id.radioButtonLiq:{
                        if(numFluido==1){ ArmazenamentoDeDados.tipoFluido1 = TrocadorBitubularHeatex.TIPOLIQ; }
                        else{ ArmazenamentoDeDados.tipoFluido2 = TrocadorBitubularHeatex.TIPOLIQ; }
                        break;
                    }
                    case R.id.radioButtonGas:{
                        if(numFluido==1){ ArmazenamentoDeDados.tipoFluido1 = TrocadorBitubularHeatex.TIPOGAS; }
                        else{ ArmazenamentoDeDados.tipoFluido2 = TrocadorBitubularHeatex.TIPOGAS; }
                        break;
                    }
                    case R.id.radioButtonMetalLiq:{
                        if(numFluido==1){ ArmazenamentoDeDados.tipoFluido1 = TrocadorBitubularHeatex.TIPOMETLIQ; }
                        else{ ArmazenamentoDeDados.tipoFluido2 = TrocadorBitubularHeatex.TIPOMETLIQ; }
                        break;
                    }
                }

            }
        });


        //Marmotas que serão usadas para os recycler views funcionarem ok
        //Pega esse recycler e ajeita ele
        recyclerView1 = rootView.findViewById(R.id.props1RecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayout.VERTICAL));
        recyclerView1.setAdapter(adapter1);

        //Agora faz o segundo:
        recyclerView2 = rootView.findViewById(R.id.props2RecyclerView);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(rootView.getContext());
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.addItemDecoration(new DividerItemDecoration(rootView.getContext(), LinearLayout.VERTICAL));
        recyclerView2.setAdapter(adapter2);


        //Resolvendo o botão de calcular as besteiras
        //Método do botão de calcular T média
        //Metodo para o botão de calcular temperatura
        textoValorTemperatura = rootView.findViewById(R.id.tempMediaValorTextView);
        buttonCalcTemp = rootView.findViewById(R.id.tempMediaButton);

        buttonCalcTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numFluido==1){
                    Tmedia = (ArmazenamentoDeDados.valoresFluido1[1] + ArmazenamentoDeDados.valoresFluido1[2])/2;
                    textoValorTemperatura.setText(Double.toString(Tmedia));
                } else{
                    Tmedia = (ArmazenamentoDeDados.valoresFluido2[1] + ArmazenamentoDeDados.valoresFluido2[2])/2;
                    textoValorTemperatura.setText(Double.toString(Tmedia));
                }
            }
        });




        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
    }



    //A ordem é: sigla, nome completo da prop, unidadeSI
    List<PropriedadeBasica> gerarListaPropsMassaTemp(){
        //Pode ser void pq estou preenchendo uma lista ja criada
        String sigla, texto, unidade;
        PropriedadeBasica miniProp;
        List<PropriedadeBasica> listaInterna = new ArrayList<>();

        //Salva os ids para serem abertos lá dentro do negócio
        int[] idSiglas = {R.string.mass_flux_sigla, R.string.T_in_sigla, R.string.T_out_sigla};
        int[] idTextos = {R.string.mass_flux_texto, R.string.T_in_texto, R.string.T_out_texto};
        int[] idUnidadesSI = {R.string.kg_segundo_sigla, R.string.kelvin_sigla, R.string.kelvin_sigla};

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

    //Segund parte: Agora cria as propriedades que o fluido recebe
    List<PropriedadeBasica> gerarListaPropsFluido(){
        String sigla, texto, unidade;
        PropriedadeBasica miniProp;
        List<PropriedadeBasica> listaInterna = new ArrayList<>();


        int[] idSiglas = {R.string.densidade_sigla, R.string.cp_sigla, R.string.k_condutividade_sigla, R.string.viscosidade_sigla, R.string.prandtl_sigla};
        int[] idTextos = {R.string.densidade_texto, R.string.cp_texto, R.string.k_condutividade_texto, R.string.viscosidade_texto, R.string.pr_texto};
        int[] idUnidadesSI = {R.string.densidade_unidade_si,R.string.cp_unidade_si, R.string.k_condutividade_unidade_si, R.string.viscosidade_unidade_si, R.string.pr_unidade_si};

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



}
