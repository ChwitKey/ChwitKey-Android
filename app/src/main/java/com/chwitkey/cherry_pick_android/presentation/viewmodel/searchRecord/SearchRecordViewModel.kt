package com.chwitkey.cherry_pick_android.presentation.viewmodel.searchRecord

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chwitkey.cherry_pick_android.data.model.SearchRecordEntity
import com.chwitkey.cherry_pick_android.domain.usecase.AddRecordUseCase
import com.chwitkey.cherry_pick_android.domain.usecase.DeleteRecordUseCase
import com.chwitkey.cherry_pick_android.domain.usecase.ReadRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRecordViewModel @Inject constructor(
    private val addRecordUseCase: AddRecordUseCase,
    private val deleteRecordUseCase: DeleteRecordUseCase,
    readRecordUseCase: ReadRecordUseCase
): ViewModel() {
    private val recordList: LiveData<List<SearchRecordEntity>> = readRecordUseCase.invoke()

    fun addRecord(record: String) {
        viewModelScope.launch {
            addRecordUseCase.invoke(record)
        }
    }

    fun deleteRecord(record: String) {
        viewModelScope.launch {
            deleteRecordUseCase.invoke(record)
        }
    }

    fun loadRecord(): LiveData<List<SearchRecordEntity>> {
        return recordList
    }
}