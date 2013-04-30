package br.com.eps.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.TelefoneDao;
import br.com.eps.model.Companhia;
import br.com.eps.model.Fornecedor;
import br.com.eps.model.Telefone;

/**
 * Na vers�o 3.1, quando o acesso a um SLSB � local, n�o � mais necess�rio definir uma
 * interface Java nem utilizar a anota��o @LOCAL. Ent�o, bastaria implementar uma classe Java
 * com a anota��o @STATELESS.
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