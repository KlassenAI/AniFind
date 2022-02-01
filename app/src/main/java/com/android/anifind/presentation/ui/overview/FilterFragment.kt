package com.android.anifind.presentation.ui.overview

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.android.anifind.Constants
import com.android.anifind.R
import com.android.anifind.databinding.FragmentFilterBinding
import com.android.anifind.domain.model.Genre
import com.android.anifind.domain.model.QueryMap
import com.android.anifind.domain.model.Studio
import com.android.anifind.extensions.*
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.ui.dialog.MultiChoiceDialog
import com.android.anifind.presentation.ui.dialog.MultiChoiceDialog.Type.GENRE
import com.android.anifind.presentation.ui.dialog.MultiChoiceDialog.Type.STUDIO
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.google.android.material.textfield.TextInputLayout

class FilterFragment : BaseOverviewFragment(R.layout.fragment_filter), MultiChoiceDialog.Listener {

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
    private val binding: FragmentFilterBinding by viewBinding()
    private val overviewViewModel: OverviewViewModel by activityViewModels()
    private lateinit var adapter: AnimePagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AnimePagingAdapter(overviewViewModel, this)
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

    private fun initFields() = with(binding) {
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
        recycler.init(adapter, progressBar, errorMessage, emptyMessage)
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

    private fun initClicks() = with(binding) {
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

    private fun showMultiChoiceDialog(
        type: MultiChoiceDialog.Type, title: String, items: Array<String>, booleans: BooleanArray
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

    private fun clearFilters() = with(binding) {
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
        private val type: MultiChoiceDialog.Type,
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

    override fun positiveAction(type: MultiChoiceDialog.Type, booleans: BooleanArray) {
        when (type) {
            GENRE -> overviewViewModel.saveGenres(genreField.booleans!!)
            STUDIO -> overviewViewModel.saveStudios(studioField.booleans!!)
        }
    }

    override fun negativeAction(type: MultiChoiceDialog.Type) {
        when (type) {
            GENRE -> overviewViewModel.restoreGenres()
            STUDIO -> overviewViewModel.restoreStudios()
        }
    }

    override fun neutralAction(type: MultiChoiceDialog.Type) {
        when (type) {
            GENRE -> overviewViewModel.clearGenres()
            STUDIO -> overviewViewModel.clearStudios()
        }
    }
}
