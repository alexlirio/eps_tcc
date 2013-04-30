package br.com.eps.facade;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.jboss.logging.Logger;
import org.jboss.security.auth.spi.Util;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;

import br.com.eps.bean.CompanhiaBean;
import br.com.eps.bean.CotacaoBean;
import br.com.eps.bean.EnderecoBean;
import br.com.eps.bean.FornecedorBean;
import br.com.eps.bean.ItemCotacaoBean;
import br.com.eps.bean.LogCotacaoBean;
import br.com.eps.bean.MunicipioBean;
import br.com.eps.bean.PaisBean;
import br.com.eps.bean.ProdutoBean;
import br.com.eps.bean.TelefoneBean;
import br.com.eps.bean.UfBean;
import br.com.eps.bean.UnidadeMedidaBean;
import br.com.eps.bean.UsuarioBean;
import br.com.eps.model.Cotacao;
import br.com.eps.model.Endereco;
import br.com.eps.model.Fornecedor;
import br.com.eps.model.FornecedorUser;
import br.com.eps.model.ItemCotacao;
import br.com.eps.model.LogCotacao;
import br.com.eps.model.Municipio;
import br.com.eps.model.Pais;
import br.com.eps.model.Produto;
import br.com.eps.model.Role;
import br.com.eps.model.StatusCotacao;
import br.com.eps.model.StatusUsuario;
import br.com.eps.model.Telefone;
import br.com.eps.model.TipoEndereco;
import br.com.eps.model.TipoTelefone;
import br.com.eps.model.Uf;
import br.com.eps.model.UnidadeMedida;
import br.com.eps.model.Usuario;
import br.com.eps.primefaces.lazydatamodel.CotacaoLazyList;
import br.com.eps.primefaces.lazydatamodel.FornecedorLazyList;
import br.com.eps.primefaces.lazydatamodel.ProdutoLazyList;

@SessionScoped
@ManagedBean
public class CompanhiaFacade implements Serializable{

	private static final long serialVersionUID = 1L;

	Logger log = Logger.getLogger(CompanhiaFacade.class);
	
	private boolean editFornecedor;
	private boolean editProduto;
	
	private Fornecedor fornecedor = new Fornecedor();
	private Telefone telefone = new Telefone();
	private Endereco endereco = new Endereco();
	private Pais pais = new Pais();
	private Uf uf = new Uf();
	private Municipio municipio = new Municipio();
	private String municipioString;
	private String bairroString;

	private Pais brasil = new Pais();
	
	private LazyDataModel<Fornecedor> fornecedoresLazy;
	private LazyDataModel<Produto> produtosLazy;
	private LazyDataModel<Cotacao> cotacoesLazy;
	
	// Usados para serem persistidos com a finalidade de log.
	Fornecedor fornecedorLog = new Fornecedor();
	Produto produtoLog = new Produto();
	
	// Usados para deleção.
	// Usando o 'confirmDialog' do primefaces, não se pode passar o objeto da linha do dataTable.
	private Fornecedor fornecedorSelected = new Fornecedor();
	private Produto produtoSelected = new Produto();
	private Cotacao cotacaoSelected = new Cotacao();
	
	private Produto produto = new Produto();
	
	// Usados na associação Fornecedor x Produtos. Usados no cadastro de 'Cotação' também.
	private List<Produto> produtosSource = new ArrayList<Produto>();
	private List<Produto> produtosTarget = new ArrayList<Produto>();
	private DualListModel<Produto> produtosDualListModel;

	// Usados no cadastro de 'Cotação'.
	private String codigoCotacao;
	private Date dtInicioValidade;
	private Date dtFimValidade;
	private List<ItemCotacao> itensCotacao;
	private Date minDtInicioValidade;
	private Date minDtFimValidade;
	private Cotacao cotacao;
	
	@Resource(lookup="java:jboss/mail/Eps_Gmail_MS")
	private static Session mailSession;
	
	@EJB
	private FornecedorBean fornecedorBean;

	@EJB
	private CompanhiaBean companhiaBean;
	
	@EJB
	private PaisBean paisBean;
	
	@EJB
	private UnidadeMedidaBean unidadeMedidaBean;
	
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
	
	@EJB
	private ProdutoBean produtoBean;
	
	@EJB
	private CotacaoBean cotacaoBean;
	
	@EJB
	private ItemCotacaoBean itemCotacaoBean;
	
	@EJB
	private LogCotacaoBean logCotacaoBean;
	
	@PostConstruct
	public void init() {
		this.brasil = paisBean.findByCodigo("1058"); // Brasil
		log.info("Nova CompanhiaFacade instanciada, método 'init'.");
	}
	
