package com.example.sqlitewithrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MyAdapter(private val listData: MutableList<UserModel>, private val listener: OnItemClickListener):
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private var ps : Int = 0
   // private var listData: MutableList<UserModel> = list as MutableList<UserModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usermodel, parent, false)
        val holder = ViewHolder(view)
        return holder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user : UserModel = listData[position]
        holder.bind(user)
      /*  val nom : String = listData[position]
        holder.bind(nom) */

    }


    override fun getItemCount(): Int {
        return listData.size
    }



     inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView),
         View.OnClickListener {
         private var nom: TextView = itemView.findViewById(R.id.nom1)
         private var classe: TextView = itemView.findViewById(R.id.classe1)
         private var date_naissance: TextView = itemView.findViewById(R.id.date1)
         private var email: TextView = itemView.findViewById(R.id.email1)

         init {
             ItemView.setOnClickListener(this)
         }

         fun bind(userModel: UserModel){
             nom.text = userModel.nom
             classe.text = userModel.classe
             date_naissance.text = userModel.date_naissance
             email.text = userModel.email
         }


         override fun onClick(p0: View?) {
             ps = adapterPosition
             if (ps !=RecyclerView.NO_POSITION){
                 listener.OnItemClick(ps)
             }
         }

     }



    interface OnItemClickListener {
        fun OnItemClick(position: Int)
    }

}

