package com.android.anifind.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ArrayRes
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.anifind.R
import com.android.anifind.databinding.FragmentFilterBinding
import com.android.anifind.domain.QueryMap
import com.android.anifind.domain.model.Genre
import com.android.anifind.extensions.*
import com.android.anifind.presentation.adapter.AnimePagingAdapter
import com.android.anifind.presentation.viewmodel.OverviewViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class FilterFragment : Fragment() {

    private lateinit var sortItems: List<String?>
    private lateinit var sortAdapter: ArrayAdapter<String>
    private lateinit var kindItems: List<String?>
    private lateinit var kindAdapter: ArrayAdapter<String>
    private lateinit var statusItems: List<String?>
    private lateinit var statusAdapter: ArrayAdapter<String>
    private lateinit var seasonItems: List<String?>
    private lateinit var seasonAdapter: ArrayAdapter<String>
    private lateinit var yearItems: List<String?>
    private lateinit var yearAdapter: ArrayAdapter<String>
    private lateinit var genres: List<Genre>
    private lateinit var genreBooleans: BooleanArray

    private val animeAdapter = AnimePagingAdapter()
    private val viewModel by activityViewModels<OverviewViewModel>()
    private lateinit var binding: FragmentFilterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initFields()
        initClicks()
    }

    private fun initObservers() {
        viewModel.genres.observe(viewLifecycleOwner, { genres = it })
        viewModel.genreBooleans.observe(viewLifecycleOwner, { array ->
            genreBooleans = array.copyOf()
            binding.textFieldGenres.setText(getGenreText())
        })
        viewModel.filterAnimes.observe(viewLifecycleOwner, {
            if (it == null) {
                Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show()
            }
            it?.subscribe { data ->
                animeAdapter.submitData(lifecycle, data)
                binding.recycler.smoothScrollToPosition(0)
            }
        })
        viewModel.isFilterChanging.observe(viewLifecycleOwner, {
            binding.filterForm.isVisible = it!!
            binding.filterList.isVisible = !it
        })
    }

    private fun initFields() {
        val years = getYearList()

        sortItems = getList(R.array.sort_data_items)
        kindItems = getList(R.array.kind_data_items)
        statusItems = getList(R.array.status_data_items)
        seasonItems = getList(R.array.season_data_items)
        yearItems = listOf(null) + years

        sortAdapter = createAdapter(R.array.sort_ui_items)
        kindAdapter = createAdapter(R.array.kind_ui_items)
        statusAdapter = createAdapter(R.array.status_ui_items)
        seasonAdapter = createAdapter(R.array.season_ui_items)
        yearAdapter = createAdapter(listOf("Не важно") + years)

        binding.textFieldSort.setAdapter(sortAdapter)
        binding.textFieldKind.setAdapter(kindAdapter)
        binding.textFieldStatus.setAdapter(statusAdapter)
        binding.textFieldYear.setAdapter(yearAdapter)
        binding.textFieldYear.editText?.addTextChangedListener {
            binding.textFieldSeason.isEnabled = it.toString() != "Не важно"
            if (it.toString() == "Не важно") binding.textFieldSeason.clear(seasonAdapter)
        }
        binding.textFieldSeason.setAdapter(seasonAdapter)

        binding.recycler.init(animeAdapter)
    }

    private fun initClicks() {
        binding.apply {
            textFieldGenres.editText?.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Жанры")
                    .setMultiChoiceItems(
                        genres.map { it.russian }.toTypedArray(), genreBooleans
                    ) { _, _, _ -> }
                    .setPositiveButton("Ок") { _, _ -> viewModel.saveGenres(genreBooleans) }
                    .setNegativeButton("Отмена") { _, _ -> viewModel.restoreGenres() }
                    .setNeutralButton("Очистить") { _, _ -> viewModel.clearGenres() }
                    .show()
            }
            btnBack.setOnClickListener { navigateUp() }
            btnClear.setOnClickListener { clearFilters() }
            btnApply.setOnClickListener { viewModel.requestFilterAnimes(getQueryMap()) }
            btnFilter.setOnClickListener { viewModel.setFilterChanging() }
            btnRandom.setOnClickListener { viewModel.requestFilterAnimes(getQueryMapWithRandom()) }
        }
    }

    private fun clearFilters() {
        binding.apply {
            textFieldSort.clear(sortAdapter)
            textFieldKind.clear(kindAdapter)
            textFieldStatus.clear(statusAdapter)
            textFieldSeason.clear(seasonAdapter)
            textFieldYear.clear(yearAdapter)
            viewModel.clearGenres()
        }
    }

    private fun getQueryMapBuilder() = QueryMap.Builder()
        .order(binding.textFieldSort.getParam(sortAdapter, sortItems))
        .kind(binding.textFieldKind.getParam(kindAdapter, kindItems))
        .status(binding.textFieldStatus.getParam(statusAdapter, statusItems))
        .season(binding.textFieldSeason.getParam(seasonAdapter, seasonItems))
        .year(binding.textFieldYear.getParam(yearAdapter, yearItems))
        .genre(getGenreParam())
        .build()

    private fun getGenreParam(): String? {
        val param = genres.indices.filter { genreBooleans[it] }
            .joinToString(";") { genres[it].id.toString() }
        return if (param.isEmpty()) null else param
    }

    private fun getGenreText(): String {
        val text = genres.indices.filter { genreBooleans[it] }
            .joinToString(", ") { genres[it].russian }
        return if (text.isEmpty()) { "Не важно" } else { text }
    }

    private fun getQueryMap() = getQueryMapBuilder().getHashMap()

    private fun getQueryMapWithRandom() = getQueryMapBuilder().getHashMapWithRandom()

    private fun createAdapter(@ArrayRes id: Int): ArrayAdapter<String> {
        return ArrayAdapter(requireContext(), R.layout.item_list, resources.getStringArray(id))
    }

    private fun createAdapter(objects: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(requireContext(), R.layout.item_list, objects)
    }

    private fun getList(@ArrayRes id: Int): List<String?> {
        return listOf(null) + resources.getStringArray(id).toList()
    }
}
