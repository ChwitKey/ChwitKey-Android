package com.chwitkey.cherry_pick_android.presentation.viewmodel.keyword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chwitkey.cherry_pick_android.data.model.KeywordEntity
import com.chwitkey.cherry_pick_android.domain.usecase.DeleteKeywordUseCase
import com.chwitkey.cherry_pick_android.domain.usecase.ReadKeywordUseCase
import com.chwitkey.cherry_pick_android.domain.usecase.WriteKeywordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchKeywordViewModel @Inject constructor(
    private val deleteKeywordUseCase: DeleteKeywordUseCase,
    private val writeKeywordUseCase: WriteKeywordUseCase,
    private val readKeywordUseCase: ReadKeywordUseCase
): ViewModel(){
    private val keywordList: LiveData<List<KeywordEntity>> = readKeywordUseCase.invoke() // 키워드 리스트 받기

    // 키워드 추가
    fun addKeyword(keyword: String){
        viewModelScope.launch {
            writeKeywordUseCase.invoke(keyword)
        }
    }

    // 키워드 삭제
    fun deleteKeyword(keyword: String){
        viewModelScope.launch {
            deleteKeywordUseCase.invoke(keyword)
        }
    }

    // 키워드 리스트 갱신
    fun loadKeyword(): LiveData<List<KeywordEntity>>{
        return keywordList
    }

    // 키워드 중복검사
    fun checkKeyword(keyword: String): Boolean{
        Log.d("SearchKeywordVM", "ckKeyword: $keyword , ${loadKeyword().value?.size}")
        val existingKeywords = loadKeyword().value
        return existingKeywords == null || !existingKeywords.any { it.keyword == keyword }
    }

    // 키워드 개수 검사
    fun checkKeywordCnt(): Boolean{
        return loadKeyword().value?.size?: 0 < 10
    }

}