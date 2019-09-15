package com.inspiregeniussquad.handstogether.appHelpers;

import android.net.Uri;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inspiregeniussquad.handstogether.appData.Admin;
import com.inspiregeniussquad.handstogether.appData.DataStorage;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appData.Users;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DbHelper {

    private FirebaseDatabase appDb;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference teamLogoStorageReference;

    private DatabaseReference usersDatabaseReference, newsDbReference;

    private DataStorage dataStorage;

    public interface UserDbCallback {
        void onUpdated();

        void onFailed();
    }

    public interface NewsDbCallback {
        void onUpdated();

        void onDataCancelled();

        void onDataLoaded();

        void onDataInserted();
    }

    private static FirebaseDatabase firebaseDatabase;

    public static FirebaseDatabase getFirebaseDatabase() {
        if(firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(false);
        }
        return firebaseDatabase;
    }

    public DbHelper() {
        appDb = getFirebaseDatabase();

        usersDatabaseReference = appDb.getReference().child(Keys.TABLE_USER);
        newsDbReference = appDb.getReference().child(Keys.TABLE_NEWSFEED);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        teamLogoStorageReference = firebaseStorage.getReference();
    }

    public void updateUser(Users users, final UserDbCallback userDbCallback) {
        usersDatabaseReference.child(users.getUserId()).setValue(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userDbCallback.onUpdated();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userDbCallback.onFailed();
            }
        });
    }

    public void getUser() {
        usersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addNews(NewsFeedItems newsFeedItems, final NewsDbCallback newsDbCallback) {
        String id = newsDbReference.push().getKey();
        newsFeedItems.setNfId(id);
        newsDbReference.child(newsFeedItems.getNfId()).setValue(newsFeedItems)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        newsDbCallback.onDataInserted();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                newsDbCallback.onDataCancelled();

            }
        });
    }

    public void addPostImage(String eventName, Uri eventPoster,
                             final NewsFeedItems newsFeedItems, final NewsDbCallback newsDbCallback) {
        final Uri[] uploadedPosterUri = new Uri[1];
        String posterName = eventName + "_poster";
        final StorageReference storageRef = storageReference.child("Posters/" + posterName);
        UploadTask uploadTask = storageRef.putFile(eventPoster);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    newsDbCallback.onDataCancelled();
                    //showInfoAlert(getString(R.string.upload_failed));
                    AppHelper.print("Task unsuccessful!");

                    throw Objects.requireNonNull(task.getException());
                }
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    uploadedPosterUri[0] = task.getResult();
                    AppHelper.print("Upload " + uploadedPosterUri[0]);
                    if (uploadedPosterUri[0] != null) {
                        String photoStringLink = uploadedPosterUri[0].toString();
                        newsFeedItems.setPstrUrl(photoStringLink);

                        AppHelper.print("Uploaded logo Uri " + photoStringLink);
                        addNews(newsFeedItems, newsDbCallback);
                    } else {
                        newsDbCallback.onDataCancelled();
                        AppHelper.print("Image uploaded but uri null");
                    }
                } else {
                    newsDbCallback.onDataCancelled();
                }
            }
        });
    }

    private List<String> getBookmarks(DataStorage dataStorage, final UserDbCallback userDbCallback) {
        final List<String> bookmarksList = new ArrayList<>();
        usersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookmarksList.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    bookmarksList.add(keyNode.getKey());
                }
                userDbCallback.onUpdated();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                userDbCallback.onFailed();
            }
        });
        return bookmarksList;
    }

    public void addToBookmarks(DataStorage dataStorage, String nfId, final UserDbCallback userDbCallback) {
        usersDatabaseReference.child(dataStorage.getString(Keys.USER_ID))
                .child(Keys.BOOKMARKS)
                .child(nfId)
                .setValue(true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userDbCallback.onUpdated();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userDbCallback.onFailed();
                AppHelper.print("db error in addToBookmarks: "+e.getMessage());
            }
        });
    }

    public void removeFromBookmark(String nfId, final DataStorage dataStorage,
                                   final UserDbCallback userDbCallback) {
        usersDatabaseReference.child(dataStorage.getString(Keys.USER_ID))
                .child(Keys.BOOKMARKS)
                .child(nfId)
                .setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userDbCallback.onUpdated();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userDbCallback.onFailed();
                AppHelper.print("db error in removeFromBookmarks: "+e.getMessage());
            }
        });
    }

    public interface BookmarkCallback {
        void onLoaded(List<String> bookmarkList);
        void onError();
    }

    public void getUserBookmarks(DataStorage dataStorage, final BookmarkCallback bookmarkCallback) {
        final List<String> bookmarksList = new ArrayList<>();
        final DatabaseReference bookmarksReference = usersDatabaseReference.child(dataStorage.getString(Keys.USER_ID))
                .child(Keys.BOOKMARKS);
        bookmarksReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    bookmarksList.clear();
                    String bookmarkId = dataSnapshot1.getKey();
                    bookmarksList.add(bookmarkId);
                    AppHelper.print("Bookmark id : "+bookmarkId);
                }
                bookmarkCallback.onLoaded(bookmarksList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AppHelper.print("db error in getUserBookmarks: "+databaseError.getMessage());
                bookmarkCallback.onError();
            }
        });
    }

    private Users getUserData(DataStorage dataStorage, String nfId) {
        Gson gson = new Gson();
        final Users users = new Users();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                AppHelper.print("FCM TOKEN: " + token);
                users.setFcmToken(token);
            }
        });

        users.setName(dataStorage.getString(Keys.USER_NAME));
        users.setEmail(dataStorage.getString(Keys.USER_EMAIL));
        users.setGender(dataStorage.getString(Keys.USER_GENDER));
        users.setUserId(dataStorage.getString(Keys.USER_ID));
        users.setMobile(dataStorage.getString(Keys.MOBILE));

        ArrayList<String> bookmarkedPostArrayList = new ArrayList<>();
        if (dataStorage.getString(Keys.USER_BOOKMARKS) != null) {

        }

        users.setBookmarkedPosts(bookmarkedPostArrayList);

        if (dataStorage.isDataAvailable(Keys.ADMIN_INFO)) {
            String admins = dataStorage.getString(Keys.ADMIN_INFO);
            ArrayList<Admin> adminArrayList = gson.fromJson(admins, new TypeToken<ArrayList<Admin>>() {
            }.getType());

            for (Admin admin : adminArrayList) {
                if (admin.getMobile().equalsIgnoreCase(dataStorage.getString(Keys.MOBILE))) {
                    users.setIsAdmin(admin.getPosition());
                } else {
                    users.setIsAdmin("-1");
                }
            }
        }

        return users;
    }

}
