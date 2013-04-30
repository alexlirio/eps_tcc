package br.com.eps.model;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-02-23T16:13:28.377-0300")
@StaticMetamodel(ItemCotacao.class)
public class ItemCotacao_ {
	public static volatile SingularAttribute<ItemCotacao, Long> id;
	public static volatile SingularAttribute<ItemCotacao, Produto> produto;
	public static volatile SingularAttribute<ItemCotacao, Cotacao> cotacao;
	public static volatile SingularAttribute<ItemCotacao, Double> qtdProduto;
	public static volatile SingularAttribute<ItemCotacao, BigDecimal> valorTotal;
}
