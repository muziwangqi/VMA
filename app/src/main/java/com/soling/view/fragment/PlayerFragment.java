package com.soling.view.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.soling.App;
import com.soling.R;
import com.soling.custom.view.LyricView;
import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.model.PlayList;
import com.soling.presenter.PlayerContract;
import com.soling.presenter.PlayerPresenter;
import com.soling.service.player.IPlayer;
import com.soling.utils.BitmapUtil;
import com.soling.utils.MusicFileManager;
import com.soling.utils.StringUtil;
import com.soling.view.activity.SearchMusicActivity;
import com.soling.view.adapter.MediaButtonReceiver;
import com.soling.view.adapter.MusicAdapter;

public class PlayerFragment extends BaseFragment implements PlayerContract.View, View.OnClickListener {

    public static final String PLAY_INDEX = "PLAY_INDEX";
    private static final String TAG = "PlayerFragment";
    private static final int PERIOD_REFRESH_SEEK_BAR = 1000;
    private static final int PERIOD_REFRESH_LYRIC = 1000;
    private static final int DELAY_HIDE_SB_VOLUME = 2000;

    private MusicFileManager musicFileManager;

    private boolean isSeeking = false;

    private ImageButton ibPlay;
    private ImageButton ibPlayLast;
    private ImageButton ibPlayNext;
    private ImageButton ibChangeModel;
    private ImageButton ibLike;
    private ImageButton ibList;
    private ImageButton ibVolume;
    private ImageButton ibShare;
    private ImageButton ibSearch;
    private TextView tvName;
    private TextView tvArtist;
    private TextView tvCurrentPosition;
    private TextView tvDuration;
    private AppCompatSeekBar sbMusic;
    private AppCompatSeekBar sbVolume;
    private RelativeLayout rlPlay;
    private LyricView lyricView;
    private ViewPager vpMusicList;
    private List<MusicListFragment> musicListFragments;
    private ImageView ivAlbumCover;
    private MusicAdapter likeAdapter;

    private Handler handler;
    private Runnable taskHideSBVolume;
    private Runnable taskRefreshLyric;
    private Runnable taskRefreshSBMusic;

