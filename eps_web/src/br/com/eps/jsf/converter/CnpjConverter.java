package br.com.eps.jsf.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.jboss.logging.Logger;

@FacesConverter(value="cnpjConverter", forClass=String.class)
public class CnpjConverter implements Converter {
	
	static Logger log = Logger.getLogger(CnpjConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
        if (value == null || "".equals(value.trim())) {
        	return null;
        }
        
        return ((String)value).replaceAll("\\D", "");
	
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (value == null) {
			return "";
		}
		
		return converte((String)value);

	}

	public static String converte(String cnpj) {
		
		try {
			if (cnpj.replaceAll("\\D", "").length() == 14) {
				return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ impossível de se converter", "CNPJ impossível de se converter");
				throw new ConverterException(msg);
			}
			
		} catch (ConverterException e) {
			log.error("CNPJ impossível de se converter: " + cnpj);
			throw e;
		}

	}
	

	 public static void main(String[] args) {
	 System.out.println(converte("12345678901234"));
	 System.out.println(converte("1234567890123"));
	 System.out.println(converte("000000000000000"));
	 System.out.println(converte("asdsdaasdasdad"));
	 }
	
}
