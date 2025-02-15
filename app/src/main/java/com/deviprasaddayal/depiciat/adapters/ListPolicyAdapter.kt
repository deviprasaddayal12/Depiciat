package com.deviprasaddayal.depiciat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.models.ItemPolicyModel

class ListPolicyAdapter(
    var context: Context,
    var itemPolicyModels: ArrayList<ItemPolicyModel>
): RecyclerView.Adapter<ListPolicyAdapter.PolicyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolicyHolder {
        return PolicyHolder(LayoutInflater.from(context).inflate(R.layout.row_policy, parent, false))
    }

    override fun getItemCount(): Int {
        return /*itemPolicyModels.size*/ 20
    }

    override fun onBindViewHolder(holder: PolicyHolder, position: Int) {

    }

    class PolicyHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}