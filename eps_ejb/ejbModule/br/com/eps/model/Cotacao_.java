package br.com.eps.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-02-25T23:33:56.783-0300")
@StaticMetamodel(Cotacao.class)
public class Cotacao_ extends Persistente_ {
	public static volatile SingularAttribute<Cotacao, Long> id;
	public static volatile SingularAttribute<Cotacao, String> codigo;
	public static volatile SingularAttribute<Cotacao, Fornecedor> fornecedor;
	public static volatile SingularAttribute<Cotacao, Date> dtInicioValidade;
	public static volatile SingularAttribute<Cotacao, Date> dtFimValidade;
	public static volatile SetAttribute<Cotacao, ItemCotacao> itensCotacao;
	public static volatile SetAttribute<Cotacao, LogCotacao> logsCotacao;
	public static volatile SingularAttribute<Cotacao, StatusCotacao> statusCotacao;
}
