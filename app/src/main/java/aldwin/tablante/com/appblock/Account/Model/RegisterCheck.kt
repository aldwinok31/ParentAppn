package aldwin.tablante.com.appblock.Account.Model


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Created by Bobby on 03/05/2018.
 */
class RegisterCheck {

    private lateinit var database: FirebaseDatabase
    private lateinit var dataref: DatabaseReference


    // Creating a New Account
    fun registerNewAccount(a: User) {
        this.database = FirebaseDatabase.getInstance()
        this.dataref = this.database.getReference("Accounts")
        val acc = User(a.accID, a.username.toLowerCase(),
                a.password.toLowerCase(),
                a.email.toLowerCase(), a.codd.toLowerCase(), a.Firstname, a.Lastname)
        dataref.child(a.accID).setValue(acc)
        var mmap : HashMap<String,Any?> = HashMap()
        mmap.put("ParentID",a.accID)
       var db = FirebaseFirestore.getInstance().collection("Parent")
        db.add(mmap)

    }

    //  Checking of Accounts if Already exist
    fun checkacc(acc: User, acclist: ArrayList<User>): Boolean {
        var count1 = 0
        var bool = true
        if (!acclist.isEmpty()) {
            while (acclist.size > count1) {
                if (acclist[count1].username == acc.username
                        || acclist[count1].password == acc.password
                        || acclist[count1].email == acc.email) {


                    bool = false
                    break
                }

                count1++
            }


        }
        return bool

    }




}