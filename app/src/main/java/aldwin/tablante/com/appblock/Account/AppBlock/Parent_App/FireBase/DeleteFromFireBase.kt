package aldwin.tablante.com.appblock.Account.AppBlock.Parent_App.FireBase


import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class DeleteFromFireBase() {
private val database = FirebaseDatabase.getInstance()
    private val dataref = database.getReference("Accounts")
    private val datarefdev = database.getReference("Devices")
    var firestore = FirebaseFirestore.getInstance()

    fun DeleteThis(userId:String,serial:String){
        dataref.child(userId).child("Devices").child(serial).removeValue()
         datarefdev.child(serial).child("ParentList").child(userId).removeValue()
    }
}