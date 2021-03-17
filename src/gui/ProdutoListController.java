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
import model.entities.Produto;
import model.services.ProdutoService;

public class ProdutoListController implements Initializable, DataChangeListener{

	private ProdutoService service; 
	
	@FXML
	private TableView<Produto> tableViewProduto; 
	
	@FXML
	private TableColumn<Produto, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Produto, String> tableColumnNome;
	
	@FXML
	private TableColumn<Produto, Double> tableColumnPreco;
	
	@FXML
	private TableColumn<Produto, Integer> tableColumnQuantidade;
	
	@FXML
	private TableColumn<Produto, Produto> tableColumnEDIT;
	
	@FXML
	private TableColumn<Produto, Produto> tableColumnREMOVE;
		
	@FXML
	private Button btRegistrar;
	
	private ObservableList<Produto> obsList; 
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Produto obj = new Produto();
		createProdutoForm(obj, "/gui/ProdutoForm.fxml", parentStage);
	}
	
	//injeção da dependência
	public void setProdutoService(ProdutoService service) {
			this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// método auxiliar para inicializar elementos na tela
		initializeNodes();
	}
	
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
		Utils.formatTableColumnDouble(tableColumnPreco, 2);
		tableColumnQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
		
		//faz com que a os elementos da tela se ajustem ao tamanho da janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewProduto.prefHeightProperty().bind(stage.heightProperty());
		
	}
	
	//carrega os departamentos e o insere no ObservableList
		public void updateTableView() {
			if(service == null) {
				throw new IllegalStateException("Serviço Nulo!");
			}
			List<Produto> list = service.findAll();
			
			//faz com que a lista seja carregada
			obsList = FXCollections.observableArrayList(list);
			
			tableViewProduto.setItems(obsList);
			initEditButtons();
			initRemoveButtons();
		}

	//carrega a tela para cadastrar um novo cliente
		private void createProdutoForm(Produto obj, String absoluteName, Stage parentStage) {
			try {
				
				FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
				Pane pane = loader.load();
				
				//injeção de dependências
				ProdutoFormController controller = loader.getController();
				controller.setProduto(obj);
				controller.setProdutoService(new ProdutoService());
				
				controller.subscribeDataChangeListener(this);
				
				controller.updateFormData();
				
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Lista de Produtos:");
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
			tableColumnEDIT.setCellFactory(param -> new TableCell<Produto, Produto>(){
				private final Button button = new Button("editar");
				
				@Override
				protected void updateItem(Produto obj, boolean empty) {
					super.updateItem(obj, empty);
					if (obj == null) {
						setGraphic(null);
						return;
					}
					setGraphic(button);
					button.setOnAction(
							event -> createProdutoForm(obj, "/gui/ProdutoForm.fxml", Utils.currentStage(event)));
				}
			});
			
		}
		
		private void initRemoveButtons() {
			tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			tableColumnREMOVE.setCellFactory(param -> new TableCell<Produto, Produto>(){
				private final Button button = new Button("remover");
				
				@Override
				protected void updateItem(Produto obj, boolean empty) {
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
		
		private void removeEntity(Produto obj) {
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
