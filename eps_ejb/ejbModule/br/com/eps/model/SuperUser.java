package br.com.eps.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("su")
public class SuperUser extends Usuario {

	private static final long serialVersionUID = 1L;


}