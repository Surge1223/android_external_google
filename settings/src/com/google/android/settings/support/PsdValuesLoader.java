package com.google.android.settings.support;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.net.wifi.WifiConfiguration;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.storage.VolumeInfo;
import android.os.storage.StorageManager;
import android.app.ActivityManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;

import java.util.Date;

import android.os.Build.VERSION;
import android.provider.Settings;
import android.location.LocationManager;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;

import java.util.Iterator;
import java.util.Arrays;
import java.util.ArrayList;

import android.os.SystemProperties;
import android.net.NetworkInfo;
import android.net.Network;
import android.net.NetworkInfo.State;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.android.settings.connecteddevice.usb.UsbBackend;

import android.content.ContentResolver;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;

import java.util.List;

import android.telephony.CellInfoWcdma;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;

import java.io.IOException;

import android.os.Debug;

import java.io.FileOutputStream;
import java.io.File;

import com.google.android.gsf.Gservices;

import android.content.Context;
import android.util.Log;

import java.util.Locale;

import android.os.UserManager;
import android.hardware.usb.UsbManager;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.CRC32;

import com.android.settingslib.utils.AsyncLoader;

public class PsdValuesLoader extends AsyncLoader<PsdBundle> {
    private static final SimpleDateFormat DATE_FORMATTER;
    private static final boolean DEBUG;
    static final String NOE_ALARM_MAX_VOLUME = "noe_alarm_max_volume";
    static final String NOE_BATTERY_LEVEL = "noe_battery_level";
    static final String NOE_BATTERY_SCALE = "noe_battery_scale";
    static final String NOE_BATTERY_STATUS = "noe_battery_status";
    static final String NOE_BRIGHTNESS_LEVEL = "noe_brightness_level";
    static final String NOE_CAMERA_VERSION_CODE = "noe_camera_version_code";
    static final String NOE_CAMERA_VERSION_NAME = "noe_camera_version_name";
    static final String NOE_CELLULAR_DBM = "noe_cellular_dbm";
    static final String NOE_CELLULAR_RADIO_TYPE = "noe_cellular_radio_type";
    static final String NOE_CELLULAR_STRENGTH = "noe_cellular_strength";
    static final String NOE_CHARGING_STATUS = "noe_charging_status";
    static final String NOE_DEVICE_ACTIVATION_TIME = "noe_device_activation_time";
    static final String NOE_IMEI = "noe_imei";
    static final String NOE_LOCATION_MODE = "noe_location_mode";
    static final String NOE_MUSIC_MAX_VOLUME = "noe_music_max_volume";
    static final String NOE_NOTIFICATION_MAX_VOLUME = "noe_notification_max_volume";
    static final String NOE_PREVIOUS_OS = "noe_previous_os";
    static final String NOE_RINGER_MAX_VOLUME = "noe_ringer_max_volume";
    static final String NOE_SYSTEM_MAX_VOLUME = "noe_system_max_volume";
    static final String NOE_USB_DATA_ROLE = "noe_usb_data_role";
    static final String NOE_USB_FUNCTIONS = "noe_usb_functions";
    static final String NOE_USB_POWER_ROLE = "noe_usb_power_role";
    static final String NOE_VOICE_CALL_MAX_VOLUME = "noe_voice_call_max_volume";
    static final String NOE_WEAR_VERSION_CODE = "noe_wear_version_code";
    static final String NOE_WEAR_VERSION_NAME = "noe_wear_version_name";
    static final String NOE_WIFI_AVAILABLE = "noe_wifi_available";
    static final String NOE_WIFI_CONNECTED = "noe_wifi_connected";
    static final String NOE_WIFI_SECURITY_KEY_VALID = "noe_wifi_security_key_valid";
    static final String NOE_WIFI_SPEED = "noe_wifi_speed";
    static final String NOE_WIFI_STRENGTH = "noe_wifi_strength";
    static UsbManager sUsbManager;
    static UserManager sUserManager;

    static {
        DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        DEBUG = Log.isLoggable("PsdValuesLoader", 3);
    }