    private PlayerPresenter presenter;
    private OnShareMusicListener shareMusicListener;
    private AudioManager audioManager;
    private ComponentName componentName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new PlayerPresenter(this);
        initView();
        initData();
        initEvent();
        float scale = App.getInstance().getResources().getDisplayMetrics().density;
        Log.d(TAG, "onViewCreated: scale" + scale);
        float fontScale = App.getInstance().getResources().getDisplayMetrics().scaledDensity;
        Log.d(TAG, "onViewCreated: fontScale" + fontScale);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        audioManager.unregisterMediaButtonEventReceiver(componentName);
    }

    private void initView() {
        ibPlay = (ImageButton) findViewById(R.id.ib_play);
        ibPlayLast = (ImageButton) findViewById(R.id.ib_play_last);
        ibPlayNext = (ImageButton) findViewById(R.id.ib_play_next);
        ibChangeModel = (ImageButton) findViewById(R.id.ib_change_model);
        ibList = (ImageButton) findViewById(R.id.ib_list);
        ibLike = (ImageButton) findViewById(R.id.ib_like);
        ibVolume = (ImageButton) findViewById(R.id.ib_volume);
        ibShare = (ImageButton) findViewById(R.id.ib_share);
        ibSearch = (ImageButton) findViewById(R.id.ib_search);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        tvCurrentPosition = (TextView) findViewById(R.id.tv_current_position);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        ivAlbumCover = (ImageView) findViewById(R.id.iv_album_cover);
        lyricView = (LyricView) findViewById(R.id.lyric_view);
        sbMusic = (AppCompatSeekBar) findViewById(R.id.seek_bar);
        sbVolume = (AppCompatSeekBar) findViewById(R.id.sb_volume);
        rlPlay = (RelativeLayout) findViewById(R.id.rl_play);
        vpMusicList = (ViewPager) findViewById(R.id.vp_music_list);
    }

    private void initData() {
        presenter.bindPlayService();

        handler = new Handler();
        taskHideSBVolume = new Runnable() {
            @Override
            public void run() {
                PlayerFragment.this.sbVolume.setVisibility(View.INVISIBLE);
            }
        };
        taskRefreshLyric = new Runnable() {
            @Override
            public void run() {
                lyricView.refresh(presenter.getProgress());
                handler.postDelayed(this, PERIOD_REFRESH_LYRIC);
            }
        };
        taskRefreshSBMusic = new Runnable() {
            @Override
            public void run() {
                if (presenter.isPlaying() && !isSeeking) {
                    sbMusic.setProgress(presenter.getProgress());
                }
                handler.postDelayed(this, PERIOD_REFRESH_LYRIC);
            }
        };
        audioManager = (AudioManager) App.getInstance().getSystemService(Context.AUDIO_SERVICE);
        componentName = new ComponentName(App.getInstance().getPackageName(), MediaButtonReceiver.class.getName());
        audioManager.registerMediaButtonEventReceiver(componentName);
    }

    private void initEvent() {
        ivAlbumCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivAlbumCover.setVisibility(View.INVISIBLE);
                lyricView.setVisibility(View.VISIBLE);
            }
        });
        lyricView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyricView.setVisibility(View.INVISIBLE);
                ivAlbumCover.setVisibility(View.VISIBLE);
            }
        });
        sbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvCurrentPosition.setText(StringUtil.msToString(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                presenter.seekTo(seekBar.getProgress());
                isSeeking = false;
            }
        });

        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                handler.removeCallbacks(PlayerFragment.this.taskHideSBVolume);
                presenter.setVolume(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.postDelayed(PlayerFragment.this.taskHideSBVolume, PlayerFragment.DELAY_HIDE_SB_VOLUME);
            }
        });
        ibPlay.setOnClickListener(this);
        ibPlayNext.setOnClickListener(this);
        ibPlayLast.setOnClickListener(this);
        ibChangeModel.setOnClickListener(this);
        ibList.setOnClickListener(this);
        ibLike.setOnClickListener(this);
        ibVolume.setOnClickListener(this);
        ibShare.setOnClickListener(this);
        ibSearch.setOnClickListener(this);
    }

    private void initPlayer() {
        if (presenter.getPlayingMusic() == null) {
            musicFileManager = MusicFileManager.getInstance(getContext());
            musicFileManager.getLocalMusics(new MusicFileManager.Callback() {
                @Override
                public void onFinish(final List<Music> musics) {
                    ((App) Objects.requireNonNull(getActivity()).getApplication()).setLocalMusics(new PlayList(musics));
                    PlayerFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (musics.size() > 0) {
                                presenter.play(App.getInstance().getLocalMusics(), 0);
                                presenter.pause();
                            }
                            refreshView();
                            initMusicListFragments();
                            startRefreshLyric();
                        }
                    });
                }
            });
        } else {
            refreshView();
            initMusicListFragments();
            startRefreshLyric();
        }
    }

    private void initMusicListFragments() {
        musicListFragments = new ArrayList<>();
        likeAdapter = new MusicAdapter(App.getInstance().getLikeMusics().getMusics());
        likeAdapter.setShowDeleteBtn(true);
        likeAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.play(App.getInstance().getLikeMusics(), position);
            }
        });
        likeAdapter.setOnItemDeleteClickListener(new MusicAdapter.OnItemDeleteClickListener() {
            @Override
            public void onItemDeleteClick(int position) {
                presenter.likeToggle(App.getInstance().getLikeMusics().getMusics().get(position));

            }
        });
        MusicListFragment likeListFragment = MusicListFragment.build("我喜欢的", R.drawable.player_unlike, likeAdapter);

        final MusicAdapter localAdapter = new MusicAdapter(App.getInstance().getLocalMusics().getMusics());
        localAdapter.setShowDeleteBtn(true);
        localAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.play(App.getInstance().getLocalMusics(), position);
