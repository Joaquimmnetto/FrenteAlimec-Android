package com.alimec.joaquim.alimecproject.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alimec.joaquim.alimecproject.R;
import com.alimec.joaquim.alimecproject.activities.adapter.MenuAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KithLenovo on 21/08/2015.
 */
public class BaseMenuActivity extends ActionBarActivity{

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawer;
    private ListView menuList;
    private MenuAdapter menuAdapter;

    private Map<String,Fragment> menuFragments = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base_menu);

        init();
        setListeners();
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            //nao olhe para mim, a documentacao diz literalmente pra fazer so isso
            //https://developer.android.com/reference/android/support/v7/app/ActionBarDrawerToggle.html#onOptionsItemSelected(android.view.MenuItem)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init(){

        drawer = (DrawerLayout) findViewById(R.id.base_drawerLayout);
        menuList = (ListView) findViewById(R.id.drawer_navList);
        menuAdapter = new MenuAdapter(getApplicationContext());

        drawerToggle = new ActionBarDrawerToggle(this, drawer,R.string.hello_world,R.string.hello_world) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//            Log.d(TAG, "onDrawerClosed: " + getTitle());
                invalidateOptionsMenu();
            }
        };


        menuList.setAdapter(menuAdapter);
        drawer.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListeners() {

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String menuName = (String) menuList.getItemAtPosition(position);
                startMenuOption(menuName);
            }
        });

    }




    protected void addMenuOption(Drawable icon, String menuName, Fragment fragment){
        menuAdapter.add(menuName, icon);
        menuFragments.put(menuName, fragment);
        menuAdapter.notifyDataSetChanged();
    }

    protected void removeMenuOption(String menuName){
        if(menuFragments.containsKey(menuName)){
            menuAdapter.remove(menuName);
            menuFragments.remove(menuName);
            menuAdapter.notifyDataSetChanged();
        }
    }

    protected void startMenuOption(String menuName){
        startMenuOption(menuName,null);
    }

    protected void startMenuOption(String menuName,Bundle fragmentArgs){
        Fragment tobeActive = menuFragments.get(menuName);


        if(tobeActive != null){
            if(fragmentArgs != null){
                tobeActive.setArguments(fragmentArgs);
            }
            setActiveFragment(menuName,tobeActive);
        }else{
            throw new IllegalArgumentException("Menu não existe!");
        }


    }

    private void setActiveFragment(String title, Fragment newFrag){
        getSupportActionBar().setTitle(title);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.base_mainContent, newFrag, title);
        transaction.commit();
    }

}
