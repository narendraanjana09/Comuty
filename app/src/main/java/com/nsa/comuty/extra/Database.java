package com.nsa.comuty.extra;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    private FirebaseDatabase database;
    private DatabaseReference referenceColleges,referenceUsersCollege;

    public Database() {
        database=FirebaseDatabase.getInstance("https://comuty-dc2c7-default-rtdb.asia-southeast1.firebasedatabase.app/");
        referenceUsersCollege=database.getReference("usersCollege");
        referenceColleges=database.getReference("colleges");
    }

    public DatabaseReference getReferenceUsersCollege() {
        return referenceUsersCollege;
    }

    public DatabaseReference getReferenceColleges() {
        return referenceColleges;
    }

    public DatabaseReference getDatabase() {
        return database.getReference();
    }

}
