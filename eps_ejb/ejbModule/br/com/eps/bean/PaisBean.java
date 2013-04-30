package br.com.eps.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.PaisDao;
import br.com.eps.model.Pais;

/**
 * Na vers�o 3.1, quando o acesso a um SLSB � local, n�o � mais necess�rio definir uma
 * interface Java nem utilizar a anota��o @LOCAL. Ent�o, bastaria implementar uma classe Java
 * com a anota��o @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class PaisBean {

	@EJB 
	private PaisDao paisDAO;
	
	public Pais findById(Long id) {
		return paisDAO.find(id);
	}
	
	public Pais findByCodigo(String codigo) {
		return paisDAO.findByCodigo(codigo);
	}
	
	public List<Pais> listAll() {
		return paisDAO.listAll();
	}

}