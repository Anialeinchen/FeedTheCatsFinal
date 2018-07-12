package com.example.anna.feedthecatsfinal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    private var events: HashMap<String, CatsFeedingEvent> = HashMap()

    private lateinit var dbRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Write a message to the database
        dbRef = FirebaseDatabase.getInstance().getReference(TABLE)

        // Read from the database
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                events.clear()
                for (child in dataSnapshot.children) {
                    child.getValue(CatsFeedingEvent::class.java)?.let {
                        events.put(child.key, it)
                    }
                }
                Toast.makeText(this@MainActivity, "Got ${events.size} events", Toast.LENGTH_LONG).show()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Toast.makeText(this@MainActivity, "Got an error: $error", Toast.LENGTH_LONG).show()
            }
        })

        addDummyEvent()
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
