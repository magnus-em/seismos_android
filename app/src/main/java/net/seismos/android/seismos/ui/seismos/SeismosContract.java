package net.seismos.android.seismos.ui.seismos;

import net.seismos.android.seismos.BasePresenter;
import net.seismos.android.seismos.BaseView;

public interface SeismosContract {

    interface View extends BaseView<Presenter> {
        void showSomething();
    }

    interface Presenter extends BasePresenter {
        void somethingHappened();
    }
}
