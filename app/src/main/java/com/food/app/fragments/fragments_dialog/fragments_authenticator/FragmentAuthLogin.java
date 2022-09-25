package com.food.app.fragments.fragments_dialog.fragments_authenticator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.food.app.MainActivity;
import com.food.app.R;
import com.food.app.db_package.DatabaseManager;
import com.food.app.fragments.FragmentAccount;
import com.food.app.fragments.FragmentCarts;

// ==== login fragment ==== //
public class FragmentAuthLogin extends Fragment {
    final private DialogFragment dialogFragment;
    final private DatabaseManager dbManager;
    private TextView loginButton;
    private EditText usernameField;
    private EditText passwordField;
    private TextView badLoginMsg;
    private TextView createAccountButton;

    public FragmentAuthLogin(DialogFragment dialogFragment, DatabaseManager databaseManager) {
        this.dialogFragment = dialogFragment;
        this.dbManager = databaseManager;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {
        View view = inflater.inflate(R.layout.dialog_frag_auth_login, ui, false);
        loginButton = view.findViewById(R.id.button_dialog_auth_login);
        badLoginMsg = view.findViewById(R.id.dialog_auth_bad_login);
        usernameField = view.findViewById(R.id.text_dialog_login_username);
        passwordField = view.findViewById(R.id.text_dialog_login_password);
        createAccountButton = view.findViewById(R.id.button_dialog_auth_new_user);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginButton.setOnClickListener(v -> {
            boolean userAuthenticated = dbManager.authenticateUser(String.valueOf(usernameField.getText()), String.valueOf(passwordField.getText()));
            Log.d("AUTH", "USER IS AUTHENTICATED: " + userAuthenticated);
            if (!userAuthenticated) {
                badLoginMsg.setText(R.string.bad_login_msg);
            } else {

                // login successful
                if (MainActivity.currSelectedFragment instanceof FragmentAccount){
                    ((FragmentAccount) MainActivity.currSelectedFragment).updateAccountViewDetails();
                }
                if (MainActivity.currSelectedFragment instanceof FragmentCarts) {

                    ((FragmentCarts) MainActivity.currSelectedFragment).updateOrderRV();

                }
                dialogFragment.dismiss();

            }
        });

        createAccountButton.setOnClickListener( v -> {
            if (savedInstanceState == null) {

                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frag_auth_dialog, new FragmentAuthCreate(dialogFragment, dbManager)).commit();
            }
        });
    }

}