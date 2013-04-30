package br.com.eps.facade;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.jboss.logging.Logger;
import org.jboss.security.auth.spi.Util;

import br.com.eps.bean.UsuarioBean;
import br.com.eps.model.CompanhiaUser;
import br.com.eps.model.FornecedorUser;
import br.com.eps.model.StatusUsuario;
import br.com.eps.model.SuperUser;
import br.com.eps.model.Usuario;

@RequestScoped
@ManagedBean
public class LoginFacade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	Logger log = Logger.getLogger(LoginFacade.class);
	
	private String email;
	private String senha;
	private String novaSenha;
	
	@EJB
	private UsuarioBean usuarioBean;
	
	public String trocarSenhaInicial() {

		Usuario usuario = usuarioBean.findByEmailSenha(email, senha);
		
		// Verifica existe 'Usuario'.
		if (usuario.getId() == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "'Usu�rio' n�o encontrado ou 'Login/Senha' inv�lido!", "'Usu�rio n�o encontrado' ou 'Login/Senha' inv�lido!");
			FacesContext.getCurrentInstance().addMessage("login", msg);
			FacesContext.getCurrentInstance().addMessage("senha", msg);
			return null;
		}

		// Verifica 'Usuario' j� trocou a 'Senha Inicial'.
		if (!senha.equals(Util.createPasswordHash("SHA-256", "BASE64", null, null, usuario.getSenhaInicial()))) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "'Usu�rio' j� trocou a 'Senha Inicial'!", "'Usu�rio j� trocou a 'Senha Inicial'!");
			FacesContext.getCurrentInstance().addMessage("login", msg);
			return null;
		}
		
		// Verifica 'Usuario' est� com o 'Status' diferente de 'Inativo'.
		if (usuario.getStatusUsuario() != StatusUsuario.INATIVO) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "'Usu�rio' n�o est� 'Inativo'!", "'Usu�rio' n�o est� 'Inativo'!");
			FacesContext.getCurrentInstance().addMessage("login", msg);
			return null;
		}
		
		// Verifica se a 'Nova Senha' � igual a alguma das 3 �ltimas(Senhas Antigas).
		if (usuario.getSenhasAntigas().contains(novaSenha)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "A 'Nova Senha' n�o pode ser igual a uma 'Senha Antiga'", "A 'Nova Senha' n�o pode ser igual a uma 'Senha Antiga'");
			FacesContext.getCurrentInstance().addMessage("novaSenha1", msg);
			return null;
		}

		// Dele��o l�gica. Usada para manter hist�rico.
		// Obs.: O 'Atual Usuario(usuario)' � persistido com o mesmo 'ID' pois assim mant�m as associa��es int�ctas.
		//       O 'Novo Usuario(usuarioLog)' � persistido com um novo 'ID', para efeito de log(historico).
		Usuario usuarioLog;
		
		if (usuario.isSuperUser()) {
			usuarioLog = new SuperUser();
		} else if (usuario.isCompanhiaUser()) {
			usuarioLog = new CompanhiaUser();
		} else if (usuario.isFornecedorUser()) {
			usuarioLog = new FornecedorUser();
		} else {
			log.error("N�o foi poss�vel identificar o tipo de 'Usu�rio'.");
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "N�o foi poss�vel identificar o tipo de 'Usu�rio'.", "N�o foi poss�vel identificar o tipo de 'Usu�rio'.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		
		// Biblioteca Commons BeanUtils, implementa��o j� pronta para 'clonar' a entidade.
		try {
			/*
		     * Altera os valores defaults do BeanUtils. Agora, ao copiar de um
		     * bean para o outro, quando o valor da origem for nulo, o do destino
		     * passa a ser nulo tamb�m.
		     * No m�todo do BeanUtils, os valores default s�o:
		     * Integer = 0
		     * Character = ' '
		     * Long = 0 
		     */
	        ConvertUtils.register(new IntegerConverter(null), Integer.class);
	        ConvertUtils.register(new CharacterConverter(null), Character.class);
	        ConvertUtils.register(new LongConverter(null), Long.class);
	        // Sera 'null', caso seja 'null' no BD. 
			ConvertUtils.register(new DateConverter(null), Date.class);
			
			// Faz um 'clone' de 'usuario'.
			BeanUtils.copyProperties(usuarioLog, usuario);

		} catch (IllegalAccessException e) {
			log.error("Acesso Ilegal", e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("Erro Invocation", e);
			e.printStackTrace();
		}

		usuario.setDtInsercao(new Date());
		usuario.setUsuarioInsercao(usuario.getEmail());
		usuario.setStatusUsuario(StatusUsuario.ATIVO);
		usuario.setSenha(novaSenha);
		
		// Guarda a 'Senha Antiga' em 'Senhas Antigas'.
		if (usuario.getSenhasAntigas().length() < senha.length() * 3) {
			usuario.setSenhasAntigas(novaSenha + usuario.getSenhasAntigas());
		} else {
			usuario.setSenhasAntigas(novaSenha + usuario.getSenhasAntigas().substring(0, novaSenha.length() * 2));
		}
		
		usuarioLog.setId(null);
		usuarioLog.setDtDelecao(new Date());
		usuarioLog.setUsuarioDelecao(usuario.getEmail());

		usuarioBean.save(usuarioLog);
		log.info("'Usu�rio Log' salvo no BD: Com ID " + usuarioLog.getId());
		
		usuarioBean.update(usuario);
		log.info("'Usu�rio' atualizado no BD: Com ID " + usuario.getId());

		return "senhaInicialChangeSuccess.xhtml";
		
	}
	
	public String trocarSenha() {
	
		// 'Login(E-mail)' do usu�rio logado.
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();

		Usuario usuario = usuarioBean.findByEmail(usuarioLogin);
		
		// Verifica se a senha est� correta e bloqueia o 'Usu�rio' caso digite a senha incorreta por 3 vezes consecutivas.
		if (!senha.equals(usuario.getSenha())) {
			
			if (usuario.getQtdTentativas() <= 2) {
				usuario.setQtdTentativas(usuario.getQtdTentativas() + 1);
				usuarioBean.update(usuario);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "'Senha' inv�lida, o usu�rio ser� bloqueado ap�s 3 tentativas incorretas!", "'Senha' inv�lida, o usu�rio ser� bloqueado ap�s 3 tentativas incorretas!");
				FacesContext.getCurrentInstance().addMessage("senha", msg);
				return null;
			} else {
				usuario.setQtdTentativas(usuario.getQtdTentativas() + 1);
				usuario.setStatusUsuario(StatusUsuario.BLOQUEADO);
				usuarioBean.update(usuario);
				
				HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);  
				session.invalidate();  
				log.info("Usu�rio bloqueado: " + usuarioLogin);
				
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "'Usu�rio' bloqueado!", "'Usu�rio' bloqueado!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return "/pages/public/logout.xhtml";
			}

		}
		
		usuario.setQtdTentativas(0);
		
		// Verifica se a 'Nova Senha' � igual a alguma das 3 �ltimas(Senhas Antigas).
		if (usuario.getSenhasAntigas().contains(novaSenha)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "A 'Nova Senha' n�o pode ser igual a uma 'Senha Antiga'", "A 'Nova Senha' n�o pode ser igual a uma 'Senha Antiga'");
			FacesContext.getCurrentInstance().addMessage("novaSenha1", msg);
			return null;
		}
		
		// Dele��o l�gica. Usada para manter hist�rico.
		// Obs.: O 'Atual Usuario(usuario)' � persistido com o mesmo 'ID' pois assim mant�m as associa��es int�ctas.
		//       O 'Novo Usuario(usuarioLog)' � persistido com um novo 'ID', para efeito de log(historico).
		Usuario usuarioLog;
		
		if (usuario.isSuperUser()) {
			usuarioLog = new SuperUser();
		} else if (usuario.isCompanhiaUser()) {
			usuarioLog = new CompanhiaUser();
		} else if (usuario.isFornecedorUser()) {
			usuarioLog = new FornecedorUser();
		} else {
			log.error("N�o foi poss�vel identificar o tipo de 'Usu�rio'.");
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "N�o foi poss�vel identificar o tipo de 'Usu�rio'.", "N�o foi poss�vel identificar o tipo de 'Usu�rio'.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		
		// Biblioteca Commons BeanUtils, implementa��o j� pronta para 'clonar' a entidade.
		try {
			/*
			 * Altera os valores defaults do BeanUtils. Agora, ao copiar de um
			 * bean para o outro, quando o valor da origem for nulo, o do destino
			 * passa a ser nulo tamb�m.
			 * No m�todo do BeanUtils, os valores default s�o:
			 * Integer = 0
			 * Character = ' '
			 * Long = 0 
			 */
			ConvertUtils.register(new IntegerConverter(null), Integer.class);
			ConvertUtils.register(new CharacterConverter(null), Character.class);
			ConvertUtils.register(new LongConverter(null), Long.class);
			// Sera 'null', caso seja 'null' no BD. 
			ConvertUtils.register(new DateConverter(null), Date.class);
			
			// Faz um 'clone' de 'usuario'.
			BeanUtils.copyProperties(usuarioLog, usuario);
			
		} catch (IllegalAccessException e) {
			log.error("Acesso Ilegal", e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("Erro Invocation", e);
			e.printStackTrace();
		}
		
		usuario.setDtInsercao(new Date());
		usuario.setUsuarioInsercao(usuario.getEmail());
		usuario.setStatusUsuario(StatusUsuario.ATIVO);
		usuario.setSenha(novaSenha);
		
		// Guarda a 'Senha Antiga' em 'Senhas Antigas'.
		if (usuario.getSenhasAntigas().length() < senha.length() * 3) {
			usuario.setSenhasAntigas(novaSenha + usuario.getSenhasAntigas());
		} else {
			usuario.setSenhasAntigas(novaSenha + usuario.getSenhasAntigas().substring(0, novaSenha.length() * 2));
		}
		
		usuarioLog.setId(null);
		usuarioLog.setDtDelecao(new Date());
		usuarioLog.setUsuarioDelecao(usuario.getEmail());
		
		usuarioBean.save(usuarioLog);
		log.info("'Usu�rio Log' salvo no BD: Com ID " + usuarioLog.getId());
		
		usuarioBean.update(usuario);
		log.info("'Usu�rio' atualizado no BD: Com ID " + usuario.getId());
		
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Senha alterada com sucesso.", "Senha alterada com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "principal.xhtml";
		
	}

	public String logOut() {
		
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);  
		session.invalidate();  
		log.info("Logout: " + usuarioLogin);
		
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Usu�rio' finalizou a sess�o!", "'Usu�rio' finalizou a sess�o!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "/pages/public/logout.xhtml";
		
	}
	
	// Formata o login(e-mail) para pegar o que tiver antes do '@' e colocar a primeira letra em mai�scula.
	public String getUsuarioLogado() {
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		usuarioLogin = usuarioLogin.substring(0, usuarioLogin.indexOf("@"));
		
		return Character.toUpperCase(usuarioLogin.charAt(0)) + usuarioLogin.substring(1);
		
	}
	
	/**
	 * Metodo utilizado para redirecionar o usu�rio logado para a sua respectiva pasta dom�nio 'Role'.
	 * 
	 * @throws IOException
	 */
	public void redirectAfteLogin() throws IOException {
		
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		log.info("Login: " + usuarioLogin);
		
		Usuario usuario = usuarioBean.findByEmail(usuarioLogin);
		
		if (usuario.isSuperUser()) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("admin/principal.xhtml");
		} else if (usuario.isCompanhiaUser()) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("companhia/principal.xhtml");
		} else if (usuario.isFornecedorUser()) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("fornecedor/principal.xhtml");
		} else {
			log.error("N�o foi poss�vel identificar o tipo de 'Usu�rio'.");
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "N�o foi poss�vel identificar o tipo de 'Usu�rio'.", "N�o foi poss�vel identificar o tipo de 'Usu�rio'.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			new IOException("N�o foi poss�vel identificar o tipo de 'Usu�rio'.");
		}
		
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

}