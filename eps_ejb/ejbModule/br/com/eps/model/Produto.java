package br.com.eps.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="Produto.findAllPersist", query="SELECT p FROM Produto p WHERE p.dtDelecao IS NULL"),
	@NamedQuery(name="Produto.findByCodigo", query="SELECT p FROM Produto p WHERE p.codigo = :codigo AND p.dtDelecao IS NULL"),
	@NamedQuery(name="Produto.findByFornecedor", query="SELECT p FROM Produto p JOIN p.fornecedores pf WHERE pf.dtDelecao IS NULL AND pf = :fornecedor"),
	@NamedQuery(name="Produto.findByCotacao", query="SELECT p FROM ItemCotacao it JOIN it.produto p WHERE it.cotacao = :cotacao AND p.dtDelecao IS NULL")
})
public class Produto extends Persistente {

	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL_PERSIST = "Produto.findAllPersist";
	public static final String FIND_BY_CODIGO = "Produto.findByCodigo";
	public static final String FIND_BY_FORNECEDOR = "Produto.findByFornecedor";
	public static final String FIND_BY_COTACAO = "Produto.findByCotacao";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="codigo", length=20, nullable=false)
	private String codigo;
	
	@Column(name="nome", length=50, nullable=false)
	private String nome;
	
	@Column(name="descricao", length=240, nullable=false)
	private String descricao;
	
	@ManyToOne
	@JoinColumn(name="unidade_medida_id")
	private UnidadeMedida unidadeMedida;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="produto_fornecedor",
		joinColumns=@JoinColumn(name="produto_id"),
		inverseJoinColumns=@JoinColumn(name="fornecedor_id"))
	private Set<Fornecedor> fornecedores;
	
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}
	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}
	public Set<Fornecedor> getFornecedores() {
		return fornecedores;
	}
	public void setFornecedores(Set<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Produto other = (Produto) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
 
