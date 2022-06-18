package com.nsa.comuty.extra;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Storage {
    StorageReference storageRef,collegeRef;

    public Storage() {
        storageRef = FirebaseStorage.getInstance().getReference();
        collegeRef=storageRef.child("colleges");
    }

    public StorageReference getCollegeRef() {
        return collegeRef;
    }

    public StorageReference getStorageRef() {
        return storageRef;
    }
}
