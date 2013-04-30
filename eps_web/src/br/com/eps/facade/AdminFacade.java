package br.com.eps.facade;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.jboss.logging.Logger;
import org.jboss.security.auth.spi.Util;
import org.primefaces.model.LazyDataModel;

import br.com.eps.bean.CompanhiaBean;
import br.com.eps.bean.EnderecoBean;
import br.com.eps.bean.FornecedorBean;
import br.com.eps.bean.MunicipioBean;
import br.com.eps.bean.PaisBean;
import br.com.eps.bean.TelefoneBean;
import br.com.eps.bean.UfBean;
import br.com.eps.bean.UsuarioBean;
import br.com.eps.model.Companhia;
import br.com.eps.model.CompanhiaUser;
import br.com.eps.model.Endereco;
import br.com.eps.model.Municipio;
import br.com.eps.model.Pais;
import br.com.eps.model.Role;
import br.com.eps.model.StatusUsuario;
import br.com.eps.model.Telefone;
import br.com.eps.model.TipoEndereco;
import br.com.eps.model.TipoTelefone;
import br.com.eps.model.Uf;
import br.com.eps.model.Usuario;
import br.com.eps.primefaces.lazydatamodel.CompanhiaLazyList;

@SessionScoped
@ManagedBean
public class AdminFacade implements Serializable{

	private static final long serialVersionUID = 1L;

	Logger log = Logger.getLogger(AdminFacade.class);
	
	private boolean editCompanhia;
	
	private Companhia companhia = new Companhia();
	private Telefone telefone = new Telefone();
	private Endereco endereco = new Endereco();
	private Pais pais = new Pais();
	private Uf uf = new Uf();
	private Municipio municipio = new Municipio();
	private String municipioString;
	private String bairroString;

	private Pais brasil = new Pais();
	
	private LazyDataModel<Companhia> companhiasLazy;
	
	// Usados para serem persistidos com a finalidade de log.
	Companhia companhiaLog = new Companhia();
	
	// Usados para deleção.
	// Usando o 'confirmDialog' do primefaces, não se pode passar o objeto da linha do dataTable.
	private Companhia companhiaSelected = new Companhia();
	
	@Resource(lookup="java:jboss/mail/Eps_Gmail_MS")
	private static Session mailSession;
	
	@EJB
	private CompanhiaBean companhiaBean;
	
	@EJB
	private FornecedorBean fornecedorBean;
	
	@EJB
	private PaisBean paisBean;
	
	@EJB
	private UfBean ufBean;
	
	@EJB
	private MunicipioBean municipioBean;
	
	@EJB
	private UsuarioBean usuarioBean;

	@EJB
	private TelefoneBean telefoneBean;
	
	@EJB
	private EnderecoBean enderecoBean;
	
	@PostConstruct
	public void init() {
		this.brasil = paisBean.findByCodigo("1058"); // Brasil
		log.info("Novo AdminFacade instanciado, método 'init'.");
	}
	
	public String cadastroCompanhiaInit() {
		this.editCompanhia = false;
		this.companhia = new Companhia();
		this.telefone = new Telefone();
		this.endereco = new Endereco();
		this.pais = new Pais();
		this.uf = new Uf();
		this.municipio = new Municipio();
		
		return "companhia.xhtml";
	}

	public String listCompanhiaInit() {
		companhiasLazy = new CompanhiaLazyList();
		return "companhiaList.xhtml";
	}

	public String editCompanhiaInit(Companhia companhia) {
		
		this.editCompanhia = true;
		this.companhia = companhia;
		this.telefone = telefoneBean.listByCompanhia(companhia).get(0);
		this.endereco = enderecoBean.listByCompanhia(companhia).get(0);
		// Verifica se a 'Companhia' contém um 'Pais' com 'Uf' e 'Municipio'.
		if (this.endereco.getMunicipio() != null) {
			this.pais = this.endereco.getMunicipio().getUf().getPais();
			this.uf = this.endereco.getMunicipio().getUf();
			this.municipio = this.endereco.getMunicipio();
		} else {
			this.pais = this.endereco.getPais();
		}
		
		companhiaLog = new Companhia();
		Telefone telefoneLog = new Telefone();
		Endereco enderecoLog = new Endereco();
		
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
			BeanUtils.copyProperties(companhiaLog, companhia);
			BeanUtils.copyProperties(telefoneLog, companhia.getTelefones().iterator().next());
			BeanUtils.copyProperties(enderecoLog, companhia.getEnderecos().iterator().next());

		} catch (IllegalAccessException e) {
			log.error("Acesso Ilegal", e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("Erro Invocation", e);
			e.printStackTrace();
		}
		
		// Inicializa 'companhiaLog'.
		companhiaLog.setId(null);
		companhiaLog.setTelefones(new HashSet<Telefone>());
		telefoneLog.setId(null);
		telefoneLog.setCompanhia(companhiaLog);
		companhiaLog.getTelefones().add(telefoneLog);
		companhiaLog.setEnderecos(new HashSet<Endereco>());
		enderecoLog.setId(null);
		enderecoLog.setCompanhia(companhiaLog);
		companhiaLog.getEnderecos().add(enderecoLog);
		
		return "companhia.xhtml";

	}
	
