package com.simplemobiletools.notes.dialogs

import android.app.Activity
import android.content.DialogInterface.BUTTON_POSITIVE
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.WindowManager
import com.simplemobiletools.commons.extensions.setupDialogStuff
import com.simplemobiletools.commons.extensions.toast
import com.simplemobiletools.commons.extensions.value
import com.simplemobiletools.notes.R
import com.simplemobiletools.notes.helpers.DBHelper
import com.simplemobiletools.notes.models.Note
import kotlinx.android.synthetic.main.new_note.view.*

class RenameNoteDialog(val activity: Activity, val db: DBHelper, val note: Note, callback: (note: Note) -> Unit) {

    init {
        val view = LayoutInflater.from(activity).inflate(R.layout.rename_note, null)
        view.note_name.setText(note.title)

        AlertDialog.Builder(activity)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null)
                .create().apply {
            window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            activity.setupDialogStuff(view, this, R.string.rename_note)
            getButton(BUTTON_POSITIVE).setOnClickListener({
                val title = view.note_name.value
                if (title.isEmpty()) {
                    activity.toast(R.string.no_title)
                } else if (db.doesTitleExist(title)) {
                    activity.toast(R.string.title_taken)
                } else {
                    note.title = title
                    db.updateNote(note)
                    dismiss()
                    callback.invoke(note)
                }
            })
        }
    }
}
