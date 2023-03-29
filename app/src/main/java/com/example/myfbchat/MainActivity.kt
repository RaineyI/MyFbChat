package com.example.myfbchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myfbchat.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database
        val myRef = database.getReference("message")
        binding.bSend.setOnClickListener{
            myRef.setValue(binding.editMessage.text.toString())
        }
        //выбираем на каком пути будем прослушивать
        onChangeListener(myRef)

    }
    private fun onChangeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               //изменения происходят здесь
                binding.apply {
                    tvMessages.append("\n")
                    tvMessages.append("Собеседник 2: ${snapshot.value.toString()}")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}