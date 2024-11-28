package com.example.juegos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.login_fragment, container, false);

        Button login = rootView.findViewById(R.id.loginButton);


        login.setOnClickListener(v -> {
            // Use the activity's FragmentManager to replace the fragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_fragment, new GameFragment())
                    .addToBackStack(null)
                    .commit();
        });
        return rootView;
    }


}
