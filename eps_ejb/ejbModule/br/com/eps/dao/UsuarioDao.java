package br.com.eps.dao;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jboss.logging.Logger;

import br.com.eps.model.Usuario;


@Stateless
@LocalBean
public class UsuarioDao extends GenericDao<Usuario> {
	
	Logger log = Logger.getLogger(UsuarioDao.class);
	
	public UsuarioDao() {
		super(Usuario.class);
	}

	public Usuario findByEmail(String email) {
		
		Usuario usuario = null;
		
		try {
			TypedQuery<Usuario> query = em.createNamedQuery(Usuario.FIND_BY_EMAIL, Usuario.class);
			query.setParameter("email" , email);
			usuario = query.getSingleResult();
			return usuario;
		} catch (NoResultException e) {
			log.info("Erro de 'Cast', 'Usuário' não encontrado: " + email);
			usuario = new Usuario();
			return usuario;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}
		
		return usuario;

	}
	
	public Usuario findByEmailSenha(String email, String senha) {
		
		Usuario usuario = null;
		
		try {
			TypedQuery<Usuario> query = em.createNamedQuery(Usuario.FIND_BY_EMAIL_SENHA, Usuario.class);
			query.setParameter("email" , email);
			query.setParameter("senha" , senha);
			usuario = query.getSingleResult();
			return usuario;
		} catch (NoResultException e) {
			log.info("Erro de 'Cast', 'Usuário' não encontrado: " + email);
			usuario = new Usuario();
			return usuario;
		} catch (Exception e) {
			log.error("Error while running query: " + e.getMessage(), e);
			e.printStackTrace();
		}
		
		return usuario;
		
	}

	public void refresh(Usuario usuario) {
		em.refresh(usuario);
		
	}
	
}