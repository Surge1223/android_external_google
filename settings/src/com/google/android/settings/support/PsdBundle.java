package com.google.android.settings.support;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import com.android.internal.os.BatterySipper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Collections;
import java.util.Iterator;
import com.android.settings.fuelgauge.anomaly.Anomaly;
import com.android.settings.fuelgauge.anomaly.AnomalyDetectionPolicy;
import com.android.settings.fuelgauge.anomaly.AnomalyUtils;
import com.android.settingslib.bluetooth.A2dpProfile;
import com.android.settingslib.bluetooth.HeadsetProfile;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import com.android.internal.os.BatterySipper;
import com.android.settings.bluetooth.Utils;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import android.net.Uri;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothDevice;
import java.util.Set;
import android.os.Bundle;
import android.os.UserManager;
import android.database.Cursor;
import android.net.Uri.Builder;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.android.settings.fuelgauge.BatteryUtils;
import com.android.internal.os.BatteryStatsHelper;

public class PsdBundle
{
    private static final String[] TELEPHONY_PROJECTION;
    private final String[] mKeys;
    private final String[] mValues;

    static {
        TELEPHONY_PROJECTION = new String[] { "KEY", "VALUE" };
    }

    private PsdBundle(final Builder builder, Object o) {
        this.mKeys = builder.mSignalKeys.toArray(new String[builder.mSignalKeys.size()]);
        this.mValues = builder.mSignalValues.toArray(new String[builder.mSignalValues.size()]);
    }

    public String[] getKeys() {
        return this.mKeys;
    }

    public String[] getValues() {
        return this.mValues;
    }

    public static class Builder
    {
        private final String CALL_STATISTICS_PATH;
        private final String DIAGNOSTICS_PATH;
        private final String TELEPHONY_AUTHORITY;
        private BatteryStatsHelper mBatteryStatsHelper;
        private BatteryUtils mBatteryUtils;
        private final Context mContext;
        private long mPsdValuesSize;
        private long mPsdValuesSizeLimit;
        private List<String> mSignalKeys;
        private List<String> mSignalValues;
        private Object CompareShit;
       // private Object BatterySipper;

        public Builder(final Context mContext, final long mPsdValuesSizeLimit) {
            this.mSignalKeys = new ArrayList<String>();
            this.mSignalValues = new ArrayList<String>();
            this.mPsdValuesSize = 0L;
            this.TELEPHONY_AUTHORITY = "com.google.android.connectivitymonitor.troubleshooterprovider";
            this.CALL_STATISTICS_PATH = "call_statistics";
            this.DIAGNOSTICS_PATH = "diagnostics";
            this.mContext = mContext;
            this.mPsdValuesSizeLimit = mPsdValuesSizeLimit;
        }

        private Builder addTelephonyCursorSignals(String s) {
            final Cursor query = this.mContext.getContentResolver().query(new Uri.Builder().scheme("content").authority("com.google.android.connectivitymonitor.troubleshooterprovider").path(s).build(), PsdBundle.TELEPHONY_PROJECTION, (String)null, (String[])null, (String)null);
            if (query == null) {
                if (query != null) {
                    query.close();
                }
                return this;
            }
            final String s2 = s = null;
            while (true) {
                try {
                    try {
                        final String[] columnNames = query.getColumnNames();
                        if (columnNames != null) {
                            s = s2;
                            if (columnNames.length != 0) {
                                s = s2;
                                if (!query.moveToFirst()) {
                                    if (query != null) {
                                        query.close();
                                    }
                                    return this;
                                }
                                s = s2;
                                final int columnIndex = query.getColumnIndex("KEY");
                                s = s2;
                                final int columnIndex2 = query.getColumnIndex("VALUE");
                                if (columnIndex2 != -1 && columnIndex != -1) {
                                    do {
                                        s = s2;
                                        this.addSignal(query.getString(columnIndex), query.getString(columnIndex2));
                                        s = s2;
                                    } while (query.moveToNext());
                                    if (query != null) {
                                        query.close();
                                    }
                                    return this;
                                }
                                if (query != null) {
                                    query.close();
                                }
                                return this;
                            }
                        }
                        if (query != null) {
                            query.close();
                        }
                        return this;
                    }
                    finally {
                        if (query != null) {
                            if (s != null) {
                                final Cursor cursor = query;
                                cursor.close();
                            }
                            else {
                                query.close();
                            }
                        }
                    }
                }
                catch (Throwable t) {}
                try {
                    final Cursor cursor = query;
                    cursor.close();
                    continue;
                }
                catch (Throwable t2) {}
                break;
            }
            return null;
        }

