package br.com.eps.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-01-18T23:07:59.555-0200")
@StaticMetamodel(Municipio.class)
public class Municipio_ {
	public static volatile SingularAttribute<Municipio, Long> id;
	public static volatile SingularAttribute<Municipio, String> codigo;
	public static volatile SingularAttribute<Municipio, String> nome;
	public static volatile SingularAttribute<Municipio, Uf> uf;
}
