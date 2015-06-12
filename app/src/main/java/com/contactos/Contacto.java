package com.contactos;

import android.graphics.Bitmap;
public class Contacto {
	private long _id;
	private String nome;
	private String email;
	private String telefone;
	private Bitmap foto;
	public Contacto(long id, String nome, String email,String telefone){
		this._id=id;
		this.nome=nome;
		this.email=email;
		this.telefone=telefone;
	}
	public Contacto(long id, String nome, String email,String telefone, Bitmap bmp){
		this._id=id;
		this.nome=nome;
		this.email=email;
		this.telefone=telefone;
		this.foto = bmp;
	}
	public Bitmap getFoto(){
		return foto;		
	}
	public void setFoto(Bitmap btm){
		this.foto= btm;
	}
	public long getId() {
		return _id;
	}
	public void setId(long id) {
		this._id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.nome = email;
	}
	
	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.nome = telefone;
	}
}

