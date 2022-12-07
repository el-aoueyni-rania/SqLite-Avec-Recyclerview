package com.example.sqlitewithrecyclerview
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.SQLException
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlitewithrecyclerview.DBContract.UserEntry.Companion.COLUMN_USER_ID
import com.example.sqlitewithrecyclerview.DBContract.UserEntry.Companion.TABLE_NAME
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() , MyAdapter.OnItemClickListener{
    lateinit var lisetusers: MutableList<UserModel>
    lateinit var userDbhelper: UserDbHelper
    lateinit var nom: EditText
    lateinit var date: EditText
    lateinit var email: EditText
    lateinit var classe: EditText
    lateinit var submit: Button
    lateinit var mRecyclerView : RecyclerView
    lateinit var myAdapter: MyAdapter
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userDbhelper = UserDbHelper(this)
        nom = findViewById(R.id.nom)
        date = findViewById(R.id.date)
        email = findViewById(R.id.email)
        classe = findViewById(R.id.classe)
        submit = findViewById(R.id.submit)
        mRecyclerView = findViewById(R.id.recyclerview)
        lisetusers = showallusers()

        mRecyclerView.layoutManager = GridLayoutManager(this, 1)

        myAdapter = MyAdapter(lisetusers,this)
        mRecyclerView.adapter = myAdapter

        fab = findViewById(R.id.fab)

        fab.setOnClickListener{
            val sharedPreferences : SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            val position : Int = sharedPreferences.getInt("pos", 0)
            deleteItem(position)
        }
    }

    fun addUser(view: View) {
        val nom = nom.text.toString()
        val date = date.text.toString()
        val email = email.text.toString()
        val classe = classe.text.toString()

        val db = userDbhelper.db
        val values = ContentValues()
        values.put(UserDbHelper.COLUMN_NAME, nom)
        values.put(UserDbHelper.COLUMN_DATE, date)
        values.put(UserDbHelper.COLUMN_classe, classe)
        values.put(UserDbHelper.COLUMN_email, email)
        val newrow = db.insert(UserDbHelper.TABLE_NAME, null, values)
        Toast.makeText(this, newrow.toString(), Toast.LENGTH_SHORT).show()
        lisetusers.clear()
        lisetusers.addAll(showallusers())
        myAdapter.notifyDataSetChanged();

    }

    @SuppressLint("Range")
    fun showallusers(): MutableList<UserModel> {
        val users = ArrayList<UserModel>()
        // val users = ArrayList<String>()
        val db = userDbhelper.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + UserDbHelper.TABLE_NAME, null)


            if (cursor?.moveToFirst() == true) {
                while (cursor.isAfterLast == false) {


                    var nom = cursor.getString(cursor.getColumnIndex(UserDbHelper.COLUMN_NAME))
                    var email = cursor.getString(cursor.getColumnIndex(UserDbHelper.COLUMN_email))
                    var classe = cursor.getString(cursor.getColumnIndex(UserDbHelper.COLUMN_classe))
                    var date = cursor.getString(cursor.getColumnIndex(UserDbHelper.COLUMN_DATE))
                    var user = UserModel(nom, classe, date, email)
                    users.add(user)
                    cursor.moveToNext()


                }
            }
        }catch (e:SQLException){

        }

        return users;

    }
    fun deleteItem(pos: Int){
        val db = userDbhelper.readableDatabase
        db.delete(UserDbHelper.TABLE_NAME, "id="+ pos , null )
        db.close()
        Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show()
        lisetusers.clear()
        lisetusers.addAll(showallusers())
        myAdapter.notifyDataSetChanged();
    }



    override fun OnItemClick(position: Int) {
        val pos = position
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs" , Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.apply {
            putInt("pos" , pos)
        }.apply()
        Toast.makeText(this, "Item " + position + " clicked" , Toast.LENGTH_LONG).show()
    }

}



