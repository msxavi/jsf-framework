package com.mx.jsf.controllers.base;

import java.io.Serializable;

/**
 * 
 * @author Emerson
 *
 */
public class StateManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4105697377386531731L;

	private boolean update;
	private boolean create;
	private boolean view;
	private boolean search;

	/**
	 * 
	 * @return
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * 
	 * @param update
	 */
	public void setUpdate(boolean update) {
		this.update = update;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isCreate() {
		return create;
	}

	/**
	 * 
	 * @param create
	 */
	public void setCreate(boolean create) {
		this.create = create;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isView() {
		return view;
	}

	/**
	 * 
	 * @param view
	 */
	public void setView(boolean view) {
		this.view = view;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSearch() {
		return search;
	}

	/**
	 * 
	 * @param search
	 */
	public void setSearch(boolean search) {
		this.search = search;
	}

	/**
	 * 
	 */
	public void setUpdateState() {
		setUpdate(true);
		setCreate(false);
		setView(false);
	}

	/**
	 * 
	 */
	public void setCreateState() {
		setUpdate(false);
		setCreate(true);
		setView(false);
	}

	/**
	 * 
	 */
	public void setViewState() {
		setUpdate(false);
		setCreate(false);
		setView(true);
	}

	/**
	 * 
	 */
	public void reset() {
		setSearch(false);
		resetCreateUpdateViewState();
	}

	/**
	 * 
	 */
	public void resetCreateUpdateViewState() {
		setUpdate(false);
		setCreate(false);
		setView(false);
	}

}
