package model.services;

import java.util.List;

import model.dao.ClienteDao;
import model.dao.DaoFactory;
import model.entities.Cliente;

public class ClienteService {
	
	private ClienteDao dao = DaoFactory.createClienteDao();
	
	public List<Cliente> findAll(){
		return dao.findAll();
	}
	
	//insere um novo cliente no banco ou atualiza um existente
	public void saveOrUpdate(Cliente obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	//remove um cliente
	public void remove(Cliente obj) {
		dao.deleteById(obj.getId());
	}
	
}
