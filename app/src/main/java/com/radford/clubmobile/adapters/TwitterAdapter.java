package com.radford.clubmobile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.radford.clubmobile.R;

import java.util.ArrayList;
import java.util.List;

public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.ViewHolder> {
    private List<String> tweets;

    public TwitterAdapter() {
        tweets = new ArrayList<>();
    }
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tweet;
        public ViewHolder(View v) {
            super(v);
            tweet = v.findViewById(R.id.tweet);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TwitterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_tweet, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String tweet = tweets.get(position);

        holder.tweet.setText(tweet);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void load(List<String> tweets) {
        this.tweets = tweets;
        notifyDataSetChanged();
    }
}