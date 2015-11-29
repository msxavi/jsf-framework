package com.mx.jsf.controllers.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mx.common.exceptions.ApplicationException;

/**
 * Class to standardize Controller methods names.
 * 
 * In case of fewer features use BaseController instead.
 * 
 * @author Emerson
 *
 * @param <T>
 */
public abstract class AbstractCrudController<T> extends
		AbstractSearchController<T> {

	private static final long serialVersionUID = 9185293361311068479L;
	public static final Logger LOG = LoggerFactory
			.getLogger(AbstractCrudController.class);

	protected static final String EDIT = "edit";

	/**
	 * Open an empty form to creation. Call this method from views.
	 * 
	 * @return String
	 */
	public String create() throws ApplicationException {
		LOG.info("create() <<");
		setCreateState();
		resetEntityBean();
		String forward = openCreate();
		updateCurrentTransactionCode();
		LOG.info("create() >>");
		return forward;
	}

	/**
	 * Carrega um objeto para edição e invoca o openEdit da subClasse Controller
	 * para obter o forward. Este deve ser o método chamado pela view.
	 * 
	 * @return String
	 */
	public final String edit() throws ApplicationException {
		LOG.info("edit() <<");
		setUpdateState();
		loadEntityForm();
		String forward = openEdit();
		updateCurrentTransactionCode();
		LOG.info("edit() >>");
		return forward;
	}

	/**
	 * Executa a ação de salvar os dados do form.
	 * 
	 * @return String
	 * @throws ApplicationException
	 * @throws EntityNotQualifiedException
	 */
	public abstract String save() throws ApplicationException;

	/**
	 * Primeiro método chamado quando o edit() é invocado pela view. Este método
	 * deve ser implementado para carregar no form o objeto Entity
	 * correspondente que foi selecionado a partir de um ID.
	 * 
	 * Ex: Entity entity = entityService.findByID(id);
	 * 
	 */
	protected abstract void loadEntityForm();

	/**
	 * 
	 * @return String
	 */
	public String prepareDelete() {
		super.setIdToSelection(getIdParam());
		return SAME_PAGE;
	}

	/**
	 * Abre o form para a edição nas subClasses Controller.
	 * 
	 * @return String
	 */
	protected String openEdit() throws ApplicationException {
		return EDIT;
	}

	/**
	 * Executa a ação de remover um registro selecionado.
	 * 
	 * @return String
	 */
	public String delete() throws ApplicationException {
		LOG.info("delete() <<");
		loadEntityForm();
		executeDelete();
		executeSearch();
		LOG.info("delete() >>");
		return SAME_PAGE;
	}

	/**
	 * Executa a ação de remover um registro selecionado.
	 * 
	 */
	protected abstract void executeDelete() throws ApplicationException;

	/**
	 * Abre um form vazio para inclusão de dados.
	 * 
	 * @return String
	 */
	protected String openCreate() throws ApplicationException {
		return EDIT;
	}

	/**
	 * Abre uma tela para visualização dos dados da entidade
	 * 
	 * @return String
	 */
	public String view() throws ApplicationException {
		LOG.info("view() <<");
		setViewState();
		loadEntityForm();
		updateCurrentTransactionCode();
		LOG.info("view() >>");
		return openView();
	}

	/**
	 * Abre um form vazio para inclusão de dados.
	 * 
	 * @return String
	 */
	protected String openView() throws ApplicationException {
		return EDIT;
	}

	/**
	 * 
	 * @return String
	 */
	public String getTitle() {
		String titleKey;
		if (getState().isCreate()) {
			titleKey = getCreateTitleKey();
		} else if (getState().isUpdate()) {
			titleKey = getUpdateTitleKey();
		} else if (getState().isView()) {
			titleKey = getViewTitleKey();
		} else {
			titleKey = getSearchTitleKey();
		}
		return getMessage(titleKey);
	}

	/**
	 * 
	 * @return String
	 */
	protected abstract String getSearchTitleKey();

	/**
	 * 
	 * @return String
	 */
	protected abstract String getViewTitleKey();

	/**
	 * 
	 * @return String
	 */
	protected abstract String getUpdateTitleKey();

	/**
	 * 
	 * @return String
	 */
	protected abstract String getCreateTitleKey();

}
