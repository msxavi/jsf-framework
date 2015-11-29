package com.mx.jsf.entities.enums;


public interface BaseEnumType {

	String GET_ENUM_VALUE_METHOD_NAME = "getEnum";
	String GET_STRING_VALUE_METHOD_NAME = "getStringValue";
	String GET_PROPERTY_FILE_METHOD_NAME = "getPropertyFileName";

	BaseEnumType getEnumValue(String code);
	String getStringValue();

}
