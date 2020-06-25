package com.example.heatex.Classes;

import android.util.Log;

import com.renanmendes.eqat.R;

import java.util.ArrayList;
import java.util.List;


public class TrocadorBitubularHeatex{


    //Constantes:
    static public final int TIPOLIQ=0, TIPOGAS=1, TIPOMETLIQ = 2;
    static public final int ORIENTACAOCONTRACORRENTE=0, ORIENTACAOPARALELO=1;
    static public final int BONNET_TYPE_RETURN=0, STRAIGHT_PIPE_RETURN=1;
    static public final int TUBO=0, ANULO=1;


    private int orientacao; //Determinar se vai ser CC (contracorrente) ou PAR(paralelo)
    private int tipoFluido1 = TIPOLIQ, tipoFluido2 = TIPOLIQ;
    private int tipoReturn = BONNET_TYPE_RETURN; //Por padrão é bonnet
    //Variáveis do fluido do tubo (i)
    //W_i, Cp_i, μ_i, ρ_i, Pr_i, k_i, ff_i
    private double Tin_i, Tout_i, W_i, Cp_i, μ_i, ρ_i, Pr_i, k_i, G_i;
    private double Re_i, ff_i, vi;//Variáveis que serão usadas apenas dentro do programa.
    private String nomeFluido1, nomeFluido2;
    public String nomeDoTrocador = "nome";

    //Para saber de onde vem:
    public static final int BITUBULAR=0, MULTITUBULAR=1, CASCOETUBOS=2;

    public int idQueDefineTipoTrocador = BITUBULAR; // mudar depois!
    public int correlacaoTubo, correlacaoAnulo;

    //Variáveis do fluido do ânulo(o)
    private double Tin_o, Tout_o, W_o, Cp_o, μ_o, ρ_o, Pr_o, k_o, G_o;
    private double Re_o, ff_o, vo;
    //private double[] o_valor; //Guarda valores do ânulo nessa sequencia de cima

    //Variáveis do metal(m) e velocidades
    private double rugosidade, k_m, Di, Si, Do, So, Ds, De, Dso, L_total, L_hairpin=0, L_max, q, ΔT1, ΔT2, ΔTlm, hi, hi_passado, ho, ho_passado, U, Uc, Ud, Rdi, Rdo, Area, Alinear, AreaExtra, AreaFinal, Ad, Ac, percOverDesign=0, percOverSurface=0, percFoullingOverSurface=0, ΔPi=0, ΔPo=0, ΔPtotal=0; //onde Si e So são as áreas de seção tubo e anulo
    private int n_hairpin=0, n_tubos; //Número de hairpins

    //Para o processo iterativo:
    public double segurancaMinimoExtraPercentual=10, Latual=0, incrementoL=0.1, dL=0.02; //(metros) Incremento --> o quanto aumenta o L a cada teste. dL será feito vez por vez!!

    //Declarando aqui as constantes em relação ao nome das correlações:
    public static final int TODAS=0, UWT=1, UHT=2, DITTUSBOELTER=3, SIEDERTATELAM=4, SIEDERTATETURB=5, PETUKHOVPOPOV=6, HAUSENTRANSITION=7, NOTTERCLEICHER=8, SEBANSHIMAZAKI=9, GNIELISNK=10;
    public static final String[] correlacoesDisponiveis = {"TODAS","UWT", "UHT", "Dittus-Boelter", "Sieder-Tate(Lam)", "Sieder-Tate(Turb)", "PetukhovPopov", "Hausen(transition)", "NotterSleicher", "Seban & Shimazaki", "Gnielisnk(Annulus eq.)"};
    public static final String[] correlacoesDisponiveisTubo = {"TODAS","UWT", "UHT", "Dittus-Boelter", "Sieder-Tate(Lam)", "Sieder-Tate(Turb)", "PetukhovPopov", "Hausen(transition)", "NotterCleicher", "Seban & Shimazaki"};
    public boolean forcarCorrelacaoTubo=false, forcarCorrelacaoAnulo=false; //Por padrão não força
    public int correlacaoForcadaTubo=0,correlacaoForcadaAnulo=0; //Por padrão são zero

    //Variáveis de decisão
    //Fluido mistério é tubo --> Calcula para o tubo. Se não, calcula para o anulo
    //Esse código de dentro não vai decidir nada, só calcular. Decisões por fora!
    //public static boolean isDeterminarW,fluidoMisterioEhTubo;
    private boolean decisoes[]; //Segue a ordem acima!
    private  boolean hiEhConstante=false, hoEhConstante=false, fluidoUmNoTubo;
    boolean sucessoNoProjeto=false;

    double Pe=0, Peo=0; //--> Número de Peclet!!

