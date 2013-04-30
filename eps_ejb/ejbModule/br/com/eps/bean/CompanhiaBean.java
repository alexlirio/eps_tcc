package br.com.eps.bean;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.CompanhiaDao;
import br.com.eps.model.Companhia;

/**
 * Na versão 3.1, quando o acesso a um SLSB é local, não é mais necessário definir uma
 * interface Java nem utilizar a anotação @LOCAL. Então, bastaria implementar uma classe Java
 * com a anotação @STATELESS.
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