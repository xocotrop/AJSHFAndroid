package br.com.irweb.ajshf.Fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.Voice;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import br.com.irweb.ajshf.Adapter.ItemAddressAdapter;
import br.com.irweb.ajshf.Bus.MessageBus;
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

    public static ListAddressFragment newInstance(){
        ListAddressFragment fragment = new ListAddressFragment();

        return fragment;
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

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Address address = allAddress.get(position);
                MessageBus bus = new MessageBus();
                bus.className = ListAddressFragment.class + "";
                bus.message = "openAddress";
                bus.additionalInfo = address.IdAddress + "";
                EventBus.getDefault().post(bus);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

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

    private class GetAllAddress extends AsyncTask<Void, Void, List<Address>> {

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
        protected List<Address> doInBackground(Void... params) {

            try {
                return userBusiness.getAllAddress();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onLongItemClick(View view, int position);
    }

    private class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
    }

}
