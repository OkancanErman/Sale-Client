package com.oe.saleclient.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.oe.moviesearcher.util.hide
import com.oe.moviesearcher.util.hideKeyboard
import com.oe.moviesearcher.util.show
import com.oe.moviesearcher.util.toast
import com.oe.saleclient.databinding.AdditemFragmentBinding
import com.oe.saleclient.ui.viewmodels.ItemsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private var _binding: AdditemFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ItemsViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = AdditemFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        lifecycleScope.launchWhenStarted {
            viewModel.eventAddFlow.collect { event ->
                when(event) {
                    is ItemsViewModel.Status.Loading -> {
                        binding.progressBarAdd.show()
                        hideKeyboard()
                    }
                    is ItemsViewModel.Status.Success -> {
                        binding.progressBarAdd.hide()
                        findNavController().navigateUp()
                    }
                    is ItemsViewModel.Status.Error -> {
                        event.message?.let { requireContext().toast(it) }
                        binding.progressBarAdd.hide()
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}