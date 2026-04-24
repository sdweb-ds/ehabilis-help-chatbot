package es.sdweb.application.vista.util;

import java.lang.reflect.Method;
import java.util.StringTokenizer;

import es.sdweb.application.model.exceptions.ExceptionErrorInterno;
import es.sdweb.application.util.GeneralException;

public class PropertyUtils {

	public static Object getAttribute(javax.servlet.jsp.PageContext pPageContext, String pName) {
		if (pPageContext.getRequest().getParameter(pName) != null)
			return pPageContext.getRequest().getParameter(pName);
		if (pPageContext.getAttribute(pName) != null)
			return pPageContext.getAttribute(pName);
		if (pPageContext.getRequest().getAttribute(pName) != null)
			return pPageContext.getRequest().getAttribute(pName);
		if (pPageContext.getSession().getAttribute(pName) != null)
			return pPageContext.getSession().getAttribute(pName);

		try {
			return (javax.servlet.jsp.PageContext.class.getMethod("get" + pName.substring(0, 1).toUpperCase() + pName.substring(1), new Class[0])).invoke(pPageContext, new Object[0]);
		}
		catch (java.lang.NoSuchMethodException e) {}
		catch (java.lang.reflect.InvocationTargetException e) {
			new GeneralException("PropertyUtils", "getAttribute(PageContext,String)", "Se ha producido la excepcion: " + e.getMessage());
		}
		catch (java.lang.IllegalAccessException e) {
			new GeneralException("PropertyUtils", "getAttribute(PageContext,String)", "Se ha producido la excepcion: " + e.getMessage());
		}

		return null;
	}

	public static Object getObjectValue(Object pObject, String pPropertyString) {
		if (pObject == null) return null;
		Object value = pObject;
		if (pPropertyString != null && pPropertyString.length() > 0) {
			StringTokenizer st = new StringTokenizer(pPropertyString, ".");
			while (st.hasMoreTokens() && value != null) {
				String propertyName = st.nextToken();
				Object result = null;
				try {
					result = getNextObject(value, propertyName);
				}
				catch (NoSuchMethodException e0) {
					if (st.hasMoreTokens()) {
						try {
							result = getNextObject(value, propertyName, st.nextToken());
						}
						catch (NoSuchMethodException e) {}
					}
				}
				value = result;
			}
		}
		return value;
	}

	public static boolean setObjectValue(Object pObject, String pPropertyString, Object pValue) {
		return setObjectValue(pObject, pPropertyString, pValue, null);
	}

	public static boolean setObjectValue(Object pObject, String pPropertyString, Object pValue, String pType) {
		if (pObject == null) return false;
		Object obj = pObject;
		if (pPropertyString != null && pPropertyString.length() > 0) {
			StringTokenizer st = new StringTokenizer(pPropertyString, ".");
			while (st.hasMoreTokens() && obj != null) {
				String propertyName = st.nextToken();
				if (st.hasMoreTokens()) {
					try {
						obj = getNextObject(obj, propertyName);
					}
					catch (NoSuchMethodException e) {}
				}
				else return setNextObjectValue(obj, pPropertyString, pValue, pType);
			}
		}
		return false;
	}

	private static Object getNextObject(Object pObject, String pPropertyName)
	throws NoSuchMethodException {
		if (pObject == null) return null;
		if (pObject instanceof javax.servlet.http.HttpSession) {
			return ((javax.servlet.http.HttpSession)pObject).getAttribute(pPropertyName);
		}
		// Try pObject.getPPropertyName();
		try {
			Class[] parameterClasses = new Class[0];
			Object[] parameterObjects = new Object[0];
			return (pObject.getClass().getMethod("get" + pPropertyName.substring(0,1).toUpperCase() + pPropertyName.substring(1), parameterClasses)).invoke(pObject, parameterObjects);
		}
		catch (java.lang.reflect.InvocationTargetException e) {
			new ExceptionErrorInterno("PropertyUtils", "Se ha producido la excepci�n: " + e.getMessage());			
		}
		catch (IllegalAccessException e) {
			new GeneralException("PropertyUtils", "getNextObject(Object,String)", "Se ha producido la excepci�n: " + e.getMessage());
		}
		catch (NoSuchMethodException e0) {
			// Try pObject.get(pPropertyName);
			try {
				Class[] parameterClasses = new Class[1];
				parameterClasses[0] = (new Object()).getClass();
				Object[] parameterObjects = new Object[1];
				parameterObjects[0] = pPropertyName;
				return (pObject.getClass().getMethod("get", parameterClasses)).invoke(pObject, parameterObjects);
			}
			catch (java.lang.reflect.InvocationTargetException e) {
				new GeneralException("PropertyUtils", "getNextObject(Object,String)", "Se ha producido la excepci�n: " + e.getMessage());
			}
			catch (IllegalAccessException e) {
				new GeneralException("PropertyUtils", "getNextObject(Object,String)", "Se ha producido la excepci�n: " + e.getMessage());
			}
			catch (NoSuchMethodException e1) {
				// Try pObject.getPPropertyName();
				try {
					Class[] parameterClasses = new Class[0];
					Object[] parameterObjects = new Object[0];
					return (pObject.getClass().getMethod("is" + pPropertyName.substring(0,1).toUpperCase() + pPropertyName.substring(1), parameterClasses)).invoke(pObject, parameterObjects);
				}
				catch (java.lang.reflect.InvocationTargetException e) {
					new GeneralException("PropertyUtils", "getNextObject(Object,String)", "Se ha producido la excepci�n: " + e.getMessage());
				}
				catch (IllegalAccessException e) {
					new GeneralException("PropertyUtils", "getNextObject(Object,String)", "Se ha producido la excepci�n: " + e.getMessage());
				}
			}
		}
		return null;
	}

