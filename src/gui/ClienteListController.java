package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Cliente;

public class ClienteListController implements Initializable{

	// Associação com os elementos da tela 'Registar Clientes'
	@FXML
	private TableView<Cliente> tableViewCliente; 
	
	@FXML
	private TableColumn<Cliente, String> tableColumnNome;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnEmail;
	
	@FXML
	private Button btRegistrar;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// método auxiliar para inicializar elementos na tela
		initializeNodes();
	}
	
	//inicializa os componentes da tabela
	private void initializeNodes() {
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
		
		//faz com que a os elementos da tela se ajustem ao tamanho da janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewCliente.prefHeightProperty().bind(stage.heightProperty());
	}
	
}
