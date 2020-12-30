package org.mcnative.licensing.platform;

import org.bukkit.plugin.Plugin;
import org.mcnative.licensing.*;
import org.mcnative.licensing.utils.McNativeServerInfoUtil;

/**
 * McNative licensing integration into Bukkit
 *
 * See @{@link LicenseVerifier} and @{@link ReportingService} for the method documentations
 */
public class BukkitIntegration {

    public static License verifyLicense(Plugin plugin, String resourceId, String publicKey){
        checkMcNativeAvailable();
        ServerInfo serverInfo = McNativeServerInfoUtil.getDefaultServerInfo(plugin.getDataFolder(),plugin.getName());
        return LicenseVerifier.verify(resourceId,publicKey,serverInfo);
    }

    public static License verifyOrCheckoutLicense(Plugin plugin, String resourceId, String publicKey){
        checkMcNativeAvailable();
        ServerInfo serverInfo = McNativeServerInfoUtil.getDefaultServerInfo(plugin.getDataFolder(),plugin.getName());
        return LicenseVerifier.verifyOrCheckout(resourceId,publicKey,serverInfo);
    }

    public static void startReportingService(Plugin plugin, String resourceId){
        checkMcNativeAvailable();
        ServerInfo serverInfo = McNativeServerInfoUtil.getDefaultServerInfo(plugin.getDataFolder(),plugin.getName());
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
