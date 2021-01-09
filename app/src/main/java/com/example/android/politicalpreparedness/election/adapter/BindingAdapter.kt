package com.example.android.politicalpreparedness.election.adapter

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.domain.Election


@BindingAdapter("listElection")
fun bindElectionRecycleView(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)

}

@BindingAdapter("progressStaus")
fun displayLoadingStatus(progressBar: ProgressBar, it: Any?) {
    progressBar.visibility = if (it == null) View.VISIBLE else View.GONE
}