//                presenter.play(position);
            }
        });
        localAdapter.setOnItemDeleteClickListener(new MusicAdapter.OnItemDeleteClickListener() {
            @Override
            public void onItemDeleteClick(int position) {
                presenter.delete(localAdapter.getMusics(), position);
                localAdapter.notifyDataSetChanged();
                likeAdapter.notifyDataSetChanged();
            }
        });
        MusicListFragment localListFragment = MusicListFragment.build("本地列表", R.drawable.ic_shuffle, localAdapter);

        musicListFragments.add(localListFragment);
        musicListFragments.add(likeListFragment);
        vpMusicList.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return musicListFragments.get(i);
            }

            @Override
            public int getCount() {
                return musicListFragments.size();
            }
        });
    }

    @Override
    public void refreshView() {
        final Music music = presenter.getPlayingMusic();
        lyricView.reset();

        final RoundedBitmapDrawable drawable = BitmapUtil.roundedBitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.player_cover_default));
        ivAlbumCover.setImageDrawable(drawable);
        if (music != null) {
            tvName.setText(music.getName());
            tvArtist.setText("- " + music.getArtist() + " -");
            tvDuration.setText(StringUtil.msToString(music.getDuration()));
            sbMusic.setMax(music.getDuration());
            if (presenter.isPlaying()) {
                ibPlay.setImageResource(R.drawable.player_pause);
            } else {
                ibPlay.setImageResource(R.drawable.player_play);
            }
            refreshLike(music.isLike());
            startRefreshSeekBar();
            presenter.loadLyric();
            presenter.loadCover();
        }
    }

    @Override
    public void refreshPlayState() {
        ibPlay.setImageResource(presenter.isPlaying() ? R.drawable.player_pause : R.drawable.player_play);
    }

    @Override
    public void refreshLike(boolean like) {
        if (like) {
            ibLike.setImageResource(R.drawable.player_like);
        } else {
            ibLike.setImageResource(R.drawable.player_unlike);
        }
        if (likeAdapter != null)
            likeAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadCover(final Bitmap cover) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (cover == null) return;
                final RoundedBitmapDrawable roundedBitmapDrawable = BitmapUtil.roundedBitmapDrawable(cover);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ivAlbumCover.setImageDrawable(roundedBitmapDrawable);
                    }
                });
            }
        }).start();
    }

    @Override
    public void loadLyric(final List<LyricLine> lyric) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lyric == null || lyric.size() == 0) return;
                lyricView.setLyric(lyric);
            }
        });
    }

    private void startRefreshSeekBar() {
        handler.post(taskRefreshSBMusic);
    }


    private void startRefreshLyric() {
        handler.post(taskRefreshLyric);
    }

    @Override
    public void changeModel() {
        IPlayer.Model model = presenter.getModel();
        ImageButton ibChangeModel = (ImageButton) findViewById(R.id.ib_change_model);
        int imageResource = 0;
        switch (model) {
            case SHUFFLE:
                imageResource = R.drawable.ic_shuffle;
                break;
            case LOOP_SINGLE:
                imageResource = R.drawable.ic_repeat_once;
                break;
            case LOOP_ALL:
                imageResource = R.drawable.player_repeat;
                break;
        }
        ibChangeModel.setImageResource(imageResource);
    }

    private void showVolumeManagerBar() {
        int maxVolume = presenter.getMaxVolume();
        int volume = presenter.getVolume();
        sbVolume.setProgress(volume);
        sbVolume.setMax(maxVolume);
        sbVolume.setVisibility(View.VISIBLE);
        handler.postDelayed(taskHideSBVolume, DELAY_HIDE_SB_VOLUME);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_play_next:
                presenter.playNext();
                break;
            case R.id.ib_play_last:
                presenter.playLast();
                break;
            case R.id.ib_play:
                if (presenter.isPlaying()) {
                    presenter.pause();
                } else {
                    presenter.resume();
                }
                break;
            case R.id.ib_change_model:
                presenter.changeModel();
                break;
            case R.id.ib_list:
                if (vpMusicList.getVisibility() == View.VISIBLE) {
                    vpMusicList.setVisibility(View.INVISIBLE);
                } else {
                    vpMusicList.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ib_like:
                presenter.likeToggle(presenter.getPlayingMusic());
                break;
            case R.id.ib_volume:
                showVolumeManagerBar();
                break;
            case R.id.ib_share:
                Log.d(TAG, "onClick: " + "ib_share");
                if (shareMusicListener != null) {
                    Log.d(TAG, "onClick: " + " shareMusicListener");
                    shareMusicListener.shareMusic(presenter.getPlayingMusic());
                }
                break;
            case R.id.ib_search:
                Intent intent = new Intent(App.getInstance(), SearchMusicActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unbindPlayService();
    }

    @Override
    public void onPlayServiceBound() {
        initPlayer();
    }

    public void setShareMusicListener(OnShareMusicListener shareMusicListener) {
        this.shareMusicListener = shareMusicListener;
    }

    public interface OnShareMusicListener {
        void shareMusic(Music music);
    }

}
