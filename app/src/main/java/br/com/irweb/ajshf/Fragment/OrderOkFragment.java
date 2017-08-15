package br.com.irweb.ajshf.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import br.com.irweb.ajshf.Bus.MessageBus;
import br.com.irweb.ajshf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderOkFragment extends Fragment {

    private String idPedido;
    private TextView txtIdPedido;
    private Button btnFinish;
    public OrderOkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order_ok, container, false);
        txtIdPedido = (TextView) v.findViewById(R.id.id_pedido);
        btnFinish = (Button) v.findViewById(R.id.btn_finish);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBus bus = new MessageBus();
                bus.className = OrderOkFragment.class+"";
                bus.message = "MenuInicial";
                EventBus.getDefault().post(bus);
            }
        });

        if(getArguments() != null){
            idPedido = getArguments().getString("id");
            txtIdPedido.setText("#" + idPedido);
        }

        return v;
    }

    public static OrderOkFragment newInstance(String idPedido) {
        OrderOkFragment fragment = new OrderOkFragment();
        Bundle b = new Bundle();
        b.putString("id", idPedido);
        fragment.setArguments(b);
        return fragment;
    }
}
