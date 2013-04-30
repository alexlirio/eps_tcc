package br.com.eps.primefaces.lazydatamodel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.eps.bean.CotacaoBean;
import br.com.eps.bean.FornecedorBean;
import br.com.eps.model.Cotacao;
import br.com.eps.model.Fornecedor;
import br.com.eps.model.StatusCotacao;

public class CotacaoFornecedorLazyList extends LazyDataModel<Cotacao> {

	private static final long serialVersionUID = 1L;

	Logger log = Logger.getLogger(CotacaoFornecedorLazyList.class);

    private List<Cotacao> cotacoes;
    private Set<Cotacao> cotacoesFornecedor = new HashSet<Cotacao>();
    private Fornecedor fornecedor;

    @Override
   	public List<Cotacao> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {

        String ordernacao = sortOrder.toString();

        if(SortOrder.UNSORTED.equals(sortOrder)){
            ordernacao = SortOrder.ASCENDING.toString();
        }
        
		try {
			Context initialContext = new InitialContext();
	        FornecedorBean fornecedorBean = (FornecedorBean) initialContext.lookup("java:global/eps_ear/eps_ejb/FornecedorBean");
	        fornecedor = fornecedorBean.findByEmail(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
	        
	        CotacaoBean cotacaoBean = (CotacaoBean) initialContext.lookup("java:global/eps_ear/eps_ejb/CotacaoBean");
	        cotacoes = cotacaoBean.buscaPorPaginacao(first, pageSize, sortField, ordernacao, filters);
	        
	        // total encontrado no banco de dados, caso o filtro esteja preenchido dispara a consulta novamente
	        if (getRowCount() <= 0 || (filters != null && !filters.isEmpty())) {
	            setRowCount(cotacaoBean.countAll(filters));
	        }
	        
		} catch (NumberFormatException e) {
			log.error("Erro de parse para Long.", e);
		} catch (NamingException e) {
			log.error("Erro de Naming.", e);
		}

		// quantidade a ser exibida em cada página
        setPageSize(pageSize);

        for (Cotacao cotacao : cotacoes) {
			if (cotacao.getFornecedor() != null && cotacao.getFornecedor().equals(fornecedor) &&
					cotacao.getStatusCotacao().equals(StatusCotacao.INICIADA)) {
				cotacoesFornecedor.add(cotacao);
			}
		}
        
        return new ArrayList<Cotacao>(cotacoesFornecedor);
    }

    @Override
    public Cotacao getRowData(String rowKey) {
        for (Cotacao cotacao : cotacoes) {
            if (rowKey.equals(String.valueOf(cotacao.getId())))
                return cotacao;
        }

        return null;
    }

    @Override
    public Object getRowKey(Cotacao cotacao) {
        return cotacao.getId();
    }

}