	private static Object getNextObject(Object pObject, String pProperty1, String pProperty2)
	throws NoSuchMethodException {
		if (pObject == null) return null;
		// Try pObject.getPropertyName(nextPropertyName);
		try {
			Class[] parameterClasses = new Class[1];
			parameterClasses[0] = pProperty2.getClass();
			Object[] parameterObjects = new Object[1];
			parameterObjects[0] = pProperty2;
			return (pObject.getClass().getMethod("get" + pProperty1.substring(0, 1).toUpperCase() + pProperty1.substring(1), parameterClasses)).invoke(pObject, parameterObjects);
		}
		catch (java.lang.reflect.InvocationTargetException e) {
			new GeneralException("PropertyUtils", "getNextObject(Object,String,String)", "Se ha producido la excepci�n: " + e.getMessage());			
		}
		catch (IllegalAccessException e) {
		}

		return null;
	}

	private static Method findSetMethod(Class pInvokingClass, String pMethodName, Object pValue, String pType, Class[] pParameterClasses) {
		try {
			if (pParameterClasses == null) pParameterClasses = new Class[0];
			Class[] parameterClasses = new Class[pParameterClasses.length + 1];
			for (int i=0; i<pParameterClasses.length; i++) parameterClasses[i] = pParameterClasses[i];

			Class valueClass = (new Object()).getClass();
			if (pValue != null) valueClass = pValue.getClass();
			else if (pType != null) valueClass = Class.forName(pType);
			else valueClass = null;

			if (valueClass != null) {
				while (valueClass != null) {
					parameterClasses[parameterClasses.length - 1] = valueClass;
					valueClass = valueClass.getSuperclass();
					try{
						return pInvokingClass.getMethod(pMethodName, parameterClasses);
					}
					catch (NoSuchMethodException e) {}
				}
			}
			else {
				Method[] methods = pInvokingClass.getMethods();
				for (int i=0; i<methods.length; i++) {
					if (methods[i].getName().equals(pMethodName)) {
						return methods[i];
					}
				}
			}
		}
		catch (ClassNotFoundException e) {
			new GeneralException("PropertyUtils", "findSetMethod(Class,String,Object,String,Class[])", "Se ha producido la excepci�n: " + e.getMessage());
		}
		return null;
	}

	private static boolean setNextObjectValue(Object pObject, String pPropertyName, Object pValue, String pType) {
		if (pObject == null) return false;
		// Try pObject.setPPropertyName(pValue);
		String setPPropertyNameMethodName = "set" + pPropertyName.substring(0,1).toUpperCase() + pPropertyName.substring(1);
		Method setPPropertyNameMethod = findSetMethod(pObject.getClass(), setPPropertyNameMethodName, pValue, pType, null);
		if (setPPropertyNameMethod != null) {
			Object[] parameterObjects = new Object[1];
			parameterObjects[0] = pValue;
			try {
				setPPropertyNameMethod.invoke(pObject, parameterObjects);
			}
			catch (java.lang.reflect.InvocationTargetException e) {
				
				new GeneralException("PropertyUtils", "setNextObjectValue(Object,String,Object,String)", "Se ha producido la excepci�n: " + e.getMessage());
			}
			catch (IllegalAccessException e) {
				new GeneralException("PropertyUtils", "setNextObjectValue(Object,String,Object,String)", "Se ha producido la excepci�n: " + e.getMessage());				
			}
			return true;
		}
		else {
			Class[] parameterClasses = new Class[1];
			parameterClasses[0] = (pPropertyName == null)?(new Object()).getClass():pPropertyName.getClass();
			if (setPPropertyNameMethod != null) {
				Object[] parameterObjects = new Object[2];
				parameterObjects[0] = pPropertyName;
				parameterObjects[1] = pValue;
				try {
					setPPropertyNameMethod.invoke(pObject, parameterObjects);
				}
				catch (java.lang.reflect.InvocationTargetException e) {
					new GeneralException("PropertyUtils", "setNextObjectValue(Object,String,Object,String))", "Se ha producido la excepci�n: " + e.getMessage());					
				}
				catch (IllegalAccessException e) {
					new GeneralException("PropertyUtils", "setNextObjectValue(Object,String,Object,String)", "Se ha producido la excepci�n: " + e.getMessage());					
				}
				return true;
			}
		}
		return false;
	}

/************************************************************************** **/
/*****************************************************************************/

}