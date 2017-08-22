package br.com.irweb.ajshf.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import br.com.irweb.ajshf.Application.AJSHFApp;
import br.com.irweb.ajshf.Entities.ItemOrder;
import br.com.irweb.ajshf.Entities.Order;
import br.com.irweb.ajshf.R;

/**
 * Created by Igor on 09/08/2017.
 */

public class ItemCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context _context;
    private Order _order;
    private LayoutInflater _inflater;
    private ItemAdapterBtnClick itemAdapterBtnClick;

    public ItemCartAdapter(Context context) {
        _context = context;
        _order = AJSHFApp.getOrder();
        _inflater = LayoutInflater.from(context);
    }

    public void setItemAdapterBtnClick(ItemAdapterBtnClick itemAdapterBtnClick){
        this.itemAdapterBtnClick = itemAdapterBtnClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = _inflater.inflate(R.layout.layout_adapter_item_cart, parent, false);

        return new ItemCartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemCartViewHolder v = (ItemCartViewHolder) holder;
        ItemOrder item = _order.Items.get(position);
        v.itemTotal.setText((item.Value * item.Quantity) + "");
        v.itemQtd.setText(item.Quantity + "");
        v.itemValor.setText(item.Value + "");
        v.itemProduct.setText(item.Name);
    }

    @Override
    public int getItemCount() {
        if (_order != null && _order.Items != null && _order.Items.size() > 0) {
            return _order.Items.size();
        }

        return 0;
    }

    private class ItemCartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageButton itemRemove;
        public TextView itemQtd;
        public TextView itemValor;
        public TextView itemTotal;
        public TextView itemProduct;

        public ItemCartViewHolder(View itemView) {
            super(itemView);
            itemRemove = (ImageButton) itemView.findViewById(R.id.item_remove);
            itemProduct = (TextView) itemView.findViewById(R.id.title_product);
            itemQtd = (TextView) itemView.findViewById(R.id.item_qtd);
            itemValor = (TextView) itemView.findViewById(R.id.item_valor);
            itemTotal = (TextView) itemView.findViewById(R.id.item_total);

            itemRemove.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(itemAdapterBtnClick != null){
                itemAdapterBtnClick.onClickRemove(getAdapterPosition());
            }
        }
    }

    public interface ItemAdapterBtnClick {
        void onClickRemove(int position);

        void onClickCard(int position);
    }
}
