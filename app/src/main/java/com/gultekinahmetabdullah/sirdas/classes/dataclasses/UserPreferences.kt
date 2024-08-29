package com.gultekinahmetabdullah.sirdas.classes.dataclasses

import com.gultekinahmetabdullah.sirdas.classes.enums.Languages

data class UserPreferences(
    val userId: String,
    val themeDark: Boolean,
    val language: Languages,
    val notificationEnabled: Boolean,
    val soundEnabled: Boolean,
    val vibrationEnabled: Boolean,
) {
    constructor() : this(
        "", false, Languages.ENGLISH, true,
        true, true
    ) {
        // Default constructor required for calls to DataSnapshot.getValue(UserPreferences.class)
    }

    constructor(
        userId: String,
        isThemeDark: Boolean,
        language: String,
        isNotificationEnabled: Boolean,
        isSoundEnabled: Boolean,
        isVibrationEnabled: Boolean
    ) : this(
        userId,
        isThemeDark,
        Languages.valueOf(language),
        isNotificationEnabled,
        isSoundEnabled,
        isVibrationEnabled
    )
}