	public String cadastroFornecedorInit() {
		this.editFornecedor = false;
		this.fornecedor = new Fornecedor();
		this.telefone = new Telefone();
		this.endereco = new Endereco();
		this.pais = new Pais();
		this.uf = new Uf();
		this.municipio = new Municipio();
		
		return "fornecedor.xhtml";
	}
	
	public String cadastroProdutoInit() {
		this.editProduto = false;
		this.produto = new Produto();
		
		return "produto.xhtml";
	}

	public String cadastroCotacaoInit() {
		this.codigoCotacao = null;
		this.dtInicioValidade = null;
		this.dtFimValidade = null;
		this.itensCotacao = new ArrayList<ItemCotacao>();
		this.minDtInicioValidade = new Date();
		this.minDtFimValidade = new Date();
		this.produtosSource = produtoBean.listAll();
		this.produtosTarget = new ArrayList<Produto>();
		this.produtosDualListModel = new DualListModel<Produto>(produtosSource, produtosTarget);
		this.cotacao = new Cotacao();

		return "cotacao.xhtml";
	}

	public String listFornecedorInit() {
		fornecedoresLazy = new FornecedorLazyList();
		return "fornecedorList.xhtml";
	}
	
	public String listProdutoInit() {
		produtosLazy = new ProdutoLazyList();
		return "produtoList.xhtml";
	}
	
	public String listCotacaoInit() {
		cotacoesLazy = new CotacaoLazyList();
		return "cotacaoList.xhtml";
	}

	public String editFornecedorInit(Fornecedor fornecedor) {
		
		this.editFornecedor = true;
		this.fornecedor = fornecedor;
		this.telefone = telefoneBean.listByFornecedor(fornecedor).get(0);
		this.endereco = enderecoBean.listByFornecedor(fornecedor).get(0);
		// Verifica se o 'Fornecedor' contém um 'Pais' com 'Uf' e 'Municipio'.
		if (this.endereco.getMunicipio() != null) {
			this.pais = this.endereco.getMunicipio().getUf().getPais();
			this.uf = this.endereco.getMunicipio().getUf();
			this.municipio = this.endereco.getMunicipio();
		} else {
			this.pais = this.endereco.getPais();
		}
		
		fornecedorLog = new Fornecedor();
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
			BeanUtils.copyProperties(fornecedorLog, fornecedor);
			BeanUtils.copyProperties(telefoneLog, fornecedor.getTelefones().iterator().next());
			BeanUtils.copyProperties(enderecoLog, fornecedor.getEnderecos().iterator().next());

		} catch (IllegalAccessException e) {
			log.error("Acesso Ilegal", e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("Erro Invocation", e);
			e.printStackTrace();
		}
		
		// Inicializa 'fornecedorLog'.
		fornecedorLog.setId(null);
		fornecedorLog.setTelefones(new HashSet<Telefone>());
		telefoneLog.setId(null);
		telefoneLog.setFornecedor(fornecedorLog);
		fornecedorLog.getTelefones().add(telefoneLog);
		fornecedorLog.setEnderecos(new HashSet<Endereco>());
		enderecoLog.setId(null);
		enderecoLog.setFornecedor(fornecedorLog);
		fornecedorLog.getEnderecos().add(enderecoLog);
		
		return "fornecedor.xhtml";

	}
	
	public String editProdutoInit(Produto produto) {
		
		this.editProduto = true;
		this.produto = produto;
	
		produtoLog = new Produto();

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
			BeanUtils.copyProperties(produtoLog, produto);
			
		} catch (IllegalAccessException e) {
			log.error("Acesso Ilegal", e);
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			log.error("Erro Invocation", e);
			e.printStackTrace();
		}
		
		// Inicializa 'fornecedorLog'.
		produtoLog.setId(null);

