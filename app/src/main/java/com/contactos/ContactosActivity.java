package com.contactos;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ContactosActivity extends ListActivity {
    ListAdapter adapter;
    DBAdapter datasource;
    Button btNovoContacto;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.main);

       datasource = new DBAdapter(this);
       datasource.open();
       Cursor cursor = datasource.getContactos();
        
       String[] columns = new String[] { "nome","telefone" };
       int[] to = new int[] { R.id.nome, R.id.telefone};
        
       adapter = new SimpleCursorAdapter(
			this, 
			R.layout.contacto_list_item, 
			cursor, 
			columns, 
			to);        
		this.setListAdapter(adapter);
		datasource.close();
		
		 btNovoContacto = (Button) findViewById(R.id.btNovoContacto);
		    
		    btNovoContacto.setOnClickListener(new View.OnClickListener() {
		    	public void onClick(View v) {
		    		// TODO Auto-generated method stub
		    		Intent novo = new Intent("com.contactos.NovoContacto");
		    		startActivity(novo);
		    	     }
		    	});
    }
    
    @Override
   	protected void onResume() {
   		// TODO Auto-generated method stub
   		super.onResume();

   		datasource.open();
           	Cursor cursor = datasource.getContactos();        
           	String[] columns = new String[] { "nome","telefone" };
           	int[] to = new int[] { R.id.nome, R.id.telefone};
           	adapter = new SimpleCursorAdapter(
   				this, 
   				R.layout.contacto_list_item, 
   				cursor, 
   				columns, 
   				to);        
   		this.setListAdapter(adapter);
   		datasource.close();
   	}
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent("com.contactos.ContactosDetalhes");
        	Cursor cursor = (Cursor) adapter.getItem(position);
    intent.putExtra("idContacto",cursor.getInt(cursor.getColumnIndex("_id")));
        	startActivity(intent);		
    }



}
