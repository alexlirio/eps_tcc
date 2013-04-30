package br.com.eps.facade;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jboss.logging.Logger;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;

import br.com.eps.bean.CotacaoBean;
import br.com.eps.bean.LogCotacaoBean;
import br.com.eps.model.Cotacao;
import br.com.eps.model.ItemCotacao;
import br.com.eps.model.LogCotacao;
import br.com.eps.model.StatusCotacao;
import br.com.eps.primefaces.lazydatamodel.CotacaoFornecedorLazyList;

@SessionScoped
@ManagedBean
public class FornecedorFacade implements Serializable{

	private static final long serialVersionUID = 1L;

	Logger log = Logger.getLogger(FornecedorFacade.class);
	
	private LazyDataModel<Cotacao> cotacoesFornecedorLazy;
	
	private Cotacao cotacao = new Cotacao();
	private List<ItemCotacao> itensCotacao = new ArrayList<ItemCotacao>();
	
	@Resource(lookup = "java:jboss/mail/Eps_Gmail_MS")
	private static Session mailSession;
		
	@EJB
	private CotacaoBean cotacaoBean;
	
	@EJB
	private LogCotacaoBean logCotacaoBean;
	
	@PostConstruct
	public void init() {
		log.info("Novo FornecedorFacade instanciado, método 'init'.");
		cotacoesFornecedorLazy = new CotacaoFornecedorLazyList();
	}

	public String listCotacaoInit() {
		cotacoesFornecedorLazy = new CotacaoFornecedorLazyList();
		return "principal.xhtml";
	}
	
	public String finalizarCotacao() {

		// 'Login(E-mail)' do usuário logado.
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		
		// Adiciona informações adicionais de delecao a 'Fornecedor'.
		cotacao.setStatusCotacao(StatusCotacao.FINALIZADA);

		cotacaoBean.update(cotacao);
		log.info("Cotação " + cotacao.getCodigo() + " do fornecedor ID: " + cotacao.getFornecedor().getId() + " finalizada por" + usuarioLogin);

		logCotacaoBean.save(new LogCotacao(cotacao, new Date(), "'Cotação' finalizada por " + usuarioLogin));

		// Recursos mapeados no 'messages.properties'.
		ResourceBundle rb = ResourceBundle.getBundle("messages");
		
		String urlLink = rb.getString("application.host");
		
		// Parametro(argumento) para o título do 'E-mail'(MessageFormat)
		Object[] argsTitulo = { cotacao.getCodigo() };
		MessageFormat mft = new MessageFormat(rb.getString("email.fornecedor.cotacao.finalizada.aviso.titulo"));
		// Título formatado com o parametro.
		String msgTitulo = mft.format(argsTitulo);
		
		// Parametros(argumentos) para o 'E-mail'(MessageFormat)
		Object[] args = { cotacao.getFornecedor().getNome(), cotacao.getCodigo(), urlLink };
		MessageFormat mf = new MessageFormat(rb.getString("email.fornecedor.cotacao.finalizada.aviso.message.part0001") +
				rb.getString("email.fornecedor.cotacao.finalizada.aviso.message.part0002") +
				rb.getString("email.fornecedor.cotacao.finalizada.aviso.message.part0003"));
		// Mensagem formatada com os parametros.
		String msg = mf.format(args);
		
		sendEmail(cotacao.getUsuarioInsercao(), msgTitulo, msg);
		log.info("Email de aviso de Cotação Finalizada enviado para " + cotacao.getUsuarioInsercao());
		
		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Cotação' respondida com sucesso. Obrigado!", "'Cotação' respondida com sucesso. Obrigado!");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo);
		
		return listCotacaoInit();
			
	}

	private void sendEmail(String email, String msgTitulo, String msg) {
		// Envia 'Email'.
		try {
			// 'E-mail' configurado no 'standalone.xml'(mail-session) do Jboss.
			String mailAddress = mailSession.getProperties().getProperty("mail.smtp.user");

			MimeMessage m = new MimeMessage(mailSession);
			Address from = new InternetAddress(mailAddress);

			// TODO - Testes. Trocar linha comentada abaixo quando entrar em 'Produção'.
			Address[] to = new InternetAddress[] { new InternetAddress(mailAddress) }; // Versão para testes.
			// Address[] to = new InternetAddress[] { new InternetAddress(email) }; // Versão de 'Produção'

			m.setFrom(from);
			m.setRecipients(Message.RecipientType.TO, to);
			m.setSubject(msgTitulo);
			m.setSentDate(new Date());
			m.setContent(msg, "text/html");

			Transport.send(m);
			log.info("E-mail de aviso de 'Cotação' do 'Fornecedor' Finalizada enviado para: " + email);
		} catch (MessagingException e) {
			log.error("Não foi possível enviar email para: " + email);
			e.printStackTrace();
		}
	}

    public void onEditRow(RowEditEvent event) {  
		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Fornecedor' excluído com sucesso.", "'Fornecedor' excluído com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo); 
    }  
    
	public String responderCotacaoInit(Cotacao cotacao) {
		this.cotacao = cotacao;
		this.itensCotacao.addAll(cotacao.getItensCotacao());
		return "cotacao.xhtml";
	}	

    public LazyDataModel<Cotacao> getCotacoesFornecedorLazy() {
        return cotacoesFornecedorLazy;
    }

    public void setCotacoesFornecedorLazy(LazyDataModel<Cotacao> cotacoesFornecedorLazy) {
        this.cotacoesFornecedorLazy = cotacoesFornecedorLazy;
    }

	public Cotacao getCotacao() {
		return cotacao;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public List<ItemCotacao> getItensCotacao() {
		return itensCotacao;
	}

	public void setItensCotacao(List<ItemCotacao> itensCotacao) {
		this.itensCotacao = itensCotacao;
	}
	
}