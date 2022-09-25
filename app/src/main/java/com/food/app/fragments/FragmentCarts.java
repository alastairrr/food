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
import com.food.app.data_class.OrderData;
import com.food.app.data_class.RestaurantData;
import com.food.app.db_package.DatabaseManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public class FragmentCarts extends Fragment {
    ArrayList<OrderData> recyclerData = new ArrayList<>();
    DatabaseManager dbManager;
    CartAdapter adapter;
    RecyclerView rv;

    private class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {
        ArrayList<OrderData> data;

        public CartAdapter(ArrayList<OrderData> data) {this.data = data;}

        @NonNull
        @Override
        public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.entry_food_home_style,parent,false);
            return new CartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
            if ( getActivity() != null ) {
                holder.bind(data.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

    }

    private class CartViewHolder extends RecyclerView.ViewHolder {
        OrderData currOrderData;
        RestaurantData currRestaurantData;

        TextView entryTitle;
        TextView entrySubTitle;
        TextView entryDescription;
        ImageView entryImageRef;
        ConstraintLayout orderEntryFrame;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            entryTitle = itemView.findViewById(R.id.entry_food_title);
            entrySubTitle = itemView.findViewById(R.id.entry_food_subTitle);
            entryDescription = itemView.findViewById(R.id.entry_food_description);
            entryImageRef = itemView.findViewById(R.id.entry_food_icon);
            orderEntryFrame = itemView.findViewById(R.id.entry_food_Layout);

            orderEntryFrame.setOnClickListener(view -> {
                if ( getActivity() != null ) {
                    ((MainActivity) getActivity()).showCartDialog(currOrderData);
                }
            });
        }

        public void bind(OrderData orderData) {
            currOrderData = orderData;
            currRestaurantData = dbManager.getRestaurantFromItem(dbManager.getItemDataFromID(currOrderData.getItemID_FK()));

            entryTitle.setText(currRestaurantData.getName());

            double totalPrice = 0.0;

            for (Map.Entry<ItemData, Integer> entry : currOrderData.getCart().entrySet()) {

                totalPrice += entry.getKey().getPrice() * entry.getValue();
            }

            String subTitle = currOrderData.getCart().size() + " items  |  " + getString(R.string.dollar_sign, totalPrice);
            entrySubTitle.setText(subTitle);
            entryDescription.setText(currOrderData.getOrderDate());

            try {
                Field field = R.drawable.class.getField(currRestaurantData.getImageRef());
                int drawableID = field.getInt(null);
                entryImageRef.setImageResource(drawableID);

            } catch (Exception errorMsg) {
                Log.e("INVALID DRAWABLE", "Failed to get drawable id.", errorMsg);

            }

        }

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup ui, Bundle bundle) {

        if (dbManager.getCustomerLoggedIn() == null) {
            Log.w("AUTH", "NO USER LOGGED IN!" );

            if (getActivity() != null) {

                ((MainActivity) getActivity()).requireLoginAuth();
            }
        }
        else {
            Log.d("AUTH", "CURR USER:" + dbManager.getCustomerLoggedIn().getCustomerFirstname());

            for (OrderData orderData : dbManager.getOrderList()) {
                Log.d("TEST " + orderData.getOrderId_PK(), String.valueOf(orderData.getCart()));

                recyclerData.add(orderData);
            }

        }

        int smallestScreenWidthDp = getResources().getConfiguration().smallestScreenWidthDp;

        View view = inflater.inflate(R.layout.fragment_carts, ui, false);
        rv = view.findViewById(R.id.cartRecyclerView);

        if (smallestScreenWidthDp > 600) {
            rv.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
        else {
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        adapter = new CartAdapter(recyclerData);
        rv.setAdapter(adapter);
        return view;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public FragmentCarts(DatabaseManager databaseManager) {
        this.dbManager = databaseManager;

    }

    public void updateOrderRV(){
        for (OrderData orderData : dbManager.getOrderList()) {
            Log.d("TEST " + orderData.getOrderId_PK(), String.valueOf(orderData.getCart()));

            recyclerData.add(orderData);
        }
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(recyclerData);
        rv.setAdapter(adapter);
    }

}
