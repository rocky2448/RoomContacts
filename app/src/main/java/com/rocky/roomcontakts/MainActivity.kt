package com.rocky.roomcontakts

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var toolbarMain: Toolbar
    private var db: PersonDatabase? = null
    private lateinit var nameET: EditText
    private lateinit var phoneET: EditText
    private lateinit var saveBTN: Button
    private lateinit var contactTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Тулбар
        toolbarMain = findViewById(R.id.toolbarMain)
        setSupportActionBar(toolbarMain)
        title = "Контакты"
        toolbarMain.subtitle = "by Rocky"
        toolbarMain.setLogo(R.drawable.ic_storage)

        nameET = findViewById(R.id.nameET)
        phoneET = findViewById(R.id.phoneET)
        contactTV = findViewById(R.id.contactTV)
        saveBTN = findViewById(R.id.saveBTN)

        //БД
        db = PersonDatabase.getDatabase(context = this)
        readDataBase(db!!)
    }

    override fun onResume() {
        super.onResume()

        saveBTN.setOnClickListener {
            setUserData()
        }
    }

    private fun setUserData() {
        val name = nameET.text.toString()
        val fon = phoneET.text.toString()
        if (name.isNotBlank() && fon.isNotBlank()) {
            val person = Person(name = name, phone = fon)

            db?.let { database ->
                // Используем lifecycleScope для асинхронных операций
                lifecycleScope.launch (Dispatchers.IO) {
                    addPerson(database, person)
                    readDataBase(database)
                    //очищаем поля ввода
                    nameET.text.clear()
                    phoneET.text.clear()
                }
            }
        } else {
            Toast.makeText(this, "Введите имя и телефон!", Toast.LENGTH_SHORT).show()
        }
    }

    // Добавление персоны в базу данных
    private suspend fun addPerson(db: PersonDatabase, person: Person) {
        // Переносим операцию в фоновый поток с помощью launch с Dispatchers.IO
        db.getPersonDao().insert(person)
    }

    // Чтение данных из базы данных и обновление UI
    private fun readDataBase(db: PersonDatabase) {
        lifecycleScope.launch(Dispatchers.IO) {
            val list = db.getPersonDao().getAllPerson() ?: emptyList()

            // Обновление UI происходит на главном потоке
            withContext(Dispatchers.Main) {
                contactTV.text = ""
                list.forEach { i ->
                    contactTV.append("${i.name}  -  ${i.phone}\n")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exitMenuMain -> {
                finishAffinity()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}