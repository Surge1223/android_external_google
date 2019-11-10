package com.google.android.systemui.smartspace;

import android.content.Context;
import android.util.Log;
import com.google.protobuf.nano.MessageNano;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ProtoStore {
    private final Context mContext;

    public ProtoStore(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void store(MessageNano messageNano, String str) {
        try {
            FileOutputStream openFileOutput = this.mContext.openFileOutput(str, 0);
            if (messageNano != null) {
                try {
                    openFileOutput.write(MessageNano.toByteArray(messageNano));
                } catch (Throwable th) {
                    if (openFileOutput != null) {
                        $closeResource(th, openFileOutput);
                    }
                    throw th;
                }
            } else {
                Log.d("ProtoStore", "deleting " + str);
                this.mContext.deleteFile(str);
            }
            if (openFileOutput != null) {
                $closeResource((Throwable) null, openFileOutput);
            }
        } catch (FileNotFoundException unused) {
            Log.d("ProtoStore", "file does not exist");
        } catch (Exception e) {
            Log.e("ProtoStore", "unable to write file", e);
        }
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0024, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        $closeResource(r5, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0028, code lost:
        throw r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T extends com.google.protobuf.nano.MessageNano> boolean load(java.lang.String r6, T r7) {
        /*
            r5 = this;
            java.lang.String r0 = "ProtoStore"
            android.content.Context r5 = r5.mContext
            java.io.File r5 = r5.getFileStreamPath(r6)
            r6 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0030, Exception -> 0x0029 }
            r1.<init>(r5)     // Catch:{ FileNotFoundException -> 0x0030, Exception -> 0x0029 }
            r2 = 0
            long r3 = r5.length()     // Catch:{ all -> 0x0022 }
            int r5 = (int) r3     // Catch:{ all -> 0x0022 }
            byte[] r5 = new byte[r5]     // Catch:{ all -> 0x0022 }
            int r3 = r5.length     // Catch:{ all -> 0x0022 }
            r1.read(r5, r6, r3)     // Catch:{ all -> 0x0022 }
            MessageNano.mergeFrom(r7, r5)     // Catch:{ all -> 0x0022 }
            r5 = 1
            $closeResource(r2, r1)     // Catch:{ FileNotFoundException -> 0x0030, Exception -> 0x0029 }
            return r5
        L_0x0022:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x0024 }
        L_0x0024:
            r7 = move-exception
            $closeResource(r5, r1)     // Catch:{ FileNotFoundException -> 0x0030, Exception -> 0x0029 }
            throw r7     // Catch:{ FileNotFoundException -> 0x0030, Exception -> 0x0029 }
        L_0x0029:
            r5 = move-exception
            java.lang.String r7 = "unable to load data"
            android.util.Log.e(r0, r7, r5)
            return r6
        L_0x0030:
            java.lang.String r5 = "no cached data"
            android.util.Log.d(r0, r5)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: ProtoStore.load(java.lang.String, MessageNano):boolean");
    }
}
