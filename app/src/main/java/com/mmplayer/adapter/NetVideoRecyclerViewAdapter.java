package com.mmplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mmplayer.R;
import com.mmplayer.data.bean.MediaBean;
import com.mmplayer.iface.FragmentInteractionListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MediaBean} and makes a call to the
 * specified {@link FragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class NetVideoRecyclerViewAdapter extends RecyclerView.Adapter<NetVideoRecyclerViewAdapter.ViewHolder> {

    private final List<MediaBean> mValues;
    private final FragmentInteractionListener mListener;
    private RequestQueue mRequestQueue;
    private Logger mLogger = LoggerFactory.getLogger(NetVideoRecyclerViewAdapter.class);

    public NetVideoRecyclerViewAdapter(List<MediaBean> items, FragmentInteractionListener listener, RequestQueue context) {
        mValues = items;
        mListener = listener;
        mRequestQueue = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mImage.setImageResource(R.drawable.video);
        holder.mTitle.setText(holder.mItem.title);
        holder.mSummary.setText("来源:"+holder.mItem.source);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImage;
        public final TextView mTitle;
        public final TextView mSummary;
        public MediaBean mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = (ImageView) view.findViewById(R.id.image);
            mTitle = (TextView) view.findViewById(R.id.title);
            mSummary = (TextView) view.findViewById(R.id.summary);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.language + "'";
        }
    }
}
