package br.com.eps.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import br.com.eps.bean.ProdutoBean;
import br.com.eps.model.Produto;

@FacesConverter(value="produtoConverter", forClass=Produto.class)
public class ProdutoConverter implements Converter {
	
	Logger log = Logger.getLogger(ProdutoConverter.class);

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		Produto produto = null;
		
		if (value != null && !"".equalsIgnoreCase(value)) {
			try {
				Context initialContext = new InitialContext();
				ProdutoBean produtoBean = (ProdutoBean) initialContext.lookup("java:global/eps_ear/eps_ejb/ProdutoBean");
				produto = produtoBean.findById(Long.parseLong(value));
			} catch (NumberFormatException e) {
				log.error("Erro de parse para Long.", e);
			} catch (NamingException e) {
				log.error("Erro de Naming.", e);
			}
		}

		return produto;
	
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return (value instanceof Produto) ? String.valueOf(((Produto) value).getId()) : null;
	}
	
}