		return "produto.xhtml";
		
	}

	public String editFornecedorProdutosInit(Fornecedor fornecedor) {

		this.produto = new Produto();
		this.fornecedor = fornecedor;
		
		produtosSource = produtoBean.listAll();
		produtosTarget = produtoBean.listByFornecedor(fornecedor);
		produtosSource.removeAll(produtosTarget);
		produtosDualListModel = new DualListModel<Produto>(produtosSource, produtosTarget);
		
		return "fornecedorProdutos.xhtml";
	}

	public String viewCotacaoInit(Cotacao cotacao) {
		this.codigoCotacao = cotacao.getCodigo();
		this.dtInicioValidade = cotacao.getDtInicioValidade();
		this.dtFimValidade = cotacao.getDtFimValidade();
		this.itensCotacao = itemCotacaoBean.listByCotacao(cotacao);

		return "cotacaoView.xhtml";

	}

	// Método Chamado no 'onChange' da página de associação 'Fornecedor x Produto'(fornecedorProdutos.xhtml).
    public void onTransferProdutoFornecedor(TransferEvent event) {
    	int adicionados = 0;
    	int removidos = 0;
        for(Object item : event.getItems()) {
        	if (this.produtosSource.contains(item)) {
        		this.produtosSource.remove((Produto)item);
        		((Produto)item).getFornecedores().add(this.fornecedor);
        		this.produtosTarget.add((Produto)item);
        		produtoBean.update((Produto)item);
        		adicionados++;
			} else {
				this.produtosTarget.remove((Produto)item);
				((Produto)item).getFornecedores().remove(this.fornecedor);
				this.produtosSource.add((Produto)item);
				produtoBean.update((Produto)item);
				removidos++;
			}
        }
        
        if (adicionados > 0) {
        	FacesMessage msgAdicionado = new FacesMessage(FacesMessage.SEVERITY_INFO, adicionados + " Produto(s) Adicionado(s).", adicionados + " Produto(s) Adicionado(s).");
        	FacesContext.getCurrentInstance().addMessage(null, msgAdicionado);
		}
        
        if (removidos > 0) {
        	FacesMessage msgRemovidos = new FacesMessage(FacesMessage.SEVERITY_INFO, removidos + " Produto(s) Removido(s).", removidos + " Produto(s) Removido(s).");
        	FacesContext.getCurrentInstance().addMessage(null, msgRemovidos);
        }

    }

    // Método Chamado no 'onChange' da aba de 'Produtos' página de cadastro de 'Cotacao'(cotacao.xhtml).
    public void onTransferProdutoCotacao(TransferEvent event) {
    	int adicionados = 0;
    	int removidos = 0;
    	for(Object item : event.getItems()) {
    		if (this.produtosSource.contains(item)) {
    			this.produtosSource.remove((Produto)item);
    			this.produtosTarget.add((Produto)item);
    			adicionados++;
    		} else {
    			this.produtosTarget.remove((Produto)item);
    			this.produtosSource.add((Produto)item);
    			removidos++;
    		}
    	}
    	
    	if (adicionados > 0) {
    		FacesMessage msgAdicionado = new FacesMessage(FacesMessage.SEVERITY_INFO, adicionados + " Produto(s) Adicionado(s).", adicionados + " Produto(s) Adicionado(s).");
    		FacesContext.getCurrentInstance().addMessage(null, msgAdicionado);
    	}
    	
    	if (removidos > 0) {
    		FacesMessage msgRemovidos = new FacesMessage(FacesMessage.SEVERITY_INFO, removidos + " Produto(s) Removido(s).", removidos + " Produto(s) Removido(s).");
    		FacesContext.getCurrentInstance().addMessage(null, msgRemovidos);
    	}
    	
    }

	// Método Chamado na troca de 'Passos'(Steps) do 'Wizard' da página de cadastro de 'Cotacao'(cotacao.xhtml).
    public String cadastroCotacaoOnFlowProcess(FlowEvent event) {
    	// Regras estando no 'Passo 1'
    	if ("step1".equals(event.getOldStep())) {
    		if (produtosTarget.isEmpty()) {
        		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione no mínimo um Produto", "Selecione no mínimo um Produto");
        		FacesContext.getCurrentInstance().addMessage(null, msg);
    			return "step1";
			}
    		
    		for (Produto produto : produtosTarget) {
				if (produto.getFornecedores().isEmpty()) {
	        		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "O Produto " + produto.getNome() + " não possui nenhum Fornecedor.", "O Produto " + produto.getNome() + " não possui nenhum Fornecedor.");
	        		FacesContext.getCurrentInstance().addMessage(null, msg);
				} else {
					ItemCotacao itemCotacao = new ItemCotacao();
					itemCotacao.setProduto(produto);
					itensCotacao.add(itemCotacao);
				}
			}
			if (!FacesContext.getCurrentInstance().getMessageList().isEmpty()) {
				return "step1";
			}
			return event.getNewStep();
		}
    	// Regras estando no 'Passo 2'
    	if ("step2".equals(event.getOldStep())) {
    		// Voltar para 'Passo 1'
    		if ("step1".equals(event.getNewStep())) {
    			itensCotacao.clear();
    			return event.getNewStep();
    		} else { // Segue para 'Passo 2'
    			// Verifica se já existem 'Cotações' com este número.
    			if (!cotacaoBean.listByCodigo(codigoCotacao).isEmpty()) {
    				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Código Cotação já cadastrado", "Código Cotação já cadastrado");
    				FacesContext.getCurrentInstance().addMessage("codigoCotacao", msg);
    				return "step2";
    			}
    			
    			// Verifica as quantidades de cada 'Item Cotação'
    			for (ItemCotacao itemCotacao : itensCotacao) {
    				if (itemCotacao.getQtdProduto() == null || "".equals(itemCotacao.getQtdProduto())) {
    					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "O Item Cotação do Produto " + itemCotacao.getProduto().getNome() + " não possui um valor válido.", "O Item Cotação do Produto " + itemCotacao.getProduto().getNome() + " não possui um valor válido.");
    					FacesContext.getCurrentInstance().addMessage(null, msg);
    					continue;
    				} else if (itemCotacao.getQtdProduto() <= 0.0000) {
    					FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "O Item Cotação do Produto " + itemCotacao.getProduto().getNome() + " não pode possuir valor igual/menor que zero.", "O Item Cotação do Produto " + itemCotacao.getProduto().getNome() + " não pode possuir valor igual/menor que zero.");
    					FacesContext.getCurrentInstance().addMessage(null, msg);
    					continue;
    				}
    				
    				// Verifica se a é um valor válido para Unidade. Se tem casas decimais significativas.
    				if ("un".equalsIgnoreCase(itemCotacao.getProduto().getUnidadeMedida().getSigla())
    						&& itemCotacao.getQtdProduto() - Math.round(itemCotacao.getQtdProduto()) != 0.0) {
						FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "O Produto " + itemCotacao.getProduto().getNome() + " somente aceita valores inteiros.", "O Produto " + itemCotacao.getProduto().getNome() + " somente aceita valores inteiros.");
		    			FacesContext.getCurrentInstance().addMessage(null, msg);
					}
    				
    			}
    			if (!FacesContext.getCurrentInstance().getMessageList().isEmpty()) {
    				return "step2";
    			}
			}
    		
    		return event.getNewStep();
    	}
    	
    	return event.getNewStep();

    }

    // Método chamado no 'onChange' da 'dtInicioValidade'. Para setar o 'minDtFimValidade' >= a 'dtInicioValidade'.
    public void onChangedtInicioValidade() {
    	this.dtFimValidade = null;
    	this.minDtFimValidade = dtInicioValidade;
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
	
	public List<UnidadeMedida> getUnidadesMedida() {
		return unidadeMedidaBean.listAll();
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
	
	public String saveFornecedor() {
		
		// Verifica se email ja esta cadastrado para um 'Fornecedor' ou 'Companhia'.
		if (fornecedorBean.findByEmail(fornecedor.getEmail()).getId() != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Email já cadastrado para um 'Fornecedor'", "CNPJ/CPF já cadastrado para um 'Fornecedor'");
			FacesContext.getCurrentInstance().addMessage("email", msg);
			return null;
		}
		if (companhiaBean.findByEmail(fornecedor.getEmail()).getId() != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Email já cadastrado para uma 'Companhia'", "CNPJ/CPF já cadastrado para uma 'Companhia'");
			FacesContext.getCurrentInstance().addMessage("email", msg);
			return null;
		}

		// Verifica se cnpj ja esta cadastrado para uma 'Fornecedor' ou 'Companhia'.
		if (fornecedorBean.findByCnpjCpf(fornecedor.getCnpjCpf()).getId() != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "CNPJ/CPF já cadastrado para um 'Fornecedor'", "CNPJ/CPF já cadastrado para um 'Fornecedor'");
			FacesContext.getCurrentInstance().addMessage("cnpjCpf", msg);
			return null;
		}
		if (companhiaBean.findByCnpjCpf(fornecedor.getCnpjCpf()).getId() != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "CNPJ/CPF já cadastrado para uma 'Companhia'", "CNPJ/CPF já cadastrado para uma 'Companhia'");
			FacesContext.getCurrentInstance().addMessage("cnpjCpf", msg);
			return null;
		}
		
		// Adiciona informações adicionais de inserção a 'Fornecedor'.
		fornecedor.setUsuarioInsercao(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
		fornecedor.setDtInsercao(new Date());
		
		// Adiciona a 'Fornecedor' ao 'Telefone'.
		telefone.setFornecedor(fornecedor);
		Set<Telefone> telefones = new HashSet<Telefone>();
		telefones.add(telefone);
		fornecedor.setTelefones(telefones);
		
		// Adiciona o Municipio ao 'Endereco', se 'Pais' possuir 'Ufs'.
		// No momento, todos os paises que possuem 'Ufs' possuem 'Municípios'.
		if (paisHasUfs()) {
			// Se 'Pais' possuir 'Ufs'
			endereco.setMunicipio(municipio);
			
			// Adiciona a Fornecedor ao 'Endereco'.
			endereco.setFornecedor(fornecedor);
			Set<Endereco> enderecos = new HashSet<Endereco>();
			enderecos.add(endereco);
			fornecedor.setEnderecos(enderecos);
			
		} else {
			// Se 'Pais' não possuir 'Ufs'
			endereco.setPais(pais);
			
			// Adiciona a Fornecedor ao 'Endereco'.
			endereco.setFornecedor(fornecedor);
			Set<Endereco> enderecos = new HashSet<Endereco>();
			enderecos.add(endereco);
			fornecedor.setEnderecos(enderecos);
		}
		
		
		
		/** Criação de 'Usuário'. **/ 
		
		// Verifica se email ja esta cadastrado para um 'Usuario'.
		if (usuarioBean.findByEmail(fornecedor.getEmail()).getId() != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Email já cadastrado para um 'Usuário'", "Email já cadastrado para um 'Usuário'");
			FacesContext.getCurrentInstance().addMessage("email", msg);
			return null;
		}
		
		// Cria um 'Usuario'(FornecedorUser).
		FornecedorUser fornecedorUser = new FornecedorUser();
		fornecedorUser.setUsuarioInsercao(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
		fornecedorUser.setDtInsercao(new Date());
		fornecedorUser.setRole(Role.FORNECEDOR);
		fornecedorUser.setEmail(fornecedor.getEmail());
		
		// Criando senha inicial aleatoria para o 'Usuario'
		String senhaInicial = new BigInteger(32, new SecureRandom()).toString(32);
		
		fornecedorUser.setSenhaInicial(senhaInicial);
		fornecedorUser.setSenha(Util.createPasswordHash("SHA-256", "BASE64", null, null, senhaInicial));
		fornecedorUser.setSenhasAntigas(Util.createPasswordHash("SHA-256", "BASE64", null, null, senhaInicial));
		fornecedorUser.setQtdTentativas(0);
		fornecedorUser.setStatusUsuario(StatusUsuario.INATIVO);

		// Persiste no BD
		fornecedorBean.save(fornecedor);
		log.info("Fornecedor salvo no BD: " + fornecedor.getEmail());
		
		fornecedorUser.setFornecedor(fornecedor);
		usuarioBean.save(fornecedorUser);
		log.info("Usuário Fornecedor salvo no BD: " + fornecedorUser.getEmail());
		
		
		
		/** Criação do Email de Boas-Vindas com a "Senha Inicial" e "Instruções". **/ 
		
		// Recursos mapeados no 'messages.properties'.
	    ResourceBundle rb = ResourceBundle.getBundle("messages");

	    String msgTitulo = rb.getString("email.fornecedor.titulo");
	    String urlTrocaSenhaInicial = rb.getString("application.host") + "pages/public/senhaInicialChange.xhtml";
	    String urlFornecedorUserAccess = rb.getString("application.host");
	   
	    // Parametros(argumentos) para o 'E-mail'(MessageFormat)
	    Object[] args = { senhaInicial, urlTrocaSenhaInicial, urlFornecedorUserAccess };
	    
	    MessageFormat mf = new MessageFormat(rb.getString("email.fornecedor.message.part0001") + 
				 							 rb.getString("email.fornecedor.message.part0002") +
				 							 rb.getString("email.fornecedor.message.part0003"));
	    
	    // Mensagem formatada com os parametros.
	    String msg = mf.format(args);
		
	    sendEmail(fornecedorUser, msgTitulo, msg);
	    
		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Fornecedor' cadastrado com sucesso.", "'Fornecedor' cadastrado com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo);
		
	    return "principal.xhtml";
	}
	
	public String saveProduto() {
		
		// Verifica se o código do produto ja esta cadastrado.
		if (produtoBean.findByCodigo(String.valueOf(produto.getCodigo())).getId() != null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Código já cadastrado para um 'Produto'", "Código já cadastrado para um 'Produto'");
			FacesContext.getCurrentInstance().addMessage("codigo", msg);
			return null;
		}

		// Adiciona informações adicionais de inserção a 'Produto'.
		produto.setUsuarioInsercao(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
		produto.setDtInsercao(new Date());
		
		// Persiste no BD
		produtoBean.save(produto);
		log.info("Produto salvo no BD: " + produto.getCodigo());

		return "principal.xhtml";
	}

	public String saveCotacao() {
	
		cotacao.setCodigo(codigoCotacao);
		cotacao.setDtInicioValidade(dtInicioValidade);
		cotacao.setDtFimValidade(dtFimValidade);
		cotacao.setStatusCotacao(StatusCotacao.CRIADA);
		
		cotacao.setDtInsercao(new Date());
		cotacao.setUsuarioInsercao(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());

		for (ItemCotacao itemCotacao : itensCotacao) {
			itemCotacao.setCotacao(cotacao);
		}
		Set<ItemCotacao> ics = new HashSet<ItemCotacao>();
		ics.addAll(itensCotacao);
		cotacao.setItensCotacao(ics);
		
		cotacaoBean.save(cotacao);
		log.info("Nova Cotação criada: " + cotacao.getCodigo());

		logCotacaoBean.save(new LogCotacao(cotacao, new Date(), "'Cotação' criada."));

		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Cotação' criada com sucesso.", "'Cotação' criada com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo);
		
		return "principal.xhtml";
	}
	
	public String updateFornecedor() {
		
		// 'Login(E-mail)' do usuário logado.
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();

		// Adiciona informações adicionais de inserção a 'Fornecedor'.
		fornecedor.setUsuarioInsercao(usuarioLogin);
		fornecedor.setDtInsercao(new Date());
		
		// Adiciona a 'Fornecedor' ao 'Telefone'.
		telefone.setFornecedor(fornecedor);
		Set<Telefone> telefones = new HashSet<Telefone>();
		telefones.add(telefone);
		fornecedor.setTelefones(telefones);
		
		// Adiciona 'Telefone'.
		fornecedor.getTelefones().clear();
		fornecedor.getTelefones().add(telefone);
		
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
		fornecedor.getEnderecos().clear();
		fornecedor.getEnderecos().add(endereco);

		// Persiste no BD
		fornecedorLog.setDtDelecao(new Date());
		fornecedorLog.setUsuarioDelecao(usuarioLogin);
		
		fornecedorBean.update(fornecedor);
		log.info("'Fornecedor' atualizado no BD: Com ID " + fornecedor.getId());
		
		fornecedorBean.save(fornecedorLog);
		log.info("'Fornecedor Log' salvo no BD: Com ID " + fornecedorLog.getId());

		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Fornecedor' alterado com sucesso.", "'Fornecedor' alterado com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo);
		
		return "principal.xhtml";
	}

	public String updateProduto() {
		
		// 'Login(E-mail)' do usuário logado.
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		
		// Adiciona informações adicionais de inserção a 'Fornecedor'.
		produto.setUsuarioInsercao(usuarioLogin);
		produto.setDtInsercao(new Date());
		
		// Persiste no BD
		produtoLog.setDtDelecao(new Date());
		produtoLog.setUsuarioDelecao(usuarioLogin);
		
		produtoBean.update(produto);
		log.info("'Produto' atualizado no BD: Com ID " + produto.getId());
		
		produtoBean.save(produtoLog);
		log.info("'Produto Log' salvo no BD: Com ID " + produtoLog.getId());
		
		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Fornecedor' alterado com sucesso.", "'Fornecedor' alterado com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo);
		
		return "principal.xhtml";
	}

	public void excluirFornecedor() {

		// 'Login(E-mail)' do usuário logado.
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		
		// Adiciona informações adicionais de delecao a 'Fornecedor'.
		fornecedorSelected.setUsuarioDelecao(usuarioLogin);
		fornecedorSelected.setDtDelecao(new Date());
		
		// Seleciona o 'Usuario'(FornecedorUser) da 'Fornecedor' para deleção.
		Usuario usuario = usuarioBean.findByEmail(fornecedorSelected.getEmail());
		usuario.setUsuarioDelecao(usuarioLogin);
		usuario.setDtDelecao(new Date());
		
		// Persiste no BD
		fornecedorBean.update(fornecedorSelected);
		log.info("'Fornecedor' excluído logicamente no BD: Com ID " + fornecedorSelected.getId());
		
		usuarioBean.update(usuario);
		log.info("'Usuário'(FornecedorUser) excluído logicamente no BD: Com ID " + usuario.getId());

		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Fornecedor' excluído com sucesso.", "'Fornecedor' excluído com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo);
		
	}
	
	public void excluirProduto() {
		
		// 'Login(E-mail)' do usuário logado.
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		
		// Adiciona informações adicionais de delecao a 'Produto'.
		produtoSelected.setUsuarioDelecao(usuarioLogin);
		produtoSelected.setDtDelecao(new Date());
		
		// Persiste no BD
		produtoBean.update(produtoSelected);
		log.info("'Produto' excluído logicamente no BD: Com ID " + produtoSelected.getId());
		
		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Produto' excluído com sucesso.", "'Produto' excluído com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo);
		
	}

	public void excluirCotacao() {
		
		// 'Login(E-mail)' do usuário logado.
		String usuarioLogin = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
		
		// Adiciona informações adicionais de delecao a 'Cotacão'.
		cotacaoSelected.setUsuarioDelecao(usuarioLogin);
		cotacaoSelected.setDtDelecao(new Date());
		
		// Persiste no BD
		cotacaoBean.update(cotacaoSelected);
		log.info("'Cotacao' excluído logicamente no BD: Com ID " + cotacaoSelected.getId());
		
		logCotacaoBean.save(new LogCotacao(cotacaoSelected, new Date(), "'Cotação' excluída."));
		
		FacesMessage msgInfo = new FacesMessage(FacesMessage.SEVERITY_INFO, "'Cotação' excluída com sucesso.", "'Cotação' excluída com sucesso.");
		FacesContext.getCurrentInstance().addMessage(null, msgInfo);
		
	}

	private void sendEmail(FornecedorUser fornecedorUser, String msgTitulo,
			String msg) {
		// Envia 'Email' de boas vindas com a 'Senha Inicial' e instruções.
		try {
			// 'E-mail' configurado no 'standalone.xml'(mail-session) do Jboss.
			String mailAddress = mailSession.getProperties().getProperty("mail.smtp.user");

		    MimeMessage m = new MimeMessage(mailSession);
		    Address from = new InternetAddress(mailAddress);

		    // TODO - Testes. Trocar linha comentada abaixo quando entrar em 'Produção'.
		    Address[] to = new InternetAddress[] {new InternetAddress(mailAddress) }; // Versão para testes.
//		    Address[] to = new InternetAddress[] {new InternetAddress(fornecedorUser.getEmail()) }; // Versão de 'Produção'

		    m.setFrom(from);
		    m.setRecipients(Message.RecipientType.TO, to);
		    m.setSubject(msgTitulo);
		    m.setSentDate(new Date());
		    m.setContent(msg, "text/html");

		    Transport.send(m);
		    log.info("E-mail de boas-vindas enviado para: " + fornecedorUser.getEmail());
		} catch (MessagingException e) {
			log.error("Não foi possível enviar email para: " + fornecedorUser.getEmail());
		    e.printStackTrace();
		}
	}

	public void exportarCotacao(Cotacao cotacao) {
		// lista com os Itens da Cotação.
		List<ItemCotacao> lista = new ArrayList<ItemCotacao>();
		lista.addAll(cotacao.getItensCotacao());
		
		// Map<String, Object> com os nossos parametros.
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fornecedorNome", cotacao.getFornecedor().getNome());
		parametros.put("cotacaoCodigo", cotacao.getCodigo());
		parametros.put("cotacaoDtFimValidade", new SimpleDateFormat("dd/MM/yyyy").format(cotacao.getDtFimValidade()));
		
		try {
			// compilacao do JRXML
			JasperReport report = JasperCompileManager.compileReport("C:/desenvolvimento/jboss7_eps/standalone/deployments/eps_ear.ear/eps_web.war/relatorios/cotacaoRelatorio.jrxml");
			
			// Preenchimento do relatorio, note que o metodo recebe 3 parametros: 
			// 1 - O relatorio 
			// 
			// 2 - Um Map, com parametros que sao passados ao relatorio no momento do preenchimento.
			//     No nosso caso eh 'parametros' mas pode-se passar NULL.
			// 
			// 3 - O data source. Note que nao devemos passar a lista diretamente, 
			// e sim "transformar" em um data source utilizando a classe JRBeanCollectionDataSource
			JasperPrint print = JasperFillManager.fillReport(report, parametros, new JRBeanCollectionDataSource(lista));
			
			// Exportacao do relatorio para outro formato, no caso PDF.
			byte[] b = JasperExportManager.exportReportToPdf(print);
			
			HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            //Código abaixo gerar o relatório e disponibiliza diretamente na página. Obs.: O commandButton precisa estar com 'ajax="false"'.
//            res.setHeader("Content-disposition", "inline;filename=cotacao" + cotacao.getCodigo() + cotacao.getFornecedor().getCnpjCpf() + ".pdf");
            //Código abaixo gerar o relatório e disponibiliza para o cliente baixar ou salvar. Obs.: O commandButton precisa estar com 'ajax="false"'.
            res.setHeader("Content-disposition", "attachment;filename=cotacao" + cotacao.getCodigo() + cotacao.getFornecedor().getCnpjCpf() + ".pdf");
            res.getOutputStream().write(b);
            res.getCharacterEncoding();
            FacesContext.getCurrentInstance().responseComplete();
			
			log.info("Cotação disponibilizada para baixar em PDF.");
            
		} catch (JRException e) {
			log.error("Erro ao tentar gerar Cotação em PDF.", e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Erro de IO ao tentar gerar Cotação em PDF.", e);
			e.printStackTrace();
		}
		
	}
	
	public void paisChanged() {  
        this.uf = new Uf();
    }  

	public boolean paisHasUfs() {
		return ufBean.countByPais(pais) != 0 ? true : false;
	}
	
	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
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

    public LazyDataModel<Fornecedor> getFornecedoresLazy() {
        return fornecedoresLazy;
    }

    public void setFornecedoresLazy(LazyDataModel<Fornecedor> fornecedoresLazy) {
        this.fornecedoresLazy = fornecedoresLazy;
    }
    
    public LazyDataModel<Produto> getProdutosLazy() {
    	return produtosLazy;
    }
    
    public void setProdutosLazy(LazyDataModel<Produto> produtosLazy) {
    	this.produtosLazy = produtosLazy;
    }
    
    public LazyDataModel<Cotacao> getCotacoesLazy() {
    	return cotacoesLazy;
    }
    
    public void setCotacoesLazy(LazyDataModel<Cotacao> cotacoesLazy) {
    	this.cotacoesLazy = cotacoesLazy;
    }
    
	public Fornecedor getFornecedorLog() {
		return fornecedorLog;
	}

	public void setFornecedorLog(Fornecedor fornecedorLog) {
		this.fornecedorLog = fornecedorLog;
	}
	
	public Produto getProdutoLog() {
		return produtoLog;
	}
	
	public void setProdutoLog(Produto produtoLog) {
		this.produtoLog = produtoLog;
	}

	public Fornecedor getFornecedorSelected() {
		return fornecedorSelected;
	}

	public void setFornecedorSelected(Fornecedor fornecedorSelected) {
		this.fornecedorSelected = fornecedorSelected;
	}
	
	public Produto getProdutoSelected() {
		return produtoSelected;
	}
	
	public void setProdutoSelected(Produto produtoSelected) {
		this.produtoSelected = produtoSelected;
	}
	
	public Cotacao getCotacaoSelected() {
		return cotacaoSelected;
	}
	
	public void setCotacaoSelected(Cotacao cotacaoSelected) {
		this.cotacaoSelected = cotacaoSelected;
	}
	
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getProdutosSource() {
		return produtosSource;
	}

	public void setProdutosSource(List<Produto> produtosSource) {
		this.produtosSource = produtosSource;
	}

	public List<Produto> getProdutosTarget() {
		return produtosTarget;
	}

	public void setProdutosTarget(List<Produto> produtosTarget) {
		this.produtosTarget = produtosTarget;
	}

	public DualListModel<Produto> getProdutosDualListModel() {
		return produtosDualListModel;
	}

	public void setProdutosDualListModel(DualListModel<Produto> produtosDualListModel) {
		this.produtosDualListModel = produtosDualListModel;
	}

	public String getCodigoCotacao() {
		return codigoCotacao;
	}

	public void setCodigoCotacao(String codigoCotacao) {
		this.codigoCotacao = codigoCotacao;
	}

	public Date getDtInicioValidade() {
		return dtInicioValidade;
	}

	public void setDtInicioValidade(Date dtInicioValidade) {
		this.dtInicioValidade = dtInicioValidade;
	}

	public Date getDtFimValidade() {
		return dtFimValidade;
	}

	public void setDtFimValidade(Date dtFimValidade) {
		this.dtFimValidade = dtFimValidade;
	}

	public List<ItemCotacao> getItensCotacao() {
		return itensCotacao;
	}

	public void setItensCotacao(List<ItemCotacao> itensCotacao) {
		this.itensCotacao = itensCotacao;
	}

	public Date getMinDtInicioValidade() {
		return minDtInicioValidade;
	}

	public void setMinDtInicioValidade(Date minDtInicioValidade) {
		this.minDtInicioValidade = minDtInicioValidade;
	}
	
	public Date getMinDtFimValidade() {
		return minDtFimValidade;
	}
	
	public void setMinDtFimValidade(Date minDtFimValidade) {
		this.minDtFimValidade = minDtFimValidade;
	}
    
	public boolean isEditFornecedor() {
		return editFornecedor;
	}

	public void setEditFornecedor(boolean editFornecedor) {
		this.editFornecedor = editFornecedor;
	}
	
	public boolean isEditProduto() {
		return editProduto;
	}

	public void setEditProduto(boolean editProduto) {
		this.editProduto = editProduto;
	}

}