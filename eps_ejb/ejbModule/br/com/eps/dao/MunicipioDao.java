package br.com.eps.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.eps.model.Municipio;
import br.com.eps.model.Uf;


@Stateless
@LocalBean
public class MunicipioDao extends GenericDao<Municipio> {
	
	public MunicipioDao() {
		super(Municipio.class);
	}
	
	public List<Municipio> listAll(){
		return super.findAll();
	}

	public List<Municipio> listByUf(Uf uf){
		TypedQuery<Municipio> query = em.createNamedQuery(Municipio.FIND_BY_UF, Municipio.class);
		query.setParameter("uf" , uf);

		return query.getResultList();
	}
	
}