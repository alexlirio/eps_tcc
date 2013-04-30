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
	@NamedQuery(name="Telefone.findByCompanhia", query="SELECT t FROM Telefone t WHERE t.companhia = :companhia ORDER BY t.id"),
	@NamedQuery(name="Telefone.findByFornecedor", query="SELECT t FROM Telefone t WHERE t.fornecedor = :fornecedor ORDER BY t.id"),
})
public class Telefone implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_COMPANHIA = "Telefone.findByCompanhia";
	public static final String FIND_BY_FORNECEDOR = "Telefone.findByFornecedor";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="numero", length=15, nullable=false)
	private String numero;
	
	@Enumerated(EnumType.STRING)
	private TipoTelefone tipoTelefone;

	@ManyToOne
    @JoinColumn(name="fornecedor_id")
	private Fornecedor fornecedor;
	
	@ManyToOne
    @JoinColumn(name="companhia_id")
	private Companhia companhia;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public TipoTelefone getTipoTelefone() {
		return tipoTelefone;
	}
	public void setTipoTelefone(TipoTelefone tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
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
		Telefone other = (Telefone) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
 
