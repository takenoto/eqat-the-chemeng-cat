package com.renanmendes.eqat;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.heatex.Activities.exibirDadosProjetoUnicoActivity;
import com.example.heatex.Classes.ArmazenamentoDeDados;
import com.example.heatex.Classes.ProjetoDeBase;
import com.example.heatex.Fragments.exibirProjetosAtividadePrincipalFragment;
import com.example.heatex.Fragments.listaDeDesignsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import java.util.List;

public class DrawerActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    private boolean DEBUGRAPIDO=false;

    final Fragment fragmentListaDesigns = new listaDeDesignsFragment();
    final Fragment fragmentArmazem = new exibirProjetosAtividadePrincipalFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(DrawerActivity.this, drawer, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(drawerToggle);


        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_projetar, R.id.nav_armazem,
                R.id.nav_tools, R.id.nav_config, R.id.nav_compartilhar, R.id.nav_sobre_o_app)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                return itemClicadoNoDrawer(menuItem, drawer);
            }
        });





        //Carrega os diâmetros usados no programa...
        ArmazenamentoDeDados.gerarListasDeTubos(getApplicationContext());

        //Carregar a lista de projetos pra ver se corre tudo bem:
        List<ProjetoDeBase> listaDeProjetos = ArmazenamentoDeDados.obterListaDeProjetosSalva(getApplicationContext());


        //Coloca o certo no fragmento
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new listaDeDesignsFragment()).commit();


        //Caso o debug rápido esteja ativo:
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

    @Override
    public void onPostCreate(Bundle savedInstance){
        super.onPostCreate(savedInstance);
        drawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_topo, menu);
        return true;
    }
    */




    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            //TODO transformar essa na main pra ele não voltar pra atividade vazia!
            super.onBackPressed();
        }
    }


    public boolean itemClicadoNoDrawer(MenuItem item, View v){
        int id = item.getItemId();
        Fragment fragment = null;

        //TODO colocar todos os fragmentos necessários aqui!
        switch (id){
            case R.id.nav_projetar:
                fragment = fragmentListaDesigns;//new listaDeDesignsFragment();
                //fragment = new HomeFragment();
                getSupportActionBar().setTitle(R.string.title_home);
                break;
            case R.id.nav_armazem:
                fragment = fragmentArmazem;//
                getSupportActionBar().setTitle(R.string.menu_armazem);
                break;


                //TODO colocar o menu para configurações e o "sobre o app" aqui!
            default:
                Snackbar.make(v, getResources().getString(R.string.esse_recurso_ainda_nao_disponivel), Snackbar.LENGTH_LONG)
                        .show();
                break;
        }

        //Se houver algum fragmento
        if(fragment!=null){


            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            /*
            List<Fragment> listaFragmentos = getSupportFragmentManager().getFragments();
            for(Fragment frag:listaFragmentos){
                ft.remove(frag);
            }
             */

            ft.replace(R.id.nav_host_fragment, fragment).commit();



            navigationView.setCheckedItem(item.getItemId());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }, 250);

            return true;
        }


        //Por padrão retorna false
        return false;
    }
}
