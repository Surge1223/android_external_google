package com.google.android.settings.external.specialcase;

import android.net.Uri;
import java.util.function.Function;
import java.util.function.IntFunction;

 class ScreenFunctions implements Function {
    public static ScreenFunctions INSTANCE= new ScreenFunctions();

    static class C10251 implements IntFunction {
        static C10251 INSTANCE= new C10251();

        private  Object someobj(int i) {
            return Integer.toString(i);
        }

        private  C10251() {
        }

        public  Object apply(int i) {
            return someobj(i);
        }
    }

    private   Object someobj(Object obj) {
        return Uri.encode((String) obj);
    }

    private  ScreenFunctions() {
    }

    public  Object apply(Object obj) {
        return someobj(obj);
    }
}
