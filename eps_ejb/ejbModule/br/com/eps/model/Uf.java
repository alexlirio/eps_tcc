package br.com.eps.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="Uf.findByPais", query="SELECT u FROM Uf u WHERE u.pais = :pais ORDER BY u.nome"),
	@NamedQuery(name="Uf.countByPais", query="SELECT COUNT(u) FROM Uf u WHERE u.pais = :pais")
})
public class Uf implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BY_PAIS = "Uf.findByPais";
	public static final String COUNT_BY_PAIS = "Uf.countByPais";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="codigo", length=10, nullable=false)
	private String codigo;
	
	@Column(name="nome", length=50, nullable=false)
	private String nome;
	
	@ManyToOne
	@JoinColumn(name="pais_id")
	private Pais pais;
	
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
		Uf other = (Uf) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
 
