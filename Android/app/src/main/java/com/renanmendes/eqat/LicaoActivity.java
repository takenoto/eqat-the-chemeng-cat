package com.renanmendes.eqat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.ProjetoDeBase;
import com.example.heatex.Classes.TrocadorBitubularHeatex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LicaoActivity extends AppCompatActivity {


    public boolean veioDoSumario = false;
    int paginaAtual = 0; //A página onde o usuário está
    TextView secaoDoTexto;
    List<FrameLayout> LicaoCompleta = new ArrayList<>();
    LinearLayout licaoBox;
    ImageButton proximaEtapa;
    ImageButton etapaAnterior;
    ProgressBar LicaoSuperiorProgressBar;
    int qualOProjeto=0, qualTipo=0;
    ProjetoDeBase projeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Carrega o projeto logo!
        projeto = ArmazenamentoDeDados.projetoDaVez;

        //Coloca o layout da página
        setContentView(R.layout.licao_layout_superior);

        Button irParaSumario = findViewById(R.id.irParaSumarioButton);
        etapaAnterior = findViewById(R.id.etapaAnteriorImageButton);
        proximaEtapa = findViewById(R.id.proximaEtapaImageButton);
        secaoDoTexto = findViewById(R.id.indicarSecaoAtualTextView);
        licaoBox = findViewById(R.id.licaoBoxLinearLayout);
        LicaoSuperiorProgressBar = findViewById(R.id.LicaoSuperiorProgressBar);

        Intent intent = getIntent();
        if(intent!=null){
            veioDoSumario = intent.getBooleanExtra("veioDoSumario", false);
            qualOProjeto = intent.getIntExtra("qualProjeto", 0);
            qualTipo = intent.getIntExtra("qualTipo", 0);
        }


        //Ao apertar ir para o sumário, chama a atividade sumário passando o intent para ela saber de que é o sumário:
        irParaSumario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(veioDoSumario){
                    LicaoActivity.super.onBackPressed();
                }
                else{startActivity(new Intent(getApplicationContext(), SumarioDaLicaoActivity.class));} //chama o sumário a partir daqui...
            }
        });


        //Os onclicks já tão ok!
        //TODO implementar lá nas funções que passam as páginas!
        etapaAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaPaginaAnterior();
            }
        });
        proximaEtapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaProximaPagina();
            }
        });


        LicaoCompleta = criarLicaoDesejada();
        if(LicaoCompleta.size()>0){
            atualizarLicaoBox(licaoBox,LicaoCompleta.get(0));
        }


    }


    public void atualizarLicaoBox(LinearLayout licaoBox, FrameLayout novaLicao){
        //Tira todas as views antigas:
        licaoBox.removeAllViews();
        //Aí bota a nova:
        licaoBox.addView(novaLicao);
        int paginaParaExibir = paginaAtual+1;
        secaoDoTexto.setText(paginaParaExibir + "/" + LicaoCompleta.size());

        LicaoSuperiorProgressBar.setProgress(paginaAtual);
        LicaoSuperiorProgressBar.setMax(LicaoCompleta.size());
    }


    //Métodos para agilizar no código de cima ^^
    public void irParaProximaPagina(){
        //TODO atualizar a seção do texto para a nova correspondente...
        if(paginaAtual<LicaoCompleta.size()-1) {
            paginaAtual += 1;
            FrameLayout novaLicao = LicaoCompleta.get(paginaAtual);
            atualizarLicaoBox(licaoBox, novaLicao);
        }


    }

    public void irParaPaginaAnterior(){
        if(paginaAtual>0){
            paginaAtual-=1;
            FrameLayout novaLicao = LicaoCompleta.get(paginaAtual);
            atualizarLicaoBox(licaoBox, novaLicao);
        }

    }


    public FrameLayout obterPaginaDeLicao(List<View> listView){

        FrameLayout frameLayout = (FrameLayout) getLayoutInflater().inflate(R.layout.licao_pagina_layout, null);
        //Pega a lição Poderia ser de uma fonte externa, por hora vai ser feita na marra.
        //TODO criar 1 lição com 3 itens(é 1 Lista de LinearLayout verticais, no fim das contas!)

        //TODO usa esse aí --> R.layout.licao_pagina_layout
        LinearLayout miniLay = (LinearLayout) frameLayout.findViewById(R.id.paginaLicaoLinearLayout);

        for(View v:listView){ miniLay.addView(v); }
        //Adiciona as views que foram passadas ao linear layout


        return frameLayout;
    }


    public List<View> obterItensDaPagina(int pagina, List<ItemDeLicao> listaExternaDeItens){
        //Retorna 1 item da página, já como view, para ser colocado depois...
        List<View> listView = new ArrayList<>();
        String tipoDeItem = ItemDeLicao.TXT; //por padrão é um texto

        //Gera uma lista apenas com os que são da página atual:
        List<ItemDeLicao> itens = new ArrayList<>();
        for(ItemDeLicao item:listaExternaDeItens){
            if(item.getPagina()==pagina){
                itens.add(item);
            }
        }

        //Pegamos todos os items da página solicitada
        //Agora vamos adicionar um por um, na ordem:
        //Ordenando da maior pontuação:
        Collections.sort(itens);


        //Infla a view para ser uma imagem, equação, texto, etc, usando os padrões:
        //Substitui o valor(drawable p/ imagem) ou setText para o desejado:
        //TODO if para texto ou drawable, e switch interno para construí-lo
        for(ItemDeLicao item:itens) {
            listView.add(obterRecurso(item));
        }


        return listView;
    }



    public View obterRecurso(ItemDeLicao item){
        String tipoDeItem = item.getTipo();
        String chaveValor = item.getChaveValor();
        //Infla um por padrão só pra quando não passar nos testes:
        View view = getLayoutInflater().inflate(R.layout.licao_txt_padrao, null);

        if(tipoDeItem == ItemDeLicao.TXT){
            String texto="";
            int idTexto = R.string.vazio_sigla;

            view = getLayoutInflater().inflate(R.layout.licao_txt_padrao, null);

            switch (chaveValor){
                case "conferir_entrada_explic_licao":
                    idTexto = R.string.conferir_entrada_explic_licao;
                    break;
                case "balanco_de_energia_explic_licao":
                    idTexto = R.string.balanco_de_energia_explic_licao;
                    break;
                case "balanco_de_energia_taxa_calor_explic_licao":
                    idTexto = R.string.balanco_de_energia_taxa_calor_explic_licao;
                    break;
                case "balanco_de_energia_taxa_calor_simbolos_explic_licao":
                    idTexto = R.string.balanco_de_energia_taxa_calor_simbolos_explic_licao;
                    break;
                case "balanco_de_energia_taxa_calor_um_fluido_explic_licao":
                    idTexto = R.string.balanco_de_energia_taxa_calor_um_fluido_explic_licao;
                    break;
                case "calculo_dtml_lmtd_explic_licao":
                    idTexto=R.string.calculo_dtml_lmtd_explic_licao;
                    break;
                case "calculo_dtml_var_licao":
                    idTexto = R.string.calculo_dtml_var_licao;
                    break;
                case "calculo_dtml_lmtd_2_explic_licao":
                    idTexto = R.string.calculo_dtml_lmtd_2_explic_licao;
                    break;
                case "escolher_config_explic_licao":
                    idTexto = R.string.escolher_config_explic_licao;
                    break;
                case "escolher_config_alocacao_explic_licao":
                    idTexto = R.string.escolher_config_alocacao_explic_licao;
                    break;
                case "coef_conveccao_texto_explic_licao":
                    idTexto = R.string.coef_conveccao_texto_explic_licao;
                    break;
                case "coef_conveccao_correlacao_empregada_tubo_explic_licao":
                    idTexto = R.string.coef_conveccao_correlacao_empregada_tubo_explic_licao;
                    break;
                    case "coef_conveccao_correlacao_empregada_anulo_explic_licao":
                    idTexto = R.string.coef_conveccao_correlacao_empregada_anulo_explic_licao;
                    break;
                default:
                    break;
            }

            texto = getResources().getString(idTexto);

            if(idTexto == R.string.calculo_dtml_var_licao){
                texto = texto.replace("???@@@???", Double.toString(projeto.obterProjetoTrocador().get_dtml()));
            } else if(idTexto == R.string.coef_conveccao_correlacao_empregada_tubo_explic_licao){
                texto = texto.replace("CORRELACAO_NOME", TrocadorBitubularHeatex.correlacoesDisponiveis[projeto.obterProjetoTrocador().correlacaoTubo] );
            }else if(idTexto == R.string.coef_conveccao_correlacao_empregada_anulo_explic_licao){
                texto = texto.replace("CORRELACAO_NOME", TrocadorBitubularHeatex.correlacoesDisponiveis[projeto.obterProjetoTrocador().correlacaoAnulo] );
            }

            ((TextView)view).setText(texto);


        }else if(tipoDeItem == ItemDeLicao.IMG){
            int idImg = R.drawable.ic_mascote_genki;
            String texto="";
            switch (chaveValor){
                case "boneco_joga_chave":
                    idImg = R.drawable.ic_erro_mascote_quebrado2;
                    break;
                case "fundoprojeto":
                    idImg = R.drawable.fundo_projeto2;
                    break;
                case "tubo_bitub_img": idImg = R.drawable.tubos_concentricos_bitubular;break;
                default:
                    break;
            }

            //TODO fazer a função que formata a equação, e tirar esse de baixo
            //É pra passar uma imagem então...
            view = getLayoutInflater().inflate(R.layout.licao_img_padrao, null);
            ((ImageView)view).setImageResource(idImg);


        }else if((tipoDeItem == ItemDeLicao.EQ)){
            int idPadrao = R.string.eq_nao_encontrada;
            String texto="";

            //ESSE SWITCH DESCOBRE A EQUAÇÃO EMPREGADA!
            //Apenas para bitubular e multitubular//
            if(projeto.obterProjetoTrocador().getIdDoTipoDeTrocador() == TrocadorBitubularHeatex.BITUBULAR || projeto.obterProjetoTrocador().getIdDoTipoDeTrocador() == TrocadorBitubularHeatex.MULTITUBULAR){
                int num = 0;
                TrocadorBitubularHeatex troc = projeto.obterProjetoTrocador();
                //Verificando qual é
                if(chaveValor.equals("eq_correlacao_hi")){
                    num = troc.correlacaoTubo;
                }else if(chaveValor.equals("eq_correlacao_ho")){
                    num = troc.correlacaoAnulo;
                }

                //Agora correlaciona os nomes das correlações com os valores usados abaixo!
                //Referência:
                //"UWT", "UHT", "Dittus-Boelter", "Sieder-Tate(Lam)", "Sieder-Tate(Turb)", "PetukhovPopov", "Hausen(transition)", "NotterSleicher", "Seban & Shimazaki", "Gnielisnk(Annulus eq.)"};
                chaveValor = "";
                switch (num){
                    case TrocadorBitubularHeatex.UWT: chaveValor = "cor_uwt"; break;
                    case TrocadorBitubularHeatex.UHT: chaveValor = "cor_uht"; break;
                    case TrocadorBitubularHeatex.DITTUSBOELTER: chaveValor = "cor_dittusboelter"; break;
                    case TrocadorBitubularHeatex.SIEDERTATELAM: chaveValor = "cor_siedertate_lam"; break;
                    case TrocadorBitubularHeatex.SIEDERTATETURB: chaveValor = "cor_siedertate_turb"; break;
                    case TrocadorBitubularHeatex.PETUKHOVPOPOV: chaveValor = "cor_petukhovpopov"; break;
                    case TrocadorBitubularHeatex.HAUSENTRANSITION: chaveValor = "cor_hausen_trans"; break;
                    case TrocadorBitubularHeatex.SEBANSHIMAZAKI: chaveValor = "cor_seban_e_shimazaki"; break;
                    case TrocadorBitubularHeatex.GNIELISNK: chaveValor = "cor_gnielinsk_annulus"; break;
                    default:chaveValor = "";
                    break;
                }

            }




            List<Integer> eqs = new ArrayList<>(); //Eqs de equações

            switch (chaveValor){
                case "eq_balanco_massa": eqs.add(R.string.eq_balanco_massa);break;
                case "eq_dtml_grampo":
                    eqs.add(R.drawable.eqgrampo_lmtd_eq_geral);
                    eqs.add(R.drawable.eqgrampo_lmtd_paralelo_grampo);
                    eqs.add(R.drawable.eqgrampo_lmtd_cc_grampo);
                break;
                case "q_func_u_eq": eqs.add(R.drawable.eqgrampo_q_calor_em_funcao_de_uo_e_ao);break;
                case "eq_ao_grampo":eqs.add(R.drawable.eqgrampo_ao_grampo_bitubular);
                    break;
                case "eq_dh_grampo": eqs.add(R.drawable.eqgrampo_de_diametro_equivalente_grampo);
                    break;
                case "eq_dp_dist_grampo": eqs.add(R.drawable.eqgrampo_dp_distribuida_grampo);
                    break;
                case "eq_dpi_total_tubo_grampo": eqs.add(R.drawable.eqgrampo_dp_grampo_tubo_interno_total); break;
                case "eq_dpi_total_tubo_casco_e_tubos": eqs.add(R.drawable.eqgrampo_dpi_total_casco_e_tubos); break;
                case "eq_rei_grampo": eqs.add(R.drawable.eqgrampo_rei_grampo);
                case "eq_reo_grampo": eqs.add(R.drawable.eqgrampo_reo_grampo);
                //Corelações:
                //Correlações para pressão
                case "cor_ff_haaland": eqs.add(R.drawable.correlacaogrampo_ff_haaland_tubos_rugosos); break;
                //Correlações para coef. calor
                case "cor_uwt":
                    eqs.add(R.drawable.correlacaogrampo_uwt_pe_menor_que_1_ponto_5);
                    eqs.add(R.drawable.correlacaogrampo_uwt_pe_maior_que_5);
                    eqs.add(R.drawable.correlacaogrampo_uwt_pe_maior_que_10);
                break;
                case "cor_uht": eqs.add(R.drawable.correlacaogrampo_uhf); break;
                //todo ?????????? HEIN? case "cor_dittusboelter": eqs.add(R.drawable.); break;
                case "cor_siedertate_lam": eqs.add(R.drawable.correlacaogrampo_siedertate_lam); break;
                case "cor_siedertate_turb": eqs.add(R.drawable.correlacaogrampo_siedertate_turbulento); break;
                case "cor_petukhovpopov":
                    eqs.add(R.drawable.correlacaogrampo_petukhov_eq);
                    eqs.add(R.drawable.correlacaogrampo_petukhov_k1);
                    eqs.add(R.drawable.correlacaogrampo_petukhov_k2);
                    eqs.add(R.drawable.correlacaogrampo_ff_fanning_petukhov);
                break;
                case "cor_hausen_trans": eqs.add(R.drawable.correlacaogrampo_hausentransition); break;
                case "cor_nottersleicher":
                    eqs.add(R.drawable.correlacaogrampo_nottersleicher_eq);
                    eqs.add(R.drawable.correlacaogrampo_nottersleicher_a);
                    eqs.add(R.drawable.correlacaogrampo_nottersleicher_b);
                break;
                case "cor_seban_e_shimazaki": eqs.add(R.drawable.correlacaogrampo_sebaneshimazaki); break;
                case "cor_gnielinsk_annulus":
                    eqs.add(R.drawable.correlacaogrampo_gnielinsk_annulus_eq);
                    eqs.add(R.drawable.correlacaogrampo_k1_gnielinsk_annulus);
                    eqs.add(R.drawable.correlacaogrampo_fa_inner_gnielinsk);
                    eqs.add(R.drawable.correlacaogrampo_fa_outer_gnielinsk);
                    eqs.add(R.drawable.correlacaogrampo_ff_gnielinsk_com_re_modificado);
                    eqs.add(R.drawable.correlacaogrampo_a_re_modificado_gnielinsk);
                    eqs.add(R.drawable.correlacaogrampo_re_estrela_modificado_gnielinsk_annulus_ff);
                    break;


                default:
                    eqs.add(idPadrao);
                    break;

            }
            //Coloca todas as imagems, na ordem em que foram adicionadas:
            LinearLayout linview = (LinearLayout) getLayoutInflater().inflate(R.layout.linearlayout_padrao_limpo, null);
            ImageView img = null;
            for(int id:eqs){
                //Se em algum momento for a padrão, quebra tudo!
                if(id == R.string.eq_nao_encontrada){
                    TextView textView = (TextView) getLayoutInflater().inflate(R.layout.licao_txt_padrao, null);
                    textView.setText(idPadrao);
                    break;
                    //todo Colocar uma etapa intermediária para lidar com textos explicativos! try/catch
                }else{
                    img = (ImageView) getLayoutInflater().inflate(R.layout.licao_img_padrao, null);
                    img.setImageResource(id);
                }
                linview.addView(img);
            }
            view = linview;
            //já colocou todas as views dentro, agora é só retornar!!!
            return view;


        }else if(tipoDeItem == ItemDeLicao.VAR){
            view = getLayoutInflater().inflate(R.layout.licao_txt_padrao, null);
            String texto = "VARIOSA_DESEJADA = XYZXYZ UNITOSANA";
            String valor = "";
            String abreviacao = "";
            String posTexto = "";
            int idPosTexto = R.string.num_reynolds_texto;
            int idAbreviacao = R.string.reynolds_sigla;
            int idUnidade = R.string.calor_q_unidade;
            String unidade = "";


            //TODO fazer um switch de unidades valendo...
            switch (chaveValor){
                case "":
                    break;
                case "Re_i":

                    if(qualOProjeto==ProjetoDeBase.TROCADOR_DE_CALOR){
                        valor = ArmazenamentoDeDados.formatarValorNumerico(projeto.obterProjetoTrocador().getRei(),"e");
                        idAbreviacao = R.string.reynolds_sigla;
                        idPosTexto = R.string.interno_sigla;
                        idUnidade = R.string.vazio_sigla;
                    }
                    break;
                case "Re_o":
                    if(qualOProjeto==ProjetoDeBase.TROCADOR_DE_CALOR){
                        valor = ArmazenamentoDeDados.formatarValorNumerico(projeto.obterProjetoTrocador().getReo(),"e");
                        idAbreviacao = R.string.reynolds_sigla;
                        idPosTexto = R.string.externo_sigla;
                        idUnidade = R.string.vazio_sigla;
                    }
                    break;

                case "dtml":
                    valor = ArmazenamentoDeDados.formatarValorNumerico(projeto.obterProjetoTrocador().get_dtml(),"f");
                    idAbreviacao = R.string.temp_media_log_ΔTlm_sigla;
                    idUnidade = R.string.kelvin_sigla;
                    idPosTexto = R.string.vazio_sigla;
                    break;
                case "hi":
                    valor = ArmazenamentoDeDados.formatarValorNumerico(projeto.obterProjetoTrocador().get_hi(),"e");
                    idAbreviacao = R.string.hi_sigla;
                    idPosTexto = R.string.vazio_sigla;
                    idUnidade = R.string.hi_unidade_si;
                    break;
                case "ho":
                    valor = ArmazenamentoDeDados.formatarValorNumerico(projeto.obterProjetoTrocador().get_ho(),"e");
                    idAbreviacao = R.string.ho_sigla;
                    idPosTexto = R.string.vazio_sigla;
                    idUnidade = R.string.ho_unidade_si;
                    break;
                default:
                    valor="???@@@???"; //para demonstrar claramente que é um erro
                    break;
            }

            //Fazendo cada um receber o desejado:
            posTexto = getResources().getString(idPosTexto);
            abreviacao = getResources().getString(idAbreviacao);
            unidade = getResources().getString(idUnidade);


            texto = texto.replace("XYZXYZ", valor);
            texto = texto.replace("VARIOSA_DESEJADA", abreviacao + posTexto + "");

            if(!unidade.equals("") && !unidade.equals(" ")){
                texto = texto.replace("UNITOSANA", "[ " + unidade + " ]"); //vai ter que ser visto caso a caso!
            }else texto = texto.replace("UNITOSANA", unidade); //vai ter que ser visto caso a caso!



            ((TextView)view).setText(texto);
        }


        return view;
    }


    //EssaDaqui retorna todas as páginas da licao
    public List<FrameLayout> criarLicaoCompleta(List<ItemDeLicao> itensDaLicao){

        List<FrameLayout> listaDeLayouts = new ArrayList<>();
        List<View> listView = new ArrayList<>();

        //Infla o layout padrão
        FrameLayout miniframe = (FrameLayout) getLayoutInflater().inflate(R.layout.licao_pagina_layout,null);
        //É nesse próximo que vão ser colocadas as views da página
        LinearLayout layout = miniframe.findViewById(R.id.paginaLicaoLinearLayout);
        //Infla e bota as coisas aqui dentro!

        //Primeiro descobre o total de páginas:
        int maximoPaginas=0;
        for(ItemDeLicao item:itensDaLicao){if(item.getPagina()>maximoPaginas){maximoPaginas=item.getPagina();}}

        //Preenche dinamicamente com os itens da lista:
        int paginaAtual=0;
        for(int i=0; i<itensDaLicao.size(); i++){
            if(paginaAtual>maximoPaginas){break;}

            //Obtém os itens da página, já como views:
            List<View> itensDaPagina = obterItensDaPagina(paginaAtual, itensDaLicao);
            //Esse página é um inteiro, pra juntar todos da mesma seção, por ordem crescente
            //Cria a página:
            miniframe = obterPaginaDeLicao(itensDaPagina);
            //Adiciona a página criada ao que é desejado:

            //Aí coloca o linear dentro do frame!
            listaDeLayouts.add(miniframe);

            //Passa para a próxima página:
            paginaAtual+=1;
        }

        return listaDeLayouts;
    }




    //tem que passar o argumento também!
    public List<FrameLayout> criarLicaoDesejada(){
        //Aí passa um switch aqui e chama a lição que precisar ser chamada
        String nomeDaLicao="";
        List<FrameLayout> LicaoCompleta = new ArrayList<>();
        List<ItemDeLicao> itensDaLicao = new ArrayList<>();

        if(qualOProjeto== ProjetoDeBase.TROCADOR_DE_CALOR){
            switch (qualTipo){
                case TrocadorBitubularHeatex.BITUBULAR:
                    nomeDaLicao = ObterLicaoSpecialClass.bitubular;
                    break;
                case TrocadorBitubularHeatex.MULTITUBULAR:
                    break;
                case TrocadorBitubularHeatex.CASCOETUBOS:
                    break;
            }

            itensDaLicao = ObterLicaoSpecialClass.getItensDaLicao(nomeDaLicao);
        }


        //Agora cria a licão usando os itens obtidos:
        LicaoCompleta = criarLicaoCompleta(itensDaLicao);
        this.LicaoCompleta = LicaoCompleta;

        int paginaParaExibir = paginaAtual+1;

        secaoDoTexto.setText(paginaParaExibir + "/" + LicaoCompleta.size());

        return LicaoCompleta;
    }


}
