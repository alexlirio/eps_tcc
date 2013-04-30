package br.com.eps.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-02-25T12:17:59.983-0300")
@StaticMetamodel(Fornecedor.class)
public class Fornecedor_ extends Persistente_ {
	public static volatile SingularAttribute<Fornecedor, Long> id;
	public static volatile SingularAttribute<Fornecedor, String> cnpjCpf;
	public static volatile SingularAttribute<Fornecedor, String> nome;
	public static volatile SingularAttribute<Fornecedor, String> email;
	public static volatile SetAttribute<Fornecedor, Endereco> enderecos;
	public static volatile SetAttribute<Fornecedor, Telefone> telefones;
}
