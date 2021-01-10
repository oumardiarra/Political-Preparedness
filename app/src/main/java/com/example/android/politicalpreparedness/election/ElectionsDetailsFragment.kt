package com.example.android.politicalpreparedness.election

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionsDetailsBinding


class ElectionsDetailsFragment : Fragment() {

    private val viewModel: ElectionsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
                this,
                ElectionsViewModelFactory(activity.application)
        ).get(ElectionsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentElectionsDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val election = ElectionsDetailsFragmentArgs.fromBundle(requireArguments()).selectedElection
        binding.viewModel = viewModel
        binding.electionInfo = election
        viewModel.savedElection.observe(viewLifecycleOwner, Observer {

        })
        // Inflate the layout for this fragment
        return binding.root
    }

}