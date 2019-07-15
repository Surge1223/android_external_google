package com.google.android.settings.fuelgauge;

import android.content.pm.PackageManager;
import com.android.internal.util.ArrayUtils;
import com.android.internal.os.BatterySipper;
import com.google.android.gsf.Gservices;
import android.util.SparseIntArray;
import com.android.settingslib.utils.PowerUtil;
import java.time.Duration;
import com.android.settings.fuelgauge.Estimate;
import android.database.Cursor;
import android.text.TextUtils;
import android.net.Uri.Builder;
import android.net.Uri;
import android.content.Context;
import com.android.settings.fuelgauge.PowerUsageFeatureProviderImpl;
import com.android.settings.R;

public  class PowerUsageFeatureProviderGoogleImpl extends PowerUsageFeatureProviderImpl
{
    static  String AVERAGE_BATTERY_LIFE_COL = "average_battery_life";
    static  String BATTERY_ESTIMATE_BASED_ON_USAGE_COL = "is_based_on_usage";
    static  String BATTERY_ESTIMATE_COL = "battery_estimate";
    static  String BATTERY_LEVEL_COL = "battery_level";
    static  int CUSTOMIZED_TO_USER = 1;
    static  String GFLAG_ADDITIONAL_BATTERY_INFO_ENABLED = "settingsgoogle:additional_battery_info_enabled";
    static  String GFLAG_BATTERY_ADVANCED_UI_ENABLED = "settingsgoogle:battery_advanced_ui_enabled";
    static  String GFLAG_BATTERY_ESTIMATE_DEBUGGING_ENABLED = "settingsgoogle:battery_estimate_debugging_enabled";
    static  String GFLAG_POWER_ACCOUNTING_TOGGLE_ENABLED = "settingsgoogle:power_accounting_toggle_enabled";
    static  String IS_EARLY_WARNING_COL = "is_early_warning";
    static  int NEED_EARLY_WARNING = 1;
    private static  String[] PACKAGES_SERVICE;

    private  boolean TURBO_ENABLED = true;
    static  String TIMESTAMP_COL = "timestamp_millis";
    protected PackageManager mPackageManager;
    protected Context mContext;

    public PowerUsageFeatureProviderGoogleImpl(Context context) {
        super(context);
        mPackageManager = context.getPackageManager();
        mContext = context.getApplicationContext();
    }
    
    private static void closeResource( Throwable t,  AutoCloseable autoCloseable) {
        if (t != null) {
            try {
                autoCloseable.close();
            } catch (Throwable t2) {
                t.addSuppressed(t2);
            }
        }
    }

    static {
        PACKAGES_SERVICE = new String[] { "com.google.android.gms", "com.google.android.apps.gcs" };
    }
    
    private Uri getEnhancedBatteryPredictionCurveUri() {
        return new Uri.Builder().scheme("content").authority("com.google.android.apps.turbo.estimated_time_remaining").appendPath("discharge_curve").build();
    }

    private Uri getEnhancedBatteryPredictionUri() {
        return new Uri.Builder().scheme("content").authority("com.google.android.apps.turbo.estimated_time_remaining").appendPath("time_remaining").build();
    }

    @Override
    public String getAdvancedUsageScreenInfoString() {
        return mContext.getString(R.string.advanced_battery_graph_subtext);
    }

    @Override
    public boolean getEarlyWarningSignal(Context context,  String id) {
        Uri.Builder builder = new Uri.Builder().scheme("content")
                .authority("com.google.android.apps.turbo.estimated_time_remaining")
                .appendPath("early_warning")
                .appendPath("id");
        if (TextUtils.isEmpty(id)) {
            builder.appendPath(context.getPackageName());
        } else {
            builder.appendPath(id);
        }

        Cursor query = context.getContentResolver().query(builder.build(), (String[])null, (String)null, (String[])null, (String)null);
        Context context2 = null;
        boolean b = false;
        if (query != null) {
            context = context2;
            try {
                try {
                    if (query.moveToFirst()) {
                        context = context2;
                        if (query.getInt(query.getColumnIndex(IS_EARLY_WARNING_COL)) == 1) {
                            b = true;
                        }
                        if (query != null) {
                            closeResource(null, (AutoCloseable)query);
                        }
                        return b;
                    }
                }

                finally {
                    if (query != null) {
                        closeResource(null, query);
                    }
                }
            }
            catch (Throwable t) {

            }
        }
        return b;
    }

