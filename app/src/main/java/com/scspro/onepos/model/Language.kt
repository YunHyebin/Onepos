package com.scspro.onepos.model

enum class Language(val label:String) {
    KOREAN("한"),
    ENGLISH("EN"),
    JAPANESE("日"),
    CHINESE("中");

    fun next(): Language {
        val values = values()
        return values[(this.ordinal + 1) % values.size]
    }
}