package clubs.mobile.radford.clubmobile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import clubs.mobile.radford.clubmobile.R;
import clubs.mobile.radford.clubmobile.models.Club;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> implements Filterable{
    private List<Club> clubs;
    private List<Club> filteredClubs;
    private ClubFilter clubFilter;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ClubAdapter(List<Club> clubs) {
        this.clubs = clubs;
        this.filteredClubs = clubs;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name, description;
        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.club_name);
            description = (TextView) v.findViewById(R.id.club_description);
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
        Club club = filteredClubs.get(position);

        holder.name.setText(club.getName());
        holder.description.setText(club.getDescription());
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