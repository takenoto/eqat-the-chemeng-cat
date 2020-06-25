package com.example.heatex.Classes;

import java.util.Locale;

public class TuboHairpin {

    public int id;
    public String ScheduleDesignations, DNmm, NPSin;
    public double ODin, ODmm, IDin, IDmm, 重量lb, 重量kg;

    //Construtor da classe
    TuboHairpin(String i, String[] descricao, String[] valores){
        this.id = Integer.parseInt(i);
        this.ScheduleDesignations = descricao[0];
        this.DNmm = descricao[1];
        this.NPSin = descricao[2];
        //Passando os valores:

        //Roda o vetor valores garantindo que tudo occora bem; nada de vírgulas!
        for (int j = 0; j < valores.length; j++) { valores[j] = valores[j].replaceAll(",", "."); }

        //Dá o valor de cada variável
        this.ODin = Double.parseDouble(valores[0]);
        this.ODmm = Double.parseDouble(valores[1]);
        this.IDin = Double.parseDouble(valores[2]);
        this.IDmm = Double.parseDouble(valores[3]);
        this.重量lb = Double.parseDouble(valores[4]);
        this.重量kg = Double.parseDouble(valores[5]);
    }

    public double getDi(int sistema){ if(sistema<=1){return IDmm;} else return IDin; }

    public double getDo(int sistema){
        // É SI se sistema 1, 0 por aí valores padrão né
        if(sistema<=1){return ODmm;} else return ODin; }

    public String gerarTextoParaSpinner(int sistema){
        Double Di, Do;
        String unidadeDiametro;
        if(sistema <=1 ){
            //É em mm!
            Di = IDmm;
            Do = ODmm;
            unidadeDiametro = "mm";
        }

        else{
            unidadeDiametro = "in";
            Di = IDin;
            Do = ODin;

        }
        return ScheduleDesignations + String.format(Locale.US," \n\n Di = %.2f" + unidadeDiametro,Di) + String.format(Locale.US," \n\n Do = %.1f" + unidadeDiametro,Do);
    }

    }

