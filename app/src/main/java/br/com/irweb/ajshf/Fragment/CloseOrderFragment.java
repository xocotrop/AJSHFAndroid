package br.com.irweb.ajshf.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.com.irweb.ajshf.API.Service.OrderService;
import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Entities.Address;
import br.com.irweb.ajshf.Entities.AddressUserAJSHF;
import br.com.irweb.ajshf.Entities.Order;
import br.com.irweb.ajshf.Entities.PaymentMethod;
import br.com.irweb.ajshf.Entities.UserAuthAJSHF;
import br.com.irweb.ajshf.Helpers.StringHelper;
import br.com.irweb.ajshf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CloseOrderFragment extends Fragment {

    private RadioButton radioManha;
    private RadioButton radioTarde;
    private Spinner spinnerAddress;
    private Spinner spinnerMethodPayment;
    private EditText editTextChangeMoney;
    private EditText editTextObservation;
    private CheckBox checkBoxPickup;
    private Button btnFinishOrder;

    private Order mOrder;
    private UserAuthAJSHF user;
    private AddressUserAJSHF addressUserAJSHF;

    private OrderService orderService;


    public CloseOrderFragment() {
        // Required empty public constructor
    }

    public static CloseOrderFragment newInstance() {
        CloseOrderFragment closeOrderFragment = new CloseOrderFragment();

        return closeOrderFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mOrder = AJSHFApp.getOrder();
        user = AJSHFApp.getInstance().getUser();
        addressUserAJSHF = AJSHFApp.getInstance().getAddressUser();

        View v = inflater.inflate(R.layout.fragment_close_order, container, false);

        initViews(v);
        initAdapter();

        setupButtonFinish();

        return v;
    }

    private void setupButtonFinish() {
        btnFinishOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Tasks().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        radioTarde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrder.Period = 1;
            }
        });

        radioManha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOrder.Period = 0;
            }
        });

        checkBoxPickup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mOrder.Pickup = isChecked;
            }
        });

        spinnerMethodPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mOrder.PaymentMethod = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mOrder.IdAddress = addressUserAJSHF.addresses.get(position).IdAddress;
                mOrder.IdNeighborhood = addressUserAJSHF.addresses.get(position).IdNeighborhood;
//                mOrder.IdCity = addressUserAJSHF.addresses.get(position).
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void initAdapter() {
        String[] adrs = getAddresses();
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, adrs);
        spinnerAddress.setAdapter(adapter);

        ArrayAdapter adapterPayment = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, PaymentMethod.METHODS);
        spinnerMethodPayment.setAdapter(adapterPayment);
    }

    private String[] getAddresses() {
        List<String> list = new ArrayList<>();
        for (Address address :
                addressUserAJSHF.addresses) {
            list.add(String.format("%s, %s - %s", address.Address, address.Number, address.Neighborhood));
        }
        String[] listStr = new String[list.size()];
        return list.toArray(listStr);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderService = new OrderService(getContext());
    }

    private void initViews(View v) {
        radioManha = (RadioButton) v.findViewById(R.id.radio_manha);
        radioTarde = (RadioButton) v.findViewById(R.id.radio_tarde);
        spinnerAddress = (Spinner) v.findViewById(R.id.select_address);
        spinnerMethodPayment = (Spinner) v.findViewById(R.id.select_method_payment);
        editTextChangeMoney = (EditText) v.findViewById(R.id.change_money);
        editTextObservation = (EditText) v.findViewById(R.id.text_observation);
        checkBoxPickup = (CheckBox) v.findViewById(R.id.pickup);
        btnFinishOrder = (Button) v.findViewById(R.id.btn_finish_order);
    }

    private class Tasks extends AsyncTask<Void, Void, Void> {

        int orderId;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                orderId = orderService.createOrder();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}

