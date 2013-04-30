package br.com.eps.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorColumn(name="user_type", discriminatorType=DiscriminatorType.STRING, length=3)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("u")
@NamedQueries({
	@NamedQuery(name="Usuario.findByEmail", query="SELECT u FROM Usuario u WHERE u.email = :email AND u.dtDelecao IS NULL"),
	@NamedQuery(name="Usuario.findByEmailSenha", query="SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha AND u.dtDelecao IS NULL")
})
public class Usuario extends Persistente {

	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BY_EMAIL = "Usuario.findByEmail";
	public static final String FIND_BY_EMAIL_SENHA = "Usuario.findByEmailSenha";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="email", length=100, nullable=false)
	private String email;
	
	@Column(name="senha", length=50, nullable=false)
	private String senha;
	
	@Column(name="senhas_antigas", length=200)
	private String senhasAntigas;
	
	@Column(name="senha_inicial", length=50, nullable=false)
	private String senhaInicial;
	
	@Column(name="qtd_tentativas", length=1)
	private Integer qtdTentativas;

	@Enumerated(EnumType.STRING)
    private Role role;
	
	@Enumerated(EnumType.STRING)
	private StatusUsuario statusUsuario;
	
	public boolean isSuperUser() {
		return this instanceof SuperUser ? true : false;
	}
	
	public boolean isCompanhiaUser() {
		return this instanceof CompanhiaUser ? true : false;
	}
	
	public boolean isFornecedorUser() {
		return this instanceof FornecedorUser ? true : false;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getSenhasAntigas() {
		return senhasAntigas;
	}
	public void setSenhasAntigas(String senhasAntigas) {
		this.senhasAntigas = senhasAntigas;
	}
	public String getSenhaInicial() {
		return senhaInicial;
	}
	public void setSenhaInicial(String senhaInicial) {
		this.senhaInicial = senhaInicial;
	}
	public Integer getQtdTentativas() {
		return qtdTentativas;
	}
	public void setQtdTentativas(Integer qtdTentativas) {
		this.qtdTentativas = qtdTentativas;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public StatusUsuario getStatusUsuario() {
		return statusUsuario;
	}
	public void setStatusUsuario(StatusUsuario statusUsuario) {
		this.statusUsuario = statusUsuario;
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
		if(obj instanceof Usuario){
			Usuario usuario = (Usuario) obj;
			return usuario.getEmail().equals(getEmail());
		}
		
		return false;
	}

}
 
