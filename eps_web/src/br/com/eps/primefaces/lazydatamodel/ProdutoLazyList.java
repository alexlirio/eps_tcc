package br.com.eps.primefaces.lazydatamodel;

import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.eps.bean.ProdutoBean;
import br.com.eps.model.Produto;

public class ProdutoLazyList extends LazyDataModel<Produto> {

	private static final long serialVersionUID = 1L;

	Logger log = Logger.getLogger(ProdutoLazyList.class);

    private List<Produto> produtos;

    @Override
   	public List<Produto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
        String ordernacao = sortOrder.toString();

        if(SortOrder.UNSORTED.equals(sortOrder)){
            ordernacao = SortOrder.ASCENDING.toString();
        }
        
		try {
	        Context initialContext = new InitialContext();
	        ProdutoBean produtoBean = (ProdutoBean) initialContext.lookup("java:global/eps_ear/eps_ejb/ProdutoBean");
	        produtos = produtoBean.buscaPorPaginacao(first, pageSize, sortField, ordernacao, filters);
	        
	        // total encontrado no banco de dados, caso o filtro esteja preenchido dispara a consulta novamente
	        if (getRowCount() <= 0 || (filters != null && !filters.isEmpty())) {
	            setRowCount(produtoBean.countAll(filters));
	        }
	        
		} catch (NumberFormatException e) {
			log.error("Erro de parse para Long.", e);
		} catch (NamingException e) {
			log.error("Erro de Naming.", e);
		}

		// quantidade a ser exibida em cada página
        setPageSize(pageSize);

        return produtos;
    }

    @Override
    public Produto getRowData(String rowKey) {
        for (Produto produto : produtos) {
            if (rowKey.equals(String.valueOf(produto.getId())))
                return produto;
        }

        return null;
    }

    @Override
    public Object getRowKey(Produto produto) {
        return produto.getId();
    }

}