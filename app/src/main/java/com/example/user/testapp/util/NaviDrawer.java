package com.example.user.testapp.util;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.example.user.testapp.R;
import com.example.user.testapp.activities.AccountActivity;
import com.example.user.testapp.activities.MainActivity;
import com.example.user.testapp.activities.MapActivity;
import com.example.user.testapp.activities.MarkAddActivity;
import com.example.user.testapp.activities.TourActivity;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class NaviDrawer {

    public  NaviDrawer( final Activity activity, Toolbar toolbar) {
        AccountHeader.Result accontHeader = createAccontHeader(activity);
        Drawer.Result drawerResult =new Drawer()
                .withActivity(activity)
                //.withAccountHeader(accontHeader)
                .withToolbar(toolbar)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withSelectedItem(-1)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName("Map")
                                .withIcon(R.drawable.ic_map_black_48dp)
                                .withIdentifier(1),
                        new PrimaryDrawerItem()
                                .withName("Add Mark")
                                .withIcon(R.drawable.ic_place_black_48dp)
                                .withIdentifier(2),
                        new PrimaryDrawerItem()
                                .withName("List of markers")
                                .withIcon(R.drawable.ic_view_headline_black_48dp)
                                .withIdentifier(3),
                        new PrimaryDrawerItem()
                                .withName("Account")
                                .withIcon(R.drawable.ic_account_circle_black_48dp)
                                .withIdentifier(4),
                        new PrimaryDrawerItem()
                                .withName("Tour")
                                .withIcon(R.drawable.ic_flight_black_48dp)
                                .withIdentifier(5),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName("hotel")
                                .withIcon(R.drawable.ic_hotel_black_48dp),
                        new SecondaryDrawerItem()
                                .withName("aiport")
                                .withIcon(R.drawable.ic_local_airport_black_48dp),
                        new SecondaryDrawerItem()
                                .withName("taxi")
                                .withIcon(R.drawable.ic_local_taxi_black_48dp)

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        //Intent intent = new Intent(MainActivity.this, MapActivity.class);
                        //startActivity(intent);
                        //Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT);
                        if (iDrawerItem.getIdentifier() == 1) {
                            Intent intent = new Intent(activity, MapActivity.class);
                            intent.putExtra("from", "navi");
                            activity.startActivity(intent);
                        }
                        if (iDrawerItem.getIdentifier() == 2) {
                            //Intent intent = new Intent(MainActivity.this, MarkAddActivity.class);
                            //startActivity(intent);
                            Intent intent = new Intent(activity, MarkAddActivity.class);
                            activity.startActivity(intent);
                        }
                        if (iDrawerItem.getIdentifier() == 3) {
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                        }
                        if (iDrawerItem.getIdentifier() == 4) {
                            Intent intent = new Intent(activity, AccountActivity.class);
                            activity.startActivity(intent);
                        }

                        if (iDrawerItem.getIdentifier() == 5) {
                            Intent intent = new Intent(activity, TourActivity.class);
                            activity.startActivity(intent);
                        }

                    }
                })
                .build();
    }
    private AccountHeader.Result createAccontHeader(final Activity activity) {
        IProfile profile = new ProfileDrawerItem()
                .withEmail("kasuhanov@gmail.com")
                .withName("Cyril Suhanov");
        return new AccountHeader()
                .withHeaderBackground(R.drawable.bg2)
                .withActivity(activity)
                .addProfiles(profile)
                .build();
    }
}
