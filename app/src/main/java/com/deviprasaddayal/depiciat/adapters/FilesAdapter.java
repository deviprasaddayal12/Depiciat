package com.deviprasaddayal.depiciat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.deviprasaddayal.depiciat.R;
import com.deviprasaddayal.depiciat.listeners.OnFileActionListener;
import com.deviprasaddayal.depiciat.utils.DialogUtils;
import com.deviprasaddayal.depiciat.utils.ViewUtils;

import java.io.File;
import java.util.ArrayList;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDeleteImage;
        TextView tvFileName;
        View separator;

        public ViewHolder(View itemView) {
            super(itemView);
            ivDeleteImage = itemView.findViewById(R.id.iv_delete_file);
            tvFileName = itemView.findViewById(R.id.tv_name_file);
            separator = itemView.findViewById(R.id.separator);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onFileActionListener != null) {
                        onFileActionListener.onFileViewRequest(getAdapterPosition());
                    }
                }
            });
            ivDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Runnable deleteRunnable = new Runnable() {
                        @Override
                        public void run() {
                            int position = getAdapterPosition();
                            String removedFilePath = deleteImage(position);
                            if (onFileActionListener != null) {
                                onFileActionListener.onFileDeleted(position, removedFilePath);
                            }
                        }
                    };
                    DialogUtils.showDeleteDialog(context, deleteRunnable);
                }
            });
        }
    }

    private Context context;
    private RecyclerView recyclerPaths;
    private ArrayList<String> pathList;

    private OnFileActionListener onFileActionListener;

    private boolean showDelete;

    public FilesAdapter(Context context, RecyclerView recyclerPaths, ArrayList<String> pathList,
                        OnFileActionListener onFileActionListener, boolean showDelete) {
        this.context = context;
        this.recyclerPaths = recyclerPaths;
        this.pathList = pathList;
        this.onFileActionListener = onFileActionListener;
        this.showDelete = showDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_file_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ViewUtils.toggleViewVisibility(showDelete, viewHolder.ivDeleteImage);
        ViewUtils.toggleViewVisibility(i != pathList.size() - 1, viewHolder.separator);

        File file = new File(pathList.get(i));
        viewHolder.tvFileName.setText(file.getName());
    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    private String deleteImage(int position) {
        String removedFilePath = pathList.remove(position);
//        notifyItemRemoved(position);
        notifyDataSetChanged();
        return removedFilePath;
    }
}
