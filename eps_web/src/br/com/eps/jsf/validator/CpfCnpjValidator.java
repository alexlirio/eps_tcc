package br.com.eps.jsf.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("CpfCnpjValidator")
public class CpfCnpjValidator implements Validator {
	
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		
		// Retira os caracteres que nao sao numeros
		String str = ((String)value).replaceAll("\\D", "");
		
		if (!valida(str)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF inválido", "CNPJ/CPF inválido");
            throw new ValidatorException(msg);
		}

	}
	
	private static boolean valida(String cnpjCpf) {
		
		if (cnpjCpf.length() == 14) {
			return CnpjValidator.valida(cnpjCpf);
		}
		
		if (cnpjCpf.length() == 11) {
			return CpfValidator.valida(cnpjCpf);
		}
		
		return false;

	}
	
	public static void main(String[] args) {
		 System.out.println(valida("31217174000134"));
		 System.out.println(valida("12345678901234"));
		 System.out.println(valida("00000000000000"));
		 
		 System.out.println(valida("46618232732"));
		 System.out.println(valida("12312312312"));
		 System.out.println(valida("00000000000"));
	}

}