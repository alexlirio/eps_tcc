package br.com.eps.jsf.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.jboss.logging.Logger;

@FacesConverter(value="cnpjCpfConverter", forClass=String.class)
public class CnpjCpfConverter implements Converter {
	
	static Logger log = Logger.getLogger(CnpjCpfConverter.class);

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

	public static String converte(String cnpjCpf) {

		try {
			if (cnpjCpf.replaceAll("\\D", "").length() == 14) {
				return CnpjConverter.converte(cnpjCpf);
			} else if (cnpjCpf.replaceAll("\\D", "").length() == 11) {
				return CpfConverter.converte(cnpjCpf);
			} else {
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "CNPJ/CPF impossível de se converter", "CNPJ/CPF impossível de se converter");
				throw new ConverterException(msg);
			}	
		} catch (ConverterException e) {
			log.error("CNPJ/CPF impossível de se converter: " + cnpjCpf);
			throw e;
		}

	}
	

	 public static void main(String[] args) {
	 System.out.println(converte("12312312312"));
	 System.out.println(converte("73222726000142"));
	 System.out.println(converte("000000000000000"));
	 System.out.println(converte("asdsdaasdasdad"));
	 }
	
}
