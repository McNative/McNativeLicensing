package org.mcnative.licensing;

import org.mcnative.licensing.context.LicenseContext;
import org.mcnative.licensing.exceptions.CloudNotCheckoutLicenseException;
import org.mcnative.licensing.exceptions.LicenseNotValidException;
import org.mcnative.licensing.utils.LicenseUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

public class LicenseVerifier {

    /**
     * Read and verify a license.
     *
     * @param context The license context
     * @return The verified license details
     */
    public static License verify(LicenseContext context){
        Objects.requireNonNull(context.getLicenseDatLocation());
        try{
            License license = License.read(context.getLicenseDatLocation());
            if(!license.verify(context.getResourceId(),context.getPublicKey())) throw new LicenseNotValidException("Invalid license");
            return license;
        }catch (Exception exception){
            throw new LicenseNotValidException(exception);
        }
    }

    /**
     * Read and check a license, if the license is not available, a new license is fetched from the license server.
     *
     * @param context The license context
     * @return The verified license details
     */
    public static License verifyOrCheckout(LicenseContext context){
        try{
            License license = null;
            if(context.getLicenseDatLocation().exists()){
                license = License.read(context.getLicenseDatLocation());
                if(license.shouldRefresh()){
                    try {
                        license = checkout(context);
                    }catch (IllegalArgumentException ignored){}
                }
            }
            if(license == null) license = checkout(context);
            if(!license.verify(context.getResourceId(),context.getPublicKey())) throw new LicenseNotValidException("Invalid license");
            return license;
        }catch (Exception exception){
            throw new LicenseNotValidException(exception);
        }
    }

    private static License checkout(LicenseContext context){
        if(context.getLicenseKey() == null && (context.getNetworkId() == null || context.getNetworkSecret() == null)){
            throw new LicenseNotValidException("Missing authorization data (Provide a license key or McNative console credentials)");
        }

        CertificateValidation.disable();
        try {
            HttpURLConnection connection = (HttpURLConnection)new URL(context.getLicenseServer().replace("{resourceId}",context.getResourceId())).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Accept-Charset", "UTF-8");

            connection.setRequestProperty("DeviceId", LicenseUtil.getDeviceId());

            if(context.getLicenseKey() != null){
                connection.setRequestProperty("LicenseKey",context.getLicenseKey());
            }else{
                connection.setRequestProperty("NetworkId",context.getNetworkId());
                connection.setRequestProperty("NetworkSecret",context.getNetworkSecret());
            }

            if(connection.getResponseCode() != 200){
                InputStream response = connection.getErrorStream();
                String content;
                try (Scanner scanner = new Scanner(response)) {
                    content = scanner.useDelimiter("\\A").next();
                }
                if(connection.getResponseCode() == 500){
                    throw new IllegalArgumentException("("+connection.getResponseCode()+") "+content);
                }else{
                    throw new CloudNotCheckoutLicenseException("("+connection.getResponseCode()+") "+content);
                }
            }

            InputStream response = connection.getInputStream();

            String content;
            try (Scanner scanner = new Scanner(response)) {
                content = scanner.useDelimiter("\\A").next();
            }

            License license = License.read(content);
            license.save(context.getLicenseDatLocation());
            response.close();
            return license;
        } catch (IOException e) {
            throw new IllegalArgumentException("Connection failed",e);
        }finally {
            CertificateValidation.reset();
        }
    }
}
