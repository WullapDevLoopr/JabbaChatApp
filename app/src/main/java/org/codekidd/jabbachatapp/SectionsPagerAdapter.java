package org.codekidd.jabbachatapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by codekid on 13/03/2018.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
//                first fragment
                BlogFragment blogFragment = new BlogFragment();
                return blogFragment;

            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;

            case 2:
                FriendsFragment friendsFragment = new FriendsFragment();
                return friendsFragment;

            case 3:
                RequestFragment requestFragment = new RequestFragment();
                return requestFragment;

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 4;
    }
    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "BLOGGER";
            case 1:
                return "CHATS";
            case 2:
                return "FRIENDS";
            case 3:
                return null;
            default:
                return null;
        }
    }

}
