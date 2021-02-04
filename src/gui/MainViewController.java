package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable{
	//Atributos para os itens de menu
	@FXML
	private MenuItem menuItemCliente;
	
	@FXML
	private MenuItem menuItemFuncionario;
	
	@FXML
	private MenuItem menuItemLivraria;
	
	@FXML
	private MenuItem menuItemLogin;
	
	@FXML
	private MenuItem menuItemSobre;
	
	//metodos para tratar os itens de menu
	@FXML
	public void onMenuItemClienteAction() {
		System.out.println("onMenuItemClienteAction");
	}
	
	@FXML
	public void onMenuItemFuncionarioAction() {
		System.out.println("onMenuItemFuncionarioAction");
	}
	
	@FXML
	public void onMenuItemLivrariaAction() {
		System.out.println("onMenuItemLivrariaAction");
	}
	
	@FXML
	public void onMenuItemLoginAction() {
		System.out.println("onMenuItemLoginAction");
	}
	
	@FXML
	public void onMenuItemSobreAction() {
		System.out.println("onMenuItemSobreAction");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}

}
