package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	/* Excess�o personalizada que vai carregar uma cole��o contendo todos
	 * os erros poss�veis
	 * */
	
	private Map<String, String> errors = new HashMap<>();
	
	public ValidationException(String msg) {
		super(msg);
	}
	
	public Map<String, String> getErrors(){
		return errors;
	}
	
	public void addError(String fieldNome, String errorMessage) {
		errors.put(fieldNome, errorMessage);
	}
	
}
