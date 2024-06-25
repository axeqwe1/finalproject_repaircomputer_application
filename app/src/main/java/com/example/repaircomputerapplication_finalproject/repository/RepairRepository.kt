package com.example.repaircomputerapplication_finalproject.repository

import com.example.repaircomputerapplication_finalproject.model.ReceiveRepair
import com.example.repaircomputerapplication_finalproject.model.RepairDetails
import com.example.repaircomputerapplication_finalproject.model.RepairStatus
import com.example.repaircomputerapplication_finalproject.model.RequestForRepair

// data/RepairRepository.kt
class RepairRepository {

    fun getCombinedStatus(
        requestForRepair: List<RequestForRepair>,
        repairDetails: List<RepairDetails>,
        receiveRepair: List<ReceiveRepair>
    ): List<RepairStatus> {
        val combinedStatus = mutableListOf<RepairStatus>()

        // Add request for repair statuses
        combinedStatus.addAll(
            requestForRepair.map {
                RepairStatus(
                    timestamp = it.timestamp,
                    status = it.requestStatus,
                    subStatus = listOf(it.rrDescription)
                )
            }
        )

        // Add repair details statuses
        combinedStatus.addAll(
            repairDetails.map {
                RepairStatus(
                    timestamp = it.timestamp,
                    status = "กำลังดำเนินการ",
                    subStatus = listOf(it.rdDescription)
                )
            }
        )

        // Add receive repair statuses
        combinedStatus.addAll(
            receiveRepair.map {
                RepairStatus(
                    timestamp = it.dateReceive,
                    status = "รับคืนโดย: ช่างที่ ${it.techId}",
                    subStatus = emptyList()
                )
            }
        )

        // Sort by timestamp
        return combinedStatus.sortedBy { it.timestamp }
    }

    // Dummy functions to simulate data fetching
    fun fetchRequestForRepair(): List<RequestForRepair> {
        return listOf(
            RequestForRepair(1, "tet", "20240530_050146.jpg", "กำลังดำเนินการ", "2024-05-30 05:02:40"),
            RequestForRepair(2, "ฟ้าไฟ", "20240606_010526.jpg", "กำลังส่งการแจ้งซ่อม", "2024-06-06 01:05:44")
        )
    }

    fun fetchRepairDetails(): List<RepairDetails> {
        return listOf(
            RepairDetails(1, 1, 1, "ฟ้าไฟ", "2024-06-06 01:29:27"),
            RepairDetails(4, 1, 1, "test From Api", "2024-06-06 02:22:13"),
            RepairDetails(5, 1, 1, "test From Api", "2024-06-06 02:22:44"),
            RepairDetails(6, 1, 1, "สวัสดี", "2024-06-06 02:28:21")
        )
    }

    fun fetchReceiveRepair(): List<ReceiveRepair> {
        return listOf(
            ReceiveRepair(1, 1, "2024-05-30 05:16:31", 1)
        )
    }
}
