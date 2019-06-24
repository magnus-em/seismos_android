package net.seismos.android.seismos.ui.global;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

@Navigator.Name("custom_fragment")
public class KeepStateNavigator extends FragmentNavigator {

    private Context context;
    private FragmentManager manager;
    private int containerId;

    KeepStateNavigator(Context context, FragmentManager manager, int containerId) {
        super(context, manager, containerId);
        this.context = context;
        this.manager = manager;
        this.containerId = containerId;
    }

    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args,
                                   NavOptions navOptions,  Navigator.Extras navigatorExtras) {

        String tag = Integer.toString(destination.getId());
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment currentFrag = manager.getPrimaryNavigationFragment();

        boolean initialNavigate = false;

        if (currentFrag != null) {
            transaction.detach(currentFrag);
        } else {
            initialNavigate = true;
        }

        Fragment nextFrag = manager.findFragmentByTag(tag);

        // watch this – I'm not sure if instantiate fragment is the right method to use here
        if (nextFrag == null) {
            nextFrag = manager.getFragmentFactory().instantiate(context.getClassLoader(),
                                                                destination.getClassName());
            nextFrag.setArguments(args);
            transaction.add(containerId, nextFrag, tag);
        } else {
            transaction.attach(nextFrag);
        }

        transaction.setPrimaryNavigationFragment(nextFrag);
        transaction.setReorderingAllowed(true);
        transaction.commit();

         if (initialNavigate) {
             return destination;
         } else {
             return null;
         }
    }


}
