package by.dashkevichpavel.currencyrates.features.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.dashkevichpavel.currencyrates.CurrencyRatesViewModelFactory
import by.dashkevichpavel.currencyrates.R
import by.dashkevichpavel.currencyrates.databinding.FragmentSettingsBinding
import by.dashkevichpavel.currencyrates.utils.extensions.setupToolbar
import com.google.android.material.snackbar.Snackbar

class FragmentSettings :
    Fragment(R.layout.fragment_settings),
    CurrencySettingsItemActionListener {
    private val viewModel: SettingsViewModel by viewModels(
        factoryProducer = { CurrencyRatesViewModelFactory(requireContext().applicationContext) }
    )
    private var fragmentBinding: FragmentSettingsBinding? = null
    private val binding get() = fragmentBinding!!
    private val adapter = CurrencySettingsItemAdapter(this)
    private var itemTouchHelper: ItemTouchHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews(view)
        setupObservers()
        viewModel.loadSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.settings_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            R.id.mi_apply -> viewModel.saveSettings()
            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }

    private fun setupViews(view: View) {
        fragmentBinding = FragmentSettingsBinding.bind(view)
        setupToolbar(binding.tbActions)

        binding.rvCurrencies.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCurrencies.adapter = adapter
        val callback: ItemTouchHelper.Callback = ReorderCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper?.attachToRecyclerView(binding.rvCurrencies)
    }

    private fun setupObservers() {
        viewModel.currencySettingsItems.observe(viewLifecycleOwner, adapter::setItems)
        viewModel.state.observe(viewLifecycleOwner, this::onChangeUiState)
    }

    private fun onChangeUiState(state: SettingsUiState) {
        when (state) {
            SettingsUiState.Start, SettingsUiState.Ready -> updateUI(showProgressBar = false)
            SettingsUiState.Loading, SettingsUiState.Saving -> updateUI(showProgressBar = true)
            SettingsUiState.SavingComplete -> {
                updateUI(showProgressBar = false)
                Snackbar
                    .make(
                        requireView(),
                        getString(R.string.screen_settings_saving_complete),
                        Snackbar.LENGTH_SHORT
                    )
                    .show()
                viewModel.notificationHasShown()
            }
        }
    }

    private fun updateUI(showProgressBar: Boolean) {
        binding.pbProgress.isVisible = showProgressBar
    }

    override fun onCurrencySettingsItemAction(action: CurrencySettingsItemAction) {
        when (action) {
            is CurrencySettingsItemAction.ChangeShowFlag ->
                viewModel.changeItemShowFlag(action.currencyId, action.isChecked)
            is CurrencySettingsItemAction.StartDrag ->
                itemTouchHelper?.startDrag(action.viewHolder)
            is CurrencySettingsItemAction.MoveItem ->
                viewModel.moveItem(action.fromPosition, action.toPosition)
        }
    }
}