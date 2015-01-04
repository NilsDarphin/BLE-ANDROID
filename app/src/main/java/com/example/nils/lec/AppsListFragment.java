package com.example.nils.lec;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by nils on 04/01/15.
 */
public class AppsListFragment extends ListFragment {

    private ArrayList<ItemList> appItems;
    private ItemsAdapter itemsAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (itemsAdapter == null) {

            appItems = new ArrayList<ItemList>();

            for (AppsManager.App app : ((MainActivity) getActivity()).getAppsManager().getApps()) {
                appItems.add(app.getItemList());
            }

            itemsAdapter = new ItemsAdapter(getActivity(), appItems);

            setListAdapter(itemsAdapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        DevicesListFragment devicesListFragment = new DevicesListFragment(((MainActivity) getActivity()).getAppsManager().getApps().get(position));

        Bundle bundle = new Bundle();
        bundle.putInt(DevicesListFragment.ARG_POSITION, position);
        devicesListFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();

        fragmentTransaction.setCustomAnimations(R.animator.slide_from_right, R.animator.slide_to_left, R.animator.slide_from_left, R.animator.slide_to_right);

        fragmentTransaction.replace(R.id.fragment_container, devicesListFragment);

        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}
