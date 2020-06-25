package com.example.heatex.Adapters;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.PropriedadeBasica;
import com.example.heatex.R;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class propriedadesBasicasTrocAdapter extends RecyclerView.Adapter<propriedadesBasicasTrocAdapter.MyViewHolder> {

    Context context;

    //Declaração de variáveis:
    private List<PropriedadeBasica> listaDePropriedades;
    private List<String> vetorValores = new ArrayList<>(); //Vou armazenar aqui o texto inserido pelo usuráio!!
    //Armazena cada id pra que saibamos o que é o quê na hora de salvar!!!
    private final int idDasPropriedades[] = ArmazenamentoDeDados.idDasPropriedades;
    private  final int idDasPropriedadesMetalBit[] = ArmazenamentoDeDados.idDasPropriedadesMETALBIT;

    //Para que saibamos se é o primeiro ou o segundo fluido:
    private final int idFluido[] = {1,2};
    private int numFluido;
    private String tipo;
    //public String textoDigitadoPeloUser;


    public propriedadesBasicasTrocAdapter(Context context, List<PropriedadeBasica> lista, String tipo, int numFluido) {
        this.context = context;
        this.listaDePropriedades = lista;

        //Preenche a lista com "0" em tudo... quando o usuário mexer, altera depois
        if(vetorValores.size()==0){
            for(int i=0; i<lista.size(); i++){ vetorValores.add(""); }
        }
        //else{ this.vetorValores = vetorVals;}

        this.numFluido = numFluido;
        this.tipo = tipo;

    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.propriedades_basicas_adapter, parent, false);

        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final PropriedadeBasica propBas = listaDePropriedades.get( position );
        holder.sigla.setText( propBas.getSiglaPropriedade() );
        holder.nomePropriedade.setText( propBas.getNomePropriedade());
        holder.unidade.setText( propBas.getUnidadeSI() );


        //----------------------------------------------------------------
        //--------------------------- FLUIDO -----------------------------
        //----------------------------------------------------------------
        //Caso seja um fluido, faz os testes para os valores já combinados!!
        if(tipo=="fluido") {
            //Se houver correspondência entre o valor, pegar!!
            //Roda todos os valores, preenchendo
            //Se a sigla encontrada for igual à salva pelo programa(mesmo id na verdade)
            //Como são os mesmos valores(classes iguais) podemos rodar juntos
            for (int i = 0; i < idDasPropriedades.length; i++) {
                //O primeiro loop é pra conferir se são propriedades cadastradas(pra não dar bode dpois)
                if (idDasPropriedades[i] == propBas.getIdSigla()) {
                    //Se corresponder, então podemos dizer que é o mesmo!!!
                    //Agora vamos lá na classe já que encontramos a posição e botamos o texto
                    if (numFluido == idFluido[0] & ArmazenamentoDeDados.valoresFluido1[i] != 0.0) {
                        holder.digiteAqui.setText(String.format(Locale.US, "%.7f", ArmazenamentoDeDados.valoresFluido1[i]));
                    }
                    //Fluido 2:
                    //Se corresponder, então podemos dizer que é o mesmo!!!
                    //Agora vamos lá na classe já que encontramos a posição e botamos o texto
                    if (numFluido == idFluido[1] & ArmazenamentoDeDados.valoresFluido2[i] != 0.0) {
                        holder.digiteAqui.setText(String.format(Locale.US, "%.7f", ArmazenamentoDeDados.valoresFluido2[i]));
                    }
                }

            }

        }


            //----------------------------------------------------------------
            //---------------------------- METAL -----------------------------
            //----------------------------------------------------------------
            if(tipo=="metalBitub"){
                for(int i = 0; i<idDasPropriedadesMetalBit.length; i++){
                    if(ArmazenamentoDeDados.valoresMetalBit[i]>0.0) {
                        if (idDasPropriedadesMetalBit[i] == propBas.getIdSigla()) {
                            holder.digiteAqui.setText(String.format(Locale.US,"%.7f",ArmazenamentoDeDados.valoresMetalBit[i]));
                        }
                    }
                }
            }




        // Desisti :v final int corAntiga = holder.digiteAqui.getSolidColor();
        holder.digiteAqui.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int iz, int i1, int i2) {
                //Seta aqui o texto salvo anteriormente isso trava :v
                //digiteAqui.setText(vetorValores.get(lugarzineo+1));
                //holder.nomePropriedade.setTextColor( context.getResources().getColor(R.color.design_default_color_primary_dark));
                //holder.sigla.setTextColor( context.getResources().getColor(R.color.colorAccent));

                String texto = charSequence.toString();

                //Seta aqui o texto salvo anteriormente isso trava :v
                //digiteAqui.setText(vetorValores.get(lugarzineo+1));


                //Segurança para não travar!!!
                if((texto.equals(null)) || (texto.equals(""))){texto = "0.0";}
                //Segurança para quando começar com um ponto!
                if(texto.substring(0,1).equals(".")){
                    //Coloca um zero na frente do ponto
                    texto = "0" + texto;
                }

                //Caso sejam os dados do fluido:
                if(tipo=="fluido") {
                    //Agora vamos mudar os valores da classe que armazena as coisas
                    if (numFluido == idFluido[0]) {
                        //Roda todos os valores, preenchendo
                        //Se a sigla encontrada for igual à salva pelo programa(mesmo id na verdade)
                        for (int i = 0; i < idDasPropriedades.length; i++) {
                            if (idDasPropriedades[i] == propBas.getIdSigla()) {
                                //Troca o valor na clase
                                ArmazenamentoDeDados.valoresFluido1[i] = Double.parseDouble(texto);
                            }
                        }
                    } else if (numFluido == idFluido[1]) {
                        //Roda todos os valores, preenchendo
                        //Se a sigla encontrada for igual à salva pelo programa(mesmo id na verdade)
                        for (int i = 0; i < idDasPropriedades.length; i++) {
                            //O primeiro loop é pra conferir se são propriedades cadastradas(pra não dar bode dpois)
                            if (idDasPropriedades[i] == propBas.getIdSigla()) {
                                ArmazenamentoDeDados.valoresFluido2[i] = Double.parseDouble(texto);
                            }
                        }
                    }
                }

                //Caso sejam os dados do metal:
                if(tipo=="metalBitub"){
                    for (int i = 0; i < idDasPropriedadesMetalBit.length; i++) {
                        if (idDasPropriedadesMetalBit[i] == propBas.getIdSigla()) {
                            //Troca o valor na clase
                            ArmazenamentoDeDados.valoresMetalBit[i] = Double.parseDouble(texto);
                        }
                    }
                }



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }


    @Override
    public int getItemCount() {
        return listaDePropriedades.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sigla;
        TextView nomePropriedade;
        TextView unidade;
        EditText digiteAqui;


        public MyViewHolder(View itemView) {
            super(itemView);

            sigla = itemView.findViewById(R.id.textSiglaProp);
            nomePropriedade = itemView.findViewById(R.id.textNomeProp);
            unidade = itemView.findViewById(R.id.textUnidadeProp);
            digiteAqui = itemView.findViewById(R.id.valorPropEditText);

            digiteAqui.setText(vetorValores.get(getAdapterPosition()+1));

        }
    }


    //TODO
    //O método abaixo, num dado momento(que for invocado),
    //irá preencher a lista de valores e devolver



}
