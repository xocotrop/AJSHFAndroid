package br.com.irweb.ajshf.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.Voice;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import br.com.irweb.ajshf.Adapter.ItemAddressAdapter;
import br.com.irweb.ajshf.Business.UserBusiness;
import br.com.irweb.ajshf.Entities.Address;
import br.com.irweb.ajshf.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListAddressFragment extends Fragment {

    private UserBusiness userBusiness;
    private List<Address> allAddress;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    public ListAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        userBusiness = new UserBusiness(getContext());

        View v = inflater.inflate(R.layout.fragment_list_address, container, false);
        progressBar = (ProgressBar) v.findViewById(R.id.progress);
        recyclerView = (RecyclerView) v.findViewById(R.id.address_list);

        initAdapter();

        init();

        return v;
    }

    private void initAdapter() {
        ItemAddressAdapter addressAdapter = new ItemAddressAdapter(getContext());
        recyclerView.setAdapter(addressAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void init() {
        new GetAllAddress().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setAllAddress(List<Address> addresses) {
        if (addresses != null && addresses.size() > 0) {
            allAddress = null;
            allAddress = addresses;

            ((ItemAddressAdapter)recyclerView.getAdapter()).setAddresses(allAddress);

            showList();
        }
    }

    private void showList() {
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private class GetAllAddress extends AsyncTask<Voice, Void, List<Address>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgress();
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            super.onPostExecute(addresses);
            if (addresses != null) {
                setAllAddress(addresses);
            }
        }

        @Override
        protected List<Address> doInBackground(Voice... params) {

            try {
                return userBusiness.getAllAddress();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}
