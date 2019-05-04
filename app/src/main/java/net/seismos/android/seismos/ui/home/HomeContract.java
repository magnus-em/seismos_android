package net.seismos.android.seismos.ui.home;

import android.content.Context;

import net.seismos.android.seismos.BasePresenter;
import net.seismos.android.seismos.BaseView;

public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void showSomething();
        Context getContextHandle();
    }

    interface Presenter extends BasePresenter {
        void somethingHappened();
        void onResume();
        void onPause();
        void onStop();
    }
}
