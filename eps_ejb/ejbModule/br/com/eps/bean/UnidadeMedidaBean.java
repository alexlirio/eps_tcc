package br.com.eps.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.UnidadeMedidaDao;
import br.com.eps.model.UnidadeMedida;

/**
 * Na versão 3.1, quando o acesso a um SLSB é local, não é mais necessário definir uma
 * interface Java nem utilizar a anotação @LOCAL. Então, bastaria implementar uma classe Java
 * com a anotação @STATELESS.
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