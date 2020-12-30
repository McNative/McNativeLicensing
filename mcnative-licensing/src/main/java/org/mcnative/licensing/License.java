/*
 * (C) Copyright 2020 The McNative Project (Davide Wietlisbach & Philipp Elvin Friedhoff)
 *
 * @author Davide Wietlisbach
 * @since 13.04.20, 20:37
 * @web %web%
 *
 * The McNative Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.mcnative.licensing;

import net.pretronic.libraries.utility.SystemInfo;
import net.pretronic.libraries.utility.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Properties;

/**
 * The license objects contains the extracted data from a license file.
 */
public class License {

    private final String raw;
    private final byte[] rawContent;
    private final byte[] signature;
    private final Properties properties;

    private License(String raw,byte[] rawContent, byte[] signature,Properties properties) {
        this.raw = raw;
        this.rawContent = rawContent;
        this.signature = signature;
        this.properties = properties;
    }

    public String getRaw() {
        return raw;
    }

    public byte[] getRawContent() {
        return rawContent;
    }

    public byte[] getSignature() {
        return signature;
    }

    public Properties getProperties() {
        return properties;
    }

    public String getId(){
        return this.properties.getProperty("Id");
    }

    public String getIssuer(){
        return this.properties.getProperty("Issuer");
    }

    public String getResourceId(){
        return this.properties.getProperty("ResourceId");
    }

    public String getDeviceId(){
        return this.properties.getProperty("DeviceId");
    }

    public long getExpiry(){
        return Long.parseLong(this.properties.getProperty("Expiry"));
    }

    public long getPreferredRefreshTime(){
        return Long.parseLong(this.properties.getProperty("PreferredRefreshTime"));
    }

    /**
     * Check if the license is expired
     *
     * @return True if the license is expired
     */
    public boolean isExpired(){
        return getExpiry() < System.currentTimeMillis();
    }

    /**
     * Check if the license should be refreshed
     *
     * @return True if the license should be refreshed
     */
    public boolean shouldRefresh(){
        return getPreferredRefreshTime() < System.currentTimeMillis();
    }

    /**
     * Verify if this license is valid
     *
     * @param resourceId The id of the resource for witch the license was issued
     * @param publicKey The public key to validate the signature
     * @return True if the license is valid
     * @throws GeneralSecurityException If the signature validation process fails
     */
    public boolean verify(String resourceId,String publicKey) throws GeneralSecurityException {
        Signature signatureTool = Signature.getInstance("SHA512WithRSA");
        signatureTool.initVerify(getPublicKeyFromString(publicKey));
        signatureTool.update(this.rawContent);

        return getResourceId().equals(resourceId)
                && getExpiry() > System.currentTimeMillis()
                && getDeviceId().equals(SystemInfo.getDeviceId())
                && signatureTool.verify(this.signature);
    }

    /**
     * Save the raw data of the license to a file
     *
     * @param file The destination file
     * @throws IOException If the file could not be written
     */
    public void save(File file) throws IOException{
        Files.write(file.toPath(), raw.getBytes(), StandardOpenOption.CREATE);
    }

    /**
     * Read the license from a file
     *
     * @param file The license source file
     * @return The license object
     * @throws IOException If the license could not be read
     */
    public static License read(File file) throws IOException{
        return read(FileUtil.readContent(file));
    }

    /**
     * Read the license from a string (BASE-64)
     *
     * @param content The base 64 string
     * @return The license object
     * @throws IOException If the license could not be read
     */
    public static License read(String content) throws IOException{
        String[] parts = content.split(";");
        if(parts.length != 2) throw new IllegalArgumentException("Invalid license content");

        byte[] rawContent = Base64.getDecoder().decode(parts[0].getBytes(StandardCharsets.UTF_8));
        byte[] signature = Base64.getDecoder().decode(parts[1].getBytes(StandardCharsets.UTF_8));

        Properties properties = new Properties();
        properties.load(new StringReader(new String(rawContent)));

        return new License(content,rawContent,signature,properties);
    }

    private static RSAPublicKey getPublicKeyFromString(String key) throws GeneralSecurityException {
        byte[] encoded = Base64.getDecoder().decode(key);
        KeyFactory factory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) factory.generatePublic(new X509EncodedKeySpec(encoded));
    }

}
