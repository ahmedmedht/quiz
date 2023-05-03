package com.example.quizproject.homepages

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.quizproject.R
import kotlinx.android.synthetic.main.fragment_take_code.*
import kotlinx.android.synthetic.main.fragment_take_code.view.*


class TakeCodeFragment : Fragment() {

    private val args:TakeCodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_take_code, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        val codeQuiz=args.codeQuiz

        view.txt_code_quiz.text = codeQuiz

        img_copy.setOnClickListener {
            val clipboard =
                context?.let { it1 -> ContextCompat.getSystemService(it1, ClipboardManager::class.java) }
            val clip = ClipData.newPlainText("label",codeQuiz)
            clipboard?.setPrimaryClip(clip)
        }
        btn_go_home_from_take_code.setOnClickListener {
            navController.navigate(R.id.action_takeCodeFragment_to_homeFragment)
        }
    }


}