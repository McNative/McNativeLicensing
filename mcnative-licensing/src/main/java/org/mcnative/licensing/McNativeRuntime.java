package org.mcnative.licensing;

import org.mcnative.licensing.context.LicenseContextBuilder;
import org.mcnative.licensing.utils.McNativeServerInfoUtil;

import java.lang.reflect.Method;

public class McNativeRuntime {

    /**
     * Check if McNative is installed and available on this server.
     *
     * @return True if McNative is operational
     */
    public static boolean isAvailable(){
        try{
            Class<?> clazz = Class.forName("org.mcnative.runtime.api.McNative");
            Method isAvailable = clazz.getDeclaredMethod("isAvailable");
            if((boolean)isAvailable.invoke(null)) return true;
        }catch (Exception ignored){}
        return false;
    }

    public static void initializeContext(LicenseContextBuilder contextBuilder) {
        if(isAvailable()) McNativeServerInfoUtil.initializeContext(contextBuilder);
    }

}
