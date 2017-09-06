package br.com.irweb.ajshf.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import br.com.irweb.ajshf.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {

    private Button btnSendMail;
    private Button btnOpenSite;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about, container, false);
        btnSendMail = (Button) v.findViewById(R.id.btn_send_mail);
        btnOpenSite = (Button) v.findViewById(R.id.btn_open_site);

        btnSendMail.setOnClickListener(this);
        btnOpenSite.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_send_mail) {
            Intent it = new Intent(Intent.ACTION_SEND);
            it.setData(Uri.parse("mailto:"));
            it.setType("text/plain");
            it.putExtra(Intent.EXTRA_EMAIL, new String[]{ "contato@ajsgastronomia.com.br"} );
            it.putExtra(Intent.EXTRA_SUBJECT, "Contato pelo aplicativo");

            try {
                startActivity(Intent.createChooser(it, "Enviar email"));
            } catch (Exception e) {
                Toast.makeText(getContext(), "Erro ao enviar o email", Toast.LENGTH_SHORT).show();
            }
        } else {
            String site = "http://www.irweb.com.br";
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse(site));
            startActivity(it);
        }
    }

    public static Fragment newInstance() {

        Fragment fragment = new AboutFragment();
        return fragment;
    }
}
