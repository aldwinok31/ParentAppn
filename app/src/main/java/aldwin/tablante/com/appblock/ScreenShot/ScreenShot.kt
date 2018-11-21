package aldwin.tablante.com.appblock.ScreenShot

import aldwin.tablante.com.appblock.R
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_screenshot.*

class ScreenShot : AppCompatActivity() {
    var id = ""
    var serial = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screenshot)
        id = intent.getStringExtra("id")
        serial = intent.getStringExtra("serial")
        videoView.visibility = View.INVISIBLE
        imageView14.visibility = View.INVISIBLE

        button5.setOnClickListener {

            imageView14.setBackgroundResource(R.drawable.loading)
            var anim = imageView14.background as AnimationDrawable
            anim!!.start()

            videoView.visibility = View.INVISIBLE

            imageView14.visibility = View.VISIBLE
            sendRequest(id, serial)
            button5.isEnabled = false
            var dbase = FirebaseDatabase.getInstance()

            var rbase = dbase.getReference("Videos")

            rbase.child(serial).addValueEventListener(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError?) {

                    Toast.makeText(applicationContext, "Requesting...", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot?) {
                    if (p0!!.exists()) {
                        var s = p0!!.child("file").value.toString()

                        var sbase = FirebaseStorage.getInstance()
                        var rsbase = sbase.getReference("Videos")
                        var vidurl = rsbase.child(serial).child(s).downloadUrl
                        try {


                            vidurl.addOnSuccessListener { v ->
                                if (anim.isRunning) {
                                    anim.stop()
                                }
                                imageView14.visibility = View.INVISIBLE
                                button5.isEnabled = true

                                videoView.visibility = View.VISIBLE
                                videoView.setVideoURI(v)
                                videoView.requestFocus()
                                videoView.start()


                            }
                        } catch (e: Exception) {

                            e.printStackTrace()
                        }








                        vidurl.addOnCompleteListener {
                            try {
                                rbase.child(serial).removeValue()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }


                    } else {
                        //sendRequest(id, serial)
                        Toast.makeText(applicationContext, "Requesting...", Toast.LENGTH_LONG).show()

                    }


                }

            })


        }


    }

    fun sendRequest(id: String, serial: String) {

        var fbase = FirebaseFirestore.getInstance()
        var rbase = fbase.collection("RequestImage")
        var mmap: HashMap<String, Any?> = HashMap()
        var tbase = fbase.collection("Devices").document(serial).update("Screenshot", true)


    }

}