    //Construtor. Já deve ser capaz de juntar tudo. Os cálculos ficarão no projetar para poder reaproveitar mudando configs depois!
    public TrocadorBitubularHeatex(double[] um_valor, double[] dois_valor, double[] m_valor, int orientacao, int nTubosDado, boolean hiEhConstante, boolean hoEhConstante, int tipoFluido1, int tipoFluido2, String nomeFluido1, String nomeFluido2, Boolean fluidoUmNoTubo, double segurancaPercentual) {

        this.orientacao = orientacao;
        this.tipoFluido1 = tipoFluido1;
        this.tipoFluido2 = tipoFluido2;
        this.nomeFluido1 = nomeFluido1;
        this.nomeFluido2 = nomeFluido2;
        this.fluidoUmNoTubo = fluidoUmNoTubo;
        this.segurancaMinimoExtraPercentual = segurancaPercentual;

        //Log.d("tag", nomeCorrelacoes[0]);

        //Determinando o que fica no anulo e no tubo:
        //Carregando os dados do tubo
        if (fluidoUmNoTubo) {
            W_i = um_valor[0];
            Tin_i = um_valor[1];
            Tout_i = um_valor[2];
            ρ_i = um_valor[3];
            Cp_i = um_valor[4];
            k_i = um_valor[5];
            μ_i = um_valor[6] / 1000; //Convertendo para Pa.s
            Pr_i = um_valor[7];
            //Carregando os dados do ânulo
            W_o = dois_valor[0];
            Tin_o = dois_valor[1];
            Tout_o = dois_valor[2];
            ρ_o = dois_valor[3];
            Cp_o = dois_valor[4];
            k_o = dois_valor[5];
            μ_o = dois_valor[6] / 1000; //Convertendo para Pa.s
            Pr_o = dois_valor[7];
        }else{
            W_o = um_valor[0];
            Tin_o = um_valor[1];
            Tout_o = um_valor[2];
            ρ_o = um_valor[3];
            Cp_o = um_valor[4];
            k_o = um_valor[5];
            μ_o = um_valor[6] / 1000;
            Pr_o = um_valor[7];
            W_i = dois_valor[0];
            Tin_i = dois_valor[1];
            Tout_i = dois_valor[2];
            ρ_i = dois_valor[3];
            Cp_i = dois_valor[4];
            k_i = dois_valor[5];
            μ_i = dois_valor[6] / 1000;
            Pr_i = dois_valor[7];
        }

        //Carregando os dados do metal
        k_m = m_valor[0];
        rugosidade = m_valor[1]*Math.pow(10,-3);
        Di = m_valor[2]*Math.pow(10,-3);
        Do = m_valor[3]*Math.pow(10,-3);
        Ds = m_valor[4]*Math.pow(10,-3);
        Dso = m_valor[5]*Math.pow(10,-3);
        L_max = m_valor[6];
        Rdi = m_valor[7];
        Rdo = m_valor[8];
        q = m_valor[9];


        //n_hairpin = (int) m_valor[6]; Acho melhor determinar só no fim né zzz....

        //Valores pré-determinados pelo usuários
        n_tubos = (int) nTubosDado;
        hi_passado = m_valor[9];
        ho_passado = m_valor[10];
        this.hiEhConstante = hiEhConstante;
        this.hoEhConstante = hoEhConstante;

        //Carregando os dados e limitações estabelecidas pelo usuário


    }



