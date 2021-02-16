package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Cliente;
import model.services.ClienteService;

public class ClienteListController implements Initializable{
	
	//declaração de dependencia da classe 'ClienteService'
	private ClienteService service; 

	// Associação com os elementos da tela 'Registar Clientes'
	@FXML
	private TableView<Cliente> tableViewCliente; 
	
	@FXML
	private TableColumn<Cliente, String> tableColumnNome;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnEmail;
	
	@FXML
	private Button btRegistrar;
	
	private ObservableList<Cliente> obsList; 
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	//injeção da dependência
	public void setClienteService(ClienteService service) {
		this.service = service;
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
	
	//carrega os departamentos e o insere no ObservableList
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Serviço Nulo!");
		}
		List<Cliente> list = service.findAll();
		
		//faz com que a lista seja carregada
		obsList = FXCollections.observableArrayList(list);
		
		tableViewCliente.setItems(obsList);
	}
	
}
