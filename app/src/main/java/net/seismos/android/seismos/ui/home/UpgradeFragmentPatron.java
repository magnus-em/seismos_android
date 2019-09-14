package net.seismos.android.seismos.ui.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.util.DepressAnimationUtil;

public class UpgradeFragmentPatron extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_upgrade_patron, container, false);

        DepressAnimationUtil.setup(root.findViewById(R.id.fiatChip));
        DepressAnimationUtil.setup(root.findViewById(R.id.seiChip));
        return root;
    }
}
