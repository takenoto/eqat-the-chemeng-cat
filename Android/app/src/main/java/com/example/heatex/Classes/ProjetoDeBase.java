package com.example.heatex.Classes;

public class ProjetoDeBase {

    String nome = "";
    int tipo = 0; //para saber que classe... por padrão é trocador de calor
    public static final int TROCADOR_DE_CALOR=0, REATOR=1;
    public static final int[] LISTA_DE_TIPOS = {TROCADOR_DE_CALOR, REATOR};

    public String getNome(){return nome; }
    public void setNome(String nome){this.nome = nome;}

    public int getTipoDeProjeto(){ return tipo;}
    public void setTipoDeProjeto(int tipo){ this.tipo = tipo;}

    public TrocadorBitubularHeatex trocador = null;
    public ReatorRxn reator = null;


    //Criando o tipo de projeto
    public void setTrocadorDeCalor(TrocadorBitubularHeatex trocador){
        this.trocador=trocador;
        this.reator=null;
        setTipoDeProjeto(TROCADOR_DE_CALOR);
    }
    public void setReator(ReatorRxn reator){
        this.reator=reator;
        this.trocador=null;
        setTipoDeProjeto(REATOR);
    }

    //Cria um método desses pra cada tipo de projeto!!!!
    //Trocador de calor
    public TrocadorBitubularHeatex obterProjetoTrocador(){
        if(tipo==TROCADOR_DE_CALOR){
            return trocador;
        }else return null;
    }

    //Reator
    public ReatorRxn obterProjetoReator(){
        if(tipo==REATOR){
            return reator;
        }else return null;
    }

}
