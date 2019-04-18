package net.seismos.android.seismos.ui.map;

import com.google.android.gms.maps.model.LatLng;

import net.seismos.android.seismos.BasePresenter;
import net.seismos.android.seismos.BaseView;

public interface MapContract {

    interface View extends BaseView<Presenter> {
        void showSomething();
        void toggleBottomSheet();
        void navigateToLatLng(LatLng latLng);
    }

    interface Presenter extends BasePresenter {
        void somethingHappened();
        void mapClicked();
        void openMapToLatLng(LatLng latLng);
    }
}
