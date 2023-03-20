package com.techno_3_team.task_manager.structures

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListOfLists(
    var list: ArrayList<ListOfTasks>,
) : Parcelable