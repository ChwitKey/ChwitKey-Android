package com.chwitkey.cherry_pick_android.data.mapper

import com.chwitkey.cherry_pick_android.data.model.SearchRecordEntity

object RecordMapper {
    fun mapperToRecordEntity(record: String): SearchRecordEntity {
        return SearchRecordEntity(
            record = record
        )
    }
}