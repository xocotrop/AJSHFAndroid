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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import br.com.irweb.ajshf.Business.UserBusiness;
import br.com.irweb.ajshf.Entities.Client;
import br.com.irweb.ajshf.R;


public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView nome;
    private AutoCompleteTextView email;
    private EditText senha;
    private EditText confirmarSenha;
    private Button btnRegister;
    private UserBusiness userBusiness;
    private CheckBox atualizacoes;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userBusiness = new UserBusiness(this);

        nome = (AutoCompleteTextView) findViewById(R.id.nome);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.password);
        confirmarSenha = (EditText) findViewById(R.id.confirm_password);
        atualizacoes = (CheckBox) findViewById(R.id.check_updates);

        btnRegister = (Button) findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Client client = new Client();
                    String[] nameUser = nome.getText().toString().split(" ");
                    client.Name = nameUser[0];
                    client.LastName = getLastName(nameUser);
                    client.Email = email.getText().toString();
                    client.Password = senha.getText().toString();
                    client.ConfirmPassword = confirmarSenha.getText().toString();
                    client.ReceiveUpdates = atualizacoes.isChecked();

                    createAlertDialog();

                    new RegisterTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, client);
                }
            }
        });

    }

    private String getLastName(String[] nameUser) {
        String lastName = "";
        if (nameUser != null && nameUser.length > 1) {
            for (int i = 1; i < nameUser.length; i++) {
                if (i == 1) {
                    lastName += nameUser[i];
                } else {
                    lastName += " " + nameUser[i];
                }
            }
        }
        return lastName;
    }

    private boolean validate() {
        boolean error = false;
        StringBuilder strB = new StringBuilder();

        if (nome.getText().toString().isEmpty()) {
            error = true;
            strB.append("O campo nome é Obrigatorio");
        }
        if (!nome.getText().toString().isEmpty()) {
            if (nome.getText().toString().split(" ").length <= 1) {
                error = true;
                strB.append("\nO nome deve ser completo");
            }
        }
        if (email.getText().toString().isEmpty()) {
            error = true;
            strB.append("\nO campo e-mail e obrigatório");
        }
        if (senha.getText().toString().isEmpty()) {
            error = true;
            strB.append("\nO campo senha e obrigatorio");
        }
        if (confirmarSenha.getText().toString().isEmpty()) {
            error = true;
            strB.append("\nO campo confirmar senha e obrigatorio");
        }
        if (!senha.getText().toString().isEmpty() && !confirmarSenha.toString().isEmpty()) {
            if (senha.getText().toString().equals(confirmarSenha.toString())) {
                error = true;
                strB.append("\nAs senhas precisam ser iguais");
            }
        }
        if (error) {
            Toast.makeText(this, strB.toString(), Toast.LENGTH_SHORT).show();
        }
        return !error;

    }

    private void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
        builder.setTitle("Aguarde");
        builder.setMessage("Realizando o cadastro");
        builder.setCancelable(false);
        alertDialog = builder.create();
    }

    private class RegisterTask extends AsyncTask<Client, Void, Boolean> {

        private String message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            alertDialog.show();
        }


        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);

            if (aVoid) {
                successRegister();
            } else {
                alertDialog.dismiss();
                Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected Boolean doInBackground(Client... params) {

            try {
                userBusiness.Register(params[0]);
                return true;
            } catch (Exception e) {
                message = e.getMessage();
                e.printStackTrace();
            }

            return false;
        }
    }

    private void successRegister() {
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
    }

}
