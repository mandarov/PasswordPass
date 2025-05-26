package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> {

    private List<PasswordItem> passwordList;
    private PasswordActionListener listener;

    public PasswordAdapter(List<PasswordItem> passwordList, PasswordActionListener listener) {
        this.passwordList = passwordList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_password, parent, false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        PasswordItem passwordItem = passwordList.get(position);
        holder.serviceNameTextView.setText(passwordItem.getServiceName());
        holder.loginTextView.setText(passwordItem.getLogin());
        holder.passwordTextView.setText(passwordItem.getPassword());

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeletePassword(passwordItem.getId()); // Уведомляем слушателя
                passwordList.remove(position);
                notifyItemRemoved(position); // Удаляем элемент из адаптера
            }
        });
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    static class PasswordViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTextView, loginTextView, passwordTextView;
        Button deleteButton;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceNameTextView = itemView.findViewById(R.id.serviceNameTextView);
            loginTextView = itemView.findViewById(R.id.loginTextView);
            passwordTextView = itemView.findViewById(R.id.passwordTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}