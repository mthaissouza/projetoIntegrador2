package model.entities;

import model.services.Login;

public class Cliente {
	
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
	
	public void alterarDados(String nome, String email, String senha) {
		this.email = email;
		this.nome = nome;
		this.senha = senha;
	}
	
	public void excluirConta() {
		
	}
}

