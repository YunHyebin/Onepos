package com.scspro.onepos.model

import com.scspro.onepos.data.entity.OptionItem

data class Option(
    val id: Int = 0,
    val name: String,
    val isRequired: Boolean,
    val optionItems: List<OptionItem>
)
