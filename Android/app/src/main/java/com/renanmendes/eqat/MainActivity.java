package com.renanmendes.eqat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;


//TODO depois essa main aqui vai é rodar!!!!

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, DrawerActivity.class);
        startActivity(intent);



    }


    //Carrega as coisas antes de abrir a menu_topo activity:
    //TODO carregando arquivos....



}
