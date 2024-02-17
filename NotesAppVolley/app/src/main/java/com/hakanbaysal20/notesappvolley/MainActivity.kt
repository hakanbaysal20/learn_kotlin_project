package com.hakanbaysal20.notesappvolley

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.hakanbaysal20.notesappvolley.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var noteList:ArrayList<Note>
    private lateinit var adapter:CardViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //rv
        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this@MainActivity)
        getNotes()

        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity,AddNotesActivity::class.java)
            startActivity(intent)
        }

    }
    fun getNotes(){
        noteList = ArrayList<Note>()
        val baseUrl = "http://kasimadalan.pe.hu/notlar/tum_notlar.php"
        val request = StringRequest(Request.Method.GET,baseUrl,Response.Listener{response ->

            try {
                val jsonObj = JSONObject(response)
                val notes = jsonObj.getJSONArray("notlar")
                for (i in 0 until notes.length()){
                    val responseNote = notes.getJSONObject(i)
                    val note = Note(responseNote.getInt("not_id"),responseNote.getString("ders_adi"),responseNote.getInt("not1"),responseNote.getInt("not2"))
                    noteList.add(note)
                }
                adapter = CardViewAdapter(this,noteList)
                binding.rv.adapter = adapter
            }catch (e:JSONException){
                e.printStackTrace()
            }

        },Response.ErrorListener {

        })
        Volley.newRequestQueue(this).add(request)
    }
}