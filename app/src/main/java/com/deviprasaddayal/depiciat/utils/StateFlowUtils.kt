package com.deviprasaddayal.depiciat.utils

import com.deviprasaddayal.depiciat.constants.StateFlow
import com.deviprasaddayal.depiciat.models.StateFlowModel

object StateFlowUtils {
    val TAG = StateFlowUtils::class.java.canonicalName

    fun getStateList(stateTitles: Array<String>): ArrayList<StateFlowModel> {
        var stateFlowModels: ArrayList<StateFlowModel> = ArrayList()

        for (position in stateTitles.indices) {
            stateFlowModels.add(
                    StateFlowModel(
                            stateTitle = stateTitles[position],
                            statePosition = position + 1,
                            currentState = StateFlow.STATE_NEXT))
        }
        return stateFlowModels
    }
}