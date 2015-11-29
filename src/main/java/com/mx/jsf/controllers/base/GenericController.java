package com.mx.jsf.controllers.base;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.mx.common.notifications.BusinessMessage;
import com.mx.common.notifications.BusinessMessageType;
import com.mx.common.notifications.Notification;
import com.mx.jsf.helpers.FacesMessageHelper;

/**
 * Classe base para classes controller do JSF
 * 
 * 
 * @param <T>
 *            Classe controller a ser manipulada
 * 
 */
public class GenericController<T> implements Serializable {

    public static final String USER_ATTRIBUTE = "user";

    private static final long serialVersionUID = 322457213341853755L;

    protected static final String SAME_PAGE = null;

    protected static final String CHARACTER_ENCONDING = "UTF-8";

    /**
     * 
     * @param mimeType
     * @param downloadFileName
     */
    protected void preSetXLSExternalContext(String mimeType,
	    String downloadFileName) {
	getExternalContext().setResponseCharacterEncoding(CHARACTER_ENCONDING);
	getExternalContext().setResponseContentType(mimeType);
	getExternalContext().setResponseHeader("Content-Disposition",
		"inline; filename=\"" + downloadFileName + "\"");
    }

    /**
     * Retorna object ServletContext
     * 
     * @return ServletContext
     * 
     */
    protected ServletContext getServletContext() {
	return (ServletContext) FacesContext.getCurrentInstance()
		.getExternalContext().getContext();
    }

    /**
     * Retorna objeto servlet context
     * 
     * @return ExternalContext
     */
    protected ExternalContext getExternalContext() {
	return FacesContext.getCurrentInstance().getExternalContext();
    }

    /**
     * Invoca facesContext.responseComplete();
     */
    protected void responseComplete() {
	FacesContext facesContext = FacesContext.getCurrentInstance();
	facesContext.responseComplete();
    }

    /**
     * Invoca requestContext.execute(String command);
     * 
     * @param command
     */
    protected void executeContextCommand(String command) {
	RequestContext requestContext = RequestContext.getCurrentInstance();
	requestContext.execute(command);
    }

    /**
     * Retorna object Request
     * 
     * @return HttpServletRequest
     * 
     */
    protected HttpServletRequest getRequest() {
	return (HttpServletRequest) FacesContext.getCurrentInstance()
		.getExternalContext().getRequest();
    }

    /**
     * Retorna object Response
     * 
     * @return HttpServletResponse
     * 
     */
    protected HttpServletResponse getResponse() {
	return (HttpServletResponse) FacesContext.getCurrentInstance()
		.getExternalContext().getResponse();
    }

    /**
     * Retorna object Session
     * 
     * @return HttpSession
     * 
     */
    protected HttpSession getSession() {
	return this.getRequest().getSession();
    }

    /**
     * Seta atributo no Request
     * 
     * @param name
     *            Nome do atributo
     * @param value
     *            Valor do atributo
     * 
     */
    protected void setAttributeOnRequest(String name, Object value) {
	this.getRequest().setAttribute(name, value);
    }

    /**
     * Seta atributo na Session
     * 
     * @param name
     *            Nome do atributo
     * @param value
     *            Valor do atributo
     * 
     */
    protected void setAttributeOnSession(String name, Object value) {
	this.getSession().setAttribute(name, value);
    }

    /**
     * Recupera objeto na Session
     * 
     * @param name
     *            Nome do atributo na Session
     * @param removeAttribute
     *            Indica se atributo deve ser removido apís ser recuperado
     * 
     * @return Object Retorna null caso atributo nío seja encontrado
     * 
     */
    protected Object getAttributeOnSession(String name, boolean removeAttribute) {
	// Recupera objeto na session, depois remove
	HttpSession session = this.getSession();
	Object object = session.getAttribute(name);

	// Caso atributo precise ser removido
	if (object != null && removeAttribute) {
	    session.removeAttribute(name);
	} // end if

	// Retorna objeto
	return object;
    }

