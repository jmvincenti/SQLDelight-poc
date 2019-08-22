package com.jmvincenti.sqldelightpoc.repositories

import com.jmvincenti.sqldelightpoc.domain.LocalizedString
import com.squareup.sqldelight.ColumnAdapter

object LocalizedStringAdapter : ColumnAdapter<LocalizedString, String> {
    override fun decode(databaseValue: String): LocalizedString =
        LocalizedString(
            lang = databaseValue.substring(0, 2),
            value = databaseValue.substring(2)
        )

    override fun encode(value: LocalizedString): String =
        "${value.lang}${value.value}"
}
