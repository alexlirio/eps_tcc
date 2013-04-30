package br.com.eps.jsf.validator;

import java.util.InputMismatchException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("CpfValidator")
public class CpfValidator implements Validator {

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		if (!valida(String.valueOf(value))) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF inválido", "CPF inválido");
			throw new ValidatorException(msg);
		}

	}

	public static boolean valida(String cpf) {

		if (cpf == null || cpf.length() != 11 ||
			cpf.equals("00000000000") || cpf.equals("11111111111") ||
			cpf.equals("22222222222") || cpf.equals("33333333333") ||
			cpf.equals("44444444444") || cpf.equals("55555555555") ||
			cpf.equals("66666666666") || cpf.equals("77777777777") ||
			cpf.equals("88888888888") || cpf.equals("99999999999")) {
			
			return false;
			
		}

	    char dig10, dig11;
	    int sm, i, r, num, peso;

		//
		// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
	    try {
	    	// Calculo do 1o. Digito Verificador
	    	sm = 0;
	    	peso = 10;
	      for (i=0; i<9; i++) {             
	    	  // converte o i-esimo caractere do CPF em um numero:
	    	  // por exemplo, transforma o caractere '0' no inteiro 0        
	    	  // (48 é a posicao de '0' na tabela ASCII)        
	    	  num = (int)(cpf.charAt(i) - 48);
	    	  sm = sm + (num * peso);
	    	  peso = peso - 1;
	      }
	 
	      r = 11 - (sm % 11);
	      
	      if ((r == 10) || (r == 11)){
	    	  dig10 = '0';
	    	  
	      } else {
	    	  dig10 = (char)(r + 48); // converte no respectivo caractere numerico
	      }
	 
	      // Calculo do 2o. Digito Verificador
	      sm = 0;
	      peso = 11;
	      
	      for(i=0; i<10; i++) {
	        num = (int)(cpf.charAt(i) - 48);
	        sm = sm + (num * peso);
	        peso = peso - 1;
	      }
	 
	      r = 11 - (sm % 11);
	      
	      if ((r == 10) || (r == 11)){
	    	  dig11 = '0';
	      } else {
	    	  dig11 = (char)(r + 48);
	      }
	 
	      // Verifica se os digitos calculados conferem com os digitos informados.
	      if ((dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10))) {
	    	  return true;
	      } else {
	    	  return false;
	      }
	      
	    } catch (InputMismatchException erro) {
	        return false;
	    }

	}

	public static void main(String[] args) {
		System.out.println(valida("46618232732"));
		System.out.println(valida("12312312312"));
		System.out.println(valida("00000000000"));
	}

}