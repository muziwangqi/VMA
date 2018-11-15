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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.soling.view.adapter.SearchHistoryAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class SearchMusicActivity extends AppCompatActivity implements SearchMusicContract.View {

    private static final String TAG = "SearchMusicActivity";
    private static final String PREFERENCE_FILE = "data";

    private static final String PREFERENCE_SEARCH_RECORD = "search_history";
    private static final int MAX_RECORD = 10;

    private SearchMusicContract.Presenter presenter;
    private LinearLayout llHotSearch;
    private SearchView searchView;
    private Toolbar toolbar;
    private RecyclerView rvNetResult;
    private RecyclerView rvLocalResult;
    private RecyclerView rvSearchHistory;
    private MusicAdapter netAdapter;
    private MusicAdapter localAdapter;
    private SearchHistoryAdapter historyAdapter;
    private LinearLayout llSearchHelp;
    private LinearLayout llSearchResult;
    private ImageButton ibBack;

    private List<String> searchHistory = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_music);

        initView();
        initData();
        initEvent();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbindPlayerService(this);
    }

    public void initView() {
        llHotSearch = findViewById(R.id.ll_hot_search);
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.search_view);
        rvNetResult = findViewById(R.id.rv_net_result);
        rvLocalResult = findViewById(R.id.rv_local_result);
        rvSearchHistory = findViewById(R.id.rv_search_history);
        llSearchHelp = findViewById(R.id.ll_search_help);
        llSearchResult = findViewById(R.id.ll_search_result);
        ibBack = findViewById(R.id.ib_back);
    }

    public void initData() {
        presenter = new SearchMusicPresenter(this);
        presenter.bindPlayerService(this);
        presenter.searchHot();

        setSupportActionBar(toolbar);

        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();

        searchHistory.addAll(readFromSharedPreferences());

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

        historyAdapter = new SearchHistoryAdapter(searchHistory);
        historyAdapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                searchView.setQuery(searchHistory.get(position), true);
            }
        });
        historyAdapter.setOnDeleteClickListener(new SearchHistoryAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                searchHistory.remove(position);
                historyAdapter.notifyDataSetChanged();
                saveToSharedPreferences();
            }
        });
        rvSearchHistory.setLayoutManager(new LinearLayoutManager(this));
        rvSearchHistory.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvSearchHistory.setAdapter(historyAdapter);
    }

    public void initEvent() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!TextUtils.isEmpty(s)) {
                    saveSearchRecord(s);
                    presenter.search(s);
                    llSearchResult.setVisibility(View.VISIBLE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    if (llSearchResult.getVisibility() == View.VISIBLE)
                        llSearchResult.setVisibility(View.INVISIBLE);
                    if (llSearchHelp.getVisibility() == View.INVISIBLE) {
                        llSearchHelp.setVisibility(View.VISIBLE);
                        historyAdapter.notifyDataSetChanged();
                    }
                }
                if (!TextUtils.isEmpty(s) && llSearchHelp.getVisibility() == View.VISIBLE) {
                    llSearchHelp.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchMusicActivity.this.finish();
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
        if (searchHistory.size() >= MAX_RECORD) {
            searchHistory.remove(searchHistory.size() - 1);
        }
        searchHistory.remove(s);
        searchHistory.add(0, s);

        saveToSharedPreferences();

        Log.d(TAG, "saveSearchRecord: " + new LinkedHashSet<>(searchHistory).toString());
    }

    private void saveToSharedPreferences() {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < searchHistory.size(); i++) {
            set.add(Integer.toString(i) + "_" + searchHistory.get(i));
        }
        SharedPreferences.Editor edit = getSharedPreferences(PREFERENCE_FILE, MODE_PRIVATE).edit();
        edit.putStringSet(PREFERENCE_SEARCH_RECORD, set);
        edit.apply();
    }

    private List<String> readFromSharedPreferences() {
        List<String> result = new LinkedList<>();
        SharedPreferences pref = getSharedPreferences(PREFERENCE_FILE, MODE_PRIVATE);
        result.addAll(Objects.requireNonNull(pref.getStringSet(PREFERENCE_SEARCH_RECORD, new HashSet<String>())));
        Collections.sort(result);
        for (int i = 0; i < result.size(); i++) {
            result.set(i, result.get(i).split("_")[1]);
        }
        return result;
    }

}
