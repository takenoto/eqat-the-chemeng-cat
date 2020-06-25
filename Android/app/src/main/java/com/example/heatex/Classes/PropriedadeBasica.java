package com.example.heatex.Classes;

//Essa classe fornece apenas uma forma fácil de agrupar um símbolo de propriedade com seu respectivo
//caminho de texto, como "CP" e "R.~~.textoDoCp"
public class PropriedadeBasica {

    private String siglaPropriedade, nomePropriedade, unidadeSI;
    private int idSigla;


    //Coloca tudo num construtor logo pra facilitar a vida!
    public PropriedadeBasica(String siglaPropriedade, String nomePropriedade, String unidadeSI, int passOIdDaSiglaPraFicarFacil){
        setSiglaPropriedade(siglaPropriedade);
        setNomePropriedade(nomePropriedade);
        setUnidadeSI(unidadeSI);
        idSigla = passOIdDaSiglaPraFicarFacil;
    }

    public String getSiglaPropriedade() {
        return siglaPropriedade;
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

    public int getIdSigla(){return idSigla; }
}
