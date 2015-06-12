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

public class NovoContacto extends Activity {
	
	Button btadicionar;
	Button btTirarFoto;
	EditText edtNome;
	EditText edtEmail;
	EditText edtTelefone;
	ImageView iv;

	final static int cameraData = 0;
	private DBAdapter datasource;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
			setContentView(R.layout.novocontacto);
			
			datasource = new DBAdapter(this);
			edtNome = (EditText) findViewById(R.novocontacto.ednome);
			edtEmail = (EditText) findViewById(R.novocontacto.edEmail);
			edtTelefone = (EditText) findViewById(R.novocontacto.edTelefone);
			btadicionar =  (Button) findViewById(R.novocontacto.btadicionar);
			btTirarFoto =  (Button) findViewById(R.novocontacto.tirarFoto);
			iv = (ImageView) findViewById(R.novocontacto.ivReturnedPic);

			btTirarFoto.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(i,cameraData);
				}
			});
			
			btadicionar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
						// TODO Auto-generated method stub
						datasource.open();
				Contacto c = datasource.createContacto(edtNome.getText().toString(), edtEmail.getText().toString(), edtTelefone.getText().toString(),loadBitmapFromView(iv));
						datasource.close();
						AlertDialog.Builder dialogo = new
							AlertDialog.Builder(NovoContacto.this);
							dialogo.setTitle("Aviso");
							dialogo.setMessage("Contacto:" + c.getNome());			
				dialogo.setNeutralButton("OK", new DialogInterface.OnClickListener() {  
								   public void onClick(DialogInterface dialog, int id) {  
								    finish();
							      }  
							});  
							dialogo.show(); 
						}
				});
			



	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
		if(resultCode == RESULT_OK){
			Bundle extras = data.getExtras();
			Bitmap bmp = (Bitmap) extras.get("data");
			iv.setImageBitmap(bmp);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public static Bitmap loadBitmapFromView (View v) {
		Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);                
	    Canvas c = new Canvas(b);
	    v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
	    v.draw(c);
	    return b;
}

}


