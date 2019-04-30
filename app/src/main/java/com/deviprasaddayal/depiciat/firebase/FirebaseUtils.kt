package com.deviprasaddayal.depiciat.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseUtils {
    companion object{
        fun getBearerReference() : DatabaseReference {
            return FirebaseDatabase.getInstance().reference.child("bearers")
        }

        fun getBankReference() : DatabaseReference {
            return FirebaseDatabase.getInstance().reference.child("banks")
        }

        fun getNomineeReference() : DatabaseReference {
            return FirebaseDatabase.getInstance().reference.child("nominees")
        }

        fun getPolicyReference() : DatabaseReference {
            return FirebaseDatabase.getInstance().reference.child("policies")
        }
    }
}