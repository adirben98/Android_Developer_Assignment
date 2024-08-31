package com.example.android_developer_assignment.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_developer_assignment.Model.User;
import com.example.android_developer_assignment.R;
import com.squareup.picasso.Picasso;

import java.util.List;

class ViewHolder extends RecyclerView.ViewHolder {
    TextView email, name;
    ImageView avatar;
    ImageButton edit;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        email = itemView.findViewById(R.id.emailTextView);
        name = itemView.findViewById(R.id.nameTextView);
        avatar = itemView.findViewById(R.id.avatarImageView);
        edit = itemView.findViewById(R.id.editBtn);
    }

    public void bind(User user, OnItemClickListener onItemClickListener) {
        email.setText(user.getEmail());
        name.setText(user.getFirst_name() + " " + user.getLast_name());
        Picasso.get().load(user.getAvatar()).into(avatar);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(getAbsoluteAdapterPosition());
            }
    });
}}

public class UserListAdapter extends RecyclerView.Adapter<ViewHolder> {
    List<User> data;
    LayoutInflater inflater;
    OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public UserListAdapter(List<User> data, LayoutInflater inflater) {
        this.data = data;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(inflater.inflate(R.layout.user_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position),onItemClickListener);

    }

    @Override
    public int getItemCount() {

        return data.size();
    }
    public void setData(List<User> data) {
        this.data = data;
        notifyDataSetChanged();
    }

}
