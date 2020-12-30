package org.mcnative.licensing.utils;

import org.mcnative.licensing.ServerInfo;
import org.mcnative.runtime.api.McNative;
import org.mcnative.runtime.api.McNativeConsoleCredentials;
import org.mcnative.runtime.api.rollout.RolloutConfiguration;
import org.mcnative.runtime.api.rollout.RolloutProfile;

import java.io.File;

public class McNativeServerInfoUtil {

    public static ServerInfo getDefaultServerInfo(File basePath,String name){
        RolloutConfiguration configuration = McNative.getInstance().getRolloutConfiguration();
        RolloutProfile profile = configuration.getProfile(name);
        McNativeConsoleCredentials identifier = McNative.getInstance().getConsoleCredentials();
        return new ServerInfo(new File(basePath,"license.dat")
                ,"https://"+profile.getServer()+"/v1/licenses/{resourceId}/checkout"
                ,identifier.getNetworkId(),identifier.getSecret());
    }

}
