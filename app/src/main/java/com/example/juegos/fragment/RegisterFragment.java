package com.example.juegos.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.juegos.R;
import com.example.juegos.model.User;
import com.example.juegos.repository.AppDatabase;

import java.util.concurrent.atomic.AtomicLong;

public class RegisterFragment extends Fragment {

    AtomicLong usuarioID = new AtomicLong();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_fragment, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Button submit = view.findViewById(R.id.submitButton);
        EditText user = view.findViewById(R.id.username);
        //String usuario = user.
        EditText pass = view.findViewById(R.id.password);
        EditText pass2 = view.findViewById(R.id.password2);
        String text1 = pass.getText().toString();
        String text2 = pass2.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        submit.setOnClickListener(v -> {

            if(!text1.equals(text2)) {

                builder.setTitle("Warning")
                        .setMessage("Passwords do not match")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                //TODO save
                builder.setTitle("Success!")
                        .setMessage("Successfully Registered")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                User username = new User();
                                username.userName = user.getText().toString();
                                username.password = text1;
                                createUser(username);

                                //guardar en la sesion
                                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("user_id", usuarioID.intValue()); // Save user ID
                                editor.putString("username", user.getText().toString()); // Save username
                                editor.apply();


                                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.login_fragment, new GameFragment()) // Ensure you're replacing the correct fragment container
                                        .addToBackStack(null) // Allows going back if needed
                                        .commit();
                            }
                        }).show();
            }

        });
    }

    public void createUser(User user){
        AppDatabase db = AppDatabase.getInstance(requireContext());

        new Thread(() -> {
            usuarioID.set(db.userDao().insertUser(user));
            //return x;
            Log.d("User", "User created");
        }).start();

    }
}
