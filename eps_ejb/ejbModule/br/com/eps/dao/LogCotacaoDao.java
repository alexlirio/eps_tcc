package br.com.eps.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import br.com.eps.model.LogCotacao;


@Stateless
@LocalBean
public class LogCotacaoDao extends GenericDao<LogCotacao> {
	
	public LogCotacaoDao() {
		super(LogCotacao.class);
	}
	
	public LogCotacao findById(Long id) {
		return super.find(id);
	}

	public List<LogCotacao> listAll() {
		return super.findAll();
	}

}