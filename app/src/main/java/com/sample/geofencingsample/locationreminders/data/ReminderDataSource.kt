package com.sample.geofencingsample.locationreminders.data

import com.sample.geofencingsample.locationreminders.data.dto.ReminderDTO
import com.sample.geofencingsample.locationreminders.data.dto.Result

/**
 * Main entry point for accessing reminders data.
 */
interface ReminderDataSource {
    suspend fun getReminders(): Result<List<ReminderDTO>>
    suspend fun saveReminder(reminder: ReminderDTO)
    suspend fun getReminder(id: String): Result<ReminderDTO>
    suspend fun deleteReminder(id: String)
    suspend fun deleteAllReminders()
}