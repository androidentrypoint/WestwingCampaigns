package com.example.campaigns.ui.campaigndetail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.campaigns.R
import com.example.campaigns.databinding.FragmentCampaignDetailBinding
import com.example.campaigns.util.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class CampaignDetailFragment : Fragment(R.layout.fragment_campaign_detail) {

    private val binding by viewBinding(FragmentCampaignDetailBinding::bind)

    private val viewModel by viewModels<CampaignDetailViewModel>()

    private val campaignDetailAdapter = CampaignDetailAdapter()

    private val args by navArgs<CampaignDetailFragmentArgs>()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                makeCall()
            } else {
                Toast.makeText(requireContext(), R.string.permission_required, Toast.LENGTH_LONG)
                    .show()
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pager.adapter = campaignDetailAdapter
        campaignDetailAdapter.submitList(listOf(args.selectedCampaign))
        binding.call.setOnClickListener {
            checkPermission {
                makeCall()
            }
        }
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        setUpObservers()

    }

    private fun setUpObservers() {
        viewModel.cachedCampaignFlow.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            campaignDetailAdapter.submitList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun makeCall() {
        startActivity(
            Intent(Intent.ACTION_CALL, Uri.parse(getString(R.string.westwing_support)))
        )
    }

    private fun checkPermission(performAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED -> {
                performAction()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) -> {

            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CALL_PHONE
                )
            }
        }
    }
}