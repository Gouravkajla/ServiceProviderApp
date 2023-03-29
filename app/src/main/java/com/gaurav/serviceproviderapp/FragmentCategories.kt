package com.gaurav.serviceproviderapp

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.gaurav.serviceproviderapp.databinding.AddLayoutBinding
import com.gaurav.serviceproviderapp.databinding.FragmentCategoriesBinding
import com.gaurav.serviceproviderapp.databinding.UpdateAndDeleteBinding
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
class FragmentCategories : Fragment() ,CategoryInterface{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentCategoriesBinding
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var userList=ArrayList<CategoryModel>()
    lateinit var userAdapter: CategoryAdapter
    lateinit var mainActivity:MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity=activity as MainActivity
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        userAdapter= CategoryAdapter(userList,mainActivity,this)

        db.collection("Category").addSnapshotListener { value, error ->
            for (snapshots in value!!.documentChanges) {
                when (snapshots.type) {
                    DocumentChange.Type.ADDED -> {
                        var userModel = snapshots.document.toObject(CategoryModel::class.java)
                        userModel.key = snapshots.document.id
                        userList.add(userModel)
                        userAdapter.notifyDataSetChanged()
                    }

                    DocumentChange.Type.MODIFIED -> {
                        var userModel = CategoryModel()
                        userModel = snapshots.document.toObject(CategoryModel::class.java)
                        userModel.key = snapshots.document.id ?: ""
                        for (i in 0..userList.size - 1) {
                            if ((snapshots.document.id ?: "").equals(userList[i].key)) {
                                userList.set(i, userModel)
                                break
                            }
                        }
                    }
                    DocumentChange.Type.REMOVED -> {
                        var userModel = CategoryModel()
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
        binding= FragmentCategoriesBinding.inflate(layoutInflater)
        binding.ryclistview.adapter=userAdapter
        binding.ryclistview.layoutManager=LinearLayoutManager(mainActivity)

        binding.ftnAdd.setOnClickListener {
            mainActivity.navController.navigate(R.id.addFragmentCategories)
        }
//            val customDialog= Dialog(requireActivity())
//            val dialogBinding=AddLayoutBinding.inflate(layoutInflater)
//             customDialog.setContentView(dialogBinding.root)
//            dialogBinding.btnAdd.setOnClickListener {
//                if (dialogBinding.etAddCategory.text.isNullOrEmpty()){
//                    dialogBinding.etAddCategory.error="Enter Your Category"
//                    dialogBinding.etAddCategory.requestFocus()
//                }else{
//                    val userModel=CategoryModel(dialogBinding.etAddCategory.text.toString())
//                     db.collection("Category")
//                         .add(userModel)
//                         .addOnSuccessListener {
//                             Toast.makeText(requireActivity(), "Category Add Successfully", Toast.LENGTH_SHORT).show()
//                         }.addOnFailureListener {
//                             Toast.makeText(requireActivity(), "Category Not Added", Toast.LENGTH_SHORT).show()
//                         }
//
//
//                    customDialog.dismiss()
//                userAdapter.notifyDataSetChanged()
//                }
//            }
//            customDialog.show()


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

    override fun ListClicked(position: Int) {
        mainActivity.navController.navigate(R.id.addFragmentCategories, bundleOf("data" to userList[position], "isUpdate" to true))
//        val customdialog1=Dialog(requireActivity())
//        val dialogbinding1=UpdateAndDeleteBinding.inflate(layoutInflater)
//        customdialog1.setContentView(dialogbinding1.root)
//        dialogbinding1.etUpdateandDelete.setText(userList[position].name)
//        dialogbinding1.btnUpdate.setOnClickListener {
//            if (dialogbinding1.etUpdateandDelete.text.isNullOrEmpty()){
//                dialogbinding1.etUpdateandDelete.error="Enter Category Name"
//                dialogbinding1.etUpdateandDelete.requestFocus()
//            }else{
//                var  updateUserModel=CategoryModel()
//                updateUserModel.name=dialogbinding1.etUpdateandDelete.text.toString()
//                updateUserModel.key=userList[position].key
//                db.collection("Category")
//                    .document(userList[position].key?:"")
//                    .set(updateUserModel).addOnSuccessListener {
//                        Toast.makeText(requireActivity(), "Your Category is Update Successfully", Toast.LENGTH_SHORT).show()
//                    }.addOnFailureListener {
//                        Toast.makeText(requireActivity(), "Error Category not Update", Toast.LENGTH_SHORT).show()
//                    }
//                userAdapter.notifyDataSetChanged()
//                customdialog1.dismiss()
//            }
//        }
//        dialogbinding1.btnDelete.setOnClickListener {
//                var deleteUserModel=CategoryModel()
//                deleteUserModel.name=dialogbinding1.etUpdateandDelete.text.toString()
//                deleteUserModel.key=userList[position].key
//                db.collection("Category")
//                    .document(deleteUserModel.key?:"").delete()
//                userAdapter.notifyDataSetChanged()
//                customdialog1.dismiss()
//
//        }
//        customdialog1.show()

    }
}



