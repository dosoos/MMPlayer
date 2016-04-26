package com.mmplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mmplayer.R;
import com.mmplayer.activity.MyWebViewActivity;
import com.mmplayer.adapter.NetVideoRecyclerViewAdapter;
import com.mmplayer.data.bean.MediaBean;
import com.mmplayer.data.net.BaiduVideoBean;
import com.mmplayer.events.NetVideoFragSearchEvent;
import com.mmplayer.iface.FragmentInteractionListener;
import com.thefinestartist.finestwebview.FinestWebView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link FragmentInteractionListener}
 * interface.
 */
public class NetVideoFragment extends Fragment implements FragmentInteractionListener {

    public static final String TAG = NetVideoFragment.class.getName();

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RequestQueue mRequestQueue;
    private List<MediaBean> mPlayBean = new ArrayList<MediaBean>();
    private NetVideoRecyclerViewAdapter mNetVideoAdapter;
    private Logger mLogger = LoggerFactory.getLogger(NetVideoFragment.class);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NetVideoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NetVideoFragment newInstance(int columnCount) {
        NetVideoFragment fragment = new NetVideoFragment();
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
        mRequestQueue = Volley.newRequestQueue(getContext());
        sendRequest("何以笙箫默");

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
        super.onStop();
    }

    @Subscribe
    public void searchVideo(NetVideoFragSearchEvent event) {
        sendRequest(event.query);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_netvideo_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mNetVideoAdapter = new NetVideoRecyclerViewAdapter(mPlayBean, this, mRequestQueue);
            recyclerView.setAdapter(mNetVideoAdapter);
        }

        return view;
    }

    @Override
    public void onListFragmentInteraction(MediaBean mediaBean) {
        new FinestWebView.Builder(getActivity())
                .show(mediaBean.value);


//        .theme(R.style.AppTheme)
//                .webViewAppCacheEnabled(true)
//                .webViewDatabaseEnabled(true)
//                .webViewJavaScriptEnabled(true)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface NetVideoFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MediaBean item);
    }

    public void sendRequest(final String query) {
        String url = "http://apis.baidu.com/baidu_openkg/shipin_kg/shipin_kg";
        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.POST,
                url,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        if (response == null){
                            Toast.makeText(getContext(), "没有搜索到！", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (response.toString().length() <= 0){
                            Toast.makeText(getContext(), "没有搜索到！", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (response.toString().length() <= 100) {
                            Toast.makeText(getContext(), "没有搜索到！", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Log.i("首次请求返回结果", response.toString());
                        BaiduVideoBean baiduVideoBean = new Gson().fromJson(response.toString(), BaiduVideoBean.class);
                        if (baiduVideoBean.data == null || baiduVideoBean.data.length <= 0) {
                            return;
                        }
                        for (int i = 0; i < baiduVideoBean.data.length; i++) {
                            for (MediaBean mb : baiduVideoBean.data[i].mPlayUrl) {
                                mb.flag = 3;
                                mb.title = query;
                                mPlayBean.add(mb);
                            }
                        }
                        mNetVideoAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("首次请求返回结果", error.toString());
                        Toast.makeText(getContext(), "请求失败!请检查网络", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("apikey", "2be42a4f5b9aa3c7c0b6b96fa7581993");
                return map;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("query", query);
                params.put("resource", "video_haiou");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String str = "{\"query\": \"" + query + "\", \"resource\": \"video_haiou\"}";
                Log.i(TAG, "request connection: " + str);
                return str.getBytes();
            }
        };

        stringRequest.setTag(TAG);
        mRequestQueue.add(stringRequest);


    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
