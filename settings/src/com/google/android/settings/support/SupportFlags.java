package com.google.android.settings.support;

import com.google.android.gsf.Gservices;
import android.content.ContentResolver;

public class SupportFlags
{
    public String getPhoneSupportCountries(final ContentResolver contentResolver) {
        return Gservices.getString(contentResolver, "settingsgoogle:phone_supported_countries", null);
    }
    
    public String getPhoneSupportNumbers(final ContentResolver contentResolver, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("settingsgoogle:phone_support_numbers_");
        sb.append(s);
        return Gservices.getString(contentResolver, sb.toString(), null);
    }
}
