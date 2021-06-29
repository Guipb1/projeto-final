package com.guilherme.contatos.model;

import java.util.UUID;

public class  Contato {
    public String Id;
    private String nome;
    private int  ddd;
    private String telefone;
    private String enderecoImage;


    public Contato(String nome, int ddd, String telefone, String enderecoImage) {
        this.Id = UUID.randomUUID().toString();
        this.nome = nome;
        this.ddd = ddd;
        this.telefone = telefone;
        this.enderecoImage = enderecoImage;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDdd() {
        return ddd;
    }

    public void setDdd(int ddd) {
        this.ddd = ddd;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEnderecoImage() {
        return enderecoImage;
    }

    public void setEnderecoImage(String enderecoImage) {
        this.enderecoImage = enderecoImage;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

}
