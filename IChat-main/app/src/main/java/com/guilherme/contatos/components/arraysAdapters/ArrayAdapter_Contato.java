package com.guilherme.contatos.components.arraysAdapters;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guilherme.contatos.R;
import com.guilherme.contatos.database.DataSingleton;
import com.guilherme.contatos.model.Contato;
import com.guilherme.contatos.services.ServiceGoogleAuthentication;
import com.guilherme.contatos.services.ServicesContatoFirestore;

import java.io.File;
import java.util.ArrayList;


public class ArrayAdapter_Contato extends RecyclerView.Adapter<ArrayAdapter_Contato.ViewHolder> {

    private static ClickListener clickListener;
    private  Context context;


    public void setClickListener(ClickListener clickListener) {
        ArrayAdapter_Contato.clickListener = clickListener;
    }

    public ArrayAdapter_Contato(Context context) {
        this.context = context;

    }

    public interface ClickListener {

        void onItemClick(int position, View view);
        boolean onItemLongClick(int position,View view);
    }


    @NonNull
    @Override
    public ArrayAdapter_Contato.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) { //QUANDO ELE CRIAR
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.card_view_contato,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArrayAdapter_Contato.ViewHolder viewHolder, int i) { //QUANDO ELE APARECER PARA USUÁRIO, IRÁ CHAMAR ESSA FUNÇÃO



        //Contato contato = DataSingleton.getInstance().getContatos().get(i);
        Contato contato =  DataSingleton.getInstance().getContatos().get(i);
        viewHolder.nameContact.setText(contato.getNome());
        viewHolder.numberPhone.setText("("+contato.getDdd()+") "+contato.getTelefone());
        if(contato.getEnderecoImage() != null){
            File file = new File(contato.getEnderecoImage());
            viewHolder.imageView.setImageURI(Uri.fromFile(file));
        }


    }

    @Override
    public int getItemCount() {
        return DataSingleton.getInstance().getContatos().size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameContact;
        TextView numberPhone;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameContact = itemView.findViewById(R.id.nome_contato);
            numberPhone = itemView.findViewById(R.id.telefone);
            imageView = itemView.findViewById(R.id.foto_contato);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(clickListener==null)
                        return;
                    else
                        clickListener.onItemClick(getAdapterPosition(),view);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(clickListener == null)
                        return  false;
                    return clickListener.onItemLongClick(getAdapterPosition(),v);
                }
            });

        }

    }




}