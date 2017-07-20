package br.com.irweb.ajshf.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.API.Service.FoodService;
import br.com.irweb.ajshf.Adapter.MenuItemViewHolderAdapter;
import br.com.irweb.ajshf.Entities.Food;
import br.com.irweb.ajshf.R;

public class MenuFragment extends Fragment {

    private RecyclerView recyclerViewMenu;
    private MenuItemViewHolderAdapter adapter;
    private FoodService service;
    private List<Food> mFoods;


    private OnFragmentInteractionListener mListener;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = new FoodService(getContext());
        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new MenuTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setListInAdapter(){
        if(mFoods != null){
            adapter.setFoods(mFoods);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerViewMenu = (RecyclerView) v.findViewById(R.id.menu_items);

        adapter = new MenuItemViewHolderAdapter(getContext(), null);
        recyclerViewMenu.setAdapter(adapter);
        recyclerViewMenu.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewMenu.setLayoutManager(layoutManager);

        recyclerViewMenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //// TODO: talvez precisa remover isso aqui se nao for necessário
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class MenuTask extends AsyncTask<Object, Object, List<Food>> {
        private String messageError;

        @Override
        protected void onPostExecute(List<Food> foods) {
            super.onPostExecute(foods);

            if (foods != null) {
                mFoods = foods;
                setListInAdapter();
            }
            else if(messageError == null || messageError.isEmpty()){
                Toast.makeText(getContext(), messageError, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Food> doInBackground(Object... params) {
            List<Food> foods = null;
            try {
                foods = service.GetFood();
            } catch (IOException e) {
                e.printStackTrace();

            } catch (ApiException e) {
                messageError = e.getMessage();
            }
            return foods;
        }
    }
}
