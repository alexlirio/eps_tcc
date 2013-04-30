package br.com.eps.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import br.com.eps.bean.PaisBean;
import br.com.eps.model.Pais;

@FacesConverter(value="paisConverter", forClass=Pais.class)
public class PaisConverter implements Converter {
	
	Logger log = Logger.getLogger(PaisConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		Pais pais = null;
		
		if (value != null && !"".equalsIgnoreCase(value)) {
			try {
				Context initialContext = new InitialContext();
				PaisBean paisBean = (PaisBean) initialContext.lookup("java:global/eps_ear/eps_ejb/PaisBean");
				pais = paisBean.findById(Long.parseLong(value));
			} catch (NumberFormatException e) {
				log.error("Erro de parse para Long.", e);
			} catch (NamingException e) {
				log.error("Erro de Naming.", e);
			}
		}

		return pais;
	
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof Pais) ? String.valueOf(((Pais) value).getId()) : null;
	}
	
}
