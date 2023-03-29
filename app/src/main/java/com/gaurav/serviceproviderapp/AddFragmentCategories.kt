package com.gaurav.serviceproviderapp

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.gaurav.serviceproviderapp.databinding.AddLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentCategories.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragmentCategories : Fragment(){
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: AddLayoutBinding
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private var btmap : Bitmap? = null
    var userList=ArrayList<CategoryModel>()
    private var storageRef = FirebaseStorage.getInstance()
    lateinit var userAdapter: CategoryAdapter
    lateinit var mainActivity:MainActivity
    private var imageUri : Uri?=null
    var categoryModel = CategoryModel()
    var isupdate = false
    var collectionName = "Category"
    var getPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted->
        if(isGranted){
            Toast.makeText(mainActivity, "Granted", Toast.LENGTH_SHORT).show()
            getImage.launch("image/*")
        }else{
            Toast.makeText(mainActivity,"Not Granted", Toast.LENGTH_SHORT).show()
        }
    }

    var getImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        System.out.println("it $it")
        binding.image.setImageURI(it)
        it?.let {
            btmap = MediaStore.Images.Media.getBitmap(mainActivity.contentResolver, it)
            binding.image.setImageBitmap(btmap)
            imageUri = it
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity=activity as MainActivity



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth=Firebase.auth
        binding= AddLayoutBinding.inflate(layoutInflater)
        arguments?.let {
            categoryModel = it.getSerializable("data") as CategoryModel
            isupdate = it.getBoolean("isUpdate",false)
        }

        binding.image.setOnClickListener {
            if(ContextCompat.checkSelfPermission(mainActivity,
                    READ_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED)
            {
                getImage.launch("image/*")
                Toast.makeText(mainActivity,"Granted",Toast.LENGTH_LONG).show()
            }
            else
                getPermission.launch(READ_EXTERNAL_STORAGE)
            }
        if(isupdate == true){
            binding.btnAdd.setText("Update")
            binding.btnDelete.visibility = View.VISIBLE
            binding.etAddCategory.setText((categoryModel.name?:""))

            Glide.with(mainActivity)
                .load(categoryModel.image)
                .circleCrop()
                .into(binding.image)
        }else{
            binding.btnAdd.setText("Add")
            binding.btnDelete.visibility = View.GONE
        }
        binding.btnDelete.setOnClickListener {
            db.collection(collectionName).document(categoryModel.key?:"").delete()
                .addOnSuccessListener {
//                toast
                }
                .addOnFailureListener {
//                    toast
                }
        }

        binding.btnAdd.setOnClickListener {
            if (binding.etAddCategory.text.isNullOrEmpty()){
                binding.etAddCategory.error="Enter Your Category"
                binding.etAddCategory.requestFocus()
            }else{
                if (this::imageUri !=null){
                    binding?.llLoader?.visibility = View.VISIBLE
                    val ref = storageRef.reference.child(Calendar.getInstance().timeInMillis.toString())
                    var uploadTask = imageUri?.let {it1 -> ref.putFile(it1) }
                    uploadTask?.continueWithTask{ task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                binding?.llLoader?.visibility = View.GONE
                                throw it
                            }
                        }
                        ref.downloadUrl
                    }?.addOnCompleteListener { task ->
                        binding?.llLoader?.visibility = View.GONE

                        System.out.println("in on complete listener")
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            categoryModel.image = downloadUri.toString()
                            AddUpdateCategory()

                        }
                    }
                } else{
                    AddUpdateCategory()

                }


              /*  if (isupdate == true) {
                    categoryModel.name = binding.etAddCategory.text.toString()
                    db.collection("Category").document(categoryModel.key?:"")
                        .set(categoryModel)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireActivity(),
                                "Category Add Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                requireActivity(),
                                "Category Not Added",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                else{
                    if (this::imageUri !=null){
                        binding?.llLoader?.visibility = View.VISIBLE
                        val ref = storageRef.reference.child(Calendar.getInstance().timeInMillis.toString())
                        var uploadTask = imageUri?.let {it1 -> ref.putFile(it1) }
                        uploadTask?.continueWithTask{ task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    binding?.llLoader?.visibility = View.GONE

                                    throw it
                                }
                            }
                            ref.downloadUrl
                        }?.addOnCompleteListener { task ->
                            binding?.llLoader?.visibility = View.GONE

                            System.out.println("in on complete listener")
                            if (task.isSuccessful) {
                                val downloadUri = task.result
                                categoryModel.image = downloadUri.toString()
                                db.collection(collectionName).add(CategoryModel(
                                    key = "",
                                    name = binding.etAddCategory.text.toString(),
                                    image = downloadUri.toString()
                                ))
                                    .addOnSuccessListener {
                                        mainActivity.navController.popBackStack()
                                    }.addOnFailureListener {

                                    }

                            }
                        }
                    } else{

                    }
                }*/




                }
            }

        return (binding.root)
        }

    fun AddUpdateCategory(){
        if(isupdate){
            db.collection(collectionName).document(categoryModel.key?:"").set(categoryModel)
                .addOnSuccessListener {
                    mainActivity.navController.popBackStack()
                }.addOnFailureListener {

                }
        }else{
            db.collection(collectionName).add(categoryModel)
                .addOnSuccessListener {
                    mainActivity.navController.popBackStack()
                }.addOnFailureListener {

                }
        }
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


}



