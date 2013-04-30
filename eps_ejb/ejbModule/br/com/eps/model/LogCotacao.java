package br.com.eps.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="log_cotacao")
public class LogCotacao implements Serializable {

	private static final long serialVersionUID = 1L;

	public LogCotacao() {
		super();
	}
 
	public LogCotacao(Cotacao cotacao, Date data, String descricao) {
		super();
		this.cotacao = cotacao;
		this.dtInsercao = data;
		this.descricao = descricao;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name="cotacao_id")
	private Cotacao cotacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtInsercao;
	
	@Column(name="descricao", length=240, nullable=false)
	private String descricao;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Cotacao getCotacao() {
		return cotacao;
	}
	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}
	public Date getDtInsercao() {
		return dtInsercao;
	}
	public void setDtInsercao(Date dtInsercao) {
		this.dtInsercao = dtInsercao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
 
