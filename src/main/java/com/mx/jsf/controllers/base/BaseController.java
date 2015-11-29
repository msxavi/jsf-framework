/**
 * 
 */
package com.mx.jsf.controllers.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mx.common.exceptions.ApplicationException;

/**
 * @author Emerson
 * @param <T>
 * 
 */
public abstract class BaseController<T> extends GenericController<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8529467540251307292L;

	public static final String CURRENT_TRANSACTION_CODE = "transactionCodeAttribute";
	public static final String ENVIRONMENT_DESCRIPTION = "environmentDescriptionAttribute";
	public static final String HIGHTLIGHT_ENVIRONMENT_DESCRIPTION = "highlightEnvironmentDescriptionAttribute";

	private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);
	private StateManager state = new StateManager();

	/**
	 * Método chamado no primeiro acesso a funcionalidade através do menu. Método a ser implementado pelas subClasses
	 * 
	 * @return
	 * @throws ApplicationException
	 */
	public String load() throws ApplicationException {
		LOG.info("load() <<");
		reset();
		String forward = openPage();
		updateCurrentTransactionCode();
		LOG.info("load() >>");
		return forward;
	}

	protected void updateCurrentTransactionCode() {
		getSession().setAttribute(CURRENT_TRANSACTION_CODE, getTransactionCode());
	}

	protected void reset() {
		getState().reset();
		resetEntityBean();
	}

	public abstract String openPage() throws ApplicationException;

	protected abstract void resetEntityBean();

	protected abstract String getTransactionCode();

	/**
	 * @return the state
	 */
	public StateManager getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(StateManager state) {
		this.state = state;
	}

}
