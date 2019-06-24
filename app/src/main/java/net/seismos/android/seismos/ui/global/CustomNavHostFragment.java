package net.seismos.android.seismos.ui.global;

import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;

public class CustomNavHostFragment extends NavHostFragment {

    @Override
    protected Navigator<? extends FragmentNavigator.Destination> createFragmentNavigator() {
        return new KeepStateNavigator(requireContext(), getChildFragmentManager(), getId());
    }

}
