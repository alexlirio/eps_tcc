package br.com.eps.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.eps.model.Cotacao;
import br.com.eps.model.Fornecedor;
import br.com.eps.model.Produto;

@Stateless
@LocalBean
public class ProdutoDao extends GenericDao<Produto> {

	public ProdutoDao() {
		super(Produto.class);
	}

	public List<Produto> listAll() {

		List<Produto> produtos = null;

		try {
			TypedQuery<Produto> query = em.createNamedQuery(Produto.FIND_ALL_PERSIST, Produto.class);
			produtos = query.getResultList();
			return produtos;
		} catch (NoResultException e) {
			produtos = new ArrayList<Produto>();
			return produtos;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return produtos;
	}

	public Produto findByCodigo(String codigo) {

		Produto produto = null;

		try {
			TypedQuery<Produto> query = em.createNamedQuery(Produto.FIND_BY_CODIGO, Produto.class);
			query.setParameter("codigo", codigo);
			produto = query.getSingleResult();
			return produto;
		} catch (NoResultException e) {
			log.error("Código do Produto não encontrado: " + codigo);
			produto = new Produto();
			return produto;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return produto;
	}

	public int countAll(Map<String, String> filtros) {
		String jpql = "SELECT COUNT(p) FROM Produto p ";

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = em.createQuery(jpql, Long.class);

		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Produto> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo, String ordernacao,
			Map<String, String> filtros) {

		String jpql = "SELECT p FROM Produto p ";

		jpql = adicionarParametros(filtros, jpql);

		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernacao.contains("DESC")) {
				ordernacao = "DESC";
			} else {
				ordernacao = "ASC";
			}

			jpql += " ORDER BY p." + ordernarPeloCampo + " " + ordernacao;
		}
		// metodo buscarPorPaginacao
		TypedQuery<Produto> query = em.createQuery(jpql, Produto.class);

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
			jpql += "p.dtDelecao IS NULL";
		} else {
			jpql += "WHERE p.dtDelecao IS NULL";
		}

		return jpql;

	}

	public List<Produto> listByFornecedor(Fornecedor fornecedor) {

		List<Produto> produtos = null;

		try {
			TypedQuery<Produto> query = em.createNamedQuery(Produto.FIND_BY_FORNECEDOR, Produto.class);
			query.setParameter("fornecedor", fornecedor);
			produtos = query.getResultList();
			return produtos;
		} catch (NoResultException e) {
			produtos = new ArrayList<Produto>();
			return produtos;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return produtos;
	}
	
	public List<Produto> listByCotacao(Cotacao cotacao) {
		
		List<Produto> produtos = null;
		
		try {
			TypedQuery<Produto> query = em.createNamedQuery(Produto.FIND_BY_COTACAO, Produto.class);
			query.setParameter("cotacao", cotacao);
			produtos = query.getResultList();
			return produtos;
		} catch (NoResultException e) {
			produtos = new ArrayList<Produto>();
			return produtos;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}
		
		return produtos;
	}

}