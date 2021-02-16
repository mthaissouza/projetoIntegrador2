package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Cliente;

public class ClienteService {
	
	public List<Cliente> findAll(){
		List<Cliente> list = new ArrayList<>();
		list.add(new Cliente("Ana", "ana@gmail.com"));
		list.add(new Cliente("João", "joao@gmail.com"));
		list.add(new Cliente("Nina", "nina@gmail.com"));
		return list;
	}
	
}
