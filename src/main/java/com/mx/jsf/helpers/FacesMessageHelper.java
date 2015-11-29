/**
 * 
 */
package com.mx.jsf.helpers;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import com.mx.common.notifications.BusinessMessage;
import com.mx.common.notifications.BusinessMessageType;
import com.mx.common.notifications.Notification;
import com.mx.common.resources.MessageResources;
import com.mx.common.resources.Translator;

/**
 * 
 * @author Emerson
 *
 */
public class FacesMessageHelper implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 729118282729794284L;

    private FacesContext context;

    public FacesMessageHelper(FacesContext context) {
	this.context = context;
    }

    /**
     * 
     * Creates a customized FacesMessage
     * 
     * @param businessMessage
     * @return FacesMessage
     */
    private FacesMessage createFacesMessage(BusinessMessage businessMessage) {
	String messageKey = businessMessage.getMessage();
	Object[] arguments = businessMessage.getArguments();
	String translatedMessage = Translator.translate(messageKey, arguments);
	BusinessMessageType messageType = businessMessage.getMessageType();
	return createFacesMessage(translatedMessage, messageType);
    }

    /**
     * 
     * @param translatedMessage
     * @param messageType
     * @return FacesMessage
     */
    public FacesMessage createFacesMessage(String translatedMessage,
	    BusinessMessageType messageType) {
	return new FacesMessage(getFacesMessageSeverety(messageType),
		translatedMessage, null);
    }

    /**
     * 
     * @param messageKey
     * @param arguments
     * @return String
     */
    public String getTranslatedMessage(String messageKey, Object... arguments) {
	Application application = this.context.getApplication();
	ResourceBundle bundle = ResourceBundle.getBundle(application
		.getMessageBundle());
	return Translator.translate(new MessageResources(bundle), messageKey,
		arguments);
    }

    /**
     * 
     * @param bundleBaseName
     * @param messageKey
     * @param arguments
     * @return String
     */
    public String getTranslatedMessage(String bundleBaseName,
	    String messageKey, Object... arguments) {
	ResourceBundle bundle = ResourceBundle.getBundle(bundleBaseName,
		this.context.getExternalContext().getRequestLocale());
	return Translator.translate(new MessageResources(bundle), messageKey,
		arguments);
    }

    /**
     * Adiciona um objeto Notification que exibe mensagens em diversos níveis
     * para o cliente. Níveis: INFO, WARN, ERROR
     * 
     * @param Notification
     */
    public void addNotification(Notification notification) {
	this.addMessages(notification);
    }

    /**
     * Adiciona uma Coleção de objeto Notification
     * 
     * @param List
     *            <Notification> notifications
     * @return void
     * @see method addNotification(Notification notification)
     * 
     */
    public void addNotifications(List<Notification> notifications) {
	if (notifications == null) {
	    return;
	}
	for (Notification notification : notifications) {
	    addNotification(notification);
	}
    }

    private void addMessages(Notification notification) {
	Collection<BusinessMessage> businessMessages = notification
		.getMessages();
	for (BusinessMessage businessMessage : businessMessages) {
	    addMessage(businessMessage);
	}
    }

    /**
     * 
     * @param clientId
     * @param businessMessage
     */
    public void addMessage(String clientId, BusinessMessage businessMessage) {
	context.addMessage(clientId, createFacesMessage(businessMessage));
    }

    /**
     * 
     * @param businessMessage
     */
    public void addMessage(BusinessMessage businessMessage) {
	addMessage(null, businessMessage);
    }

    private Severity getFacesMessageSeverety(BusinessMessageType messageType) {
	switch (messageType) {
	case FATAL:
	    return FacesMessage.SEVERITY_FATAL;
	case ERROR:
	    return FacesMessage.SEVERITY_ERROR;
	case WARN:
	    return FacesMessage.SEVERITY_WARN;
	case INFO:
	    return FacesMessage.SEVERITY_INFO;
	default:
	    throw new IllegalArgumentException(messageType.toString());
	}
    }

}
