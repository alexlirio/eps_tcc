package br.com.eps.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import br.com.eps.bean.UfBean;
import br.com.eps.model.Uf;

@FacesConverter(value="ufConverter", forClass=Uf.class)
public class UfConverter implements Converter {
	
	Logger log = Logger.getLogger(UfConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		Uf uf = null;
		
		if (value != null && !"".equalsIgnoreCase(value)) {
			try {
				Context initialContext = new InitialContext();
				UfBean ufBean = (UfBean) initialContext.lookup("java:global/eps_ear/eps_ejb/UfBean");
				uf = ufBean.findById(Long.parseLong(value));
			} catch (NumberFormatException e) {
				log.error("Erro de parse para Long.", e);
			} catch (NamingException e) {
				log.error("Erro de Naming.", e);
			}
		}

		return uf;
	
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof Uf) ? String.valueOf(((Uf) value).getId()) : null;
	}
	
}