    public PsdValuesLoader(Context context) {
        super(context);
    }

    protected static String dumpBatteryStats(final Context context) {
        if (!Gservices.getBoolean(context.getContentResolver(), "settingsgoogle:psd_battery_stats", false)) {
            if (PsdValuesLoader.DEBUG) {
                Log.d("PsdValuesLoader", "Not collecting battery_stats, skip.");
            }
            return null;
        }
        File file = null;
        File tempFile = null;
        while (true) {
            try {
                final File file2 = file = (tempFile = (file = (tempFile = File.createTempFile("batterystats", null, context.getFilesDir()))));
                final FileOutputStream fileOutputStream = new FileOutputStream(file2);
                tempFile = file2;
                file = file2;
                if (Debug.dumpService("batterystats", fileOutputStream.getFD(), new String[]{"-c"})) {
                    tempFile = file2;
                    file = file2;
                    fileOutputStream.close();
                    tempFile = file2;
                    file = file2;
                    final String file3 = readFile(file2);
                    if (file2 != null) {
                        file2.delete();
                    }
                    return file3;
                }
                tempFile = file2;
                file = file2;
                if (PsdValuesLoader.DEBUG) {
                    tempFile = file2;
                    file = file2;
                    Log.d("PsdValuesLoader", "Failed to dump battery stats.");
                }
                if (file2 != null) {
                    file2.delete();
                    return null;
                }
                return null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (tempFile != null) {
                    tempFile.delete();
                }
                final File file2 = file;
                continue;
            }
        }
    }

    private static String[] getCellularStrength(final TelephonyManager telephonyManager) {
        @SuppressLint("MissingPermission") final List<CellInfo> allCellInfo = telephonyManager.getAllCellInfo();
        final StringBuilder sb = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        final StringBuilder sb3 = new StringBuilder();
        if (allCellInfo == null) {
            return new String[]{"", "", ""};
        }
        for (int i = 0; i < allCellInfo.size(); ++i) {
            final CellInfo cellInfo = allCellInfo.get(i);
            int n = -1;
            int n2 = Integer.MIN_VALUE;
            sb.append(cellInfo.getClass().getSimpleName());
            sb.append(',');
            if (cellInfo instanceof CellInfoGsm) {
                final CellSignalStrengthGsm cellSignalStrength = ((CellInfoGsm) cellInfo).getCellSignalStrength();
                n = cellSignalStrength.getLevel();
                n2 = cellSignalStrength.getDbm();
            } else if (cellInfo instanceof CellInfoCdma) {
                final CellSignalStrengthCdma cellSignalStrength2 = ((CellInfoCdma) cellInfo).getCellSignalStrength();
                n = cellSignalStrength2.getLevel();
                n2 = cellSignalStrength2.getDbm();
            } else if (cellInfo instanceof CellInfoLte) {
                final CellSignalStrengthLte cellSignalStrength3 = ((CellInfoLte) cellInfo).getCellSignalStrength();
                n = cellSignalStrength3.getLevel();
                n2 = cellSignalStrength3.getDbm();
            } else if (cellInfo instanceof CellInfoWcdma) {
                final CellSignalStrengthWcdma cellSignalStrength4 = ((CellInfoWcdma) cellInfo).getCellSignalStrength();
                n = cellSignalStrength4.getLevel();
                n2 = cellSignalStrength4.getDbm();
            }
            sb2.append(n);
            sb2.append(',');
            sb3.append(n2);
            sb3.append(',');
        }
        if (allCellInfo.size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            sb2.deleteCharAt(sb2.length() - 1);
            sb3.deleteCharAt(sb3.length() - 1);
        }
        return new String[]{sb.toString(), sb2.toString(), sb3.toString()};
    }

    static long getDeviceAgeInDays(final ContentResolver contentResolver) {
        return roundToDays(System.currentTimeMillis() - Gservices.getLong(contentResolver, "device_registration_time", -1L));
    }

