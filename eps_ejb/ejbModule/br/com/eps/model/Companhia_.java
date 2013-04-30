package br.com.eps.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-02-25T12:02:34.793-0300")
@StaticMetamodel(Companhia.class)
public class Companhia_ extends Persistente_ {
	public static volatile SingularAttribute<Companhia, Long> id;
	public static volatile SingularAttribute<Companhia, String> cnpjCpf;
	public static volatile SingularAttribute<Companhia, String> email;
	public static volatile SingularAttribute<Companhia, String> nome;
	public static volatile SetAttribute<Companhia, Telefone> telefones;
	public static volatile SetAttribute<Companhia, Endereco> enderecos;
}
