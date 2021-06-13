package com.example.campaigns.ui.campaigns

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.campaigns.R
import com.example.campaigns.databinding.FragmentCampaignListBinding
import com.example.campaigns.model.Campaign
import com.example.campaigns.util.GridSpacingItemDecoration
import com.example.campaigns.util.NetworkStatus
import com.example.campaigns.util.px
import com.example.campaigns.util.removeItemDecorations
import com.example.campaigns.util.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class CampaignListFragment : Fragment(R.layout.fragment_campaign_list), CampaignAdapter.Listener {

    private val viewModel by viewModels<CampaignListViewModel>()

    private val binding by viewBinding(FragmentCampaignListBinding::bind)

    private val adapter = CampaignAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                binding.campaignList.layoutManager = GridLayoutManager(requireContext(), 2)
                binding.campaignList.addItemDecoration(
                    GridSpacingItemDecoration(2, 8.px, 0, 0, 0)
                )
            }
            else -> {
                binding.campaignList.layoutManager = LinearLayoutManager(requireContext())
                binding.campaignList.removeItemDecorations()
            }
        }
        binding.campaignList.adapter = adapter
        binding.swipe.setOnRefreshListener {
            viewModel.getCampaigns()
        }
        binding.retry.setOnClickListener {
            viewModel.getCampaigns()
        }
        setUpObservers()
    }

    private fun setUpObservers() {
        viewModel.campaignFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            when (it) {
                is NetworkStatus.Error -> {
                    binding.swipe.isRefreshing = false
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    binding.swipe.isVisible = false
                    binding.errorLayout.isVisible = true
                }
                is NetworkStatus.Loading -> {
                    binding.swipe.isRefreshing = true
                    adapter.submitList(it.data)
                    binding.swipe.isVisible = true
                    binding.errorLayout.isVisible = false
                }
                is NetworkStatus.Success -> {
                    adapter.submitList(it.data)
                    binding.swipe.isVisible = true
                    binding.swipe.isRefreshing = false
                    binding.errorLayout.isVisible = false
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onItemClicked(view: View, adapterPosition: Int, campaign: Campaign) {
        findNavController().navigate(
            CampaignListFragmentDirections.actionCampaignListFragmentToCampaignDetailFragment(
                campaign
            )
        )
    }
}