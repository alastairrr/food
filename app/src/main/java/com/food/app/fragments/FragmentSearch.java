package com.food.app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.food.app.MainActivity;
import com.food.app.R;
import com.food.app.data_class.RestaurantData;
import com.food.app.db_package.DatabaseManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class FragmentSearch extends Fragment {
    ArrayList<RestaurantData> recyclerData;

    public FragmentSearch() {}
    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        ArrayList<RestaurantData> data;

        public SearchAdapter(ArrayList<RestaurantData> data) {this.data = data;}

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.entry_restaurant,parent,false);

            return new SearchViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if ( getActivity() != null ) {
                holder.bind(data.get(position));
            }

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {
        RestaurantData currRestaurantData;
        TextView entryName;
        TextView entryDescription;
        ImageView entryImageRef;
        ConstraintLayout restaurantEntryFrame;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            entryName = itemView.findViewById(R.id.entry_restaurant_title);
            entryDescription = itemView.findViewById(R.id.entry_restaurant_subTitle);
            entryImageRef = itemView.findViewById(R.id.entry_restaurant_banner);
            restaurantEntryFrame = itemView.findViewById(R.id.entry_restaurant_Layout);

            restaurantEntryFrame.setOnClickListener(view -> {
                Log.d("app", currRestaurantData.getName());

                if ( getActivity() != null ) {
                    ((MainActivity) getActivity()).showOrderDialog(currRestaurantData);
                }

            });
        }

        public void bind(RestaurantData restaurantData) {
            currRestaurantData = restaurantData;
            entryName.setText(restaurantData.getName());
            entryDescription.setText(restaurantData.getDescription());

            try {
                Field field = R.drawable.class.getField(restaurantData.getImageRef());
                int drawableID = field.getInt(null);
                entryImageRef.setImageResource(drawableID);

            } catch (Exception errorMsg) {
                Log.e("INVALID DRAWABLE", "Failed to get drawable id.", errorMsg);

            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {
        int smallestScreenWidthDp = getResources().getConfiguration().smallestScreenWidthDp;

        View view = inflater.inflate(R.layout.fragment_search, ui, false);
        RecyclerView rv = view.findViewById(R.id.searchRecyclerView);
        if ( smallestScreenWidthDp > 600 ) {

            rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else {
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        SearchAdapter myAdapter = new SearchAdapter(recyclerData);
        rv.setAdapter(myAdapter);
        return view;
    }

    public FragmentSearch (DatabaseManager dbManager) {
        this.recyclerData = dbManager.getRestaurantData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
