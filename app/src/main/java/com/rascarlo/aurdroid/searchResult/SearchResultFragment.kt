package com.rascarlo.aurdroid.searchResult

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.rascarlo.aurdroid.R
import com.rascarlo.aurdroid.databinding.FragmentSearchResultBinding
import com.rascarlo.aurdroid.utils.*
import kotlinx.coroutines.*
import timber.log.Timber

class SearchResultFragment : Fragment() {

    private var sortArg: Int = Constants.SORT_BY_PACKAGE_NAME
    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var adapter: SearchResultAdapter

    companion object {
        private const val OUTSTATE_SORT = "sort_outstate"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater)
        val keywordArg: String = SearchResultFragmentArgs.fromBundle(requireArguments()).keyword
        val fieldArg: String = SearchResultFragmentArgs.fromBundle(requireArguments()).field
        sortArg = SearchResultFragmentArgs.fromBundle(requireArguments()).sort
        Timber.d("Keyword $keywordArg")
        Timber.d("Field $fieldArg")
        Timber.d("Sort $sortArg")
        // view model factory
        val viewModelFactory =
            SearchResultViewModelFactory(keywordArg, fieldArg, sortArg)
        // view model
        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchResultViewModel::class.java)
        // binding
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        // adapter
        adapter = SearchResultAdapter(SearchResultAdapter.OnClickListener {
            viewModel.displaySelectedPackage(it)
        })
        // recycler view
        val recyclerView: RecyclerView = binding.fragmentSearchResultRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        // observe search result list view model
        viewModel.searchResultList.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                adapter.submitList(it)
                binding.fragmentSearchResultInfoTextView.text =
                    String.format(
                        resources.getQuantityString(
                            R.plurals.plurals_formatted_package_found,
                            it.size,
                            it.size,
                            keywordArg
                        )
                    )
            }
        })
        // observe search result status view model
        viewModel.status.observe(viewLifecycleOwner, Observer {
            bindAurDroidApiStatusImageView(
                binding.fragmentSearchResultStatusInclude.apiStatusImage,
                it
            )
            bindAurDroidApiStatusTextView(
                binding.fragmentSearchResultStatusInclude.apiStatusText,
                it
            )
            bindAurDroidApiStatusProgressBar(
                binding.fragmentSearchResultStatusInclude.apiStatusProgressBar,
                it
            )
        })
        // observe search result error view model
        viewModel.error.observe(viewLifecycleOwner, Observer {
            bindAurDroidApiErrorTextView(
                binding.fragmentSearchResultStatusInclude.apiErrorText,
                it
            )
        })
        // observe navigation
        viewModel.displayPackage.observe(viewLifecycleOwner, Observer {
            if (it?.name != null && it.name.isNotEmpty()) {
                this.findNavController()
                    .navigate(
                        SearchResultFragmentDirections
                            .actionSearchResultFragmentToInfoResultFragment(it.name)
                    )
                viewModel.displaySelectedPackageComplete()
            }
        })
        // add options menu
        setHasOptionsMenu(true)
        // inflate layout
        return binding.root
    }

    private fun submitSortedList() {
        binding.viewModel?.sortList(sortArg)
        adapter.notifyItemRangeChanged(0, adapter.itemCount)
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                delay(500)
                binding.fragmentSearchResultRecyclerView.scrollToPosition(0)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_result, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_search_result_item_sort).isVisible =
            binding.viewModel?.searchResultList?.value != null
        menu.apply {
            when (sortArg) {
                Constants.SORT_BY_PACKAGE_NAME
                -> findItem(R.id.menu_sort_by_package_name).isChecked = true
                Constants.SORT_BY_VOTES
                -> findItem(R.id.menu_sort_by_votes).isChecked = true
                Constants.SORT_BY_POPULARITY
                -> findItem(R.id.menu_sort_by_popularity).isChecked = true
                Constants.SORT_BY_LAST_UPDATED
                -> findItem(R.id.menu_sort_by_last_update).isChecked = true
                Constants.SORT_BY_FIRST_SUBMITTED
                -> findItem(R.id.menu_sort_by_first_submitted).isChecked = true
            }
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val newSort = when (item.itemId) {
            R.id.menu_sort_by_package_name -> Constants.SORT_BY_PACKAGE_NAME
            R.id.menu_sort_by_votes -> Constants.SORT_BY_VOTES
            R.id.menu_sort_by_popularity -> Constants.SORT_BY_POPULARITY
            R.id.menu_sort_by_last_update -> Constants.SORT_BY_LAST_UPDATED
            R.id.menu_sort_by_first_submitted -> Constants.SORT_BY_FIRST_SUBMITTED
            else -> sortArg
        }
        if (newSort != sortArg) {
            sortArg = newSort
            submitSortedList()
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(OUTSTATE_SORT, sortArg)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        sortArg =
            savedInstanceState?.getInt(OUTSTATE_SORT) ?: Constants.SORT_BY_PACKAGE_NAME
        super.onViewStateRestored(savedInstanceState)
    }
}