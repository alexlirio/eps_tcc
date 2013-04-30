package br.com.eps.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-02-24T13:39:39.215-0300")
@StaticMetamodel(LogCotacao.class)
public class LogCotacao_ {
	public static volatile SingularAttribute<LogCotacao, Long> id;
	public static volatile SingularAttribute<LogCotacao, Cotacao> cotacao;
	public static volatile SingularAttribute<LogCotacao, Date> dtInsercao;
	public static volatile SingularAttribute<LogCotacao, String> descricao;
}
