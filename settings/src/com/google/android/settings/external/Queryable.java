package com.google.android.settings.external;

import android.database.Cursor;
import com.android.settings.search.DatabaseIndexingUtils;
import android.app.Fragment;
import android.content.Context;

public interface Queryable
{
    default String getIntentString(final Context context, final String s, final Class<? extends Fragment> clazz, final String s2) {
        return DatabaseIndexingUtils.buildSearchResultPageIntent(context, clazz.getName(), s, s2, 1033).toUri(0);
    }
    
    default Cursor getUpdateCursor(final Context context, final float n) {
        throw new UnsupportedOperationException("Method not supported");
    }
    
    default Cursor getUpdateCursor(final Context context, final int n) {
        throw new UnsupportedOperationException("Method not supported");
    }
    
    default Cursor getUpdateCursor(final Context context, final String s) {
        try {
            return this.getUpdateCursor(context, Integer.valueOf(s));
        }
        catch (NumberFormatException ex) {
            return this.getUpdateCursor(context, Float.valueOf(s));
        }
    }
    
    default boolean shouldChangeValue(final int n, final int n2, final int n3) {
        return n == 0 && n2 != n3;
    }
    
    default boolean shouldChangeValue(final int n, final long n2, final long n3) {
        return n == 0 && n2 != n3;
    }
}
