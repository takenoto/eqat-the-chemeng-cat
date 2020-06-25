package com.example.heatex.Classes;

import java.util.Locale;

public class SpecHairpinMultitub {


    public int id, tubes;
    public String NPSin, DNmm;
    public double ODin, ODmm, IDin, IDmm;


    SpecHairpinMultitub(String i, String[] descricao, String[] valores) {
        this.id = Integer.parseInt(i);
        this.DNmm = descricao[1];
        this.NPSin = descricao[0];

        //Roda o vetor valores garantindo que tudo occora bem; nada de vírgulas!
        for (int j = 0; j < valores.length; j++) { valores[j] = valores[j].replaceAll(",", "."); }

        //Passando os valores:
        this.tubes = Integer.parseInt(valores[0]);
        this.ODin = Double.parseDouble(valores[1]);
        this.ODmm = Double.parseDouble(valores[2]);
        this.IDin = Double.parseDouble(valores[3]);
        this.IDmm = Double.parseDouble(valores[4]);
    }


    public double getDi(int sistema){ if(sistema<=1){return IDmm;} else return IDin; }

    public double getDo(int sistema){
        // É SI se sistema 1, 0 por aí valores padrão né
        if(sistema<=1){return ODmm;} else return ODin; }

    public String getDs(int sistema){ //Retorna o Ds, mas em Schedule!!! CUIDADO!
        //O shell do multitubular é o 40!!!!
        String Ds;

        if(sistema==0){return DNmm;
        }else return NPSin;
    }


    public String gerarTextoParaSpinner(int sistema){
        String string, unidadeDiametro;
        Double Di,Do;
        if(sistema<=1){
            unidadeDiametro="mm";
            Di=IDmm;
            Do = ODmm;
        }
        else {
            unidadeDiametro="in";
            Di=IDin;
            Do = ODin;
        }
        string = String.format(Locale.US,"("+id+")"+" \n\n Di = %.2f" + unidadeDiametro,Di) + String.format(Locale.US," \n\n Do = %.1f" + unidadeDiametro,Do) + String.format(Locale.US," \n\n Nº tubos = " + tubes) + "\n\nShell Schedule " + DNmm;

        return string;
    }

}
