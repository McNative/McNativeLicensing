package org.mcnative.licensing.context.platform;

import org.bukkit.plugin.Plugin;
import org.mcnative.licensing.*;
import org.mcnative.licensing.context.LicenseContext;
import org.mcnative.licensing.context.LicenseContextBuilder;
import org.mcnative.licensing.utils.LicenseUtil;

import java.io.File;

/**
 * McNative licensing integration into Bukkit
 *
 * See @{@link LicenseVerifier} and @{@link ReportingService} for the method documentations
 */
public class BukkitLicenseIntegration {

    public static LicenseContext newContext(Plugin plugin, String resourceId, String publicKey){
        LicenseContextBuilder builder = LicenseContext.newContext();

        builder.licenseDatLocation(new File(plugin.getDataFolder(),"license.dat"));

        File license = new File(plugin.getDataFolder(),"license.key");
        if(license.exists()) builder.licenseKey(LicenseUtil.readAllText(license));

        builder.resourceId(resourceId).publicKey(publicKey);

        return builder.create();
    }

}
