package br.com.eps.primefaces.lazydatamodel;

import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.eps.bean.FornecedorBean;
import br.com.eps.model.Fornecedor;

public class FornecedorLazyList extends LazyDataModel<Fornecedor> {

	private static final long serialVersionUID = 1L;

	Logger log = Logger.getLogger(FornecedorLazyList.class);

    private List<Fornecedor> fornecedores;

    @Override
   	public List<Fornecedor> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
        String ordernacao = sortOrder.toString();

        if(SortOrder.UNSORTED.equals(sortOrder)){
            ordernacao = SortOrder.ASCENDING.toString();
        }
        
		try {
	        Context initialContext = new InitialContext();
	        FornecedorBean fornecedorBean = (FornecedorBean) initialContext.lookup("java:global/eps_ear/eps_ejb/FornecedorBean");
	        fornecedores = fornecedorBean.buscaPorPaginacao(first, pageSize, sortField, ordernacao, filters);
	        
	        // total encontrado no banco de dados, caso o filtro esteja preenchido dispara a consulta novamente
	        if (getRowCount() <= 0 || (filters != null && !filters.isEmpty())) {
	            setRowCount(fornecedorBean.countAll(filters));
	        }
	        
		} catch (NumberFormatException e) {
			log.error("Erro de parse para Long.", e);
		} catch (NamingException e) {
			log.error("Erro de Naming.", e);
		}

		// quantidade a ser exibida em cada página
        setPageSize(pageSize);

        return fornecedores;
    }

    @Override
    public Fornecedor getRowData(String rowKey) {
        for (Fornecedor fornecedor : fornecedores) {
            if (rowKey.equals(String.valueOf(fornecedor.getId())))
                return fornecedor;
        }

        return null;
    }

    @Override
    public Object getRowKey(Fornecedor fornecedor) {
        return fornecedor.getId();
    }

}