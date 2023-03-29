package com.gaurav.serviceproviderapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gaurav.serviceproviderapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentLogin.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentLogin : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var auth:FirebaseAuth
    val db = Firebase.firestore
    lateinit var binding: FragmentLoginBinding
    lateinit var mainActivity:MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity=activity as MainActivity

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth= Firebase.auth
        binding=FragmentLoginBinding.inflate(layoutInflater)

        // Inflate the layout for this fragment
        binding.btnLogin.setOnClickListener {
        if (binding.etEmail.text.isNullOrEmpty()){
              binding.etEmail.error="Enter your EMail"
              binding.etEmail.requestFocus()
          }else if (binding.etPassword.text.isNullOrEmpty()){
              binding.etPassword.error="Enter Your Password"
              binding.etPassword.requestFocus()
          }else{
              auth.signInWithEmailAndPassword(binding.etEmail.text.toString(),binding.etPassword.text.toString())
                  .addOnSuccessListener {
                      Toast.makeText(requireActivity(),"Login Seccessfully",Toast.LENGTH_SHORT).show()
                      mainActivity.navController.navigate(R.id.categoryandServiceProvider)

                  }.addOnFailureListener {
                      Toast.makeText(requireActivity(), "Login Failed", Toast.LENGTH_SHORT).show()
                  }

        }}
        return (binding.root)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentLogin.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentLogin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}