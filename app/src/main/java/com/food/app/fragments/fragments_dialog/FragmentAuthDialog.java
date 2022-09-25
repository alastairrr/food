package com.food.app.fragments.fragments_dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.food.app.MainActivity;
import com.food.app.R;
import com.food.app.custom_views.CustomSwipeImageView;
import com.food.app.db_package.DatabaseManager;
import com.food.app.fragments.fragments_dialog.fragments_authenticator.FragmentAuthLogin;


public class FragmentAuthDialog extends DialogFragment {


    private static DatabaseManager dbManager;


    public static void display(FragmentManager fragmentManager, DatabaseManager database) {
        FragmentAuthDialog loginDialog = new FragmentAuthDialog();
        dbManager = database;

        loginDialog.show(fragmentManager, "login");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
    }


    public void onStart() {
        super.onStart();
        Dialog loginDialog = getDialog();
        if (loginDialog != null) {
            loginDialog.getWindow().setWindowAnimations(R.style.DialogAnimProperties);
            loginDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            loginDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_frag_auth, container, false);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frag_auth_dialog, new FragmentAuthLogin(this, dbManager)).commit();
        }
        return view;
    }


    @Override
    public void dismiss() {
        MainActivity.resetLoginDebounce();
        super.dismiss();
    }


    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        MainActivity.resetLoginDebounce();
        super.onCancel(dialog);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CustomSwipeImageView dismissAuth = view.findViewById(R.id.button_auth_dismiss);
        dismissAuth.setRelativeFragment(this);

    }



}
