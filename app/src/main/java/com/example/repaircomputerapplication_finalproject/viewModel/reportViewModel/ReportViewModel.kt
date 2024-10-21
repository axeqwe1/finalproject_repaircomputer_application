package com.example.repaircomputerapplication_finalproject.viewModel.reportViewModel

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.repaircomputerapplication_finalproject.api_service.RetrofitInstance.apiService
import com.example.repaircomputerapplication_finalproject.model.DashboardModel
import com.example.repaircomputerapplication_finalproject.model.DateForReport
import com.example.repaircomputerapplication_finalproject.model.RepairReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.regex.Pattern

open class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val _dashboardData = MutableStateFlow<DashboardModel?>(null)
    val dashboardData: StateFlow<DashboardModel?> = _dashboardData
    private val _repairData = MutableStateFlow<List<RepairReport?>?>(null)
    val repairData: StateFlow<List<RepairReport?>?> = _repairData
    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _dashboardData.value = fetchDashboardData()

        }
    }

    suspend fun fetchReportData(startDate: String,endDate: String){
        try {
            val response = apiService.getReportData(DateForReport(startDate,endDate))
            if(response.isSuccessful){
                Log.d("fetchReportData", "fetchDashboardData: ${response.body()}")
                _repairData.value = response.body() ?: emptyList()
            }else{
                throw Exception("Fail to Fetch Report Data")
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.e("fetchReportData", "Exception during export CSV: ${e.message}")
            throw Exception("Fail to Fetch Report Data")
        }
    }
    private suspend fun fetchDashboardData(): DashboardModel {
        val response = apiService.getDashboardData()
        if (response.isSuccessful && response.body() != null) {
            Log.d("ReportViewModel", "fetchDashboardData: ${response.body()}")
            return response.body()!!
        } else {
            throw Exception("Fail to Fetch DashboardData")
        }
    }

    fun exportReport(context: Context,startDate:String,endDate:String) {
        viewModelScope.launch {
            downloadCSV(context,startDate,endDate)
        }
    }

    private suspend fun downloadCSV(context: Context,startDate:String,endDate:String) {
        try {
            val response = apiService.exportCSV(DateForReport(startDate,endDate))
            if (response.isSuccessful) {
                Log.d(TAG, "downloadCSV: Success")
                response.body()?.let { responseBody ->
                    val filename = extractFileName(response.headers()["Content-Disposition"])
                    saveCSVToFile(context, responseBody,filename)
                    Log.d(TAG, "downloadCSV: ${responseBody} filename ${filename}")
                }
            } else {
                Log.e("ReportViewModel", "Failed to export CSV")
                // Handle unsuccessful response
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("ReportViewModel", "Exception during export CSV: ${e.message}")
            // Handle the exception
        }
    }
    private fun extractFileName(contentDisposition: String?): String {
        if (contentDisposition != null) {
            val pattern = Pattern.compile("filename=\"?([^\";]*)\"?")
            val matcher = pattern.matcher(contentDisposition)
            if (matcher.find()) {
                return matcher.group(1) ?: "exported_report.csv" // Default filename if extraction fails
            }
        }
        return "exported_report.csv" // Fallback filename if header is missing
    }
    private fun saveCSVToFile(context: Context, body: ResponseBody,fileName: String) {
        val downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsPath, fileName)

        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null

        try {
            inputStream = body.byteStream()
            outputStream = FileOutputStream(file)

            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            outputStream.flush()
            Toast.makeText(context,"Create File Success at DIRECTORY DOWNLOADS",Toast.LENGTH_LONG).show()
            Log.d("ReportViewModel", "CSV file saved successfully")

            // Show a message or notification to inform the user that the file has been saved
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("ReportViewModel", "Error saving CSV file: ${e.message}")
            // Handle the exception
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }
}
