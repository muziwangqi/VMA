package com.soling.view.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import com.soling.App;
import com.soling.R;
import com.soling.custom.view.LyricView;
import com.soling.model.LyricLine;
import com.soling.model.Music;
import com.soling.model.PlayList;
import com.soling.presenter.PlayerContract;
import com.soling.presenter.PlayerPresenter;
import com.soling.service.player.IPlayer;
import com.soling.utils.BlurUtil;
import com.soling.utils.MusicFileManager;

public class PlayerFragment extends BaseFragment implements PlayerContract.View, View.OnClickListener {

    public static final String PLAY_INDEX = "PLAY_INDEX";
    private static final String TAG = "PlayerFragment";
    private static final int REFRESH_SEEK_BAR_PERIOD = 1000;
    private static final int REFRESH_LYRIC_PERIOD = 1000;

    private MusicFileManager musicFileManager;

    private boolean isSeeking = false;

    private ImageButton ibPlay;
    private ImageButton ibPlayLast;
    private ImageButton ibPlayNext;
    private ImageButton ibChangeModel;
    private ImageButton ibList;
    private TextView tvName;
    private TextView tvArtist;
    private TextView tvCurrentPosition;
    private TextView tvDuration;
    private AppCompatSeekBar seekBar;
    private RelativeLayout rlPlay;
    private LyricView lyricView;
    private ViewPager vpMusicList;
    private List<MusicListFragment> musicListFragments;

    private PlayerPresenter presenter;
    private Timer sbTimer;
    private Timer lyricTimer;
    private ImageView ivAlbumCover;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_player, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        presenter = new PlayerPresenter(this, getContext());
        initView();
        initData();
        initEvent();
        requestPermission();
        sbTimer = new Timer();
        lyricTimer = new Timer();
    }

    private void initView() {
        ibPlay = (ImageButton) findViewById(R.id.ib_play);
        ibPlayLast = (ImageButton) findViewById(R.id.ib_play_last);
        ibPlayNext = (ImageButton) findViewById(R.id.ib_play_next);
        ibChangeModel = (ImageButton) findViewById(R.id.ib_change_model);
        ibList = (ImageButton) findViewById(R.id.ib_list);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvArtist = (TextView) findViewById(R.id.tv_artist);
        tvCurrentPosition = (TextView) findViewById(R.id.tv_current_position);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        ivAlbumCover = (ImageView) findViewById(R.id.iv_album_cover);
        lyricView = (LyricView) findViewById(R.id.lyric_view);
        seekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar);
        rlPlay = (RelativeLayout) findViewById(R.id.rl_play);
        vpMusicList = (ViewPager) findViewById(R.id.vp_music_list);
    }

    private void initData() {
        Bitmap bitmap = BlurUtil.doBlur(BitmapFactory.decodeResource(getResources(), R.drawable.jay), 3, 100);
        rlPlay.setBackground(new BitmapDrawable(getResources(), bitmap));
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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvCurrentPosition.setText(msToString(seekBar.getProgress()));
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
        ibPlay.setOnClickListener(this);
        ibPlayNext.setOnClickListener(this);
        ibPlayLast.setOnClickListener(this);
        ibChangeModel.setOnClickListener(this);
        ibList.setOnClickListener(this);
    }

    private void initPlayer() {
        presenter.bindPlayService();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        else {
            initPlayer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initPlayer();
                } else {
                    Toast.makeText(getContext(), "未授权访问外部存储权限，无法使用应用！", Toast.LENGTH_LONG).show();

                }
                break;
            default:
        }
    }

    private void playToggle() {
        if (presenter.isPlaying()) {
            presenter.pause();
        }
        else {
            presenter.resume();
        }
    }

    @Override
    public void refreshView() {
        Log.d(TAG, "refreshView: ");
        final Music music = presenter.getPlayingMusic();
        if (music == null) return;
        tvName.setText(music.getName());
        tvArtist.setText("- " + music.getArtist() + " -");
        tvDuration.setText(msToString(music.getDuration()));
        seekBar.setMax(music.getDuration());
        if (presenter.isPlaying()) {
            ibPlay.setImageResource(R.drawable.ic_pause);
        }
        else {
            ibPlay.setImageResource(R.drawable.ic_play);
        }
        startRefreshSeekBar();
    }

    @Override
    public void refreshCover(final Bitmap cover) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (cover == null) return;
                final RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), cover);
                roundedBitmapDrawable.setAntiAlias(true);
                roundedBitmapDrawable.setCornerRadius(Math.min(cover.getWidth(), cover.getHeight()));
                final Bitmap background = BlurUtil.doBlur(cover, 3, 200);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ivAlbumCover.setImageDrawable(roundedBitmapDrawable);
                        rlPlay.setBackground(new BitmapDrawable(getResources(), background));
                    }
                });
            }
        }).start();
    }

    @Override
    public void refreshLyric(final List<LyricLine> lyric) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lyric == null || lyric.size() == 0) return;
                lyricView.setLyric(lyric);
            }
        });
    }

    private void startRefreshSeekBar() {
        sbTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (presenter.isPlaying()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isSeeking) {
                                seekBar.setProgress(presenter.getProgress());
                            }
                        }
                    });
                }
            }
        }, 0, REFRESH_SEEK_BAR_PERIOD);
    }


    private void startRefreshLyric() {
        lyricTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lyricView.refresh(presenter.getProgress());
                    }
                });
            }
        }, 0, REFRESH_LYRIC_PERIOD);
    }

    public void playNext() {
        presenter.playNext();
    }

    public void playLast() {
        presenter.playLast();
    }

    public void changeModel() {
        presenter.changeModel();
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
                imageResource = R.drawable.ic_repeat;
                break;
        }
        ibChangeModel.setImageResource(imageResource);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_play_next:
                playNext();
                break;
            case R.id.ib_play_last:
                playLast();
                break;
            case R.id.ib_play:
                playToggle();
                break;
            case R.id.ib_change_model:
                changeModel();
                break;
            case R.id.ib_list:
                Log.d(TAG, "onClick: " + "ib_list");
                if (vpMusicList.getVisibility() == View.VISIBLE) {
                    vpMusicList.setVisibility(View.INVISIBLE);
                }
                else {
                    vpMusicList.setVisibility(View.VISIBLE);
                }
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
        musicFileManager = MusicFileManager.getInstance(getContext());
        List<Music> musics = musicFileManager.getLocalMusics();
        ((App)Objects.requireNonNull(getActivity()).getApplication()).setLocalMusics(musics);
        PlayList playList = new PlayList(musics);
        presenter.play(playList, 0);
        presenter.pause();
//        refreshView();
        initMusicListFragments();
        startRefreshLyric();
    }

    private void initMusicListFragments() {
        Log.d(TAG, "initMusicListFragments: ");
        musicListFragments = new ArrayList<>();
        MusicListFragment localListFragment = MusicListFragment.build("本地列表", R.drawable.ic_shuffle, presenter.getMusicList());
        MusicListFragment loveListFragment = MusicListFragment.build("我喜欢的", R.drawable.ic_heart_outline, presenter.getMusicList());
        localListFragment.setOnItemClickListener(new MusicListFragment.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: " + position);
                presenter.play(position);
            }
        });
        musicListFragments.add(localListFragment);
        musicListFragments.add(loveListFragment);
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

    private static String msToString(int millisecond) {
        int second = millisecond / 1000;
        int min = second / 60;
        second = second % 60;
        return String.valueOf(min) +
                (second < 10 ? ":0" : ":") +
                second;
    }

}
