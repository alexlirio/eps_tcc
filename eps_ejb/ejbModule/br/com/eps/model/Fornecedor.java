package br.com.eps.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
	@NamedQuery(name="Fornecedor.findAllPersist", query="SELECT f FROM Fornecedor f WHERE f.dtDelecao IS NULL"),
	@NamedQuery(name="Fornecedor.findByEmail", query="SELECT f FROM Fornecedor f WHERE f.email = :email AND f.dtDelecao IS NULL"),
	@NamedQuery(name="Fornecedor.findByCnpjCpf", query="SELECT f FROM Fornecedor f WHERE f.cnpjCpf = :cnpjCpf AND f.dtDelecao IS NULL"),
	@NamedQuery(name="Fornecedor.findByProduto", query="SELECT f FROM Produto p JOIN p.fornecedores f WHERE p = :produto AND p.dtDelecao IS NULL AND f.dtDelecao IS NULL"),
	@NamedQuery(name="Fornecedor.findByNome", query="SELECT f FROM Fornecedor f WHERE f.nome = :nome AND f.dtDelecao IS NULL"),
})
public class Fornecedor extends Persistente {

	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ALL_PERSIST = "Fornecedor.findAllPersist";
	public static final String FIND_BY_EMAIL = "Fornecedor.findByEmail";
	public static final String FIND_BY_CNPJ_CPF = "Fornecedor.findByCnpjCpf";
	public static final String FIND_BY_PRODUTO = "Fornecedor.findByProduto";
	public static final String FIND_BY_NOME = "Fornecedor.findByNome";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="cnpjCpf", length=18, nullable=false)
	private String cnpjCpf;
	
	@Column(name="nome", length=150, nullable=false)
	private String nome;
	
	@Column(name="email", length=100, nullable=false)
	private String email;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="fornecedor", fetch=FetchType.EAGER)
	private Set<Endereco> enderecos;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="fornecedor", fetch=FetchType.EAGER)
	private Set<Telefone> telefones;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCnpjCpf() {
		return cnpjCpf;
	}
	public void setCnpjCpf(String cnpjCpf) {
		this.cnpjCpf = cnpjCpf;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<Endereco> getEnderecos() {
		return enderecos;
	}
	public void setEnderecos(Set<Endereco> enderecos) {
		this.enderecos = enderecos;
	}
	public Set<Telefone> getTelefones() {
		return telefones;
	}
	public void setTelefones(Set<Telefone> telefones) {
		this.telefones = telefones;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cnpjCpf == null) ? 0 : cnpjCpf.hashCode());
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
		Fornecedor other = (Fornecedor) obj;
		if (cnpjCpf == null) {
			if (other.cnpjCpf != null)
				return false;
		} else if (!cnpjCpf.equals(other.cnpjCpf))
			return false;
		return true;
	}
	
}
 
