package com.example.timetrackerv2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DatabaseAdapter extends RecyclerView.Adapter<DatabaseAdapter.MyViewHolder> {

    private List<AppModel> mList;
    private MainActivity activity;
    private DatabaseHelper myDB;
    private OnItemClickListener mListener; // Ajout d'un écouteur de clic sur les éléments de la liste

    // Interface pour l'écouteur de clic sur les éléments de la liste
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Méthode pour définir l'écouteur de clic sur les éléments de la liste
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public DatabaseAdapter(DatabaseHelper myDB, MainActivity activity) {
        this.activity = activity;
        this.myDB = myDB;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (mList != null && mList.size() > 0) {
            final AppModel item = mList.get(position);
            holder.mCheckBox.setText(item.getTask());
            holder.mCheckBox.setChecked(toBoolean(item.getStatus()));
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        myDB.updateStatus(item.getId(), 1);
                    } else {
                        myDB.updateStatus(item.getId(), 0);
                    }
                }
            });

            // Gestion du clic sur l'élément de la liste
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = holder.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public boolean toBoolean(int num) {
        return num != 0;
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<AppModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        if (mList != null && mList.size() > position) {
            AppModel item = mList.get(position);
            myDB.deleteTask(item.getId());
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void editItem(int position) {
        if (mList != null && mList.size() > position) {
            AppModel item = mList.get(position);
            Bundle bundle = new Bundle();
            bundle.putInt("id", item.getId());
            bundle.putString("task", item.getTask());
            AddNewTask task = new AddNewTask();
            task.setArguments(bundle);
            task.show(activity.getSupportFragmentManager(), task.getTag());
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}