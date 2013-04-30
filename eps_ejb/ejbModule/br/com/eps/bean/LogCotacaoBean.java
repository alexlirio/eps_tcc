package br.com.eps.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.LogCotacaoDao;
import br.com.eps.model.LogCotacao;

/**
 * Na versão 3.1, quando o acesso a um SLSB é local, não é mais necessário definir uma
 * interface Java nem utilizar a anotação @LOCAL. Então, bastaria implementar uma classe Java
 * com a anotação @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class LogCotacaoBean {

	@EJB 
	private LogCotacaoDao logCotacaoDAO;
	
	public LogCotacao findById(Long id) {
		return logCotacaoDAO.find(id);
	}
	
	public List<LogCotacao> listAll() {
		return logCotacaoDAO.listAll();
	}

	public void save(LogCotacao logCotacao) {
		logCotacaoDAO.save(logCotacao);
	}

	public void update(LogCotacao logCotacao) {
		logCotacaoDAO.update(logCotacao);	
	}

}