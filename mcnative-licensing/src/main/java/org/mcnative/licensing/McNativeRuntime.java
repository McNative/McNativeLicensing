package org.mcnative.licensing;

import java.lang.reflect.Method;

public class McNativeRuntime {

    /**
     * Check if McNative is installed and available on this server.
     *
     * @return True if McNative is operational
     */
    public static boolean isAvailable(){
        try{
            Class<?> clazz = Class.forName("org.mcnative.common.McNative");
            Method isAvailable = clazz.getDeclaredMethod("isAvailable");
            if((boolean)isAvailable.invoke(null)) return true;
        }catch (Exception ignored){}
        return false;
    }

}
