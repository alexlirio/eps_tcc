package br.com.eps.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.jboss.logging.Logger;

import br.com.eps.bean.CotacaoBean;
import br.com.eps.bean.FornecedorBean;
import br.com.eps.bean.ItemCotacaoBean;
import br.com.eps.bean.LogCotacaoBean;
import br.com.eps.bean.ProdutoBean;
import br.com.eps.model.Cotacao;
import br.com.eps.model.Fornecedor;
import br.com.eps.model.ItemCotacao;
import br.com.eps.model.LogCotacao;
import br.com.eps.model.StatusCotacao;

@Stateless
public class CotacaoService {

	Logger log = Logger.getLogger(CotacaoService.class);

	@Resource(lookup = "java:jboss/mail/Eps_Gmail_MS")
	private static Session mailSession;

	@EJB
	private CotacaoBean cotacaoBean;

	@EJB
	private ItemCotacaoBean itemCotacaoBean;

	@EJB
	private LogCotacaoBean logCotacaoBean;

	@EJB
	private ProdutoBean produtoBean;

	@EJB
	private FornecedorBean fornecedorBean;

	// TODO - Testes. Trocar linha comentada abaixo quando entrar em 'Produ��o'.
//	@Schedule // Vers�o de 'Produ��o'. Executa 1 vez por dia, sempre �s 0h.
	@Schedule(minute = "*/1", hour = "*", persistent = false) // Vers�o para testes. Executa a cada 1 minuto.
	private void gerarCotacoes(Timer timer) {

		log.info("'Schedule inicializarCotacoesTask' de 'CotacaoService.java' iniciado.");
		
		// Lista as cota��es com status = Criada e com data inicio <= hoje.
		List<Cotacao> cotacoesInicializaveis = cotacaoBean.listInicializaveis();
		for (Cotacao cotacaoInicializavel : cotacoesInicializaveis) {
			//
			// Lista de Cota��es. Uma Cota��o para cada Fornecedor
			List<Cotacao> cotacoesFornecedores = new ArrayList<Cotacao>();

			List<ItemCotacao> itensCotacao = itemCotacaoBean.listByCotacao(cotacaoInicializavel);
			for (ItemCotacao ic : itensCotacao) {
				
				for (Fornecedor f : fornecedorBean.listByProduto(ic.getProduto())) {
					
					Cotacao c = new Cotacao();
					
					c.setCodigo(cotacaoInicializavel.getCodigo());
					c.setDtInsercao(new Date());
					c.setUsuarioInsercao(cotacaoInicializavel.getUsuarioInsercao());
					c.setDtInicioValidade(cotacaoInicializavel.getDtInicioValidade());
					c.setDtFimValidade(cotacaoInicializavel.getDtFimValidade());
					c.setStatusCotacao(StatusCotacao.INICIADA);
					c.setItensCotacao(new HashSet<ItemCotacao>());
					c.setFornecedor(f);

					if (cotacoesFornecedores.contains(c)) {
						continue;
					}
					
					cotacoesFornecedores.add(c);

				}
			}

			for (ItemCotacao ic : itensCotacao) {
				
				List<Fornecedor> fornecedores = fornecedorBean.listByProduto(ic.getProduto());
				// Se produto n�o tiver fonecedores, envia email para a companhia avisando.
				if (fornecedores.isEmpty()) {
					// Recursos mapeados no 'messages.properties'.
					ResourceBundle rb = ResourceBundle.getBundle("messages");

					String msgTitulo = rb.getString("email.companhia.cotacao.titulo");
					String codigoCotacao = cotacaoInicializavel.getCodigo();
					String nomeCodigoProduto = ic.getProduto().getNome() + "(C�digo: " + ic.getProduto().getCodigo() + ")";

					// Parametros(argumentos) para o 'E-mail'(MessageFormat)
					Object[] args = { codigoCotacao, nomeCodigoProduto };

					MessageFormat mf = new MessageFormat(rb.getString("email.companhia.cotacao.message.part0001") +
														 rb.getString("email.companhia.cotacao.message.part0002") +
														 rb.getString("email.companhia.cotacao.message.part0003"));

					// Mensagem formatada com os parametros.
					String msg = mf.format(args);

					sendEmail(cotacaoInicializavel.getUsuarioInsercao(), msgTitulo, msg);
					
					log.warn("'Email' de alerta enviado para " + cotacaoInicializavel.getUsuarioInsercao() + " pois 'Produto' " + nomeCodigoProduto + " da 'Cota��o' " + codigoCotacao + " n�o possui 'Fornecedor'.");

				} else {
					for (Fornecedor f : fornecedores) {
						
						for (Cotacao c : cotacoesFornecedores) {
							
							if (f.equals(c.getFornecedor())) {
								ItemCotacao itemCotacaoNew = new ItemCotacao();
								
								copyProperties(itemCotacaoNew, ic);
								
								itemCotacaoNew.setId(null);
								itemCotacaoNew.setCotacao(c);
								c.getItensCotacao().add(itemCotacaoNew);

							}
						}
					}
				}
			}
			// Envia email para cada Fornecedor com aviso de cota��o dispon�vel.
			for (Cotacao c : cotacoesFornecedores) {
				
				cotacaoBean.save(c);
				logCotacaoBean.save(new LogCotacao(c, new Date(), "'Cota��o' " + c.getCodigo() + " fornecedor " + c.getFornecedor().getEmail() + " iniciada."));
				log.info("'Cota��o' " + c.getCodigo() + " fornecedor " + c.getFornecedor().getEmail() + " iniciada.");
				
				// Recursos mapeados no 'messages.properties'.
				ResourceBundle rb = ResourceBundle.getBundle("messages");

				String codigoCotacao = c.getCodigo();
				String nomeFornecedor = c.getFornecedor().getNome();
				String urlLink = rb.getString("application.host");

				// Parametro(argumento) para o t�tulo do 'E-mail'(MessageFormat)
				Object[] argsTitulo = { codigoCotacao };
				MessageFormat mft = new MessageFormat(rb.getString("email.cotacao.aviso.titulo"));
				// T�tulo formatado com o parametro.
				String msgTitulo = mft.format(argsTitulo);

				// Parametros(argumentos) para o 'E-mail'(MessageFormat)
				Object[] args = { nomeFornecedor, urlLink };
				MessageFormat mf = new MessageFormat(rb.getString("email.cotacao.aviso.message.part0001") +
													 rb.getString("email.cotacao.aviso.message.part0002") +
													 rb.getString("email.cotacao.aviso.message.part0003"));
				// Mensagem formatada com os parametros.
				String msg = mf.format(args);

				sendEmail(c.getFornecedor().getEmail(), msgTitulo, msg);
				log.info("Email de aviso de Cota��o Criada enviado para " + c.getFornecedor().getEmail());

			}
			
			// Depois de criar as cota��es para os fornecedores, a 'Cota��o' com o 'Status CRIADA' � deletada.
			cotacaoInicializavel.setDtDelecao(new Date());
			cotacaoInicializavel.setUsuarioDelecao("sistema");
			
			cotacaoBean.save(cotacaoInicializavel);
			log.info("'Cotacao' exclu�do logicamente no BD: Com ID " + cotacaoInicializavel.getId());
			logCotacaoBean.save(new LogCotacao(cotacaoInicializavel, new Date(), "'Cota��o' exclu�da."));
		}
		
		log.info("'Schedule inicializarCotacoesTask' de 'CotacaoService.java' finalizado.");
		
		log.info("'Schedule finalizarCotacoesTask' de 'CotacaoService.java' iniciado.");
		
		// Lista as cota��es com data fim <= hoje.
		List<Cotacao> cotacoesFinalizaveis = cotacaoBean.listFinalizaveis();
		
		// Lista com os c�digos das cota��es finalizadas pelo sistema. Cota��es com data fim <= hoje.
		Map<String, String> cotacoesFinalizadas = new HashMap<String, String>();
		
		for (Cotacao cotacaoFinalizavel : cotacoesFinalizaveis) {
			
			cotacoesFinalizadas.put(cotacaoFinalizavel.getCodigo(), cotacaoFinalizavel.getUsuarioInsercao());

			// Seta as cota��es com o 'Status FINALIZADA', pois algumas podem n�o terem sido finalizadas pelos fornecedores.
			cotacaoFinalizavel.setStatusCotacao(StatusCotacao.FINALIZADA);
			
			cotacaoBean.update(cotacaoFinalizavel);
			log.info("'Cotacao' ID " + cotacaoFinalizavel.getId() + " finalizada pelo sistema pois a expirou a Data Fim Validade.");
			logCotacaoBean.save(new LogCotacao(cotacaoFinalizavel, new Date(), "'Cotacao' ID " + cotacaoFinalizavel.getId() + " finalizada pelo sistema pois a expirou a Data Fim Validade."));

		}
		
		if (!cotacoesFinalizadas.isEmpty()) {
			
			// Envia email para cada Companhia avisando das Cota��es Finalizadas com data fim <= hoje.
			for (String cotacao : cotacoesFinalizadas.keySet()) {
				
				String emailCompanhia = cotacoesFinalizadas.get(cotacao);

				// Recursos mapeados no 'messages.properties'.
				ResourceBundle rb = ResourceBundle.getBundle("messages");
				
				String codigoCotacao = cotacao;
				String urlLink = rb.getString("application.host");
				
				// Parametro(argumento) para o t�tulo do 'E-mail'(MessageFormat)
				Object[] argsTitulo = { codigoCotacao };
				MessageFormat mft = new MessageFormat(rb.getString("email.companhia.cotacao.finalizada.aviso.titulo"));
				// T�tulo formatado com o parametro.
				String msgTitulo = mft.format(argsTitulo);
				
				// Parametros(argumentos) para o 'E-mail'(MessageFormat)
				Object[] args = { emailCompanhia, codigoCotacao, urlLink };
				MessageFormat mf = new MessageFormat(rb.getString("email.companhia.cotacao.finalizada.aviso.message.part0001") +
						rb.getString("email.companhia.cotacao.finalizada.aviso.message.part0002") +
						rb.getString("email.companhia.cotacao.finalizada.aviso.message.part0003"));
				// Mensagem formatada com os parametros.
				String msg = mf.format(args);
				
				sendEmail(emailCompanhia, msgTitulo, msg);
				log.info("Email de aviso de Cota��o Finalizada enviado para " + emailCompanhia);
				
			}

		}
		
		log.info("'Schedule InicializarCotacoesTask' de 'CotacaoService.java' finalizado.");

	}

