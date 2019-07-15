package com.google.android.gsf;

import java.util.TreeMap;
import android.database.Cursor;
import android.util.Log;
import android.os.Handler;
import android.database.ContentObserver;
import java.util.Map;
import android.content.ContentResolver;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.HashMap;
import java.util.regex.Pattern;
import android.net.Uri;
import com.android.settings.R;

public class Gservices
{
    public static final Uri CONTENT_PREFIX_URI;
    public static final Uri CONTENT_URI;
    public static final Pattern FALSE_PATTERN;
    public static final Pattern TRUE_PATTERN;
    static final HashMap<String, Boolean> sBooleanCache;
    static HashMap<String, String> sCache;
    static final HashMap<String, Float> sFloatCache;
    static final HashMap<String, Integer> sIntCache;
    private static final AtomicBoolean sInvalidateCache;
    static final HashMap<String, Long> sLongCache;
    private static boolean sPreloaded;
    static String[] sPreloadedPrefixes;
    private static Object sVersionToken;

    static {
        CONTENT_URI = Uri.parse("content://com.google.android.gsf.gservices");
        CONTENT_PREFIX_URI = Uri.parse("content://com.google.android.gsf.gservices/prefix");
        TRUE_PATTERN = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
        FALSE_PATTERN = Pattern.compile("^(0|false|f|off|no|n)$", 2);
        sInvalidateCache = new AtomicBoolean();
        sBooleanCache = new HashMap<String, Boolean>();
        sIntCache = new HashMap<String, Integer>();
        sLongCache = new HashMap<String, Long>();
        sFloatCache = new HashMap<String, Float>();
        Gservices.sPreloadedPrefixes = new String[0];
    }

    private static void bulkCacheByPrefixLocked(final ContentResolver contentResolver, final String[] array) {
        Gservices.sCache.putAll(getStringsByPrefix(contentResolver, array));
        Gservices.sPreloaded = true;
    }

    private static void ensureCacheInitializedLocked(final ContentResolver contentResolver) {
        if (Gservices.sCache == null) {
            Gservices.sInvalidateCache.set(false);
            Gservices.sCache = new HashMap<String, String>();
            Gservices.sVersionToken = new Object();
            Gservices.sPreloaded = false;
            contentResolver.registerContentObserver(Gservices.CONTENT_URI, true, (ContentObserver)new ContentObserver(null) {
                public void onChange(final boolean b) {
                    Gservices.sInvalidateCache.set(true);
                }
            });
        }
        else if (Gservices.sInvalidateCache.getAndSet(false)) {
            Gservices.sCache.clear();
            Gservices.sBooleanCache.clear();
            Gservices.sIntCache.clear();
            Gservices.sLongCache.clear();
            Gservices.sFloatCache.clear();
            Gservices.sVersionToken = new Object();
            Gservices.sPreloaded = false;
        }
    }

    public static boolean getBoolean(final ContentResolver contentResolver, final String s, boolean b) {
        final Object versionToken = getVersionToken(contentResolver);
        final Boolean b2 = getValue(Gservices.sBooleanCache, s, b);
        if (b2 != null) {
            return b2;
        }
        final String string = getString(contentResolver, s);
        Boolean b3;
        if (string != null && !string.equals("")) {
            if (Gservices.TRUE_PATTERN.matcher(string).matches()) {
                b = true;
                b3 = true;
            }
            else if (Gservices.FALSE_PATTERN.matcher(string).matches()) {
                b = false;
                b3 = false;
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append("attempt to read gservices key ");
                sb.append(s);
                sb.append(" (value \"");
                sb.append(string);
                sb.append("\") as boolean");
                Log.w("Gservices", sb.toString());
                b3 = b2;
            }
        }
        else {
            b3 = b2;
        }
        putValueAndRemoveFromStringCache(versionToken, Gservices.sBooleanCache, s, b3);
        return b;
    }

