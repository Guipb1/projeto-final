package com.guilherme.contatos.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.guilherme.contatos.R;
import com.guilherme.contatos.components.arraysAdapters.ArrayAdapter_Contato;
import com.guilherme.contatos.database.DataSingleton;
import com.guilherme.contatos.model.Contato;
import com.guilherme.contatos.services.ServiceGoogleAuthentication;
import com.guilherme.contatos.services.ServicesContatoFirestore;

import java.util.ArrayList;

public class ListaContato extends AppCompatActivity {

    ArrayAdapter_Contato arrayAdapter_contato;
    RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_lista_contato);
        listView = findViewById(R.id.lista_contato);
        Context context = this.getApplicationContext();
        arrayAdapter_contato = new ArrayAdapter_Contato(context);
        DataSingleton.getInstance().setArrayAdapter_contato(arrayAdapter_contato);
        listView.setAdapter(arrayAdapter_contato);
        listView.setLayoutManager(new LinearLayoutManager(this));

        ServicesContatoFirestore.getInstance(ListaContato.this).GetListaContato();
        arrayAdapter_contato.setClickListener(new ArrayAdapter_Contato.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Contato  contato = DataSingleton.getInstance().contatos.get(position);
                Intent intent = new Intent(ListaContato.this, AdicionarContato.class);
                intent.putExtra("ID_CONTATO",String.valueOf(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(int position, View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ListaContato.this);
                builder.setTitle("Tem certeza que deseja excluir ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ServicesContatoFirestore.getInstance(ListaContato.this).DeleteContato(DataSingleton.getInstance().contatos.get(position).getId());
                        ServicesContatoFirestore.getInstance(ListaContato.this).GetListaContato();
                        arrayAdapter_contato.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
                builder.show();

                return true;
            }

        });

        arrayAdapter_contato.notifyDataSetChanged();

    }

    public void floatButton(View view){

        Intent intent = new Intent(this,AdicionarContato.class);
        startActivity(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        arrayAdapter_contato.notifyDataSetChanged();

    }

    public void SignOut(View view){
        ServiceGoogleAuthentication.getInstance(ListaContato.this).SignOut();
        finish();
    }

}