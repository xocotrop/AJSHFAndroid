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

    public MenuItemViewHolderAdapter(Context context, List<Food> foods) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        if (foods != null) {
            mFoods = foods;
        }
    }

    public void setFoods(List<Food> foods) {
        mFoods = foods;
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
        holder.img.setImageDrawable(null);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mFoods != null ? mFoods.size() : 0;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView title;
        private Button btnAdd;
        private EditText itemQuantity;
        private CardView cardView;

        public FoodViewHolder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.img);
            title = (TextView) itemView.findViewById(R.id.title);
            btnAdd = (Button) itemView.findViewById(R.id.addButton);
            itemQuantity = (EditText) itemView.findViewById(R.id.itemQuantity);
            cardView = (CardView) itemView.findViewById(R.id.itemCard);
        }
    }
}
