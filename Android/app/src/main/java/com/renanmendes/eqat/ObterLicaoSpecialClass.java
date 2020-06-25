package com.renanmendes.eqat;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObterLicaoSpecialClass{

    static final String bitubular="bitubular", multitubular="multitubular", cascoETubos="cascoETubos";

    //Métodos estáticos que permitem solicitar a lição a um servidor externo, por exemplo


    public static List<String> getSumarioDaLicao(String qualLicao, Context context){
        List<String> sumario = new ArrayList<>();

        //Adiciona os itens conforme o solicitado:
        switch (qualLicao){
            case bitubular:
                //Rodaria um db externo, por exemplo, e adicionaria os itens obtidos nele aqui
                //sumario = lista que vem do database

                /*sumario.add("Conferir entrada"); //temperatura cruzada e afins
                sumario.add("Obter dados");
                sumario.add("Balanço de Energia");
                sumario.add("Escolher Diâmetros");
                sumario.add("Coeficiente de convecção interno");
                sumario.add("Coeficiente de convecção externo");
                sumario.add("Comprimento e número de trocadores");
                sumario.add("Cálculo da perda de carga");*/

                sumario = Arrays.asList(context.getResources().getStringArray(R.array.sumario_troc_bitubular_grampo_array));
                break;
            default:
                sumario.add("Não pôde ser concluído com êxito");
                break;
        }

        return sumario;
    }



    public static List<ItemDeLicao> getItensDaLicao(String qualLicao){
        List<ItemDeLicao> lista = new ArrayList<>();

        ItemDeLicao miniItem;

        switch (qualLicao){
            case bitubular:
                //Obtém as coisas depois eu organizo lá
                miniItem = new ItemDeLicao(0, 0, 1, "エラー", "txt");
                /*
                lista.add(miniItem);
                lista.add(new ItemDeLicao(0, 0, 2, "explicar_balanco_energia", "txt"));
                lista.add(new ItemDeLicao(0, 0, 3, "eq_balanco_energia", "img"));
                lista.add(new ItemDeLicao(0, 0, 3, "Re_i", "var"));
                lista.add(new ItemDeLicao(0, 1, 1, "Re_o", "var"));
                lista.add(new ItemDeLicao(0, 1, 2, "explicar_balanco_massa", "txt"));
                lista.add(new ItemDeLicao(0, 1, 2, "eq_balanco_massa", "eq"));
                lista.add(new ItemDeLicao(0, 2, 1, "fundoprojeto", "img"));
                lista.add(new ItemDeLicao(0, 2, 2, "boneco_joga_chave", "img"));
                 */
                lista.add(new ItemDeLicao(0, 0, 1, "conferir_entrada_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 1, 2, "balanco_de_energia_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 1, 3, "balanco_de_energia_taxa_calor_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 1, 4, "q_func_u_eq", "eq"));
                lista.add(new ItemDeLicao(0, 1, 5, "balanco_de_energia_taxa_calor_simbolos_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 1, 6, "balanco_de_energia_taxa_calor_um_fluido_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 1, 7, "eq_balanco_energia_fluido_calor", "eq"));
                lista.add(new ItemDeLicao(0, 1, 8, "calculo_dtml_lmtd_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 1, 9, "eq_dtml_grampo", "eq"));
                lista.add(new ItemDeLicao(0, 1, 10, "dtml", "var"));
                lista.add(new ItemDeLicao(0, 1, 11, "calculo_dtml_lmtd_2_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 2, 1, "escolher_config_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 2, 1, "tubo_bitub_img", "img"));
                lista.add(new ItemDeLicao(0, 2, 2, "escolher_config_alocacao_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 3, 1, "coef_conveccao_texto_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 3, 2, "coef_conveccao_correlacao_empregada_tubo_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 3, 3, "eq_correlacao_hi", "eq"));
                lista.add(new ItemDeLicao(0, 3, 4, "hi", "var"));
                lista.add(new ItemDeLicao(0, 4, 1, "coef_conveccao_texto_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 4, 2, "coef_conveccao_correlacao_empregada_anulo_explic_licao", "txt"));
                lista.add(new ItemDeLicao(0, 4, 3, "eq_correlacao_ho", "eq"));
                lista.add(new ItemDeLicao(0, 4, 4, "ho", "var"));

                break;

            default:
                miniItem = new ItemDeLicao(0, 0, 0, "エラー", "aviso");
                lista.add(miniItem);
                break;
        }


        return lista;
    }


}
