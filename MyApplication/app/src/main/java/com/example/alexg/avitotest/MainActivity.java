package com.example.alexg.avitotest;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.alexg.avitotest.asyncloader.DataLoader;
import com.example.alexg.avitotest.responce.ResultResponce;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private static final String DATA_STATE = "DATA_STATE";

    private ListView userList;
    private UserListAdapter userListAdapter;
    private ProgressBar progressBar;
    private TextView emptyList;

    private ResultResponce usersData;
    private DataLoader usersDataLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreData(savedInstanceState);
        final File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPoolSize(5)
                        // default 3
                .threadPriority(Thread.NORM_PRIORITY - 1)
                        // default Thread.NORM_PRIORITY-1
                .denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                .memoryCacheSize(5 * 1024 * 1024).diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(60 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        setContentView(R.layout.activity_main);
        initView();
        if(getSupportLoaderManager().getLoader(DataLoader.ID)!=null) {
            startLoaders(false);
        } else {
            if (usersData == null)
                startLoaders(true);
            else
                loadedData(usersData);
        }
    }

    private void restoreData(Bundle data){
        if(data!=null)
            usersData = data.getParcelable(DATA_STATE);
    }

    private void initView(){
        userList = (ListView) findViewById(R.id.user_list);
        userListAdapter = new UserListAdapter(this);
        userList.setAdapter(userListAdapter);
        emptyList = (TextView) findViewById(R.id.empty_list);
        progressBar = (ProgressBar) findViewById(R.id.progress_loader);
    }

    private void startLoading(){
        progressBar.setVisibility(View.VISIBLE);
        emptyList.setVisibility(View.GONE);
        userList.setVisibility(View.GONE);
    }

    private void badLoading(){
        usersData = null;
        userListAdapter.setUsers(null);
        progressBar.setVisibility(View.GONE);
        emptyList.setVisibility(View.VISIBLE);
    }

    private void loadedData(ResultResponce data){
        usersData = data;
        userListAdapter.setUsers(usersData);
        progressBar.setVisibility(View.GONE);
        userList.setVisibility(View.VISIBLE);
    }

    protected void startLoaders(boolean isRestart) {
        startLoading();
        if (isRestart)
            usersDataLoader = (DataLoader) getSupportLoaderManager().restartLoader(DataLoader.ID, null, usersCallback);
        else
            usersDataLoader = (DataLoader) getSupportLoaderManager().initLoader(DataLoader.ID, null, usersCallback);
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        if(usersData!=null && usersData.isOk())
        outState.putParcelable(DATA_STATE,usersData);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            startLoaders(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private LoaderManager.LoaderCallbacks<ResultResponce> usersCallback = new LoaderManager.LoaderCallbacks<ResultResponce>() {

        @Override
        public void onLoaderReset(Loader<ResultResponce> loader) { }

        @Override
        public void onLoadFinished(Loader<ResultResponce> loader, final ResultResponce data) {
            if (data.isOk()) {
                loadedData(data);
            } else {
                badLoading();
            }
            usersDataLoader = null;
            getSupportLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public Loader<ResultResponce> onCreateLoader(int id, Bundle args) {
            return new DataLoader<ResultResponce>(getApplicationContext());
        }
    };
}
