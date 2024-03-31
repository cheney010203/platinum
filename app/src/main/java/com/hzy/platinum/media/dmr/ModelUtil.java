package com.hzy.platinum.media.dmr;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;

public class ModelUtil {

    /**
     * True if this class is executing on an Android runtime
     */
    final public static boolean ANDROID_RUNTIME;
    static {
        boolean foundAndroid = false;
        try {
            Class androidBuild = Thread.currentThread().getContextClassLoader().loadClass("android.os.Build");
            foundAndroid = androidBuild.getField("ID").get(null) != null;
        } catch (Exception ex) {
            // Ignore
        }
        ANDROID_RUNTIME = foundAndroid;
    }

    /**
     * True if this class is executing on an Android emulator runtime.
     */
    final public static boolean ANDROID_EMULATOR;
    static {
        boolean foundEmulator = false;
        try {
            Class androidBuild = Thread.currentThread().getContextClassLoader().loadClass("android.os.Build");
            String product = (String)androidBuild.getField("PRODUCT").get(null);
            if ("google_sdk".equals(product) || ("sdk".equals(product)))
                foundEmulator = true;
        } catch (Exception ex) {
            // Ignore
        }
        ANDROID_EMULATOR = foundEmulator;
    }



    /**
     * Converts the comma-separated elements of a string into an array of strings,
     * unescaping backslashed commas.
     */
    public static String[] fromCommaSeparatedList(String s) {
        return fromCommaSeparatedList(s, true);
    }

    /**
     * Converts the comma-separated elements of a string into an array of strings,
     * optionally unescaping backslashed commas.
     */
    public static String[] fromCommaSeparatedList(String s, boolean unescapeCommas) {
        if (s == null || s.length() == 0) {
            return null;
        }

        final String QUOTED_COMMA_PLACEHOLDER = "XXX1122334455XXX";
        if (unescapeCommas) {
            s = s.replaceAll("\\\\,", QUOTED_COMMA_PLACEHOLDER);
        }

        String[] split = s.split(",");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].replaceAll(QUOTED_COMMA_PLACEHOLDER, ",");
            split[i] = split[i].replaceAll("\\\\\\\\", "\\\\");
        }
        return split;
    }

    /**
     * @param seconds The number of seconds to convert.
     * @return A string representing hours, minutes, seconds, e.g. <code>11:23:44</code>
     */
    public static String toTimeString(long seconds) {
        long hours = seconds / 3600,
                remainder = seconds % 3600,
                minutes = remainder / 60,
                secs = remainder % 60;

        return ((hours < 10 ? "0" : "") + hours
                + ":" + (minutes < 10 ? "0" : "") + minutes
                + ":" + (secs < 10 ? "0" : "") + secs);
    }

    /**
     * @param s A string representing hours, minutes, seconds, e.g. <code>11:23:44</code>
     * @return The converted number of seconds.
     */
    public static long fromTimeString(String s) {
        // Handle "00:00:00.000" pattern, drop the milliseconds
        if (s.lastIndexOf(".") != -1)
            s = s.substring(0, s.lastIndexOf("."));
        String[] split = s.split(":");
        if (split.length != 3)
            throw new IllegalArgumentException("Can't parse time string: " + s);
        return (Long.parseLong(split[0]) * 3600) +
                (Long.parseLong(split[1]) * 60) +
                (Long.parseLong(split[2]));
    }

    /**
     * @param s A string with commas.
     * @return The same string, a newline appended after every comma.
     */
    public static String commaToNewline(String s) {
        StringBuilder sb = new StringBuilder();
        String[] split = s.split(",");
        for (String splitString : split) {
            sb.append(splitString).append(",").append("\n");
        }
        if (sb.length() > 2) {
            sb.deleteCharAt(sb.length() - 2);
        }
        return sb.toString();
    }

    /**
     * DNS reverse name lookup.
     *
     * @param includeDomain <code>true</code> if the whole FQDN should be returned, instead of just the first (host) part.
     * @return The resolved host (and domain-) name, or "UNKNOWN HOST" if resolution failed.
     */
    public static String getLocalHostName(boolean includeDomain) {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            return includeDomain
                    ? hostname
                    : hostname.indexOf(".") != -1 ? hostname.substring(0, hostname.indexOf(".")) : hostname;

        } catch (Exception ex) {
            // Return a dummy String
            return "UNKNOWN HOST";
        }
    }

    /**
     * @return The MAC hardware address of the first network interface of this host.
     */
    public static byte[] getFirstNetworkInterfaceHardwareAddress() {
        try {
            Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface iface : Collections.list(interfaceEnumeration)) {
                if (!iface.isLoopback() && iface.isUp() && iface.getHardwareAddress() != null) {
                    return iface.getHardwareAddress();
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not discover first network interface hardware address");
        }
        throw new RuntimeException("Could not discover first network interface hardware address");
    }

}
