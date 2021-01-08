package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionItemsBinding

import com.example.android.politicalpreparedness.network.models.Election


class ElectionListAdapter(private val clickListener: ElectionListener) : ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {
    class ElectionViewHolder(private var binding: ElectionItemsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(election: Election) {
            binding.electionProperty = election
            binding.executePendingBindings()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        var view = ElectionItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ElectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(election)
        }
        holder.bind(election)
    }


    companion object ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem == newItem
        }

    }
}





class ElectionListener(val electionClickListner: (election: Election) -> Unit) {
    fun onClick(election: Election) = electionClickListner(election)
}