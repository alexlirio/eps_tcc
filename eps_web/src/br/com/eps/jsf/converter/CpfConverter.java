package br.com.eps.jsf.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.jboss.logging.Logger;

@FacesConverter(value="cpfConverter", forClass=String.class)
public class CpfConverter implements Converter {
	
	static Logger log = Logger.getLogger(CpfConverter.class);

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
			if (cnpj.replaceAll("\\D", "").length() == 11) {
				return cnpj.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF impossível de se converter", "CPF impossível de se converter");
				throw new ConverterException(msg);
			}
			
		} catch (ConverterException e) {
			log.error("CPF impossível de se converter: " + cnpj);
			throw e;
		}

	}
	

	 public static void main(String[] args) {
	 System.out.println(converte("12312312312"));
	 System.out.println(converte("1234567890123"));
	 System.out.println(converte("000000000000000"));
	 System.out.println(converte("asdsdaasdasdad"));
	 }
	
}
