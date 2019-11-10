package com.google.android.systemui.smartspace;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.android.systemui.C1735R$dimen;
import com.android.systemui.C1741R$plurals;
import com.android.systemui.C1743R$string;
import com.android.systemui.smartspace.nano.SmartspaceProto$CardWrapper;
import com.android.systemui.smartspace.nano.SmartspaceProto$SmartspaceUpdate;

public class SmartSpaceCard {
    private static int sRequestCode;
    private final SmartspaceProto$SmartspaceUpdate.SmartspaceCard mCard;
    private final Context mContext;
    private Bitmap mIcon;
    private boolean mIconProcessed;
    private final Intent mIntent;
    private final boolean mIsIconGrayscale;
    private final boolean mIsWeather;
    private final long mPublishTime;
    private int mRequestCode;

    public SmartSpaceCard(Context context, SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard, Intent intent, boolean z, Bitmap bitmap, boolean z2, long j) {
        this.mContext = context.getApplicationContext();
        this.mCard = smartspaceCard;
        this.mIsWeather = z;
        this.mIntent = intent;
        this.mIcon = bitmap;
        this.mPublishTime = j;
        this.mIsIconGrayscale = z2;
        int i = sRequestCode + 1;
        sRequestCode = i;
        if (i > 2147483646) {
            sRequestCode = 0;
        }
        this.mRequestCode = sRequestCode;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public Bitmap getIcon() {
        return this.mIcon;
    }

    public void setIcon(Bitmap bitmap) {
        this.mIcon = bitmap;
    }

    public void setIconProcessed(boolean z) {
        this.mIconProcessed = z;
    }

    public boolean isIconProcessed() {
        return this.mIconProcessed;
    }

    public String getTitle() {
        return substitute(true);
    }

    public CharSequence getFormattedTitle() {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam[] formatParamArr;
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message = getMessage();
        if (message == null) {
            return "";
        }
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText formattedText = message.title;
        if (formattedText != null) {
            String str = formattedText.text;
            if (str != null) {
                if (!hasParams(formattedText)) {
                    return str;
                }
                String str2 = null;
                String str3 = null;
                int i = 0;
                while (true) {
                    formatParamArr = formattedText.formatParam;
                    if (i >= formatParamArr.length) {
                        break;
                    }
                    SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam formatParam = formatParamArr[i];
                    if (formatParam != null) {
                        int i2 = formatParam.formatParamArgs;
                        if (i2 == 1 || i2 == 2) {
                            str3 = getDurationText(formatParam);
                        } else if (i2 == 3) {
                            str2 = formatParam.text;
                        }
                    }
                    i++;
                }
                if (this.mCard.cardType == 3 && formatParamArr.length == 2) {
                    str3 = formatParamArr[0].text;
                    str2 = formatParamArr[1].text;
                }
                if (str2 == null) {
                    return "";
                }
                if (str3 == null) {
                    if (message != this.mCard.duringEvent) {
                        return str;
                    }
                    str3 = this.mContext.getString(C1743R$string.smartspace_now);
                }
                return this.mContext.getString(C1743R$string.smartspace_pill_text_format, new Object[]{str3, str2});
            }
        }
        return "";
    }

    public String getSubtitle() {
        return substitute(false);
    }

    private SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message getMessage() {
        long currentTimeMillis = System.currentTimeMillis();
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
        long j = smartspaceCard.eventTimeMillis;
        long j2 = smartspaceCard.eventDurationMillis + j;
        if (currentTimeMillis < j) {
            SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message = smartspaceCard.preEvent;
            if (message != null) {
                return message;
            }
        }
        if (currentTimeMillis > j2) {
            SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message2 = this.mCard.postEvent;
            if (message2 != null) {
                return message2;
            }
        }
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message3 = this.mCard.duringEvent;
        if (message3 != null) {
            return message3;
        }
        return null;
    }

    private SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText getFormattedText(boolean z) {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message message = getMessage();
        if (message != null) {
            return z ? message.title : message.subtitle;
        }
        return null;
    }

    private boolean hasParams(SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText formattedText) {
        if (!(formattedText == null || formattedText.text == null)) {
            SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam[] formatParamArr = formattedText.formatParam;
            return formatParamArr != null && formatParamArr.length > 0;
        }
    }

    /* access modifiers changed from: package-private */
    public long getMillisToEvent(SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam formatParam) {
        long j;
        if (formatParam.formatParamArgs == 2) {
            SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
            j = smartspaceCard.eventTimeMillis + smartspaceCard.eventDurationMillis;
        } else {
            j = this.mCard.eventTimeMillis;
        }
        return Math.abs(System.currentTimeMillis() - j);
    }

    private int getMinutesToEvent(SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam formatParam) {
        return (int) Math.ceil(((double) getMillisToEvent(formatParam)) / 60000.0d);
    }

    private String substitute(boolean z) {
        return substitute(z, (String) null);
    }

    private String[] getTextArgs(SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam[] formatParamArr, String str) {
        String[] strArr = new String[formatParamArr.length];
        for (int i = 0; i < strArr.length; i++) {
            int i2 = formatParamArr[i].formatParamArgs;
            if (i2 == 1 || i2 == 2) {
                strArr[i] = getDurationText(formatParamArr[i]);
            } else {
                String str2 = "";
                if (i2 != 3) {
                    strArr[i] = str2;
                } else if (str == null || formatParamArr[i].truncateLocation == 0) {
                    if (formatParamArr[i].text != null) {
                        str2 = formatParamArr[i].text;
                    }
                    strArr[i] = str2;
                } else {
                    strArr[i] = str;
                }
            }
        }
        return strArr;
    }

    private String getDurationText(SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText.FormatParam formatParam) {
        int minutesToEvent = getMinutesToEvent(formatParam);
        if (minutesToEvent >= 60) {
            int i = minutesToEvent / 60;
            int i2 = minutesToEvent % 60;
            String quantityString = this.mContext.getResources().getQuantityString(C1741R$plurals.smartspace_hours, i, new Object[]{Integer.valueOf(i)});
            if (i2 <= 0) {
                return quantityString;
            }
            String quantityString2 = this.mContext.getResources().getQuantityString(C1741R$plurals.smartspace_minutes, i2, new Object[]{Integer.valueOf(i2)});
            return this.mContext.getString(C1743R$string.smartspace_hours_mins, new Object[]{quantityString, quantityString2});
        }
        return this.mContext.getResources().getQuantityString(C1741R$plurals.smartspace_minutes, minutesToEvent, new Object[]{Integer.valueOf(minutesToEvent)});
    }

    private String substitute(boolean z, String str) {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.Message.FormattedText formattedText = getFormattedText(z);
        if (formattedText == null) {
            return "";
        }
        String str2 = formattedText.text;
        if (str2 != null) {
            return hasParams(formattedText) ? String.format(str2, getTextArgs(formattedText.formatParam, str)) : str2;
        }
        return "";
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > getExpiration();
    }

    public long getExpiration() {
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard smartspaceCard = this.mCard;
        if (smartspaceCard == null) {
            return 0;
        }
        SmartspaceProto$SmartspaceUpdate.SmartspaceCard.ExpiryCriteria expiryCriteria = smartspaceCard.expiryCriteria;
        if (expiryCriteria != null) {
            return expiryCriteria.expirationTimeMillis;
        }
        return 0;
    }

    public String toString() {
        return "title:" + getTitle() + " subtitle:" + getSubtitle() + " expires:" + getExpiration() + " published:" + this.mPublishTime;
    }

    static SmartSpaceCard fromWrapper(Context context, SmartspaceProto$CardWrapper smartspaceProto$CardWrapper, boolean z) {
        if (smartspaceProto$CardWrapper == null) {
            return null;
        }
        try {
            Intent parseUri = (smartspaceProto$CardWrapper.card.tapAction == null || TextUtils.isEmpty(smartspaceProto$CardWrapper.card.tapAction.intent)) ? null : Intent.parseUri(smartspaceProto$CardWrapper.card.tapAction.intent, 0);
            Bitmap decodeByteArray = smartspaceProto$CardWrapper.icon != null ? BitmapFactory.decodeByteArray(smartspaceProto$CardWrapper.icon, 0, smartspaceProto$CardWrapper.icon.length, (BitmapFactory.Options) null) : null;
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(C1735R$dimen.header_icon_size);
            if (decodeByteArray != null && decodeByteArray.getHeight() > dimensionPixelSize) {
                decodeByteArray = Bitmap.createScaledBitmap(decodeByteArray, (int) (((float) decodeByteArray.getWidth()) * (((float) dimensionPixelSize) / ((float) decodeByteArray.getHeight()))), dimensionPixelSize, true);
            }
            SmartSpaceCard smartSpaceCard = new SmartSpaceCard(context, smartspaceProto$CardWrapper.card, parseUri, z, decodeByteArray, smartspaceProto$CardWrapper.isIconGrayscale, smartspaceProto$CardWrapper.publishTime);
            return smartSpaceCard;
        } catch (Exception e) {
            Log.e("SmartspaceCard", "from proto", e);
            return null;
        }
    }

    public void performCardAction(View view) {
        if (this.mCard.tapAction == null) {
            Log.e("SmartspaceCard", "no tap action available: " + this);
            return;
        }
        Intent intent = new Intent(getIntent());
        int i = this.mCard.tapAction.actionType;
        if (i == 1) {
            intent.addFlags(268435456);
            intent.setPackage("com.google.android.googlequicksearchbox");
            try {
                view.getContext().sendBroadcast(intent);
            } catch (SecurityException e) {
                Log.w("SmartspaceCard", "Cannot perform click action", e);
            }
        } else if (i != 2) {
            Log.w("SmartspaceCard", "unrecognized tap action: " + this.mCard.tapAction.actionType);
        } else {
            this.mContext.startActivity(intent);
        }
    }

    public PendingIntent getPendingIntent() {
        if (this.mCard.tapAction == null) {
            return null;
        }
        Intent intent = new Intent(getIntent());
        int i = this.mCard.tapAction.actionType;
        if (i == 1) {
            intent.addFlags(268435456);
            intent.setPackage("com.google.android.googlequicksearchbox");
            return PendingIntent.getBroadcast(this.mContext, this.mRequestCode, intent, 0);
        } else if (i != 2) {
            return null;
        } else {
            return PendingIntent.getActivity(this.mContext, this.mRequestCode, intent, 0);
        }
    }
}
