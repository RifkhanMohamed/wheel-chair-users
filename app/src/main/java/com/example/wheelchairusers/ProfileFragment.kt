package com.example.wheelchairusers

import android.content.Context
import android.content.Intent
import android.content.LocusId
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var modeSwitch : SwitchCompat
    private var nightMode : Boolean = false
    private var editor : SharedPreferences.Editor?=null
    private var sharedPreferences : SharedPreferences?=null


    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myLoginButton = view.findViewById<Button>(R.id.profile_login_button)
        val myLogoutButton = view.findViewById<Button>(R.id.profile_logout_button)
        val adminButton = view.findViewById<Button>(R.id.admin)
        adminButton.visibility = View.GONE
        // Get a reference to the button

        if (auth.currentUser!=null){
            gedData(auth.currentUser!!.uid)
            myLoginButton.visibility = View.GONE
            myLogoutButton.visibility = View.VISIBLE
        }else{
            myLoginButton.visibility = View.VISIBLE
            myLogoutButton.visibility = View.GONE
        }


        // Set the button's click listener
        myLoginButton.setOnClickListener {
            // Do something when the button is clicked
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        myLogoutButton.setOnClickListener{
            auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        modeSwitch=view.findViewById(R.id.mode_switch)
        sharedPreferences=activity?.getSharedPreferences("MODE",Context.MODE_PRIVATE)
        nightMode=sharedPreferences?.getBoolean("night",false)!!

        if (nightMode){
            modeSwitch.isChecked=true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        modeSwitch.setOnCheckedChangeListener{compoundButton,state ->
            if (nightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor=sharedPreferences?.edit()
                editor?.putBoolean("night",false)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor=sharedPreferences?.edit()
                editor?.putBoolean("night",true)
            }
            editor?.apply()
        }

    }

    private fun gedData(id: String){
        database=FirebaseDatabase.getInstance().getReference("users")
        database.child(id).get().addOnSuccessListener {

            if (it.exists()){
                val admin = it.child("admin").value
                val carer = it.child("carers").value
                val email = it.child("email").value.toString()
                val name = it.child("name").value.toString()
                val phone = it.child("phone").value.toString()
                val sid = it.child("sid").value

                val adminButton = view?.findViewById<Button>(R.id.admin)
                view?.findViewById<TextView>(R.id.user_name)!!.text ="Hi, "+name
                view?.findViewById<TextView>(R.id.user_phone)!!.text = "Phone : "+phone
//                if (adminButton != null) {
//                    if(admin==true){
//                        adminButton.visibility = View.VISIBLE
//                    }
//                else{
//                    adminButton.visibility = View.GONE
//                }
//                }
            }


            }

        }

}