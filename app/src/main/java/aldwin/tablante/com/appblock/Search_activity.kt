package aldwin.tablante.com.appblock.Histories

import aldwin.tablante.com.appblock.R
import aldwin.tablante.com.appblock.SearchText
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.search_history.*

class Search_activity : AppCompatActivity (){
    var adapter: SearchAdapter? = null
    var data = FirebaseDatabase.getInstance()
    var dataref: DatabaseReference = data.getReference("Devices")

    var FireData = FirebaseFirestore.getInstance()

    var id = ""
    var name = ""
    var arrofDevices: ArrayList<SearchText> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_history)
        clearingList.setOnClickListener {

            alertFirst(intent.getStringExtra("serial"))



        }
        devicesdb(intent.getStringExtra("serial"))



    }

    fun devicesdb(serial:String) {

        FireData.collection("Search")
                .document(serial)
                .collection("History")
                .whereEqualTo("DeviceID",serial)
                .orderBy("DateStamp",Query.Direction.DESCENDING)


                .addSnapshotListener(object: EventListener<QuerySnapshot> {

                    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                        arrofDevices!!.clear()
                        try {
                            for (doc in p0!!.documents) {
                                var devicet = doc.toObject(SearchText::class.java)
                                arrofDevices!!.add(devicet)
                                adapter = SearchAdapter(arrofDevices, applicationContext)
                                var layout_manager = LinearLayoutManager(applicationContext)
                                layout_manager.stackFromEnd = true
                                layout_manager.reverseLayout = false
                                searchRview.layoutManager = layout_manager
                                searchRview.setHasFixedSize(true)
                                searchRview.adapter = adapter
                                searchRview.adapter.notifyDataSetChanged()
                                searchRview.scrollToPosition(adapter!!.itemCount -1)

                            }
                        }

                        catch (e:KotlinNullPointerException){
                            e.printStackTrace()
                            Toast.makeText(applicationContext,"No Available Searches",Toast.LENGTH_LONG).show()

                        }
                    }
                })



    }


    fun alertFirst(serial:String){


        val alert: AlertDialog.Builder = AlertDialog.Builder(this@Search_activity)
        alert.setIcon(R.drawable.applock)

        alert.setTitle("Clear Search History")
        alert.setMessage("Are you sure?")

        alert.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, whichButton ->
            val batch = FireData.batch()

            FireData.collection("Search")
                    .document(serial).collection("History").get().addOnSuccessListener {t->
                      t.forEach { batch.delete(it.reference) }
                        batch.commit()
                    }




        })
        alert.setNegativeButton("No", DialogInterface.OnClickListener { dialog, whichButton ->

            null

        })

        alert.create()
        alert.show()

    }
}
