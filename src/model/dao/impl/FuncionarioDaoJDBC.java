package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

import db.DB;
import db.DbException;
import model.dao.FuncionarioDao;
import model.entities.Cliente;
import model.entities.Funcionario;

public class FuncionarioDaoJDBC implements FuncionarioDao {

	private Connection conn;
	
	public FuncionarioDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Funcionario obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO funcionario "
					+ "(Nome, Email, DataAniversaio, SalarioBase, ClienteId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataAniversario().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getCliente().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Funcionario obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE funcionario "
					+ "SET Nome = ?, Email = ?, DataAniversario = ?, SalarioBase = ?, ClienteId = ? "
					+ "WHERE Id = ?");
			
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataAniversario().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getCliente().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM funcionario WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Funcionario findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT funcionario.*,cliente.Nome as DepNome "
					+ "FROM funcionario INNER JOIN cliente "
					+ "ON funcionario.ClienteId = cliente.Id "
					+ "WHERE funionario.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Cliente dep = instantiateCliente(rs);
				Funcionario obj = instantiateFuncionario(rs, dep);
				return obj;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Funcionario instantiateFuncionario(ResultSet rs, Cliente dep) throws SQLException {
		Funcionario obj = new Funcionario();
		obj.setId(rs.getInt("Id"));
		obj.setNome(rs.getString("Nome"));
		obj.setEmail(rs.getString("Email"));
		obj.setSalarioBase(rs.getDouble("SalarioBase"));
		obj.setDataAniversario(rs.getDate("DataAniversário"));
		obj.setCliente(dep);
		return obj;
	}

	private Cliente instantiateCliente(ResultSet rs) throws SQLException {
		Cliente dep = new Cliente();
		dep.setId(rs.getInt("ClienteId"));
		dep.setNome(rs.getString("ClienteNome"));
		return dep;
	}

	@Override
	public List<Funcionario> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT funcionario.*,cliente.Nome as DepNome "
					+ "FROM funcionario INNER JOIN cliente "
					+ "ON funcionario.ClienteId = cliente.Id "
					+ "ORDER BY Nome");
			
			rs = st.executeQuery();
			
			List<Funcionario> list = new ArrayList<>();
			Map<Integer, Cliente> map = new HashMap<>();
			
			while (rs.next()) {
				
				Cliente dep = map.get(rs.getInt("ClienteId"));
				
				if (dep == null) {
					dep = instantiateCliente(rs);
					map.put(rs.getInt("ClienteId"), dep);
				}
				
				Funcionario obj = instantiateFuncionario(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Funcionario> findByDepartment(Cliente cliente) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT funcionario.*,cliente.Nome as DepNome "
					+ "FROM funcionario INNER JOIN cliente "
					+ "ON funcionario.ClienteId = cliente.Id "
					+ "WHERE ClienteId = ? "
					+ "ORDER BY Nome");
			
			st.setInt(1, cliente.getId());
			
			rs = st.executeQuery();
			
			List<Funcionario> list = new ArrayList<>();
			Map<Integer, Cliente> map = new HashMap<>();
			
			while (rs.next()) {
				
				Cliente dep = map.get(rs.getInt("ClienteId"));
				
				if (dep == null) {
					dep = instantiateCliente(rs);
					map.put(rs.getInt("ClienteId"), dep);
				}
				
				Funcionario obj = instantiateFuncionario(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
