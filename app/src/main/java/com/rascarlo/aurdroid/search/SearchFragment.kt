package com.rascarlo.aurdroid.search

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.rascarlo.aurdroid.R
import com.rascarlo.aurdroid.databinding.FragmentSearchBinding
import com.rascarlo.aurdroid.searchResult.SearchResultFragment
import com.rascarlo.aurdroid.utils.Constants
import timber.log.Timber

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding
        binding = FragmentSearchBinding.inflate(inflater)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        // option menu
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // input edit text
        setInputEditText()
        // field
        setField()
        // sort
        setSort()
    }

    private fun setInputEditText() {
        binding.fragmentSearchKeywordLayout.searchKeywordTextInputEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if ((v.editableText != null && v.editableText.toString().trim().length >= 2)) {
                    val keyword = v.editableText.toString().trim()
                    val field = getField()
                    val sort = getSort()
                    Timber.d("keyword: $keyword")
                    Timber.d("field: $field")
                    Timber.d("sort: $sort")
                    /**
                     * query is valid:
                     * navigate to [SearchResultFragment]
                     */
                    v.findNavController()
                        .navigate(
                            SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(
                                keyword,
                                field,
                                sort
                            )
                        )
                } else {
                    val hint = v.context.resources.getString(R.string.keywords_hint)
                    v.error = hint.substringBefore(delimiter = ".", missingDelimiterValue = hint)
                }
            }
            val inputMethodManager =
                v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
            true
        }
    }

    /**
     * init field chip group
     */
    private fun setField() {
        binding.fragmentSearchFieldLayout.fieldChipGroup.apply {
            val preferenceField = sharedPreferences.getString(
                getString(R.string.key_field),
                getString(R.string.key_field_default_value)
            )
            when (preferenceField) {
                getString(R.string.key_field_name_or_description) -> check(R.id.field_chip_name_or_description)
                getString(R.string.key_field_name) -> check(R.id.field_chip_name)
                getString(R.string.key_field_maintainer) -> check(R.id.field_chip_maintainer)
                getString(R.string.key_field_depends) -> check(R.id.field_chip_depends)
                getString(R.string.key_field_maintainer) -> check(R.id.field_chip_make_depends)
                getString(R.string.key_field_opt_depends) -> check(R.id.field_chip_opt_depends)
                getString(R.string.key_field_check_depends) -> check(R.id.field_chip_check_depends)
                else -> check(R.id.field_chip_name_or_description)
            }
        }
    }

    /**
     * init sort radio group
     */
    private fun setSort() {
        binding.fragmentSearchSortLayout.searchSortRadioGroup.apply {
            val preferenceSort = sharedPreferences.getString(
                getString(R.string.key_sort),
                getString(R.string.key_sort_default_value)
            )
            when (preferenceSort) {
                getString(R.string.key_field_name_or_description) -> check(R.id.field_chip_name_or_description)
                getString(R.string.key_field_name) -> check(R.id.field_chip_name)
                getString(R.string.key_field_maintainer) -> check(R.id.field_chip_maintainer)
                getString(R.string.key_field_depends) -> check(R.id.field_chip_depends)
                getString(R.string.key_field_make_depends) -> check(R.id.field_chip_make_depends)
                getString(R.string.key_field_opt_depends) -> check(R.id.field_chip_opt_depends)
                getString(R.string.key_field_check_depends) -> check(R.id.field_chip_check_depends)
            }
        }
    }

    /**
     * @return any from
     * [Constants.SEARCH_FIELD_NAME_OR_DESCRIPTION]
     * [Constants.SEARCH_FIELD_NAME]
     * [Constants.SEARCH_FIELD_MAINTAINER]
     * [Constants.SEARCH_FIELD_DEPENDS]
     * [Constants.SEARCH_FIELD_MAKE_DEPENDS]
     * [Constants.SEARCH_FIELD_OPT_DEPENDS]
     * [Constants.SEARCH_FIELD_CHECK_DEPENDS]
     *
     * default to [Constants.SEARCH_FIELD_NAME_OR_DESCRIPTION]
     */
    private fun getField(): String {
        binding.apply {
            return when (fragmentSearchFieldLayout.fieldChipGroup.checkedChipId) {
                R.id.field_chip_name_or_description -> Constants.SEARCH_FIELD_NAME_OR_DESCRIPTION
                R.id.field_chip_name -> Constants.SEARCH_FIELD_NAME
                R.id.field_chip_maintainer -> Constants.SEARCH_FIELD_MAINTAINER
                R.id.field_chip_depends -> Constants.SEARCH_FIELD_DEPENDS
                R.id.field_chip_make_depends -> Constants.SEARCH_FIELD_MAKE_DEPENDS
                R.id.field_chip_opt_depends -> Constants.SEARCH_FIELD_OPT_DEPENDS
                R.id.field_chip_check_depends -> Constants.SEARCH_FIELD_CHECK_DEPENDS
                else -> Constants.SEARCH_FIELD_NAME_OR_DESCRIPTION
            }
        }
    }

    /**
     * @return any from
     * [Constants.SORT_BY_PACKAGE_NAME]
     * [Constants.SORT_BY_VOTES]
     * [Constants.SORT_BY_POPULARITY]
     * [Constants.SORT_BY_LAST_UPDATED]
     * [Constants.SORT_BY_FIRST_SUBMITTED]
     *
     * default to [Constants.SORT_BY_PACKAGE_NAME]
     */
    private fun getSort(): Int {
        binding.apply {
            return when (fragmentSearchSortLayout.searchSortRadioGroup.checkedRadioButtonId) {
                R.id.sort_radio_button_package_name -> Constants.SORT_BY_PACKAGE_NAME
                R.id.sort_radio_button_votes -> Constants.SORT_BY_VOTES
                R.id.sort_radio_button_popularity -> Constants.SORT_BY_POPULARITY
                R.id.sort_radio_button_last_updated -> Constants.SORT_BY_LAST_UPDATED
                R.id.sort_radio_button_first_submitted -> Constants.SORT_BY_FIRST_SUBMITTED
                else -> Constants.SORT_BY_PACKAGE_NAME
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search_settings -> this.findNavController()
                .navigate(SearchFragmentDirections.actionGlobalSettings())
        }
        return super.onOptionsItemSelected(item)
    }
}