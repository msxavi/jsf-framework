/**
 * 
 */
package com.mx.jsf.exception.handlers;

import java.util.Calendar;
import java.util.Iterator;
import java.util.UUID;

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mx.common.converters.Converter;
import com.mx.common.dates.DatePatterns;
import com.mx.common.exceptions.ApplicationException;
import com.mx.common.notifications.Notification;
import com.mx.common.resources.MessageResources;
import com.mx.jsf.helpers.FacesMessageHelper;

/**
 * Classe reponsável por tratar todas exceções lançadas pela aplicação.
 * 
 * @author Emerson
 * 
 */
public class RequestExceptionHandler extends ExceptionHandlerWrapper {

    private static final String VERSION = "version";

    public interface ErrorAttributes {
	String EXCEPTION = "exceptionAttribute";
	String EXCEPTION_MESSAGE = "exceptionMessageAttribute";
	String ERROR_DATE_TIME = "errorDateTimeAttribute";
	String ERROR_ID = "errorIdAttribute";
	String EXCEPTION_NAME = "exceptionNameAttribute";
	String SYSTEM_VERSION = "systemVersion";
    }

    private static final String ERROR_PAGE_FORWARD = "/errors/errorPage.xhml?faces-redirect=true";
    private static final MessageResources VERSION_CONFIG = new MessageResources(
	    VERSION);
    private static final String SYSTEM_VERSION = VERSION_CONFIG
	    .getResourceMessage(VERSION);
    private static final Logger LOG = LoggerFactory
	    .getLogger(RequestExceptionHandler.class);
    private ExceptionHandler wrapped;

    RequestExceptionHandler(ExceptionHandler exception) {
	this.wrapped = exception;
    }

    @Override
    public ExceptionHandler getWrapped() {
	return this.wrapped;
    }

    @Override
    public void handle() {
	final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents()
		.iterator();
	while (i.hasNext()) {
	    ExceptionQueuedEvent event = i.next();
	    ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event
		    .getSource();

	    // get the exception from context
	    Throwable originalException = context.getException();

	    boolean exceptionWasHandled = false;
	    FacesContext fc = FacesContext.getCurrentInstance();
	    try {
		Throwable cause = originalException.getCause();
		while (cause != null) {
		    if (cause instanceof ApplicationException) {
			handleCheckedException(fc, (ApplicationException) cause);
			exceptionWasHandled = true;
		    }
		    cause = cause.getCause();
		}
		if (!exceptionWasHandled) {
		    handleUncheckedException(fc, originalException);
		}
	    } catch (ViewExpiredException e) {
		LOG.error(
			"RequestExceptionHandler --> Throwing ViewExpiredException...",
			e);
	    } catch (Exception e) {
		LOG.error("ERROR while processing RequestExceptionHandler!", e);
	    } finally {
		i.remove();
	    }
	}
	getWrapped().handle();
    }

    private void handleUncheckedException(FacesContext fc,
	    Throwable originalException) {
	UUID errorId = UUID.randomUUID();
	LOG.error("ERROR caught by RequestExceptionHandler. Error ID: ["
		+ errorId.toString() + "]", originalException);
	storeError(fc, originalException, errorId);
	forwardToPage(fc, ERROR_PAGE_FORWARD);
    }

    private void forwardToPage(FacesContext fc, String forwardPage) {
	NavigationHandler nav = fc.getApplication().getNavigationHandler();
	nav.handleNavigation(fc, null, forwardPage);
	fc.renderResponse();
	fc.responseComplete();
    }

    /**
     * 
     * @param FacesContext
     * @param ApplicationException
     */
    protected void handleCheckedException(FacesContext fc,
	    ApplicationException cause) {
	Notification notification = cause.getNotification();
	publishNotification(fc, notification);
    }

    /**
     * 
     * @param FacesContext
     * @param notification
     */
    protected void publishNotification(FacesContext fc,
	    Notification notification) {
	new FacesMessageHelper(fc).addNotification(notification);
    }

    /**
     * 
     * @param facesContext
     * @param exception
     * @param errorId
     */
    private void storeError(FacesContext fc, Throwable exception, UUID errorId) {
	HttpServletRequest request = (HttpServletRequest) fc
		.getExternalContext().getRequest();
	HttpSession session = request.getSession();
	session.setAttribute(ErrorAttributes.EXCEPTION_NAME, exception
		.getClass().getCanonicalName());
	session.setAttribute(ErrorAttributes.ERROR_ID, errorId.toString());
	session.setAttribute(ErrorAttributes.ERROR_DATE_TIME, Converter
		.dateToString(Calendar.getInstance().getTime(),
			DatePatterns.DDMMYYYYHHMMSS_BAR));
	session.setAttribute(ErrorAttributes.EXCEPTION_MESSAGE,
		exception.getMessage());
	session.setAttribute(ErrorAttributes.EXCEPTION, exception);
	session.setAttribute(ErrorAttributes.SYSTEM_VERSION, SYSTEM_VERSION);
    }

}
