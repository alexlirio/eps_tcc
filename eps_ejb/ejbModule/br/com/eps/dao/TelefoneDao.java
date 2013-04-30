package br.com.eps.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.eps.model.Companhia;
import br.com.eps.model.Fornecedor;
import br.com.eps.model.Telefone;

@Stateless
@LocalBean
public class TelefoneDao extends GenericDao<Telefone> {

	public TelefoneDao() {
		super(Telefone.class);
	}

	public List<Telefone> listAll() {
		return super.findAll();
	}

	public List<Telefone> listByCompanhia(Companhia companhia) {
		TypedQuery<Telefone> query = em.createNamedQuery(Telefone.FIND_BY_COMPANHIA, Telefone.class);
		query.setParameter("companhia" , companhia);

		return query.getResultList();
	}
	
	public List<Telefone> listByFornecedor(Fornecedor fornecedor) {
		TypedQuery<Telefone> query = em.createNamedQuery(Telefone.FIND_BY_FORNECEDOR, Telefone.class);
		query.setParameter("fornecedor" , fornecedor);
		
		return query.getResultList();
	}

}