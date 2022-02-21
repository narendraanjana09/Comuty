package com.nsa.comuty.extra;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    private FirebaseDatabase database;
    private DatabaseReference referenceUsers;

    public Database() {
        database=FirebaseDatabase.getInstance("https://comuty-7dd70-default-rtdb.asia-southeast1.firebasedatabase.app/");
        referenceUsers=database.getReference("users");
    }

    public DatabaseReference getReferenceUsers() {
        return referenceUsers;
    }
}
