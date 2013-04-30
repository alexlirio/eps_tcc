package br.com.eps.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import br.com.eps.bean.UnidadeMedidaBean;
import br.com.eps.model.UnidadeMedida;

@FacesConverter(value="unidadeMedidaConverter", forClass=UnidadeMedida.class)
public class UnidadeMedidaConverter implements Converter {
	
	Logger log = Logger.getLogger(UnidadeMedidaConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		UnidadeMedida unidadeMedida = null;
		
		if (value != null && !"".equalsIgnoreCase(value)) {
			try {
				Context initialContext = new InitialContext();
				UnidadeMedidaBean unidadeMedidaBean = (UnidadeMedidaBean) initialContext.lookup("java:global/eps_ear/eps_ejb/UnidadeMedidaBean");
				unidadeMedida = unidadeMedidaBean.findById(Long.parseLong(value));
			} catch (NumberFormatException e) {
				log.error("Erro de parse para Long.", e);
			} catch (NamingException e) {
				log.error("Erro de Naming.", e);
			}
		}

		return unidadeMedida;
	
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof UnidadeMedida) ? String.valueOf(((UnidadeMedida) value).getId()) : null;
	}
	
}
