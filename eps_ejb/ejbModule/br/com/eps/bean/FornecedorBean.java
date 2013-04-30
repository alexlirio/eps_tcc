package br.com.eps.bean;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.FornecedorDao;
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
public class FornecedorBean {

	@EJB 
	private FornecedorDao fornecedorDAO;
	
	public Fornecedor findById(Long id) {
		return fornecedorDAO.findById(id);
	}
	
	public List<Fornecedor> listAll() {
		return fornecedorDAO.listAll();
	}
	
	public void save(Fornecedor fornecedor) {
		fornecedorDAO.save(fornecedor);
	}

	public void update(Fornecedor fornecedor) {
		fornecedorDAO.update(fornecedor);	
	}
	
	public Fornecedor findByEmail(String email) {
		return fornecedorDAO.findByEmail(email);
	}

	public Fornecedor findByCnpjCpf(String cnpjCpf) {
		return fornecedorDAO.findByCnpjCpf(cnpjCpf);
	}

	public int countAll(Map<String, String> filtros) {
		return fornecedorDAO.countAll(filtros);
	}

	public List<Fornecedor> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo, String ordernacao,
			Map<String, String> filtros) {
		return fornecedorDAO.buscaPorPaginacao(posicaoPrimeiraLinha, maximoPorPagina, ordernarPeloCampo, ordernacao, filtros);
	}

	public List<Fornecedor> listByProduto(Produto produto) {
		return fornecedorDAO.listByProduto(produto);
	}
	
}