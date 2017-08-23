package br.com.irweb.ajshf.Fragment;


import android.content.Context;
import android.icu.text.NumberFormat;
import android.icu.util.Currency;
import android.icu.util.CurrencyAmount;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import br.com.irweb.ajshf.Adapter.ItemCartAdapter;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Bus.MessageBus;
import br.com.irweb.ajshf.Entities.ItemOrder;
import br.com.irweb.ajshf.Entities.Order;
import br.com.irweb.ajshf.MainAJSActivity;
import br.com.irweb.ajshf.R;
import br.com.irweb.ajshf.Service.CartService;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private Button btnCloseOrder;
    private Button btnBack;
    private RecyclerView itemsCart;
    private ItemCartAdapter itemCartAdapter;
    private CartService cartService;
    private Order _order;
    private TextView txtOrderTotal;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cartService = new CartService(getContext());
        _order = AJSHFApp.getOrder();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainAJSActivity)getContext()).hideFAB();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        btnCloseOrder = (Button) v.findViewById(R.id.btn_close_order);
        itemsCart = (RecyclerView) v.findViewById(R.id.list_items);
        btnBack = (Button) v.findViewById(R.id.btn_back);
        txtOrderTotal = (TextView) v.findViewById(R.id.order_total);

        itemCartAdapter = new ItemCartAdapter(getContext());
        itemsCart.setAdapter(itemCartAdapter);
        itemsCart.setLayoutManager(new LinearLayoutManager(getContext()));
        itemCartAdapter.setItemAdapterBtnClick(new ItemCartAdapter.ItemAdapterBtnClick() {
            @Override
            public void onClickRemove(int position) {
                ItemOrder itemOrder = _order.Items.get(position);
                cartService.removeItemOrder(itemOrder.MenuId);
                itemCartAdapter.notifyDataSetChanged();
                SetTotal();
            }

            @Override
            public void onClickCard(int position) {

            }
        });

        btnCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageBus bus = new MessageBus();
                bus.className = CartFragment.class + "";
                bus.message = "fecharPedido";
                EventBus.getDefault().post(bus);

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainAJSActivity)getContext()).onBackPressed();
            }
        });

        SetTotal();

        return v;
    }

    private void SetTotal() {
        txtOrderTotal.setText(String.format("R$ %.2f", _order.TotalValue));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
