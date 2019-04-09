package net.seismos.android.seismos.ui.home;

import net.seismos.android.seismos.BasePresenter;
import net.seismos.android.seismos.BaseView;

public interface HomeContract {

    interface View extends BaseView<Presenter> {
        void showSomething();
    }

    interface Presenter extends BasePresenter {
        void somethingHappened();
    }
}
