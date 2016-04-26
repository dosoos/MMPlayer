package com.mmplayer.activity;

import android.content.Intent;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mmplayer.R;
import com.mmplayer.adapter.ViewPagerAdapter;
import com.mmplayer.events.NetVideoFragSearchEvent;
import com.mmplayer.fragment.MusicFragment;
import com.mmplayer.fragment.NetVideoFragment;
import com.mmplayer.fragment.VideoFragment;
import com.mmplayer.service.MusicService;
import com.thefinestartist.utils.etc.APILevel;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, SearchView.OnQueryTextListener {

    private Logger mLogger = LoggerFactory.getLogger(MainActivity.class);

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Menu mMenu;
    private MenuItem mMenuSearch;
    private MenuItem mMenuAbout;
    private SearchView mMenuSeachView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EventBus.getDefault().register(this);
//        if (APILevel.require(21)) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
//        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);//返回按钮不显示

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(this);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);

        mLogger.info("MainActivity.onCreate");

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(MusicFragment.newInstance(1), "音乐");
        adapter.addFragment(VideoFragment.newInstance(1), "视频");
        adapter.addFragment(NetVideoFragment.newInstance(1), "在线");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mMenuAbout = menu.findItem(R.id.action_about);
        mMenuSearch = menu.findItem(R.id.my_search);
        mMenuSearch.setVisible(false);
        mMenuSeachView = (SearchView) mMenuSearch.getActionView();
        mMenuSeachView.setOnQueryTextListener(this);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (mMenu == null)
            return;
        if (position == 2){
            mMenuSearch.setVisible(true);
            mMenuAbout.setVisible(false);
        }else {
            mMenuSearch.setVisible(false);
            mMenuAbout.setVisible(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mLogger.info("onQueryTextChange:"+query);
        EventBus.getDefault().post(new NetVideoFragSearchEvent(query));
        mMenuSeachView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
