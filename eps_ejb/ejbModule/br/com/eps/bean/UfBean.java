package br.com.eps.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.UfDao;
import br.com.eps.model.Pais;
import br.com.eps.model.Uf;

/**
 * Na vers�o 3.1, quando o acesso a um SLSB � local, n�o � mais necess�rio definir uma
 * interface Java nem utilizar a anota��o @LOCAL. Ent�o, bastaria implementar uma classe Java
 * com a anota��o @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class UfBean {

	@EJB 
	private UfDao ufDAO;

	public List<Uf> listAll() {
		return ufDAO.listAll();
	}
	
	public Uf findById(Long id) {
		return ufDAO.find(id);
	}
	
	public List<Uf> listByPais(Pais pais) {
		return ufDAO.listByPais(pais);
	}

	public Long countByPais(Pais pais) {
		return ufDAO.countByPais(pais);
	}
}