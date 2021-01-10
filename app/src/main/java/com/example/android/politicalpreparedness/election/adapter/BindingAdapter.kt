package com.example.android.politicalpreparedness.election.adapter

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.domain.Election


@BindingAdapter("listElection")
fun bindElectionRecycleView(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)

}
@BindingAdapter("listSavedElection")
fun bindSavedElectionRecycleView(recyclerView: RecyclerView, data: List<Election>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)

}

@BindingAdapter("progressStaus")
fun displayLoadingStatus(progressBar: ProgressBar, it: Any?) {
    progressBar.visibility = if (it == null) View.VISIBLE else View.GONE
}

@BindingAdapter("followStatus")
fun AdaptButtonActionAndText(button: Button, isSaved: Boolean) {
    if (isSaved) {
        button.text = button.context.getString(R.string.un_follow_election_text)
        //set action to delete saved election
    }
    else{
        button.text = button.context.getString(R.string.follow_election_text)
        // set action to save the election to the database
    }

}