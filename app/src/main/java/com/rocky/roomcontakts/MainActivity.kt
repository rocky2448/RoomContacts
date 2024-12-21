package com.rocky.roomcontakts

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {

    var db: NoteDatabase? = null
    private lateinit var noteET: EditText
    private lateinit var saveBTN: Button
    private lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteET = findViewById(R.id.noteET)
        saveBTN = findViewById(R.id.saveBTN)
        textView = findViewById(R.id.textView)

        db = NoteDatabase.getDatabase(this)
        reafDatabase(db!!)
    }

    override fun onResume() {
        super.onResume()
        saveBTN.setOnClickListener {
            val note = Note(noteET.text.toString(), Date().time.toString())
            addNote(db!!, note)
            reafDatabase(db!!)
        }
    }

    private fun addNote(db: NoteDatabase, note: Note) = GlobalScope.async {
        db.getNoteDao().insert(note)
    }

    private fun reafDatabase(db: NoteDatabase) = GlobalScope.async {
        textView.text = ""
        val list = db.getNoteDao().getAllNotes()
        list.forEach { i -> textView.append(i.text + "\n") }
    }
}