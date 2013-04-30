package br.com.eps.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="item_cotacao")
@NamedQuery(name="ItemCotacao.findByCotacao", query="SELECT ic FROM ItemCotacao ic WHERE ic.cotacao = :cotacao")
public class ItemCotacao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String FIND_BY_COTACAO = "ItemCotacao.findByCotacao";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="produto_id")
	private Produto produto;
	
	@ManyToOne
	@JoinColumn(name="cotacao_id")
	private Cotacao cotacao;
	
	@Column(name="qtd_produto", nullable=false, precision=10, scale=4)
	private Double qtdProduto;
	
	@Column(name="valor_total", precision=10, scale=4)
	private BigDecimal valorTotal;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	public Cotacao getCotacao() {
		return cotacao;
	}
	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}
	public Double getQtdProduto() {
		return qtdProduto;
	}
	public void setQtdProduto(Double qtdProduto) {
		this.qtdProduto = qtdProduto;
	}
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cotacao == null) ? 0 : cotacao.hashCode());
		result = prime * result + ((cotacao == null) ? 0 : produto.hashCode());
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
		ItemCotacao other = (ItemCotacao) obj;
		if (cotacao == null) {
			if (other.cotacao != null)
				return false;
		} else if (!cotacao.equals(other.cotacao))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}
	
}
 
