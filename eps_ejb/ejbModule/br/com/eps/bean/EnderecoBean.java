package br.com.eps.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.EnderecoDao;
import br.com.eps.model.Companhia;
import br.com.eps.model.Endereco;
import br.com.eps.model.Fornecedor;

/**
 * Na vers�o 3.1, quando o acesso a um SLSB � local, n�o � mais necess�rio definir uma
 * interface Java nem utilizar a anota��o @LOCAL. Ent�o, bastaria implementar uma classe Java
 * com a anota��o @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class EnderecoBean {

	@EJB 
	private EnderecoDao enderecoDAO;

	public List<Endereco> listAll() {
		return enderecoDAO.listAll();
	}

	public List<Endereco> listByCompanhia(Companhia companhia) {
		return enderecoDAO.listByCompanhia(companhia);
	}
	
	public List<Endereco> listByFornecedor(Fornecedor fornecedor) {
		return enderecoDAO.listByFornecedor(fornecedor);
	}
}