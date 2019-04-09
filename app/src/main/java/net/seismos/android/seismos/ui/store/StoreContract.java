package net.seismos.android.seismos.ui.store;

import net.seismos.android.seismos.BasePresenter;
import net.seismos.android.seismos.BaseView;

public interface StoreContract {

    interface View extends BaseView<Presenter> {
        void showSomething();
    }

    interface Presenter extends BasePresenter {
        void somethingHappened();
    }
}
