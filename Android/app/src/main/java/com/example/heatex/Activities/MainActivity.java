package com.example.heatex.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.ProjetoDeBase;
import com.example.heatex.Fragments.exibirProjetosAtividadePrincipalFragment;
import com.example.heatex.Fragments.listaDeDesignsFragment;
import com.renanmendes.eqat.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private boolean DEBUGRAPIDO=false;

    //Agora adiciona os fragmento
    final Fragment fragmentHome = new listaDeDesignsFragment();
    final Fragment fragmentoExibicao = new exibirProjetosAtividadePrincipalFragment();
    Fragment activeFragment = fragmentHome;
    final FragmentManager fm = getSupportFragmentManager();


    /*
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //Dentro do oncreate, cria logo o fragmento sendo inflado!!!
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(activeFragment).show(fragmentHome).commit();
                    activeFragment=fragmentHome;
                    return true;
                case R.id.navigation_dashboard:
                    fm.beginTransaction().hide(activeFragment).show(fragmentoExibicao).commit();
                    activeFragment=fragmentoExibicao;
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }





            return false;
        }
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        //navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //Carregar tudo, mas esconder todos menos o último, que é o inicial:
        fm.beginTransaction().add(R.id.mainFrameLayout, fragmentoExibicao, "2").hide(fragmentoExibicao).commit();
        fm.beginTransaction().add(R.id.mainFrameLayout, fragmentHome, "1").commit();
        activeFragment = fragmentHome;

        //Carrega os diâmetros usados no programa...
        ArmazenamentoDeDados.gerarListasDeTubos(getApplicationContext());

        //Carregar a lista de projetos pra ver se corre tudo bem:
        List<ProjetoDeBase> listaDeProjetos = ArmazenamentoDeDados.obterListaDeProjetosSalva(getApplicationContext());

        //TODO remover isso aqui!
        if(DEBUGRAPIDO){
            //passa a atividade direto para a única!
            ArmazenamentoDeDados.testarTodosOsDiametros=false;
            ArmazenamentoDeDados.criarTrocadorDaVez();
            Intent intent = new Intent(getApplicationContext(), exibirDadosProjetoUnicoActivity.class);
            intent.putExtra("tipoDeProjeto", R.string.trocador__de_calor);
            startActivity(intent);
        }

    }


}
