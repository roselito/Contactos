package com.contactos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditarContacto extends Activity {

    final static int cameraData = 0;
    int idContacto;
    Contacto contacto;
    Button btadicionar;
    Button btTirarFoto;
    EditText edtNome;
    EditText edtEmail;
    EditText edtTelefone;
    ImageView iv;
    private DBAdapter datasource;

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editarcontacto);

        datasource = new DBAdapter(this);
        edtNome = (EditText) findViewById(R.editarcontacto.ednome);
        edtEmail = (EditText) findViewById(R.editarcontacto.edEmail);
        edtTelefone = (EditText) findViewById(R.editarcontacto.edTelefone);
        btadicionar = (Button) findViewById(R.editarcontacto.btadicionar);
        btTirarFoto = (Button) findViewById(R.editarcontacto.tirarFoto);
        iv = (ImageView) findViewById(R.editarcontacto.ivReturnedPic);

        carregaDetalhesContacto();

        btTirarFoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, cameraData);
            }
        });

        btadicionar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                datasource.open();
                Contacto c = datasource.updateContacto(idContacto, edtNome.getText().toString(), edtEmail.getText().toString(), edtTelefone.getText().toString(), loadBitmapFromView(iv));
                datasource.close();
                AlertDialog.Builder dialogo = new
                        AlertDialog.Builder(EditarContacto.this);
                dialogo.setTitle("Aviso");
                dialogo.setMessage("Contacto:" + c.getNome());
                dialogo.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                dialogo.show();
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");
            iv.setImageBitmap(bmp);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void carregaDetalhesContacto() {
        idContacto = getIntent().getIntExtra("idContacto", 0);

        datasource = new DBAdapter(this);
        datasource.open();
        contacto = datasource.getContacto(idContacto);
        datasource.close();

        iv.setImageBitmap(contacto.getFoto());
        edtNome.setText(contacto.getNome());
        edtEmail.setText(contacto.getEmail());
        edtTelefone.setText(contacto.getTelefone());
    }

}


