package com.example.heatex.Classes;

//Essa classe fornece apenas uma forma fácil de agrupar um símbolo de propriedade com seu respectivo
//caminho de texto, como "CP" e "R.~~.textoDoCp"
public class PropriedadeBasicaExibicao {

    private String siglaPropriedade, nomePropriedade, unidadeSI, valor;



    //Coloca tudo num construtor logo pra facilitar a vida!
    public PropriedadeBasicaExibicao(String siglaPropriedade, String nomePropriedade, String unidadeSI, String valor){
        setSiglaPropriedade(siglaPropriedade);
        setNomePropriedade(nomePropriedade);
        setUnidadeSI(unidadeSI);
        setValor(valor);
    }

    public String getSiglaPropriedade() {
        return this.siglaPropriedade;
    }

    public void setSiglaPropriedade(String siglaPropriedade) {
        this.siglaPropriedade = siglaPropriedade;
    }

    public String getNomePropriedade() {
        return nomePropriedade;
    }

    public void setNomePropriedade(String nomePropriedade) {
        this.nomePropriedade = nomePropriedade;
    }

    public String getUnidadeSI() {
        return unidadeSI;
    }

    public void setUnidadeSI(String unidadeSI) {
        this.unidadeSI = unidadeSI;
    }


    public  void setValor(String valor){
        this.valor = valor;
    }

    public String getValor(){
        return this.valor;
    }
}
