package tech.yeswecode.week11.adapters

import tech.yeswecode.week11.models.User

interface OnUserSelected {
    fun onUserSelected(selected: User)
}