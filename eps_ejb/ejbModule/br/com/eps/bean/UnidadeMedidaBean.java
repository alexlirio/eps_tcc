package br.com.eps.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.UnidadeMedidaDao;
import br.com.eps.model.UnidadeMedida;

/**
 * Na vers�o 3.1, quando o acesso a um SLSB � local, n�o � mais necess�rio definir uma
 * interface Java nem utilizar a anota��o @LOCAL. Ent�o, bastaria implementar uma classe Java
 * com a anota��o @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class UnidadeMedidaBean {

	@EJB 
	private UnidadeMedidaDao unidadeMedidaDAO;
	
	public UnidadeMedida findById(Long id) {
		return unidadeMedidaDAO.find(id);
	}

	public List<UnidadeMedida> listAll() {
		return unidadeMedidaDAO.listAll();
	}

}