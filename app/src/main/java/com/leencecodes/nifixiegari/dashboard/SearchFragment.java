package com.leencecodes.nifixiegari.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leencecodes.nifixiegari.adapters.SearchMechanicsAdapter;
import com.leencecodes.nifixiegari.databinding.FragmentSearchBinding;
import com.leencecodes.nifixiegari.models.Mechanic;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private FragmentSearchBinding binding;
    private DatabaseReference databaseReference;

    private SearchMechanicsAdapter mechanicsAdapter = new SearchMechanicsAdapter(Mechanic.itemCallback);
    private ArrayList<Mechanic> mechanicArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        getMechanics();

        binding.searchCharacter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        binding.searchCharacter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (binding.searchCharacter.getRight() - binding.searchCharacter.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here

                        Snackbar.make(binding.getRoot(),"No Mechanic in that Location", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getActivity().getApplicationContext(), "No Mechanic in that Location", Toast.LENGTH_LONG).show();

                        return true;
                    }
                }
                return false;
            }
        });

        return view;
    }

    public void filter(String e) {
        ArrayList<Mechanic> filteredlist = new ArrayList<>();

        for (Mechanic item : mechanicArrayList) {
            if (item.getMechanicLocation().toLowerCase().contains(e.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        if (!filteredlist.isEmpty() || filteredlist != null){
            mechanicsAdapter.submitList(filteredlist);
        }else{
            Snackbar.make(binding.getRoot(),"No Mechanic in that Location", Snackbar.LENGTH_LONG).show();
            Toast.makeText(getActivity().getApplicationContext(), "No Mechanic in that Location", Toast.LENGTH_LONG).show();
        }
    }

    private void getMechanics() {
        mechanicArrayList.clear();
        databaseReference.child("mechanics").orderByChild("accountType").equalTo("Mechanic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot i : snapshot.getChildren()) {
                        Mechanic mechanic = i.getValue(Mechanic.class);
                        Log.d(TAG, "onDataChange: getRefKey" + mechanic.getUniqueUUID());
                        mechanicArrayList.add(mechanic);
                    }
                    mechanicsAdapter.submitList(mechanicArrayList);
                    binding.searchMechanicRecyclerview.setAdapter(mechanicsAdapter);
                } else {
                    Toast.makeText(requireContext(), "Data Does not Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}