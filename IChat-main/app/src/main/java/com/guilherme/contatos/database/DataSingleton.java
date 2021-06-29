package com.guilherme.contatos.database;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.guilherme.contatos.components.arraysAdapters.ArrayAdapter_Contato;
import com.guilherme.contatos.model.Contato;
import com.guilherme.contatos.services.ServicesContatoFirestore;

import java.util.ArrayList;

public class DataSingleton {

    public static ArrayList<Contato> contatos;
    public static ArrayAdapter_Contato arrayAdapter_contato;


    public DataSingleton() {
        contatos = new ArrayList<Contato>();
    }
    public FirebaseUser user;

    public static DataSingleton instance ;


    public static DataSingleton getInstance() {
        if(instance == null){
            instance = new DataSingleton();
        }

        return instance;
    }


    public ArrayList<Contato> getContatos() {


        return contatos;
    }

    public void setContatos(ArrayList<Contato> contatos) {
        this.contatos = contatos;
    }

    public void addContato(Contato contato){
        contatos.add(contato);
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {

        this.user = user;
    }

    public ArrayAdapter_Contato getArrayAdapter_contato() {
        return arrayAdapter_contato;
    }

    public  void setArrayAdapter_contato(ArrayAdapter_Contato arrayAdapter_contato) {
        DataSingleton.arrayAdapter_contato = arrayAdapter_contato;
    }
}
