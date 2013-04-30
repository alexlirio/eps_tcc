package br.com.eps.jsf.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.jboss.logging.Logger;

@FacesConverter(value="cepConverter", forClass=String.class)
public class CepConverter implements Converter {
	
	static Logger log = Logger.getLogger(CepConverter.class);

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
			if (cnpj.replaceAll("\\D", "").length() == 8) {
				return cnpj.replaceAll("(\\d{5})(\\d{3})", "$1-$2");
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "CEP impossível de se converter", "CEP impossível de se converter");
				throw new ConverterException(msg);
			}
			
		} catch (ConverterException e) {
			log.error("CEP impossível de se converter: " + cnpj);
			throw e;
		}

	}
	

	 public static void main(String[] args) {
	 System.out.println(converte("12345678"));
	 System.out.println(converte("123456789"));
	 System.out.println(converte("000000000000000"));
	 System.out.println(converte("asdsdaasdasdad"));
	 }
	
}
