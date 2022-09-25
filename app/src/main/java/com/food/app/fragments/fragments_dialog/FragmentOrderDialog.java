package com.food.app.fragments.fragments_dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.food.app.MainActivity;
import com.food.app.R;
import com.food.app.custom_views.CustomSwipeImageView;
import com.food.app.data_class.ItemData;
import com.food.app.data_class.RestaurantData;
import com.food.app.db_package.DatabaseManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;


public class FragmentOrderDialog extends DialogFragment {

    private static final HashMap<String, Integer> cartHashMap = new HashMap <>();
    private static double totalPriceValue = 0.0;
    private static RestaurantData currRestaurant;

    private static ArrayList<ItemData> itemDataArrayList;
    private static DatabaseManager dbManager;
    private TextView placeOrderButton;

    private TextView totalPriceText;


    public FragmentOrderDialog() {}

    public static void display(FragmentManager fragmentManager, RestaurantData restaurantData, DatabaseManager database) {
        FragmentOrderDialog orderDialog = new FragmentOrderDialog();
        currRestaurant = restaurantData;
        dbManager = database;
        itemDataArrayList = database.getItemsFromRestaurantData(restaurantData);

        initializeCart(null);

        orderDialog.show(fragmentManager, restaurantData.getRestaurantID());
    }

    public static void display(FragmentManager fragmentManager, RestaurantData restaurantData, ItemData item, DatabaseManager database) {
        FragmentOrderDialog orderDialog = new FragmentOrderDialog();
        currRestaurant = restaurantData;
        dbManager = database;
        itemDataArrayList = database.getItemsFromRestaurantData(restaurantData);

        initializeCart(item);

        orderDialog.show(fragmentManager, restaurantData.getRestaurantID());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
    }