    public void projetar(){
        sucessoNoProjeto=false;
        double Nu_i=0, Nu_o=0;

        //Agora toma decisões
        //Primeiro calcula as áreas
        De = (Math.pow(Ds, 2) - Math.pow(Do,2)*n_tubos)/(Ds + Do*n_tubos) ; //Diâmetro hidráulico
        Si = n_tubos*(((Math.PI) * (Di * Di)) / 4);
        G_i = W_i/Si;

        So = (((Math.PI) * (Math.pow(Ds, 2) - Math.pow(Do,2)*n_tubos)) / 4);
        G_o = W_o/So;


        //第1 -- Determinar logdeltaT, Reynolds e coisas do tipo.
        //Calor trocado:
        q = W_i*Cp_i*Math.abs(Tin_i - Tout_i);


        //Determinando os valores de deltaT1 e deltaT2
        if(orientacao == ORIENTACAOPARALELO) {
            ΔT1 = Tin_i - Tin_o;
            ΔT2 = Tout_i - Tout_o;
        }

        else {
            ΔT1 = Tin_i - Tout_o;
            ΔT2 = Tout_i - Tin_o;
        }
        ΔT1 = Math.abs(ΔT1);
        ΔT2 = Math.abs(ΔT2);

        //Calculando a deltaTM
        if(ΔT1!=ΔT2){ΔTlm = Math.abs(   (ΔT1 - ΔT2)/(Math.log(ΔT1/ΔT2))   );}
        else{ΔTlm = ΔT1;}


        //Reynolds:
        //Re_i = (Di*G_i)/μ_i;
        Re_i = getReynolds(Di, G_i, μ_i);
        //Re_o = (De*G_o)/μ_o;
        Re_o = getReynolds(De, G_o, μ_o);


        //Calculando coeficientes de atrito para o tubo e ânulo: HAALAND
        //Interno
        ff_i = getfFRoughTubesHaaland(Re_i, rugosidade, Di );
        //Externo
        ff_o = getfFRoughTubesHaaland(Re_o, rugosidade, De );

        //Caso seja tubo liso
        if(rugosidade<=0){ ff_i = getFfSmoothTube(Re_i); ff_o = getFfSmoothTube(Re_o);}

        //Chutar o L dinamicamente e testar com TODAS as correlações(pra já saber logo as que não dão)
        //No primeiro Nu !=0 para e aceita ele. É isso?
        //Testa com até mil possibilidades(100 metros de trocador)
        for(int i=0; i<=10000; i++){
            Latual+=incrementoL;
            Nu_o=0;
            Nu_i=0;

            //Primeiro calcula se vai ser só 1 hairpin ou mais...
            n_hairpin = getNumeroHairpin(Latual, L_max);
            if(n_hairpin <= 1){ //Se ultrapassar o permitido...
                L_hairpin = Latual;
                n_hairpin=1;
                //Tem que arredondar pra cima não esquece!!!
            }else{L_hairpin = 0.1 + ((int)(10*Latual/(2*n_hairpin)))/10.0;} //arredonda 1 casa decimal

            //Determina o comprimento total atual
            this.L_total = 2*n_hairpin*L_hairpin; //salva como comprimento máximo
            this.Latual = this.L_total;

            //第2 -- Determinar correlação a ser usada e calcular hi:
            //Verificando qual correlação usar e em seguida já calcula. Vai ter que ser tudo junto pelo visto...

            //Calculando o hi:
            if(!hiEhConstante){
                if(forcarCorrelacaoTubo){
                    correlacaoTubo = correlacaoForcadaTubo;
                }else correlacaoTubo = determinarCorrelacao(TUBO, tipoFluido1, Pr_i, Re_i, L_hairpin, Di );


                //Agora calcula:
                switch (correlacaoTubo){
                    case UWT:
                        Nu_i = getNuUWT(tipoFluido1, Re_i*Pr_i, Re_i, Pr_i);
                        break;

                    case UHT:
                        Nu_i = getNuUHT(tipoFluido1, Re_i, Pr_i);
                        break;

                    case DITTUSBOELTER:
                        Nu_i = getNuDittusBoelter(tipoFluido1, Re_i, Pr_i, Tin_i, Tout_i);
                        break;

                    case SIEDERTATELAM:
                        Nu_i = getNuSiederTateLam(tipoFluido1, Re_i, Pr_i, Di, L_hairpin);
                        break;

                    case SIEDERTATETURB:
                        Nu_i = getNuSiederTateTurb(Re_i, Pr_i);
                        break;

                    case PETUKHOVPOPOV:
                        Nu_i = getNuPetukhovPopov(tipoFluido1, Re_i, Pr_i, ff_i);
                        break;

                    case HAUSENTRANSITION:
                        Nu_i = getNuHausenTransition(tipoFluido1, Re_i, Pr_i, Di, L_hairpin);
                        break;

                    case NOTTERCLEICHER:
                        Nu_i = getNuNotterCleicherMetalLiq(tipoFluido1, Re_i, Pr_i, Di, L_hairpin, "UNIF???");
                        break;

                    case SEBANSHIMAZAKI:
                        Nu_i = getSebanShimazakiMetalLiq(Re_i,Pr_i, L_hairpin, Di);
                        break;

                    case GNIELISNK:
                        //Não faz nada pq é pro tubo, não tem cabimento!!!
                        break;

                    default:
                        Nu_i = getNuPetukhovPopov(tipoFluido1, Re_i, Pr_i, ff_i);
                }


                hi = gethFromNu(Nu_i, k_i, Di);

            } else{hi = hi_passado;}

            //Calculando o ho:
            if(!hoEhConstante){
                if(forcarCorrelacaoAnulo){
                    correlacaoAnulo = correlacaoForcadaAnulo;
                }else correlacaoAnulo = determinarCorrelacao(ANULO, tipoFluido2, Pr_o, Re_o, L_hairpin, De );


                //Agora calcula:
                switch (correlacaoAnulo){
                    case UWT:
                        Nu_o = getNuUWT(tipoFluido2, Re_o*Pr_o, Re_o, Pr_o);
                        break;

                    case UHT:
                        Nu_o = getNuUHT(tipoFluido2, Re_o, Pr_o);
                        break;

                    case DITTUSBOELTER:
                        Nu_o = getNuDittusBoelter(tipoFluido2, Re_o, Pr_o, Tin_o, Tout_o);
                        break;

                    case SIEDERTATELAM:
                        Nu_o = getNuSiederTateLam(tipoFluido1, Re_o, Pr_o, De, L_hairpin);
                        break;

                    case SIEDERTATETURB:
                        Nu_o = getNuSiederTateTurb(Re_o, Pr_o);
                        break;

                    case PETUKHOVPOPOV:
                        Nu_o = getNuPetukhovPopov(tipoFluido2, Re_o, Pr_o, ff_o);
                        break;

                    case HAUSENTRANSITION:
                        Nu_o = getNuHausenTransition(tipoFluido2, Re_o, Pr_o, De, L_hairpin);
                        break;

                    case NOTTERCLEICHER:
                        Nu_o = getNuNotterCleicherMetalLiq(tipoFluido2, Re_o, Pr_o, De, L_hairpin, "UNIF???");
                        break;

                    case SEBANSHIMAZAKI:
                        Nu_o = getSebanShimazakiMetalLiq(Re_o, Pr_o, L_hairpin, De);
                        break;

                    case GNIELISNK:
                        Nu_o = getNuGnielinskAnnulusEquation(tipoFluido2, Re_o, Pr_o, Do, Ds, De, L_hairpin, Tout_i, Tout_o);
                        break;

                    default:
                        Nu_i = getNuPetukhovPopov(tipoFluido2, Re_o, Pr_o, ff_o);
                }


                //Se depois de tudo ainda for zero, força o uso do Gnielinsk!
                if(Nu_o<=0){
                    Nu_o = getNuGnielinskAnnulusEquation(tipoFluido2, Re_o, Pr_o, Do, Ds, De, Latual, Tout_i, Tout_o);
                }

                ho = gethFromNu(Nu_o, k_o, De);
            } else{ho = ho_passado;}



            //第3 -- Ver se tá tudo ok. Arredondar os valores incluindo o coeficiente mínimo de segurança
            //, dividindo em  N trocadores e arredondando para múltiplos de 10cm(.1 m)

            //Verificando o calor trocado... Se for o suficiente, fica com o L selecionado...
            //Calculando Uc e Ud
            Alinear = getALinear(Do);
            Uc = getUc(k_m, Di, Do, hi, ho);
            Ac = getAfromU(Uc, q, ΔTlm);
            Ud = getUd(Uc, Di, Do, Rdi, Rdo);
            Ad = getAfromU(Ud, q, ΔTlm);


            AreaFinal = getAreaTotal(Alinear, L_hairpin, n_hairpin);


            //Calculos finais:
            percOverDesign = 100*((AreaFinal/Ad - 1));
            percOverSurface = 100*((AreaFinal/Ac) - 1);
            percFoullingOverSurface = 100*((Ad/Ac) - 1);



            //Caso seja maior ou igual ao necessário...
            //AQUI VEMOS SE ENCONTRAMOS O L DESEJADO!!!!
            //Verificando se o valor de q bate com o desejado mínimo...
            //double Lencontrado = Ad/Alinear; //é o valor necessário para as condições dadas
            //double Lnecessario = Lencontrado*(1 + (segurancaMinimoExtraPercentual/100));

            if(percOverDesign>=segurancaMinimoExtraPercentual){
                this.sucessoNoProjeto=true;

                //第4 -- Determinar Perda de carga
                //NOVO ISSO antes tava lá em cima! debug
                //->Tubo
                double ΔPi_dist = 4*L_hairpin*n_hairpin*ff_i*(G_i*G_i)/(Di*ρ_i);
                double Ki = 0.7 + (2000/Re_i) + (17.78/(Di*1000));
                double ΔPi_loc = (((G_i*G_i)*Ki)/(2*ρ_o))*(2*n_hairpin - 1);
                ΔPi = ΔPi_dist + ΔPi_loc;


                //->Ânulo
                double ΔPo_dist = 2*(G_o*G_o)*(2*L_hairpin*n_hairpin)*ff_o/(De*ρ_o);
                double Ko=0;
                if(tipoReturn == BONNET_TYPE_RETURN){
                    Ko = 0.75 + (500/Re_o) + (17.78/(1000*De));
                }else if(tipoReturn == STRAIGHT_PIPE_RETURN){
                    Ko = 2.8 + (2000/Re_o) + ((71.12)/(1000*De));
                }
                double ΔPo_loc = (G_o*G_o)*Ko*n_hairpin/(2*ρ_o);
                ΔPo = ΔPo_dist + ΔPo_loc;

                ΔPtotal = ΔPi + ΔPo;

                break; //Quebra o for loop, pois já escontramos o valor desejado...
            }

        }



    }

