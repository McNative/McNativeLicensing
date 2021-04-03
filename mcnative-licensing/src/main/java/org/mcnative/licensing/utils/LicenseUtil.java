package org.mcnative.licensing.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Enumeration;

//Copied methods from libraries
public class LicenseUtil {

    public static String readAllText(File file) {
        return readAllText(file, StandardCharsets.UTF_8);
    }

    public static String readAllText(File file, Charset charset) {
        try {
            return readAllText(Files.newInputStream(file.toPath()), charset);
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static String readAllText(InputStream stream, Charset charset) {
        if (!Charset.isSupported(charset.name())) {
            throw new UnsupportedOperationException("Charset " + charset.name() + " is not supported.");
        } else {
            try {
                byte[] content = new byte[stream.available()];
                stream.read(content);
                return new String(content, charset);
            } catch (IOException var3) {
                throw new RuntimeException(var3);
            }
        }
    }

    public static String getDeviceId() {
        try {
            ByteBuf buffer = Unpooled.directBuffer();
            Enumeration<?> interfaces = NetworkInterface.getNetworkInterfaces();

            while(interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface)interfaces.nextElement();
                if (networkInterface.getHardwareAddress() != null) {
                    buffer.writeBytes(networkInterface.getHardwareAddress());
                }
            }

            byte[] result = new byte[buffer.readableBytes()];
            buffer.readBytes(result);
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

}
