package com.example.android_developer_assignment.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.android_developer_assignment.Model.Model;
import com.example.android_developer_assignment.Model.User;
import com.example.android_developer_assignment.databinding.EditUserFragmentBinding;
import com.squareup.picasso.Picasso;


public class EditUserFragment extends Fragment {


    EditUserFragmentBinding binding;
    ActivityResultLauncher<Void> cameraAppLauncher;
    ActivityResultLauncher<String> galleryAppLauncher;
    boolean photoSelected = false;
    EditUserViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EditUserViewModel.class);

        cameraAppLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), new ActivityResultCallback<Bitmap>() {
            @Override
            public void onActivityResult(Bitmap o) {
                if (o != null) {
                    binding.avatar.setImageBitmap(o);
                    photoSelected = true;
                }
            }
        });

        galleryAppLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                if (o != null) {
                    binding.avatar.setImageURI(o);
                    photoSelected = true;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = EditUserFragmentBinding.inflate(inflater, container, false);
        binding.cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraAppLauncher.launch(null);
            }
        });

        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryAppLauncher.launch("image/*");
            }
        });
        int id = EditUserFragmentArgs.fromBundle(getArguments()).getId();
        viewModel.getUserById(id).observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.email.setText(user.getEmail());
                binding.firstName.setText(user.getFirst_name());
                binding.lastName.setText(user.getLast_name());
                Picasso.get().load(user.getAvatar()).into(binding.avatar);
                binding.EditBtn.setOnClickListener(v -> {
                    if(!inputIsValid()) return;
                    if (photoSelected) {
                        binding.avatar.setDrawingCacheEnabled(true);
                        binding.avatar.buildDrawingCache();
                        Bitmap bitmap = ((BitmapDrawable) binding.avatar.getDrawable()).getBitmap();
                        Model.instance().uploadPhoto(String.valueOf(id), bitmap, uri -> {
                            update(id,uri,v);

                        });
                    }
                    else{
                        update(id,user.getAvatar(),v);
                    }
                });
            }
        });

        return binding.getRoot();
    }



    boolean inputIsValid(){
        if (binding.email.getText().toString().isEmpty()) {
            binding.email.setError("Email is required");
            return false;
        } else if (binding.firstName.getText().toString().isEmpty()) {
            binding.firstName.setError("First Name is required");
            return false;
        } else if (binding.lastName.getText().toString().isEmpty()) {
            binding.lastName.setError("Last Name is required");
            return false;
        } else {
            return true;
        }
    }

    void update(int id,String uri,View v) {
        String email = binding.email.getText().toString();
        String firstName = binding.firstName.getText().toString();
        String lastName = binding.lastName.getText().toString();
        User user = new User(id, email, firstName, lastName, uri);
        Model.instance().insert(user, (unused) -> {
            Navigation.findNavController(v).popBackStack();
        });
    }
}