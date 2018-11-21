package aldwin.tablante.com.appblock.Account.Model

import com.google.firebase.database.FirebaseDatabase

/**
 * Created by Bobby on 04/05/2018.
 */
class GenerateID {
    // Generates ID
    fun getNewId(): String {

        var database = FirebaseDatabase.getInstance()
        var dataref = database.getReference("Accounts")
        var idkey = dataref.push().key

        return idkey
    }
}