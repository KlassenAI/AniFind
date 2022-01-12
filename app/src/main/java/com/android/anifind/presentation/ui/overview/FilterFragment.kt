package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ArrayRes
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.anifind.Constants
import com.android.anifind.R
import com.android.anifind.databinding.FragmentFilterBinding
import com.android.anifind.domain.model.QueryMap
import com.android.anifind.domain.model.Genre
import com.android.anifind.domain.model.MultiChoiceDialogType
import com.android.anifind.domain.model.MultiChoiceDialogType.GENRE
import com.android.anifind.domain.model.MultiChoiceDialogType.STUDIO
import com.android.anifind.domain.model.Studio
import com.android.anifind.extensions.*
import com.android.anifind.presentation.adapter.AdapterType.DEFAULT
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.ui.dialog.MultiChoiceDialog
import com.android.anifind.presentation.viewmodel.AnimeViewModel
import com.android.anifind.presentation.viewmodel.BookmarksViewModel
import com.android.anifind.presentation.viewmodel.HomeViewModel
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.google.android.material.textfield.TextInputLayout

class FilterFragment : Fragment(), MultiChoiceDialog.MultiChoiceDialogListener {

    companion object {
        const val GENRES = "Жанры"
        const val STUDIOS = "Студии"
    }

    private lateinit var orderField: Field
    private lateinit var kindField: Field
    private lateinit var typeField: Field
    private lateinit var statusField: Field
    private lateinit var seasonField: Field
    private lateinit var scoreField: Field
    private lateinit var durationField: Field
    private lateinit var ratingField: Field
    private lateinit var genreField: MultiChoiceField<Genre>
    private lateinit var studioField: MultiChoiceField<Studio>
    private val adapter = AnimePagingAdapter(DEFAULT)
    private val animeViewModel: AnimeViewModel by activityViewModels()
    private val overviewViewModel: OverviewViewModel by activityViewModels()
    private lateinit var binding: FragmentFilterBinding

    override fun onCreateView(inflater: LayoutInflater, c: ViewGroup?, b: Bundle?): View {
        binding = FragmentFilterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
        initObservers()
        initClicks()
    }

    private fun initObservers() {
        overviewViewModel.genres.observe(viewLifecycleOwner, { genreField.list = it })
        overviewViewModel.genreBooleans.observe(viewLifecycleOwner,
            { genreField.booleans = it.copyOf() })
        overviewViewModel.studios.observe(viewLifecycleOwner, { studioField.list = it })
        overviewViewModel.studiosBooleans.observe(
            viewLifecycleOwner, { studioField.booleans = it.copyOf() })
        overviewViewModel.filterAnimes.observe(viewLifecycleOwner, {
            adapter.submitData(lifecycle, it)
        })
        overviewViewModel.isFilterChanging.observe(viewLifecycleOwner, {
            binding.layoutFilter.isVisible = it!!
            binding.layoutList.isVisible = !it
        })
    }

    private fun initFields() {
        binding.run {
            orderField = getField(R.array.order_params, R.array.order_items, fieldOrder)
            kindField = getField(R.array.kind_params, R.array.kind_items, fieldKind)
            typeField = getField(R.array.type_params, R.array.type_items, fieldType)
            statusField = getField(R.array.status_params, R.array.status_items, fieldStatus)
            seasonField = getField(R.array.season_params, R.array.season_items, fieldSeason)
            scoreField = getField(R.array.score_params, R.array.score_items, fieldScore)
            durationField = getField(R.array.duration_params, R.array.duration_items, fieldDuration)
            ratingField = getField(R.array.rating_params, R.array.rating_items, fieldRating)
            genreField = MultiChoiceField(fieldGenre, GENRE)
            studioField = MultiChoiceField(fieldStudio, STUDIO)
            recycler.init(adapter, animeViewModel, progressBar, errorMessage, emptyMessage)
        }
    }

    private fun getField(
        @ArrayRes paramsId: Int, @ArrayRes adapterId: Int, field: TextInputLayout
    ): Field {
        return Field(
            listOf(null) + resources.getStringArray(paramsId).toList(),
            ArrayAdapter(requireContext(), R.layout.item_list, resources.getStringArray(adapterId)),
            field
        )
    }

