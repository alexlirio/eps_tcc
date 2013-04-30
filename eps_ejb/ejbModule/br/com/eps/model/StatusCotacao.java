package br.com.eps.model;

public enum StatusCotacao {
	CRIADA("Criada"),
	INICIADA("Iniciada"),
	FINALIZADA("Finalizada"),
	;
	
	private final String label;
	
	private StatusCotacao(String label) {
		this.label = label;
	}
	
    public String getLabel() {  
    	return this.label;
    } 

}
 