    /**
     * Recupera objeto na Session
     * 
     * @param name
     *            Nome do atributo na Session
     * 
     * @return Object Retorna null caso atributo nío seja encontrado
     * 
     */
    protected Object getAttributeOnSession(String name) {
	return this.getAttributeOnSession(name, false);
    }

    /**
     * Recupera objeto no Request
     * 
     * @param name
     *            Nome do atributo no Request
     * 
     * @return Object Retorna null caso atributo nío seja encontrado
     * 
     */
    protected Object getAttributeOnRequest(String name) {
	return this.getRequest().getAttribute(name);
    }

    /**
     * Verifica se determinado atributo se encontra na Session
     * 
     * @param name
     *            Nome do atributo
     * 
     * @return boolean true caso atributo seja encontrado
     * 
     */
    protected boolean existsAttributeOnSession(String name) {
	boolean exists = false;
	// Busca atributo na session
	@SuppressWarnings("rawtypes")
	Enumeration atributos = getSession().getAttributeNames();
	while (atributos.hasMoreElements()) {
	    String atributo = (String) atributos.nextElement();
	    if (name.equals(atributo)) {
		exists = true;
		break;
	    } // end if
	} // end while

	// Retorna exists
	return exists;
    }

    /**
     * Adiciona um objeto Business notification que exibe mensagens em diversos
     * níveis para o cliente. Níveis: INFO, WARN, ERROR
     * 
     * @param notification
     */
    protected void addNotification(Notification notification) {
	getFacesMessagesHelper().addNotification(notification);
    }

    /**
     * Retorna valor da chave informada por parímetro, para o arquivo de
     * ResourceBundle padrão
     * 
     * @param messageKey
     *            Chave do arquivo de resource
     * 
     * @return String Valor da chave
     * 
     */
    protected String getMessage(String messageKey, Object... arguments) {
	return getFacesMessagesHelper().getTranslatedMessage(messageKey,
		arguments);
    }

    /**
     * Adiciona uma mensagem INFO para ser renderizada no response
     * 
     * @param clientId
     *            ID do componente de tela a qual a mensagem se refere
     * 
     * @param messageKey
     *            Chave da mensagem no arquivo de ResourceBundle padrão da
     *            aplicaçõa
     * 
     * @param arguments
     *            Argumentos da mensagem, caso estes existam
     * 
     */
    protected void addInfoMessage(String clientId, String messageKey,
	    Object... arguments) {
	BusinessMessage businessMessage = new BusinessMessage(
		BusinessMessageType.INFO, messageKey, arguments);
	getFacesMessagesHelper().addMessage(clientId, businessMessage);
    }

    /**
     * Adiciona uma mensagem INFO para ser renderizada no response
     * 
     * 
     * @param messageKey
     *            Chave da mensagem no arquivo de ResourceBundle padrão da
     *            aplicaçõa
     * 
     * @param arguments
     *            Argumentos da mensagem, caso estes existam
     * 
     */
    protected void addInfoMessage(String messageKey, Object... arguments) {
	addInfoMessage(null, messageKey, arguments);
    }

    /**
     * 
     * @param messageKey
     */
    protected void addInfoMessage(String messageKey) {
	addInfoMessage(null, messageKey, new Object[] {});
    }

    /**
     * Adiciona uma mensagem WARN para ser renderizada no response
     * 
     * @param clientId
     *            ID do componente de tela a qual a mensagem se refere
     * 
     * @param messageKey
     *            Chave da mensagem no arquivo de ResourceBundle padrão da
     *            aplicaçõa
     * 
     * @param arguments
     *            Argumentos da mensagem, caso estes existam
     * 
     */
    protected void addWarnMessage(String clientId, String messageKey,
	    Object... arguments) {
	BusinessMessage businessMessage = new BusinessMessage(
		BusinessMessageType.WARN, messageKey, arguments);
	getFacesMessagesHelper().addMessage(clientId, businessMessage);
    }

    /**
     * Adiciona uma mensagem WARN para ser renderizada no response
     * 
     * @param messageKey
     *            Chave da mensagem no arquivo de ResourceBundle padrão da
     *            aplicação
     * @param arguments
     *            Argumentos da mensagem, caso estes existam
     */
    protected void addWarnMessage(String messageKey, Object... arguments) {
	addWarnMessage(null, messageKey, arguments);
    }

