package org.mcnative.licensing.platform;

import org.mcnative.licensing.License;
import org.mcnative.licensing.LicenseVerifier;
import org.mcnative.licensing.ReportingService;
import org.mcnative.licensing.ServerInfo;
import org.mcnative.licensing.utils.McNativeServerInfoUtil;
import org.mcnative.runtime.api.plugin.MinecraftPlugin;

/**
 * McNative licensing integration into McNative Runtime Plugins
 *
 * See @{@link LicenseVerifier} and @{@link ReportingService} for the method documentations
 */
public class McNativeIntegration {

    public static License verify(MinecraftPlugin plugin, String resourceId, String publicKey){
        ServerInfo serverInfo = McNativeServerInfoUtil.getDefaultServerInfo(plugin.getDataFolder(),plugin.getName());
        return LicenseVerifier.verify(resourceId,publicKey,serverInfo);
    }

    public static License verifyOrCheckout(MinecraftPlugin plugin, String resourceId, String publicKey){
        ServerInfo serverInfo = McNativeServerInfoUtil.getDefaultServerInfo(plugin.getDataFolder(),plugin.getName());
        return LicenseVerifier.verifyOrCheckout(resourceId,publicKey,serverInfo);
    }

    public static void startReportingService(MinecraftPlugin plugin, String resourceId){
        ServerInfo serverInfo = McNativeServerInfoUtil.getDefaultServerInfo(plugin.getDataFolder(),plugin.getDescription().getName());
        ReportingService.start(plugin.getClass(),resourceId,serverInfo);
    }

}
