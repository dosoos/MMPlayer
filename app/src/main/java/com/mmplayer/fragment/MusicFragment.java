package com.mmplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mmplayer.R;
import com.mmplayer.activity.MusicPlayerActivity;
import com.mmplayer.adapter.MusicRecyclerViewAdapter;
import com.mmplayer.data.DataContorl;
import com.mmplayer.data.bean.MediaBean;
import com.mmplayer.events.NextMusicEvent;
import com.mmplayer.events.OverMusicEvent;
import com.mmplayer.events.PrivateMusicEvent;
import com.mmplayer.events.StartEvent;
import com.mmplayer.events.UpdateInfoEvent;
import com.mmplayer.iface.FragmentInteractionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link FragmentInteractionListener}
 * interface.
 */
public class MusicFragment extends Fragment implements FragmentInteractionListener {

    private static final int MUSIC_PLAYING = 0x1;
    private static final int MUSIC_STOPING = 0x2;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private FragmentInteractionListener mListener;

    private int mStatus = MUSIC_PLAYING;//0.play,1.pause
    private DataContorl.MediaData mMediaData;
    private int mCurrIndex = 0;
    private List<MediaBean> mMusicDatas;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MusicFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MusicFragment newInstance(int columnCount) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        mMediaData = DataContorl.getMediaData(getContext(), DataContorl.DATA_MUSIC);
        mMusicDatas = mMediaData.getData();

    }

    @Subscribe
    public void onPrevateMusic(PrivateMusicEvent event){
        if (mCurrIndex <= 0)
            return;
        mCurrIndex--;
        EventBus.getDefault().post(new StartEvent(mMusicDatas.get(mCurrIndex)));
        EventBus.getDefault().post(new UpdateInfoEvent(mMusicDatas.get(mCurrIndex)));
    }

    @Subscribe
    public void onNextMusic(NextMusicEvent event){
        if (mCurrIndex >= mMusicDatas.size()-1)
            mCurrIndex = 0;
        mCurrIndex++;
        EventBus.getDefault().post(new StartEvent(mMusicDatas.get(mCurrIndex)));
        EventBus.getDefault().post(new UpdateInfoEvent(mMusicDatas.get(mCurrIndex)));
    }

    @Subscribe
    public void onPlayComplete(OverMusicEvent event){
        if (mCurrIndex >= mMusicDatas.size()-1)
            mCurrIndex = 0;
        mCurrIndex++;
        EventBus.getDefault().post(new StartEvent(mMusicDatas.get(mCurrIndex)));
        EventBus.getDefault().post(new UpdateInfoEvent(mMusicDatas.get(mCurrIndex)));
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MusicRecyclerViewAdapter(mMusicDatas, MusicFragment.this));
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListFragmentInteraction(MediaBean item) {
        mCurrIndex = mMusicDatas.indexOf(item);
        Intent intent = new Intent(getActivity(), MusicPlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("mediabean", item);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
