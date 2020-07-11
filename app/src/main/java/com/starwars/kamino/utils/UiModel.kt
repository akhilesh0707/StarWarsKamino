package com.starwars.kamino.utils

interface UiModel

open class  TransientAwareUiModel: UiModel {
    var isRedelivered: Boolean = false
}