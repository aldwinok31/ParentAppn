package aldwin.tablante.com.appblock.CaptureImages

import aldwin.tablante.com.appblock.R
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.capture_image.*
import java.io.File
import java.io.InputStream
import java.net.URL

class CaptureImage : AppCompatActivity() {
    var id = ""
    var serial = ""
    var img = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.capture_image)
       imageView7.visibility = View.INVISIBLE
        imageView12.visibility = View.INVISIBLE
        id = intent.getStringExtra("id")
        serial = intent.getStringExtra("serial")




        captureNow.setOnClickListener {
            imageView7.visibility = View.INVISIBLE
            imageView12.visibility= View.VISIBLE
            imageView12.setBackgroundResource(R.drawable.loading)
            captureNow.isEnabled = false
            sendRequest(id, serial)
        }


    }


    fun sendRequest(id: String, serial: String) {
var     anim = imageView12.background as AnimationDrawable
        anim!!.start()
        var fbase = FirebaseFirestore.getInstance()
        var rbase = fbase.collection("RequestImage")
        var mmap: HashMap<String, Any?> = HashMap()
        var datab = FirebaseDatabase.getInstance()
        var datar = datab.getReference("Images")
        datar.child(serial).removeValue()

      var tbase = fbase.collection("Devices").document(serial).update("CaptureCam", true)

      /*  datar.child(serial).orderByChild("timeStamp").addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError?) {
                Toast.makeText(applicationContext,"cancelled",Toast.LENGTH_LONG).show()
                null
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()) {
                    for (h in p0!!.children) {

                        var imageurl = h.child("image").getValue().toString()
                        var storage: FirebaseStorage = FirebaseStorage.getInstance()
                        var ref = storage.getReference("Images").child(serial)

                        ref.child(imageurl).downloadUrl.addOnSuccessListener { v ->
                            Toast.makeText(applicationContext, v.toString(), Toast.LENGTH_LONG).show()
                            Picasso.with(applicationContext).load(v).fit().into(imageView7)

                        }

                    }
                }
            }
        })*/
       rbase.whereEqualTo("RequestID", id)
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                        if (p0!!.isEmpty) {

                            //downloadImage(serial)

                            datar.child(serial).addValueEventListener(object : ValueEventListener {

                                override fun onCancelled(p0: DatabaseError?) {
                                    Toast.makeText(applicationContext,"cancelled",Toast.LENGTH_LONG).show()
                                    null
                                }

                                override fun onDataChange(p0: DataSnapshot?) {
                                    if(p0!!.exists()) {
                                        if(anim.isRunning) {
                                            anim.stop()
                                        }
                                        imageView7.visibility = View.VISIBLE
                                        imageView12.visibility= View.INVISIBLE

                                        captureNow.isEnabled = true
                                            var imageurl = p0!!.child("ChildImage").child("url").getValue().toString()
                                        Picasso.with(applicationContext).load(imageurl).fit().into(imageView7)





                                    }
                                }
                            })


                        } else {
                            val alert: AlertDialog.Builder = AlertDialog.Builder(this@CaptureImage)
                            alert.setTitle("Request picture Error:")
                            alert.setMessage("Already has a pending request")
                            alert.setNegativeButton("OK", DialogInterface.OnClickListener { dialog, whichButton ->
                                dialog.dismiss()

                            })
                            alert.show()

                        }
                    }
                })

    }





    }
