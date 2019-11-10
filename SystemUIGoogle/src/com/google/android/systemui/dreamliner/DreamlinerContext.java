package com.google.android.systemui.dreamliner;

import android.content.Context;

public class DreamlinerContext {
    private Context mContext;

    public DreamlinerContext(Context context) {
        mContext = context;
    }

    public boolean isAvailable() {
        // TODO: enable back when everything gets forward ported
        return /* mContext.getResources().getBoolean(
                com.android.internal.R.bool.config_enableDreamlinerService); */ false;
    }
}
