package br.com.eps.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.eps.model.Pais;

@Stateless
@LocalBean
public class PaisDao extends GenericDao<Pais> {

	public PaisDao() {
		super(Pais.class);
	}

	public List<Pais> listAll() {
		return super.findAll();
	}

	public Pais findByCodigo(String codigo) {

		Pais pais = null;

		try {
			TypedQuery<Pais> query = em.createNamedQuery(Pais.FIND_BY_CODIGO, Pais.class);
			query.setParameter("codigo", codigo);
			pais = query.getSingleResult();
			return pais;
		} catch (NoResultException e) {
			log.error("Código do Pais não encontrado: " + codigo);
			pais = new Pais();
			return pais;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}

		return pais;
	}
}