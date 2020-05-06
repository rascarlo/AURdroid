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
import com.rascarlo.aurdroid.utils.FieldEnum
import com.rascarlo.aurdroid.utils.SortEnum
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
     * init sort chip group
     */
    private fun setSort() {
        binding.fragmentSearchSortLayout.searchSortChipGroup.apply {
            val preferenceSort = sharedPreferences.getString(
                getString(R.string.key_sort),
                getString(R.string.key_sort_default_value)
            )
            when (preferenceSort) {
                getString(R.string.key_sort_by_package_name) -> check(R.id.sort_chip_package_name)
                getString(R.string.key_sort_by_votes) -> check(R.id.sort_chip_votes)
                getString(R.string.key_sort_by_popularity) -> check(R.id.sort_chip_popularity)
                getString(R.string.key_sort_by_last_updated) -> check(R.id.sort_chip_last_updated)
                getString(R.string.key_sort_by_first_submitted) -> check(R.id.sort_chip_first_submitted)
                else -> check(R.id.sort_chip_package_name)
            }
        }
    }

    /**
     * @return any from
     * [FieldEnum.NAME_OR_DESCRIPTION]
     * [FieldEnum.NAME]
     * [FieldEnum.MAINTAINER]
     * [FieldEnum.DEPENDS]
     * [FieldEnum.MAKE_DEPENDS]
     * [FieldEnum.OPT_DEPENDS]
     * [FieldEnum.CHECK_DEPENDS]
     *
     * default to [FieldEnum.NAME_OR_DESCRIPTION]
     */
    private fun getField(): String {
        binding.apply {
            return when (fragmentSearchFieldLayout.fieldChipGroup.checkedChipId) {
                R.id.field_chip_name_or_description -> FieldEnum.NAME_OR_DESCRIPTION.toString()
                R.id.field_chip_name -> FieldEnum.NAME.toString()
                R.id.field_chip_maintainer -> FieldEnum.MAINTAINER.toString()
                R.id.field_chip_depends -> FieldEnum.DEPENDS.toString()
                R.id.field_chip_make_depends -> FieldEnum.MAKE_DEPENDS.toString()
                R.id.field_chip_opt_depends -> FieldEnum.OPT_DEPENDS.toString()
                R.id.field_chip_check_depends -> FieldEnum.CHECK_DEPENDS.toString()
                else -> FieldEnum.NAME_OR_DESCRIPTION.toString()
            }
        }
    }

    /**
     * @return any from
     * [SortEnum.PACKAGE_NAME]
     * [SortEnum.VOTES]
     * [SortEnum.POPULARITY]
     * [SortEnum.LAST_UPDATED]
     * [SortEnum.FIRST_SUBMITTED]
     * [SortEnum.PACKAGE_NAME]
     *
     * default to [SortEnum.PACKAGE_NAME]
     */
    private fun getSort(): String {
        binding.apply {
            return when (fragmentSearchSortLayout.searchSortChipGroup.checkedChipId) {
                R.id.sort_chip_package_name -> SortEnum.PACKAGE_NAME.toString()
                R.id.sort_chip_votes -> SortEnum.VOTES.toString()
                R.id.sort_chip_popularity -> SortEnum.POPULARITY.toString()
                R.id.sort_chip_last_updated -> SortEnum.LAST_UPDATED.toString()
                R.id.sort_chip_first_submitted -> SortEnum.FIRST_SUBMITTED.toString()
                else -> SortEnum.PACKAGE_NAME.toString()
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