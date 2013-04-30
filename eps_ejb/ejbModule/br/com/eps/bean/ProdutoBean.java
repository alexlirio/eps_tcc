package br.com.eps.bean;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.ProdutoDao;
import br.com.eps.model.Cotacao;
import br.com.eps.model.Fornecedor;
import br.com.eps.model.Produto;

/**
 * Na versão 3.1, quando o acesso a um SLSB é local, não é mais necessário definir uma
 * interface Java nem utilizar a anotação @LOCAL. Então, bastaria implementar uma classe Java
 * com a anotação @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class ProdutoBean {

	@EJB 
	private ProdutoDao produtoDAO;
	
	public Produto findById(Long id) {
		return produtoDAO.find(id);
	}
	
	public Produto findByCodigo(String codigo) {
		return produtoDAO.findByCodigo(codigo);
	}
	
	public List<Produto> listAll() {
		return produtoDAO.listAll();
	}

	public void save(Produto produto) {
		produtoDAO.save(produto);
	}

	public void update(Produto produto) {
		produtoDAO.update(produto);	
	}
	
	public int countAll(Map<String, String> filtros) {
		return produtoDAO.countAll(filtros);
	}

	public List<Produto> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo, String ordernacao,
			Map<String, String> filtros) {
		return produtoDAO.buscaPorPaginacao(posicaoPrimeiraLinha, maximoPorPagina, ordernarPeloCampo, ordernacao, filtros);
	}

	public List<Produto> listByFornecedor(Fornecedor fornecedor) {
		return produtoDAO.listByFornecedor(fornecedor);
	}
	
	public List<Produto> listByCotacao(Cotacao cotacao) {
		return produtoDAO.listByCotacao(cotacao);
	}
	
}