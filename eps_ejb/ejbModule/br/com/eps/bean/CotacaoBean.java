package br.com.eps.bean;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.CotacaoDao;
import br.com.eps.model.Cotacao;
import br.com.eps.model.Fornecedor;

/**
 * Na versão 3.1, quando o acesso a um SLSB é local, não é mais necessário definir uma
 * interface Java nem utilizar a anotação @LOCAL. Então, bastaria implementar uma classe Java
 * com a anotação @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class CotacaoBean {

	@EJB 
	private CotacaoDao cotacaoDAO;
	
	public Cotacao findById(Long id) {
		return cotacaoDAO.find(id);
	}
	
	public List<Cotacao> listAll() {
		return cotacaoDAO.listAll();
	}
	
	public List<Cotacao> listByCodigo(String codigo) {
		return cotacaoDAO.listByCodigo(codigo);
	}

	public void save(Cotacao cotacao) {
		cotacaoDAO.save(cotacao);
	}

	public void update(Cotacao cotacao) {
		cotacaoDAO.update(cotacao);	
	}
	
	public int countAll(Map<String, String> filtros) {
		return cotacaoDAO.countAll(filtros);
	}

	public List<Cotacao> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo, String ordernacao,
			Map<String, String> filtros) {
		return cotacaoDAO.buscaPorPaginacao(posicaoPrimeiraLinha, maximoPorPagina, ordernarPeloCampo, ordernacao, filtros);
	}

	public List<Cotacao> listByFornecedor(Fornecedor fornecedor) {
		return cotacaoDAO.listByFornecedor(fornecedor);
	}

	public List<Cotacao> listInicializaveis() {
		return cotacaoDAO.listInicializaveis();
	}

	public List<Cotacao> listFinalizaveis() {
		return cotacaoDAO.listFinalizaveis();
	}
	
}