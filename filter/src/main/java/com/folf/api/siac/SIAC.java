package com.folf.api.siac;

import java.io.Serializable;

public class SIAC implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String usuario;
	private String token;
	
	public SIAC(){
		
	}
	public String obtenerTokenUsuario(String usuario, String password) {
		String usu = "aker";
		String pass = "aker";
		if (usu.equals(usuario) && pass.equals(password)) {
			this.usuario=usuario;
			return "akersftwsdsf224fsdfhghg";
		} else {
			return "";
		}
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
