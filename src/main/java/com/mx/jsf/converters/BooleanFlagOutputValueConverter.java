package com.mx.jsf.converters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.mx.common.exceptions.SystemException;
import com.mx.common.strings.StringHelper;
import com.mx.jsf.helpers.FacesMessageHelper;

public class BooleanFlagOutputValueConverter implements Converter {

    public interface Parameter {
	String BOOLEAN_TYPE_CLASS_NAME = "booleanTypeClassName";
	String GET_TRUE_STRING_VALUE_METHOD = "getTrueStringValueMethod";
	String GET_FALSE_STRING_VALUE_METHOD = "getFalseStringValueMethod";
	String GET_PROPERTY_FILE_METHOD = "getPropertyFileMethod";
    }

    public interface DefaultValues {
	String GET_TRUE_STRING_VALUE_METHOD = "getTrueStringValue";
	String GET_FALSE_STRING_VALUE_METHOD = "getFalseStringValue";
	String GET_PROPERTY_FILE_METHOD = "getPropertyFileName";
    }

    public Object getAsObject(FacesContext context, UIComponent component,
	    String value) {
	return value;
    }

    public String getAsString(FacesContext context, UIComponent component,
	    Object value) {
	if (value == null) {
	    return "";
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
	String propertyFileMethod = (String) component.getAttributes().get(
		Parameter.GET_PROPERTY_FILE_METHOD);
	if (StringHelper.isBlank(propertyFileMethod)) {
	    propertyFileMethod = DefaultValues.GET_PROPERTY_FILE_METHOD;
	}
	try {
	    Class<?> booleanTypeClass = Class.forName(className);

	    Boolean booleanValue = (Boolean) value;
	    String key;
	    if (booleanValue.booleanValue()) {
		Method getTrueStringValueMethod = booleanTypeClass.getMethod(
			trueMthd, new Class[] {});
		String trueValue = (String) getTrueStringValueMethod.invoke(
			booleanTypeClass, new Object[] {});
		key = trueValue;
	    } else {
		Method getFalseStringValueMethod = booleanTypeClass.getMethod(
			falseMthd, new Class[] {});
		String falseValue = (String) getFalseStringValueMethod.invoke(
			booleanTypeClass, new Object[] {});
		key = falseValue;
	    }

	    Method getPropertyFileMethod = booleanTypeClass.getMethod(
		    propertyFileMethod, new Class[] {});
	    String propertyFile = (String) getPropertyFileMethod.invoke(
		    booleanTypeClass, new Object[] {});

	    FacesMessageHelper fh = new FacesMessageHelper(context);
	    return fh.getTranslatedMessage(propertyFile, key);
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

}