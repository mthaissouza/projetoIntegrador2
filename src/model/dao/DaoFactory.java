package model.dao;

import db.DB;
import model.dao.impl.ClienteDaoJDBC;
import model.dao.impl.FuncionarioDaoJDBC;
import model.dao.impl.ProdutoDaoJDBC;

public class DaoFactory {

	public static FuncionarioDao createFuncionarioDao() {
		return new FuncionarioDaoJDBC(DB.getConnection());
	}
	
	public static ClienteDao createClienteDao() {
		return new ClienteDaoJDBC(DB.getConnection());
	}
	
	public static ProdutoDAO createProdutoDAO() {
		return new ProdutoDaoJDBC(DB.getConnection());
	}
}
