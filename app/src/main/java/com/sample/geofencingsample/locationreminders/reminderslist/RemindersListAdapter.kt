package com.sample.geofencingsample.locationreminders.reminderslist

import com.sample.geofencingsample.R
import com.sample.geofencingsample.base.BaseRecyclerViewAdapter


//Use data binding to show the reminder on the item
class RemindersListAdapter(callBack: (selectedReminder: ReminderDataItem) -> Unit) :
    BaseRecyclerViewAdapter<ReminderDataItem>(callBack) {
    override fun getLayoutRes(viewType: Int) = R.layout.it_reminder
}