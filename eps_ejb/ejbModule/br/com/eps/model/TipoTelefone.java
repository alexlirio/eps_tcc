package br.com.eps.model;

public enum TipoTelefone {
	COMERCIAL("Comercial"),
	RESIDENCIAL("Residencial"),
	CELULAR("Celular"),
	FAX("Fax"),
	;
	
	private final String label;
	
	private TipoTelefone(String label) {
		this.label = label;
	}
	
    public String getLabel() {  
    	return this.label;
    } 

}
 
