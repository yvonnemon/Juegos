package com.example.juegos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.juegos.R;

public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);

        Button login = rootView.findViewById(R.id.loginButton);
        Button registerButton = rootView.findViewById(R.id.registerButton);


        login.setOnClickListener(v -> {
            //TODO login
            // Use the activity's FragmentManager to replace the fragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_fragment, new GameFragment())
                    .addToBackStack(null)
                    .commit();
        });
        registerButton.setOnClickListener(v -> {
            //TODO login
            // Use the activity's FragmentManager to replace the fragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_fragment, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });
        return rootView;
    }


}
