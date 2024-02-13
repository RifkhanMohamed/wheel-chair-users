package com.example.wheelchairusers.adapter

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.wheelchairusers.HelpDetailsFragment
import com.example.wheelchairusers.LoginActivity
import com.example.wheelchairusers.R
import com.example.wheelchairusers.model.Help
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase

class HelpAdapter : RecyclerView.Adapter<HelpAdapter.MyViewHolder>() {

    private val helpList = ArrayList<Help>()
    private lateinit var navController: NavController

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.help_item,
            parent,false
        )
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = helpList[position]

        holder.name.text = currentItem.user?.name
        holder.studentID.text = currentItem.user?.sid


        holder.itemView.setOnClickListener {
            val phoneNumber = currentItem.user?.phone
            val uid = currentItem.user?.uid
            var database = FirebaseDatabase.getInstance().getReference("help").child(uid.toString())
            database.child("status").setValue("done")

            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            holder.itemView.context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return helpList.size
    }

    fun updateUserList(userList : List<Help>){

        this.helpList.clear()
        this.helpList.addAll(userList)
        notifyDataSetChanged()

    }

    class  MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = itemView.findViewById(R.id.list_item_name)
        val studentID : TextView = itemView.findViewById(R.id.list_item_student_id)
//        val phone : TextView = itemView.findViewById(R.id.help_user_phone)



    }

}