        private BatteryStatsHelper getBatteryStatsHelper() {
            if (this.mBatteryStatsHelper == null) {
                this.mBatteryStatsHelper = new BatteryStatsHelper(this.mContext, true);
                this.mBatteryUtils.initBatteryStatsHelper(this.mBatteryStatsHelper, null, (UserManager)this.mContext.getSystemService(Context.USER_SERVICE));
            }
            return this.mBatteryStatsHelper;
        }

        private Set<BluetoothDevice> getBondedBtDevices(final Context context) {
            final BluetoothManager bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (bluetoothManager != null) {
                final BluetoothAdapter adapter = bluetoothManager.getAdapter();
                if (adapter != null) {
                    return (Set<BluetoothDevice>)adapter.getBondedDevices();
                }
            }
            return null;
        }

        private String getEncodedName(final String s) {
            return Uri.encode(s);
        }

        private LocalBluetoothManager getLocalBtManager(final Context context) {
            return Utils.getLocalBtManager(context);
        }
        private void processBTDevice(final LocalBluetoothManager localBluetoothManager, final StringBuilder[] array, final BluetoothDevice bluetoothDevice) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
            boolean b = false;
            boolean b2 = false;
            if (localBluetoothManager != null) {
                final List<LocalBluetoothProfile> connectableProfiles = localBluetoothManager.getCachedDeviceManager().findDevice(bluetoothDevice).getConnectableProfiles();

                b2 = false;
                b = false;
                boolean b3;
                boolean b4;
                for (int i = 0; i < connectableProfiles.size(); ++i, b = b3, b2 = b4) {
                    final LocalBluetoothProfile localBluetoothProfile = connectableProfiles.get(i);
                    b3 = b;
                    if (localBluetoothProfile instanceof HeadsetProfile) {
                        b3 = b;
                        if (localBluetoothProfile.isPreferred(bluetoothDevice)) {
                            b3 = true;
                        }
                    }
                    b4 = b2;
                    if (localBluetoothProfile instanceof A2dpProfile) {
                        b4 = b2;
                        if (localBluetoothProfile.isPreferred(bluetoothDevice)) {
                            b4 = true;
                        }
                    }
                }
            }
            Method method = bluetoothDevice.getClass().getMethod("getAliasName");method.invoke(bluetoothDevice);

            final StringBuilder sb = array[0];
            sb.append(this.getEncodedName(bluetoothDevice.getClass().getSimpleName()));
            sb.append(',');
            final StringBuilder sb2 = array[1];
            sb2.append(b);
            sb2.append(',');
            final StringBuilder sb3 = array[2];
            sb3.append(b2);
            sb3.append(',');
        }

        public Builder addBatteryAnomalyApps() {
            final List<Anomaly> detectAnomalies = AnomalyUtils.getInstance(this.mContext).detectAnomalies(this.getBatteryStatsHelper(), new AnomalyDetectionPolicy(this.mContext), null);
            final StringBuilder sb = new StringBuilder();
            final StringBuilder sb2 = new StringBuilder();
            for (int i = 0; i < detectAnomalies.size(); ++i) {
                final Anomaly anomaly = detectAnomalies.get(i);
                sb.append(anomaly.packageName);
                sb.append(',');
                sb2.append(anomaly.type);
                sb2.append(',');
            }
            if (sb.length() != 0) {
                sb.setLength(sb.length() - 1);
                sb2.setLength(sb2.length() - 1);
            }
            this.addSignal("noe_battery_anomaly_names", sb.toString());
            this.addSignal("noe_battery_anomaly_types", sb2.toString());
            return this;
        }

