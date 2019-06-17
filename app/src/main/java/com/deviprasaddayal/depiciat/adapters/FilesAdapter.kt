package com.deviprasaddayal.depiciat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.listeners.OnFileActionListener
import com.deviprasaddayal.depiciat.utils.DialogUtils
import com.deviprasaddayal.depiciat.utils.ViewUtils

import java.io.File
import java.util.ArrayList

class FilesAdapter(
    private val context: Context,
    private val recyclerPaths: RecyclerView,
    private val pathList: ArrayList<String>,
    private val onFileActionListener: OnFileActionListener?,
    private val showDelete: Boolean
) : RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var ivDeleteImage: ImageView
        internal var tvFileName: TextView
        internal var separator: View

        init {
            ivDeleteImage = itemView.findViewById(R.id.iv_delete_file)
            tvFileName = itemView.findViewById(R.id.tv_name_file)
            separator = itemView.findViewById(R.id.separator)

            itemView.setOnClickListener {
                onFileActionListener?.onFileViewRequest(adapterPosition)
            }
            ivDeleteImage.setOnClickListener {
                val deleteRunnable = Runnable {
                    val position = adapterPosition
                    val removedFilePath = deleteImage(position)
                    onFileActionListener?.onFileDeleted(position, removedFilePath)
                }
                DialogUtils.showDeleteDialog(context, deleteRunnable)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.row_file_adapter, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        ViewUtils.toggleViewVisibility(showDelete, viewHolder.ivDeleteImage)
        ViewUtils.toggleViewVisibility(i != pathList.size - 1, viewHolder.separator)

        val file = File(pathList[i])
        viewHolder.tvFileName.text = file.name
    }

    override fun getItemCount(): Int {
        return pathList.size
    }

    private fun deleteImage(position: Int): String {
        val removedFilePath = pathList.removeAt(position)
        //        notifyItemRemoved(position);
        notifyDataSetChanged()
        return removedFilePath
    }
}
