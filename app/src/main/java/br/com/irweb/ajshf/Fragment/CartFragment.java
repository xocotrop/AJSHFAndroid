package br.com.irweb.ajshf.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import br.com.irweb.ajshf.Adapter.ItemCartAdapter;
import br.com.irweb.ajshf.Bus.MessageBus;
import br.com.irweb.ajshf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private Button btnCloseOrder;
    private RecyclerView itemsCart;
    private ItemCartAdapter itemCartAdapter;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    public static CartFragment newIstance() {
        CartFragment fragment = new CartFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        btnCloseOrder = (Button) v.findViewById(R.id.btn_close_order);
        itemsCart = (RecyclerView) v.findViewById(R.id.list_items);

        itemCartAdapter = new ItemCartAdapter(getContext());
        itemsCart.setAdapter(itemCartAdapter);
        itemsCart.setLayoutManager(new LinearLayoutManager(getContext()));

        btnCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBus bus = new MessageBus();
                bus.className = CartFragment.class + "";
                bus.message = "fecharPedido";
                EventBus.getDefault().post(bus);

            }
        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
