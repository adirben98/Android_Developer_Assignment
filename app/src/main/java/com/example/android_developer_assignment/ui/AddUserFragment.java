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
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_developer_assignment.Model.Model;
import com.example.android_developer_assignment.Model.User;
import com.example.android_developer_assignment.R;
import com.example.android_developer_assignment.databinding.FragmentAddUserBinding;

import java.util.UUID;

public class AddUserFragment extends Fragment {

    FragmentAddUserBinding binding;
    ActivityResultLauncher<Void> cameraAppLauncher;
    ActivityResultLauncher<String> galleryAppLauncher;
    boolean photoSelected = false;
    EditUserViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(EditUserViewModel.class);
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                // Inflate the menu if needed, or skip if already inflated in the Activity
                // menuInflater.inflate(R.menu.menu_main, menu); // Uncomment if you need to inflate here
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                // Handle menu item selections if needed
                return false;
            }

            @Override
            public void onPrepareMenu(@NonNull Menu menu) {
                // Hide the Add button
                MenuItem addItem = menu.findItem(R.id.add);
                if (addItem != null) {
                    addItem.setVisible(false);
                }
            }
        }, this, Lifecycle.State.RESUMED);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddUserBinding.inflate(inflater, container, false);
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
        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputIsValid()) return;
                int id = Model.instance().generateNewId();
                if (!photoSelected) {
                    Uri defaultAvatarUri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.drawable.avatar);

                    User user = new User(id, binding.email.getText().toString(), binding.firstName.getText().toString(), binding.lastName.getText().toString(), defaultAvatarUri.toString());
                    Model.instance().insert(user, (unused) -> {
                        Navigation.findNavController(v).popBackStack(R.id.usersListFragment, false);
                    });                    return;
                }

                binding.avatar.setDrawingCacheEnabled(true);
                binding.avatar.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.avatar.getDrawable()).getBitmap();
                Model.instance().uploadPhoto(String.valueOf(id), bitmap, (uri -> {
                    User user = new User(id, binding.email.getText().toString(), binding.firstName.getText().toString(), binding.lastName.getText().toString(), uri);
                    Model.instance().insert(user, (unused) -> {
                        Navigation.findNavController(v).popBackStack(R.id.usersListFragment, false);
                    });
                }));
            }
        });
        return binding.getRoot();
    }

    private boolean inputIsValid() {
        if (binding.email.getText().toString().isEmpty()) {
            binding.email.setError("Email is required");
            return false;
        }
        else if (binding.firstName.getText().toString().isEmpty()) {
            binding.firstName.setError("First Name is required");
            return false;
        }
        else if (binding.lastName.getText().toString().isEmpty()) {
            binding.lastName.setError("Last Name is required");
            return false;
        }
        return true;
    }
}