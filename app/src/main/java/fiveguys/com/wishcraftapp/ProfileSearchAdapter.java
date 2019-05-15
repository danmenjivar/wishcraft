package fiveguys.com.wishcraftapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileSearchAdapter extends RecyclerView.Adapter<ProfileSearchAdapter.Viewholder> {
    private static final String TAG = "ProfileSearchAdapter";

    private ArrayList<String> mNames = new ArrayList<>();
    private Context mContext;

    public ProfileSearchAdapter(Context Context, ArrayList<String> Names) {
        this.mNames = Names;
        this.mContext = Context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_profile_search_layout, viewGroup, false);
        Viewholder holder = new Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        viewholder.PSAName.setText(mNames.get(i));

    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView PSAName;
        RelativeLayout parentLayout;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            PSAName = itemView.findViewById(R.id.profile_search_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
