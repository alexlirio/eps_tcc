package br.com.eps.model;

public enum TipoEndereco {
	COMERCIAL("Comercial"),
	RESIDENCIAL("Residencial"),
	;
	
	private final String label;
	
	private TipoEndereco(String label) {
		this.label = label;
	}
	
    public String getLabel() {  
    	return this.label;
    } 

}
