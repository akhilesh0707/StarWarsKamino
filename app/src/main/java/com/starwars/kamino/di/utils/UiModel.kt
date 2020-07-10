package com.starwars.kamino.di.utils

interface UiModel

open class  TransientAwareUiModel: UiModel{
    var isRedelivered: Boolean = false
}