    @Override
    public Estimate getEnhancedBatteryPrediction(Context context) {
        Cursor query = context.getContentResolver().query(getEnhancedBatteryPredictionUri(), (String[])null, (String)null, (String[])null, (String)null);
        Context context2 = null;
        if (query != null) {
            context = context2;
            try {
                try {
                    if (query.moveToFirst()) {
                        context = context2;
                        int columnIndex = query.getColumnIndex("is_based_on_usage");
                        boolean b = true;
                        if (columnIndex != -1) {
                            context = context2;
                            int int1 = query.getInt(columnIndex);
                            b = true;
                            if (int1 == 0) {
                                b = false;
                            }
                        }
                        context = context2;
                        int columnIndex2 = query.getColumnIndex("average_battery_life");
                        long roundTimeToNearestThreshold = 0L;
                        Label_0182: {
                            if (columnIndex2 != -1) {
                                context = context2;
                                long long1 = query.getLong(columnIndex2);
                                if (long1 != -1L) {
                                    context = context2;
                                    long n = Duration.ofMinutes(15L).toMillis();
                                    context = context2;
                                    if (Duration.ofMillis(long1).compareTo(Duration.ofDays(1L)) >= 0) {
                                        context = context2;
                                        n = Duration.ofHours(1L).toMillis();
                                    }
                                    context = context2;
                                    roundTimeToNearestThreshold = PowerUtil.roundTimeToNearestThreshold(long1, n);
                                    break Label_0182;
                                }
                            }
                            roundTimeToNearestThreshold = -1L;
                        }
                        context = context2;
                        Estimate estimate = new Estimate(query.getLong(query.getColumnIndex("battery_estimate")), b, roundTimeToNearestThreshold);
                        if (query != null) {
                            closeResource(null, query);
                        }
                        return estimate;
                    }
                } catch (Throwable t) {
                    if (query != null) {
                        closeResource(t, query);
                    }
                    return null;
                }
            } catch (NullPointerException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public SparseIntArray getEnhancedBatteryPredictionCurve( Context context,  long n) {
        Uri enhancedBatteryPredictionCurveUri = getEnhancedBatteryPredictionCurveUri();
        try {
            Cursor query = context.getContentResolver().query(enhancedBatteryPredictionCurveUri, (String[])null, (String)null, (String[])null, (String)null);
            if (query == null) {
                    closeResource(null, (AutoCloseable)query);
                return null;
            }
            try {
                int columnIndex = query.getColumnIndex("timestamp_millis");
                int columnIndex2 = query.getColumnIndex("battery_level");
                SparseIntArray sparseIntArray = new SparseIntArray(query.getCount());
                while (query.moveToNext()) {
                    sparseIntArray.append((int)(query.getLong(columnIndex) - n), query.getInt(columnIndex2));
                }
                closeResource(null, query);
                return sparseIntArray;
            } catch (Throwable th) {
                if (query != null) {
                    closeResource(th, query);
                }
                return null;
            }
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public String getEnhancedEstimateDebugString( String timeRemaining) {
        return mContext.getString(R.string.power_usage_level_and_status, new Object[] { timeRemaining });
    }

    @Override
    public String getOldEstimateDebugString( String timeRemaining) {
        return mContext.getString(R.string.power_usage_summary, new Object[] {
                timeRemaining 
        });
    }

    @Override
    public boolean isEnhancedBatteryPredictionEnabled(Context context) {
        if (TURBO_ENABLED) {
            try {
                boolean turboEnabled = mPackageManager.getPackageInfo("com.google.android.apps.turbo",
                        PackageManager.MATCH_DISABLED_COMPONENTS).applicationInfo.enabled;
                return turboEnabled;
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isEstimateDebugEnabled() {
        return mContext.getResources().getBoolean(R.bool.battery_estimate_debugging_enabled);
        }

        @Override
        public boolean isSmartBatterySupported() {
            return mContext.getResources().getBoolean(
                    com.android.internal.R.bool.config_smart_battery_available);
        }
        

    @Override
    public boolean isTypeService( BatterySipper batterySipper) {
        String[] packagesForUid = mPackageManager.getPackagesForUid(batterySipper.getUid());
        if (packagesForUid == null) {
            return false;
        }
        for (int length = packagesForUid.length, i = 0; i < length; ++i) {
            if (ArrayUtils.contains((Object[])PowerUsageFeatureProviderGoogleImpl.PACKAGES_SERVICE, (Object)packagesForUid[i])) {
                return true;
            }
        }
        return false;
    }
}


