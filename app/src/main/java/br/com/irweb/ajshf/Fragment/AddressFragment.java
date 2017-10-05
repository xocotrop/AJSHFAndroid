package br.com.irweb.ajshf.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Bus.MessageBus;
import br.com.irweb.ajshf.Business.UserBusiness;
import br.com.irweb.ajshf.Entities.Address;
import br.com.irweb.ajshf.Entities.AddressDataModel;
import br.com.irweb.ajshf.Entities.City;
import br.com.irweb.ajshf.Entities.Neighborhood;
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
    private List<City> cities;
    private Button btnRegister;
    private List<Neighborhood> neighborhoods;
    private int idCity;
    private int idNeighborhood;
    private int tempIdNeighborhood;
    private AlertDialog dialogUpdateInfo;
    private AlertDialog dialogSave;

    private Address userAddress;

    //endregion

    public AddressFragment() {
        // Required empty public constructor
    }

    public static AddressFragment newInstance() {
        AddressFragment frag = new AddressFragment();

        return frag;
    }

    public static AddressFragment newInstance(String idAddress) {
        AddressFragment frag = new AddressFragment();

        Bundle b = new Bundle();
        b.putString("idAddress", idAddress);

        frag.setArguments(b);

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

        loadAddress();

        initButtons();

        initFunctions();

        initTasks();

        return v;
    }

    private void loadAddress() {
        if (getArguments() != null) {
            int idAddress = Integer.parseInt(getArguments().getString("idAddress"));
            if (idAddress > 0) {
                List<Address> addresses = AJSHFApp.getInstance().getAddressUser().addresses;
                for (Address a :
                        addresses) {
                    if (a.IdAddress == idAddress) {
                        userAddress = a;
                        break;
                    }
                }
            }
        }

        if (userAddress != null) {
            cep.setText(userAddress.CEP);
            address.setText(userAddress.Address);
            number.setText(userAddress.Number);
            celNumber.setText(userAddress.CellphoneNumber);
            phoneNumber.setText(userAddress.PhoneNumber);
            complement.setText(userAddress.Complement);
        }
    }

    private void initFunctions() {

        cep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && !TextUtils.isEmpty(cep.getText().toString())) {
                    if (cep.getText().toString().length() != 8) {
                        cep.setError("CEP deve conter 8 dígitos");
                        return;
                    }
                    createDialogLoading();
                    new GetAddressInfoTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cep.getText().toString());
                }
            }
        });
        cep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cep.getText().length() == 8) {
                    address.requestFocus();
                }
            }
        });
        final boolean[] init = {true};
        neighborhood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (init[0]) {
                        init[0] = false;
                        return;
                    }
                    Toast.makeText(getContext(), "Selecione um bairro", Toast.LENGTH_SHORT).show();

                    return;
                }

                Neighborhood n = neighborhoods.get(position);
                idNeighborhood = n.Id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final boolean[] initCity = {true};
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    if (initCity[0]) {
                        initCity[0] = false;
                        return;
                    }
                    Toast.makeText(getContext(), "Selecione uma cidade", Toast.LENGTH_SHORT).show();

                    return;
                }

                City c = cities.get(position);
                idCity = c.Id;

                createDialogLoading();

                new GetAllNeighborhood().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createDialogSaveAddress() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Aguarde");
        builder.setMessage("Estamos realizando o cadastro do endereço");
        builder.setCancelable(false);
        dialogSave = builder.create();
        dialogSave.show();
    }

    private void createDialogLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Aguarde");
        builder.setMessage("Atualizando algumas informações");
        builder.setCancelable(false);
        dialogUpdateInfo = builder.create();
        dialogUpdateInfo.show();
    }

    private void initButtons() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address addressModel = new Address();
                addressModel.Number = number.getText().toString();
                addressModel.IdNeighborhood = idNeighborhood;
                addressModel.Complement = complement.getText().toString();
                addressModel.CEP = cep.getText().toString();
                addressModel.Address = address.getText().toString();
                addressModel.CellphoneNumber = celNumber.getText().toString();
                addressModel.PhoneNumber = phoneNumber.getText().toString();

                if (validate(addressModel)) {
                    createDialogSaveAddress();
                    if (userAddress != null) {
                        addressModel.IdAddress = userAddress.IdAddress;
                    }
                    new RegisterAddressTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, addressModel);
                }
            }
        });
    }

    private boolean validate(Address addressModel) {
        boolean error = false;
        StringBuilder strB = new StringBuilder();

        View viewFocus = null;

        if (addressModel.Address.isEmpty()) {
            error = true;
            strB.append("Endereço é obrigatório");
            viewFocus = address;
            address.setError("Endereço é obrigatório");
        }

        if (addressModel.CellphoneNumber.isEmpty()) {
            error = true;
            strB.append("\r\nNúmero de celular é obrigatório");
            celNumber.setError("Número de celular é obrigatório");
            if (viewFocus == null) {
                viewFocus = celNumber;
            }
        }
        if (addressModel.CEP.isEmpty()) {
            error = true;
            strB.append("\r\nO CEP é obrigatório");
            cep.setError("Cep é obrigatório");
            if (viewFocus == null) {
                viewFocus = cep;
            }
        }
        if (addressModel.Complement.isEmpty()) {
            error = true;
            strB.append("\r\nO Complemento é obrigatório");
            complement.setError("Complemento é obrigatório");
            if (viewFocus == null) {
                viewFocus = complement;
            }
        }
        if (addressModel.IdNeighborhood == 0) {
            error = true;
            strB.append("\r\nSelecione o bairro");

        }
        if (addressModel.Number.isEmpty()) {
            error = true;
            strB.append("\r\nNúmero da rua é obrigatório");
            number.setError("Número da rua é obrigatório");
            if (viewFocus == null) {
                viewFocus = number;
            }
        }

        if (viewFocus != null) {
            viewFocus.requestFocus();// setFocusable(true);
        }

        return !error;
    }

    private void initTasks() {
        createDialogLoading();
        new CityTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void initAdapter() {
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, cities);
        city.setAdapter(adapter);

        if (userAddress != null) {
            for (int i = 0; i < cities.size(); i++) {
                if (cities.get(i).Cidade.equals(userAddress.City)) {
                    city.setSelection(i, true);
                    break;
                }
            }
        }

        ArrayAdapter adapterPayment = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"Selecione uma cidade"});
        neighborhood.setAdapter(adapterPayment);
    }

    private void updateAdapterNeighborhood() {
        ArrayAdapter neighborhoodAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, neighborhoods);
        neighborhood.setAdapter(neighborhoodAdapter);

        if (userAddress != null && tempIdNeighborhood == 0) {

            for (int i = 0; i < neighborhoods.size(); i++) {
                if (neighborhoods.get(i).Id == userAddress.IdNeighborhood) {
                    neighborhood.setSelection(i, true);
                    break;
                }
            }

        } else if (tempIdNeighborhood > 0) {
            int position = 0;
            for (int i = 0; i < neighborhoods.size(); i++) {
                if (neighborhoods.get(i).Id == tempIdNeighborhood) {
                    position = i;
                    break;
                }

            }
            neighborhood.setSelection(position, true);
        }
    }

    private void CloseOK() {

        MessageBus bus = new MessageBus();
        bus.className = AddressFragment.class + "";
        bus.message = "enderecoCadastrado";
        EventBus.getDefault().post(bus);
        if (userAddress != null) {
            Toast.makeText(getContext(), "Endereço atualizado com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Endereço cadastrado com sucesso", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetAddressTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean loadSuccess) {

            super.onPostExecute(loadSuccess);
            if (!loadSuccess) {
                Toast.makeText(getContext(), "Erro ao recarregar os endereços", Toast.LENGTH_SHORT).show();
            }
            dialogSave.dismiss();
            CloseOK();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                userBusiness.loadAddressesInCache();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    private class RegisterAddressTask extends AsyncTask<Address, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                new GetAddressTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } else {
                Toast.makeText(getContext(), "Erro ao finalizar o cadastro", Toast.LENGTH_SHORT).show();
                dialogSave.dismiss();
            }
        }

        @Override
        protected Boolean doInBackground(Address... params) {

            try {
                if (userAddress != null) {
                    userBusiness.updateAddress(params[0]);
                } else {
                    userBusiness.insertAddress(params[0]);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    private class CityTask extends AsyncTask<Void, Void, List<City>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<City> cityList) {
            super.onPostExecute(cityList);
            if (cityList != null) {
                cities = null;
                cities = new ArrayList<>();
                City c = new City();
                c.Cidade = "Selecione";
                c.Id = 0;
                cities.add(c);
                cities.addAll(cityList);
                initAdapter();
            } else {
                Toast.makeText(getContext(), "Erro ao carregar as cidades", Toast.LENGTH_SHORT).show();
            }

            dialogUpdateInfo.dismiss();
        }

        @Override
        protected List<City> doInBackground(Void... params) {

            try {
                return userBusiness.getCities();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void setAddressModel(AddressDataModel addressModel) {
        address.setText(addressModel.Address);
        City c = null;
        int positionCity = 0;
        tempIdNeighborhood = addressModel.IdNeighborhood;
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).Id == addressModel.IdCity) {
                c = cities.get(i);
                positionCity = i;
                break;
            }
        }

        if (c != null) {

            if (city.getSelectedItemPosition() == positionCity) {
                createDialogLoading();

                new GetAllNeighborhood().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, idCity);
            } else {
                city.setSelection(positionCity, true);
            }

            //createDialogLoading();
            //new GetAllNeighborhood().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, c.Id);
        }

    }

    private class GetAddressInfoTask extends AsyncTask<String, Void, AddressDataModel> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(AddressDataModel addressDataModel) {
            super.onPostExecute(addressDataModel);

            if (addressDataModel != null) {
                dialogUpdateInfo.dismiss();
                setAddressModel(addressDataModel);
            }

        }

        @Override
        protected AddressDataModel doInBackground(String... params) {

            try {
                return userBusiness.getAddressInfo(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class GetAllNeighborhood extends AsyncTask<Integer, Void, List<Neighborhood>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Neighborhood> neighborhoodList) {
            super.onPostExecute(neighborhoodList);

            if (neighborhoodList != null) {
                neighborhoods = null;
                neighborhoods = new ArrayList<>();
                Neighborhood n = new Neighborhood();
                n.Bairro = "Selecione";
                n.Id = 0;
                neighborhoods.add(n);
                neighborhoods.addAll(neighborhoodList);
                updateAdapterNeighborhood();
            } else {
                Toast.makeText(getContext(), "Erro ao buscar os bairros da cidade", Toast.LENGTH_SHORT).show();
            }

            dialogUpdateInfo.dismiss();
        }

        @Override
        protected List<Neighborhood> doInBackground(Integer... params) {

            try {
                return userBusiness.getAllNeighborhood(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
