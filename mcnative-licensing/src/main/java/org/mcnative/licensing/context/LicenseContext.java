package org.mcnative.licensing.context;

import org.mcnative.licensing.LicenseVerifier;
import org.mcnative.licensing.McNativeRuntime;
import org.mcnative.licensing.ReportingService;

import java.io.File;

/**
 * This object contains information about the server credentials (Id and secret) and license location (local and remove location).
 */
public class LicenseContext {

    private final String resourceId;
    private final String publicKey;

    private final String licenseServer;
    private final File licenseDatLocation;

    //Console credentials
    private final String networkId;
    private final String networkSecret;

    private final String licenseKey;

    public LicenseContext(String resourceId, String publicKey, String licenseServer, File licenseDatLocation, String networkId, String networkSecret, String licenseKey) {
        this.resourceId = resourceId;
        this.publicKey = publicKey;
        this.licenseServer = licenseServer;
        this.licenseDatLocation = licenseDatLocation;
        this.networkId = networkId;
        this.networkSecret = networkSecret;
        this.licenseKey = licenseKey;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getLicenseServer() {
        return licenseServer;
    }

    public File getLicenseDatLocation() {
        return licenseDatLocation;
    }

    public String getNetworkId() {
        return networkId;
    }

    public String getNetworkSecret() {
        return networkSecret;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void verifyOrCheckout(){
        LicenseVerifier.verifyOrCheckout(this);
    }

    public void verify(){
        LicenseVerifier.verify(this);
    }

    public void startReportingService(Class<?> ownerClass){
        ReportingService.start(ownerClass,this);
    }

    public static LicenseContextBuilder newContext(){
        LicenseContextBuilder builder = new LicenseContextBuilder();
        McNativeRuntime.initializeContext(builder);
        return builder;
    }

}
