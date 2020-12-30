package org.mcnative.licensing.platform;

import net.md_5.bungee.api.plugin.Plugin;
import org.mcnative.licensing.*;
import org.mcnative.licensing.utils.McNativeServerInfoUtil;

/**
 * McNative licensing integration into BungeeCord
 *
 * See @{@link LicenseVerifier} and @{@link ReportingService} for the method documentations
 */
public class BungeeIntegration {

    public static License verify(Plugin plugin, String resourceId, String publicKey){
        checkMcNativeAvailable();
        ServerInfo serverInfo = McNativeServerInfoUtil.getDefaultServerInfo(plugin.getDataFolder(),plugin.getDescription().getName());
        return LicenseVerifier.verify(resourceId,publicKey,serverInfo);
    }

    public static License verifyOrCheckout(Plugin plugin, String resourceId, String publicKey){
        checkMcNativeAvailable();
        ServerInfo serverInfo = McNativeServerInfoUtil.getDefaultServerInfo(plugin.getDataFolder(),plugin.getDescription().getName());
        return LicenseVerifier.verifyOrCheckout(resourceId,publicKey,serverInfo);
    }

    public static void startReportingService(Plugin plugin, String resourceId){
        checkMcNativeAvailable();
        ServerInfo serverInfo = McNativeServerInfoUtil.getDefaultServerInfo(plugin.getDataFolder(),plugin.getDescription().getName());
        ReportingService.start(plugin.getClass(),resourceId,serverInfo);
    }

    public static boolean isMcNativeAvailable(){
        return McNativeRuntime.isAvailable();
    }

    private static void checkMcNativeAvailable(){
        if(!McNativeRuntime.isAvailable()){
            throw new IllegalArgumentException("McNative is not available (To run the plugin without McNative use the LicenseVerifier class)");
        }
    }

}