    //Dando nome à coisas necessárias:
    public void nomearTrocador(String nome){ this.nomeDoTrocador = nome; }
    public String getNomeTrocador(){return this.nomeDoTrocador;}
    public void setIdDoTipoDeTrocador(int id){this.idQueDefineTipoTrocador = id;}
    public int getIdDoTipoDeTrocador(){return idQueDefineTipoTrocador; }
    public double getComprimentoTotal(){return L_total;}
    public double getAreaDeTrocaTotal(){return AreaFinal;}
    public double getDP(){return ΔPtotal;}
    public String getCorrelacaoNome(int tuboOuAnulo){
        if(tuboOuAnulo == TUBO)
            return correlacoesDisponiveis[correlacaoTubo];
        else return correlacoesDisponiveis[correlacaoAnulo];
    }
    public double getRei(){return Re_i;}
    public double getReo(){return Re_o;}


    //Retorna as props calculadas
    public double[] getListaPropriedadesTrocador(){
        //TlmLtotal, Numero hairpins, Numero tubos, LporHairpin,
        double[] vetorProps={q, ΔTlm, Ud, hi, ho, Alinear, AreaFinal, n_hairpin, n_tubos, L_hairpin, percOverDesign, percOverSurface, percFoullingOverSurface, ΔPi, ΔPo, ΔPtotal};
        return vetorProps; }

