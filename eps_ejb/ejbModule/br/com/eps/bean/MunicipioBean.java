package br.com.eps.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.MunicipioDao;
import br.com.eps.model.Municipio;
import br.com.eps.model.Uf;

/**
 * Na vers�o 3.1, quando o acesso a um SLSB � local, n�o � mais necess�rio definir uma
 * interface Java nem utilizar a anota��o @LOCAL. Ent�o, bastaria implementar uma classe Java
 * com a anota��o @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class MunicipioBean {

	@EJB 
	private MunicipioDao municipioDAO;

	public List<Municipio> listAll() {
		return municipioDAO.listAll();
	}
	
	public Municipio findById(Long id) {
		return municipioDAO.find(id);
	}
	
	public List<Municipio> listByUf(Uf uf) {
		return municipioDAO.listByUf(uf);
	}
	
}