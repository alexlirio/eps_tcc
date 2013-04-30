package br.com.eps.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2012-12-21T22:34:03.139-0200")
@StaticMetamodel(Telefone.class)
public class Telefone_ {
	public static volatile SingularAttribute<Telefone, Long> id;
	public static volatile SingularAttribute<Telefone, String> numero;
	public static volatile SingularAttribute<Telefone, TipoTelefone> tipoTelefone;
	public static volatile SingularAttribute<Telefone, Fornecedor> fornecedor;
	public static volatile SingularAttribute<Telefone, Companhia> companhia;
}
