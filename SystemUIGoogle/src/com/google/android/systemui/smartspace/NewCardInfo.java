package com.google.android.systemui.smartspace;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.android.systemui.smartspace.nano.CardWrapper;
import com.android.systemui.smartspace.nano.SmartspaceUpdate;

import java.io.ByteArrayOutputStream;

public class NewCardInfo {
    private SmartspaceUpdate.SmartspaceCard mCard;
    private Intent mIntent;
    private boolean mIsPrimary;
    private PackageInfo mPackageInfo;
    private long mPublishTime;

    public NewCardInfo(SmartspaceUpdate.SmartspaceCard smartspaceCard, Intent intent, boolean z, long j, PackageInfo packageInfo) {
        mCard = smartspaceCard;
        mIsPrimary = z;
        mIntent = intent;
        mPublishTime = j;
        mPackageInfo = packageInfo;
    }

    private static <T> T retrieveFromIntent(String str, Intent intent) {
        if (!TextUtils.isEmpty(str)) {
            return intent.getParcelableExtra(str);
        }
        return null;
    }

    static Bitmap createIconBitmap(Intent.ShortcutIconResource shortcutIconResource, Context context) {
        try {
            Resources resourcesForApplication = context.getPackageManager().getResourcesForApplication(shortcutIconResource.packageName);
            if (resourcesForApplication != null) {
                return BitmapFactory.decodeResource(resourcesForApplication, resourcesForApplication.getIdentifier(shortcutIconResource.resourceName, null, null));
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public boolean isPrimary() {
        return mIsPrimary;
    }

    public Bitmap retrieveIcon(Context context) {
        SmartspaceUpdate.SmartspaceCard.Image image = mCard.icon;
        if (image == null) {
            return null;
        }
        Bitmap bitmap = retrieveFromIntent(image.key, mIntent);
        if (bitmap != null) {
            return bitmap;
        }
        try {
            if (!TextUtils.isEmpty(image.uri)) {
                return MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(image.uri));
            }
            if (!TextUtils.isEmpty(image.gsaResourceName)) {
                Intent.ShortcutIconResource shortcutIconResource = new Intent.ShortcutIconResource();
                shortcutIconResource.packageName = "com.google.android.googlequicksearchbox";
                shortcutIconResource.resourceName = image.gsaResourceName;
                return createIconBitmap(shortcutIconResource, context);
            }
            return null;
        } catch (Exception unused) {
            Log.e("NewCardInfo", "retrieving bitmap uri=" + image.uri + " gsaRes=" + image.gsaResourceName);
        }
        return bitmap;
    }

    public CardWrapper toWrapper(Context context) {
        CardWrapper CardWrapper = new CardWrapper();
        Bitmap retrieveIcon = retrieveIcon(context);
        if (retrieveIcon != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            retrieveIcon.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            CardWrapper.icon = byteArrayOutputStream.toByteArray();
        }
        CardWrapper.card = mCard;
        CardWrapper.publishTime = mPublishTime;
        PackageInfo packageInfo = mPackageInfo;
        if (packageInfo != null) {
            CardWrapper.gsaVersionCode = packageInfo.versionCode;
            CardWrapper.gsaUpdateTime = packageInfo.lastUpdateTime;
        }
        return CardWrapper;
    }

    public int getUserId() {
        return mIntent.getIntExtra("uid", -1);
    }

    public boolean shouldDiscard() {
        SmartspaceUpdate.SmartspaceCard smartspaceCard = mCard;
        return smartspaceCard == null || smartspaceCard.shouldDiscard;
    }
}
