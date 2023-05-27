package com.example.quizproject.homepages

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizproject.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var name = ""
    private var email = ""

    private lateinit var ref: DatabaseReference
    private val currentUser = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController= Navigation.findNavController(view)
        ref = FirebaseDatabase.getInstance().getReference("user")

        btn_create_home.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_createQuizFragment)
        }
        btn_join_home.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_joinCodeFragment)
        }

        img_menu_drawer.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        nav_view.setNavigationItemSelectedListener(this)
        val headerView: View = nav_view.getHeaderView(0)
        val header=headerView.findViewById<View>(R.id.nav_header)

        header.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_profileFragment)
        }
        getNameFromFirebase(navController, currentUser){
            if (it){
                headerView.findViewById<TextView>(R.id.txt_user_email).text = email
                headerView.findViewById<TextView>(R.id.txt_user_name).text = name
            }

        }



    }



    private fun getNameFromFirebase(
        navController: NavController,
        currentUser1: FirebaseAuth,
        callback: (Boolean) -> Unit

    ){



        Log.d("userIdLogin",currentUser1.uid.toString())
        ref.child(currentUser1.uid.toString()).get().addOnSuccessListener {
            if (it.exists()){
                name = it.child("name").value.toString()
                email=it.child("email").value.toString()
                callback(true)
            }else{
                Toast.makeText(context, "USER DOESN'T EXISTS ", Toast.LENGTH_LONG).show()
                callback(false)
                FirebaseAuth.getInstance().signOut()
                navController.navigateUp()
            }

        }.addOnFailureListener {
            callback(false)
            Toast.makeText(context, "Sorry, there was a error, try again", Toast.LENGTH_LONG).show()
            FirebaseAuth.getInstance().signOut()
            navController.navigateUp()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navigate=view?.let { Navigation.findNavController(it)}
        return when (item.itemId) {
            R.id.item_nav_about -> {
                navigate?.navigate(R.id.action_homeFragment_to_aboutUsFragment)
                true
            }
            R.id.item_nav_my_quizzes_created -> {
                navigate?.navigate(R.id.action_homeFragment_to_createdQuizzesFragment)
                true
            }
            R.id.item_nav_logout -> {
                currentUser.signOut()
                val sharedPreferences = requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                val editShare= sharedPreferences.edit()
                editShare.putBoolean("Stay sign",false)
                editShare.apply()
                navigate?.navigate(R.id.action_homeFragment_to_mainActivity)

                Toast.makeText(context,"Logout done",Toast.LENGTH_SHORT).show()
                true
            }
            // Handle other items as needed
            else -> false
        }
    }


}