	private void copyProperties(ItemCotacao ic, ItemCotacao itemCotacaoNew) {
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
			ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
			
			// Faz um 'clone' de 'usuario'.
			BeanUtils.copyProperties(ic, itemCotacaoNew);

		} catch (IllegalAccessException e) {
			log.error("Acesso Ilegal", e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("Erro Invocation", e);
			e.printStackTrace();
		}
	}

	@Timeout
	private void scheduledTimeout(Timer timer) {
		log.info("M�todo 'Timeout' de 'CotacaoService' invocado.");
	}

	private void sendEmail(String email, String msgTitulo, String msg) {
		// Envia 'Email'.
		try {
			// 'E-mail' configurado no 'standalone.xml'(mail-session) do Jboss.
			String mailAddress = mailSession.getProperties().getProperty("mail.smtp.user");

			MimeMessage m = new MimeMessage(mailSession);
			Address from = new InternetAddress(mailAddress);

			// TODO - Testes. Trocar linha comentada abaixo quando entrar em 'Produ��o'.
			Address[] to = new InternetAddress[] { new InternetAddress(mailAddress) }; // Vers�o para testes.
			// Address[] to = new InternetAddress[] { new InternetAddress(email) }; // Vers�o de 'Produ��o'

			m.setFrom(from);
			m.setRecipients(Message.RecipientType.TO, to);
			m.setSubject(msgTitulo);
			m.setSentDate(new Date());
			m.setContent(msg, "text/html");

			Transport.send(m);
			log.info("E-mail de aviso de 'Cota��o' com 'Produto' sem 'Fornecedor' enviado para: "
					+ email);
		} catch (MessagingException e) {
			log.error("N�o foi poss�vel enviar email para: " + email);
			e.printStackTrace();
		}
	}
}