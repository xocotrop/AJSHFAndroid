package br.com.irweb.ajshf.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import br.com.irweb.ajshf.R;


public class RegisterActivity extends AppCompatActivity {

    private AutoCompleteTextView nome;
    private AutoCompleteTextView email;
    private EditText senha;
    private EditText confirmarSenha;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nome = (AutoCompleteTextView) findViewById(R.id.nome);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.password);
        confirmarSenha = (EditText) findViewById(R.id.confirm_password);

        btnRegister = (Button) findViewById(R.id.btn_register);

    }

}
