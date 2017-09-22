package br.com.irweb.ajshf.Activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.irweb.ajshf.Business.UserBusiness;
import br.com.irweb.ajshf.R;


public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView nome;
    private AutoCompleteTextView email;
    private EditText senha;
    private EditText confirmarSenha;
    private Button btnRegister;
    private UserBusiness userBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userBusiness = new UserBusiness(this);

        nome = (AutoCompleteTextView) findViewById(R.id.nome);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.password);
        confirmarSenha = (EditText) findViewById(R.id.confirm_password);

        btnRegister = (Button) findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

    }

    private class RegisterTask extends AsyncTask<Void, Void, Boolean>{

        private String message;
        private AlertDialog alertDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
            builder.setTitle("Aguarde");
            builder.setMessage("Realizando o cadastro");
            builder.setCancelable(false);
            alertDialog = builder.create();

            alertDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);

            if(aVoid){
                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                builder.setTitle("Sucesso");
                builder.setMessage("Seu cadastro foi criado com sucesso, agora você já pode se logar no app =D");
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.setCancelable(false);

                builder.create().show();
            } else {
                alertDialog.dismiss();
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                userBusiness.Register(null);
                return true;
            } catch (Exception e) {
                message = e.getMessage();
                e.printStackTrace();
            }

            return false;
        }
    }

}
