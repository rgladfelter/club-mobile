package com.radford.clubmobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.radford.clubmobile.Constants;
import com.radford.clubmobile.R;
import com.radford.clubmobile.delegates.ItemSelector;
import com.radford.clubmobile.models.Club;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder>{
    private final Context context;
    private List<Club> clubs;
    private ItemSelector<Club> delegate;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ClubAdapter(List<Club> clubs, ItemSelector<Club> delegate, Context context) {
        this.clubs = clubs;
        this.delegate = delegate;
        this.context = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public ImageView image;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.club_name);
            image = v.findViewById(R.id.club_image);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ClubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_club, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Club club = clubs.get(position);

        holder.name.setText(club.getName());
        Glide.with(context)
                .load(Constants.BaseImageUrl + club.getAvatarUrl())
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate.itemSelected(club);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return clubs.size();
    }
}