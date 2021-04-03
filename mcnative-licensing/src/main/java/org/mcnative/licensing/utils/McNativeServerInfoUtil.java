package org.mcnative.licensing.utils;

import org.mcnative.licensing.context.LicenseContextBuilder;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.McNativeConsoleCredentials;
import org.mcnative.runtime.api.loader.LoaderConfiguration;

public class McNativeServerInfoUtil {

    public static void initializeContext(LicenseContextBuilder context){
        LoaderConfiguration configuration = McNative.getInstance().getRolloutConfiguration();

        String licenseServer = "https://" + configuration.getEndpoint() + "/v1/licenses/{resourceId}/checkout";
        context.licenseServer(licenseServer);

        McNativeConsoleCredentials identifier = McNative.getInstance().getConsoleCredentials();
        if(identifier != null){
            context.networkId(identifier.getNetworkId());
            context.networkSecret(identifier.getSecret());
        }
    }

}
