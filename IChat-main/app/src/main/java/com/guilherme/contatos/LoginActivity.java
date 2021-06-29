package com.guilherme.contatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.guilherme.contatos.database.DataSingleton;
import com.guilherme.contatos.pages.ListaContato;
import com.guilherme.contatos.services.ServiceGoogleAuthentication;

public class LoginActivity extends AppCompatActivity {


    private ServiceGoogleAuthentication serviceGoogleAuth;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        serviceGoogleAuth = ServiceGoogleAuthentication.getInstance(this);
        button = findViewById(R.id.buttonEntrar);
    }


public void SignIn(View view ){
    Intent signInIntent = serviceGoogleAuth.mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, serviceGoogleAuth.RC_SIGN_IN);
    button.setText("Carregando...");
}


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean Auth = serviceGoogleAuth.SignIn(requestCode,this,data);
        if(Auth){
            Intent intent = new Intent(LoginActivity.this, ListaContato.class);
            this.startActivity(intent);
        }else{
            Intent signInIntent = serviceGoogleAuth.mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, serviceGoogleAuth.RC_SIGN_IN);
            boolean Auth2 = serviceGoogleAuth.SignIn(requestCode,this,data);
            if(Auth2) {
                Intent intent = new Intent(LoginActivity.this, ListaContato.class);
                this.startActivity(intent);
            }else{
                Toast.makeText(this,"Erro ao entrar com google", Toast.LENGTH_LONG);
                button.setText("Entrar com o google");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        button.setText("Entrar com o google");
        // Check if user is signed in (non-null) and update UI accordingly.

        if(serviceGoogleAuth.GetUser()!=null){
            DataSingleton.getInstance().setUser(serviceGoogleAuth.GetUser());
            Intent intent = new Intent(LoginActivity.this, ListaContato.class);
            this.startActivity(intent);
        }

    }


}