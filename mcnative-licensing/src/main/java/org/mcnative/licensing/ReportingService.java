package org.mcnative.licensing;

import org.mcnative.licensing.context.LicenseContext;
import org.mcnative.licensing.utils.LicenseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * The Reporting Service is a part of the McNative Premium subscription model, it is used to measure plugin
 * usage and uptime. The services send a request to the McNative mirror every 10 minutes to confirm that it is still running.
 */
public class ReportingService {

    private static final String REPORTING_ENDPOINT = "https://mirror.mcnative.org/v1/licenses/{resourceId}/alive";
    private static final Thread SERVICE_THREAD = new ServiceThread();
    private static final long THREAD_SLEEP_TIME = TimeUnit.MINUTES.toMillis(10);
    private static final Map<Class<?>,LicenseContext> ATTACHED_SERVICES = new ConcurrentHashMap<>();

    /**
     * Start and attach a new configuration to the service thread.
     * Every 10 minutes a request is sent to the McNative mirror server.
     *
     * @param ownerClass The owner class (Usually plugin main class)
     */
    public static void start(Class<?> ownerClass,LicenseContext context){
        Objects.requireNonNull(ownerClass);
        ATTACHED_SERVICES.put(ownerClass,context);
        if(!SERVICE_THREAD.isAlive()){
            SERVICE_THREAD.start();
        }
    }

    /**
     * Stop a running service
     *
     * @param ownerClass The owner class (Usually plugin main class)
     */
    public static void stop(Class<?> ownerClass){
        Objects.requireNonNull(ownerClass);
        ATTACHED_SERVICES.remove(ownerClass);
        if(ATTACHED_SERVICES.size() == 0){
            SERVICE_THREAD.interrupt();
        }
    }

    /**
     * Send a single lifetime request to the McNative mirror server.
     *
     * @param context Information about the server
     */
    public static void sendLifetimeRequest(LicenseContext context){
        Objects.requireNonNull(context);
        try {
            URLConnection connection = new URL(REPORTING_ENDPOINT.replace("{resourceId}",context.getResourceId())).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            connection.setRequestProperty("DeviceId", LicenseUtil.getDeviceId());
            connection.setRequestProperty("NetworkId",context.getNetworkId());
            connection.setRequestProperty("NetworkSecret",context.getNetworkSecret());

            InputStream response = connection.getInputStream();
            response.close();
        } catch (IOException ignored) {}
    }

    private static class ServiceThread extends Thread{

        public ServiceThread(){
            super("Plugin lifetime reporting service");
            setDaemon(true);
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                for (LicenseContext context : ATTACHED_SERVICES.values()) {
                    ReportingService.sendLifetimeRequest(context);
                }
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (InterruptedException ignored) {}
            }
        }
    }

}
