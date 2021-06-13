package com.example.campaigns.ui.campaigns

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.SharedElementCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.example.campaigns.MainActivity
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
import java.util.concurrent.atomic.AtomicBoolean


@AndroidEntryPoint
class CampaignListFragment : Fragment(R.layout.fragment_campaign_list), CampaignAdapter.Listener {

    private val viewModel by viewModels<CampaignListViewModel>()

    private val binding by viewBinding(FragmentCampaignListBinding::bind)

    private val adapter = CampaignAdapter(this)

    private val enterTransitionStarted = AtomicBoolean()

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
        prepareTransitions()
        postponeEnterTransition()
        scrollToPosition()
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

    private fun scrollToPosition() {
        binding.campaignList.addOnLayoutChangeListener(object : OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                binding.campaignList.removeOnLayoutChangeListener(this)
                val layoutManager = binding.campaignList.layoutManager
                val position = MainActivity.currentPosition
                val viewAtPosition = layoutManager?.findViewByPosition(position)

                if (viewAtPosition == null || layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)
                ) {
                    binding.campaignList.post { layoutManager?.scrollToPosition(position) }
                }
            }
        })
    }

    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_exit_transition)

        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: List<String?>,
                    sharedElements: MutableMap<String?, View?>
                ) {
                    val position = MainActivity.currentPosition
                    // Locate the ViewHolder for the clicked position.
                    val selectedViewHolder: RecyclerView.ViewHolder = binding.campaignList
                        .findViewHolderForAdapterPosition(position) ?: return

                    // Map the first shared element name to the child ImageView.
                    sharedElements[names[0]] =
                        selectedViewHolder.itemView.findViewById(R.id.campaign_img)
                }
            })
    }

    override fun onLoadCompleted(view: View, adapterPosition: Int) {
        val position = MainActivity.currentPosition
        if (position != adapterPosition) {
            return
        }
        if (enterTransitionStarted.getAndSet(true)) {
            return
        }
        startPostponedEnterTransition()
    }

    override fun onItemClicked(view: View, adapterPosition: Int, campaign: Campaign) {
        MainActivity.currentPosition = adapterPosition
        (exitTransition as? TransitionSet)?.excludeTarget(view, true)
        val transitioningView: ImageView = view.findViewById(R.id.campaign_img)
        findNavController().navigate(
            CampaignListFragmentDirections.actionCampaignListFragmentToCampaignDetailFragment(
                campaign
            ),
            FragmentNavigatorExtras(transitioningView to transitioningView.transitionName)
        )
    }
}