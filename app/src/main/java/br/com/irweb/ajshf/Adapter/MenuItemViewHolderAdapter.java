package br.com.irweb.ajshf.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import br.com.irweb.ajshf.Entities.Food;
import br.com.irweb.ajshf.R;


/**
 * Created by Igor on 30/06/2017.
 */

public class MenuItemViewHolderAdapter extends RecyclerView.Adapter<MenuItemViewHolderAdapter.FoodViewHolder> {

    private Context mContext;
    private List<Food> mFoods;
    private LayoutInflater inflater;
    private ItemAdapterBtnClick itemAdapterBtnClick;
    private HashMap<Integer, Integer> qtyPosition = new HashMap<Integer, Integer>();

    public MenuItemViewHolderAdapter(Context context, List<Food> foods) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        if (foods != null) {
            mFoods = foods;
        }
    }

    public void setItemAdapterBtnClick(ItemAdapterBtnClick itemAdapterBtnClick) {
        this.itemAdapterBtnClick = itemAdapterBtnClick;
    }

    public void setFoods(List<Food> foods) {
        mFoods = foods;
        notifyDataSetChanged();
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_food, parent, false);

        FoodViewHolder f = new FoodViewHolder(v);

        return f;
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        Food f = mFoods.get(position);

        holder.title.setText(f.Title);
        int qty = 0;
        if (qtyPosition.containsKey(position)) {
            qty = qtyPosition.get(position);
        }
        holder.itemQuantity.setText(qty + "");
//        holder.img.setImageDrawable(null);

        Picasso.with(mContext).load(f.Image.replace(" ", "%20")).fit().into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mFoods != null ? mFoods.size() : 0;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;
        private TextView title;
        private Button btnAdd;
        private EditText itemQuantity;
        private CardView cardView;
        private Button btnPlus;
        private Button btnMinus;

        public FoodViewHolder(final View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img);
            title = (TextView) itemView.findViewById(R.id.title);
            btnAdd = (Button) itemView.findViewById(R.id.addButton);
            btnPlus = (Button) itemView.findViewById(R.id.btn_plus);
            btnMinus = (Button) itemView.findViewById(R.id.btn_minus);
            itemQuantity = (EditText) itemView.findViewById(R.id.itemQuantity);
            cardView = (CardView) itemView.findViewById(R.id.itemCard);

            btnAdd.setOnClickListener(this);
            cardView.setOnClickListener(this);
            btnPlus.setOnClickListener(this);
            btnMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                if (v.getId() == R.id.addButton) {
                    if (itemAdapterBtnClick != null) {
                        String qtd;
                        if (itemQuantity.getText().toString().isEmpty()) {
                            qtd = "1";
                        } else {
                            qtd = itemQuantity.getText().toString();
                        }
                        itemAdapterBtnClick.onClickBtn(getAdapterPosition(), Integer.parseInt(qtd));
                    }
                } else if (v.getId() == R.id.btn_plus) {
                    int qtd = 0;
                    String qtdStr = itemQuantity.getText().toString();


                    if (qtdStr.isEmpty()) {
                        qtdStr = "0";
                        qtd = Integer.valueOf(qtdStr);
                        qtd++;
                    } else {
                        qtd = Integer.valueOf(qtdStr);
                        qtd++;
                    }

                    qtyPosition.put(getAdapterPosition(), qtd);

                    itemQuantity.setText(qtd + "");
                } else if (v.getId() == R.id.btn_minus) {
                    int qtd = 0;
                    String qtdStr = itemQuantity.getText().toString();
                    if (!qtdStr.isEmpty()) {
                        qtd = Integer.valueOf(qtdStr);
                        qtd--;
                        if (qtd < 0) {
                            qtd = 0;
                        }
                    }
                    qtyPosition.put(getAdapterPosition(), qtd);
                    itemQuantity.setText(qtd + "");
                }
            } else if (v instanceof CardView) {
                if (itemAdapterBtnClick != null) {
                    itemAdapterBtnClick.onClickCard(getAdapterPosition());
                }
            }
        }
    }

    public interface ItemAdapterBtnClick {
        void onClickBtn(int position, int quantity);

        void onClickCard(int position);
    }
}
