package com.gaurav.serviceproviderapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaurav.serviceproviderapp.databinding.FragmentCategoriesBinding
import com.gaurav.serviceproviderapp.databinding.FragmentCustomerListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentCategories.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentCustomerList : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentCustomerListBinding
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var userList=ArrayList<CustomerModel>()
    lateinit var userAdapter: CustomerAdapter
    lateinit var mainActivity:MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity=activity as MainActivity

        val let = arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        userAdapter=CustomerAdapter(userList,mainActivity,this)
        db.collection("customer").addSnapshotListener { value, error ->
            for (snapshots in value!!.documentChanges) {
                when (snapshots.type) {
                    DocumentChange.Type.ADDED -> {
                        var userModel = snapshots.document.toObject(CustomerModel::class.java)
                        userModel.key = snapshots.document.id
                        userList.add(userModel)
                        userAdapter.notifyDataSetChanged()
                    }

                    DocumentChange.Type.MODIFIED -> {
                        var userModel = CustomerModel()
                        userModel = snapshots.document.toObject(CustomerModel::class.java)
                        userModel.key = snapshots.document.id ?: ""
                        for (i in 0..userList.size - 1) {
                            if ((snapshots.document.id ?: "").equals(userList[i].key)) {
                                userList.set(i, userModel)
                                break
                            }
                        }
                    }
                    DocumentChange.Type.REMOVED -> {
                        var userModel = CustomerModel()
                        userModel = snapshots.document.toObject(userModel::class.java)
                        userModel.key = snapshots.document.id ?: ""
                        for (i in 0..userList.size - 1) {
                            if ((snapshots.document.id ?: "").equals(userList[i].key)) {
                                userList.removeAt(i)
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth=Firebase.auth
        binding= FragmentCustomerListBinding.inflate(layoutInflater)

        binding.rycCustomerLsit.adapter=userAdapter

        binding.rycCustomerLsit.layoutManager=LinearLayoutManager(mainActivity)


        return (binding.root)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentCategories.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentCategories().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



/*    override fun ServiceProviderClicked(position: Int) {
        var serviceProvider = userList[position]

        AlertDialog.Builder(mainActivity).apply {
            setTitle(mainActivity.resources.getString(R.string.accept_provider))
            setMessage(mainActivity.resources.getString(R.string.accept_provider_msg))
            setPositiveButton(mainActivity.resources.getString(R.string.yes)){_,_->
                serviceProvider.currentStatus = 1
                db.collection("serviceProvider")
                    .document(userList[position].key?:"")
                    .set(serviceProvider).addOnSuccessListener {
                    //toast
                }
            }
            setNegativeButton(mainActivity.resources.getString(R.string.no)){_,_->
                serviceProvider.currentStatus = 2

                db.collection("serviceProvider")
                    .document(userList[position].key?:"")
                    .set(serviceProvider).addOnSuccessListener {
                        //toast
                    }
            }
            show()

        }

    }*/
}



