package com.google.android.systemui.keyguard;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.builders.ListBuilder;
import com.android.systemui.C1735R$dimen;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.google.android.systemui.smartspace.SmartSpaceCard;
import com.google.android.systemui.smartspace.SmartSpaceController;
import com.google.android.systemui.smartspace.SmartSpaceData;
import com.google.android.systemui.smartspace.SmartSpaceUpdateListener;
import java.lang.ref.WeakReference;

public class KeyguardSliceProviderGoogle extends KeyguardSliceProvider implements SmartSpaceUpdateListener {
    private static final boolean DEBUG = Log.isLoggable("KeyguardSliceProvider", 3);
    private final Uri mCalendarUri = Uri.parse("content://com.android.systemui.keyguard/smartSpace/calendar");
    private SmartSpaceData mSmartSpaceData;
    private final Uri mWeatherUri = Uri.parse("content://com.android.systemui.keyguard/smartSpace/weather");

    private static class AddShadowTask extends AsyncTask<Bitmap, Void, Bitmap> {
        private final float mBlurRadius;
        private final WeakReference<KeyguardSliceProviderGoogle> mProviderReference;
        private final SmartSpaceCard mWeatherCard;

        AddShadowTask(KeyguardSliceProviderGoogle keyguardSliceProviderGoogle, SmartSpaceCard smartSpaceCard) {
            this.mProviderReference = new WeakReference<>(keyguardSliceProviderGoogle);
            this.mWeatherCard = smartSpaceCard;
            this.mBlurRadius = keyguardSliceProviderGoogle.getContext().getResources().getDimension(C1735R$dimen.smartspace_icon_shadow);
        }

        /* access modifiers changed from: protected */
        public Bitmap doInBackground(Bitmap... bitmapArr) {
            return applyShadow(bitmapArr[0]);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bitmap bitmap) {
            KeyguardSliceProviderGoogle keyguardSliceProviderGoogle;
            synchronized (this) {
                this.mWeatherCard.setIcon(bitmap);
                keyguardSliceProviderGoogle = (KeyguardSliceProviderGoogle) this.mProviderReference.get();
            }
            if (keyguardSliceProviderGoogle != null) {
                keyguardSliceProviderGoogle.notifyChange();
            }
        }

