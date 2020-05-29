package com.rascarlo.aurdroid.searchResult

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.rascarlo.aurdroid.R
import com.rascarlo.aurdroid.databinding.FragmentSearchResultBinding
import com.rascarlo.aurdroid.utils.*
import kotlinx.coroutines.*
import timber.log.Timber

class SearchResultFragment : Fragment() {

    private val args: SearchResultFragmentArgs by navArgs()
    private var sortArg: String = SortEnum.PACKAGE_NAME.toString()
    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var adapter: SearchResultAdapter

    companion object {
        private const val OUTSTATE_SORT = "sort_outstate"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater)
        val keywordArg: String = args.keyword
        val fieldArg: String = args.field
        sortArg = args.sort
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
                activity?.invalidateOptionsMenu()
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
                SortEnum.PACKAGE_NAME.toString()
                -> findItem(R.id.menu_sort_by_package_name).isChecked = true
                SortEnum.VOTES.toString()
                -> findItem(R.id.menu_sort_by_votes).isChecked = true
                SortEnum.POPULARITY.toString()
                -> findItem(R.id.menu_sort_by_popularity).isChecked = true
                SortEnum.LAST_UPDATED.toString()
                -> findItem(R.id.menu_sort_by_last_update).isChecked = true
                SortEnum.FIRST_SUBMITTED.toString()
                -> findItem(R.id.menu_sort_by_first_submitted).isChecked = true
            }
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val newSort = when (item.itemId) {
            R.id.menu_sort_by_package_name -> SortEnum.PACKAGE_NAME.toString()
            R.id.menu_sort_by_votes -> SortEnum.VOTES.toString()
            R.id.menu_sort_by_popularity -> SortEnum.POPULARITY.toString()
            R.id.menu_sort_by_last_update -> SortEnum.LAST_UPDATED.toString()
            R.id.menu_sort_by_first_submitted -> SortEnum.FIRST_SUBMITTED.toString()
            else -> sortArg
        }
        if (newSort != sortArg) {
            sortArg = newSort
            submitSortedList()
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(OUTSTATE_SORT, sortArg)
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        sortArg =
            savedInstanceState?.getString(OUTSTATE_SORT) ?: SortEnum.PACKAGE_NAME.toString()
        super.onViewStateRestored(savedInstanceState)
    }
}