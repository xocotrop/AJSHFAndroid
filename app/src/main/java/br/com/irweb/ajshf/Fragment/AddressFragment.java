package br.com.irweb.ajshf.Fragment;


import android.icu.text.DateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.irweb.ajshf.Business.UserBusiness;
import br.com.irweb.ajshf.Entities.Address;
import br.com.irweb.ajshf.Entities.PaymentMethod;
import br.com.irweb.ajshf.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {

    //region vars

    private UserBusiness userBusiness;

    private EditText cep;
    private EditText address;
    private EditText number;
    private Spinner city;
    private Spinner neighborhood;
    private EditText complement;
    private EditText phoneNumber;
    private EditText celNumber;
    private String[] cities;
    private Button btnRegister;

    private AlertDialog dialog;
    //endregion

    public AddressFragment() {
        // Required empty public constructor
    }

    public static AddressFragment newInstance(){
        AddressFragment frag = new AddressFragment();

        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        userBusiness = new UserBusiness(getContext());

        View v = inflater.inflate(R.layout.fragment_address, container, false);

        btnRegister = (Button) v.findViewById(R.id.btn_register);
        cep = (EditText) v.findViewById(R.id.cep);
        address = (EditText) v.findViewById(R.id.address);
        number = (EditText) v.findViewById(R.id.number);
        city = (Spinner) v.findViewById(R.id.city);
        neighborhood = (Spinner) v.findViewById(R.id.neighborhood);
        complement = (EditText) v.findViewById(R.id.complement);
        phoneNumber = (EditText) v.findViewById(R.id.phone_number);
        celNumber = (EditText) v.findViewById(R.id.cellphone_number);

        initTasks();

        initButtons();

        return v;
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Aguarde");
        builder.setMessage("Estamos realizando o cadastro do endereço");
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initButtons() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address addressModel = new Address();
                if (validate(addressModel)) {
                    createDialog();
                    new RegisterAddressTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, addressModel);
                }
            }
        });
    }

    private boolean validate(Address addressModel) {
        boolean error = false;
        StringBuilder strB = new StringBuilder();

        if (addressModel.Address.isEmpty()) {
            error = true;
            strB.append("Endereço é obrigatório");
        }

        if (addressModel.CellphoneNumber.isEmpty()) {
            error = true;
            strB.append("\r\nNúmero de celular é obrigatório");
        }
        if (addressModel.CEP.isEmpty()) {
            error = true;
            strB.append("\r\nO CEP é obrigatório");
        }
        if (addressModel.Complement.isEmpty()) {
            error = true;
            strB.append("\r\nO Complemento é obrigatório");
        }
        if (addressModel.IdNeighborhood != 0) {
            error = true;
            strB.append("\r\nBairro é obrigatório");
        }
        if (addressModel.Number.isEmpty()) {
            error = true;
            strB.append("\r\nNúmero da rua é obrigatório");
        }

        return !error;
    }

    private void initTasks() {
        new CityTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void initAdapter() {
        String[] getCities = getCities();
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, getCities);
        city.setAdapter(adapter);

        ArrayAdapter adapterPayment = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, PaymentMethod.METHODS);
        neighborhood.setAdapter(adapterPayment);
    }

    public String[] getCities() {
        return cities;
    }

    private class RegisterAddressTask extends AsyncTask<Address, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);

            dialog.dismiss();

        }

        @Override
        protected Boolean doInBackground(Address... params) {

            try {
                userBusiness.insertAddress(params[0]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    private class CityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initAdapter();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                userBusiness.getCities();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class GetAddressInfoTask extends AsyncTask<Void, Void, Void> {

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
            return null;
        }
    }

}
