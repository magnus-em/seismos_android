package net.seismos.android.seismos.ui.profile;

import net.seismos.android.seismos.ui.BasePresenter;
import net.seismos.android.seismos.ui.BaseView;

public interface ProfileContract {

    interface View extends BaseView<Presenter> {
        void showSomething();
    }

    interface Presenter extends BasePresenter {
        void somethingHappened();
    }
}
