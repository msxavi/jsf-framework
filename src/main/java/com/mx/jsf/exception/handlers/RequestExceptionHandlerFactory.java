/**
 * 
 */
package com.mx.jsf.exception.handlers;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * Classe para cria o ExceptionHandler do JSF e que deve ser incluída no arquivo faces-config
 * 
 * <factory> <exception-handler-factory> com.accenture.jsf.exception.handlers.RequestExceptionHandlerFactory
 * </exception-handler-factory> </factory>
 * 
 * @author Emerson
 * 
 */
public class RequestExceptionHandlerFactory extends ExceptionHandlerFactory {

	private ExceptionHandlerFactory parent;

	@Override
	public ExceptionHandler getExceptionHandler() {
		return new RequestExceptionHandler(parent.getExceptionHandler());
	}

	// this injection handles jsf
	public RequestExceptionHandlerFactory(ExceptionHandlerFactory parent) {
		this.parent = parent;
	}

}
