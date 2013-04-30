package br.com.eps.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.eps.model.Fornecedor;
import br.com.eps.model.Produto;


@Stateless
@LocalBean
public class FornecedorDao extends GenericDao<Fornecedor> {
	
	public FornecedorDao() {
		super(Fornecedor.class);
	}
	
	public Fornecedor findById(Long id) {
		return super.find(id);
	}

	public List<Fornecedor> listAll() {

		List<Fornecedor> fornecedores = null;

		try {
			TypedQuery<Fornecedor> query = em.createNamedQuery(Fornecedor.FIND_ALL_PERSIST, Fornecedor.class);
			fornecedores = query.getResultList();
			return fornecedores;
		} catch (NoResultException e) {
			fornecedores = new ArrayList<Fornecedor>();
			return fornecedores;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return fornecedores;
	}
	
	public Fornecedor findByEmail(String email){

		Fornecedor fornecedor = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("email", email);		
			
			fornecedor = super.findOneResult(Fornecedor.FIND_BY_EMAIL, parameters);
			return fornecedor;
		} catch (ClassCastException e) {
			log.info("Erro de 'Cast', E-mail do 'Fornecedor' não encontrado: " + email);
			fornecedor = new Fornecedor();
			return fornecedor;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}
		
		return fornecedor;
		
	}

	public Fornecedor findByCnpjCpf(String cnpjCpf) {
		
		Fornecedor fornecedor = null;
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cnpjCpf", cnpjCpf);		
			
			fornecedor = super.findOneResult(Fornecedor.FIND_BY_CNPJ_CPF, parameters);
			return fornecedor;
		} catch (ClassCastException e) {
			log.info("Erro de 'Cast', CNPJ/CPF do 'Fornecedor' não encontrado: " + cnpjCpf);
			fornecedor = new Fornecedor();
			return fornecedor;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}
		
		return fornecedor;
		
	}

	public int countAll(Map<String, String> filtros) {
		String jpql = "SELECT COUNT(f) FROM Fornecedor f ";

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = em.createQuery(jpql, Long.class);

		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Fornecedor> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo, String ordernacao,
			Map<String, String> filtros) {

		String jpql = "SELECT f FROM Fornecedor f ";

		jpql = adicionarParametros(filtros, jpql);

		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernacao.contains("DESC")) {
				ordernacao = "DESC";
			} else {
				ordernacao = "ASC";
			}

			jpql += " ORDER BY f." + ordernarPeloCampo + " " + ordernacao;
		}
		// metodo buscarPorPaginacao
		TypedQuery<Fornecedor> query = em.createQuery(jpql, Fornecedor.class);

		popularParametros(query, filtros);

		query.setFirstResult(posicaoPrimeiraLinha);
		query.setMaxResults(maximoPorPagina);
		return query.getResultList();
	}
	
	// Evitando SQL Injection
	private void popularParametros(TypedQuery<?> query, Map<String, String> filtros) {
		for (Map.Entry<String, String> entry : filtros.entrySet()) {
			query.setParameter(entry.getKey(), "%" + entry.getValue() + "%");
		}
	}

	private String adicionarParametros(Map<String, String> filtros, String jpql) {
		if (filtros != null && !filtros.isEmpty()) {
			jpql += " WHERE ";
			for (Map.Entry<String, String> entry : filtros.entrySet()) {
				jpql += entry.getKey() + " LIKE :" + entry.getKey() + " AND ";
			}
			jpql += "f.dtDelecao IS NULL";
		} else {
			jpql += "WHERE f.dtDelecao IS NULL";
		}

		return jpql;

	}

	public List<Fornecedor> listByProduto(Produto produto) {
		TypedQuery<Fornecedor> query = em.createNamedQuery(Fornecedor.FIND_BY_PRODUTO, Fornecedor.class);
		query.setParameter("produto" , produto);

		return query.getResultList();
	}

}