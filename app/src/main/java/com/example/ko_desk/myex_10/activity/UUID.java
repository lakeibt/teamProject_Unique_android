package com.example.ko_desk.myex_10.activity;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

public class UUID {
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return getUniqueID(context);
    }

    @SuppressLint("HardwareIds")
    private static String getUniqueID(Context context) {

        String telephonyDeviceId = "NoTelephonyId";
        String androidDeviceId = "NoAndroidId";

        // get telephony id
        try {
            final TelephonyManager tm =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyDeviceId = tm.getDeviceId();
            if (telephonyDeviceId == null) {
                telephonyDeviceId = "NoTelephonyId";
            }
        } catch (Exception ignored) {

        }

        // get internal android device id
        try {
            androidDeviceId =
                    android.provider.Settings.Secure.getString(context.getContentResolver(),
                            android.provider.Settings.Secure.ANDROID_ID);
            if (androidDeviceId == null) {
                androidDeviceId = "NoAndroidId";
            }
        } catch (Exception ignored) {

        }

        // build up the uuid
        try {
            String id =
                    getStringIntegerHexBlocks(androidDeviceId.hashCode()) + "-"
                            + getStringIntegerHexBlocks(telephonyDeviceId.hashCode());

            return id;
        } catch (Exception e) {
            return "0000-0000-1111-1111";
        }
    }

    public static String getStringIntegerHexBlocks(int value) {
        StringBuilder result = new StringBuilder();
        String string = Integer.toHexString(value);

        int remain = 8 - string.length();
        char[] chars = new char[remain];
        Arrays.fill(chars, '0');
        string = new String(chars) + string;

        int count = 0;
        for (int i = string.length() - 1; i >= 0; i--) {
            count++;
            result.insert(0, string.substring(i, i + 1));
            if (count == 4) {
                result.insert(0, "-");
                count = 0;
            }
        }

        if (result.toString().startsWith("-")) {
            result = new StringBuilder(result.substring(1, result.length()));
        }
        return result.toString();
    }
}



