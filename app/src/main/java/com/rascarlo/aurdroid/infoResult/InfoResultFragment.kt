package com.rascarlo.aurdroid.infoResult

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rascarlo.aurdroid.R
import com.rascarlo.aurdroid.databinding.FragmentInfoResultBinding
import com.rascarlo.aurdroid.utils.*
import timber.log.Timber

class InfoResultFragment : Fragment() {

    private val args: InfoResultFragmentArgs by navArgs()
    private lateinit var binding: FragmentInfoResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding
        binding = FragmentInfoResultBinding.inflate(inflater)
        // arg
        val nameArg: String = args.name
        Timber.d("name: $nameArg")
        // view model factory
        val viewModelFactory = InfoResultViewModelFactory(nameArg)
        // view model
        val viewModel: InfoResultViewModel =
            ViewModelProvider(this, viewModelFactory).get(InfoResultViewModel::class.java)
        // lifecycle owner
        binding.lifecycleOwner = this
        // assign view model
        binding.viewModel = viewModel
        // observe live data
        viewModel.infoResult.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                binding.infoResult = it
                // assign adapters
                assignAdapters()
                // execute bindings
                binding.executePendingBindings()
            } else {
                binding.infoResult = null
            }
        })
        // observe search result status view model
        viewModel.status.observe(viewLifecycleOwner, Observer {
            bindAurDroidApiStatusImageView(
                binding.fragmentInfoResultStatusInclude.apiStatusImage,
                it
            )
            bindAurDroidApiStatusTextView(
                binding.fragmentInfoResultStatusInclude.apiStatusText,
                it
            )
            bindAurDroidApiStatusProgressBar(
                binding.fragmentInfoResultStatusInclude.apiStatusProgressBar,
                it
            )
        })
        // observe search result error view model
        viewModel.error.observe(viewLifecycleOwner, Observer {
            bindAurDroidApiErrorTextView(
                binding.fragmentInfoResultStatusInclude.apiErrorText,
                it
            )
        })
        // options menu
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun assignAdapters() {
        binding.apply {
            // depends
            infoResultDependsInclude.infoResultDependsRecyclerView.adapter =
                DependencyAdapter()
            // make depends
            infoResultMakeDependsInclude.infoResultMakeDependsRecyclerView.adapter =
                DependencyAdapter()
            // opt depends
            infoResultOptDependsInclude.infoResultOptDependsRecyclerView.adapter =
                DependencyAdapter()
            // check depends
            infoResultCheckDependsInclude.infoResultCheckDependsRecyclerView.adapter =
                DependencyAdapter()
            // conflicts
            infoResultConflictsInclude.infoResultConflictsRecyclerView.adapter =
                DependencyAdapter()
            // provides
            infoResultProvidesInclude.infoResultProvidesRecyclerView.adapter =
                DependencyAdapter()
            // replaces
            infoResultReplacesInclude.infoResultReplacesRecyclerView.adapter =
                DependencyAdapter()
            // groups
            infoResultGroupsInclude.infoResultGroupsRecyclerView.adapter =
                DependencyAdapter()
            // keywords
            infoResultKeywordsInclude.infoResultKeywordsRecyclerView.adapter =
                DependencyAdapter()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_info_result, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.apply {
            setGroupVisible(
                R.id.menu_info_result_share,
                null != binding.infoResult
            )
            setGroupVisible(
                R.id.menu_info_result_main,
                null != binding.infoResult
            )
            // view pkgbuild
            findItem(R.id.menu_info_result_view_pkgbuild).isEnabled = null != getViewPkgbuildUri()
            // view changes
            findItem(R.id.menu_info_result_view_changes).isEnabled = null != getViewChangesUri()
            // maintainer
            findItem(R.id.menu_info_result_maintainer).isEnabled = null != getMaintainer()
            // open in browser
            findItem(R.id.menu_info_result_open_in_browser).isEnabled = null != getUri()
            // download snapshot
            findItem(R.id.menu_info_result_download_snapshot).isEnabled = null != getSnapshotUri()
            // share pkgbuild
            findItem(R.id.menu_info_result_share_pkgbuild).isEnabled = null != getViewChangesUri()
            // share changes
            findItem(R.id.menu_info_result_share_changes).isEnabled = null != getViewChangesUri()
            // share package
            findItem(R.id.menu_info_result_share_package).isEnabled = null != getUri()
            // share upstream url
            findItem(R.id.menu_info_result_share_upstream_url).isEnabled = null != getUpstreamUrl()
            // share git clone url
            findItem(R.id.menu_info_result_share_git_clone_url).isEnabled = null != getGitCloneUrl()
            // share snapshot
            findItem(R.id.menu_info_result_share_snapshot).isEnabled = null != getSnapshotUri()
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // view pkgbuild
            R.id.menu_info_result_view_pkgbuild -> startIntentViewUri(getViewPkgbuildUri())
            // view changes
            R.id.menu_info_result_view_changes -> startIntentViewUri(getViewChangesUri())
            // maintainer
            R.id.menu_info_result_maintainer -> browseMaintainer()
            // open in browser
            R.id.menu_info_result_open_in_browser -> startIntentViewUri(getUri())
            // download snapshot
            R.id.menu_info_result_download_snapshot -> startIntentViewUri(getSnapshotUri())
            // share pkgbuild
            R.id.menu_info_result_share_pkgbuild -> startIntentShare(getViewPkgbuildUri().toString())
            // share changes
            R.id.menu_info_result_share_changes -> startIntentShare(getViewChangesUri().toString())
            // share package
            R.id.menu_info_result_share_package -> startIntentShare(getUri().toString())
            // share upstream url
            R.id.menu_info_result_share_upstream_url -> startIntentShare(getUpstreamUrl())
            // share git clone url
            R.id.menu_info_result_share_git_clone_url -> startIntentShare(getGitCloneUrl().toString())
            // share snapshot
            R.id.menu_info_result_share_snapshot -> startIntentShare(getSnapshotUri().toString())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getViewPkgbuildUri(): Uri? = when {
        binding.infoResult?.packageBase != null
        -> Uri.parse(Constants.PKGBUILD_BASE_URL)
            .buildUpon()
            .appendQueryParameter("h", binding.infoResult!!.packageBase)
            .build()
        else -> null
    }

    private fun getViewChangesUri(): Uri? {
        return when {
            binding.infoResult?.packageBase != null
            -> Uri.parse(Constants.PACKAGE_LOG_BASE_URL)
                .buildUpon()
                .appendQueryParameter("h", binding.infoResult!!.packageBase)
                .build()
            else -> null
        }
    }

    private fun getMaintainer(): String? {
        return binding.infoResult?.maintainer?.trim() ?: return null
    }

    private fun getUri(): Uri? = when {
        binding.infoResult?.name != null
        -> Uri.parse(Constants.PACKAGES_BASE_URL)
            .buildUpon()
            .appendPath(binding.infoResult!!.name)
            .build()
        else -> null
    }

    private fun getSnapshotUri(): Uri? = when {
        binding.infoResult?.name != null
        -> Uri.parse(Constants.PACKAGE_SNAPSHOT_BASE_URL)
            .buildUpon()
            .appendPath(binding.infoResult!!.urlPath)
            .build()
        else -> null
    }

    private fun getUpstreamUrl(): String? {
        return binding.infoResult?.url ?: return null
    }

    private fun getGitCloneUrl(): Uri? = when {
        binding.infoResult?.name != null
        -> Uri.parse(Constants.PACKAGES_GIT_CLONE_BASE_URL)
            .buildUpon()
            .appendPath(binding.infoResult!!.name + ".git")
            .build()
        else -> null
    }

    private fun startIntentViewUri(uri: Uri?) {
        try {
            binding.root.context?.startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (e: Exception) {
            showToast()
            Timber.e(e)
        }
    }

    private fun startIntentShare(string: String?) {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, string)
            intent.type = "text/plain"
            binding.root.context?.startActivity(intent)
        } catch (e: Exception) {
            showToast()
            Timber.e(e)
        }
    }

    private fun browseMaintainer() = this.findNavController()
        .navigate(
            InfoResultFragmentDirections
                .actionInfoResultFragmentToSearchResultFragment(
                    getMaintainer()!!,
                    FieldEnum.MAINTAINER.toString(),
                    SortEnum.PACKAGE_NAME.toString()
                )
        )

    private fun showToast() = Toast.makeText(
        binding.root.context,
        binding.root.context.resources.getString(R.string.something_went_wrong),
        Toast.LENGTH_SHORT
    ).show()
}