package com.google.android.setupwizard.deviceorigin;

import android.net.Uri;
import android.util.Log;
import android.content.Context;
import android.database.Cursor;

public final class DeviceOrigin
{
    private static final String[] COLUMN_VALUE_ARRAY;
    
    static {
        COLUMN_VALUE_ARRAY = new String[] { "value" };
    }
    
    private static void closeCursor(final Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            }
            catch (Exception ex) {}
        }
    }
    
    private static Cursor getCursorForKey(final Context context, final String s, final String s2) {
        final Cursor query = context.getContentResolver().query(getUriForKey(s), DeviceOrigin.COLUMN_VALUE_ARRAY, "type=?", new String[] { s2 }, (String)null);
        if (query == null) {
            return null;
        }
        final int count = query.getCount();
        if (count == 1) {
            query.moveToFirst();
            return query;
        }
        if (count == 0) {
            closeCursor(query);
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Multiple values found for key=");
        sb.append(s);
        Log.w("DeviceOrigin", sb.toString());
        return null;
    }
    
    public static String getString(final Context context, String string, final String s) {
        Cursor cursor = null;
        try {
            final Cursor cursorForKey = getCursorForKey(context, string, "java.lang.String");
            if (cursorForKey != null) {
                cursor = cursorForKey;
                string = cursorForKey.getString(0);
                return string;
            }
            return s;
        }
        finally {
            closeCursor(cursor);
        }
    }
    
    private static Uri getUriForKey(final String s) {
        return Contract.VALUE_URI.buildUpon().appendEncodedPath(s).build();
    }
    
    public static final class Contract
    {
        public static final Uri CONTENT_URI;
        public static final Uri LIST_URI;
        public static final Uri VALUE_URI;
        
        static {
            CONTENT_URI = Uri.parse("content://com.google.android.setupwizard.deviceorigin");
            LIST_URI = Contract.CONTENT_URI.buildUpon().appendPath("list").build();
            VALUE_URI = Contract.CONTENT_URI.buildUpon().appendPath("value").build();
        }
    }
}
