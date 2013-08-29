package com.puma.exception;

public class BusinessException extends Exception
{

	public static final BusinessException DATA_NOT_FOUND = new BusinessException("data_not_found");
    public static final BusinessException DATA_DUPLICATION = new BusinessException("data_duplication");
    public static final BusinessException PASSWORD_NOT_MATCH = new BusinessException("password_not_match");
    public static final BusinessException USER_NOT_LOGIN = new BusinessException("user_not_login");
    private static final long serialVersionUID = 1L;
    private String code;
    
    public BusinessException(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

}
