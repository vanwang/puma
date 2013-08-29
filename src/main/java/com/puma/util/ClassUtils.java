package com.puma.util;

public class ClassUtils {

	public static boolean isInterface(Class c, String szInterface)
    {
            Class[] face = c.getInterfaces();
            for (int i = 0, j = face.length; i < j; i++) 
            {
                    if(face[i].getName().equals(szInterface))
                    {
                            return true;
                    }
                    else
                    { 
                            Class[] face1 = face[i].getInterfaces();
                            for(int x = 0; x < face1.length; x++)
                            {
                                    if(face1[x].getName().equals(szInterface))
                                    {
                                            return true;
                                    }
                                    else if(isInterface(face1[x], szInterface))
                                    {
                                            return true;
                                    }
                            }
                    }
            }
            if (null != c.getSuperclass())
            {
                    return isInterface(c.getSuperclass(), szInterface);
            }
            return false;
    }
	
}
