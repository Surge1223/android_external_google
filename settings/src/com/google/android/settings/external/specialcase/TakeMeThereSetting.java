package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.database.Cursor;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class TakeMeThereSetting implements Queryable
{
    public final String mIntentString;
    
    public TakeMeThereSetting(final String mIntentString) {
        this.mIntentString = mIntentString;
    }
    
    public Cursor getAccessCursor(final Context context) {
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)0).add("availability", (Object)5).add("intent", (Object)this.mIntentString);
        return (Cursor)matrixCursor;
    }
}
