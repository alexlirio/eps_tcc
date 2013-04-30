package br.com.eps.jsf.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("CnpjValidator")
public class CnpjValidator implements Validator {

	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		if (!valida(String.valueOf(value))) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ inválido", "CNPJ inválido");
			throw new ValidatorException(msg);
		}

	}

	public static boolean valida(String cnpj) {
		
		if (cnpj == null || cnpj.length() != 14 ||
			cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
			cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
			cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
			cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
			cnpj.equals("88888888888888") || cnpj.equals("99999999999999")) {
			
			return false;
			
		}

		try {
			Long.parseLong(cnpj);
		} catch (NumberFormatException e) { // Não possui somente números
			return false;
		}

		int soma = 0;
		String cnpj_calc = cnpj.substring(0, 12);
		char chr_cnpj[] = cnpj.toCharArray();

		for (int i = 0; i < 4; i++) {
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
			}
		}

		for (int i = 0; i < 8; i++){
			if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9){
				soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
			}
		}

		int dig = 11 - soma % 11;

		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(
					dig != 10 && dig != 11 ? Integer.toString(dig) : "0")
					.toString();
		soma = 0;

		for (int i = 0; i < 5; i++) {
			if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
				soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
			}
		}

		for (int i = 0; i < 8; i++) {
			if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
				soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
			}
		}

		dig = 11 - soma % 11;
		cnpj_calc = (new StringBuilder(String.valueOf(cnpj_calc))).append(
					dig != 10 && dig != 11 ? Integer.toString(dig) : "0")
					.toString();

		return cnpj.equals(cnpj_calc);

	}

	 public static void main(String[] args) {
	 System.out.println(valida("31217174000134"));
	 System.out.println(valida("12345678901234"));
	 System.out.println(valida("00000000000000"));
	 }

}