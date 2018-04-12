package com.radford.clubmobile.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.radford.clubmobile.Constants;
import com.radford.clubmobile.R;
import com.radford.clubmobile.delegates.ItemSelector;
import com.radford.clubmobile.models.Club;

import java.util.ArrayList;
import java.util.List;

public class FindClubAdapter extends RecyclerView.Adapter<FindClubAdapter.ViewHolder> implements Filterable{
    private List<Club> clubs;
    private List<Club> filteredClubs;
    private ClubFilter clubFilter;
    private ItemSelector<Club> delegate;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FindClubAdapter(List<Club> clubs, ItemSelector<Club> delegate, Context context) {
        this.clubs = clubs;
        this.filteredClubs = clubs;
        this.delegate = delegate;
        this.context = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView name, description;
        public ImageView clubImage, iconImage;
        public ViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.club_name);
            description = v.findViewById(R.id.club_description);
            clubImage = v.findViewById(R.id.club_image);
            iconImage = v.findViewById(R.id.club_join_icon);
        }
    }
    // Create new views (invoked by the layout manager)
    @Override
    public FindClubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_find_club, parent, false);

        return new ViewHolder(v);
    }

    public void updateClub(Club club) {
        for (int i=0; i<clubs.size(); i++) {
            if(clubs.get(i).getId().equals(club.getId())) {
                clubs.set(i,club);
                break;
            }
        }

        this.notifyDataSetChanged();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Club club = filteredClubs.get(position);

        holder.name.setText(club.getName());
        if (club.getHasJoined()) {
            holder.iconImage.setVisibility(View.GONE);
        }

        holder.description.setText(club.getDescription());
        Glide.with(context)
                .load(Constants.BaseImageUrl + club.getAvatarUrl())
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.clubImage);
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
        return filteredClubs.size();
    }

    @Override
    public Filter getFilter() {
        if(clubFilter == null) {
            clubFilter = new ClubFilter();
        }

        return clubFilter;
    }

    private class ClubFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            if (charSequence!=null && charSequence.length()>0) {
                List<Club> tempList = new ArrayList<>();

                // search content in friend list
                for (Club club : clubs) {
                    if (club.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        tempList.add(club);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = clubs.size();
                filterResults.values = clubs;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredClubs = (ArrayList<Club>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}