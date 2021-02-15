package model.entities;

import java.io.Serializable;

import model.services.Login;

public class Cliente implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// Atributos
	private String nome;
	private String email;
	private String senha;
	private Login login;
	
	//Construtores
	public Cliente() {
	}
	
	public Cliente(String nome, String email, String senha) {
		super();
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}
	
	
	//Métodos get e set
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
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	//toString
	@Override
	public String toString() {
		return "Cliente [nome=" + nome + ", email=" + email + ", senha=" + senha + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
	
	public void alterarDados(String nome, String email, String senha) {
		this.email = email;
		this.nome = nome;
		this.senha = senha;
	}
	
	public void excluirConta() {
		
	}
}

