package com.google.android.settings.widget;

import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.content.res.Resources.Theme;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;

public class MarlinColorImageView extends ImageView
{
    public MarlinColorImageView(final Context context) {
        super(context);
    }
    
    public MarlinColorImageView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    private Theme getDeviceColorTheme() {
        final Theme theme = this.getResources().newTheme();
        final String value = SystemProperties.get("ro.boot.hardware.color");
        if ("BLU00".equals(value)) {
            theme.applyStyle(2131951822, true);
        }
        else if ("SLV00".equals(value)) {
            theme.applyStyle(2131951824, true);
        }
        else if ("GRA00".equals(value)) {
            theme.applyStyle(2131951823, true);
        }
        else {
            theme.applyStyle(2131951821, true);
        }
        return theme;
    }
    
    public void setImageDrawable(Drawable mutate) {
        mutate = mutate.mutate();
        mutate.applyTheme(this.getDeviceColorTheme());
        super.setImageDrawable(mutate);
    }
}
