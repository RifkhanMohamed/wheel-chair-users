package com.example.wheelchairusers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.wheelchairusers.model.Help
import com.example.wheelchairusers.model.Users
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var latitude:String
    private lateinit var longitude:String
    val CITY:String = "Stoke-on-Trent"
    val API:String= "907d232eaa93fcce054f3599021123df"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val imageSlider = view.findViewById<ImageSlider>(R.id.image_slider)
        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel("https://images.unsplash.com/photo-1626253838843-6638dda75df4?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzZ8fHdoZWVsY2hhaXJ8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60","Do you see me or my disability?"))
        imageList.add(SlideModel("https://images.unsplash.com/photo-1581090123456-6405208b0264?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzB8fHdoZWVsY2hhaXJ8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60","Keep calm! It's just a wheelchair"))
        imageList.add(SlideModel("https://images.unsplash.com/photo-1556343405-79c5caffbf41?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTh8fHdoZWVsY2hhaXJ8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60","See the person, not the wheelchair"))
        imageList.add(SlideModel("https://images.unsplash.com/photo-1508847154043-be5407fcaa5a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjB8fHdoZWVsY2hhaXJ8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60","I am in a wheelchair"))
        gedData(auth.currentUser!!.uid)
        imageSlider.setImageList(imageList,ScaleTypes.FIT)
        weatherTask().execute()
        askHelp()
    }

    @SuppressLint("StaticFieldLeak")
    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                    Date(updatedAt*1000)
                )
                val temp = main.getString("temp")+"°C"



                val address = jsonObj.getString("name")+", "+sys.getString("country")

                val weatherCode = weather.getString("id").toInt()
                val weatherIcon = view?.findViewById<ImageView>(R.id.weather_image)

                val iconResourceId = when (weatherCode) {
                    800 -> R.drawable.sunny
                    801 -> R.drawable.few_cloud
                    in 802..804 -> R.drawable.cloudy
                    in 701 .. 781 ->R.drawable.fog
                    in 600..622 ->R.drawable.freeze
                    in 500..504 -> R.drawable.rain
                    511 -> R.drawable.freeze
                    in 520 .. 531 -> R.drawable.heavy_rain
                    in 300..321 ->R.drawable.drizzle
                    in 200 .. 232 -> R.drawable.thunderstorm
                    // Add more cases for other weather conditions

                    else -> R.drawable.sunny
                }
                weatherIcon!!.setImageResource(iconResourceId)

                /* Populating extracted data into our views */
                view?.findViewById<TextView>(R.id.address)!!.text = address
                view?.findViewById<TextView>(R.id.updated_at)!!.text =  updatedAtText
                view?.findViewById<TextView>(R.id.temp)!!.text = temp

            } catch (e: Exception) {

            }

        }
    }


    private fun askHelp() {
        if(ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)
        !=PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED  ){
                ActivityCompat.requestPermissions(requireContext() as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),100)
            return
        }
        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if(it!=null){
                latitude= it.latitude.toString()
                longitude=it.longitude.toString()
            }
        }
        val user = Users()
        val userDatabaseRef = database.reference.child("users").child(auth.currentUser!!.uid)
        userDatabaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {

                    val name = dataSnapshot.child("name").value as String
                    val email = dataSnapshot.child("email").value as String
                    val admin = dataSnapshot.child("admin").value as Boolean
                    var phone = dataSnapshot.child("phone").value as String
                    var sid = dataSnapshot.child("sid").value as String
                    var uid = dataSnapshot.child("uid").value as String
                    var carers = dataSnapshot.child("carers").value as Boolean

                    user.name = name
                    user.email = email
                    user.admin = admin
                    user.phone = phone
                    user.sid = sid
                    user.uid = uid
                    user.carers = carers

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })


        val carer : Users? =null
        val status="request"
        val askButton = view?.findViewById<Button>(R.id.help_button)
        askButton?.setOnClickListener{
                        val databaseRef = database.reference.child("help").child(auth.currentUser!!.uid)
                        val helps : Help = Help(user,carer,status,latitude,longitude)
                        databaseRef.get().addOnSuccessListener { it ->
                            if (it.exists()){
                                databaseRef.child("status").setValue("request").addOnCompleteListener{
                                    if (it.isSuccessful){
                                        Toast.makeText(requireContext(), "Your request submit... our care person will contact you", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(requireContext(), "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            else{
                                databaseRef.setValue(helps).addOnCompleteListener{
                                    if (it.isSuccessful){
                                        Toast.makeText(requireContext(), "Your request submit... our care person will contact you", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(requireContext(), "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }


                }

        }

    private fun gedData(id: String){
        val registerProgressBar = view?.findViewById<ProgressBar>(R.id.registerProgressBar)
        registerProgressBar?.visibility = View.VISIBLE
        val database1=database.getReference("users")
        database1.child(id).get().addOnSuccessListener {

            if (it.exists()){
                val admin = it.child("admin").value
                val carer = it.child("carers").value
                val email = it.child("email").value
                val name = it.child("name").value.toString()
                val phone = it.child("phone").value
                val sid = it.child("sid").value

                val askButton = view?.findViewById<Button>(R.id.help_button)
                if (askButton != null) {
                    if(admin==true||carer==true){
                        askButton.visibility = View.GONE
                    }
                    else{
                        askButton.visibility = View.VISIBLE
                    }
                }
            }


        }
        registerProgressBar?.visibility = View.GONE

    }


    }
