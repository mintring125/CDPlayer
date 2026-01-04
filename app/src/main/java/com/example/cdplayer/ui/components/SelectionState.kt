package com.example.cdplayer.ui.components

data class SelectionState(
    val isSelectionMode: Boolean = false,
    val selectedIds: Set<Long> = emptySet()
) {
    fun toggle(id: Long): SelectionState {
        val newSelectedIds = if (id in selectedIds) {
            selectedIds - id
        } else {
            selectedIds + id
        }
        
        return copy(
            selectedIds = newSelectedIds,
            isSelectionMode = newSelectedIds.isNotEmpty() || (isSelectionMode && newSelectedIds.isEmpty())
        )
    }

    fun clear(): SelectionState {
        return copy(isSelectionMode = false, selectedIds = emptySet())
    }
}