	public SelectItem[] getTiposTelefones() {
	    SelectItem[] items = new SelectItem[TipoTelefone.values().length];
	    int i = 0;
	    for(TipoTelefone t: TipoTelefone.values()) {
	      items[i++] = new SelectItem(t, t.getLabel());
	    }
	    return items;
	  }
	
	public SelectItem[] getTiposEnderecos() {
		SelectItem[] items = new SelectItem[TipoEndereco.values().length];
		int i = 0;
		for(TipoEndereco t: TipoEndereco.values()) {
			items[i++] = new SelectItem(t, t.getLabel());
		}
		return items;
	}
	
	public List<Pais> getPaises() {
		
        List<Pais> paises = paisBean.listAll();
        
        // Troca posição do Brasil para primeiro da lista.
        Pais p = paises.get(paises.indexOf(this.brasil));
        paises.remove(p);
        paises.add(0, p);
        
		return paises;
    }
	
	public List<Uf> getUfs() {
		if (this.pais.getId() != null) {
			return ufBean.listByPais(this.pais);
		} else {
			return new ArrayList<Uf>();
		}
	}
	
	public List<Municipio> getMunicipios() {
		if (this.uf.getId() != null) {
			return municipioBean.listByUf(this.uf);
		} else {
			return new ArrayList<Municipio>();
		}
	}
	
