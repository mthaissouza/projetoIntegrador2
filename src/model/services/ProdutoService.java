package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProdutoDAO;
import model.entities.Produto;

public class ProdutoService {

private ProdutoDAO dao = DaoFactory.createProdutoDAO();
	
	public List<Produto> findAll(){
		return dao.findAll();
	}
	
	//insere um novo cliente no banco ou atualiza um existente
	public void saveOrUpdate(Produto obj) {
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	//remove um produto
	public void remove(Produto obj) {
		dao.deleteById(obj.getId());
	}
	
}
