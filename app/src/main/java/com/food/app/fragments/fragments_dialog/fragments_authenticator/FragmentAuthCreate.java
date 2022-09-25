package com.food.app.fragments.fragments_dialog.fragments_authenticator;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.food.app.R;
import com.food.app.data_class.CustomerData;
import com.food.app.db_package.DatabaseManager;

// ==== login fragment ==== //
// ==== create account fragment ==== //
public class FragmentAuthCreate extends Fragment {
    private final DialogFragment dialogFragment;
    private final DatabaseManager dbManager;
    private TextView createAccButton;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText usernameField;
    private EditText passwordField;
    private EditText emailField;
    private TextView badAccCreateMsg;

    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {
        View view = inflater.inflate(R.layout.dialog_frag_auth_create, ui, false);
        createAccButton = view.findViewById(R.id.button_dialog_create_account);
        firstNameField = view.findViewById(R.id.text_dialog_create_firstname);
        lastNameField = view.findViewById(R.id.text_dialog_login_lastname);
        usernameField = view.findViewById(R.id.text_dialog_create_username);
        passwordField = view.findViewById(R.id.text_dialog_create_password);
        emailField = view.findViewById(R.id.text_dialog_create_email);
        badAccCreateMsg = view.findViewById(R.id.dialog_create_bad_account);
        return view;
    }

    public FragmentAuthCreate (DialogFragment dialogFragment, DatabaseManager databaseManager) {
        this.dialogFragment = dialogFragment;
        this.dbManager = databaseManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createAccButton.setOnClickListener(v -> {

            String firstNameFieldText = String.valueOf(firstNameField.getText());
            String lastNameFieldText = String.valueOf(lastNameField.getText());
            String usernameFieldText = String.valueOf(usernameField.getText());
            String passwordFieldText = String.valueOf(passwordField.getText());
            String emailFieldText = String.valueOf(emailField.getText());

            Log.d("app", emailFieldText);

            if (! (!TextUtils.isEmpty(emailFieldText) && Patterns.EMAIL_ADDRESS.matcher(emailFieldText).matches()) ) {
                badAccCreateMsg.setText(R.string.invalidAccountCreate3);
            }
            else if ( usernameFieldText.equals("") || passwordFieldText.equals("") || emailFieldText.equals("") ) {
                badAccCreateMsg.setText(R.string.invalidAccountCreate1);
            }

            else {
                CustomerData newCustomer = new CustomerData(firstNameFieldText, lastNameFieldText, usernameFieldText, passwordFieldText, emailFieldText);

                boolean customerAdded = dbManager.add_Customer(newCustomer);

                if (customerAdded) {

                    if (savedInstanceState == null) {
                        FragmentManager fragmentManager = getParentFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frag_auth_dialog, new FragmentAuthLogin(dialogFragment, dbManager)).commit();
                    }
                }

                else {
                    badAccCreateMsg.setText(R.string.invalidAccountCreate2);
                }
            }
        });
    }

}