package br.com.eps.bean;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import br.com.eps.dao.UsuarioDao;
import br.com.eps.model.Usuario;

/**
 * Na vers�o 3.1, quando o acesso a um SLSB � local, n�o � mais necess�rio definir uma
 * interface Java nem utilizar a anota��o @LOCAL. Ent�o, bastaria implementar uma classe Java
 * com a anota��o @STATELESS.
 * @author Alex Lirio
 *
 */
@Stateless
public class UsuarioBean {

	@EJB 
	private UsuarioDao usuarioDAO;

	public void save(Usuario usuario) {
		usuarioDAO.save(usuario);
	}

	public void update(Usuario usuario) {
		usuarioDAO.update(usuario);	
	}
	
	public Usuario findByEmail(String email) {
		return usuarioDAO.findByEmail(email);
	}
	
	public Usuario findByEmailSenha(String email, String senha) {
		return usuarioDAO.findByEmailSenha(email, senha);
	}

	public void refresh(Usuario usuario) {
		usuarioDAO.refresh(usuario);
		
	}

}