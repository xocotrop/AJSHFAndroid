package br.com.irweb.ajshf.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.irweb.ajshf.Entities.Address;
import br.com.irweb.ajshf.R;

/**
 * Created by Igor on 29/09/2017.
 */

public class ItemAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Address> addresses;

    public ItemAddressAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public ItemAddressAdapter(Context context, List<Address> addresses){
        this(context);

        if(addresses != null){
            this.addresses = addresses;
        }
    }

    public void setAddresses(List<Address> addresses){
        if(addresses != null){
            this.addresses = addresses;
        }
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_address, parent, false);

        MyViewHolder v = new MyViewHolder(view);

        return v;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder v = (MyViewHolder) holder;

        Address a = addresses.get(position);
        String addressText = String.format("%s, %s - %s",a.Address, a.Number, a.Neighborhood);
        v.txt.setText(addressText);
        v.txtCellNumber.setText(a.CellphoneNumber);
        v.txtCep.setText(a.CEP);
    }

    @Override
    public int getItemCount() {
        return addresses == null ? 0 : addresses.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt;
        TextView txtCep;
        TextView txtCellNumber;
        public MyViewHolder(View itemView) {
            super(itemView);

            txt = (TextView) itemView.findViewById(R.id.txt_address);
            txtCellNumber = (TextView) itemView.findViewById(R.id.txt_cellnumber);
            txtCep = (TextView) itemView.findViewById(R.id.txt_cep);

        }
    }


}
