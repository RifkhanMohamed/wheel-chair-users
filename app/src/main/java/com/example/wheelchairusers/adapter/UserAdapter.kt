package com.example.wheelchairusers.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wheelchairusers.R
import com.example.wheelchairusers.model.Help
import com.example.wheelchairusers.model.Users

class UserAdapter : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    private val userList = ArrayList<Users>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_item,
            parent,false
        )
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = userList[position]

        holder.itemView.setOnClickListener {
            val phoneNumber = currentItem.phone
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            holder.itemView.context.startActivity(intent)
        }

        holder.name.text = currentItem.name
        holder.studentID.text = currentItem.sid
        holder.phone.text = currentItem.phone

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateUserList(userList : List<Users>){

        this.userList.clear()
        this.userList.addAll(userList)
        notifyDataSetChanged()

    }

    class  MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.list_item_name)
        val studentID : TextView = itemView.findViewById(R.id.list_item_student_id)
        val phone : TextView = itemView.findViewById(R.id.list_item_phone)

    }

}