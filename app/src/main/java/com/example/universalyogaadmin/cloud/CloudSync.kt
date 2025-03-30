package com.example.universalyogaadmin

import android.content.Context
import android.util.Log
import com.example.universalyogaadmin.model.ClassInstance
import com.example.universalyogaadmin.model.YogaClass
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object CloudSync {
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private const val TAG = "CloudSync"

    suspend fun sync(context: Context, dbHelper: DatabaseHelper): Boolean {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Log.e(TAG, "No internet connection")
            return false
        }
        return try {
            val yogaClasses = dbHelper.getAllClasses()
            syncYogaClasses(yogaClasses)
            val classInstances = dbHelper.getAllInstances()
            syncClassInstances(classInstances)
            Log.d(TAG, "Sync completed successfully")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Sync failed: ${e.message}", e)
            false
        }
    }

    private suspend fun syncYogaClasses(yogaClasses: List<YogaClass>) {
        val collection = firestore.collection("yoga_classes")
        yogaClasses.forEach { yogaClass ->
            val data = hashMapOf(
                "id" to yogaClass.id,
                "day" to yogaClass.day,
                "time" to yogaClass.time,
                "capacity" to yogaClass.capacity,
                "duration" to yogaClass.duration,
                "price" to yogaClass.price,
                "type" to yogaClass.type,
                "description" to yogaClass.description
            )
            collection.document(yogaClass.id.toString())
                .set(data, SetOptions.merge()) // Merge để không ghi đè hoàn toàn
                .await()
            Log.d(TAG, "Synced YogaClass: ${yogaClass.id}")
        }
    }

    private suspend fun syncClassInstances(classInstances: List<ClassInstance>) {
        val collection = firestore.collection("class_instances")
        classInstances.forEach { instance ->
            val data = hashMapOf(
                "id" to instance.id,
                "classId" to instance.classId,
                "date" to instance.date,
                "teacher" to instance.teacher,
                "comments" to instance.comments
            )
            collection.document(instance.id.toString())
                .set(data, SetOptions.merge()) // Merge để không ghi đè hoàn toàn
                .await()
            Log.d(TAG, "Synced ClassInstance: ${instance.id}")
        }
    }
}