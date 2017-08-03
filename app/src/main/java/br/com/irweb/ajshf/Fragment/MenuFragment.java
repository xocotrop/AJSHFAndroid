package br.com.irweb.ajshf.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import br.com.irweb.ajshf.API.Exception.ApiException;
import br.com.irweb.ajshf.API.Service.FoodService;
import br.com.irweb.ajshf.Adapter.MenuItemViewHolderAdapter;
import br.com.irweb.ajshf.Entities.Food;
import br.com.irweb.ajshf.Helpers.StringHelper;
import br.com.irweb.ajshf.R;
import br.com.irweb.ajshf.Service.CartService;

public class MenuFragment extends Fragment {

    private RecyclerView recyclerViewMenu;
    private MenuItemViewHolderAdapter adapter;
    private FoodService service;
    private List<Food> mFoods;
    private CartService cartService;


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
        cartService = new CartService(getContext());
        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new MenuTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setListInAdapter() {
        if (mFoods != null) {
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
        adapter.setItemAdapterBtnClick(new MenuItemViewHolderAdapter.ItemAdapterBtnClick() {
            @Override
            public void onClickBtn(int position, int quantity) {
                Food f = mFoods.get(position);
                try {
                    cartService.addItemOrder(f, quantity);
                    Toast.makeText(getContext(), String.format("%s adicionado com sucesso ao carrinho", f.Title), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    if (e.getMessage() == "Food is null") {
                        Toast.makeText(getContext(), "Houve um erro ao adicionar o Item no carrinho", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onClickCard(int position) {
                showDialogFood(position);
            }

        });
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
//        recyclerViewMenu.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerViewMenu, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                showDialogFood(position);
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));

        return v;
    }

    private void showDialogFood(int position) {
        Food f = mFoods.get(position);

        View v = LayoutInflater.from(getContext()).inflate(R.layout.food_detail_popup, null, false);
        ImageView img = (ImageView) v.findViewById(R.id.img);
        TextView txt = (TextView) v.findViewById(R.id.text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txt.setText(Html.fromHtml(f.Description, Html.FROM_HTML_MODE_LEGACY));
        } else {
            txt.setText(Html.fromHtml(f.Description));
        }
        if (StringHelper.checkValidUrlImage(f.Image))
            Picasso.with(getContext()).load(f.Image).placeholder(R.drawable.ic_update_black_24dp).into(img);
        else
            Picasso.with(getContext()).load(R.drawable.ic_do_not_disturb_black_24dp).into(img);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(f.Title);

        builder.setView(v);

        builder.setNeutralButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setTitle(f.Title);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
            } else if (messageError == null || messageError.isEmpty()) {
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

    private static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

        private OnItemClickListener mListener;

        private GestureDetector gestureDetector;

        public RecyclerItemClickListener(Context context, RecyclerView recyclerView, OnItemClickListener itemClickListener) {
            mListener = itemClickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && gestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongItemClick(View view, int position);
        }
    }
}
