package org.mcnative.licensing.context;

import java.io.File;

public class LicenseContextBuilder {

    private String resourceId;
    private String publicKey;
    private String licenseServer = "https://mirror.mcnative.org/v1/licenses/{resourceId}/checkout";
    private File licenseDatLocation;
    private String networkId;
    private String networkSecret;
    private String licenseKey;

    protected LicenseContextBuilder() {}

    public LicenseContextBuilder resourceId(String resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public LicenseContextBuilder publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public LicenseContextBuilder licenseServer(String licenseServer) {
        this.licenseServer = licenseServer;
        return this;
    }

    public LicenseContextBuilder licenseDatLocation(File licenseDatLocation) {
        this.licenseDatLocation = licenseDatLocation;
        return this;
    }

    public LicenseContextBuilder networkId(String networkId) {
        this.networkId = networkId;
        return this;
    }

    public LicenseContextBuilder networkSecret(String networkSecret) {
        this.networkSecret = networkSecret;
        return this;
    }

    public LicenseContextBuilder licenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
        return this;
    }

    public LicenseContext create() {
        return new LicenseContext(resourceId, publicKey, licenseServer, licenseDatLocation, networkId, networkSecret, licenseKey);
    }
}
