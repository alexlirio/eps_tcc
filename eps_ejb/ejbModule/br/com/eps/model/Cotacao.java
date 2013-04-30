package br.com.eps.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
	@NamedQuery(name="Cotacao.findAllPersist", query="SELECT c FROM Cotacao c WHERE c.dtDelecao IS NULL"),
	@NamedQuery(name="Cotacao.findByCodigo", query="SELECT c FROM Cotacao c WHERE c.codigo = :codigo AND c.dtDelecao IS NULL"),
	@NamedQuery(name="Cotacao.findByFornecedor", query="SELECT c FROM Cotacao c WHERE c.fornecedor = :fornecedor AND c.dtDelecao IS NULL"),
	@NamedQuery(name="Cotacao.findInicializaveis", query="SELECT c FROM Cotacao c WHERE c.dtInicioValidade <= current_date AND c.statusCotacao = br.com.eps.model.StatusCotacao.CRIADA AND c.dtDelecao IS NULL"),
	@NamedQuery(name="Cotacao.findFinalizaveis", query="SELECT c FROM Cotacao c WHERE c.dtFimValidade <= current_date AND c.statusCotacao = br.com.eps.model.StatusCotacao.INICIADA AND c.dtDelecao IS NULL")
})
public class Cotacao extends Persistente {

	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL_PERSIST = "Cotacao.findAllPersist";
	public static final String FIND_BY_CODIGO = "Cotacao.findByCodigo";
	public static final String FIND_BY_FORNECEDOR = "Cotacao.findByFornecedor";
	public static final String FIND_INICIALIZAVEIS = "Cotacao.findInicializaveis";
	public static final String FIND_FINALIZAVEIS = "Cotacao.findFinalizaveis";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="codigo", length=20, nullable=true)
	private String codigo;
	
	@ManyToOne
	@JoinColumn(name="fornecedor_id")
	private Fornecedor fornecedor;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtInicioValidade;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtFimValidade;

	@OneToMany(mappedBy="cotacao", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<ItemCotacao> itensCotacao;
	
	@OneToMany(mappedBy="cotacao")
	private Set<LogCotacao> logsCotacao;
	
	@Enumerated(EnumType.STRING)
	private StatusCotacao statusCotacao;

	public boolean isExcluivel() {
		return this.statusCotacao.equals(StatusCotacao.CRIADA);
	}
	
	public boolean isExportavel() {
		return (this.statusCotacao.equals(StatusCotacao.INICIADA) && this.fornecedor != null);
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
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
	public Set<ItemCotacao> getItensCotacao() {
		return itensCotacao;
	}
	public void setItensCotacao(Set<ItemCotacao> itensCotacao) {
		this.itensCotacao = itensCotacao;
	}
	public Set<LogCotacao> getLogsCotacao() {
		return logsCotacao;
	}
	public void setLogsCotacao(Set<LogCotacao> logsCotacao) {
		this.logsCotacao = logsCotacao;
	}
	public StatusCotacao getStatusCotacao() {
		return statusCotacao;
	}
	public void setStatusCotacao(StatusCotacao statusCotacao) {
		this.statusCotacao = statusCotacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((fornecedor == null) ? 0 : fornecedor.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cotacao other = (Cotacao) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (fornecedor == null) {
			if (other.fornecedor != null)
				return false;
		} else if (!fornecedor.equals(other.fornecedor))
			return false;
		return true;
	}
	
}
 
