package br.com.eps.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.TelefoneDao;
import br.com.eps.model.Companhia;
import br.com.eps.model.Fornecedor;
import br.com.eps.model.Telefone;

/**
 * Na versão 3.1, quando o acesso a um SLSB é local, não é mais necessário definir uma
 * interface Java nem utilizar a anotação @LOCAL. Então, bastaria implementar uma classe Java
 * com a anotação @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class TelefoneBean {

	@EJB 
	private TelefoneDao telefoneDAO;

	public List<Telefone> listAll() {
		return telefoneDAO.listAll();
	}

	public List<Telefone> listByCompanhia(Companhia companhia) {
		return telefoneDAO.listByCompanhia(companhia);
	}
	
	public List<Telefone> listByFornecedor(Fornecedor fornecedor) {
		return telefoneDAO.listByFornecedor(fornecedor);
	}
}