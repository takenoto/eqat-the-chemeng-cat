package FragmentBons;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.renanmendes.heatex.R;


public class LiquidoDadosFragment extends Fragment {

    /*@Override
    public View onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //Inflando o layout
        View v =  inflater.inflate(R.layout.fragment_prop_liquido, container, false);

        System.out.println("AQUI AQUI FRAGMENTO INTERNO");
        return v;
    }
    */

    //@Override
    public View onCreate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //super.onCreate(savedInstanceState);
        //View v =  inflater.inflate(R.layout.fragment_prop_liquido, container, false);
        System.out.println("AQUI AQUI FRAGMENTO INTERNO");
        //return inflater.inflate(R.layout.fragment_prop_liquido, container, false);
        //EU TROQUEI O FRAGMENTO PRA TESTAR!
        return inflater.inflate(R.layout.teste_fragment, container, false);
    }

    //Acho que essa aqui é pra quando já tem um instanciado e vai chamar ele novamente
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

}
