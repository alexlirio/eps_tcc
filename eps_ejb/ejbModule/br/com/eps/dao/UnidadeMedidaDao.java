package br.com.eps.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import br.com.eps.model.UnidadeMedida;

@Stateless
@LocalBean
public class UnidadeMedidaDao extends GenericDao<UnidadeMedida> {

	public UnidadeMedidaDao() {
		super(UnidadeMedida.class);
	}

	public List<UnidadeMedida> listAll() {
		return super.findAll();
	}

}