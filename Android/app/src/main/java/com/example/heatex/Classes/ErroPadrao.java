package com.example.heatex.Classes;

public class ErroPadrao {

    public int tipo_de_erro=0;
    public int idPropRelacionada;
    public int ondeFicaPropZeradaIdDoTituloDaAba;
    public int descricaoDoErroTexto;


    ErroPadrao(int tipo_de_erro, int idPropRelacionada, int ondeFicaPropZeradaIdDoTituloDaAba, int descricaoDoErroTexto){
        this.tipo_de_erro=tipo_de_erro;
        this.idPropRelacionada = idPropRelacionada;
        this.ondeFicaPropZeradaIdDoTituloDaAba = ondeFicaPropZeradaIdDoTituloDaAba;
        this.descricaoDoErroTexto = descricaoDoErroTexto;
    }


}
