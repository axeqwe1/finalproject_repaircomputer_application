package com.example.repaircomputerapplication_finalproject.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.model.RepairStatus
import com.example.repaircomputerapplication_finalproject.repository.RepairRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RepairViewModel(private val repository: RepairRepository) : ViewModel() {

    private val _combinedStatus = MutableStateFlow<List<RepairStatus>>(emptyList())
    val combinedStatus: StateFlow<List<RepairStatus>> = _combinedStatus.asStateFlow()

    init {
        fetchCombinedStatus()
    }

    private fun fetchCombinedStatus() {
        viewModelScope.launch {
            val requestForRepair = repository.fetchRequestForRepair()
            val repairDetails = repository.fetchRepairDetails()
            val receiveRepair = repository.fetchReceiveRepair()

            _combinedStatus.value = repository.getCombinedStatus(requestForRepair, repairDetails, receiveRepair)
        }
    }
}