        public Builder addConnectedBluetoothDevicesSignals() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
            final Set<BluetoothDevice> bondedBtDevices = this.getBondedBtDevices(this.mContext);
            final StringBuilder[] array = { new StringBuilder(), new StringBuilder(), new StringBuilder() };
            final LocalBluetoothManager localBtManager = this.getLocalBtManager(this.mContext);
            for (final BluetoothDevice bluetoothDevice : bondedBtDevices) {
           /*     if (bluetoothDevice.isConnected()) {
                    this.processBTDevice(localBtManager, array, bluetoothDevice);
                }

            */
            }
            for (final StringBuilder sb : array) {
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
            }
            final String string = array[0].toString();
            final String string2 = array[1].toString();
            final String string3 = array[2].toString();
            this.addSignal("noe_connected_bluetooth_devices", string);
            this.addSignal("noe_connected_bluetooth_devices_headset", string2);
            this.addSignal("noe_connected_bluetooth_devices_a2dp", string3);
            return this;
        }

        public Builder addPairedBluetoothDevices() {
            final Set<BluetoothDevice> bondedBtDevices = this.getBondedBtDevices(this.mContext);
            final StringBuilder sb = new StringBuilder();
            final Iterator<BluetoothDevice> iterator = bondedBtDevices.iterator();
            while (iterator.hasNext()) {
            //    sb.append(this.getEncodedName(iterator.next().getAliasName()));
                sb.append(',');
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            this.addSignal("noe_paired_bluetooth_devices", sb.toString());
            return this;
        }

        public Builder addSignal(final String s, final String s2) {
            this.mSignalKeys.add(s);
            if (s2 != null) {
                final long n = s2.length();
                if (this.mPsdValuesSize + n <= this.mPsdValuesSizeLimit) {
                    this.mSignalValues.add(s2);
                    this.mPsdValuesSize += n;
                    return this;
                }
            }
            this.mSignalValues.add("");
            return this;
        }

        public Builder addTelephonyTroubleshooterDiagnosticSignals() {
            this.addSignal("noe_telephony_diagnostic_signal_flag", "");
            return this.addTelephonyCursorSignals("diagnostics");
        }

        public Builder addTelephonyTroubleshooterStatisticsSignals() {
            this.addSignal("noe_telephony_stats_signal_flag", "");
            return this.addTelephonyCursorSignals("call_statistics");
        }

        public Builder addTopBatteryApps() {
            final BatteryStatsHelper batteryStatsHelper = this.getBatteryStatsHelper();
            final int dischargeAmount = batteryStatsHelper.getStats().getDischargeAmount(0);
            final double totalPower = batteryStatsHelper.getTotalPower();
            final double n = totalPower / dischargeAmount;
            final List usageList = batteryStatsHelper.getUsageList();
            Collections.sort((List<Object>)usageList, (Comparator<? super Object>)CompareShit);
            final StringBuilder sb = new StringBuilder();
            final StringBuilder sb2 = new StringBuilder();

     //       for (BatterySipper  batterySipper : usageList) {

            com.android.internal.os.BatterySipper batterySipper = null;
            if (!batterySipper.shouldHide && batterySipper.drainType ==
                com.android.internal.os.BatterySipper.DrainType.APP) {
              //      if (batterySipper.totalSmearedPowerMah < n) {
              //          continue;
              //      }
                    
                    final String packageName = this.mBatteryUtils.getPackageName(batterySipper.getUid());
                    sb2.append((int)this.mBatteryUtils.calculateBatteryPercent(batterySipper.totalSmearedPowerMah, totalPower, 0.0, dischargeAmount));
                    sb2.append(',');
                    sb.append(packageName);
                    sb.append(',');
                }
            
            final int length = sb.length();
            final int length2 = sb2.length();
            if (length > 0) {
                sb.setLength(length - 1);
                sb2.setLength(length2 - 1);
            }
            this.addSignal("noe_battery_usage_names", sb.toString());
            this.addSignal("noe_battery_usage_percentages", sb2.toString());
            return this;
        }

        class CompareShit implements Comparator
        {
            @Override
            public final int compare(final Object o, final Object o2) {
                 // return PsdBundle.Builder.addTopBatteryApps((BatterySipper)o, (BatterySipper)o2);
                return 0;
            }
    
        }
/*
        private static int addTopBatteryApps(BatterySipper o, BatterySipper o2) {
			return 
        }
*/
        public PsdBundle build() {
            return new PsdBundle(this, null);
        }
    }
}
