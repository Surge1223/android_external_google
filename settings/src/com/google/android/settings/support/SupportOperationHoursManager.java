package com.google.android.settings.support;

import java.util.Date;
import java.text.ParseException;
import android.util.Log;
import java.util.ArrayList;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.util.Pair;
import java.util.List;
import java.util.Locale;
import android.content.ContentResolver;
import android.text.TextUtils;
import com.google.android.gsf.Gservices;
import android.util.ArrayMap;
import android.content.Context;
import android.util.SparseArray;
import java.util.Map;

final class SupportOperationHoursManager
{
    private final String mDeviceCountry;
    private final Map<String, SparseArray<SupportOperationHours>> mOperationHours;
    
    SupportOperationHoursManager(final Context context, final SupportFlags supportFlags) {
        final ContentResolver contentResolver = context.getContentResolver();
        this.mOperationHours = (Map<String, SparseArray<SupportOperationHours>>)new ArrayMap();
        this.mDeviceCountry = Gservices.getString(contentResolver, "device_country", null);
        final String phoneSupportCountries = supportFlags.getPhoneSupportCountries(contentResolver);
        if (!TextUtils.isEmpty((CharSequence)phoneSupportCountries)) {
            this.initOperationHours(context, 2, phoneSupportCountries.split(","), "settingsgoogle:phone_support_always_operating_", "settingsgoogle:phone_support_hours_", "settingsgoogle:support_hours_timezone_");
        }
        final String string = Gservices.getString(contentResolver, "settingsgoogle:chat_supported_countries", null);
        if (!TextUtils.isEmpty((CharSequence)string)) {
            this.initOperationHours(context, 3, string.split(","), "settingsgoogle:chat_support_always_operating_", "settingsgoogle:chat_support_hours_", "settingsgoogle:support_hours_timezone_");
        }
    }
    
    private SupportOperationHours getOperationConfig(final int n, final String s) {
        SparseArray<SupportOperationHours> sparseArray;
        if (TextUtils.isEmpty((CharSequence)s)) {
            final Map<String, SparseArray<SupportOperationHours>> mOperationHours = this.mOperationHours;
            final StringBuilder sb = new StringBuilder();
            sb.append(Locale.getDefault().getLanguage());
            sb.append("_");
            sb.append(this.mDeviceCountry);
            if ((sparseArray = mOperationHours.get(sb.toString())) == null) {
                sparseArray = this.mOperationHours.get(this.mDeviceCountry);
            }
        }
        else {
            sparseArray = this.mOperationHours.get(s);
        }
        if (sparseArray != null) {
            return (SupportOperationHours)sparseArray.get(n);
        }
        return null;
    }
    
    private void initOperationHours(final Context context, final int n, final String[] array, final String s, final String s2, final String s3) {
        final ContentResolver contentResolver = context.getContentResolver();
        int i = 0;
        final Map<String, String> stringsByPrefix = Gservices.getStringsByPrefix(contentResolver, s, s2, s3);
        while (i < array.length) {
            final String s4 = array[i];
            SparseArray sparseArray;
            if ((sparseArray = this.mOperationHours.get(s4)) == null) {
                sparseArray = new SparseArray(2);
                this.mOperationHours.put(s4, (SparseArray<SupportOperationHours>)sparseArray);
            }
            sparseArray.put(n, (Object)new SupportOperationHours(stringsByPrefix, s4, s, s2, s3));
            ++i;
        }
    }
    
    String getCurrentCountryCodeIfHasConfig(final int n) {
        String countryCode = null;
        final SupportOperationHours operationConfig = this.getOperationConfig(n, null);
        if (operationConfig != null) {
            countryCode = operationConfig.countryCode;
        }
        return countryCode;
    }
    
    public String getDeviceCountry() {
        return this.mDeviceCountry;
    }
    
    static final class SupportOperationHours
    {
        public final String countryCode;
        public final boolean isAlwaysOperating;
        private final List<Pair<Calendar, Calendar>> mHours;
        private final SimpleDateFormat timeConfigParser;
        public final TimeZone timeZone;
        
        SupportOperationHours(final Map<String, String> map, String countryCode, String s, final String s2, final String s3) {
            this.timeConfigParser = new SimpleDateFormat("HH:mm");
            this.countryCode = countryCode;
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(countryCode);
            this.isAlwaysOperating = Boolean.valueOf(map.get(sb.toString()));
            this.mHours = new ArrayList<Pair<Calendar, Calendar>>(7);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s3);
            sb2.append(countryCode);
            s = map.get(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s2);
            sb3.append(countryCode);
            countryCode = map.get(sb3.toString());
            String id = s;
            if (s == null) {
                id = TimeZone.getDefault().getID();
            }
            this.timeZone = TimeZone.getTimeZone(id);
            if (!this.isAlwaysOperating && !TextUtils.isEmpty((CharSequence)countryCode)) {
                final String[] split = countryCode.split(",");
                for (int length = split.length, i = 0; i < length; ++i) {
                    final String[] split2 = split[i].split("-");
                    this.mHours.add((Pair<Calendar, Calendar>)new Pair((Object)this.parseOperationTime(split2[0]), (Object)this.parseOperationTime(split2[1])));
                }
            }
        }
        
        private Calendar parseOperationTime(final String s) {
            try {
                final Calendar instance = Calendar.getInstance(this.timeZone);
                final Date parse = this.timeConfigParser.parse(s);
                instance.set(11, parse.getHours());
                instance.set(12, parse.getMinutes());
                instance.set(13, 0);
                return instance;
            }
            catch (ParseException ex) {
                Log.e("SupportOperationHours", "Failed to parse operation hours. skipping.");
                return null;
            }
        }
    }
}
