package com.example.fyp1

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fyp1.databinding.ExampleCommentItemBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class CommentsAdapter(private val commentsList: MutableList<Comments>, private val context:Context, private val functionSwipe:String,private val activity: AppCompatActivity) :
    RecyclerView.Adapter<CommentsAdapter.ExampleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExampleViewHolder = ExampleViewHolder(
        ExampleCommentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),context,functionSwipe,activity
    )
    override fun onBindViewHolder(holder: ExampleViewHolder, position: Int) {
        val currentItem = commentsList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = commentsList.size

    class ExampleViewHolder(private val binding: ExampleCommentItemBinding, private val context: Context,private val functionSwipe:String,private val activity: AppCompatActivity) : RecyclerView.ViewHolder(binding.root) {

        var commentDatabaseReference: DatabaseReference? = FirebaseDatabase.getInstance().getReference("comment")
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd-MMMM-yyyy")
        fun bind(comments: Comments) {

            binding.userID = comments.patientID
            //binding.deleteComm.setImageResource(R.drawable.ic_edit)
            val date = dateFormat.parse("${comments.commDate}")
            val dateStr = formatter.format(date)
            binding.dateTime = "Posted by $dateStr ${comments.commTime}"
            binding.commContent = comments.commentContent
            binding.rate = String.format("%.1f", comments.rating!!.toDouble())
            if(functionSwipe == "edit"){
                binding.functionImage.setImageResource(R.drawable.ic_edit)
                binding.functionLayout.setBackgroundColor(Color.parseColor("#3FB5A3"))
                binding.functionText.setText("Edit Comment")
            }else{
                binding.functionImage.setImageResource(R.drawable.ic_baseline_report)
                binding.functionLayout.setBackgroundColor(Color.parseColor("#CD6E6E"))
                binding.functionText.setText("Report Abuse")
            }

            binding.functionImage.setOnClickListener {
                //Snackbar.make(it, "$patientID click", Snackbar.LENGTH_SHORT).show()
                if(functionSwipe == "edit"){
                    val position = adapterPosition
                    editComments(comments,context,activity)
                }else{
                    reportComments(comments, context)
                    //Toast.makeText(context, "Report", Toast.LENGTH_SHORT).show()
                }
            }
            binding.functionLayout.setOnClickListener {
                //Snackbar.make(it, "$patientID click", Snackbar.LENGTH_SHORT).show()
                if(functionSwipe == "edit"){
                    val position = adapterPosition
                    editComments(comments,context,activity)
                }else{
                    reportComments(comments, context)
                }
            }

        }

        private fun editComments(comments: Comments,context: Context, activity: AppCompatActivity){

            val date = dateFormat.parse("${comments.commDate}")
            val dateStr = formatter.format(date)

            val builder = AlertDialog.Builder(context)
            val inflater = activity.layoutInflater
            val dialogLayout = inflater.inflate(R.layout.alert_dialog_update_comments, null)
            val commentContent  = dialogLayout.findViewById<EditText>(R.id.commentsEditText)
            val textCount = dialogLayout.findViewById<TextView>(R.id.textCounter)
            val ratingBar = dialogLayout.findViewById<RatingBar>(R.id.rateBar)
            val tRate = dialogLayout.findViewById<TextView>(R.id.rateText)
            ratingBar.setOnRatingBarChangeListener(object : RatingBar.OnRatingBarChangeListener {
                override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                    tRate.text = p1.toString()
                }
            })
            builder.setView(dialogLayout)
            commentContent.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    textCount.text = "${s.length}/100"
                }
            })
            commentContent.setText("${comments.commentContent}")
            ratingBar.rating = comments.rating.toString().toFloat()
            builder.setPositiveButton("Update") { _, _ ->
                val ID = comments.commentID.toString()
                val content = commentContent.text.toString().trim{it<=' '}
                val rating = tRate.text.toString()

                if (content==comments.commentContent.toString() && rating == comments.rating.toString()){
                    Toast.makeText(context,"Nothing update.",Toast.LENGTH_SHORT).show()
                }else{
                    val alertDialog = AlertDialog.Builder(context).create()
                    alertDialog.setTitle("Confirmation")
                    alertDialog.setMessage("Confirm to update this comment posted by $dateStr?")

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes"
                    ) { _, _ ->

                        if (TextUtils.isEmpty(content)) {
                            commentContent.error = "Please enter the comment content!"
                        }else {
                        commentDatabaseReference!!.child(ID).child("commentContent").setValue(content)
                        commentDatabaseReference!!.child(ID).child("rating").setValue(rating)
                            Log.i("myInfoTag","completed")
                        //Toast.makeText(context,"${comments.commentID} $content $rating",Toast.LENGTH_SHORT).show()
                        }
                    }

                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No"
                    ) { _, _ ->
                        Toast.makeText(context,"Nothing update.",Toast.LENGTH_SHORT).show() }

                    alertDialog.show()

                    val btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    val btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                    val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                    layoutParams.weight = 10f
                    btnPositive.layoutParams = layoutParams
                    btnNegative.layoutParams = layoutParams
                }
            }

            builder.setNegativeButton("Cancel"){dialog, _ ->  dialog.dismiss()}
            builder.show()
        }
        private fun reportComments(comments: Comments,context: Context){

            val ID = comments.commentID.toString()
            val status:String = "reported"

            val alertDialog = AlertDialog.Builder(context, R.style.AlertDialogCustom).create()
            alertDialog.setTitle("Report Issue")
            alertDialog.setMessage("Confirm to report this comment posted by ${comments.patientID}")

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes") { _, _ ->
                Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show()
                commentDatabaseReference!!.child(ID).child("status").setValue("")
                commentDatabaseReference!!.child(ID).child("status").setValue(status)
                var toast = Toast.makeText(context,"We have received your feedback. Your report is anonymous. Thank you.", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No") { dialog, _ ->dialog.dismiss()}
            alertDialog.show()
        }
    }

    override fun toString(): String {
        return commentsList.toString()
    }
}

