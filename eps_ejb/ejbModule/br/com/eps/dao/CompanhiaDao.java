package br.com.eps.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.eps.model.Companhia;

@Stateless
@LocalBean
public class CompanhiaDao extends GenericDao<Companhia> {

	public CompanhiaDao() {
		super(Companhia.class);
	}

	public Companhia findById(Long id) {
		return super.find(id);
	}
	
	public Companhia findByEmail(String email) {

		Companhia companhia = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("email", email);

			companhia = super.findOneResult(Companhia.FIND_BY_EMAIL, parameters);
			return companhia;
		} catch (ClassCastException e) {
			log.info("Erro de 'Cast', E-mail da 'Companhia' não encontrado: " + email);
			companhia = new Companhia();
			return companhia;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return companhia;
	}

	public Companhia findByCnpjCpf(String cnpjCpf) {

		Companhia companhia = null;

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cnpjCpf", cnpjCpf);

			companhia = super.findOneResult(Companhia.FIND_BY_CNPJ_CPF, parameters);
			return companhia;
		} catch (ClassCastException e) {
			log.info("Erro de 'Cast', CNPJ/CPF da 'Companhia' não encontrado: " + cnpjCpf);
			companhia = new Companhia();
			return companhia;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return companhia;

	}

	public int countAll(Map<String, String> filtros) {
		String jpql = "SELECT COUNT(c) FROM Companhia c ";

		jpql = adicionarParametros(filtros, jpql);

		TypedQuery<Long> query = em.createQuery(jpql, Long.class);

		popularParametros(query, filtros);

		return query.getSingleResult().intValue();
	}

	public List<Companhia> buscaPorPaginacao(int posicaoPrimeiraLinha,
			int maximoPorPagina, String ordernarPeloCampo, String ordernacao,
			Map<String, String> filtros) {

		String jpql = "SELECT c FROM Companhia c ";

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
		TypedQuery<Companhia> query = em.createQuery(jpql, Companhia.class);

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

}