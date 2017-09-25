package br.com.irweb.ajshf.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.irweb.ajshf.Business.UserBusiness;
import br.com.irweb.ajshf.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddressFragment extends Fragment {

    private UserBusiness userBusiness;

    private EditText cep;
    private EditText address;
    private EditText number;
    private Spinner city;
    private Spinner neighborhood;
    private EditText complement;
    private EditText phoneNumber;
    private EditText celNumber;


    public AddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_address, container, false);
        return v;
    }

    private class RegisterAddressTask extends AsyncTask<Void, Void, Void>{

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

    private class GetAddressInfoTask extends AsyncTask<Void, Void, Void>{

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
