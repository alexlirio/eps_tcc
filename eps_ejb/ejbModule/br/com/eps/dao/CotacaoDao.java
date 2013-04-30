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


@Stateless
@LocalBean
public class CotacaoDao extends GenericDao<Cotacao> {
	
	public CotacaoDao() {
		super(Cotacao.class);
	}
	
	public Cotacao findById(Long id) {
		return super.find(id);
	}

	public List<Cotacao> listAll() {

		List<Cotacao> cotacoes = null;

		try {
			TypedQuery<Cotacao> query = em.createNamedQuery(Cotacao.FIND_ALL_PERSIST, Cotacao.class);
			cotacoes = query.getResultList();
			return cotacoes;
		} catch (NoResultException e) {
			cotacoes = new ArrayList<Cotacao>();
			return cotacoes;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return cotacoes;
	}

	public List<Cotacao> listByCodigo(String codigo) {

		List<Cotacao> cotacoes = null;

		try {
			TypedQuery<Cotacao> query = em.createNamedQuery(Cotacao.FIND_BY_CODIGO, Cotacao.class);
			query.setParameter("codigo", codigo);
			cotacoes = query.getResultList();
			return cotacoes;
		} catch (NoResultException e) {
			log.error("Código da Cotacão não encontrado: " + codigo);
			cotacoes = new ArrayList<Cotacao>();
			return cotacoes;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return cotacoes;
	}

	public List<Cotacao> listByFornecedor(Fornecedor fornecedor) {

		List<Cotacao> cotacoes = null;

		try {
			TypedQuery<Cotacao> query = em.createNamedQuery(Cotacao.FIND_BY_FORNECEDOR, Cotacao.class);
			query.setParameter("fornecedor", fornecedor);
			cotacoes = query.getResultList();
			return cotacoes;
		} catch (NoResultException e) {
			cotacoes = new ArrayList<Cotacao>();
			return cotacoes;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return cotacoes;
	}

	public int countAll(Map<String, String> filtros) {
		String jpql = "SELECT COUNT(c) FROM Cotacao c ";

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = em.createQuery(jpql, Long.class);

		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Cotacao> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo, String ordernacao,
			Map<String, String> filtros) {

		String jpql = "SELECT c FROM Cotacao c ";

		jpql = adicionarParametros(filtros, jpql);

		if (ordernarPeloCampo != null && !ordernarPeloCampo.isEmpty()) {

			if (ordernacao.contains("DESC")) {
				ordernacao = "DESC";
			} else {
				ordernacao = "ASC";
			}

			jpql += " ORDER BY c." + ordernarPeloCampo + " " + ordernacao;
		}
		// metodo buscarPorPaginacao
		TypedQuery<Cotacao> query = em.createQuery(jpql, Cotacao.class);

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
			jpql += "c.dtDelecao IS NULL";
		} else {
			jpql += "WHERE c.dtDelecao IS NULL";
		}

		return jpql;

	}

	public List<Cotacao> listInicializaveis() {

		List<Cotacao> cotacoes = null;

		try {
			TypedQuery<Cotacao> query = em.createNamedQuery(Cotacao.FIND_INICIALIZAVEIS, Cotacao.class);
			cotacoes = query.getResultList();
			return cotacoes;
		} catch (NoResultException e) {
			cotacoes = new ArrayList<Cotacao>();
			return cotacoes;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return cotacoes;
	}

	public List<Cotacao> listFinalizaveis() {

		List<Cotacao> cotacoes = null;

		try {
			TypedQuery<Cotacao> query = em.createNamedQuery(Cotacao.FIND_FINALIZAVEIS, Cotacao.class);
			cotacoes = query.getResultList();
			return cotacoes;
		} catch (NoResultException e) {
			cotacoes = new ArrayList<Cotacao>();
			return cotacoes;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return cotacoes;
	}

}