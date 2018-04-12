package com.radford.clubmobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.radford.clubmobile.Constants;
import com.radford.clubmobile.R;
import com.radford.clubmobile.models.Announcement;
import com.radford.clubmobile.utils.DateHelper;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>{
    private final Context context;
    private List<Announcement> announcements;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AnnouncementAdapter(Context context) {
        this.announcements = new ArrayList<>();
        this.context = context;
    }

    public void load(List<Announcement> announcements) {
        this.announcements = announcements;
        notifyDataSetChanged();
    }

    public void add(Announcement announcement) {
        this.announcements.add(0, announcement);
        notifyItemInserted(0);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name, userName, description, time;
        public ImageView image;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.announcement_name);
            userName = v.findViewById(R.id.user_name);
            description = v.findViewById(R.id.announcement_description);
            time = v.findViewById(R.id.announcement_time);
            image = v.findViewById(R.id.userImage);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AnnouncementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_announcement, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Announcement announcement = announcements.get(position);

        holder.name.setText(announcement.getName());
        holder.userName.setText(String.format("by %s %s", announcement.getUser().getFirstName(), announcement.getUser().getLastName()));
        holder.description.setText(announcement.getDescription());
        holder.time.setText(DateHelper.timeAgo(announcement.getCreatedAt()));
        Glide.with(context)
                .load(Constants.BaseImageUrl + announcement.getUser().getAvatarUrl())
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.image);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return announcements.size();
    }
}