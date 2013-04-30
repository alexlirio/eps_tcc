package br.com.eps.jsf.validator;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("EmailValidator")
public class EmailValidator implements Validator {
	
	private static final String PATTERN = 	"^" + // inicio
											"[a-zA-Z_0-9]{1}" +
											"[a-zA-Z_0-9.-]*" +
											"@" +
											"[a-zA-Z_0-9]{1}" +
											"[a-zA-Z_0-9-.]*" +
											"." +
											"[a-zA-Z]{2,4}" +
											"$" // fim
											;
	
	private static Pattern pattern = Pattern.compile(PATTERN);
	
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {

        if (!valida(String.valueOf(value))) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email inválido", "Email inválido");
            throw new ValidatorException(msg);
        }
	}
	
	private static boolean valida(String email) {
		return pattern.matcher(email).matches();
	}
	
	public static void main(String[] args) {
		System.out.println(valida("nono@nono.no"));
		System.out.println(valida("nono@nono.non.no6"));
		System.out.println(valida("nono@o45o.no.d"));
	}

}