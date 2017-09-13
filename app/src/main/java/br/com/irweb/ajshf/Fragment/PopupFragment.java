package br.com.irweb.ajshf.Fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.irweb.ajshf.Entities.Food;
import br.com.irweb.ajshf.Helpers.StringHelper;
import br.com.irweb.ajshf.R;
import br.com.irweb.ajshf.View.HtmlTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopupFragment extends Fragment {

    private TextView title;
    private ImageView img;
    private HtmlTextView text;

    private Food food;

    public PopupFragment() {
        // Required empty public constructor
    }

    public PopupFragment newInstance(Food food) {
        PopupFragment fragment = new PopupFragment();

        Bundle b = new Bundle();
        b.putSerializable("food", food);

        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle b = getArguments();
            Food f = (Food) b.getSerializable("food");
            if (f != null) {
                food = f;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_popup, container, false);

        title = (TextView) v.findViewById(R.id.title);
        img = (ImageView) v.findViewById(R.id.img);
        text = (HtmlTextView) v.findViewById(R.id.text);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (food != null) {

            title.setText(food.Title);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                text.setText(Html.fromHtml(food.Description, Html.FROM_HTML_MODE_LEGACY));
            } else {
                text.setText(Html.fromHtml(food.Description));
            }
            if (StringHelper.checkValidUrlImage(food.Image)) {
                Picasso.with(getContext()).load(food.Image.replace(" ", "%20")).centerCrop().into(img, new Callback() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Log.e("ERROR", "Erro ao carregar a imagem: " + food.Image);
                    }
                });
            }
        }
    }
}
