package br.com.eps.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="Endereco.findByCompanhia", query="SELECT e FROM Endereco e WHERE e.companhia = :companhia ORDER BY e.id"),
	@NamedQuery(name="Endereco.findByFornecedor", query="SELECT e FROM Endereco e WHERE e.fornecedor = :fornecedor ORDER BY e.id"),
})
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_COMPANHIA = "Endereco.findByCompanhia";
	public static final String FIND_BY_FORNECEDOR = "Endereco.findByFornecedor";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private TipoEndereco tipoEndereco;
	
	@Column(name="logradouro", length=150)
	private String logradouro;
	
	@Column(name="numero", length=10)
	private String numero;
	
	@Column(name="complemento", length=50)
	private String complemento;
	
	@Column(name="bairro", length=50)
	private String bairro;
	
	@Column(name="cep", length=9)
	private String cep;
	
	@ManyToOne
	@JoinColumn(name="municipio_id")
	private Municipio municipio;
	
	@ManyToOne
    @JoinColumn(name="fornecedor_id")
	private Fornecedor fornecedor;
	
	@ManyToOne
    @JoinColumn(name="companhia_id")
	private Companhia companhia;

	@ManyToOne
	@JoinColumn(name="pais_id")
	private Pais pais;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}
	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public Municipio getMunicipio() {
		return municipio;
	}
	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	public Companhia getCompanhia() {
		return companhia;
	}
	public void setCompanhia(Companhia companhia) {
		this.companhia = companhia;
	}
	public Pais getPais() {
		return pais;
	}
	public void setPais(Pais pais) {
		this.pais = pais;
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endereco other = (Endereco) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
 