    public int getTipoReturn(){ return tipoReturn; }

    public int getOrientacao(){ return  orientacao; }

    public int getIdAlocacao(int numFluido){
        int alocacaoFluido1, alocacaoFluido2; //Guarda onde estão: Fluido 1, Fluido2
        if(fluidoUmNoTubo){
            alocacaoFluido1 = R.string.tubo;
            alocacaoFluido2 = R.string.anulo;
        }else{
            alocacaoFluido1 = R.string.anulo;
            alocacaoFluido2 = R.string.tubo;
        }

        if(numFluido==1){
            return alocacaoFluido1;
        }else{
            return alocacaoFluido2;
        }
    }
    public void setTipoReturn(int tipoReturn){ this.tipoReturn = tipoReturn;}

    //Retorna as propriedades do metal
    public double[] getListaPropsMetal(){ double[] vetorProps = {k_m, rugosidade, Di, Do, Ds, Dso, L_max, Rdi, Rdo}; return vetorProps; }

    public String getNomeFluido(int numFluid){ if(numFluid == 1){ return nomeFluido1;} else{ return nomeFluido2;} }

    public boolean isH_PreDeterminado(int umTuboDoisAnulo){ if(umTuboDoisAnulo==1){ return hiEhConstante;} else{return hoEhConstante;} }

    //Retorna as props obtidas para os fluidos!
    public double[] getListaPropsFluidos(int numFluido){
        int tipoFluido=0;
        double W=0, h=0, Re=0, ρ=0, Cp=0, k=0,  μ=0, Pr=0, Tin=0, Tout=0;

        //Determinando os números:
        if (numFluido == 1) {
            tipoFluido = tipoFluido1;
            if (fluidoUmNoTubo) {
                W = W_i;
                h = hi;
                Re = Re_i;
                ρ = ρ_i;
                Cp = Cp_i;
                k = k_i;
                μ = μ_i;
                Pr = Pr_i;
                Tin = Tin_i;
                Tout = Tout_i;
            } else { //O fluido 1 está no ânulo...
                W = W_o;
                h = ho;
                Re = Re_o;
                ρ = ρ_o;
                Cp = Cp_o;
                k = k_o;
                μ = μ_o;
                Pr = Pr_o;
                Tin = Tin_o;
                Tout = Tout_o;
            }

        } else {
            tipoFluido = tipoFluido2;
            if (!fluidoUmNoTubo) { //Caso o fluido 2 esteja no tubo (não ====> !!!! < o fluido 1 no anulo)
                W = W_i;
                h = hi;
                Re = Re_i;
                ρ = ρ_i;
                Cp = Cp_i;
                k = k_i;
                μ = μ_i;
                Pr = Pr_i;
                Tin = Tin_i;
                Tout = Tout_i;
            } else { //O fluido 1 está no tubo... E o 2 portanto no ânulo
                W = W_o;
                h = ho;
                Re = Re_o;
                ρ = ρ_o;
                Cp = Cp_o;
                k = k_o;
                μ = μ_o;
                Pr = Pr_o;
                Tin = Tin_o;
                Tout = Tout_o;
            }
        }

        //Agora junta tudo no vetor:
        //Ordem: TipoFluido, W, Tin, Tout, Tmédia  ρ, Cp, k,  μ, Pr, Reynolds, h
        double[] vetorProps = {tipoFluido, W, Tin, Tout, (Tin+Tout)/2.0, ρ, Cp, k,  μ, Pr, Re};//, h};
        return vetorProps;
    }


