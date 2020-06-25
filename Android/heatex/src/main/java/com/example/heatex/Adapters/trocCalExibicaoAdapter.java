package com.example.heatex.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heatex.Activities.exibirDadosProjetoUnicoActivity;
import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.TrocadorBitubularHeatex;
import com.example.heatex.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class trocCalExibicaoAdapter extends RecyclerView.Adapter<trocCalExibicaoAdapter.MyViewHolder> {

    Context context;
    List<TrocadorBitubularHeatex> listaTrocs;

    int var[], unidades[];
    double valores[];


    public trocCalExibicaoAdapter(Context context, List<TrocadorBitubularHeatex> listaTrocs) {
        this.context = context;
        this.listaTrocs = listaTrocs;


    }





    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listar_projetos_trocador_de_calor_adapter, parent, false);


        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        String textoNum = holder.getAdapterPosition() + 1 + "";
        holder.numProjeto.setText(textoNum);
        TrocadorBitubularHeatex troquinho;
        troquinho = listaTrocs.get(holder.getAdapterPosition());
        holder.idTipoTrocador = troquinho.getIdDoTipoDeTrocador();

        if(holder.idTipoTrocador ==R.string.trocador_bitubular){
            //Variáveis que vão armazenar os nomes que usaremos, na ordem:
            var = troquinho.getLDiDsDp(0);
            valores = troquinho.getValoresLDiDsDp();
            unidades = troquinho.getLDiDsDp(1);
        }
        //Uma vez isso feito, arrumar os outros:
        else if(holder.idTipoTrocador ==R.string.trocador_de_calor_multitubular_hairpin){

        }else if(holder.idTipoTrocador ==R.string.trocador_casco_e_tubos){

        }



        //Preenchedor universal, serve pra qualquer trocador...
        //Só é pra criar da primeira vez... aqui é só pra atualizarZZZ
        for(int j=0; j<valores.length; j++){
            if(j>=holder.miniprops.size()){ //garantia pra não travar
                break;
            }
            LinearLayout simplexMiniPropBasica = holder.miniprops.get(j);
            //Roda a view por dentro, preenchendo
            TextView sigla = simplexMiniPropBasica.findViewById(R.id.siglaSimplexTextView);
            sigla.setText(var[j]);

            TextView valor = simplexMiniPropBasica.findViewById(R.id.valorSimplexTextView);
            String valorPuro =  String.format(Locale.US,"%5.2e",valores[j]);
            if(valores[j]>1000){
                valor.setText(ArmazenamentoDeDados.transformarEmNotacaoCientifica(valorPuro));
            }
            else{
                valorPuro =  String.format(Locale.US,"%.3f",valores[j]);
                valor.setText(valorPuro);
            }

            TextView unidade = simplexMiniPropBasica.findViewById(R.id.unidadeSimplexTextView);
            unidade.setText(unidades[j]);


        }




    }


    @Override
    public int getItemCount() {
        return listaTrocs.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        //Por padrão é o bitubular; para outros editar!!
        //TODO tipo de trocador, mudar para outros!
        int idTipoTrocador =R.string.trocador_bitubular;
        int idTipoProjeto = R.string.trocador__de_calor;

        TextView tipoProjeto, numProjeto;
        LinearLayout listaProjetosLinearLayout;
        LinearLayoutCompat linearGlobal;

        CardView cardView;
        //Props que serão preenchidas ou descartadas:
        //Por padrão são 5 textViews de cada. As que não forem usadas serão sumariamente jogadas fora!!!


        //Lista de views que serão adicionadas
        List<LinearLayout> miniprops = new ArrayList<LinearLayout>();


        public MyViewHolder(View itemView) {
            super(itemView);

            listaProjetosLinearLayout = itemView.findViewById(R.id.listaProjetosLinearLayout);

            numProjeto = itemView.findViewById(R.id.numeroProjetoTextView);
            tipoProjeto = itemView.findViewById(R.id.identificadorPojetoTextView);


            cardView = itemView.findViewById(R.id.adapterProjetosCardView);

            linearGlobal = itemView.findViewById(R.id.listagemGlobalLinearLayout);

            //Coloca evento de clique
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Você clicou no trocador nº "+(getAdapterPosition()+1), Toast.LENGTH_LONG).show();
                    Intent saidaIntent = new Intent(context.getApplicationContext(), exibirDadosProjetoUnicoActivity.class);
                    saidaIntent.putExtra("tipoDeProjeto", idTipoProjeto);
                    saidaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ArmazenamentoDeDados.projetoDaVez.setTrocadorDeCalor(listaTrocs.get(getAdapterPosition()));
                    context.getApplicationContext().startActivity(saidaIntent);
                }
            });


            //Criando as views necessárias:
            if(idTipoTrocador ==R.string.trocador_bitubular){
                valores = listaTrocs.get(0).getValoresLDiDsDp();
            }else if(idTipoTrocador ==R.string.trocador_de_calor_multitubular_hairpin) {

            }
            else if(idTipoTrocador ==R.string.trocador_casco_e_tubos){

            }

            //Só pra saber o tamanho da variável...





            //Só é pra criar da primeira vez... aqui é só pra atualizarZZZ
            for(int j=0; j<valores.length; j++){
                    //Cria a view
                    LinearLayout simplexMiniPropBasica = (LinearLayout) LayoutInflater.from(context)
                            .inflate(R.layout.simplex_exibir_propriedade, null, false);

                    //Adiciona ela à lista completa
                    miniprops.add(simplexMiniPropBasica);
                    listaProjetosLinearLayout.addView(simplexMiniPropBasica);

            }

        }
    }


}
