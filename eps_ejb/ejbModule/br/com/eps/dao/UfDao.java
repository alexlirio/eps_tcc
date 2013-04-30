package br.com.eps.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.eps.model.Pais;
import br.com.eps.model.Uf;


@Stateless
@LocalBean
public class UfDao extends GenericDao<Uf> {
	
	public UfDao() {
		super(Uf.class);
	}
	
	public List<Uf> listAll(){
		return super.findAll();
	}

	public List<Uf> listByPais(Pais pais){
		TypedQuery<Uf> query = em.createNamedQuery(Uf.FIND_BY_PAIS, Uf.class);
		query.setParameter("pais" , pais);

		return query.getResultList();
	}

	public Long countByPais(Pais pais) {

		if (pais.getId() == null) {
			return 0L;
		}

		Query query = em.createNamedQuery(Uf.COUNT_BY_PAIS);
		query.setParameter("pais" , pais);

		return (Long)query.getSingleResult();
	}
	
}