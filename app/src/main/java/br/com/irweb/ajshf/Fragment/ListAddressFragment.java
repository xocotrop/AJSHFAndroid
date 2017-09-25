package br.com.irweb.ajshf.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.irweb.ajshf.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListAddressFragment extends Fragment {


    public ListAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_address, container, false);
    }

}
