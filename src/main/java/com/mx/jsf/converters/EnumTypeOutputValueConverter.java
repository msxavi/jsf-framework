package com.mx.jsf.converters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.mx.common.exceptions.SystemException;
import com.mx.common.strings.StringHelper;
import com.mx.jsf.entities.enums.BaseEnumType;
import com.mx.jsf.helpers.FacesMessageHelper;

/**
 * 
 * @author emerson.xavier
 *
 */
public class EnumTypeOutputValueConverter implements Converter {

    public interface Parameter {
	String GET_STRING_VALUE_METHOD = "getStringValueMethod";
	String GET_PROPERTY_FILE_METHOD = "getPropertyFileMethod";
    }

    public Object getAsObject(FacesContext context, UIComponent component,
	    String value) {
	return value;
    }

    public String getAsString(FacesContext context, UIComponent component,
	    Object value) {
	if (value == null) {
	    return null;
	}
	String stringValueMethod = (String) component.getAttributes().get(
		Parameter.GET_STRING_VALUE_METHOD);
	if (StringHelper.isBlank(stringValueMethod)) {
	    stringValueMethod = BaseEnumType.GET_STRING_VALUE_METHOD_NAME;
	}
	String propertyFileMethod = (String) component.getAttributes().get(
		Parameter.GET_PROPERTY_FILE_METHOD);
	if (StringHelper.isBlank(propertyFileMethod)) {
	    propertyFileMethod = BaseEnumType.GET_PROPERTY_FILE_METHOD_NAME;
	}
	try {
	    Class<?> enumClass = value.getClass();
	    Method getStringValueMethod = enumClass.getMethod(
		    stringValueMethod, new Class[] {});
	    String key = (String) getStringValueMethod.invoke(value);
	    Method getPropertyFileMethod = enumClass.getMethod(
		    propertyFileMethod, new Class[] {});
	    String propertyFile = (String) getPropertyFileMethod.invoke(value);
	    FacesMessageHelper fh = new FacesMessageHelper(context);
	    return fh.getTranslatedMessage(propertyFile, key);
	} catch (SecurityException e) {
	    throw new SystemException(e);
	} catch (NoSuchMethodException e) {
	    throw new SystemException(e);
	} catch (IllegalArgumentException e) {
	    throw new SystemException(e);
	} catch (IllegalAccessException e) {
	    throw new SystemException(e);
	} catch (InvocationTargetException e) {
	    throw new SystemException(e);
	}
    }

}