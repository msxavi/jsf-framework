package com.mx.jsf.converters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.mx.common.exceptions.SystemException;
import com.mx.common.strings.StringHelper;
import com.mx.jsf.entities.enums.BaseEnumType;

/**
 * 
 * @author emerson.xavier
 *
 */
public class EnumTypeConverter implements Converter {

    public interface Parameter {
	String ENUM_CLASS_NAME = "enumClassName";
	String GET_ENUM_VALUE_METHOD = "getEnumValueMethod";
	String GET_STRING_VALUE_METHOD = "getStringValueMethod";
    }

    public Object getAsObject(FacesContext context, UIComponent component,
	    String value) {
	if (StringHelper.isBlank(value)) {
	    return null;
	}
	String className = (String) component.getAttributes().get(
		Parameter.ENUM_CLASS_NAME);
	String enumMthd = (String) component.getAttributes().get(
		Parameter.GET_ENUM_VALUE_METHOD);
	if (StringHelper.isBlank(enumMthd)) {
	    enumMthd = BaseEnumType.GET_ENUM_VALUE_METHOD_NAME;
	}
	try {
	    Class<?> enumClass = Class.forName(className);
	    Method getEnumValueMethod = enumClass.getMethod(enumMthd,
		    new Class[] { String.class });
	    return getEnumValueMethod.invoke(enumClass, new Object[] { value });
	} catch (ClassNotFoundException e) {
	    throw new SystemException("invalid enumClassName" + className, e);
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

    public String getAsString(FacesContext context, UIComponent component,
	    Object value) {
	return value == null ? null : value.toString();
    }

}