package com.guilherme.contatos.pages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guilherme.contatos.R;
import com.guilherme.contatos.database.DataSingleton;
import com.guilherme.contatos.model.Contato;
import com.guilherme.contatos.services.ServicesContatoFirestore;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdicionarContato extends AppCompatActivity {

    TextView title;

    TextView nome;
    TextView ddd;
    TextView telefone;

    static final int CAMERA_PERMISSION_CODE = 2001;
    static final int CAMERA_INTENT_CODE = 3001;

    ImageView imageViewCamera;

    String picturePath;
    String id_contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_adicionar_contato);
        nome = findViewById(R.id.inputNome);
        ddd = findViewById(R.id.inputDDD);
        telefone = findViewById(R.id.inputTelefone);
        imageViewCamera = findViewById(R.id.foto_criar_contato);
        title = findViewById(R.id.title_criar_contato);
        id_contato = getIntent().getStringExtra("ID_CONTATO");
        if(id_contato != null){

            title.setText("Atualizar contato");
            Contato contato = ServicesContatoFirestore.getInstance(this).GetContato(Integer.parseInt(id_contato));
            nome.setText(contato.getNome());
            ddd.setText(String.valueOf(contato.getDdd()));
            telefone.setText(contato.getTelefone());
            picturePath = contato.getEnderecoImage();
            imageViewCamera.setImageURI(Uri.fromFile(new File(contato.getEnderecoImage())));

        }

    }

    public void criarContato(View view){

        String status = "Salvo com sucesso.";
        Contato contato = new Contato(nome.getText().toString(),Integer.parseInt(ddd.getText().toString().substring(0,2)),telefone.getText().toString(),picturePath);
        if(id_contato != null){
            status = "Atualizado com sucesso.";
            contato.setId(DataSingleton.getInstance().contatos.get(Integer.parseInt(id_contato)).getId());
        }


        ServicesContatoFirestore.getInstance(this).AdicionarContato(contato);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(status);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ServicesContatoFirestore.getInstance(AdicionarContato.this).GetListaContato();
                DataSingleton.getInstance().getArrayAdapter_contato().notifyDataSetChanged();
                finish();

            }
        });

        builder.show();

    }

    //**----------------- CAMERA APARTIR DAQUI PARA BAIXO -----------------**//

    public void buttonCameraClicked(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestCameraPermission();
        }else{
            sendCameraIntent();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestCameraPermission(){
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            if(checkSelfPermission(Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA
                },CAMERA_PERMISSION_CODE);
            }else{
                sendCameraIntent();
            }
        }else{
            Toast.makeText(AdicionarContato.this,
                    "No camera available",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                sendCameraIntent();
            }else{
                Toast.makeText(AdicionarContato.this,
                        "Camera Permission Denied",Toast.LENGTH_LONG).show();
            }
        }
    }


    void sendCameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION,true);
        if(intent.resolveActivity(getPackageManager()) != null){
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                    .format(new Date());
            String picName = "pic_"+timeStamp;
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File pictureFile = null;
            try {
                pictureFile = File.createTempFile(picName,".jpg",dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(pictureFile != null){
                picturePath = pictureFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(
                        AdicionarContato.this,
                        "com.guilherme.contatos.fileprovider",
                        pictureFile
                );
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent,CAMERA_INTENT_CODE);

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_INTENT_CODE){
            if(resultCode == RESULT_OK){
                File file = new File(picturePath);
                if(file.exists()){
                    imageViewCamera.setImageURI(Uri.fromFile(file));
                }
            }else{
                Toast.makeText(AdicionarContato.this,
                        "Problem getting the image from the camera app",
                        Toast.LENGTH_LONG).show();
            }
        }
    }





}