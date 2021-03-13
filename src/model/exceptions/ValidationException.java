package model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	/* Excessão personalizada que vai carregar uma coleção contendo todos
	 * os erros possíveis
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
