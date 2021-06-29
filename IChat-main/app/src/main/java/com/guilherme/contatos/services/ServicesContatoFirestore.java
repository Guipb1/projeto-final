package com.guilherme.contatos.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.guilherme.contatos.database.DataSingleton;
import com.guilherme.contatos.model.Contato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServicesContatoFirestore {

    private static ServicesContatoFirestore instance;
    private FirebaseFirestore firestore;
    private  FirebaseUser user;
    private boolean status;
    private ArrayList<Contato> contatoArrayList;



    private  ServicesContatoFirestore(Context context){
        firestore = FirebaseFirestore.getInstance();
        user = ServiceGoogleAuthentication.getInstance(context).GetUser();

    }

    public static ServicesContatoFirestore getInstance(Context context){
        if(instance == null){
            instance = new ServicesContatoFirestore(context);
        }
        return instance;
    }


    public ArrayList<Contato> GetListaContato(){

        contatoArrayList = new ArrayList<Contato>();

        firestore.collection("Contatos")
                .whereEqualTo("id_user", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            DataSingleton.getInstance().getContatos().clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TESTE_FIREBASE",document.get("nome").toString());
                                Contato contato_item = new Contato(
                                document.getData().get("nome").toString(),
                                Integer.parseInt(document.getData().get("ddd").toString()),
                                document.getData().get("telefone").toString(),
                                document.getData().get("EnderecoImage").toString()
                                );
                                contato_item.setId(document.getId());
                                DataSingleton.getInstance().addContato(contato_item);
                                contatoArrayList.add(contato_item);
                                DataSingleton.getInstance().getArrayAdapter_contato().notifyDataSetChanged();
                            }
                            if(task.getResult().isEmpty()){
                                DataSingleton.getInstance().getContatos().clear();
                                DataSingleton.getInstance().getArrayAdapter_contato().notifyDataSetChanged();
                            }



                        } else {

                        }
                    }
                });



        return  DataSingleton.getInstance().getContatos();

    }

    public Contato GetContato(int index){
        return DataSingleton.getInstance().contatos.get(index);
    }

    public boolean AdicionarContato(Contato contato){
        status =  false;
        Map<String, Object> data = new HashMap<>();
        data.put("nome",contato.getNome());
        data.put("ddd",contato.getDdd());
        data.put("telefone",contato.getTelefone());
        data.put("EnderecoImage",contato.getEnderecoImage());
        data.put("id_user",user.getUid());
        firestore.collection("Contatos").document(contato.getId()).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                status = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                status = false;
            }
        });

      return status;

    }

    public boolean DeleteContato(String id_contato){
        status = false;
        firestore.collection("Contatos").document(id_contato)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        status = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status = false;
                    }
                });
        return status;
    }


}
