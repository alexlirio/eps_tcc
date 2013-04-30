package br.com.eps.model;

public enum StatusUsuario {
	INATIVO("Inativo"),
	ATIVO("Ativo"),
	BLOQUEADO("Bloqueado"),
	;
	
	private final String label;
	
	private StatusUsuario(String label) {
		this.label = label;
	}
	
    public String getLabel() {  
    	return this.label;
    } 

}
 
