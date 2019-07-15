package com.google.android.settings.external;

import android.text.TextUtils;
import com.android.internal.util.ArrayUtils;
import android.os.Build;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.Context;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import android.util.Log;
import java.security.MessageDigest;

public class SignatureVerifier
{
    private static final byte[] DEBUG_DIGEST_GMSCORE;
    private static final byte[] DEBUG_DIGEST_TIPS;
    private static final byte[] RELEASE_DIGEST_GMSCORE;
    private static final byte[] RELEASE_DIGEST_TIPS;
    private static final boolean IS_DEBUGGABLE = true;
    static {
        DEBUG_DIGEST_GMSCORE = new byte[] { 25, 117, -78, -15, 113, 119, -68, -119, -91, -33, -13, 31, -98, 100, -90, -54, -30, -127, -91, 61, -63, -47, -43, -101, 29, 20, 127, -31, -56, 42, -6, 0 };
        RELEASE_DIGEST_GMSCORE = new byte[] { -16, -3, 108, 91, 65, 15, 37, -53, 37, -61, -75, 51, 70, -56, -105, 47, -82, 48, -8, -18, 116, 17, -33, -111, 4, -128, -83, 107, 45, 96, -37, -125 };
        DEBUG_DIGEST_TIPS = new byte[] { -85, 24, -24, 44, 97, -94, -43, -117, -41, 24, 20, 119, -68, -97, 117, -88, 33, 77, 23, 98, 115, -112, 37, -84, 36, -111, 9, 20, 17, -72, 79, -77 };
        RELEASE_DIGEST_TIPS = new byte[] { 14, 68, 121, -2, 25, 61, 1, -51, 70, 33, 95, -52, -48, -39, 35, 61, -20, 119, -2, -94, 89, -5, -52, -97, 9, 33, 25, -11, 10, -125, 114, -27 };
    }
    
    private static byte[] getDigestBytes(final String s, final boolean b) {
        int n = 0;
        Label_0028: {
            if (s.hashCode() == 40935373) {
                if (s.equals("com.google.android.apps.tips")) {
                    n = 0;
                    break Label_0028;
                }
            }
            n = -1;
        }
        if (n != 0) {
            byte[] array;
            if (b) {
                array = SignatureVerifier.DEBUG_DIGEST_GMSCORE;
            }
            else {
                array = SignatureVerifier.RELEASE_DIGEST_GMSCORE;
            }
            return array;
        }
        byte[] array2;
        if (b) {
            array2 = SignatureVerifier.DEBUG_DIGEST_TIPS;
        }
        else {
            array2 = SignatureVerifier.RELEASE_DIGEST_TIPS;
        }
        return array2;
    }
    
    private static boolean isCertWhitelisted(final String s, final byte[] array, final boolean b) {
        try {
            final byte[] digest = MessageDigest.getInstance("SHA-256").digest(array);
            if (Log.isLoggable("SignatureVerifier", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Checking cert for ");
                String s2;
                if (b) {
                    s2 = "debug";
                }
                else {
                    s2 = "release";
                }
                sb.append(s2);
                Log.d("SignatureVerifier", sb.toString());
            }
            return Arrays.equals(digest, getDigestBytes(s, b));
        }
        catch (NoSuchAlgorithmException ex) {
            throw new SecurityException("Failed to obtain SHA-256 digest impl.", ex);
        }
    }
    
    public static boolean isPackageWhitelisted(final Context context, final String s) {
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(s, 64);
            final String packageName = packageInfo.packageName;
            if (!verifyWhitelistedPackage(packageName)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Package name: ");
                sb.append(packageName);
                sb.append(" is not whitelisted.");
                Log.e("SignatureVerifier", sb.toString());
                return false;
            }
            return isSignatureWhitelisted(packageInfo);
        }
        catch (PackageManager.NameNotFoundException ex) {
            Log.e("SignatureVerifier", "Could not find package name.", (Throwable)ex);
            return false;
        }
    }
    
    private static boolean isSignatureWhitelisted(final PackageInfo packageInfo) {
        if (packageInfo.signatures.length != 1) {
            Log.w("SignatureVerifier", "Package has more than one signature.");
            return false;
        }
        return isCertWhitelisted(packageInfo.packageName, packageInfo.signatures[0].toByteArray(), IS_DEBUGGABLE);
    }
    
    private static String isUidWhitelisted(final Context context, int i) {
        final String[] packagesForUid = context.getPackageManager().getPackagesForUid(i);
        if (ArrayUtils.isEmpty((Object[])packagesForUid)) {
            return null;
        }
        int length;
        String s;
        for (length = packagesForUid.length, i = 0; i < length; ++i) {
            s = packagesForUid[i];
            if (isPackageWhitelisted(context, s)) {
                return s;
            }
        }
        return null;
    }
    
    public static String verifyCallerIsWhitelisted(final Context context, final int n) throws SecurityException {
        final String uidWhitelisted = isUidWhitelisted(context, n);
        if (!TextUtils.isEmpty((CharSequence)uidWhitelisted)) {
            return uidWhitelisted;
        }
        throw new SecurityException("UID is not Google Signed");
    }
    
    private static boolean verifyWhitelistedPackage(final String s) {
        return "com.google.android.googlequicksearchbox".equals(s) || "com.google.android.gms".equals(s) || "com.google.android.apps.tips".equals(s) || (IS_DEBUGGABLE && "com.google.android.settings.api.tester".equals(s));
    }
}
