package br.com.eps.bean;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.CompanhiaDao;
import br.com.eps.model.Companhia;

/**
 * Na vers�o 3.1, quando o acesso a um SLSB � local, n�o � mais necess�rio definir uma
 * interface Java nem utilizar a anota��o @LOCAL. Ent�o, bastaria implementar uma classe Java
 * com a anota��o @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class CompanhiaBean {

	@EJB 
	private CompanhiaDao companhiaDAO;
	
	public Companhia findById(Long id) {
		return companhiaDAO.findById(id);
	}
	
	public void save(Companhia companhia) {
		companhiaDAO.save(companhia);
	}

	public void update(Companhia companhia) {
		companhiaDAO.update(companhia);	
	}

	public Companhia findByEmail(String email) {
		return companhiaDAO.findByEmail(email);
	}

	public Companhia findByCnpjCpf(String cnpjCpf) {
		return companhiaDAO.findByCnpjCpf(cnpjCpf);
	}

	public int countAll(Map<String, String> filtros) {
		return companhiaDAO.countAll(filtros);
	}

	public List<Companhia> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo, String ordernacao,
			Map<String, String> filtros) {
		return companhiaDAO.buscaPorPaginacao(posicaoPrimeiraLinha, maximoPorPagina, ordernarPeloCampo, ordernacao, filtros);
	}
	
}