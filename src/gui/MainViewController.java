package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.ClienteService;

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
		loadView2("/gui/ClienteList.fxml");
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
		loadView("/gui/Sobre.fxml");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
	}
	
	private void loadView(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			//pega a referencia da cena principal
			Scene mainScene = Main.getMainScene();
			
			//pega referencia para o VBox da tela principal
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			//referencia para o menu
			Node mainMenu = mainVBox.getChildren().get(0);
			
			//limpa todos os filhos do VBox
			mainVBox.getChildren().clear();
			
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exceptiom", "Erro decarregamento", e.getMessage(), AlertType.ERROR);
		}
	}	
	
	private void loadView2(String absoluteName) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();
			
			//pega a referencia da cena principal
			Scene mainScene = Main.getMainScene();
			
			//pega referencia para o VBox da tela principal
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			//referencia para o menu
			Node mainMenu = mainVBox.getChildren().get(0);
			
			//limpa todos os filhos do VBox
			mainVBox.getChildren().clear();
			
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			ClienteListController controller = loader.getController();
			controller.setClienteService(new ClienteService());
			controller.updateTableView();
			
		} catch (IOException e) {
			Alerts.showAlert("IO Exceptiom", "Erro decarregamento", e.getMessage(), AlertType.ERROR);
		}
	}

}
