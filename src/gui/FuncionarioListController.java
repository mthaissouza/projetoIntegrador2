package gui;

import java.net.URL;
import java.util.Date;
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
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Funcionario;
import model.services.FuncionarioService;

public class FuncionarioListController implements Initializable, DataChangeListener{
	
	//declaração de dependencia da classe 'FuncionarioService'
	private FuncionarioService service; 

	// Associação com os elementos da tela 'Registar Funcionarios'
	@FXML
	private TableView<Funcionario> tableViewFuncionario; 
	
	@FXML
	private TableColumn<Funcionario, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnNome;
	
	@FXML
	private TableColumn<Funcionario, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Funcionario, Date> tableColumnDataAniversario;
	
	@FXML
	private TableColumn<Funcionario, Double> tableColumnSalarioBase;
	
	@FXML
	private TableColumn<Funcionario, Funcionario> tableColumnEDIT;
	
	@FXML
	private TableColumn<Funcionario, Funcionario> tableColumnREMOVE;
		
	@FXML
	private Button btRegistrar;
	
	private ObservableList<Funcionario> obsList; 
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Funcionario obj = new Funcionario();
		createFuncionarioForm(obj, "/gui/FuncionarioForm.fxml", parentStage);
	}
	
	//injeção da dependência
	public void setFuncionarioService(FuncionarioService service) {
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
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnDataAniversario.setCellValueFactory(new PropertyValueFactory<>("dataAniversario"));
		Utils.formatTableColumnDate(tableColumnDataAniversario, "dd/MM/yyyy");
		tableColumnSalarioBase.setCellValueFactory(new PropertyValueFactory<>("salarioBase"));
		Utils.formatTableColumnDouble(tableColumnSalarioBase, 2);
		
		//faz com que a os elementos da tela se ajustem ao tamanho da janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewFuncionario.prefHeightProperty().bind(stage.heightProperty());
	}
	
	//carrega os departamentos e o insere no ObservableList
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Serviço Nulo!");
		}
		List<Funcionario> list = service.findAll();
		
		//faz com que a lista seja carregada
		obsList = FXCollections.observableArrayList(list);
		
		tableViewFuncionario.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}
	
	//carrega a tela para cadastrar um novo cliente
	private void createFuncionarioForm(Funcionario obj, String absoluteName, Stage parentStage) {
		/*try {
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();
			
			//injeção de dependências
			FuncionarioFormController controller = loader.getController();
			controller.setFuncionario(obj);
			controller.setFuncionarioService(new FuncionarioService());
			
			controller.subscribeDataChangeListener(this);
			
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Insira o nome do Funcionario:");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}*/
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Funcionario, Funcionario>(){
			private final Button button = new Button("editar");
			
			@Override
			protected void updateItem(Funcionario obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createFuncionarioForm(obj, "/gui/FuncionarioForm.fxml", Utils.currentStage(event)));
			}
		});
		
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Funcionario, Funcionario>(){
			private final Button button = new Button("remover");
			
			@Override
			protected void updateItem(Funcionario obj, boolean empty) {
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

	private void removeEntity(Funcionario obj) {
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
