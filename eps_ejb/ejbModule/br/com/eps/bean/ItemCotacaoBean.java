package br.com.eps.bean;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.ItemCotacaoDao;
import br.com.eps.model.Cotacao;
import br.com.eps.model.ItemCotacao;

/**
 * Na vers�o 3.1, quando o acesso a um SLSB � local, n�o � mais necess�rio definir uma
 * interface Java nem utilizar a anota��o @LOCAL. Ent�o, bastaria implementar uma classe Java
 * com a anota��o @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class ItemCotacaoBean {

	@EJB 
	private ItemCotacaoDao itemCotacaoDAO;
	
	public ItemCotacao findById(Long id) {
		return itemCotacaoDAO.find(id);
	}
	
	public List<ItemCotacao> listAll() {
		return itemCotacaoDAO.listAll();
	}

	public List<ItemCotacao> listByCotacao(Cotacao cotacao) {
		return itemCotacaoDAO.listByCotacao(cotacao);
	}
	
}