    /**
     * 
     * @param messageKey
     */
    protected void addWarnMessage(String messageKey) {
	addWarnMessage(null, messageKey, new Object[] {});
    }

    /**
     * Adiciona uma mensagem ERROR para ser renderizada no response
     * 
     * @param clientId
     *            ID do componente de tela a qual a mensagem se refere
     * 
     * @param messageKey
     *            Chave da mensagem no arquivo de ResourceBundle padrão da
     *            aplicação
     * 
     * @param arguments
     *            Argumentos da mensagem, caso estes existam
     * 
     */
    protected void addErrorMessage(String clientId, String messageKey,
	    Object... arguments) {
	BusinessMessage businessMessage = new BusinessMessage(
		BusinessMessageType.ERROR, messageKey, arguments);
	getFacesMessagesHelper().addMessage(clientId, businessMessage);
    }

    /**
     * Adiciona uma mensagem ERROR para ser renderizada no response
     * 
     * @param messageKey
     *            Chave da mensagem no arquivo de ResourceBundle padrão da
     *            aplicação
     * @param arguments
     */
    protected void addErrorMessage(String messageKey, Object... arguments) {
	addErrorMessage(null, messageKey, arguments);
    }

    /**
     * 
     * @param messageKey
     */
    protected void addErrorMessage(String messageKey) {
	addErrorMessage(null, messageKey, new Object[] {});
    }

    /**
     * Adiciona uma mensagem FATAL para ser renderizada no response
     * 
     * @param clientId
     *            ID do componente de tela a qual a mensagem se refere
     * 
     * @param messageKey
     *            Chave da mensagem no arquivo de ResourceBundle padrão da
     *            aplicaçõa
     * 
     * @param arguments
     *            Argumentos da mensagem, caso estes existam
     * 
     */
    protected void addFatalMessage(String clientId, String messageKey,
	    Object... arguments) {
	BusinessMessage businessMessage = new BusinessMessage(
		BusinessMessageType.FATAL, messageKey, arguments);
	getFacesMessagesHelper().addMessage(clientId, businessMessage);
    }

    /**
     * Adiciona uma mensagem FATAL para ser renderizada no response
     * 
     * @param messageKey
     *            Chave da mensagem no arquivo de ResourceBundle padrão da
     *            aplicação
     * @param arguments
     */
    protected void addFatalMessage(String messageKey, Object... arguments) {
	addFatalMessage(null, messageKey, arguments);
    }

    /**
     * 
     * @param messageKey
     */
    protected void addFatalMessage(String messageKey) {
	addFatalMessage(null, messageKey, new Object[] {});
    }

    /**
     * Adiciona uma mensagem do tipo passado para ser renderizada no response
     * 
     * @param clientId
     *            ID do componente de tela a qual a mensagem se refere
     * 
     * @param messageType
     *            typo da mensagem a ser renderizada
     * 
     * @param messageKey
     *            Chave da mensagem no arquivo de ResourceBundle padrão da
     *            aplicaçõa
     * 
     * @param arguments
     *            Argumentos da mensagem, caso estes existam
     * 
     */
    protected void addMessage(String clientId, BusinessMessageType messageType,
	    String messageKey, Object... arguments) {
	BusinessMessage businessMessage = new BusinessMessage(messageType,
		messageKey, arguments);
	getFacesMessagesHelper().addMessage(clientId, businessMessage);
    }

    /**
     * 
     * @return FacesMessageHelper
     */
    protected FacesMessageHelper getFacesMessagesHelper() {
	return new FacesMessageHelper(FacesContext.getCurrentInstance());
    }

    /**
     * 
     * @return String null
     */
    public String refresh() {
	return SAME_PAGE;
    }

    /**
     * 
     * @param param
     * @return String
     */
    protected String getParam(String param) {
	Map<String, String> params = FacesContext.getCurrentInstance()
		.getExternalContext().getRequestParameterMap();
	return params.get(param);
    }

}
