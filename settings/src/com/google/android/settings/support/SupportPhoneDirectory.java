package com.google.android.settings.support;

import android.content.ContentResolver;
import android.content.Context;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.android.internal.annotations.VisibleForTesting;
import java.text.Collator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import com.android.settings.support.SupportPhone;
import com.google.android.settings.support.SupportFlags;

public final class SupportPhoneDirectory {
    private final Context mContext;
    private final List<Country> mCountries = new ArrayList<>();
    private final List<String> mCountryCodes = new ArrayList<>();
    private final List<String> mCountryDisplayNames = new ArrayList<>();
    private final Map<String, List<SupportPhone>> mDirectory = new ArrayMap<>();
    private Locale mDisplayLocale;
    private final SupportFlags mSupportFlags;

    @VisibleForTesting
    static final class Country {
        final String countryCode;
        String displayName;

        Country(Context context, String str) {
            this.countryCode = str;
            updateDisplayName(context);
        }

        void updateDisplayName(Context context) {
            String language = Locale.getDefault().getLanguage();
            String[] split = this.countryCode.split("_");
            if (split.length == 2) {
                Locale locale = new Locale(split[0], split[1]);
                language = context.getString(2131889409, new Object[]{locale.getDisplayCountry(), locale.getDisplayLanguage()});
            } else {
                language = new Locale(language, this.countryCode).getDisplayCountry();
            }
            this.displayName = language;
        }
    }
    private static final class CountryComparator implements Comparator<Country> {
        private final Collator mCollator;

        private CountryComparator() {
            this.mCollator = Collator.getInstance();
        }

        public int compare(Country country, Country country2) {
            return (country == null && country2 == null) ? 0 : country == null ? -1 : country2 == null ? 1 : this.mCollator.compare(country.displayName, country2.displayName);
        }
    }

    public SupportPhoneDirectory(Context context, SupportFlags supportFlags) {
        Throwable e;
        this.mContext = context;
        this.mSupportFlags = supportFlags;
        ContentResolver contentResolver = context.getContentResolver();
        String phoneSupportCountries = this.mSupportFlags.getPhoneSupportCountries(contentResolver);
        if (!TextUtils.isEmpty(phoneSupportCountries)) {
            String[] split = phoneSupportCountries.split(",");
            initCountries(context, split);
            for (String str : split) {
                phoneSupportCountries = this.mSupportFlags.getPhoneSupportNumbers(contentResolver, str);
                if (!TextUtils.isEmpty(phoneSupportCountries)) {
                    List<SupportPhone> arrayList = new ArrayList<SupportPhone>();
                    for (String supportPhone : phoneSupportCountries.split(",")) {
                        try {
                            arrayList.add(new SupportPhone(supportPhone));
                        } catch (ParseException e2) {
                            e = e2;
                        }
                    }
                    this.mDirectory.put(str, arrayList);
                }
            }
        }
    }

    private void initCountries(Context context, String[] strArr) {
        for (String country : strArr) {
            this.mCountries.add(new Country(context, country));
        }
        keepCountriesSorted();
    }

    private void keepCountriesSorted() {
        Locale locale = Locale.getDefault();
        if (!Objects.equals(this.mDisplayLocale, locale)) {
            this.mDisplayLocale = locale;
            this.mCountryCodes.clear();
            this.mCountryDisplayNames.clear();
            for (Country updateDisplayName : this.mCountries) {
                updateDisplayName.updateDisplayName(this.mContext);
            }
            Collections.sort(this.mCountries, new CountryComparator());
            for (Country updateDisplayName2 : this.mCountries) {
                this.mCountryCodes.add(updateDisplayName2.countryCode);
                this.mCountryDisplayNames.add(updateDisplayName2.displayName);
            }
        }
    }

    public SupportPhone getSupportPhones(String str, boolean z) {
        List<SupportPhone> list = (List<SupportPhone>) this.mDirectory.get(str);
        if (list != null) {
            for (SupportPhone supportPhone : list) {
                if (supportPhone.isTollFree == z) {
                    return supportPhone;
                }
            }
        }
        return null;
    }
}