	public String saveCompanhia() {
		
		// Verifica se email ja esta cadastrado para uma 'Companhia' ou 'Fornecedor'.
		if (companhiaBean.findByEmail(companhia.getEmail()).getId() != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Email já cadastrado para uma 'Companhia'", "CNPJ/CPF já cadastrado para uma 'Companhia'");
			FacesContext.getCurrentInstance().addMessage("email", msg);
			return null;
		}
		if (fornecedorBean.findByEmail(companhia.getEmail()).getId() != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Email já cadastrado para um 'Fornecedor'", "CNPJ/CPF já cadastrado para um 'Fornecedor'");
			FacesContext.getCurrentInstance().addMessage("email", msg);
			return null;
		}

		// Verifica se cnpj ja esta cadastrado para uma 'Companhia' ou 'Fornecedor'.
		if (companhiaBean.findByCnpjCpf(companhia.getCnpjCpf()).getId() != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "CNPJ/CPF já cadastrado para uma 'Companhia'", "CNPJ/CPF já cadastrado para uma 'Companhia'");
			FacesContext.getCurrentInstance().addMessage("cnpjCpf", msg);
			return null;
		}
		if (fornecedorBean.findByCnpjCpf(companhia.getCnpjCpf()).getId() != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "CNPJ/CPF já cadastrado para um 'Fornecedor'", "CNPJ/CPF já cadastrado para um 'Fornecedor'");
			FacesContext.getCurrentInstance().addMessage("cnpjCpf", msg);
			return null;
		}
		
		// Adiciona informações adicionais de inserção a 'Companhia'.
		companhia.setUsuarioInsercao(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
		companhia.setDtInsercao(new Date());
		
		// Adiciona a 'Companhia' ao 'Telefone'.
		telefone.setCompanhia(companhia);
		Set<Telefone> telefones = new HashSet<Telefone>();
		telefones.add(telefone);
		companhia.setTelefones(telefones);
		
		// Adiciona o Municipio ao 'Endereco', se 'Pais' possuir 'Ufs'.
		// No momento, todos os paises que possuem 'Ufs' possuem 'Municípios'.
		if (paisHasUfs()) {
			// Se 'Pais' possuir 'Ufs'
			endereco.setMunicipio(municipio);
			
			// Adiciona a Companhia ao 'Endereco'.
			endereco.setCompanhia(companhia);
			Set<Endereco> enderecos = new HashSet<Endereco>();
			enderecos.add(endereco);
			companhia.setEnderecos(enderecos);
			
		} else {
			// Se 'Pais' não possuir 'Ufs'
			endereco.setPais(pais);
			
			// Adiciona a Companhia ao 'Endereco'.
			endereco.setCompanhia(companhia);
			Set<Endereco> enderecos = new HashSet<Endereco>();
			enderecos.add(endereco);
			companhia.setEnderecos(enderecos);
		}
		
		
		
		/** Criação de 'Usuário'. **/ 
		
		// Verifica se email ja esta cadastrado para um 'Usuario'.
		if (usuarioBean.findByEmail(companhia.getEmail()).getId() != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Email já cadastrado para um 'Usuário'", "Email já cadastrado para um 'Usuário'");
			FacesContext.getCurrentInstance().addMessage("email", msg);
			return null;
		}
		
		// Cria um 'Usuario'(CompanhiaUser).
		CompanhiaUser companhiaUser = new CompanhiaUser();
		companhiaUser.setUsuarioInsercao(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
		companhiaUser.setDtInsercao(new Date());
		companhiaUser.setRole(Role.COMPANHIA);
		companhiaUser.setEmail(companhia.getEmail());
		
		// Criando senha inicial aleatoria para o 'Usuario'
		String senhaInicial = new BigInteger(32, new SecureRandom()).toString(32);
		
		companhiaUser.setSenhaInicial(senhaInicial);
		companhiaUser.setSenha(Util.createPasswordHash("SHA-256", "BASE64", null, null, senhaInicial));
		companhiaUser.setSenhasAntigas(Util.createPasswordHash("SHA-256", "BASE64", null, null, senhaInicial));
		companhiaUser.setQtdTentativas(0);
		companhiaUser.setStatusUsuario(StatusUsuario.INATIVO);

		// Persiste no BD
		companhiaBean.save(companhia);
		log.info("Companhia salva no BD: " + companhia.getEmail());
		
		companhiaUser.setCompanhia(companhia);
		usuarioBean.save(companhiaUser);
		log.info("Usuário Companhia salvo no BD: " + companhiaUser.getEmail());
		
		
		
		/** Criação do Email de Boas-Vindas com a "Senha Inicial" e "Instruções". **/ 
		
		// Recursos mapeados no 'messages.properties'.
	    ResourceBundle rb = ResourceBundle.getBundle("messages");

	    String msgTitulo = rb.getString("email.companhia.titulo");
	    String urlTrocaSenhaInicial = rb.getString("application.host") + "pages/public/senhaInicialChange.xhtml";
	    String urlCompanhiaUserAccess = rb.getString("application.host");
	   
	    // Parametros(argumentos) para o 'E-mail'(MessageFormat)
	    Object[] args = { senhaInicial, urlTrocaSenhaInicial, urlCompanhiaUserAccess };
	    
	    MessageFormat mf = new MessageFormat(rb.getString("email.companhia.message.part0001") + 
				 							 rb.getString("email.companhia.message.part0002") +
				 							 rb.getString("email.companhia.message.part0003"));
	    
	    // Mensagem formatada com os parametros.
	    String msg = mf.format(args);
		
	    sendEmail(companhiaUser, msgTitulo, msg);
	    
		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Companhia' cadastrada com sucesso.", "'Companhia' cadastrada com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo);
		
	    return "principal.xhtml";
	}
	
	public String updateCompanhia() {
		
		// 'Login(E-mail)' do usuário logado.
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();

		// Adiciona informações adicionais de inserção a 'Companhia'.
		companhia.setUsuarioInsercao(usuarioLogin);
		companhia.setDtInsercao(new Date());
		
		// Adiciona a 'Companhia' ao 'Telefone'.
		telefone.setCompanhia(companhia);
		Set<Telefone> telefones = new HashSet<Telefone>();
		telefones.add(telefone);
		companhia.setTelefones(telefones);
		
		// Adiciona 'Telefone'.
		companhia.getTelefones().clear();
		companhia.getTelefones().add(telefone);
		
		// Adiciona 'Endereco'.
		if (paisHasUfs()) {
			// Se 'Pais' possuir 'Ufs'
			endereco.setPais(null);
			endereco.setMunicipio(municipio);
		} else {
			// Se 'Pais' não possuir 'Ufs'
			endereco.setMunicipio(null);
			endereco.setPais(pais);
		}
		companhia.getEnderecos().clear();
		companhia.getEnderecos().add(endereco);

		// Persiste no BD
		companhiaLog.setDtDelecao(new Date());
		companhiaLog.setUsuarioDelecao(usuarioLogin);
		
		companhiaBean.update(companhia);
		log.info("'Companhia' atualizado no BD: Com ID " + companhia.getId());
		
		companhiaBean.save(companhiaLog);
		log.info("'Companhia Log' salvo no BD: Com ID " + companhiaLog.getId());

		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Companhia' alterada com sucesso.", "'Companhia' alterada com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo);
		
		return "principal.xhtml";
	}

	public void excluirCompanhia() {

		// 'Login(E-mail)' do usuário logado.
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		
		// Adiciona informações adicionais de delecao a 'Companhia'.
		companhiaSelected.setUsuarioDelecao(usuarioLogin);
		companhiaSelected.setDtDelecao(new Date());
		
		// Seleciona o 'Usuario'(CompanhiaUser) da 'Companhia' para deleção.
		Usuario usuario = usuarioBean.findByEmail(companhiaSelected.getEmail());
		usuario.setUsuarioDelecao(usuarioLogin);
		usuario.setDtDelecao(new Date());
		
		// Persiste no BD
		companhiaBean.update(companhiaSelected);
		log.info("'Companhia' excluída logicamente no BD: Com ID " + companhiaSelected.getId());
		
		usuarioBean.update(usuario);
		log.info("'Usuário'(CompanhiaUser) excluído logicamente no BD: Com ID " + usuario.getId());

		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Companhia' excluída com sucesso.", "'Companhia' excluída com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo);
		
//		return listInit();
//		companhiasLazy = new CompanhiaLazyList();
		
	}

	private void sendEmail(CompanhiaUser companhiaUser, String msgTitulo,
			String msg) {
		// Envia 'Email' de boas vindas com a 'Senha Inicial' e instruções.
		try {
			// 'E-mail' configurado no 'standalone.xml'(mail-session) do Jboss.
			String mailAddress = mailSession.getProperties().getProperty("mail.smtp.user");

		    MimeMessage m = new MimeMessage(mailSession);
		    Address from = new InternetAddress(mailAddress);

		    // TODO - Testes. Trocar linha comentada abaixo quando entrar em 'Produção'.
		    Address[] to = new InternetAddress[] {new InternetAddress(mailAddress) }; // Versão para testes.
//		    Address[] to = new InternetAddress[] {new InternetAddress(companhiaUser.getEmail()) }; // Versão de 'Produção'

		    m.setFrom(from);
		    m.setRecipients(Message.RecipientType.TO, to);
		    m.setSubject(msgTitulo);
		    m.setSentDate(new Date());
		    m.setContent(msg, "text/html");

		    Transport.send(m);
		    log.info("E-mail de boas-vindas enviado para: " + companhiaUser.getEmail());
		} catch (MessagingException e) {
			log.error("Não foi possível enviar email para: " + companhiaUser.getEmail());
		    e.printStackTrace();
		}
	}
	
	public void paisChanged() {  
        this.uf = new Uf();
    }  

	public boolean paisHasUfs() {
		return ufBean.countByPais(pais) != 0 ? true : false;
	}
	
	public Companhia getCompanhia() {
		return companhia;
	}

	public void setCompanhia(Companhia companhia) {
		this.companhia = companhia;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf = uf;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public String getMunicipioString() {
		return municipioString;
	}

	public void setMunicipioString(String municipioString) {
		this.municipioString = municipioString;
	}

	public String getBairroString() {
		return bairroString;
	}

	public void setBairroString(String bairroString) {
		this.bairroString = bairroString;
	}

	public Pais getBrasil() {
		return brasil;
	}

	public void setBrasil(Pais brasil) {
		this.brasil = brasil;
	}

    public LazyDataModel<Companhia> getCompanhiasLazy() {
        return companhiasLazy;
    }

    public void setCompanhiasLazy(LazyDataModel<Companhia> companhiasLazy) {
        this.companhiasLazy = companhiasLazy;
    }
    
	public Companhia getCompanhiaLog() {
		return companhiaLog;
	}

	public void setCompanhiaLog(Companhia companhiaLog) {
		this.companhiaLog = companhiaLog;
	}

	public Companhia getCompanhiaSelected() {
		return companhiaSelected;
	}

	public void setCompanhiaSelected(Companhia companhiaSelected) {
		this.companhiaSelected = companhiaSelected;
	}
	
	public boolean isEditCompanhia() {
		return editCompanhia;
	}

	public void setEditCompanhia(boolean editCompanhia) {
		this.editCompanhia = editCompanhia;
	}

}