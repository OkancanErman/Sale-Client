package com.oe.saleclient.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oe.moviesearcher.util.hide
import com.oe.moviesearcher.util.hideKeyboard
import com.oe.moviesearcher.util.show
import com.oe.moviesearcher.util.toast
import com.oe.saleclient.R
import com.oe.saleclient.adapters.ItemsAdapter
import com.oe.saleclient.databinding.ItemsFragmentBinding
import com.oe.saleclient.ui.viewmodels.ItemsViewModel
import com.oe.saleclient.ui.fragments.RecyclerViewClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemsFragment : Fragment(), RecyclerViewClickListener {

    private var _binding: ItemsFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ItemsViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ItemsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.items.observe(viewLifecycleOwner, Observer { items->
            binding.recyclerviewItems.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = ItemsAdapter(items, this)
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                when(event) {
                    is ItemsViewModel.Status.Loading -> {
                        binding.progressBar.show()
                        hideKeyboard()
                    }
                    is ItemsViewModel.Status.Success -> {
                        binding.progressBar.hide()
                    }
                    is ItemsViewModel.Status.Error -> {
                        event.message?.let { requireContext().toast(it) }
                        binding.progressBar.hide()
                    }
                }
            }
        }

        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRecyclerViewItemClick(view: View, index: Int) {
        when(view.id){
            R.id.layout1 -> {
                viewModel.setItem(index)
                val action = ItemsFragmentDirections.actionItemsFragmentToControlFragment(index)
                findNavController().navigate(action)
            }
        }
    }


}