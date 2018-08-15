package com.example.anna.feedthecatsfinal.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.anna.feedthecatsfinal.CatsFeedingEvent
import com.example.anna.feedthecatsfinal.R
import com.google.firebase.database.*
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.EditText
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private var events: HashMap<String, CatsFeedingEvent> = HashMap()

    private lateinit var dbRef: DatabaseReference

    private lateinit var eventsRecyclerView: RecyclerView
    private var eventAdadapter: EventsAdapter = EventsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupFirebaseDb()
        addNewEvent(123)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        when (id) {
            R.id.newShift -> {
                showChangeLangDialog()
                return true
            }

        //similarly write for other actions

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setupFirebaseDb() {
        // Write a message to the database
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE)

        // Read from the database
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                refreshEventList(dataSnapshot)
                eventAdadapter.setEvents(events.values.toList())
                eventAdadapter.notifyDataSetChanged()
                Toast.makeText(this@MainActivity, "Got ${events.size} events", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Got an error: $error", Toast.LENGTH_LONG).show()
            }
        })

        eventsRecyclerView = findViewById(R.id.events_rv);
        eventsRecyclerView.adapter = eventAdadapter
        eventsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun refreshEventList(dataSnapshot: DataSnapshot) {
        events.clear()
        for (child in dataSnapshot.children) {
            child.getValue(CatsFeedingEvent::class.java)?.let {
                events.put(child.key ?: "", it)
            }
        }
    }

    fun addNewEvent(date: Long) {
        val key = dbRef.push().key
        val event = CatsFeedingEvent().apply {
            this.date = date
            state = 0
            type = 0
        }
        dbRef.updateChildren(mapOf(Pair(key, event)))
    }

    companion object {
        private const val TABLE = "event"
    }

    fun showChangeLangDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog, null)
        dialogBuilder.setView(dialogView)

        val edt = dialogView.findViewById(R.id.edit1) as EditText

        dialogBuilder.setTitle("Custom dialog")
        dialogBuilder.setMessage("Enter text below")
        dialogBuilder.setPositiveButton("Done", DialogInterface.OnClickListener { dialog, whichButton ->
            //do something with edt.getText().toString();
            val nextShift = edt.text.toString()
            val timestampNextShift = dateFormat.parse(nextShift).time
            addNewEvent(timestampNextShift)
        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, whichButton ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }
}
