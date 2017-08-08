package br.com.irweb.ajshf.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import br.com.irweb.ajshf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private Button btnCloseOrder;
    private RecyclerView itemsCart;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newIstance(){
        CartFragment fragment = newIstance();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        btnCloseOrder = (Button) v.findViewById(R.id.btn_close_order);
        itemsCart = (RecyclerView) v.findViewById(R.id.list_items);

        return v;
    }

}
