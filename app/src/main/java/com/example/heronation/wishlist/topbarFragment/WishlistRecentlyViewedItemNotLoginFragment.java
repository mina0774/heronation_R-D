package com.example.heronation.wishlist.topbarFragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.heronation.R;

import butterknife.ButterKnife;


public class WishlistRecentlyViewedItemNotLoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
             // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_wishlist_recently_viewed_item_not_login, container,false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
