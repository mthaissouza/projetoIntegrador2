package model.services;

import java.util.List;

import model.dao.FuncionarioDao;
import model.dao.DaoFactory;
import model.entities.Funcionario;

public class FuncionarioService {
	
	private FuncionarioDao dao = DaoFactory.createFuncionarioDao();
	
	public List<Funcionario> findAll(){
		return dao.findAll();
	}
	
	//insere um novo cliente no banco ou atualiza um existente
	public void saveOrUpdate(Funcionario obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	//remove um cliente
	public void remove(Funcionario obj) {
		dao.deleteById(obj.getId());
	}
	
}
