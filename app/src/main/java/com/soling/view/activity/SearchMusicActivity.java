package com.soling.view.activity;

import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.soling.R;
import com.soling.custom.view.tabflow.FlowLayout;
import com.soling.custom.view.tabflow.TagAdapter;
import com.soling.custom.view.tabflow.TagFlowLayout;
import com.soling.model.Music;
import com.soling.presenter.SearchMusicContract;
import com.soling.presenter.SearchMusicPresenter;
import com.soling.view.adapter.MusicAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SearchMusicActivity extends AppCompatActivity implements SearchMusicContract.View {

    private static final String TAG = "SearchMusicActivity";
    private static final String PREFERENCE_FILE = "data";

    private static final String PREFERENCE_SEARCH_RECORD = "search_record";
    private static final int MAX_RECORD = 10;

    private SearchMusicContract.Presenter presenter;
    private LinearLayout llHotSearch;
    private SearchView searchView;
    private Toolbar toolbar;
    private RecyclerView rvNetResult;
    private RecyclerView rvLocalResult;
    private MusicAdapter netAdapter;
    private MusicAdapter localAdapter;


    private Set<String> searchRecords = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_music);

        initView();
        initData();
        initEvent();

    }

    public void initView() {
        llHotSearch = findViewById(R.id.ll_hot_search);
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.search_view);
        rvNetResult = findViewById(R.id.rv_net_result);
        rvLocalResult = findViewById(R.id.rv_local_result);
    }

    public void initData() {
        presenter = new SearchMusicPresenter(this);
        presenter.searchHot();

        setSupportActionBar(toolbar);

        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();

        SharedPreferences pref = getSharedPreferences(PREFERENCE_FILE, MODE_PRIVATE);
        searchRecords = pref.getStringSet(PREFERENCE_SEARCH_RECORD, new HashSet<String>());


        netAdapter = new MusicAdapter(new ArrayList<Music>());
        netAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: " + position);
            }
        });
        rvNetResult.setLayoutManager(new LinearLayoutManager(this));
        rvNetResult.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvNetResult.setAdapter(netAdapter);

        localAdapter = new MusicAdapter(new ArrayList<Music>());
        localAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.playLocal(position);
            }
        });
        rvLocalResult.setLayoutManager(new LinearLayoutManager(this));
        rvLocalResult.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvLocalResult.setAdapter(localAdapter);

    }

    public void initEvent() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                saveSearchRecord(s);
                presenter.search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showHots(final List<String> hots) {
        final TagFlowLayout tflHotSearch = new TagFlowLayout(this);
        tflHotSearch.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tflHotSearch.setTabAdapter(new TagAdapter<String>(hots) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView view = (TextView) SearchMusicActivity.this.getLayoutInflater().inflate(R.layout.item_hot_search, parent, false);
                view.setText(s);
                return view;
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                llHotSearch.addView(tflHotSearch);
            }
        });
    }

    @Override
    public void showSearchResult(final List<Music> localMusic, final List<Music> netMusics) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (localMusic != null && localMusic.size() > 0) {
                    localAdapter.setMusics(localMusic);
                    localAdapter.notifyDataSetChanged();
                }
                if (netMusics != null && netMusics.size() > 0) {
                    netAdapter.setMusics(netMusics);
                    netAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void showLocalResult(List<Music> music) {
        Log.d(TAG, "showLocalResult: " + music);
        localAdapter.setMusics(music);
        localAdapter.notifyDataSetChanged();
    }

    private void saveSearchRecord(String s) {
        searchRecords.add(s);
        if (searchRecords.size() > MAX_RECORD) {
            String[] records = (String[]) searchRecords.toArray();
            searchRecords.remove(records[records.length - 1]);
        }
        SharedPreferences.Editor edit = getSharedPreferences(PREFERENCE_FILE, MODE_PRIVATE).edit();
        edit.putStringSet(PREFERENCE_SEARCH_RECORD, searchRecords);
        edit.putString("test", "test"  + System.currentTimeMillis());
        edit.apply();
    }

}
