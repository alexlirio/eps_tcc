package br.com.eps.primefaces.lazydatamodel;

import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.eps.bean.CompanhiaBean;
import br.com.eps.model.Companhia;

public class CompanhiaLazyList extends LazyDataModel<Companhia> {

	private static final long serialVersionUID = 1L;

	Logger log = Logger.getLogger(CompanhiaLazyList.class);

    private List<Companhia> companhias;

    @Override
   	public List<Companhia> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
        String ordernacao = sortOrder.toString();

        if(SortOrder.UNSORTED.equals(sortOrder)){
            ordernacao = SortOrder.ASCENDING.toString();
        }
        
		try {
	        Context initialContext = new InitialContext();
	        CompanhiaBean companhiaBean = (CompanhiaBean) initialContext.lookup("java:global/eps_ear/eps_ejb/CompanhiaBean");
	        companhias = companhiaBean.buscaPorPaginacao(first, pageSize, sortField, ordernacao, filters);
	        
	        // total encontrado no banco de dados, caso o filtro esteja preenchido dispara a consulta novamente
	        if (getRowCount() <= 0 || (filters != null && !filters.isEmpty())) {
	            setRowCount(companhiaBean.countAll(filters));
	        }
	        
		} catch (NumberFormatException e) {
			log.error("Erro de parse para Long.", e);
		} catch (NamingException e) {
			log.error("Erro de Naming.", e);
		}

		// quantidade a ser exibida em cada página
        setPageSize(pageSize);

        return companhias;
    }

    @Override
    public Companhia getRowData(String rowKey) {
        for (Companhia companhia : companhias) {
            if (rowKey.equals(String.valueOf(companhia.getId())))
                return companhia;
        }

        return null;
    }

    @Override
    public Object getRowKey(Companhia companhia) {
        return companhia.getId();
    }

}