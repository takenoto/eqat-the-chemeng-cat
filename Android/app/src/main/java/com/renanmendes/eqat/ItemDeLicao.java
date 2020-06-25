package com.renanmendes.eqat;

public class ItemDeLicao implements Comparable{

    //Contém de onde o item vem(id da lição), qual sua página/etapa,
    //qual sua posição dentro do layout linear (1º, 2º, 3º, etc)
    //e por fim o tipo(se é texto, imagem, etc) e a chave_valor pra obter no database

    private int idLicao, pagina, posicaoNoLinLay;
    private String chaveValor, tipo;

    static final String TXT="txt", EQ="eq", IMG="img", VAR="var";

    ItemDeLicao(int idLicao){ setIdLicao(idLicao); }

    ItemDeLicao(int idLicao, int pagina, int posicaoNoLinLay, String chaveValor, String tipo){
        setIdLicao(idLicao);
        setPagina(pagina);
        setPosicaoNoLinLay(posicaoNoLinLay);
        setChaveValor(chaveValor);
        setTipo(tipo);
    }

    public void setIdLicao(int idLicao) {
        this.idLicao = idLicao;
    }

    public void setPosicaoNoLinLay(int posicaoNoLinLay) {
        this.posicaoNoLinLay = posicaoNoLinLay;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public void setChaveValor(String chave_valor) {
        this.chaveValor = chave_valor;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public int getIdLicao(){return idLicao;}
    public int getPagina(){return pagina;}
    public int getPosicaoNoLinLay(){return posicaoNoLinLay;}

    public String getChaveValor(){return chaveValor;}
    public String getTipo(){return tipo;}

    public int getPontuacao(){
        int pontuacao = this.getPagina()+this.getPosicaoNoLinLay();
        return pontuacao;
    }

    @Override
    public int compareTo(Object o) {
        int pontuacaoAqui = this.getPontuacao();
        int pontuacaoO = ((ItemDeLicao)o).getPontuacao();
        int num=0;

        //Descobre a soma atual e a do objeto o

        if(pontuacaoAqui == pontuacaoO){num = 0;}
        else if(pontuacaoAqui > pontuacaoO){num = 1;}
        else{num = -1;}

        return num;
    }
}
