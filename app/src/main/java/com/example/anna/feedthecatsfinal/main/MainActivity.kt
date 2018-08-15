package com.example.anna.feedthecatsfinal.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.example.anna.feedthecatsfinal.CatsFeedingEvent
import com.example.anna.feedthecatsfinal.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    private var events: HashMap<String, CatsFeedingEvent> = HashMap()

    private lateinit var dbRef : DatabaseReference

    private lateinit var eventsRecyclerView: RecyclerView
    private var eventAdadapter:EventsAdapter = EventsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupFirebaseDb()
        addDummyEvent()
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

    fun addDummyEvent() {
        val key = dbRef.push().key
        val event = CatsFeedingEvent().apply {
            date = 123
            state = 0
            type= 0
        }
        dbRef.updateChildren(mapOf(Pair(key, event)))
    }

    companion object {
        private const val TABLE = "event"
    }
}
