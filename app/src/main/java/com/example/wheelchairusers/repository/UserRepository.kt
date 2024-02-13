package com.example.wheelchairusers.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wheelchairusers.model.Help
import com.example.wheelchairusers.model.Users
import com.google.firebase.database.*

class UserRepository {
private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
@Volatile private var INSTANCE : UserRepository ?= null



fun getInstance() : UserRepository{
    return INSTANCE ?: synchronized(this){

        val instance = UserRepository()
        INSTANCE = instance
        instance
    }


}


fun loadUser(userList : MutableLiveData<List<Users>>){
    databaseReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            try {
                val _userList: MutableList<Users> = mutableListOf()

                for (dataSnapshot in snapshot.children) {
                    val user: Users? = dataSnapshot.getValue(Users::class.java)
                    user?.let {
                        if (it.admin == true  || it.carers==true ) {
                            _userList.add(it)
                        }
                    }
                }

                userList.postValue(_userList)
            } catch (e: Exception) {
                // Handle the exception
            }
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    })
}



}