    //Para pegar todas as siglas ou Nomes com maior facilidade:
    public int[] getCoisativos(int siglaPropOuUnidade, int qualCoisa){
        int[] listaCoisativa={};

        if(siglaPropOuUnidade == 0){
            //queremos a sigla
            if(qualCoisa==1){
                //É do líquido
                listaCoisativa = new int[]{R.string.vazio_sigla, R.string.mass_flux_sigla, R.string.T_in_sigla, R.string.T_out_sigla, R.string.temp_media_sigla, R.string.densidade_sigla, R.string.cp_sigla, R.string.k_condutividade_sigla, R.string.viscosidade_sigla, R.string.prandtl_sigla, R.string.reynolds_sigla};
            }else if(qualCoisa==2){
                //É do metal
                listaCoisativa = new int[]{R.string.k_condutividade_metal_sigla, R.string.rugosidade_sigla, R.string.diametro_interno_tubo_sigla, R.string.diametro_externo_tubo_sigla, R.string.diametro_interno_anulo_sigla, R.string.diametro_externo_anulo_sigla, R.string.comprimento_maximo_sigla, R.string.fouling_interno_sigla, R.string.fouling_externo_sigla};
            }else{
                //É do trocador em si
                listaCoisativa = new int[]{R.string.calor_q_sigla, R.string.temp_media_log_ΔTlm_sigla, R.string.Ud_sigla, R.string.hi_sigla, R.string.ho_sigla, R.string.area_A_linear_sigla, R.string.area_A_total_sigla, R.string.numero_hp_hairpin_sigla, R.string.numero_tubos_sigla, R.string.comprimento_L_hairpin_sigla,  R.string.over_design_sigla, R.string.over_surface_sigla, R.string.fouling_over_surface_sigla, R.string.queda_pressao_tubo_ΔPi_sigla, R.string.queda_pressao_anulo_ΔPo_sigla, R.string.queda_pressao_total_ΔP_sigla };
            }

        }else if(siglaPropOuUnidade == 1) {
            //queremos o nome da propriedade
            if(qualCoisa==1){
                //É do líquido
                listaCoisativa = new int[]{R.string.tipo_fluido_texto, R.string.mass_flux_texto, R.string.T_in_texto, R.string.T_out_texto, R.string.temp_media_texto, R.string.densidade_texto, R.string.cp_texto, R.string.k_condutividade_texto, R.string.viscosidade_texto, R.string.pr_texto, R.string.num_reynolds_texto};
            }else if(qualCoisa==2){
                //É do metal
                listaCoisativa = new int[]{R.string.k_condutividade_texto, R.string.rugosidade_texto, R.string.diametro_interno_tubo_texto, R.string.diametro_externo_tubo_texto, R.string.diametro_interno_anulo_texto, R.string.diametro_externo_anulo_texto, R.string.comprimento_maximo_texto, R.string.fouling_interno_texto, R.string.fouling_externo_texto};
            }else{
                //É do trocador em si
                listaCoisativa = new int[]{R.string.calor_trocado_q_texto,R.string.temp_media_log_ΔTlm_texto, R.string.Ud_texto, R.string.hi_texto, R.string.ho_texto, R.string.area_A_linear_texto, R.string.area_A_total_texto, R.string.numero_hp_hairpin_texto, R.string.numero_tubos_texto, R.string.comprimento_L_hairpin_texto, R.string.over_design_texto, R.string.over_surface_texto, R.string.fouling_over_surface_texto, R.string.queda_pressao_tubo_ΔPi_texto, R.string.queda_pressao_anulo_ΔPo_texto, R.string.queda_pressao_total_ΔP_texto };
            }

        }else{
            //queremos a unidade
            if(qualCoisa==1){
                //É do líquido
                listaCoisativa = new int[]{R.string.pr_unidade_si, R.string.kg_segundo_sigla, R.string.kelvin_sigla, R.string.kelvin_sigla, R.string.kelvin_sigla, R.string.densidade_unidade_si, R.string.cp_unidade_si, R.string.k_condutividade_unidade_si, R.string.viscosidade_unidade_si, R.string.pr_unidade_si, R.string.pr_unidade_si};
            }else if(qualCoisa==2){
                //É do metal
                listaCoisativa = new int[]{R.string.k_condutividade_unidade_si, R.string.rugosidade_unidade_si, R.string.diametro_unidade_si, R.string.diametro_unidade_si, R.string.diametro_unidade_si, R.string.diametro_unidade_si, R.string.comprimento_maximo_unidade_si, R.string.fouling_interno_unidade_si, R.string.fouling_externo_unidade_si};
            }else{
                //É do trocador em si
                listaCoisativa = new int[]{R.string.calor_q_unidade, R.string.kelvin_sigla, R.string.hi_unidade_si, R.string.hi_unidade_si, R.string.hi_unidade_si, R.string.area_linear_unidade_si, R.string.area_unidade_si, R.string.pr_unidade_si, R.string.pr_unidade_si, R.string.comprimento_maximo_unidade_si, R.string.over_design_unidade, R.string.over_design_unidade, R.string.over_design_unidade, R.string.queda_pressao_unidade_si, R.string.queda_pressao_unidade_si, R.string.queda_pressao_unidade_si };
            }
        }

        return listaCoisativa;
    }

    public int[] getLDiDsDp(int siglaUnidade){
        int[] listaInt = {};
        if(siglaUnidade==0){
            //Retorna siglas
            listaInt = new int[] {R.string.comprimento_L_sigla, R.string.diametro_interno_tubo_sigla, R.string.diametro_interno_anulo_sigla, R.string.queda_pressao_total_ΔP_sigla};
        }
        else{
            //Retorna propriedades
            listaInt = new int[] {R.string.comprimento_maximo_unidade_si, R.string.comprimento_maximo_unidade_si, R.string.comprimento_maximo_unidade_si, R.string.queda_pressao_unidade_si};
        }

        return listaInt;
    }

    public double[] getValoresLDiDsDp(){
        return new double[] {L_total, Di, Ds, ΔPtotal};
    }

    public double get_dtml(){return ΔTlm;}
    public double get_hi(){return hi;}
    public double get_ho(){return ho;}
    //--------------------------------------------------------------
    //--------------------------------------------------------------
    //Calculando o W ou a temperatura, a depender do que for mandado
    //--------------------------------------------------------------

