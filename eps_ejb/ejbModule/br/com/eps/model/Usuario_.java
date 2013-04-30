package br.com.eps.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2013-02-13T00:55:10.870-0200")
@StaticMetamodel(Usuario.class)
public class Usuario_ extends Persistente_ {
	public static volatile SingularAttribute<Usuario, Long> id;
	public static volatile SingularAttribute<Usuario, String> email;
	public static volatile SingularAttribute<Usuario, String> senha;
	public static volatile SingularAttribute<Usuario, String> senhasAntigas;
	public static volatile SingularAttribute<Usuario, String> senhaInicial;
	public static volatile SingularAttribute<Usuario, Integer> qtdTentativas;
	public static volatile SingularAttribute<Usuario, Role> role;
	public static volatile SingularAttribute<Usuario, StatusUsuario> statusUsuario;
}
