package com.example.quizproject.homepages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.quizproject.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private lateinit var ref: DatabaseReference
    private val currentUser = FirebaseAuth.getInstance().currentUser

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
        val nameLiveData = getNameFromFirebase(navController,currentUser)
        nameLiveData.observe(viewLifecycleOwner) { name ->
            user_name_home.text = name
        }

        btn_create_home.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_createQuizFragment)
        }
        btn_join_home.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_joinCodeFragment)
        }







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


}