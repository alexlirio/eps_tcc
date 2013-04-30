package br.com.eps.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.MunicipioDao;
import br.com.eps.model.Municipio;
import br.com.eps.model.Uf;

/**
 * Na versão 3.1, quando o acesso a um SLSB é local, não é mais necessário definir uma
 * interface Java nem utilizar a anotação @LOCAL. Então, bastaria implementar uma classe Java
 * com a anotação @STATELESS.
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