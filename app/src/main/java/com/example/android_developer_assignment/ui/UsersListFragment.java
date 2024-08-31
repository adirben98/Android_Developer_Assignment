package com.example.android_developer_assignment.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_developer_assignment.Model.Model;
import com.example.android_developer_assignment.Model.User;
import com.example.android_developer_assignment.R;
import com.example.android_developer_assignment.databinding.FragmentUserListBinding;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class UsersListFragment extends Fragment {
    FragmentUserListBinding binding;
    UsersListViewModel viewModel;
    UserListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(UsersListViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserListBinding.inflate(inflater, container, false);
        binding.userList.setHasFixedSize(true);
        binding.userList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new UserListAdapter(new ArrayList<>(), inflater);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                UsersListFragmentDirections.ActionUsersListFragmentToEditUserFragment action = UsersListFragmentDirections.actionUsersListFragmentToEditUserFragment(viewModel.getUsers().getValue().get(pos).getId());
                Navigation.findNavController(binding.getRoot()).navigate(action);
            }
        });
        binding.userList.setAdapter(adapter);

        viewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            adapter.setData(users);
        });

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.userList);

        return binding.getRoot();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            User user = viewModel.getUsers().getValue().get(viewHolder.getAbsoluteAdapterPosition());

            if (direction == ItemTouchHelper.LEFT) {
                Model.instance().delete(user);
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.red)).addSwipeLeftActionIcon(R.drawable.delete_icon)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


}
