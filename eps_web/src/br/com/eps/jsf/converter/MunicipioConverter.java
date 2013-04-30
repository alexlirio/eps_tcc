package br.com.eps.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import br.com.eps.bean.MunicipioBean;
import br.com.eps.model.Municipio;

@FacesConverter(value="municipioConverter", forClass=Municipio.class)
public class MunicipioConverter implements Converter {
	
	Logger log = Logger.getLogger(MunicipioConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		Municipio municipio = null;
		
		if (value != null && !"".equalsIgnoreCase(value)) {
			try {
				Context initialContext = new InitialContext();
				MunicipioBean municipioBean = (MunicipioBean) initialContext.lookup("java:global/eps_ear/eps_ejb/MunicipioBean");
				municipio = municipioBean.findById(Long.parseLong(value));
			} catch (NumberFormatException e) {
				log.error("Erro de parse para Long.", e);
			} catch (NamingException e) {
				log.error("Erro de Naming.", e);
			}
		}

		return municipio;
	
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof Municipio) ? String.valueOf(((Municipio) value).getId()) : null;
	}
	
}
