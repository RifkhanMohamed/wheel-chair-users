package com.example.wheelchairusers.repository

import androidx.lifecycle.MutableLiveData
import com.example.wheelchairusers.model.Help
import com.example.wheelchairusers.model.Users
import com.google.firebase.database.*

class HelpRepository {
    private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("help")
    private val userDatabaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    @Volatile private var INSTANCE : HelpRepository ?= null

    fun getInstance() : HelpRepository{
        return INSTANCE ?: synchronized(this){

            val instance = HelpRepository()
            INSTANCE = instance
            instance
        }


    }


    fun loadHelp(helpList : MutableLiveData<List<Help>>){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {

//                    val _helpList : List<Help> = snapshot.children.map { dataSnapshot ->
//                        dataSnapshot.getValue(Help::class.java)!!
//
//                    }

                    val _helpList: MutableList<Help> = mutableListOf()

                    for (dataSnapshot in snapshot.children) {
                        val user: Help? = dataSnapshot.getValue(Help::class.java)
                        user?.let {
                            if (it.status == "request" ) {
                                _helpList.add(it)
                            }
                        }
                    }


                    val userCollectionIds = _helpList.map { help ->
                        help.user
                    }

                    userDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val userCollectionDetails : MutableLiveData<List<Users>>

                            for (childSnapshot in userCollectionIds) {
                                val userCollection = childSnapshot?.uid
                                if (userCollection != null && userCollectionIds.equals(userCollection)) {

                                }
                            }

                            // Do something with otherCollectionDetails
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle any errors
                        }
                    })



                    helpList.postValue(_helpList)
                }
                catch (e : Exception){
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })




    }
}

