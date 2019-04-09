package net.seismos.android.seismos.ui.profile;

import net.seismos.android.seismos.BasePresenter;
import net.seismos.android.seismos.BaseView;

public interface ProfileContract {

    interface View extends BaseView<Presenter> {
        void showSomething();
    }

    interface Presenter extends BasePresenter {
        void somethingHappened();
    }
}
