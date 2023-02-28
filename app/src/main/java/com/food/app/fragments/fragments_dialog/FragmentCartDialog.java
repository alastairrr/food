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
import com.food.app.data_class.OrderData;
import com.food.app.data_class.RestaurantData;
import com.food.app.db_package.DatabaseManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FragmentCartDialog extends DialogFragment {

    private static RestaurantData currRestaurant;

    private static final ArrayList<ItemData> itemDataArrayList = new ArrayList<>();
    private static HashMap<ItemData, Integer> cart;
    private static double totalPriceValue = 0.0;
    private static OrderData orderData;

    public FragmentCartDialog() {}

    public static void display(FragmentManager fragmentManager, OrderData orderData, DatabaseManager database) {
        FragmentCartDialog cartDialog = new FragmentCartDialog();
        FragmentCartDialog.orderData = orderData;
        currRestaurant = database.getRestaurantFromItem(database.getItemDataFromID(orderData.getItemID_FK()));
        cart = orderData.getCart();

        for (Map.Entry<ItemData, Integer> entry : orderData.getCart().entrySet()) {

            totalPriceValue += entry.getKey().getPrice() * entry.getValue();
        }

        itemDataArrayList.addAll(cart.keySet());

        cartDialog.show(fragmentManager, String.valueOf(orderData.getOrderId_PK()));
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
        View view = inflater.inflate(R.layout.dialog_cart, container, false);
        int smallestScreenWidthDp = getResources().getConfiguration().smallestScreenWidthDp;

        RecyclerView rv = view.findViewById(R.id.cartDialogRecyclerView);
        if (smallestScreenWidthDp > 600) {

            rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
        else {
            rv.setLayoutManager(new LinearLayoutManager(getContext()));

        }
        DialogRecyclerAdapter myAdapter = new DialogRecyclerAdapter(itemDataArrayList);
        rv.setAdapter(myAdapter);

        CustomSwipeImageView dismissButton = view.findViewById(R.id.dismissCartButton);
        dismissButton.setRelativeFragment(this);

        TextView totalPriceText = view.findViewById(R.id.cartDialogTotal);
        totalPriceText.setText(getString(R.string.dollar_sign, totalPriceValue));


        return view;
    }

    @Override
    public void dismiss() {
        itemDataArrayList.clear();
        totalPriceValue = 0.0;
        MainActivity.resetCartDebounce();
        super.dismiss();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        itemDataArrayList.clear();
        totalPriceValue = 0.0;
        MainActivity.resetCartDebounce();
        super.onCancel(dialog);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
                View view = layoutInflater.inflate(R.layout.entry_food_home_style, parent, false);
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
                restaurantDesc.setText(FragmentCartDialog.orderData.getOrderDate());

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
            ItemData currItemData;
            TextView entryTitle;
            TextView entrySubTitle;
            TextView entryDescription;
            ImageView entryImageRef;

            public DialogRecyclerViewHolder(@NonNull View itemView) {
                super(itemView);
                entryTitle = itemView.findViewById(R.id.entry_food_title);
                entrySubTitle = itemView.findViewById(R.id.entry_food_subTitle);
                entryDescription = itemView.findViewById(R.id.entry_food_description);
                entryImageRef = itemView.findViewById(R.id.entry_food_icon);

            }

            public void bind(ItemData itemData) {
                currItemData = itemData;
                entryTitle.setText(currItemData.getName());


                Integer itemValue_NotNull = cart.get(itemData);
                int quantity = itemValue_NotNull == null ? 0 : itemValue_NotNull;

                String quantityString = "Quantity: " + quantity;
                entrySubTitle.setText(quantityString);

                double price = quantity * itemData.getPrice();
                entryDescription.setText(getString(R.string.dollar_sign, price));

                try {
                    Field field = R.drawable.class.getField(currItemData.getImageRef());
                    int drawableID = field.getInt(null);
                    entryImageRef.setImageResource(drawableID);

                } catch (Exception errorMsg) {
                    Log.e("INVALID DRAWABLE", "Failed to get drawable id.", errorMsg);

                }

            }

        }

    }


}
