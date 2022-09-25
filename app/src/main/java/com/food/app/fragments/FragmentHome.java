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
import com.food.app.data_class.ItemData;
import com.food.app.data_class.RestaurantData;
import com.food.app.db_package.DatabaseManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class FragmentHome extends Fragment {
    public static ArrayList<ItemData> recyclerData = new ArrayList<>();
    DatabaseManager dbManager;

    private class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {
        ArrayList<ItemData> data;

        public HomeAdapter(ArrayList<ItemData> data) {this.data = data;}

        @NonNull
        @Override
        public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.entry_food_home_style,parent,false);
            return new HomeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
            if ( getActivity() != null ) {
                holder.bind(data.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class HomeViewHolder extends RecyclerView.ViewHolder {
        ItemData currItemData;
        RestaurantData currRestaurantData;
        TextView entryName;
        TextView entryRestaurant;
        TextView entryDescription;
        ImageView entryImageRef;
        ConstraintLayout foodEntryFrame;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            entryName = itemView.findViewById(R.id.entry_food_title);
            entryRestaurant = itemView.findViewById(R.id.entry_food_subTitle);
            entryDescription = itemView.findViewById(R.id.entry_food_description);
            entryImageRef = itemView.findViewById(R.id.entry_food_icon);
            foodEntryFrame = itemView.findViewById(R.id.entry_food_Layout);

            foodEntryFrame.setOnClickListener(view -> {
                if ( getActivity() != null ) {
                    Log.d("app", currItemData.getName());
                    ((MainActivity) getActivity()).showOrderDialog(currRestaurantData, currItemData);
                }
            });
        }

        public void bind(ItemData itemData) {
            currItemData = itemData;
            currRestaurantData = dbManager.getRestaurantFromItem(currItemData);

            entryName.setText(currItemData.getName());
            entryRestaurant.setText(currRestaurantData.getName());
            entryDescription.setText(currItemData.getDescription());

            try {
                Field field = R.drawable.class.getField(currItemData.getImageRef());
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
        View view = inflater.inflate(R.layout.fragment_home, ui, false);
        RecyclerView rv = view.findViewById(R.id.homeRecyclerView);

        if ( smallestScreenWidthDp > 600 ) {
            rv.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
        else {
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        HomeAdapter myAdapter = new HomeAdapter(recyclerData);
        rv.setAdapter(myAdapter);
        return view;
    }

    public FragmentHome (DatabaseManager databaseManager) {
        this.dbManager = databaseManager;


    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}
