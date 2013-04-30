package br.com.eps.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-02-25T15:05:44.331-0300")
@StaticMetamodel(Produto.class)
public class Produto_ extends Persistente_ {
	public static volatile SingularAttribute<Produto, Long> id;
	public static volatile SingularAttribute<Produto, String> codigo;
	public static volatile SingularAttribute<Produto, String> nome;
	public static volatile SingularAttribute<Produto, String> descricao;
	public static volatile SingularAttribute<Produto, UnidadeMedida> unidadeMedida;
	public static volatile SetAttribute<Produto, Fornecedor> fornecedores;
}