    //Métodos para determinações gerais e ajudar a manter o código limpo:
    double getReynolds(double D, double G, double μ){
        double Reynolds;
        Reynolds = (D*G)/μ;
    return Reynolds;
    }

    /*--------------------------------------------
    ---------------Fator de atrito----------------
    --------------------------------------------*/
    double getFfIsothermalLaminarFlow(double Re){ return Re/16; }
    double getFfSmoothTube(double Re){ return 0.0791/(Math.pow(Re,0.25)); }
    double getfFRoughTubesHaaland(double Re, double rug, double D){ return 0.41 / (  Math.pow( Math.log(Math.pow( 0.23*(rug/D), 10/9 ) + 6.9/Re) , 2));}
    double getfFAllFlowsChurchill(double Rew){
        if(Rew>7){return 2/ (  Math.pow( (  Math.pow( Math.pow(8/Rew, 10) + Math.pow( Rew/36500, 20), -0.5 ) + Math.pow(2.21*Math.log(Rew/7),10)   ), 5) );}
          else{return 0;}
    }
    double getfFFilonenko(double Re){ return (1/4)*((Math.pow(0.79*Math.log(Re),-2))); }



    /*--------------------------------------------
    ---------------Nusselt e hi ho----------------
    ----------TRANSFERÊNCIA DE CALOR--------------
    --------------------------------------------*/
    double gethFromNu(double Nu, double k, double D){ return Nu*k/D; }
    double getUc(double k_m, double Di, double Do, double hi, double ho){ return (1/( (Do/(2*k_m))*Math.log(Do/Di) + (1/ho) + Do/(Di*hi) ));}
    double getUd(double Uc, double Di, double Do, double Rdi, double Rdo){return (1/(Rdo + (1/Uc) + (Do*Rdi/Di)));}
    double getAfromU(double U, double q, double ΔT){return q/(U*ΔT);}
    int getNumeroHairpin( double Ltotal, double Lhairpin){return 1 + (int)( (Ltotal/(2*Lhairpin)));}
    double getAreaTotal(double Alinear, double Lhairpin, double Nhairpin){return 2*Alinear*Lhairpin*Nhairpin;}
    double getALinear(double Do){return n_tubos*Math.PI*Do;}


    //::::::::::::::::NUSSELT DE FATO:::::::::::::
    double getNuUWT(int tipoFluido, double Pe, double Re, double Pr ){
        //Verifica o que é
        if((tipoFluido == TIPOLIQ || tipoFluido == TIPOGAS) & Re<=2100 & Pr>0.6){
            double Nu=0;
            if(Pe>10 || Pe==0){
                Nu = 3.658;
            }else if(Pe<1.5){
                Nu = 4.1807*(1 - 0.0439*Pe);
            }else if(Pe>5){
                Nu = 3.6568*(1+1.227/(Math.pow(Pe,2)));
            }
            return Nu;
        }else{return 0;}
    }

    double getNuUHT(int tipoFluido, double Re, double Pr){ if((tipoFluido == TIPOLIQ || tipoFluido == TIPOGAS) & Re<=2100 & Pr>0.6){return 4.363; } else return 0; }

    double getNuDittusBoelter(int tipoFluido, double Re, double Pr, double Tin, double Tout){
        double n, Nu=0;;
        if(Tin>Tout){ n = 0.3; } else n=0.4;
        Nu = 0.023*Math.pow(Re,0.8)*Math.pow(Pr,n);
        return Nu; }

     double getNuSiederTateLam(int tipoFluido, double Re, double Pr, double D, double L){
        double Nu=0;
         Nu = 1.89*Math.pow((Re*Pr*D/L),1/3);
         return Nu;
     }

     double getNuSiederTateTurb(double Re, double Pr){
        double Nu=0;
        Nu = 0.027*Math.pow(Re,0.8)*Math.pow(Pr,1.0/3.0);
        return Nu;
     }


     double getNuPetukhovPopov(int tipoFluido, double Re, double Pr, double fF){
        double Nu=0, K1, K2;


        K1 = 1 + 13.6*fF;
        K2 = 11.7 + 1.8/(Math.pow(Pr,1/3));

        Nu = ((fF/2.0)*Re*Pr)/(K1 + K2*(Math.pow(fF/2,1.0/2.0))*(Math.pow(Pr,2.0/3.0)-1.0));


        return Nu;
     }

     double getNuHausenTransition(int tipoFluido, double Re, double Pr, double D, double L){
        double Nu=0;
        Nu = 0.116*(Math.pow(Re,2.0/3.0) - 125)*Math.pow(Pr,1.0/3)*(1 + Math.pow((D/L),2.0/3));

        return Nu;
     }

     double getNuNotterCleicherMetalLiq(int tipoFluido, double Re, double Pr, double D, double L, String textao){
        double Nu=0;

        if(textao=="UNIFORM WALL TEMPERATURE"){Nu = 4.8 + 0.0156*(Math.pow(Re,0.85))*Math.pow(Pr,0.93);}
        else{
            Nu = 6.3 + 0.0167*(Math.pow(Re,0.85))*Math.pow(Pr,0.93);

        }
        return Nu; }


