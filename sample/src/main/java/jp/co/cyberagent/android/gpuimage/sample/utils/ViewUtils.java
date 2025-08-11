package jp.co.cyberagent.android.gpuimage.sample.utils;

import android.view.View;
import androidx.core.view.ViewCompat;

public class ViewUtils {

    public static void doOnLayout(View view, ViewAction action) {
        if (ViewCompat.isLaidOut(view) && !view.isLayoutRequested()) {
            action.run(view);
        } else {
            doOnNextLayout(view, action);
        }
    }

    public static void doOnNextLayout(View view, ViewAction action) {
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(
                    View v,
                    int left,
                    int top,
                    int right,
                    int bottom,
                    int oldLeft,
                    int oldTop,
                    int oldRight,
                    int oldBottom
            ) {
                view.removeOnLayoutChangeListener(this);
                action.run(view);
            }
        });
    }

    // Functional interface to replace Kotlin's lambda
    public interface ViewAction {
        void run(View view);
    }
}
