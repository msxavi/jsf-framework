package com.mx.jsf.converters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.mx.common.exceptions.SystemException;
import com.mx.common.strings.StringHelper;

public class BooleanFlagConverter implements Converter {

    public interface Parameter {
	String BOOLEAN_TYPE_CLASS_NAME = "booleanTypeClassName";
	String GET_TRUE_STRING_VALUE_METHOD = "getTrueStringValueMethod";
	String GET_FALSE_STRING_VALUE_METHOD = "getFalseStringValueMethod";
    }

    public interface DefaultValues {
	String GET_TRUE_STRING_VALUE_METHOD = "getTrueStringValue";
	String GET_FALSE_STRING_VALUE_METHOD = "getFalseStringValue";
    }

    public Object getAsObject(FacesContext context, UIComponent component,
	    String value) {
	if (StringHelper.isBlank(value)) {
	    return null;
	}
	String className = (String) component.getAttributes().get(
		Parameter.BOOLEAN_TYPE_CLASS_NAME);
	String trueMthd = (String) component.getAttributes().get(
		Parameter.GET_TRUE_STRING_VALUE_METHOD);
	String falseMthd = (String) component.getAttributes().get(
		Parameter.GET_FALSE_STRING_VALUE_METHOD);

	if (StringHelper.isBlank(trueMthd)) {
	    trueMthd = DefaultValues.GET_TRUE_STRING_VALUE_METHOD;
	}
	if (StringHelper.isBlank(falseMthd)) {
	    falseMthd = DefaultValues.GET_FALSE_STRING_VALUE_METHOD;
	}
	try {
	    Class<?> booleanTypeClass = Class.forName(className);
	    Method getTrueStringValueMethod = booleanTypeClass.getMethod(
		    trueMthd, new Class[] {});
	    Method getFalseStringValueMethod = booleanTypeClass.getMethod(
		    falseMthd, new Class[] {});
	    String trueValue = (String) getTrueStringValueMethod.invoke(
		    booleanTypeClass, new Object[] {});
	    String falseValue = (String) getFalseStringValueMethod.invoke(
		    booleanTypeClass, new Object[] {});
	    if (trueValue.equals(value)) {
		return Boolean.TRUE;
	    }
	    if (falseValue.equals(value)) {
		return Boolean.FALSE;
	    }
	    return null;
	} catch (ClassNotFoundException e) {
	    throw new SystemException("invalid "
		    + Parameter.BOOLEAN_TYPE_CLASS_NAME + ": " + className, e);
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