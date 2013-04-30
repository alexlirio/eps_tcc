package br.com.eps.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.jboss.logging.Logger;
import org.jboss.security.auth.spi.Util;

@FacesConverter(value="passwordConverter", forClass=String.class)
public class PasswordConverter implements Converter {
	
	static Logger log = Logger.getLogger(PasswordConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
        if (value == null || "".equals(value.trim())) {
        	return null;
        }
        
        return converte(value);
	
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
			return "";
	}

	public static String converte(String password) {
		return Util.createPasswordHash("SHA-256", "BASE64", null, null, password);
	}
	

	 public static void main(String[] args) {
	 System.out.println(converte("123456"));
	 System.out.println(converte("1234567890123"));
	 System.out.println(converte("000000000000000"));
	 System.out.println(converte("asdsdaasdasdad"));
	 }
	
}
