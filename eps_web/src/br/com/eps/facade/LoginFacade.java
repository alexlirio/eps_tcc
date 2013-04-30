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
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "'Usuário' não encontrado ou 'Login/Senha' inválido!", "'Usuário não encontrado' ou 'Login/Senha' inválido!");
			FacesContext.getCurrentInstance().addMessage("login", msg);
			FacesContext.getCurrentInstance().addMessage("senha", msg);
			return null;
		}

		// Verifica 'Usuario' já trocou a 'Senha Inicial'.
		if (!senha.equals(Util.createPasswordHash("SHA-256", "BASE64", null, null, usuario.getSenhaInicial()))) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "'Usuário' já trocou a 'Senha Inicial'!", "'Usuário já trocou a 'Senha Inicial'!");
			FacesContext.getCurrentInstance().addMessage("login", msg);
			return null;
		}
		
		// Verifica 'Usuario' está com o 'Status' diferente de 'Inativo'.
		if (usuario.getStatusUsuario() != StatusUsuario.INATIVO) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "'Usuário' não está 'Inativo'!", "'Usuário' não está 'Inativo'!");
			FacesContext.getCurrentInstance().addMessage("login", msg);
			return null;
		}
		
		// Verifica se a 'Nova Senha' é igual a alguma das 3 últimas(Senhas Antigas).
		if (usuario.getSenhasAntigas().contains(novaSenha)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "A 'Nova Senha' não pode ser igual a uma 'Senha Antiga'", "A 'Nova Senha' não pode ser igual a uma 'Senha Antiga'");
			FacesContext.getCurrentInstance().addMessage("novaSenha1", msg);
			return null;
		}

		// Deleção lógica. Usada para manter histórico.
		// Obs.: O 'Atual Usuario(usuario)' é persistido com o mesmo 'ID' pois assim mantém as associações intáctas.
		//       O 'Novo Usuario(usuarioLog)' é persistido com um novo 'ID', para efeito de log(historico).
		Usuario usuarioLog;
		
		if (usuario.isSuperUser()) {
			usuarioLog = new SuperUser();
		} else if (usuario.isCompanhiaUser()) {
			usuarioLog = new CompanhiaUser();
		} else if (usuario.isFornecedorUser()) {
			usuarioLog = new FornecedorUser();
		} else {
			log.error("Não foi possível identificar o tipo de 'Usuário'.");
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível identificar o tipo de 'Usuário'.", "Não foi possível identificar o tipo de 'Usuário'.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		
		// Biblioteca Commons BeanUtils, implementação já pronta para 'clonar' a entidade.
		try {
			/*
		     * Altera os valores defaults do BeanUtils. Agora, ao copiar de um
		     * bean para o outro, quando o valor da origem for nulo, o do destino
		     * passa a ser nulo também.
		     * No método do BeanUtils, os valores default são:
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
		log.info("'Usuário Log' salvo no BD: Com ID " + usuarioLog.getId());
		
		usuarioBean.update(usuario);
		log.info("'Usuário' atualizado no BD: Com ID " + usuario.getId());

		return "senhaInicialChangeSuccess.xhtml";
		
	}
	
	public String trocarSenha() {
	
		// 'Login(E-mail)' do usuário logado.
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();

		Usuario usuario = usuarioBean.findByEmail(usuarioLogin);
		
		// Verifica se a senha está correta e bloqueia o 'Usuário' caso digite a senha incorreta por 3 vezes consecutivas.
		if (!senha.equals(usuario.getSenha())) {
			
			if (usuario.getQtdTentativas() <= 2) {
				usuario.setQtdTentativas(usuario.getQtdTentativas() + 1);
				usuarioBean.update(usuario);
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "'Senha' inválida, o usuário será bloqueado após 3 tentativas incorretas!", "'Senha' inválida, o usuário será bloqueado após 3 tentativas incorretas!");
				FacesContext.getCurrentInstance().addMessage("senha", msg);
				return null;
			} else {
				usuario.setQtdTentativas(usuario.getQtdTentativas() + 1);
				usuario.setStatusUsuario(StatusUsuario.BLOQUEADO);
				usuarioBean.update(usuario);
				
				HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);  
				session.invalidate();  
				log.info("Usuário bloqueado: " + usuarioLogin);
				
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "'Usuário' bloqueado!", "'Usuário' bloqueado!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				return "/pages/public/logout.xhtml";
			}

		}
		
		usuario.setQtdTentativas(0);
		
		// Verifica se a 'Nova Senha' é igual a alguma das 3 últimas(Senhas Antigas).
		if (usuario.getSenhasAntigas().contains(novaSenha)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "A 'Nova Senha' não pode ser igual a uma 'Senha Antiga'", "A 'Nova Senha' não pode ser igual a uma 'Senha Antiga'");
			FacesContext.getCurrentInstance().addMessage("novaSenha1", msg);
			return null;
		}
		
		// Deleção lógica. Usada para manter histórico.
		// Obs.: O 'Atual Usuario(usuario)' é persistido com o mesmo 'ID' pois assim mantém as associações intáctas.
		//       O 'Novo Usuario(usuarioLog)' é persistido com um novo 'ID', para efeito de log(historico).
		Usuario usuarioLog;
		
		if (usuario.isSuperUser()) {
			usuarioLog = new SuperUser();
		} else if (usuario.isCompanhiaUser()) {
			usuarioLog = new CompanhiaUser();
		} else if (usuario.isFornecedorUser()) {
			usuarioLog = new FornecedorUser();
		} else {
			log.error("Não foi possível identificar o tipo de 'Usuário'.");
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível identificar o tipo de 'Usuário'.", "Não foi possível identificar o tipo de 'Usuário'.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}
		
		// Biblioteca Commons BeanUtils, implementação já pronta para 'clonar' a entidade.
		try {
			/*
			 * Altera os valores defaults do BeanUtils. Agora, ao copiar de um
			 * bean para o outro, quando o valor da origem for nulo, o do destino
			 * passa a ser nulo também.
			 * No método do BeanUtils, os valores default são:
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
		log.info("'Usuário Log' salvo no BD: Com ID " + usuarioLog.getId());
		
		usuarioBean.update(usuario);
		log.info("'Usuário' atualizado no BD: Com ID " + usuario.getId());
		
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Senha alterada com sucesso.", "Senha alterada com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "principal.xhtml";
		
	}

	public String logOut() {
		
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);  
		session.invalidate();  
		log.info("Logout: " + usuarioLogin);
		
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Usuário' finalizou a sessão!", "'Usuário' finalizou a sessão!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "/pages/public/logout.xhtml";
		
	}
	
	// Formata o login(e-mail) para pegar o que tiver antes do '@' e colocar a primeira letra em maiúscula.
	public String getUsuarioLogado() {
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		usuarioLogin = usuarioLogin.substring(0, usuarioLogin.indexOf("@"));
		
		return Character.toUpperCase(usuarioLogin.charAt(0)) + usuarioLogin.substring(1);
		
	}
	
	/**
	 * Metodo utilizado para redirecionar o usuário logado para a sua respectiva pasta domínio 'Role'.
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
			log.error("Não foi possível identificar o tipo de 'Usuário'.");
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível identificar o tipo de 'Usuário'.", "Não foi possível identificar o tipo de 'Usuário'.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			new IOException("Não foi possível identificar o tipo de 'Usuário'.");
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