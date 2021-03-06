package com.leencecodes.nifixiegari.auth;



import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.leencecodes.nifixiegari.databinding.FragmentForgotPasswordBinding;


import org.jetbrains.annotations.NotNull;

public class ForgotPasswordFragment extends DialogFragment {

    private FragmentForgotPasswordBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);

        View view = binding.getRoot();
        binding.dialogConfirm.setOnClickListener(v -> {

            if (TextUtils.isEmpty(binding.userEmail.getText().toString())) {
                binding.userEmail.setError("Required");
                return;

            }
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.userEmail.getText().toString()).matches()) {
                binding.userEmail.setError("Invalid email format");
                return;

            }

            FirebaseAuth.getInstance().sendPasswordResetEmail(binding.userEmail.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Check Your Email", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Email does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show());
        });
        return view;
    }
}