    public void onStart() {
        super.onStart();
        Dialog orderDialog = getDialog();
        if (orderDialog != null) {
            orderDialog.getWindow().setWindowAnimations(R.style.DialogAnimProperties);
            orderDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            orderDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_order, container, false);

        int smallestScreenWidthDp = getResources().getConfiguration().smallestScreenWidthDp;

        RecyclerView rv = view.findViewById(R.id.orderDialogRecyclerView);
        if (smallestScreenWidthDp > 600){
            rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else {
            rv.setLayoutManager(new LinearLayoutManager(getContext()));

        }
        DialogRecyclerAdapter myAdapter = new DialogRecyclerAdapter(itemDataArrayList);
        rv.setAdapter(myAdapter);

        CustomSwipeImageView dismissButton = view.findViewById(R.id.dismissOrderButton);
        dismissButton.setRelativeFragment(this);
        placeOrderButton = view.findViewById(R.id.buttonPlaceOrder);
        totalPriceText = view.findViewById(R.id.orderDialogTotal);

        return view;
    }

    @Override
    public void dismiss() {
        cartHashMap.clear(); // clear the cart.
        MainActivity.resetOrderDebounce();
        super.dismiss();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        cartHashMap.clear(); // clear the cart.
        MainActivity.resetOrderDebounce();
        super.onCancel(dialog);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        placeOrderButton.setOnClickListener(view1 -> {

            if (dbManager.getCustomerLoggedIn() == null) {
                if (getActivity() != null) {

                    ((MainActivity) getActivity()).requireLoginAuth();
                }
            }
            else {
                Log.d("YOUR FINAL ORDER IS:", String.valueOf(cartHashMap));
                dbManager.add_Order(cartHashMap);
                dismiss();
            }

        });

        totalPriceValue = getTotal();
        totalPriceText.setText(getString(R.string.dollar_sign, totalPriceValue));

    }

    // ==== RECYCLER VIEW FOR ORDER PAGE ==== //

    private class DialogRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<ItemData> data;

        public DialogRecyclerAdapter(ArrayList<ItemData> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

            // if first entry
            if (viewType == 1) {
                View view = layoutInflater.inflate(R.layout.entry_restaurant, parent, false);
                return new RestaurantDialogRecyclerViewHolder(view);
            }
            // else not first entry, inflate items
            else {
                View view = layoutInflater.inflate(R.layout.entry_food_order_style, parent, false);
                return new DialogRecyclerViewHolder(view);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            if (getActivity() != null) {

                if (getItemViewType(position) == 1) {
                    ((RestaurantDialogRecyclerViewHolder) holder).bind();
                }
                else{
                    ((DialogRecyclerViewHolder) holder).bind(data.get(position - 1));
                }

            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) return 1;
            else return 2;
        }

        @Override
        public int getItemCount() {
            return data.size() + 1;
        }


        private class RestaurantDialogRecyclerViewHolder extends RecyclerView.ViewHolder {
            TextView restaurantName;
            TextView restaurantDesc;
            ImageView restaurantImageRef;

            public RestaurantDialogRecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                restaurantName = itemView.findViewById(R.id.entry_restaurant_title);
                restaurantDesc = itemView.findViewById(R.id.entry_restaurant_subTitle);
                restaurantImageRef = itemView.findViewById(R.id.entry_restaurant_banner);
            }

            public void bind() {
                restaurantName.setText(currRestaurant.getName());
                restaurantDesc.setText(currRestaurant.getDescription());

                try {
                    Field field = R.drawable.class.getField(currRestaurant.getImageRef());
                    int drawableID = field.getInt(null);
                    restaurantImageRef.setImageResource(drawableID);

                } catch (Exception errorMsg) {
                    Log.e("INVALID DRAWABLE", "Failed to get drawable id.", errorMsg);

                }
            }
        }

        private class DialogRecyclerViewHolder extends RecyclerView.ViewHolder {
            ItemData currentItem;
            TextView entryName;
            TextView entryDescription;
            TextView entryPrice;
            TextView entryAmount;
            ImageView entryImageRef;
            ImageView addButton;
            ImageView removeButton;


            public DialogRecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                entryName = itemView.findViewById(R.id.entry_food_order_title);
                entryDescription = itemView.findViewById(R.id.entry_food_order_subTitle);
                entryPrice = itemView.findViewById(R.id.entry_food_order_price);
                entryImageRef = itemView.findViewById(R.id.entry_food_order_icon);
                entryAmount = itemView.findViewById(R.id.entry_food_order_amount);
                addButton = itemView.findViewById(R.id.entry_food_order_addButton);
                removeButton = itemView.findViewById(R.id.entry_food_order_removeButton);


                addButton.setOnClickListener(view -> {
                    addItemToCart(currentItem.getID());
                    totalPriceValue = getTotal();
                    totalPriceText.setText(getString(R.string.dollar_sign, totalPriceValue));
                    entryAmount.setText(String.valueOf(cartHashMap.get(currentItem.getID())));

                    Log.d("PRICE TOTAL", String.valueOf(totalPriceValue));


                });

                removeButton.setOnClickListener(view -> {
                    removeItemFromCart(currentItem.getID());
                    totalPriceValue = getTotal();
                    totalPriceText.setText(getString(R.string.dollar_sign, totalPriceValue));
                    entryAmount.setText(String.valueOf(cartHashMap.get(currentItem.getID())));

                    Log.d("PRICE TOTAL", String.valueOf(totalPriceValue));


                });
            }

            public void bind(ItemData itemData) {
                currentItem = itemData;

                entryName.setText(currentItem.getName());
                entryDescription.setText(currentItem.getDescription());
                entryAmount.setText(String.valueOf(cartHashMap.get(currentItem.getID())));

                entryPrice.setText(getString(R.string.dollar_sign, currentItem.getPrice()));

                try {
                    Field field = R.drawable.class.getField(currentItem.getImageRef());
                    int drawableID = field.getInt(null);
                    entryImageRef.setImageResource(drawableID);

                } catch (Exception errorMsg) {
                    Log.e("INVALID DRAWABLE", "Failed to get drawable id.", errorMsg);

                }

            }

        }

    }

    private double getTotal() {
        double totalPrice = 0.0;
        for (ItemData itemData : itemDataArrayList) {

            totalPrice += (double) getValueFromCart(itemData.getID()) * itemData.getPrice();
        }
        return totalPrice;
    }

    private void addItemToCart( String itemID) {
        cartHashMap.put(itemID, getValueFromCart(itemID) + 1);
    }

    private void removeItemFromCart( String itemID) {
        if (getValueFromCart(itemID) != 0 ) {
            cartHashMap.put(itemID, getValueFromCart(itemID) - 1);
        }
    }

    private static void initializeCart(ItemData initialItem) {
        if ( initialItem != null ) {
            for (ItemData itemData : itemDataArrayList) {
                if (itemData.getID().equals(initialItem.getID())) {
                    cartHashMap.put(itemData.getID(), 1);
                } else {
                    cartHashMap.put(itemData.getID(), 0);
                }
            }
        }
        else {
            for (ItemData itemData : itemDataArrayList) {
                cartHashMap.put(itemData.getID(), 0);
            }
        }
    }

    // NO NULL PTR EXCEPTION
    private static int getValueFromCart(String itemID)  {
        Integer itemValue_NotNull = cartHashMap.get(itemID);
        return itemValue_NotNull == null ? 0 : itemValue_NotNull;
    }


}
