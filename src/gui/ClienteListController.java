package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Cliente;
import model.services.ClienteService;

public class ClienteListController implements Initializable, DataChangeListener{
	
	//declaração de dependencia da classe 'ClienteService'
	private ClienteService service; 

	// Associação com os elementos da tela 'Registar Clientes'
	@FXML
	private TableView<Cliente> tableViewCliente; 
	
	@FXML
	private TableColumn<Cliente, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Cliente, String> tableColumnNome;
	
	@FXML
	private TableColumn<Cliente, Cliente> tableColumnEDIT;
	
	@FXML
	private TableColumn<Cliente, Cliente> tableColumnREMOVE;
		
	@FXML
	private Button btRegistrar;
	
	private ObservableList<Cliente> obsList; 
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Cliente obj = new Cliente();
		createClienteForm(obj, "/gui/ClienteForm.fxml", parentStage);
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
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
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
		initEditButtons();
		initRemoveButtons();
	}
	
	//carrega a tela para cadastrar um novo cliente
	private void createClienteForm(Cliente obj, String absoluteName, Stage parentStage) {
		try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			//injeção de dependências
			ClienteFormController controller = loader.getController();
			controller.setCliente(obj);
			controller.setClienteService(new ClienteService());
			
			controller.subscribeDataChangeListener(this);
			
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Insira o nome do Cliente:");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		}
		catch(IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Cliente, Cliente>(){
			private final Button button = new Button("editar");
			
			@Override
			protected void updateItem(Cliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createClienteForm(obj, "/gui/ClienteForm.fxml", Utils.currentStage(event)));
			}
		});
		
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Cliente, Cliente>(){
			private final Button button = new Button("remover");
			
			@Override
			protected void updateItem(Cliente obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
		
	}

	private void removeEntity(Cliente obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmação", "Tem certeza que deseja excluir?");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj);
				updateTableView();
			}
			catch(DbIntegrityException e) {
				Alerts.showAlert("Erro a remover objeto", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
	
}
