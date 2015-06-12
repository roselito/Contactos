package com.contactos;

import java.io.ByteArrayOutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DBAdapter {
	private SQLiteDatabase database;
	private DbHelper dbHelper;
	private String[] allColumns = { DbHelper.ID, DbHelper.NOME, DbHelper.EMAIL, DbHelper.TELEFONE, DbHelper.FOTO};

	public DBAdapter(Context context) {
		dbHelper = new DbHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public Contacto createContacto(String nome, String email, String telefone, Bitmap foto) {
		ContentValues values = new ContentValues();
		values.put(dbHelper.NOME, nome);
		values.put(dbHelper.EMAIL,email);
		values.put(dbHelper.TELEFONE,telefone);
			
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		foto.compress(Bitmap.CompressFormat.PNG, 100, baos);  
		byte[] photo = baos.toByteArray(); 
			
		values.put(dbHelper.FOTO, photo);
			
		long insertId = database.insert(dbHelper.TABLE_NAME, null, values);
		// To show how to query
		Cursor cursor = database.query(dbHelper.TABLE_NAME, allColumns, dbHelper.ID + " = " + insertId, null,null, null, null);
		cursor.moveToFirst();
		return cursorToContacto(cursor);
	}
	
	public void EliminaContacto (int idContacto){
		//database.delete(DB.TABLE_NAME, "id=?", new String [] {Integer.toString(idContacto)});
		database.delete(DbHelper.TABLE_NAME, DbHelper.ID + " = " + idContacto, null);
		}
	private Contacto cursorToContacto(Cursor cursor) {
		byte[] blob = cursor.getBlob(cursor.getColumnIndex(dbHelper.FOTO));
	      	Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
	Contacto contacto = new Contacto(cursor.getLong(0),cursor.getString(1),cursor.getString(2), cursor.getString(3),bmp);
		return contacto;
	}

	public Cursor getContactos(){
		Cursor cursor = database.rawQuery("select _id, nome,telefone,foto from contactos2", null);
			return cursor;
		}
	public Contacto getContacto (int idContacto){
		Cursor cursor = database.query(dbHelper.TABLE_NAME, allColumns, dbHelper.ID + " = " + idContacto, null,null, null, null);
			cursor.moveToFirst();
		return cursorToContacto(cursor);
		}


}
