package com.example.nils.lec;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Created by nils on 04/01/15.
 */
public class AppsListFragment extends ListFragment {

    private AppItemsAdapter appItemsAdapter;
    private AppsManager appsManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appsManager = new AppsManager();
        appItemsAdapter = new AppItemsAdapter(getActivity(), appsManager.getApps());

        setListAdapter(appItemsAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        DevicesListFragment devicesListFragment = new DevicesListFragment();

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();

        fragmentTransaction.setCustomAnimations(R.animator.slide_from_right, R.animator.slide_to_left, R.animator.slide_from_left, R.animator.slide_to_right);

        fragmentTransaction.replace(R.id.fragment_container, devicesListFragment);

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}
