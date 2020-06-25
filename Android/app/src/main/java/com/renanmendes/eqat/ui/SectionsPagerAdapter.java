package com.renanmendes.eqat.ui;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    //@StringRes
    //private static final int[] TAB_TITLES = new int[]{R.string.troc_tab_text_1, R.string.troc_tab_text_2, R.string.troc_tab_text_3};
    @StringRes
    final int[] TAB_TITLES;
    public Fragment[] vetorFragmentos;
    private final Context mContext;
    //Aqui eu vou salvar

    /*public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }*/

    public SectionsPagerAdapter(Context context, FragmentManager fm, int[] tituloDasAbasPassado, Fragment[] vetorFragmentosArg) {
        super(fm);
        //Colocando o valor do vetor int que é final!
        TAB_TITLES = tituloDasAbasPassado;
        vetorFragmentos = vetorFragmentosArg;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        /*Fragment fragment = null;
        switch(position) {
            case 0:
                fragment = new listaDeDesignsFragment();
                break;
        }
        return fragment;
        */
        return vetorFragmentos[position];
        //else return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}