        private Bitmap applyShadow(Bitmap bitmap) {
            BlurMaskFilter blurMaskFilter = new BlurMaskFilter(this.mBlurRadius, BlurMaskFilter.Blur.NORMAL);
            Paint paint = new Paint();
            paint.setMaskFilter(blurMaskFilter);
            int[] iArr = new int[2];
            Bitmap extractAlpha = bitmap.extractAlpha(paint, iArr);
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint2 = new Paint();
            paint2.setAlpha(70);
            canvas.drawBitmap(extractAlpha, (float) iArr[0], ((float) iArr[1]) + (this.mBlurRadius / 2.0f), paint2);
            extractAlpha.recycle();
            paint2.setAlpha(255);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint2);
            return createBitmap;
        }
    }

    public boolean onCreateSliceProvider() {
        boolean onCreateSliceProvider = super.onCreateSliceProvider();
        SmartSpaceController.get(getContext()).addListener(this);
        this.mSmartSpaceData = new SmartSpaceData();
        return onCreateSliceProvider;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00b7, code lost:
        r6 = r7.build();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00bd, code lost:
        if (DEBUG == false) goto L_0x00d5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00bf, code lost:
        android.util.Log.d("KeyguardSliceProvider", "Binding slice: " + r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00d5, code lost:
        android.os.Trace.endSection();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00d8, code lost:
        return r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public androidx.slice.Slice onBindSlice(android.net.Uri r7) {
        /*
            r6 = this;
            java.lang.String r7 = "KeyguardSliceProviderGoogle#onBindSlice"
            android.os.Trace.beginSection(r7)
            androidx.slice.builders.ListBuilder r7 = new androidx.slice.builders.ListBuilder
            android.content.Context r0 = r6.getContext()
            android.net.Uri r1 = r6.mSliceUri
            r2 = -1
            r7.<init>(r0, r1, r2)
            monitor-enter(r6)
            SmartSpaceData r0 = r6.mSmartSpaceData     // Catch:{ all -> 0x00d9 }
            SmartSpaceCard r0 = r0.getCurrentCard()     // Catch:{ all -> 0x00d9 }
            if (r0 == 0) goto L_0x008f
            boolean r1 = r0.isExpired()     // Catch:{ all -> 0x00d9 }
            if (r1 != 0) goto L_0x008f
            java.lang.String r1 = r0.getTitle()     // Catch:{ all -> 0x00d9 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x00d9 }
            if (r1 != 0) goto L_0x008f
            android.graphics.Bitmap r1 = r0.getIcon()     // Catch:{ all -> 0x00d9 }
            r2 = 0
            if (r1 != 0) goto L_0x0034
            r1 = r2
            goto L_0x0038
        L_0x0034:
            androidx.core.graphics.drawable.IconCompat r1 = androidx.core.graphics.drawable.IconCompat.createWithBitmap(r1)     // Catch:{ all -> 0x00d9 }
        L_0x0038:
            android.app.PendingIntent r3 = r0.getPendingIntent()     // Catch:{ all -> 0x00d9 }
            r4 = 1
            if (r1 == 0) goto L_0x004a
            if (r3 != 0) goto L_0x0042
            goto L_0x004a
        L_0x0042:
            java.lang.String r2 = r0.getTitle()     // Catch:{ all -> 0x00d9 }
            androidx.slice.builders.SliceAction r2 = androidx.slice.builders.SliceAction.create(r3, r1, r4, r2)     // Catch:{ all -> 0x00d9 }
        L_0x004a:
            androidx.slice.builders.ListBuilder$HeaderBuilder r3 = new androidx.slice.builders.ListBuilder$HeaderBuilder     // Catch:{ all -> 0x00d9 }
            android.net.Uri r5 = r6.mHeaderUri     // Catch:{ all -> 0x00d9 }
            r3.<init>(r5)     // Catch:{ all -> 0x00d9 }
            java.lang.CharSequence r5 = r0.getFormattedTitle()     // Catch:{ all -> 0x00d9 }
            r3.setTitle(r5)     // Catch:{ all -> 0x00d9 }
            if (r2 == 0) goto L_0x005d
            r3.setPrimaryAction(r2)     // Catch:{ all -> 0x00d9 }
        L_0x005d:
            r7.setHeader(r3)     // Catch:{ all -> 0x00d9 }
            java.lang.String r0 = r0.getSubtitle()     // Catch:{ all -> 0x00d9 }
            if (r0 == 0) goto L_0x007d
            androidx.slice.builders.ListBuilder$RowBuilder r3 = new androidx.slice.builders.ListBuilder$RowBuilder     // Catch:{ all -> 0x00d9 }
            android.net.Uri r5 = r6.mCalendarUri     // Catch:{ all -> 0x00d9 }
            r3.<init>(r5)     // Catch:{ all -> 0x00d9 }
            r3.setTitle(r0)     // Catch:{ all -> 0x00d9 }
            if (r1 == 0) goto L_0x0075
            r3.addEndItem(r1, r4)     // Catch:{ all -> 0x00d9 }
        L_0x0075:
            if (r2 == 0) goto L_0x007a
            r3.setPrimaryAction(r2)     // Catch:{ all -> 0x00d9 }
        L_0x007a:
            r7.addRow(r3)     // Catch:{ all -> 0x00d9 }
        L_0x007d:
            r6.addWeather(r7)     // Catch:{ all -> 0x00d9 }
            r6.addZenModeLocked(r7)     // Catch:{ all -> 0x00d9 }
            r6.addPrimaryActionLocked(r7)     // Catch:{ all -> 0x00d9 }
            android.os.Trace.endSection()     // Catch:{ all -> 0x00d9 }
            androidx.slice.Slice r7 = r7.build()     // Catch:{ all -> 0x00d9 }
            monitor-exit(r6)     // Catch:{ all -> 0x00d9 }
            return r7
        L_0x008f:
            boolean r0 = r6.needsMediaLocked()     // Catch:{ all -> 0x00d9 }
            if (r0 == 0) goto L_0x0099
            r6.addMediaLocked(r7)     // Catch:{ all -> 0x00d9 }
            goto L_0x00aa
        L_0x0099:
            androidx.slice.builders.ListBuilder$RowBuilder r0 = new androidx.slice.builders.ListBuilder$RowBuilder     // Catch:{ all -> 0x00d9 }
            android.net.Uri r1 = r6.mDateUri     // Catch:{ all -> 0x00d9 }
            r0.<init>(r1)     // Catch:{ all -> 0x00d9 }
            java.lang.String r1 = r6.getFormattedDateLocked()     // Catch:{ all -> 0x00d9 }
            r0.setTitle(r1)     // Catch:{ all -> 0x00d9 }
            r7.addRow(r0)     // Catch:{ all -> 0x00d9 }
        L_0x00aa:
            r6.addWeather(r7)     // Catch:{ all -> 0x00d9 }
            r6.addNextAlarmLocked(r7)     // Catch:{ all -> 0x00d9 }
            r6.addZenModeLocked(r7)     // Catch:{ all -> 0x00d9 }
            r6.addPrimaryActionLocked(r7)     // Catch:{ all -> 0x00d9 }
            monitor-exit(r6)     // Catch:{ all -> 0x00d9 }
            androidx.slice.Slice r6 = r7.build()
            boolean r7 = DEBUG
            if (r7 == 0) goto L_0x00d5
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r0 = "Binding slice: "
            r7.append(r0)
            r7.append(r6)
            java.lang.String r7 = r7.toString()
            java.lang.String r0 = "KeyguardSliceProvider"
            android.util.Log.d(r0, r7)
        L_0x00d5:
            android.os.Trace.endSection()
            return r6
        L_0x00d9:
            r7 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x00d9 }
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: KeyguardSliceProviderGoogle.onBindSlice(android.net.Uri):androidx.slice.Slice");
    }

    private void addWeather(ListBuilder listBuilder) {
        SmartSpaceCard weatherCard = this.mSmartSpaceData.getWeatherCard();
        if (weatherCard != null && !weatherCard.isExpired()) {
            ListBuilder.RowBuilder rowBuilder = new ListBuilder.RowBuilder(this.mWeatherUri);
            rowBuilder.setTitle(weatherCard.getTitle());
            Bitmap icon = weatherCard.getIcon();
            if (icon != null) {
                IconCompat createWithBitmap = IconCompat.createWithBitmap(icon);
                createWithBitmap.setTintMode(PorterDuff.Mode.DST);
                rowBuilder.addEndItem(createWithBitmap, 1);
            }
            listBuilder.addRow(rowBuilder);
        }
    }

    public void onSmartSpaceUpdated(SmartSpaceData smartSpaceData) {
        synchronized (this) {
            this.mSmartSpaceData = smartSpaceData;
        }
        SmartSpaceCard weatherCard = smartSpaceData.getWeatherCard();
        if (weatherCard == null || weatherCard.getIcon() == null || weatherCard.isIconProcessed()) {
            notifyChange();
            return;
        }
        weatherCard.setIconProcessed(true);
        new AddShadowTask(this, weatherCard).execute(new Bitmap[]{weatherCard.getIcon()});
    }

    /* access modifiers changed from: protected */
    public void updateClockLocked() {
        notifyChange();
    }
}
