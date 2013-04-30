package br.com.eps.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-02-12T12:27:50.524-0200")
@StaticMetamodel(Endereco.class)
public class Endereco_ {
	public static volatile SingularAttribute<Endereco, Long> id;
	public static volatile SingularAttribute<Endereco, TipoEndereco> tipoEndereco;
	public static volatile SingularAttribute<Endereco, String> logradouro;
	public static volatile SingularAttribute<Endereco, String> numero;
	public static volatile SingularAttribute<Endereco, String> complemento;
	public static volatile SingularAttribute<Endereco, String> bairro;
	public static volatile SingularAttribute<Endereco, String> cep;
	public static volatile SingularAttribute<Endereco, Municipio> municipio;
	public static volatile SingularAttribute<Endereco, Fornecedor> fornecedor;
	public static volatile SingularAttribute<Endereco, Companhia> companhia;
	public static volatile SingularAttribute<Endereco, Pais> pais;
}
