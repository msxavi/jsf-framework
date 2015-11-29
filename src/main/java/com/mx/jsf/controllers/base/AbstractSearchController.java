package com.mx.jsf.controllers.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mx.common.exceptions.ApplicationException;

/**
 * Classe base para padronizar nomenclatura dos métodos dos Controllers que
 * terão características de CRUD.
 * 
 * Caso o Controller não seja um CRUD utilize BaseController
 * 
 * @param <T>
 *            Classe controller a ser manipulada
 * 
 * @author Emerson
 *
 * @param <T>
 */
public abstract class AbstractSearchController<T> extends BaseController<T> {

	private static final long serialVersionUID = 9185293361311068479L;
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractSearchController.class);

	public static final String ID = "ID";

	private String idToSelection;

	/**
	 * Abre um form vazio para pesquisa. Este deve ser o método chamado pela
	 * view.
	 * 
	 * @return String
	 */
	public String search() throws ApplicationException {
		LOG.info("search() <<");
		this.getState().setSearch(true);
		String forward = executeSearch();
		updateCurrentTransactionCode();
		LOG.info("search() >>");
		return forward;
	}

	/**
	 * Executa uma busca utilizando filtros.
	 * 
	 * @return String
	 */
	public abstract String executeSearch() throws ApplicationException;

	/**
	 * 
	 * @return String
	 */
	public String newSearch() {
		setSearch(false);
		resetSearchResults();
		return SAME_PAGE;
	}

	public void setSearch(boolean b) {
		this.getState().setSearch(b);
	}

	protected String getIdParam() {
		String param = getParam(ID);
		String result = this.idToSelection == null ? param : this.idToSelection;
		this.idToSelection = null;
		return result;
	}

	public abstract String openPage();

	/**
	 * Volta para a tela de pesquisa, executando-a novamente caso o estado de
	 * pesquisa esteja com valor <code>true</code>
	 * 
	 * @return String
	 * @throws ApplicationException
	 */
	public String backToSearch() throws ApplicationException {
		StateManager currentState = getState();
		currentState.resetCreateUpdateViewState();
		if (currentState.isSearch()) {
			executeSearch();
		}
		updateCurrentTransactionCode();
		return openPage();
	}

	public void setViewState() {
		getState().setViewState();
	}

	public void setCreateState() {
		getState().setCreateState();
	}

	public void setUpdateState() {
		getState().setUpdateState();
	}

	protected abstract void resetFilter();

	protected abstract void resetSearchResults();

	@Override
	protected void reset() {
		super.reset();
		resetFilter();
		resetSearchResults();
		updateCurrentTransactionCode();
	}

	/**
	 * @return the idToSelection
	 */
	public String getIdToSelection() {
		return idToSelection;
	}

	/**
	 * @param idToSelection
	 *            the idToSelection to set
	 */
	public void setIdToSelection(String idToSelection) {
		this.idToSelection = idToSelection;
	}

}
