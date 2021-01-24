package org.mcnative.licensing.context.platform;

import org.mcnative.licensing.LicenseVerifier;
import org.mcnative.licensing.ReportingService;
import org.mcnative.licensing.context.LicenseContext;
import org.mcnative.licensing.context.LicenseContextBuilder;
import org.mcnative.licensing.utils.LicenseUtil;
import org.mcnative.runtime.api.plugin.MinecraftPlugin;

import java.io.File;

/**
 * McNative licensing integration into McNative Runtime Plugins
 *
 * See @{@link LicenseVerifier} and @{@link ReportingService} for the method documentations
 */
public class McNativeLicenseIntegration {

    public static LicenseContext newContext(MinecraftPlugin plugin, String resourceId, String publicKey){
        LicenseContextBuilder builder = LicenseContext.newContext();

        builder.licenseDatLocation(new File(plugin.getDataFolder(),"license.dat"));

        File license = new File(plugin.getDataFolder(),"license.key");
        if(license.exists()) builder.licenseKey(LicenseUtil.readAllText(license));

        builder.resourceId(resourceId).publicKey(publicKey);

        return builder.create();
    }

}
