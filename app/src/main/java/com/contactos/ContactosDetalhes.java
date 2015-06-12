package com.contactos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactosDetalhes extends Activity {

    int idContacto;
    DBAdapter datasource;
    Contacto contacto;
    TextView edtNome;
    TextView edtEmail;
    TextView edtTelefone;
    Button btVoltar;
    Button btEliminar;
    Button btEditar;
    ImageView ivFoto;

    @Override
    protected void onResume() {
        super.onResume();
        carregaDetalhesContacto();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhecontacto);


        edtNome = (TextView) findViewById(R.lista.txtnome);
        edtEmail = (TextView) findViewById(R.lista.txtEmail);
        edtTelefone = (TextView) findViewById(R.lista.txtTelefone);
        ivFoto = (ImageView) findViewById(R.id.ivFoto);
        btVoltar = (Button) findViewById(R.lista.btmenu);
        btEditar = (Button) findViewById(R.id.btEditar);
        btEliminar = (Button) findViewById(R.id.btEliminar);

        carregaDetalhesContacto();

        btVoltar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });


        btEditar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent editar = new Intent("com.contactos.EditarContacto");
                editar.putExtra("idContacto", idContacto);
                startActivity(editar);
            }
        });

        btEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder dialogo = new AlertDialog.Builder(ContactosDetalhes.this);
                dialogo.setTitle("Aviso");
                dialogo.setMessage("Eliminar Contacot?");
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                dialogo.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        datasource.open();
                        datasource.EliminaContacto(idContacto);
                        datasource.close();
                        finish();
                    }
                });
                dialogo.show();
            }
        });

    }

    private void carregaDetalhesContacto() {
        idContacto = getIntent().getIntExtra("idContacto", 0);

        datasource = new DBAdapter(this);
        datasource.open();
        contacto = datasource.getContacto(idContacto);
        datasource.close();
        ivFoto.setImageBitmap(contacto.getFoto());
        edtNome.setText(contacto.getNome());
        edtEmail.setText(contacto.getEmail());
        edtTelefone.setText(contacto.getTelefone());
    }

}
