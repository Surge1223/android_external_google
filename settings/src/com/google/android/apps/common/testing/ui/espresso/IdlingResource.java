package com.google.android.apps.common.testing.ui.espresso;

public interface IdlingResource
{
    public interface ResourceCallback
    {
        void onTransitionToIdle();
    }
}
