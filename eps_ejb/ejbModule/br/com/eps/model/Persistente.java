package br.com.eps.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class Persistente implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtInsercao;
	
	@Column(name="usuario_insercao", length=50)
	private String usuarioInsercao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtDelecao;
	
	@Column(name="usuario_delecao", length=50)
	private String usuarioDelecao;

	public Date getDtInsercao() {
		return dtInsercao;
	}
	public void setDtInsercao(Date dtInsercao) {
		this.dtInsercao = dtInsercao;
	}
	public String getUsuarioInsercao() {
		return usuarioInsercao;
	}
	public void setUsuarioInsercao(String usuarioInsercao) {
		this.usuarioInsercao = usuarioInsercao;
	}
	public Date getDtDelecao() {
		return dtDelecao;
	}
	public void setDtDelecao(Date dtDelecao) {
		this.dtDelecao = dtDelecao;
	}
	public String getUsuarioDelecao() {
		return usuarioDelecao;
	}
	public void setUsuarioDelecao(String usuarioDelecao) {
		this.usuarioDelecao = usuarioDelecao;
	}

}
 
