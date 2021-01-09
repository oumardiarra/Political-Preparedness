package com.example.android.politicalpreparedness.election

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionsDetailsBinding


class ElectionsDetailsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentElectionsDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val election = ElectionsDetailsFragmentArgs.fromBundle(requireArguments()).selectedElection

        binding.electionInfo = election
        // Inflate the layout for this fragment
        return binding.root
    }

}