    private fun initClicks() {
        binding.apply {
            fieldGenre.editText?.setOnClickListener {
                showMultiChoiceDialog(GENRE, GENRES, genreField.items, genreField.booleans!!)
            }
            fieldStudio.editText?.setOnClickListener {
                showMultiChoiceDialog(STUDIO, STUDIOS, studioField.items, studioField.booleans!!)
            }
            btnBack.setOnClickListener { navigateUp() }
            btnBack2.setOnClickListener { navigateUp() }
            btnClear.setOnClickListener { clearFilters() }
            btnRetry.setOnClickListener { adapter.retry() }
            btnApply.setOnClickListener { overviewViewModel.requestFilterAnimes(getQueryMap()) }
            btnFilter.setOnClickListener { overviewViewModel.setFilterChanging() }
            btnRandom.setOnClickListener {
                overviewViewModel.requestFilterAnimes(getQueryMapWithRandom())
            }
        }
    }

    private fun showMultiChoiceDialog(
        type: MultiChoiceDialogType, title: String, items: Array<String>, booleans: BooleanArray
    ) {
        MultiChoiceDialog().apply {
            arguments = bundleOf(
                Constants.KEY_TYPE to type,
                Constants.KEY_TITLE to title,
                Constants.KEY_STRING_ARRAY to items,
                Constants.KEY_BOOLEAN_ARRAY to booleans
            )
        }.show(childFragmentManager, "")
    }

    private fun clearFilters() {
        binding.apply {
            fieldOrder.clear()
            fieldKind.clear()
            fieldType.clear()
            fieldStatus.clear()
            fieldSeason.clear()
            fieldScore.clear()
            fieldDuration.clear()
            fieldRating.clear()
            overviewViewModel.clearGenres()
            overviewViewModel.clearStudios()
        }
    }

    private fun getQueryMap() = getQueryMapBuilder().getHashMap()

    private fun getQueryMapWithRandom() = getQueryMapBuilder().getHashMapWithRandom()

    private fun getQueryMapBuilder() = QueryMap.Builder().order(orderField.param)
        .kind(kindField.param).type(typeField.param).status(statusField.param)
        .season(seasonField.param).score(scoreField.param).duration(durationField.param)
        .rating(ratingField.param).genre(genreField.param).studio(studioField.param).build()

    class Field(
        private val params: List<String?>,
        private val adapter: ArrayAdapter<String>,
        private val textField: TextInputLayout
    ) {
        val param: String? get() = params[adapter.getPosition(textField.completeView()?.text.toString())]

        init {
            textField.setAdapter(adapter)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class MultiChoiceField<T>(
        private val textField: TextInputLayout,
        private val type: MultiChoiceDialogType,
        var list: List<T>? = null,
    ) {
        val items: Array<String>
            get() {
                return when (type) {
                    GENRE -> (list as List<Genre>).map { it.russian }.toTypedArray()
                    STUDIO -> (list as List<Studio>).map { it.name }.toTypedArray()
                }
            }
        var booleans: BooleanArray? = null
            set(value) {
                field = value
                setFieldText()
            }
        val param: String?
            get() {
                return when (type) {
                    GENRE -> {
                        val list = list as List<Genre>
                        val p = list.indices.filter { booleans!![it] }
                            .joinToString(";") { list[it].id.toString() }
                        if (p.isEmpty()) null else p
                    }
                    STUDIO -> {
                        val list = list as List<Studio>
                        val p = list.indices.filter { booleans!![it] }
                            .joinToString(";") { list[it].id.toString() }
                        if (p.isEmpty()) null else p
                    }
                }
            }

        private fun setFieldText() {
            when (type) {
                GENRE -> {
                    val list = list as List<Genre>
                    val text = list.indices.filter { booleans!![it] }
                        .joinToString(", ") { list[it].russian }
                    textField.setText(if (text.isEmpty()) "Не важно" else text)
                }
                STUDIO -> {
                    val list = list as List<Studio>
                    val text = list.indices.filter { booleans!![it] }
                        .joinToString(", ") { list[it].name }
                    textField.setText(if (text.isEmpty()) "Не важно" else text)
                }
            }

        }
    }

    override fun positiveAction(type: MultiChoiceDialogType, booleans: BooleanArray) {
        when (type) {
            GENRE -> overviewViewModel.saveGenres(genreField.booleans!!)
            STUDIO -> overviewViewModel.saveStudios(studioField.booleans!!)
        }
    }

    override fun negativeAction(type: MultiChoiceDialogType) {
        when (type) {
            GENRE -> overviewViewModel.restoreGenres()
            STUDIO -> overviewViewModel.restoreStudios()
        }
    }

    override fun neutralAction(type: MultiChoiceDialogType) {
        when (type) {
            GENRE -> overviewViewModel.clearGenres()
            STUDIO -> overviewViewModel.clearStudios()
        }
    }
}
