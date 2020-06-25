package com.example.heatex.Classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;

import androidx.annotation.Nullable;

import com.renanmendes.eqat.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ArmazenamentoDeDados {

    public static final String projects_key = "projects_key";
    public static final String MyPREFERENCES = "MyPreferences";


    public static ProjetoDeBase projetoDaVez = new ProjetoDeBase();

    public static List<ProjetoDeBase> obterListaDeProjetosSalva(Context context){

        //Lista de projetos obtidos até agora:
        List<ProjetoDeBase> listaDeProjetos = new ArrayList<>();

        SharedPreferences shref;
        SharedPreferences.Editor editor;
        shref = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String response = shref.getString(projects_key, "");

        listaDeProjetos = gson.fromJson(response, new TypeToken<List<ProjetoDeBase>>(){}.getType());

        //Na primeira inicialização, ela é null. Para consertar isso:
        if(listaDeProjetos==null){
            listaDeProjetos = new ArrayList<>(); //pra não ficar nula, apenas vazia1
        }


        //Caso seja a primeira vez rodando, deve criar os trocadores correspondentes!
        boolean jaFezExemplos = shref.getBoolean("jaFezExemplos", false);
        editor = shref.edit();
        editor.putBoolean("jaFezExemplos", false);
        editor.commit();

        if(jaFezExemplos || (listaDeProjetos.size()<=0)){

            //-----------------------------------------------------------------
            //Ex. 7.1 do livro grampo
            String Fluido1 = "oi", Fluido2 = "oie";
            //A ordem é W, Tin, Tou, ρ, Cp, k,  μ e Pr.
            double valoresFluido1[] = {0.700, 293.15, 328.15, 860, 1530, 0.14, 0.511, 5.58};
            double valoresFluido2[] = {0.791, 333.15, 313.5, 865, 2370, 0.13, 1.340, 24.5};
            //k, rugosidade, Di, Do, Ds_i, Ds_o, Lmax, Rdi, Rdo, hifixo, hofixo
            double valoresMetalBit[] = {16.269, 0.1, 35.08, 42.0, 52.48, 57, 5.1, 0.0002, 0.0002, 10000, 10000};
            boolean hiConstante = false;
            boolean hoConstante = false;
            int tipoFluido1 = TrocadorBitubularHeatex.TIPOLIQ, tipoFluido2=TrocadorBitubularHeatex.TIPOLIQ;
            int orientacao = TrocadorBitubularHeatex.ORIENTACAOCONTRACORRENTE;
            int numeroDeTubos = 1;
            float percentualSeguranca = 15;
            boolean fluido1NoTuboですか = false;
            TrocadorBitubularHeatex troquinho = new TrocadorBitubularHeatex(valoresFluido1, valoresFluido2, valoresMetalBit, orientacao, numeroDeTubos, hiConstante, hoConstante, tipoFluido1, tipoFluido2, Fluido1, Fluido2, fluido1NoTuboですか, percentualSeguranca);
            troquinho.setIdDoTipoDeTrocador(TrocadorBitubularHeatex.BITUBULAR);
            troquinho.setTipoReturn(ArmazenamentoDeDados.tipoReturn);
            troquinho.setTipoReturn(tipoReturn);
            troquinho.setForcarCorrelacaoTubo(true, TrocadorBitubularHeatex.SIEDERTATETURB);
            troquinho.setForcarCorrelacaoAnulo(true, TrocadorBitubularHeatex.SIEDERTATETURB);
            troquinho.projetar();
            ProjetoDeBase projeto = new ProjetoDeBase();
            projeto.setTrocadorDeCalor(troquinho);
            projeto.setNome("Worked Example 7-1(Hairpin Book - CARTAXO, 2015)");
            listaDeProjetos.add(projeto);




            //-----------------------------------------------------------------
            //Problema 8.1 do livro grampo
            //A ordem é W, Tin, Tou, ρ, Cp, k,  μ e Pr.
            valoresFluido1 = new double[]{1.800, 343.15, 318.15, 1090, 1870, 0.15, 0.651, 8.15};
            valoresFluido2 = new double[]{1.000, 305.5, 338.54, 771, 2610, 0.133, 1.08, 21.3};
            //k, rugosidade, Di, Do, Ds_i, Ds_o, Lmax, Rdi, Rdo, hifixo, hofixo
            percentualSeguranca = 17;
            valoresMetalBit = new double[]{51, 0.035, 12.48, 17.1, 77.92, 88.9, 5.2, 0.0001, 0.0001, 10000, 10000};
            hiConstante = false;
            hoConstante = false;
            fluido1NoTuboですか = false;
            numeroDeTubos = 9;
            troquinho = new TrocadorBitubularHeatex(valoresFluido1, valoresFluido2, valoresMetalBit, orientacao, numeroDeTubos, hiConstante, hoConstante, tipoFluido1, tipoFluido2, Fluido1, Fluido2, fluido1NoTuboですか, percentualSeguranca);
            troquinho.setIdDoTipoDeTrocador(TrocadorBitubularHeatex.MULTITUBULAR);
            troquinho.setTipoReturn(TrocadorBitubularHeatex.STRAIGHT_PIPE_RETURN);
            troquinho.setForcarCorrelacaoTubo(true, TrocadorBitubularHeatex.PETUKHOVPOPOV);
            troquinho.setForcarCorrelacaoAnulo(true, TrocadorBitubularHeatex.PETUKHOVPOPOV);
            troquinho.projetar();
            projeto = new ProjetoDeBase();
            projeto.setTrocadorDeCalor(troquinho);
            projeto.setNome("Worked Example 8-1(Hairpin Book - CARTAXO, 2015)");
            listaDeProjetos.add(projeto);


            //-----------------------------------------------------------------
            //Ex. ?????????? 8.1 do livro grampo







            //Tem que atualizar a lista ao fim!!
            atualizarListaDeProjetos(context, listaDeProjetos);
        }



        return listaDeProjetos;
    }

    public static void adicionarItemListaDeProjetos(Context context, ProjetoDeBase projeto){
        List<ProjetoDeBase> listaDeProjetos = obterListaDeProjetosSalva(context);
        if(listaDeProjetos == null){
            //Se for nula, vai criar!
            listaDeProjetos = new ArrayList<>();
        }
        listaDeProjetos.add(projeto);
        atualizarListaDeProjetos(context, listaDeProjetos);

    }

    public static void removerItemListaDeProjetos(Context context, int numProjeto){
        List<ProjetoDeBase> listaDeProjetos = obterListaDeProjetosSalva(context);
        listaDeProjetos.remove(numProjeto);
        atualizarListaDeProjetos(context, listaDeProjetos);
    }

    public static void atualizarListaDeProjetos(Context context, List<ProjetoDeBase> novaLista){
        //Pega do shared prefs!
        SharedPreferences shref;
        SharedPreferences.Editor editor;
        shref = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = gson.toJson(novaLista);

        editor = shref.edit();
        editor.remove(projects_key).apply();
        editor.putString(projects_key, json);
        editor.commit();
    }


    //Armazena 1 trocador para exibição...
    //public static  TrocadorBitubularHeatex trocadorDaVez;
    public static List<TrocadorBitubularHeatex> listaTrocadores = new ArrayList<>();
    public static double q1=0, q2=0;
    public static double numeroDeTubos=1; //por padrão é um!

    public static String Fluido1 = " ", Fluido2 = " ";

    public static final int idDasPropriedades[] = {R.string.mass_flux_sigla, R.string.T_in_sigla, R.string.T_out_sigla, R.string.densidade_sigla, R.string.cp_sigla, R.string.k_condutividade_sigla, R.string.viscosidade_sigla, R.string.prandtl_sigla}; // usado para conferir qual é qual
    public static final int idDasPropsNomeFluido[] = {R.string.mass_flux_texto, R.string.T_in_texto, R.string.T_out_texto, R.string.densidade_texto, R.string.cp_texto, R.string.k_condutividade_texto, R.string.viscosidade_texto, R.string.pr_texto};
    public static final int idDasUnidadesFluido[] = {R.string.kg_segundo_sigla, R.string.kelvin_sigla, R.string.kelvin_sigla, R.string.densidade_unidade_si,R.string.cp_unidade_si, R.string.k_condutividade_unidade_si, R.string.viscosidade_unidade_si, R.string.pr_unidade_si};
    //A ordem é W, Tin, Tou, ρ, Cp, k,  μ e Pr.
    public static double valoresFluido1[] = {0.700, 293.15, 328.15, 860, 1530, 0.14, 0.511, 5.58};
    public static double valoresFluido2[] = {0.791, 313.5, 333.15, 865, 2370, 0.13, 1.340, 24.5};


    //DADOS DO TROCADOR BITUBULAR // METAL
    //k, rugosidade, Di, Do, Ds_i, Ds_o, Lmax, Rdi, Rdo, hifixo, hofixo
    public static final int idDasPropriedadesMETALBIT[] = {R.string.k_condutividade_metal_sigla, R.string.rugosidade_sigla, R.string.diametro_interno_tubo_sigla, R.string.diametro_externo_tubo_sigla, R.string.diametro_interno_anulo_sigla, R.string.diametro_externo_anulo_sigla, R.string.comprimento_maximo_sigla, R.string.fouling_interno_sigla, R.string.fouling_externo_sigla, R.string.hi_sigla, R.string.ho_sigla};
    public static double valoresMetalBit[] = {16.269, 0.1, 35.08, 42.0, 52.48, 57, 5.1, 0.0002, 0.0002, 10000, 10000};

    //Casco e tubos
    //A ordem é:  Pt(ratio) e B
    public static double valoresCascoETubos[] = {1.25, 2};

    public static final double[] valoresPadraoRugosidade = {0.03, 0.1, 0.03, 0.1, 0.020, 0.0015};
    public static final double[] valoresPadraoCondutividade = {54, 54, 36, 36, 12, 200};

    //-----------------------CALOR------------------------------------------
    public static double getCalor(int numeroFluido){
        double calorzineo=0;
        if(numeroFluido==1){calorzineo = valoresFluido1[4]*valoresFluido1[0]*Math.abs(valoresFluido1[1] - valoresFluido1[2]);}
        else{calorzineo = valoresFluido2[4]*valoresFluido2[0]*Math.abs(valoresFluido2[1] - valoresFluido2[2]);}
        return calorzineo;
    }
    public static double q=0; //Valor do calor trocado!!!


    //Ordem:
    //testarTodosOsDiametros, hiConstante, hoConstante
    public static boolean testarTodosOsDiametros = true;
    public static boolean hiConstante = false;
    public static boolean hoConstante = false;


    //Descobre se o fluido 1 está no tubo. Se não, é só trocar(não precisa de 2 variáveis se 1 desarma a outra!)
    public static boolean fluido1NoTuboですか = true; //É verdadeiro por padrão

    public static double percentualSeguranca = 15.0; //Por padrão é 10%...

    //Determinando se os hs são constantes/globais:
    //Ordem: hi, ho
    public static boolean hはConstantesですか[] = {false, false};
    public static double hsConstantes [] = {0.0 , 0.0};
    //Forçar correlação no tubo?
    public static boolean forcarCorrelacaoTubo=false, forcarCorrelacaoAnulo=false;
    public static int correlacaoForcadaTubo=0, correlacaoForcadaAnulo=0;
    public static String[] nomeCorrelacoesBitEMulti = TrocadorBitubularHeatex.correlacoesDisponiveis;

    //Inicializa os tipos de fluido como líquido...
    public static int tipoFluido1 = TrocadorBitubularHeatex.TIPOLIQ, tipoFluido2 = TrocadorBitubularHeatex.TIPOLIQ, tipoReturn = TrocadorBitubularHeatex.BONNET_TYPE_RETURN, orientacao = TrocadorBitubularHeatex.ORIENTACAOCONTRACORRENTE;

    public static boolean testarParalelo = false, testarCC = true;
    public static void marcarOrientacao(){
        if(testarCC){orientacao = TrocadorBitubularHeatex.ORIENTACAOCONTRACORRENTE;}
        else{orientacao = TrocadorBitubularHeatex.ORIENTACAOPARALELO;}
    }

    //Definindo o tipo de trocador - Bitubular, Multitubular ou Casco e Tubos
    public static final int BITUBULAR=0, MULTITUBULAR=1, CASCOETUBOS=2;
    public static int tipoDeTrocador = BITUBULAR; // por padrão

    //Lista de tubos internos:
    public static List<TuboHairpin> listaTubosBitubular, listaTemporaria;
    //Quando apenas tubos bitubulares de determinada série serão testados, usar essas:
    public static List<List<TuboHairpin>> listaFiltradaTubosBitubular = new ArrayList<List<TuboHairpin>>();
    public static List<SpecHairpinMultitub> tubosMultitub;


    //Configurações tipo de SCHEDULE
    public static final String[] SCHEDULEDESIGNATIONBITUB = {null, "10", "10S", "STD", "40", "40S", "XS", "80", "80S", "5", "5S", "120", "160", "XXS", "30", "100", "140", "60"};
    public static int padraoDeProjetoBitubular=0; //Armazena a posição atual; Por padrão é zero.

    public static void gerarListasDeTubos(Context context){ //Preenche tudo. Deve ser executado no MAIN
        Database dbzinho = new Database(context); //Abre o db

        listaTubosBitubular = dbzinho.getDiametrosBitub(null);
        tubosMultitub = dbzinho.getDiametrosHairpinMultitub();


        //Gera todas as listas individuais possíveis e deixa elas prontas p/ serem usadas depois
       /*
        for (String padraoDeProjeto:SCHEDULEDESIGNATIONBITUB){
            //Cria a lista de tubos
            System.out.println("ETAPA    " + i);
            listaTemporaria =  dbzinho.getDiametrosBitub(padraoDeProjeto);
            //Adiciona a lista de tubos à lista de listas de tubos
            listaFiltradaTubosBitubular.add(listaTemporaria);
        }
        */

        //Adiciona as listas vazias à lista de lista, está ATRELADA PERMANENTEMENTE à lista de possibilidades anteriormente digitada
        for (String padraoDeProjeto:SCHEDULEDESIGNATIONBITUB){
            listaTemporaria =  new ArrayList<TuboHairpin>();
            //Adiciona a lista de tubos à lista de listas de tubos
            listaFiltradaTubosBitubular.add(listaTemporaria);
        }

        //Roda todos os valores da lista grande:
        //A primeira lista é a completa:
        listaFiltradaTubosBitubular.set(0,listaTubosBitubular);
        for(TuboHairpin tuboLocal : listaTubosBitubular){
            //Preenche as listas adicionadas:
            //Começa do 1 pq o primeiro é a opção "null"
            for(int i=1; i<SCHEDULEDESIGNATIONBITUB.length; i++){
                //Cria a lista de cada um dos valores pré-definidos
                //A listaTemporaria será acessada diretamente!

                //Divide a String em lista de strings separadas por \, cada um sendo uma coisa, e se for correspondência EXATA é que adiciona!
                String[] splitted = tuboLocal.ScheduleDesignations.split("/",10);

                //Adiciona se for coerente:
                for(String DNlocal:splitted){
                    if(DNlocal.equals(SCHEDULEDESIGNATIONBITUB[i])){
                        listaFiltradaTubosBitubular.get(i).add(tuboLocal);
                    }
                }


            }
        }


        dbzinho.close();//Fecha o db
    }

    public static List<TuboHairpin> getListaFiltradaAtual(){
        return listaFiltradaTubosBitubular.get(padraoDeProjetoBitubular);
    }



    /*----------------------MÉTODOS------------------*/

    //Criar trocador da vez para projetos únicos
    public static void criarTrocadorDaVez(){
        //Criar o trocador...
        TrocadorBitubularHeatex troquinho = new TrocadorBitubularHeatex(ArmazenamentoDeDados.valoresFluido1, ArmazenamentoDeDados.valoresFluido2, ArmazenamentoDeDados.valoresMetalBit, ArmazenamentoDeDados.orientacao, (int) ArmazenamentoDeDados.numeroDeTubos, ArmazenamentoDeDados.hiConstante, ArmazenamentoDeDados.hoConstante, ArmazenamentoDeDados.tipoFluido1, ArmazenamentoDeDados.tipoFluido2, ArmazenamentoDeDados.Fluido1, ArmazenamentoDeDados.Fluido2, ArmazenamentoDeDados.fluido1NoTuboですか, ArmazenamentoDeDados.percentualSeguranca);
        troquinho.setTipoReturn(ArmazenamentoDeDados.tipoReturn);

        //Determina o tipo de trocador
        switch (ArmazenamentoDeDados.tipoDeTrocador){
            case ArmazenamentoDeDados.BITUBULAR:
                troquinho.setIdDoTipoDeTrocador(TrocadorBitubularHeatex.BITUBULAR);
                break;

            case ArmazenamentoDeDados.MULTITUBULAR:
                troquinho.setIdDoTipoDeTrocador(TrocadorBitubularHeatex.MULTITUBULAR);
                break;

            case ArmazenamentoDeDados.CASCOETUBOS:
                troquinho.setIdDoTipoDeTrocador(TrocadorBitubularHeatex.CASCOETUBOS);
                break;

            default:
                troquinho.setIdDoTipoDeTrocador(TrocadorBitubularHeatex.BITUBULAR);

        }

        troquinho.setTipoReturn(tipoReturn);
        troquinho.setForcarCorrelacaoTubo(forcarCorrelacaoTubo, correlacaoForcadaTubo);
        troquinho.setForcarCorrelacaoAnulo(forcarCorrelacaoAnulo, correlacaoForcadaAnulo);
        troquinho.projetar();
        //Salvar ele para ser passado...
        ArmazenamentoDeDados.projetoDaVez.setTrocadorDeCalor(troquinho);
    }


    public static double folgaEmMm = 30; //FOLGA EM MM
    public static boolean keepGoing=true;
    public static int etapa=0, quantidadeDeProjetos=0; //verifica em qual etapa atualmente está, em relação ao progresso completo
    //VELHA!
    public static void projetarMultiplosTrocs(Context context){
        List<TrocadorBitubularHeatex> listosa = new ArrayList<TrocadorBitubularHeatex>();

        //Inicialização:
        ArmazenamentoDeDados.keepGoing=true;
        etapa=0;
        quantidadeDeProjetos=0;

        //Para trocador bitubular simples
        //Preenche a lista


        //Primeiramente com os diâmetros simples(tabela comum)
        List<TuboHairpin> tubosBitub = ArmazenamentoDeDados.getListaFiltradaAtual();

        if(keepGoing){

            if(tipoDeTrocador==BITUBULAR){

                double Di, Do, Ds, Dso;
                int sistema = 0; // ZERO para S.I.

                //Bitubulares:
                for(int i=0; i<tubosBitub.size(); i++){
                    if(!keepGoing){break;}
                    etapa+=1;
                    //System.out.println("DEBUG - ETAPA  = " + etapa);
                    Di = tubosBitub.get(i).getDi(sistema);
                    Do = tubosBitub.get(i).getDo(sistema);
                    for(int j=0; j<tubosBitub.size(); j++){
                        if(!keepGoing){break;}
                        etapa+=1;
                        Ds = tubosBitub.get(j).getDi(sistema);
                        Dso = tubosBitub.get(j).getDo(sistema);
                        //Se e somente se o Do for maior do que o Di, testa
                        if((Do+folgaEmMm) < (Ds)){ //Se o Do for maior que o Ds...
                            //Adiciona os diâmetros lá na classe global...
                            ArmazenamentoDeDados.valoresMetalBit[2] = Di;
                            ArmazenamentoDeDados.valoresMetalBit[3] = Do;
                            ArmazenamentoDeDados.valoresMetalBit[4] = Ds;
                            ArmazenamentoDeDados.valoresMetalBit[5] = Dso;

                            //Projeta o trocador
                            TrocadorBitubularHeatex troquinho = new TrocadorBitubularHeatex(ArmazenamentoDeDados.valoresFluido1, ArmazenamentoDeDados.valoresFluido2, ArmazenamentoDeDados.valoresMetalBit, ArmazenamentoDeDados.orientacao, 1, ArmazenamentoDeDados.hiConstante, ArmazenamentoDeDados.hoConstante, ArmazenamentoDeDados.tipoFluido1, ArmazenamentoDeDados.tipoFluido2, ArmazenamentoDeDados.Fluido1, ArmazenamentoDeDados.Fluido2, ArmazenamentoDeDados.fluido1NoTuboですか, ArmazenamentoDeDados.percentualSeguranca);
                            troquinho.setTipoReturn(ArmazenamentoDeDados.tipoReturn);
                            troquinho.setIdDoTipoDeTrocador(TrocadorBitubularHeatex.BITUBULAR);
                            troquinho.setTipoReturn(tipoReturn);
                            troquinho.setForcarCorrelacaoTubo(forcarCorrelacaoTubo, correlacaoForcadaTubo);
                            troquinho.setForcarCorrelacaoAnulo(forcarCorrelacaoAnulo, correlacaoForcadaAnulo);
                            troquinho.projetar();
                            //Por fim adiciona o trocador projetado à lista
                            if(troquinho.projetadoComSucesso()){
                                listosa.add(troquinho);
                                quantidadeDeProjetos = listosa.size();
                                }
                            }

                            //System.out.println("Numero de projetos = " + listosa.size());
                            //O número de projetos será atualizado dinamicamente, a não ser que seja null...

                            listaTrocadores = listosa; //Atualiza a cada etapa p/ já poder pegar o tamanho

                    }
                }



            }


            //Depois com o MULTITUBULAR
            else if(tipoDeTrocador==MULTITUBULAR){
                etapa=0;
                List<SpecHairpinMultitub> tubosMultitub = ArmazenamentoDeDados.tubosMultitub;

                double Di=0, Do=0, Ds=0, Dso=0;
                String DNmm="";
                int sistema = 0; // ZERO para S.I.
                int numeroDeTubos = 0;

                Database dbzinho = new Database(context); //Abre o db
                List<TuboHairpin> listaTubosBitubular = dbzinho.getDiametrosBitub(null);
                dbzinho.close();

                //Calcula para todas as opções:
                for(int j=0; j<tubosMultitub.size(); j++){
                    etapa+=1;
                    if(!keepGoing){break;}
                    Di = tubosMultitub.get(j).getDi(sistema);
                    Do = tubosMultitub.get(j).getDo(sistema);
                    DNmm = tubosMultitub.get(j).DNmm;
                    numeroDeTubos=tubosMultitub.get(j).tubes;

                    //Obtém os diâmetros corretos:
                    double[] DsEDso = obterDiametrosSchedule40(DNmm, listaTubosBitubular);
                    Ds = DsEDso[0];
                    Dso = DsEDso[1];

                    //Agora calcula o trocador com os dados obtidos!

                    //Adiciona os diâmetros lá na classe global...
                    ArmazenamentoDeDados.valoresMetalBit[2] = Di;
                    ArmazenamentoDeDados.valoresMetalBit[3] = Do;
                    ArmazenamentoDeDados.valoresMetalBit[4] = Ds;
                    ArmazenamentoDeDados.valoresMetalBit[5] = Dso;

                    //Projeta o trocador
                    TrocadorBitubularHeatex troquinho = new TrocadorBitubularHeatex(ArmazenamentoDeDados.valoresFluido1, ArmazenamentoDeDados.valoresFluido2, ArmazenamentoDeDados.valoresMetalBit, ArmazenamentoDeDados.orientacao, numeroDeTubos, ArmazenamentoDeDados.hiConstante, ArmazenamentoDeDados.hoConstante, ArmazenamentoDeDados.tipoFluido1, ArmazenamentoDeDados.tipoFluido2, ArmazenamentoDeDados.Fluido1, ArmazenamentoDeDados.Fluido2, ArmazenamentoDeDados.fluido1NoTuboですか, ArmazenamentoDeDados.percentualSeguranca);
                    troquinho.setIdDoTipoDeTrocador(TrocadorBitubularHeatex.MULTITUBULAR);
                    troquinho.setTipoReturn(ArmazenamentoDeDados.tipoReturn);
                    troquinho.setTipoReturn(tipoReturn);
                    troquinho.setForcarCorrelacaoTubo(forcarCorrelacaoTubo, correlacaoForcadaTubo);
                    troquinho.setForcarCorrelacaoAnulo(forcarCorrelacaoAnulo, correlacaoForcadaAnulo);
                    troquinho.projetar();
                    //Por fim adiciona o trocador projetado à lista
                    if(troquinho.projetadoComSucesso()){
                        listosa.add(troquinho);
                        quantidadeDeProjetos = listosa.size();
                    }

                    listaTrocadores = listosa;


                }



            }else if(tipoDeTrocador==CASCOETUBOS){

            }

        keepGoing=false; //Quando chegar no fim termina ZZZ a outra opção é terminar antes do tempo
        }



        //Por fim salva essa lista na global, substituindo
        listaTrocadores = listosa;
    }

    public static double[] obterDiametrosSchedule40(String DNmm, List<TuboHairpin> listaTubosBitubular){
        double Ds=0, Dso=0;
        int sistema=0;

        //Agora descobre onde fica o DNmm igual ao solicitado dentro do sistema bitubular, para NS 40
        //Pecorre a listaBitubular p/ achar o que bate:
        boolean jaResolveu=false;
        for(TuboHairpin tubinho:listaTubosBitubular){
            if(jaResolveu){break;}

            String[] splitted = tubinho.ScheduleDesignations.split("/",10);
            if(tubinho.DNmm.equals(DNmm)){
                //Agora toda o splitted e vê se ele bate
                for(String schedule:splitted) {
                    if (jaResolveu) {
                        break;
                    }

                    //Bateu com o Schedule, agora temos que saber se é o que buscamos
                    if (schedule.equals("40") || schedule.equals("40S")) {
                        Ds = tubinho.getDi(sistema);
                        Dso = tubinho.getDo(sistema);
                        jaResolveu = true;
                        break;
                    }
                    if (jaResolveu) {
                        break;
                    }
            }
        }
    }



        return new double[]{Ds, Dso};
    }


    //Função que passa de exponencial tipo 5.1e4 para 5.1*10^4
    public static android.text.Spanned transformarEmNotacaoCientifica(String texto){
        android.text.Spanned novoTexto=null;
        String textoContido="";
        //Troca+0 e -0 por + e -
        texto = texto.replace("+0","+");
        texto = texto.replace("-0","-");

        if(texto.contains("e")){
            textoContido = "e";
        }else if(texto.contains("E")){
            textoContido = "E";
        }

        if(!textoContido.equals("")){
            texto = texto.replace(textoContido,"・10<sup>");
            texto = texto+"</sup>";
        }
        novoTexto = Html.fromHtml(texto);
        return novoTexto;
    }

    public static String formatarValorNumerico(double valor, @Nullable String letraA ){
        String letra = letraA;
        if(letra!=null){
            if(!letra.equals("e") && !letra.equals("f")){
                letra = "f";
            }
        }else letra="f"; //por padrão é o float

        String valorFormatado="";
        if(letra.equals("f")){
            //Se o float equivaler a um inteiro:
            if(valor == (int) valor){
                valorFormatado = String.format(Locale.US,"%.3f",valor);
            } else valorFormatado = String.format(Locale.US,"%.5f",valor);
        }else valorFormatado = String.format(Locale.US,"%3.2e",valor);


        return valorFormatado;
    }


    public static final int ERROS_TROCADORES_DI_MAIOR_QUE_DO = 0, CRUZAMENTO_DE_TEMPERATURAS =1, ALGUM_DADO_ZERADO=2, CALOR_NAO_CONFERE=3;
    public static final String[] listaErrosTrocsStringoso = new String[] {"ERROS_TROCADORES_DI_MAIOR_QUE_DO", "CRUZAMENTO_DE_TEMPERATURAS", "ALGUM_DADO_ZERADO", "CALOR_NAO_CONFERE"};

    public static List<ErroPadrao> listaDeErros = new ArrayList<>();

    //m para metal, 1 para fluido1 e 2 para fluido2
    public static void gerarErrosTrocadores(){
        //Zera a lista e depois preenche com os erros detectados
        listaDeErros = new ArrayList<>();

        //Testando o calor:
        q1 = ArmazenamentoDeDados.getCalor(1);
        q2 = ArmazenamentoDeDados.getCalor(2);
        //E aí compara se é mais de 10% de erro...
        double erroPerc = 100 * Math.abs(1 - q1 / q2);
        if (erroPerc >= 5 ) {
            ErroPadrao erroPadrao = new ErroPadrao(CALOR_NAO_CONFERE, R.string.calor_q_sigla, R.string.fluidos, R.string.mensagem_erro_calores_errados_trocador);
            listaDeErros.add(erroPadrao);}

        //Checando se algo é zero:
        for(int i=0; i<valoresFluido1.length; i++){
            double valor = valoresFluido1[i];
            if(valor==0){
                ErroPadrao erroPadrao = new ErroPadrao(ALGUM_DADO_ZERADO, idDasPropriedades[i], R.string.fluido1, R.string.mensagem_erro_valor_zerado);
                listaDeErros.add(erroPadrao);}
        }
        for(int i=0; i<valoresFluido2.length; i++){
            double valor = valoresFluido2[i];
            if(valor==0){
                ErroPadrao erroPadrao = new ErroPadrao(ALGUM_DADO_ZERADO, idDasPropriedades[i], R.string.fluido2, R.string.mensagem_erro_valor_zerado);
                listaDeErros.add(erroPadrao);}
        }
        for(int i=0; i<valoresMetalBit.length; i++){
            double valor = valoresMetalBit[i];
            if(valor==0){
                ErroPadrao erroPadrao = new ErroPadrao(ALGUM_DADO_ZERADO, idDasPropriedadesMETALBIT[i], R.string.design_e_material, R.string.mensagem_erro_valor_zerado);
                listaDeErros.add(erroPadrao);}
        }

        //Checando se Di>Do, mas apenas caso não esteja marcado pra testar todos!
        if((valoresMetalBit[3]>valoresMetalBit[4]) && (!testarTodosOsDiametros)){
            ErroPadrao erroPadrao = new ErroPadrao(ERROS_TROCADORES_DI_MAIOR_QUE_DO, idDasPropriedadesMETALBIT[3], R.string.design_e_material, R.string.mensagem_erro_diametros);
            listaDeErros.add(erroPadrao); }


        //Checando cruzamento de temperaturas
        if(false){
            if(tipoDeTrocador==BITUBULAR || tipoDeTrocador==MULTITUBULAR){
                double menorTempFluido1 = Math.min(valoresFluido1[1], valoresFluido1[2]);
                double menorTempFluido2 = Math.min(valoresFluido2[1], valoresFluido2[2]);
                double maiorTempFluido1 = Math.max(valoresFluido1[1], valoresFluido1[2]);
                double maiorTempFluido2 = Math.max(valoresFluido2[1], valoresFluido2[2]);
                //TODO acho que a verificação de cruzamento de temperaturas tá toda errada...
                if((menorTempFluido1<maiorTempFluido2 || menorTempFluido2>maiorTempFluido1) && !(maiorTempFluido1<menorTempFluido2)){
                    ErroPadrao erroPadrao = new ErroPadrao(CRUZAMENTO_DE_TEMPERATURAS, R.string.T_in_sigla, R.string.fluidos, R.string.mensagem_erro_temperaturas_cruzadas);
                    listaDeErros.add(erroPadrao); }
            }
        }




    }




}