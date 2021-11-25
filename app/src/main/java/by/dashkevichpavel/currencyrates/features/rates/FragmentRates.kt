package by.dashkevichpavel.currencyrates.features.rates

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.dashkevichpavel.currencyrates.CurrencyRatesViewModelFactory
import by.dashkevichpavel.currencyrates.R
import by.dashkevichpavel.currencyrates.databinding.FragmentRatesBinding
import by.dashkevichpavel.currencyrates.utils.extensions.setupToolbar
import by.dashkevichpavel.currencyrates.utils.formatters.FormatUtil
import com.google.android.material.snackbar.Snackbar
import java.util.*

class FragmentRates : Fragment(R.layout.fragment_rates) {
    private val viewModel: RatesViewModel by viewModels(
        factoryProducer = { CurrencyRatesViewModelFactory(requireContext().applicationContext) }
    )
    private var fragmentBinding: FragmentRatesBinding? = null
    private val binding get() = fragmentBinding!!
    private val adapter = RateItemsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews(view)
        setupObservers()
        viewModel.loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rates_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> requireActivity().onBackPressed()
            R.id.mi_currencies_settings -> {}
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

    private fun setupViews(view: View) {
        fragmentBinding = FragmentRatesBinding.bind(view)
        setupToolbar(binding.tbActions)

        binding.rvRates.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRates.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.state.observe(viewLifecycleOwner, this::onChangeState)
        viewModel.rateItems.observe(viewLifecycleOwner, adapter::setItems)
        viewModel.dateFirst.observe(viewLifecycleOwner) { date: Date ->
            binding.tvDateFirst.text = FormatUtil.formatDateAsDayMonthYearString(date)
        }
        viewModel.dateLast.observe(viewLifecycleOwner) { date: Date ->
            binding.tvDateLast.text = FormatUtil.formatDateAsDayMonthYearString(date)
        }
    }

    private fun onChangeState(state: RatesViewModelState) {
        when (state) {
            RatesViewModelState.Start -> updateUi(
                showProgressBar = false,
                showSettingsButton = false
            )
            RatesViewModelState.Loading -> updateUi(
                showProgressBar = true,
                showSettingsButton = false
            )
            RatesViewModelState.LoadingError -> {
                updateUi(showProgressBar = false, showSettingsButton = false)
                Snackbar
                    .make(
                        requireView(),
                        getString(R.string.screen_rates_loading_error),
                        Snackbar.LENGTH_SHORT
                    )
                    .show()
                viewModel.errorHasShown()
            }
            RatesViewModelState.Ready -> updateUi(
                showProgressBar = false,
                showSettingsButton = true
            )
            RatesViewModelState.NotLoaded -> updateUi(
                showProgressBar = false,
                showSettingsButton = false
            )
        }
    }

    private fun updateUi(showProgressBar: Boolean, showSettingsButton: Boolean) {
        binding.tbActions.menu.findItem(R.id.mi_currencies_settings)?.isVisible = showSettingsButton
        binding.pbProgress.isVisible = showProgressBar
    }
}