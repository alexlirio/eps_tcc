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
	@NamedQuery(name="Companhia.findByEmail", query="SELECT c FROM Companhia c WHERE c.email = :email AND c.dtDelecao IS NULL"),
	@NamedQuery(name="Companhia.findByCnpjCpf", query="SELECT c FROM Companhia c WHERE c.cnpjCpf = :cnpjCpf AND c.dtDelecao IS NULL")
})
public class Companhia extends Persistente {

	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BY_EMAIL = "Companhia.findByEmail";
	public static final String FIND_BY_CNPJ_CPF = "Companhia.findByCnpjCpf";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="cnpj_cpf", length=18, nullable=false)
	private String cnpjCpf;
	
	@Column(name="email", length=100, nullable=false)
	private String email;
	
	@Column(name="nome", length=100, nullable=false)
	private String nome;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="companhia", fetch=FetchType.EAGER)
	private Set<Telefone> telefones;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="companhia", fetch=FetchType.EAGER)
	private Set<Endereco> enderecos;
	
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
	public Set<Telefone> getTelefones() {
		return telefones;
	}
	public void setTelefones(Set<Telefone> telefones) {
		this.telefones = telefones;
	}
	public Set<Endereco> getEnderecos() {
		return enderecos;
	}
	public void setEnderecos(Set<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Companhia){
			Companhia companhia = (Companhia) obj;
			return companhia.getEmail().equals(getEmail());
		}
		
		return false;
	}
	
}
 
