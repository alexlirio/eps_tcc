function mascaraUtil(o,f){
    v_obj=o;
    v_fun=f;
    setTimeout('execmascara()',1);
}

function execmascara(){
    v_obj.value=v_fun(v_obj.value);
}

function cpfCnpj(v){
    v=v.replace(/\D/g,"");
    if (v.length < 14) { //CPF
        v=v.replace(/(\d{3})(\d)/,"$1.$2");
        v=v.replace(/(\d{3})(\d)/,"$1.$2");
        v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2");
    } else { //CNPJ
        v=v.replace(/^(\d{2})(\d)/,"$1.$2");
        v=v.replace(/^(\d{2})\.(\d{3})(\d)/,"$1.$2.$3");
        v=v.replace(/\.(\d{3})(\d)/,".$1/$2");
        v=v.replace(/(\d{4})(\d)/,"$1-$2");
    }
    return v;
}

function tel(v){
	v=v.replace(/\D/g,"");
	if (v.length < 10) {
		v=v.replace(/(\d{2})(\d)/,"($1) $2");
	} else if (v.length == 10) { //Telefone com mascara, DDD e 8 digitos.
		v=v.replace(/(\d{2})(\d{4})(\d{4})/,"($1) $2-$3");
	} else { //Telefone com mascara, DDD e 9 digitos.
		v=v.replace(/(\d{2})(\d{5})(\d{4})/,"($1) $2-$3");
	}
	return v;
}

function cpf(v){
    v=v.replace(/\D/g,"");
    v=v.replace(/(\d{3})(\d)/,"$1.$2");
    v=v.replace(/(\d{3})(\d)/,"$1.$2");
    v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2");

    return v;
}

function cnpj(v){
    v=v.replace(/^(\d{2})(\d)/,"$1.$2");
    v=v.replace(/^(\d{2})\.(\d{3})(\d)/,"$1.$2.$3");
    v=v.replace(/\.(\d{3})(\d)/,".$1/$2");
    v=v.replace(/(\d{4})(\d)/,"$1-$2");
    
    return v;
}
    
function numbers(v){
	v=v.replace(/\D/g,"");
    	
   	return v;
}

function numbersAndLetters(v){
	v=v.replace(/\W/g,"");
	
	return v;
}

function letters(v){
	v=v.replace(/[^a-zA-Z]/g,"");
	
	return v;
}

function numbersFloatPointFour(v){
	
	f = 4;
	
	v=v.replace(/[^\d,]/g,"")
	   .replace(/,/, "x")
	   .replace(/,/g, "")
	   .replace(/x/, ",");

	if(v.indexOf(',') != -1 && v.indexOf(',') + f + 2 <= v.length) {
		v=v.substring(0, v.indexOf(',') + f + 1);
	}

	return v;
}

function numbersFloatPointTwo(v){
	
	f = 2;
	
	v=v.replace(/[^\d,]/g,"")
	.replace(/,/, "x")
	.replace(/,/g, "")
	.replace(/x/, ",");
	
	if(v.indexOf(',') != -1 && v.indexOf(',') + f + 2 <= v.length) {
		v=v.substring(0, v.indexOf(',') + f + 1);
	}
	
	return v;
}

// Função para limpar campo fora do 'range'.
function validateLength(o,min,max) {
	if (o.value.length < min || o.value.length > max) {
		document.getElementById(o.id).value = "";
	} 
}
