package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Produto;
import model.exceptions.ValidationException;
import model.services.ProdutoService;

public class ProdutoFormController implements Initializable{

	private Produto entity;
	
	private ProdutoService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtPreco;
	
	@FXML
	private TextField txtQuantidade;
	
	@FXML
	private Label labelErrorNome;
	
	@FXML
	private Label labelErrorPreco;
	
	@FXML
	private Label labelErrorQuantidade;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	public void setProduto(Produto entity) {
		this.entity = entity;
	}
	
	public void setProdutoService (ProdutoService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtSalvarAction(ActionEvent event) {
		// verificando as injeções de dependência
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			// salva no bd
			service.saveOrUpdate(entity);
			
			notifyDataChangeListeners();
			
			//fecha a tela após a inserção
			Utils.currentStage(event).close();
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving objetc", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	//pega os dados inseridos no formulário e retorna
	private Produto getFormData() {
		Produto obj = new Produto();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		//verifica se o campo nome é nulo
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "Campo vazio!!");
		}
		obj.setNome(txtNome.getText());
		
		if(txtPreco.getText() == null || txtPreco.getText().trim().equals("")) {
			exception.addError("nome", "Campo vazio!");
		}
		obj.setPreco(Utils.tryParseToDouble(txtPreco.getText()));
		
		if(txtQuantidade.getText() == null || txtQuantidade.getText().trim().equals("")) {
			exception.addError("nome", "Campo vazio!");
		}
		obj.setQuantidade(Utils.tryParseToInt(txtQuantidade.getText()));
		
		//verifica se tem algum erro na coleção
		if(exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}
	@FXML
	public void onBtCancelarAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 30);
		Constraints.setTextFieldDouble(txtPreco);
		Constraints.setTextFieldInteger(txtQuantidade);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		Locale.setDefault(Locale.US);
		txtPreco.setText(String.format("%.2f", entity.getPreco()));
		txtQuantidade.setText(String.valueOf(entity.getQuantidade()));

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorNome.setText((fields.contains("nome") ? errors.get("nome") : ""));
		labelErrorPreco.setText((fields.contains("preco") ? errors.get("preco") : ""));
		labelErrorQuantidade.setText((fields.contains("quantidade") ? errors.get("quantidade") : ""));
	}
	
}
