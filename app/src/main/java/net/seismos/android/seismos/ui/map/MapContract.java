package net.seismos.android.seismos.ui.map;

import net.seismos.android.seismos.BasePresenter;
import net.seismos.android.seismos.BaseView;

public interface MapContract {

    interface View extends BaseView<Presenter> {
        void showSomething();
        void toggleBottomSheet();
    }

    interface Presenter extends BasePresenter {
        void somethingHappened();
        void mapClicked();
    }
}
