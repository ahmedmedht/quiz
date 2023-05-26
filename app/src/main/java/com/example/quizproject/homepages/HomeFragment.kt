package com.example.quizproject.homepages

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizproject.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

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



    }



    private fun getNameFromFirebase(navController: NavController, currentUser1: FirebaseUser?): MutableLiveData<String> {
        val name = MutableLiveData<String>()

        Log.d("userIdLogin",currentUser1?.uid.toString())
        ref.child(currentUser1?.uid.toString()).get().addOnSuccessListener {
            if (it.exists()){
                name.value = it.child("name").value.toString()
                Toast.makeText(context, "USER Login successful", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "USER DOESN'T EXISTS ", Toast.LENGTH_LONG).show()
                if (currentUser1 != null) {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigateUp()
                }
            }

        }.addOnFailureListener {
            Toast.makeText(context, "Sorry, there was a error, try again", Toast.LENGTH_LONG).show()
            if (currentUser1 != null) {
                FirebaseAuth.getInstance().signOut()
                navController.navigateUp()
            }
        }
        return name
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_nav_about -> {
                // Handle click for Item 1
                // Perform necessary actions
                Toast.makeText(context,"zayed",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.item_nav_profile -> {
                // Handle click for Item 2
                // Perform necessary actions
                Toast.makeText(context,"zayedProfile",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.item_nav_logout -> {
                currentUser.signOut()
                val sharedPreferences = requireContext().getSharedPreferences("my_preferences", Context.MODE_PRIVATE)
                val editShare= sharedPreferences.edit()
                editShare.putBoolean("Stay sign",false)
                editShare.apply()
                view?.let { Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_mainActivity) }

                Toast.makeText(context,"Logout done",Toast.LENGTH_SHORT).show()
                true
            }
            // Handle other items as needed
            else -> false
        }
    }


}