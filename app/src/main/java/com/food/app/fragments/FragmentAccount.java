package com.food.app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.food.app.MainActivity;
import com.food.app.R;
import com.food.app.db_package.DatabaseManager;

public class FragmentAccount extends Fragment {
    DatabaseManager dbManager;
    TextView accounts_current_user;
    TextView buttonLogOut;

    public FragmentAccount(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_account, ui, false);
        buttonLogOut = view.findViewById(R.id.buttonLogOut);
        accounts_current_user = view.findViewById(R.id.accounts_current_user);

        if (dbManager.getCustomerLoggedIn() == null) {
            Log.w("AUTH", "NO USER LOGGED IN!" );
            if (getActivity() != null) {
                buttonLogOut.setVisibility(View.INVISIBLE);

                ((MainActivity) getActivity()).requireLoginAuth();
            }
        }
        else {
            Log.d("AUTH", "CURR USER: " + dbManager.getCustomerLoggedIn().getCustomerFirstname());
            updateAccountViewDetails();
        }

        buttonLogOut.setOnClickListener(view1 -> {
            Log.d("AUTH", "LOG OUT");
            accounts_current_user.setText(R.string.guest);
            dbManager.logOut();
            buttonLogOut.setVisibility(View.INVISIBLE);
        });

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateAccountViewDetails() {
        accounts_current_user.setText(dbManager.getCustomerLoggedIn().getCustomerFirstname());
        buttonLogOut.setVisibility(View.VISIBLE);

    }

}