    public static long getLong(final ContentResolver contentResolver, final String s, long n) {
        final Object versionToken = getVersionToken(contentResolver);
        final Long n2 = getValue(Gservices.sLongCache, s, n);
        if (n2 != null) {
            return n2;
        }
        final String string = getString(contentResolver, s);
        Long value = null;
        Label_0064: {
            if (string == null) {
                value = n2;
                break Label_0064;
            }
            try {
                final long long1 = Long.parseLong(string);
                value = long1;
                n = long1;
            }
            catch (NumberFormatException ex) {
                value = n2;
            }
        }
        putValueAndRemoveFromStringCache(versionToken, Gservices.sLongCache, s, value);
        return n;
    }

    @Deprecated
    public static String getString(final ContentResolver contentResolver, final String s) {
        return getString(contentResolver, s, null);
    }

  public static String getString(final ContentResolver contentResolver, final String s, String s2) {
    synchronized (Gservices.class) {
      ensureCacheInitializedLocked(contentResolver);
      final Object sVersionToken = Gservices.sVersionToken;
      if (Gservices.sCache.containsKey(s)) {
        String s3 = Gservices.sCache.get(s);
        if (s3 == null) {
          s3 = s2;
        }
        return s3;
      }
      final String[] sPreloadedPrefixes = Gservices.sPreloadedPrefixes;
      for (int length = sPreloadedPrefixes.length, i = 0; i < length; ++i) {
        if (s.startsWith(sPreloadedPrefixes[i])) {
          if (!Gservices.sPreloaded || Gservices.sCache.isEmpty()) {
            bulkCacheByPrefixLocked(contentResolver, Gservices.sPreloadedPrefixes);
            if (Gservices.sCache.containsKey(s)) {
              String s4 = Gservices.sCache.get(s);
              if (s4 == null) {
                s4 = s2;
              }
              return s4;
            }
          }
          return s2;
        }
      }
      // monitorexit(Gservices.class)
      final Cursor query =
          contentResolver.query(
              Gservices.CONTENT_URI,
              (String[]) null,
              (String) null,
              new String[] {s},
              (String) null);
      Label_0252:
      {
        if (query == null) {
          break Label_0252;
        }
        try {
          if (!query.moveToFirst()) {
            break Label_0252;
          }
          final String string = query.getString(1);
          String s5;
          if ((s5 = string) != null) {
            s5 = string;
            if (string.equals(s2)) {
              s5 = s2;
            }
          }
          putStringCache(sVersionToken, s, s5);
          if (s5 != null) {
            s2 = s5;
          }
          if (query != null) {
            query.close();
          }
          return s2;
        } finally {
          if (query != null) {
            query.close();
          }
          query.close();
          {
              putStringCache(sVersionToken, s, null);

              return s2;
            
          }
        }
      }
    }
      return s;
  }
    

    public static Map<String, String> getStringsByPrefix(ContentResolver query, final String... array) {
        query = (ContentResolver)query.query(Gservices.CONTENT_PREFIX_URI, (String[])null, (String)null, array, (String)null);
        final TreeMap<String, String> treeMap = new TreeMap<String, String>();
        if (query == null) {
            return treeMap;
        }
        try {
            while (((Cursor)query).moveToNext()) {
                treeMap.put(((Cursor)query).getString(0), ((Cursor)query).getString(1));
            }
            return treeMap;
        }
        finally {
            ((Cursor)query).close();
        }
    }

    private static <T> T getValue(final HashMap<String, T> hashMap, final String s, final T t) {
        synchronized (Gservices.class) {
            if (hashMap.containsKey(s)) {
                T value = hashMap.get(s);
                if (value == null) {
                    value = t;
                }
                return value;
            }
            return null;
        }
    }

    public static Object getVersionToken(final ContentResolver contentResolver) {
        synchronized (Gservices.class) {
            ensureCacheInitializedLocked(contentResolver);
            return Gservices.sVersionToken;
        }
    }

    private static void putStringCache(final Object o, final String s, final String s2) {
        synchronized (Gservices.class) {
            if (o == Gservices.sVersionToken) {
                Gservices.sCache.put(s, s2);
            }
        }
    }

    private static <T> void putValueAndRemoveFromStringCache(final Object o, final HashMap<String, T> hashMap, final String s, final T t) {
        synchronized (Gservices.class) {
            if (o == Gservices.sVersionToken) {
                hashMap.put(s, t);
                Gservices.sCache.remove(s);
            }
        }
    }
}

