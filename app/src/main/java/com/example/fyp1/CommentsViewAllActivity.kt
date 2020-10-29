package com.example.fyp1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_comments_view.*
import java.util.concurrent.Semaphore
import kotlin.random.Random


class CommentsViewAllActivity : AppCompatActivity() {

    var commentDatabaseReference: DatabaseReference? = FirebaseDatabase.getInstance().getReference()

    var commentCount:Int? = 0

//    private lateinit var deleteIcon: Drawable
//    private lateinit var editIcon: Drawable
//    private var swipeBgdEdit: ColorDrawable = ColorDrawable(Color.parseColor("#0000FF"))
//    private var swipeBgdDelete: ColorDrawable = ColorDrawable(Color.parseColor("#0000FF"))

    private lateinit var adapter: CommentsAdapter
    private val commentsList: MutableList<Comments> = mutableListOf<Comments>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CommentsAdapter(commentsList,this,"report",this)
        readData()
        setContentView(R.layout.activity_comments_view_all)
        var navigationView = findViewById<BottomNavigationView>(R.id.navigationView)

        navigationView.selectedItemId  = R.id.edit

        navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.edit -> {
                    Toast.makeText(this,"all click!",Toast.LENGTH_SHORT)
                    true
                }
                R.id.delete -> {
                    Toast.makeText(this,"yours click!",Toast.LENGTH_SHORT)
                    val intphto = Intent(this, CommentsViewActivity::class.java)
                    startActivity(intphto)
                    overridePendingTransition(0,0)
                    true
                }
            }
            false}
        Log.i("myInfoTag", "${adapter.toString()}")
    }

    fun loadData(dataSnapshot: DataSnapshot) {
        commentsList.clear()
        val snoList = ArrayList<String>()
        Log.i("myInfoTag", "connected")
        val count = dataSnapshot.child("commentsCounter").getValue()!!
        commentCount = count.toString().toInt() + 1
        Log.i("myInfoTag", "Current count is $count and next is ${commentCount.toString()}")

        for (i in 1 until commentCount.toString().toInt()) {
            val commentID = dataSnapshot.child("commentsSno").child(i.toString()).child("commentID").getValue()!!
            Log.i("myInfoTag", "Comment ID $i : ${commentID.toString()}")
            snoList.add(commentID.toString())
            val comment = dataSnapshot.child("comment").child(snoList.get(i - 1))
                .getValue(Comments::class.java)!!
            val item = Comments(
                comment.commDate.toString(),
                comment.commTime.toString(),
                comment.commentContent.toString(),
                comment.commentID.toString(),
                comment.patientID.toString(),
                comment.rating.toString(),
                comment.status.toString()
            )
            if (item.status!="disabled")
                commentsList.add(item)
            //Log.i("myInfoTag", "Comment ${comment.commentID.toString()} : ${comment.toString()}")
            //Log.i("myInfoTag", "${item.toString()}")
        }

        Log.i("myInfoTag", "${adapter.toString()}")
        recycler_view.adapter = adapter

        val swipeHelperCallback = SwipeHelperCallback().apply {
            setClamp(300f)
        }
        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler_view)

        recycler_view.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = this.adapter
            setHasFixedSize(true)
            setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(this)
                false
            }
        }
    }
    private fun readData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    loadData(dataSnapshot)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("myInfoTag", "loadPost:onCancelled", databaseError.toException())
            }
        }
        commentDatabaseReference!!.addValueEventListener(postListener)
    }

    fun insertItem(view: View) {
        val intphto = Intent(this, CommentAddActivity::class.java)
        startActivity(intphto)

//        val index = Random.nextInt(2)
//        val newItem = ExampleItem(
//            "New item at position $index",
//            "Line 2",
//            "5.0"
//        )
//        exampleList.add(1, newItem)
//        adapter.notifyItemInserted(1)
    }
    fun removeItem(view: View) {
        val index = Random.nextInt(5)
        commentsList.removeAt(index)
        adapter.notifyItemRemoved(index)
    }
    fun removeItem1(viewHolder: RecyclerView.ViewHolder){
        commentsList.removeAt(viewHolder.adapterPosition )
        adapter.notifyItemRemoved(viewHolder.adapterPosition)
    }
}