    private static String[] getUsbMode(final Context context) {
        UsbBackend usbBackend;
        if (PsdValuesLoader.sUserManager == null) {
            usbBackend = new UsbBackend(context);
        } else {
            usbBackend = new UsbBackend(context, PsdValuesLoader.sUserManager);
        }
        return new String[]{String.valueOf(usbBackend.getCurrentFunctions()), String.valueOf(usbBackend.getPowerRole()), String.valueOf(usbBackend.getDataRole())};
    }

    private static String[] getWifiExtras(final WifiManager wifiManager) {
        if (wifiManager != null) {
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                return new String[]{String.valueOf(connectionInfo.getLinkSpeed()), String.valueOf(connectionInfo.getRssi())};
            }
        }
        return new String[]{"", ""};
    }

    private static String[] getWifiStatus(final Context context) {
        int n = 0;
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            final Network[] allNetworks = connectivityManager.getAllNetworks();
            final int length = allNetworks.length;
            boolean b = false;
            boolean b2 = false;
            boolean b3;
            int n2;
            boolean b4;
            for (int i = 0; i < length; ++i, b2 = b3, n = n2, b = b4) {
                final Network network = allNetworks[i];
                final NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                b3 = b2;
                n2 = n;
                b4 = b;
                if (network != null) {
                    b3 = b2;
                    n2 = n;
                    b4 = b;
                    if (networkInfo.getType() == 1) {
                        final boolean b5 = true;
                        if (NetworkInfo.State.CONNECTED.equals((State) networkInfo.getState())) {
                            b = true;
                        }
                        b3 = b2;
                        n2 = (b5 ? 1 : 0);
                        b4 = b;
                        if (networkInfo.isAvailable()) {
                            b3 = true;
                            b4 = b;
                            n2 = (b5 ? 1 : 0);
                        }
                    }
                }
            }
            if (n != 0) {
                return new String[]{String.valueOf(b2), String.valueOf(b)};
            }
        }
        return new String[]{"", ""};
    }

    private static boolean hasModifiedSystemProperties() {
        return SystemProperties.get("ro.debuggable").equals("1") || SystemProperties.get("ro.secure").equals("0");
    }

    private static boolean hasSuperUserBinary() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("/sbin");
        list.add("/system/bin");
        list.add("/system/xbin");
        list.add("/data/local/xbin");
        list.add("/data/local/bin");
        list.add("/system/sd/xbin");
        list.add("/system/bin/failsafe");
        list.add("/data/local");
        list.addAll(Arrays.asList(System.getenv("PATH").split(":")));
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            final File file = new File(iterator.next(), "su");
            if (file.exists() && file.canExecute()) {
                return true;
            }
        }
        return false;
    }

    private static boolean isDevOrTestKeys() {
        final String tags = Build.TAGS;
        return tags != null && (tags.contains("test-keys") || tags.contains("dev-keys"));
    }

    private static boolean isDeviceRooted() {
        return isDevOrTestKeys() || hasModifiedSystemProperties() || hasSuperUserBinary();
    }

    @SuppressLint("ResourceType")
    public static PsdBundle makePsdBundle(final Context context, final int n) {
        final long currentTimeMillis = System.currentTimeMillis();
        final boolean b = n == 0 || n == 2;
        final boolean b2 = n == 0 || 1 == n;
        final ContentResolver contentResolver = context.getContentResolver();
        final PackageManager packageManager = context.getPackageManager();
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        final Intent registerReceiver = context.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        String string = Settings.System.getString(contentResolver, "screen_brightness_mode");
        final String string2 = Settings.System.getString(contentResolver, "screen_off_timeout");
        String value = null;
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            value = String.valueOf(locationManager.isProviderEnabled("gps"));
        }
        String networkOperatorName = null;
        String value2 = null;
        String value3 = null;
        String value4 = null;
        String value5 = null;
        if (telephonyManager != null) {
            networkOperatorName = telephonyManager.getNetworkOperatorName();
            value2 = String.valueOf(telephonyManager.getSimState());
            value3 = String.valueOf(telephonyManager.getNetworkType());
            value4 = String.valueOf(telephonyManager.getPhoneType());
            value5 = String.valueOf(telephonyManager.getDataActivity());
        }
        final long long1 = Gservices.getLong(contentResolver, "android_id", 0L);
        String value6 = SystemProperties.get("persist.sys.timezone");
        String value7 = null;
        final String s = null;
        String value8 = null;
        String value9 = null;
        final String s2 = null;
        String value10 = null;
        final String s3 = null;
        String value11 = null;
        String value12 = null;
        final String s4 = null;
        String value13 = null;
        String value14 = null;
        final String s5 = null;
        String value15 = null;
        String value16 = null;
        final String s6 = null;
        String value17 = null;
        String value18 = null;
        final String s7 = null;
        String value19 = null;
        final String s8 = null;
        String imei = null;
        final String s9 = null;
        String s10 = null;
        final String s11 = null;
        String s12 = null;
        final String s13 = null;
        final String s14 = null;
        String s15 = null;
        final String s16 = null;
        String[] usbMode = null;
        final String[] array = null;
        final String s17 = null;
        if (audioManager != null) {
            value12 = String.valueOf(audioManager.getStreamMaxVolume(1));
            value14 = String.valueOf(audioManager.getStreamMaxVolume(4));
            value16 = String.valueOf(audioManager.getStreamMaxVolume(3));
            value18 = String.valueOf(audioManager.getStreamMaxVolume(2));
            value8 = String.valueOf(audioManager.getStreamMaxVolume(0));
            value10 = String.valueOf(audioManager.getStreamMaxVolume(5));
        }
        String value20;
        String s18;
        String s19;
        String[] array2;
        if (b) {
            final String string3 = Settings.System.getString(contentResolver, "screen_brightness");
            imei = s9;
            s10 = s11;
            s12 = s13;
            if (telephonyManager != null) {
                imei = telephonyManager.getImei();
                final String[] cellularStrength = getCellularStrength(telephonyManager);
                s10 = cellularStrength[0];
                s12 = cellularStrength[1];
                s15 = cellularStrength[2];
            }
            final String dumpBatteryStats = dumpBatteryStats(context);
            if (dumpBatteryStats != null && PsdValuesLoader.DEBUG) {
                Log.d("PsdValuesLoader", String.format("Dump battery stats, length: %d", dumpBatteryStats.length()));
            }
            value7 = s;
            value9 = s2;
            if (audioManager != null) {
                final int streamVolume = audioManager.getStreamVolume(0);
                final int streamVolume2 = audioManager.getStreamVolume(5);
                final int streamVolume3 = audioManager.getStreamVolume(1);
                final int streamVolume4 = audioManager.getStreamVolume(4);
                final int streamVolume5 = audioManager.getStreamVolume(3);
                final int streamVolume6 = audioManager.getStreamVolume(2);
                final boolean wiredHeadsetOn = audioManager.isWiredHeadsetOn();
                value17 = String.valueOf(streamVolume6);
                value9 = String.valueOf(streamVolume2);
                value11 = String.valueOf(streamVolume3);
                value13 = String.valueOf(streamVolume4);
                value19 = String.valueOf(wiredHeadsetOn);
                value7 = String.valueOf(streamVolume);
                value15 = String.valueOf(streamVolume5);
            }
            value20 = String.valueOf(registerReceiver.getIntExtra("plugged", -1));
            usbMode = getUsbMode(context);
            final String[] wifiExtras = getWifiExtras(wifiManager);
            s18 = string3;
            s19 = dumpBatteryStats;
            array2 = wifiExtras;
        }
        else {
            s18 = s17;
            array2 = array;
            value20 = s16;
            s15 = s14;
            s19 = s8;
            value19 = s7;
            value17 = s6;
            value15 = s5;
            value13 = s4;
            value11 = s3;
        }
        String value21 = "";
        String s20 = "";
        if (b2) {
            final long deviceAgeInDays = getDeviceAgeInDays(contentResolver);
            value21 = String.valueOf(deviceAgeInDays);
            if (deviceAgeInDays <= 30L) {
                s20 = "1";
            }
            else {
                s20 = "0";
            }
        }
        final String[] wifiStatus = getWifiStatus(context);
        final int intExtra = registerReceiver.getIntExtra("scale", -1);
        final int intExtra2 = registerReceiver.getIntExtra("level", -1);
        final int int1 = Settings.Secure.getInt(contentResolver, "location_mode", 0);
        final String[] batteryInfo = readBatteryInfo(registerReceiver, n);
        final String[] storage = readStorage(context, b);
        final String[] ram = readRam(context, b);
        final String[] versionInfo = readVersionInfo(packageManager, "com.google.android.GoogleCamera");
        final String[] versionInfo2 = readVersionInfo(packageManager, "com.google.android.wearable.app");
        final String[] versionInfo3 = readVersionInfo(packageManager, "com.google.android.googlequicksearchbox");
        final String[] versionInfo4 = readVersionInfo(packageManager, "com.google.android.gms");
        final String[] versionInfo5 = readVersionInfo(packageManager, "com.android.vending");
        final String string4 = Gservices.getString(contentResolver, "update_url", "");
        final String[] bluetoothInfo = readBluetoothInfo(context);
        final PsdBundle.Builder addSignal = new PsdBundle.Builder(context, Gservices.getLong(contentResolver, "settingsgoogle:psd_values_size_limit_bytes", 400000L)).addSignal("noe_display_name", Build.DISPLAY).addSignal("noe_type", Build.TYPE).addSignal("noe_product", Build.PRODUCT).addSignal("noe_sdk_int", String.valueOf(VERSION.SDK_INT)).addSignal("noe_incremental", VERSION.INCREMENTAL).addSignal("noe_codename", VERSION.CODENAME).addSignal("noe_board", Build.BOARD).addSignal("noe_brand", Build.BRAND).addSignal("noe_fingerprint", Build.FINGERPRINT).addSignal("noe_base_os", VERSION.BASE_OS).addSignal("noe_preview_sdk_int", String.valueOf(VERSION.PREVIEW_SDK_INT)).addSignal("noe_security_patch", VERSION.SECURITY_PATCH).addSignal("noe_dump_datetime", PsdValuesLoader.DATE_FORMATTER.format(new Date()));
        if (value6 == null) {
            value6 = "";
        }
        final PsdBundle.Builder addSignal2 = addSignal.addSignal("noe_timezone", value6).addSignal("noe_bootloader", Build.BOOTLOADER).addSignal("noe_radio", Build.getRadioVersion());
        if (networkOperatorName == null) {
            networkOperatorName = "";
        }
        final PsdBundle.Builder addSignal3 = addSignal2.addSignal("noe_network_operator", networkOperatorName);
        if (value3 == null) {
            value3 = "";
        }
        final PsdBundle.Builder addSignal4 = addSignal3.addSignal("noe_network_type", value3);
        if (value4 == null) {
            value4 = "";
        }
        final PsdBundle.Builder addSignal5 = addSignal4.addSignal("noe_phone_type", value4);
        if (value5 == null) {
            value5 = "";
        }
        final PsdBundle.Builder addSignal6 = addSignal5.addSignal("noe_is_volte_available", value5).addSignal("noe_build_id", Build.ID).addSignal("noe_decimal_gsf_id", String.valueOf(long1)).addSignal("noe_gps_enabled", value);
        if (string == null) {
            string = "";
        }
        final PsdBundle.Builder addSignal7 = addSignal6.addSignal("noe_screen_brightness_mode", string);
        String s21;
        if (value12 == null) {
            s21 = "";
        }
        else {
            s21 = value12;
        }
        final PsdBundle.Builder addSignal8 = addSignal7.addSignal("noe_system_max_volume", s21);
        if (value14 == null) {
            value14 = "";
        }
        final PsdBundle.Builder addSignal9 = addSignal8.addSignal("noe_alarm_max_volume", value14);
        if (value8 == null) {
            value8 = "";
        }
        final PsdBundle.Builder addSignal10 = addSignal9.addSignal("noe_voice_call_max_volume", value8);
        if (value10 == null) {
            value10 = "";
        }
        final PsdBundle.Builder addSignal11 = addSignal10.addSignal("noe_notification_max_volume", value10);
        if (value16 == null) {
            value16 = "";
        }
        final PsdBundle.Builder addSignal12 = addSignal11.addSignal("noe_music_max_volume", value16);
        if (value18 == null) {
            value18 = "";
        }
        final PsdBundle.Builder addSignal13 = addSignal12.addSignal("noe_ringer_max_volume", value18);
        String s22;
        if (string2 == null) {
            s22 = "";
        }
        else {
            s22 = string2;
        }
        final PsdBundle.Builder addSignal14 = addSignal13.addSignal("noe_display_timeout", s22);
        if (value2 == null) {
            value2 = "";
        }
        final PsdBundle.Builder addSignal15 = addSignal14.addSignal("noe_sim_state", value2).addSignal("noe_good_reboots_last_day", "").addSignal("noe_bad_reboots_last_day", "").addSignal("noe_good_reboots_last_week", "").addSignal("noe_bad_reboots_last_week", "").addSignal("noe_location_mode", String.valueOf(int1)).addSignal("noe_wifi_state", readWifiState(context));
        String wifiSecurityKeyValid;
        if (wifiStatus[1].equals("true")) {
            wifiSecurityKeyValid = wifiStatus[1];
        }
        else {
            wifiSecurityKeyValid = readWifiSecurityKeyValid(context);
        }
        final PsdBundle.Builder addSignal16 = addSignal15.addSignal("noe_wifi_security_key_valid", wifiSecurityKeyValid).addSignal("noe_battery_health", batteryInfo[0]).addSignal("noe_battery_level", String.valueOf(intExtra2)).addSignal("noe_battery_scale", String.valueOf(intExtra)).addSignal("noe_battery_voltage", batteryInfo[2]).addSignal("noe_battery_present", batteryInfo[3]).addSignal("noe_storage_available", storage[0]).addSignal("noe_storage_total", storage[1]).addSignal("noe_ram_available", ram[0]).addSignal("noe_ram_total", ram[1]).addSignal("noe_google_app_version_code", versionInfo3[0]).addSignal("noe_google_app_version_name", versionInfo3[1]).addSignal("noe_google_play_services_version_code", versionInfo4[0]).addSignal("noe_google_play_services_version_name", versionInfo4[1]).addSignal("noe_google_play_store_version_code", versionInfo5[0]).addSignal("noe_google_play_store_version_name", versionInfo5[1]).addSignal("noe_update_url", string4).addSignal("noe_num_bluetooth_connections", bluetoothInfo[0]).addSignal("noe_bluetooth_enabled", bluetoothInfo[1]).addSignal("noe_is_rooted", String.valueOf(isDeviceRooted())).addSignal("noe_camera_version_code", versionInfo[0]).addSignal("noe_camera_version_name", versionInfo[1]).addSignal("noe_wear_version_code", versionInfo2[0]).addSignal("noe_wear_version_name", versionInfo2[1]);
        if (2 == n) {
            try {
            addSignal16.addPairedBluetoothDevices().addConnectedBluetoothDevicesSignals().addTopBatteryApps();
            } catch(Exception e) {
                Log.d("PsdValuesLoader", "This class totally fucked up/");
            }
        }
        if (1 == n) {
            addSignal16.addSignal("noe_device_activation_time", value21);
        }
        if (n == 0) {
            addSignal16.addSignal("noe_device_under_thirty", s20);
        }
        if (b) {
            if (value7 == null) {
                value7 = "";
            }
            final PsdBundle.Builder addSignal17 = addSignal16.addSignal("noe_voice_call_volume", value7);
            if (value9 == null) {
                value9 = "";
            }
            final PsdBundle.Builder addSignal18 = addSignal17.addSignal("noe_notification_volume", value9);
            String s23;
            if (value11 == null) {
                s23 = "";
            }
            else {
                s23 = value11;
            }
            final PsdBundle.Builder addSignal19 = addSignal18.addSignal("noe_system_volume", s23).addSignal("noe_battery_status", batteryInfo[1]);
            if (value13 == null) {
                value13 = "";
            }
            final PsdBundle.Builder addSignal20 = addSignal19.addSignal("noe_alarm_volume", value13);
            String s24;
            if (value15 == null) {
                s24 = "";
            }
            else {
                s24 = value15;
            }
            final PsdBundle.Builder addSignal21 = addSignal20.addSignal("noe_music_volume", s24);
            String s25;
            if (value17 == null) {
                s25 = "";
            }
            else {
                s25 = value17;
            }
            final PsdBundle.Builder addSignal22 = addSignal21.addSignal("noe_ringer_volume", s25);
            String s26;
            if (s18 == null) {
                s26 = "";
            }
            else {
                s26 = s18;
            }
            final PsdBundle.Builder addSignal23 = addSignal22.addSignal("noe_brightness_level", s26);
            String s27;
            if (value19 == null) {
                s27 = "";
            }
            else {
                s27 = value19;
            }
            final PsdBundle.Builder addSignal24 = addSignal23.addSignal("noe_headset_attached", s27);
            if (s19 == null) {
                s19 = "";
            }
            final PsdBundle.Builder addSignal25 = addSignal24.addSignal("noe_battery_stats", s19);
            String s28;
            if (value20 == null) {
                s28 = "";
            }
            else {
                s28 = value20;
            }
            final PsdBundle.Builder addSignal26 = addSignal25.addSignal("noe_charging_status", s28).addSignal("noe_usb_functions", usbMode[0]).addSignal("noe_usb_power_role", usbMode[1]).addSignal("noe_usb_data_role", usbMode[2]);
            String s29;
            if (imei == null) {
                s29 = "";
            }
            else {
                s29 = imei;
            }
            final PsdBundle.Builder addSignal27 = addSignal26.addSignal("noe_imei", s29);
            String s30;
            if (s10 == null) {
                s30 = "";
            }
            else {
                s30 = s10;
            }
            final PsdBundle.Builder addSignal28 = addSignal27.addSignal("noe_cellular_radio_type", s30);
            if (s12 == null) {
                s12 = "";
            }
            final PsdBundle.Builder addSignal29 = addSignal28.addSignal("noe_cellular_strength", s12);
            String s31;
            if (s15 == null) {
                s31 = "";
            }
            else {
                s31 = s15;
            }
            addSignal29.addSignal("noe_cellular_dbm", s31).addSignal("noe_wifi_available", wifiStatus[0]).addSignal("noe_wifi_connected", wifiStatus[1]).addSignal("noe_wifi_speed", array2[0]).addSignal("noe_wifi_strength", array2[1]);
            addSignal16.addBatteryAnomalyApps().addTelephonyTroubleshooterDiagnosticSignals().addTelephonyTroubleshooterStatisticsSignals();
        }
        if (context.getResources().getBoolean(0x112008b)) {
            addSignal16.addSignal("genie-eng:app_pkg_name", "com.google.android.settings.gphone");
        }
        return addSignal16.build();
    }

    private static String[] readBatteryInfo(final Intent intent, final int n) {
        final String[] array = { "", "", "", "" };
        array[0] = String.valueOf(intent.getIntExtra("health", -1));
        if (2 == n || n == 0) {
            array[1] = String.valueOf(intent.getIntExtra("status", -1));
        }
        array[2] = String.valueOf(intent.getIntExtra("voltage", -1));
        array[3] = String.valueOf(intent.getBooleanExtra("present", false));
        return array;
    }

    private static String[] readBluetoothInfo(final Context context) {
        final String[] array = { "", "" };
        final BluetoothManager bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            final BluetoothAdapter adapter = bluetoothManager.getAdapter();
            if (adapter != null) {
                int n = 0;
                final Iterator<? extends BluetoothDevice> iterator = adapter.getBondedDevices().iterator();
                while (iterator.hasNext()) {
                    int n2 = n;
                    if (iterator.next().equals("isConnected")) {
                        n2 = n + 1;
                    }
                    n = n2;
                }
                array[0] = String.valueOf(n);
                array[1] = String.valueOf(adapter.isEnabled());
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bluetooth: ");
        sb.append(array[0]);
        Log.d("PsdValuesLoader", sb.toString());
        return array;
    }

    private static String readFile(final File file) throws IOException {
        return readInputStream(new FileInputStream(file));
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder sb = new StringBuilder();
        try {
            while (true) {
                final String line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
                sb.append('\n');
            }
            br.close();
            return sb.toString();
        }
        finally {
            br.close();
        }
    }

    protected static String[] readRam(final Context context, final boolean b) {
        final String[] array = new String[2];
        final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            final ActivityManager.MemoryInfo activityManager$MemoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(activityManager$MemoryInfo);
            final long availMem = activityManager$MemoryInfo.availMem;
            final long totalMem = activityManager$MemoryInfo.totalMem;
            long roundToMB = availMem;
            long roundToMB2 = totalMem;
            if (!b) {
                roundToMB = roundToMB(availMem);
                roundToMB2 = roundToMB(totalMem);
            }
            array[0] = String.valueOf(roundToMB);
            array[1] = String.valueOf(roundToMB2);
        }
        else {
            array[1] = (array[0] = "");
        }
        return array;
    }

    protected static String[] readStorage(final Context context, final boolean b) {
        final StorageManager storageManager = (StorageManager)context.getSystemService((Class<? extends StorageManager>)StorageManager.class);
        long n = 0L;
        long n2 = 0L;
        long roundToMB = n;
        long roundToMB2 = n2;
     /*   if (storageManager != null) {
            for (final VolumeInfo volumeInfo : storageManager.getVolumes()) {
                long n3 = n;
                long n4 = n2;
                if (volumeInfo.getType() == 1) {
                    n3 = n;
                    n4 = n2;
                    if (volumeInfo.isMountedReadable()) {
                        n3 = n + volumeInfo.getPath().getFreeSpace();
                        n4 = n2 + volumeInfo.getPath().getTotalSpace();
                    }
                }
                n = n3;
                n2 = n4;
            }
            roundToMB = n;
            roundToMB2 = n2;
            if (!b) {
                roundToMB = roundToMB(n);
                roundToMB2 = roundToMB(n2);
            }
        }

      */
        return new String[] { String.valueOf(roundToMB), String.valueOf(roundToMB2) };
    }

    private static String[] readVersionInfo(final PackageManager packageManager, final String s) {
        final String[] array = new String[2];
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo(s, 0);
            if (packageInfo != null) {
                array[0] = String.valueOf(packageInfo.versionCode);
                array[1] = packageInfo.versionName;
            }
            else {
                array[1] = (array[0] = "");
            }
        }
        catch (PackageManager.NameNotFoundException ex) {
            Log.e("PsdValuesLoader", "Failed to find package");
            array[1] = (array[0] = "");
        }
        return array;
    }

    private static String readWifiSecurityKeyValid(final Context context) {
        final WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            final List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
            if (configuredNetworks != null) {
                final Iterator<WifiConfiguration> iterator = configuredNetworks.iterator();
                while (iterator.hasNext()) {
                  /*  if (iterator.next().getNetworkSelectionStatus().getNetworkSelectionDisableReason() == 3) {
                        return String.valueOf(false);
                    }

                   */
                }
            }
        }
        return "";
    }

    private static String readWifiState(final Context context) {
        final WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        String value;
        if (wifiManager == null) {
            value = "";
        }
        else {
            value = String.valueOf(wifiManager.getWifiState());
        }
        return value;
    }

    private static long roundToDays(final long n) {
        return TimeUnit.MILLISECONDS.toDays(n);
    }

    private static long roundToMB(final long n) {
        return Math.round(n / 1000000.0);
    }

    public PsdBundle loadInBackground() {
        return makePsdBundle(this.getContext(), 0);
    }

    @Override
    protected void onDiscardResult(final PsdBundle psdBundle) {
    }
}
