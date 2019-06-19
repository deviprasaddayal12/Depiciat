package com.deviprasaddayal.depiciat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.constants.StateFlow
import com.deviprasaddayal.depiciat.models.StateFlowModel
import com.deviprasaddayal.depiciat.utils.ViewUtils

class StateFlowAdapter(
    var context: Context,
    var stateFlowModels: ArrayList<StateFlowModel>
): RecyclerView.Adapter<StateFlowAdapter.PolicyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolicyHolder {
        return PolicyHolder(LayoutInflater.from(context).inflate(R.layout.row_state_add_policy, parent, false))
    }

    override fun getItemCount(): Int {
        return stateFlowModels.size
    }

    override fun onBindViewHolder(holder: PolicyHolder, position: Int) {
        val stateFlowModel = stateFlowModels.get(position)

        holder.tvStateTitle.text = stateFlowModel.stateTitle.replace(" ", "\n")
        holder.tvStatePosition.text = stateFlowModel.statePosition.toString()

        when(stateFlowModel.currentState){
            StateFlow.STATE_PASSED -> {
                holder.tvStateTitle.setTextColor(context.resources.getColor(R.color.color_stateFlowPrevious))
                holder.tvStatePosition.setTextColor(context.resources.getColor(R.color.color_stateFlowPrevious))
                holder.tvStatePosition.setBackgroundResource(R.drawable.state_flow_previous)
                holder.ivSeparator.setBackgroundColor(R.drawable.state_flow_next)
            }
            StateFlow.STATE_CURRENT -> {
                holder.tvStateTitle.setTextColor(context.resources.getColor(R.color.color_stateFlowCurrent))
                holder.tvStatePosition.setTextColor(context.resources.getColor(R.color.color_stateFlowCurrent))
                holder.tvStatePosition.setBackgroundResource(R.drawable.state_flow_current)
                holder.ivSeparator.setBackgroundColor(R.drawable.state_flow_next)
            }
            StateFlow.STATE_NEXT -> {
                holder.tvStateTitle.setTextColor(context.resources.getColor(R.color.color_stateFlowNext))
                holder.tvStatePosition.setTextColor(context.resources.getColor(R.color.color_stateFlowNext))
                holder.tvStatePosition.setBackgroundResource(R.drawable.state_flow_next)
                holder.ivSeparator.setBackgroundColor(R.drawable.state_flow_next)
            }
        }

        ViewUtils.toggleViewVisibility(position != stateFlowModels.size - 1, holder.ivSeparator)
    }

    class PolicyHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvStateTitle = itemView.findViewById<TextView>(R.id.tv_stateTitle)
        val tvStatePosition = itemView.findViewById<TextView>(R.id.tv_statePosition)
        val ivSeparator = itemView.findViewById<ImageView>(R.id.iv_separatorHorizontal)
    }
}