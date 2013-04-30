package br.com.eps.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import br.com.eps.model.Companhia;
import br.com.eps.model.Endereco;
import br.com.eps.model.Fornecedor;

@Stateless
@LocalBean
public class EnderecoDao extends GenericDao<Endereco> {

	public EnderecoDao() {
		super(Endereco.class);
	}

	public List<Endereco> listAll() {
		return super.findAll();
	}

	public List<Endereco> listByCompanhia(Companhia companhia) {
		TypedQuery<Endereco> query = em.createNamedQuery(Endereco.FIND_BY_COMPANHIA, Endereco.class);
		query.setParameter("companhia" , companhia);

		return query.getResultList();
	}
	
	public List<Endereco> listByFornecedor(Fornecedor fornecedor) {
		TypedQuery<Endereco> query = em.createNamedQuery(Endereco.FIND_BY_FORNECEDOR, Endereco.class);
		query.setParameter("fornecedor" , fornecedor);
		
		return query.getResultList();
	}

}