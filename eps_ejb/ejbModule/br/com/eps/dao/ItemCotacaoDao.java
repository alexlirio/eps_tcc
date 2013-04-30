package br.com.eps.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.eps.model.Cotacao;
import br.com.eps.model.ItemCotacao;


@Stateless
@LocalBean
public class ItemCotacaoDao extends GenericDao<ItemCotacao> {
	
	public ItemCotacaoDao() {
		super(ItemCotacao.class);
	}
	
	public ItemCotacao findById(Long id) {
		return super.find(id);
	}

	public List<ItemCotacao> listAll() {
		return super.findAll();
	}

	public List<ItemCotacao> listByCotacao(Cotacao cotacao) {

		List<ItemCotacao> itensCotacao = null;

		try {
			TypedQuery<ItemCotacao> query = em.createNamedQuery(ItemCotacao.FIND_BY_COTACAO, ItemCotacao.class);
			query.setParameter("cotacao", cotacao);
			itensCotacao = query.getResultList();
			return itensCotacao;
		} catch (NoResultException e) {
			itensCotacao = new ArrayList<ItemCotacao>();
			return itensCotacao;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return itensCotacao;
	}

}