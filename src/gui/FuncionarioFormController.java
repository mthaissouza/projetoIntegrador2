package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Funcionario;
import model.exceptions.ValidationException;
import model.services.FuncionarioService;

public class FuncionarioFormController implements Initializable{

	private Funcionario entity;
	
	private FuncionarioService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtNome;
	
	@FXML
	private TextField txtEmail;
	
	@FXML
	private DatePicker dpDataAniversario;
	
	@FXML
	private TextField txtSalarioBase;
	
	@FXML
	private Label labelErrorNome;
	
	@FXML
	private Label labelErrorEmail;
	
	@FXML
	private Label labelErrorDataAniversario;
	
	@FXML
	private Label labelErrorSalarioBase;
	
	@FXML
	private Button btSalvar;
	
	@FXML
	private Button btCancelar;
	
	public void setFuncionario(Funcionario entity) {
		this.entity = entity;
	}
	
	public void setFuncionarioService (FuncionarioService service) {
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
	private Funcionario getFormData() {
		Funcionario obj = new Funcionario();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		//verifica se o campo nome é nulo
		if(txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "O campo não pode ser vazio!");
		}
		obj.setNome(txtNome.getText());
		
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
		Constraints.setTextFieldMaxLength(txtNome, 60);
		Constraints.setTextFieldDouble(txtSalarioBase);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpDataAniversario, "dd/MM/yyyy");
	}
	
	//pega os dados dos objetos e coloca na tabela
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtSalarioBase.setText(String.format("%.2f", entity.getSalarioBase()));
		
		//verifica se a data não é nula, caso essa verificação nao ocorra acontece um NullPointerException
		if(entity.getDataAniversario() != null) {
			dpDataAniversario.setValue(LocalDate.ofInstant(entity.getDataAniversario().toInstant(), ZoneId.systemDefault()));
		}	
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("nome")) {
			labelErrorNome.setText(errors.get("nome"));
		}
	}
	
}