    double getSebanShimazakiMetalLiq(double Re, double Pr, double L, double D){
        double Nu = 0;

        Nu = 5.0 + 0.025*(Math.pow((Re*Pr),0.8));
        return Nu;
    }


    double getNuGnielinskAnnulusEquation(int tipoFluido, double Re, double Pr, double Do, double Ds, double Dh, double L, double T_outTubo, double T_outAnulo){
        double Nu = 0, Re星 = 0, Fa=0, a=0, fF=0, k1;


        a = Do/Ds;

        //#1º calcula Fa
        if(T_outTubo>=T_outAnulo){
            Fa = 0.75*Math.pow(a,-0.17);
        }else{Fa = 0.9 - 0.15*Math.pow(a,0.6);}

        //#2º calcula Re*
        Re星 = Re + ( (  ( 1 + Math.pow(a,2) )*Math.log(a) + (1 - Math.pow(a,2)) ) / ( (1 - Math.pow(a,2))*Math.log(a)  )  );
        fF = (1.0/4)*Math.pow((0.79*Math.log(Re星) - 1.64),-2);
        k1 = ( 1.07 + (900/Re) - (0.63/(1+10*Pr)) );
        Nu = ( ( (fF/2)*Re*Pr  )/( k1 + 12.7*Math.pow(fF,1/2)*( Math.pow(Pr,2/3)-1 )  ))*( 1 + Math.pow(Dh/L,2/3)  )*Fa;




        return Nu;
    }




    public int determinarCorrelacao(int tubouOuAnulo, int tipoFluido, double Pr, double Re, double L, double D){
        //Essa função determina a correlação que será usada para fluidos no tubo.
        //0 para tubo e 1 para anulo

        //Começamos assumindo a primeira/número zero (UWT)
        int correlacao=0;
        double Nu=0;


        //A partir daqui são testes diretos:
        //Pethukov
        if((tipoFluido == TIPOLIQ || tipoFluido == TIPOGAS) & Re>=Math.pow(10,4) & Re<=5*Math.pow(10,6) & Pr>=0.5 & Pr<=2000){ correlacao = PETUKHOVPOPOV; return correlacao; }

        //Sieder tate
        if((tipoFluido == TIPOLIQ || tipoFluido == TIPOGAS) & Pr>=0.48 & Pr<=16700 & (L/D)>10){
            if(Re<2100){correlacao = SIEDERTATELAM; return correlacao;}
            if(Re>Math.pow(10,4) & L/D>10.0){correlacao = SIEDERTATETURB; return correlacao;} }

        //Dittus-Boelter
        if((tipoFluido == TIPOLIQ || tipoFluido == TIPOGAS) & (Re<=Math.pow(10,7) & Re>=Math.pow(10,4)) & (Pr>=0.7 & Pr<=100) & (L/D > 60))
        {correlacao = DITTUSBOELTER; return correlacao;}


        //Haulsen
        if((tipoFluido == TIPOLIQ || tipoFluido == TIPOGAS) & Re>=2100 & Re<=Math.pow(10,4)){ correlacao = HAUSENTRANSITION; return correlacao; }

        //NotterCleicher
        if((tipoFluido == TIPOMETLIQ) & Re>=Math.pow(10,4)& Re<=Math.pow(10,6) & Pr<0.1 & L/D>10){correlacao = NOTTERCLEICHER; return correlacao;}

        //Seban&Shimazaki
        if((tipoFluido == TIPOMETLIQ) & (Re>=3.6*Math.pow(10,3) & Re<=9.05*Math.pow(10,5) & Pr>=0.003 & Pr<=0.05 & L/D>30) ){correlacao = SEBANSHIMAZAKI; return correlacao;}


        //Testa a Gnielinsk e outras só do ânulo...
        if(tubouOuAnulo == ANULO){
            //Gnielinsk
            if( (Re>=Math.pow(10,4) & Re<=Math.pow(10,6) & Pr>=0.6 & Pr<=1000 & D/L<=1)){correlacao = GNIELISNK; return correlacao;}
        }


        //Sempre zera essas duas pq já tem sieder tate laminar elas são muito doidas zzz
        Nu = getNuUWT(tipoFluido, Pe, Re, Pr);
        if(Nu>0 && Pe>0){ correlacao = UWT; return correlacao;} //Deu certo

        Nu = getNuUHT(tipoFluido, Re, Pr);
        if(Nu>0){ correlacao = UHT; return correlacao;}

        return correlacao;
    }

    public void setForcarCorrelacaoTubo(boolean forcarですか, int qualCorrelacao){
        forcarCorrelacaoTubo = forcarですか;
        correlacaoForcadaTubo = qualCorrelacao;
    }

    public void setForcarCorrelacaoAnulo(boolean forcarですか, int qualCorrelacao){
        forcarCorrelacaoAnulo = forcarですか;
        correlacaoForcadaAnulo = qualCorrelacao;
    }

    public boolean projetadoComSucesso(){
        return sucessoNoProjeto;
    }
}
