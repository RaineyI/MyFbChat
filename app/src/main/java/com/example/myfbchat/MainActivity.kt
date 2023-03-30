package com.example.myfbchat

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.myfbchat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        setUpActionBar()
        val database = Firebase.database
        val myRef = database.getReference("message")
        binding.bSend.setOnClickListener{
            myRef.setValue(binding.editMessage.text.toString())
        }
        //выбираем на каком пути будем прослушивать
        onChangeListener(myRef)

    }

    //меню с выходом из аккаунта
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //слушатель нажатий на меню с кнопкой выхода из аккаунта
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.signOnt){
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onChangeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               //изменения происходят здесь
                binding.apply {
                    tvMessages.append("\n")
                    tvMessages.append("${auth.currentUser?.displayName}: ${snapshot.value.toString()}")
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    //Для добавления изображения в профиль
    private fun setUpActionBar(){
        val actionBar = supportActionBar
        Thread{
            val bMap = Picasso.get().load(auth.currentUser?.photoUrl).get()
            val dIcon = BitmapDrawable(resources, bMap)
            runOnUiThread{
                actionBar?.setDisplayHomeAsUpEnabled(true)
                actionBar?.setHomeAsUpIndicator(dIcon)
                actionBar?.title = auth.currentUser?.displayName
            }

        }.start()

    }
}