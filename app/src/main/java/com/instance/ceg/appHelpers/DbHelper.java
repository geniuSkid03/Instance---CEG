package com.instance.ceg.appHelpers;

import android.net.Uri;
import android.text.TextUtils;

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
import com.instance.ceg.appData.Admin;
import com.instance.ceg.appData.CircularDataItems;
import com.instance.ceg.appData.Clubs;
import com.instance.ceg.appData.DataStorage;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appData.NewsFeedItems;
import com.instance.ceg.appData.Team;
import com.instance.ceg.appData.TeamMembers;
import com.instance.ceg.appData.Users;
import com.instance.ceg.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

public class DbHelper {

    private FirebaseDatabase appDb;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference teamLogoStorageReference;

    private DatabaseReference usersDatabaseReference, newsDbReference,
            membersDbReference, teamDatabaseReference, circularDbReference, clubDbReference;

    private DataStorage dataStorage;
    protected UploadTask uploadTask;

    private static FirebaseDatabase firebaseDatabase;
    private static boolean isPersistanceEnabled = false;

    public static FirebaseDatabase getFirebaseDatabase() {
        if (firebaseDatabase == null) {
            if (!isPersistanceEnabled) {
                FirebaseDatabase.getInstance().setPersistenceEnabled(false);
                isPersistanceEnabled = true;
            }
            firebaseDatabase = FirebaseDatabase.getInstance();
        }
        return firebaseDatabase;
    }

    public DbHelper() {
        appDb = getFirebaseDatabase();

        usersDatabaseReference = appDb.getReference().child(Keys.TABLE_USER);
        newsDbReference = appDb.getReference().child(Keys.TABLE_NEWSFEED);
        membersDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_MEMBERS);
        teamDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_TEAM);
        circularDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_CIRCULAR);
        clubDbReference = FirebaseDatabase.getInstance().getReference().child(Keys.TABLE_CLUBS);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        teamLogoStorageReference = firebaseStorage.getReference();
    }

    public FirebaseDatabase getAppDb() {
        return appDb;
    }

    public FirebaseStorage getFirebaseStorage() {
        return firebaseStorage;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }

    public StorageReference getTeamLogoStorageReference() {
        return teamLogoStorageReference;
    }

    public DatabaseReference getUsersDatabaseReference() {
        return usersDatabaseReference;
    }

    public DatabaseReference getNewsDbReference() {
        return newsDbReference;
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public static boolean isIsPersistanceEnabled() {
        return isPersistanceEnabled;
    }

    public void insertUser(Users users, InsertUserCallback callback) {
        String userId = usersDatabaseReference.push().getKey();
        if (!TextUtils.isEmpty(userId)) {
            users.setUserId(userId);
            usersDatabaseReference.child(users.getUserId()).setValue(users, (databaseError, databaseReference) -> {
                if (databaseError == null) {
                    callback.onSuccess(users);
                } else {
                    callback.onError();
                }
            });
        } else {
            callback.onError();
        }
    }

    public interface InsertUserCallback {
        void onSuccess(Users users);

        void onError();
    }

    public void updateUser(Users users, final UserDbCallback userDbCallback) {
        usersDatabaseReference.child(users.getUserId()).setValue(users)
                .addOnSuccessListener(aVoid -> userDbCallback.onUpdated()).addOnFailureListener(e -> userDbCallback.onFailed());
    }

    public void getUser(UserCallback userCallback) {
        usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCallback.onDataReceived(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                userCallback.onCancelled(databaseError);
            }
        });
    }

    public interface UserCallback {
        void onDataReceived(DataSnapshot dataSnapshot);

        void onCancelled(DatabaseError databaseError);
    }

    public void addNews(NewsFeedItems newsFeedItems, final NewsDbCallback newsDbCallback) {
        String id = newsDbReference.push().getKey();
        if (!TextUtils.isEmpty(id)) {
            newsFeedItems.setNfId(id);
            newsDbReference.child(newsFeedItems.getNfId()).setValue(newsFeedItems)
                    .addOnSuccessListener(aVoid -> newsDbCallback.onDataInserted())
                    .addOnFailureListener(e -> newsDbCallback.onDataCancelled());
        }
    }

    public void getAllNews(NewsRetrieveCallback newsRetrieveCallback) {
        newsDbReference.orderByChild("timeStamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    newsRetrieveCallback.onSuccess(dataSnapshot);
                    AppHelper.print("News exists and trying to retrieve!");
                } else {
                    AppHelper.print("No news found!");
                    newsRetrieveCallback.onFailed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                newsRetrieveCallback.onFailed();
                AppHelper.print("DatabaseError : " + databaseError.getMessage());
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
                AppHelper.print("db error in addToBookmarks: " + e.getMessage());
            }
        });
    }

    public void removeFromBookmark(String nfId, final DataStorage dataStorage,
                                   final UserDbCallback userDbCallback) {
        usersDatabaseReference.child(dataStorage.getString(Keys.USER_ID))
                .child(Keys.BOOKMARKS)
                .child(nfId)
                .setValue(null)
                .addOnSuccessListener(aVoid -> userDbCallback.onUpdated())
                .addOnFailureListener(e -> {
                    userDbCallback.onFailed();
                    AppHelper.print("db error in removeFromBookmarks: " + e.getMessage());
                });
    }

    public void getUserBookmarks(DataStorage dataStorage, final BookmarkCallback bookmarkCallback) {
        final List<String> bookmarksList = new ArrayList<>();
        final DatabaseReference bookmarksReference = usersDatabaseReference.child(dataStorage.getString(Keys.USER_ID))
                .child(Keys.BOOKMARKS);
        bookmarksReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    bookmarksList.clear();
                    String bookmarkId = dataSnapshot1.getKey();
                    bookmarksList.add(bookmarkId);
                    AppHelper.print("Bookmark id : " + bookmarkId);
                }
                bookmarkCallback.onLoaded(bookmarksList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AppHelper.print("db error in getUserBookmarks: " + databaseError.getMessage());
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

    public void updateCircular(CircularDataItems circularDataItems, UpdateCallback updateCallback) {
        circularDbReference.child(circularDataItems.getcId()).setValue(circularDataItems)
                .addOnSuccessListener(aVoid -> updateCallback.onSuccess())
                .addOnFailureListener(e -> updateCallback.onFailed());
    }

    public void updateNews(NewsFeedItems newsFeedItems, UpdateCallback updateCallback) {
        newsDbReference.child(newsFeedItems.getNfId()).setValue(newsFeedItems)
                .addOnSuccessListener(aVoid -> updateCallback.onSuccess())
                .addOnFailureListener(e -> updateCallback.onFailed());
    }

    public void updateTeamData(Team team, UpdateCallback updateCallback) {
        teamDatabaseReference.child(team.gettId()).setValue(team)
                .addOnSuccessListener(aVoid -> updateCallback.onSuccess())
                .addOnFailureListener(e -> updateCallback.onFailed());
    }

    public void updateTeamMembers(String teamMemId, ArrayList<TeamMembers> teamMembersArrayList, UpdateCallback updateCallback) {
        membersDbReference.child(teamMemId).setValue(teamMembersArrayList)
                .addOnSuccessListener(aVoid -> updateCallback.onSuccess())
                .addOnFailureListener(e -> updateCallback.onFailed());
    }


    public void getTeamMembers(String teamMemberId, TeamMembersCallback teamMembersCallback) {
        membersDbReference.child(teamMemberId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    teamMembersCallback.onSuccess(dataSnapshot);
                    AppHelper.print("Team member datasnapshot not null");
                } else {
                    teamMembersCallback.onFail("");
                    AppHelper.print("Team member datasnapshot null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                teamMembersCallback.onFail(databaseError.getMessage());
                AppHelper.print("Error in loading team members");
            }
        });
    }

    public void insertClubs(Clubs clubs, boolean isNewRecord, boolean isEditAndImageEdited,
                            ClubsCallback clubsCallback) {
        String clubsImgName = clubs.getClubsName() + "_icon";

        if (isEditAndImageEdited) {
            final StorageReference storageRef = storageReference.child("ClubsIcon/" + clubsImgName);

            uploadTask = storageRef.putFile(Uri.parse(clubs.getClubsImgUrl()));
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    clubsCallback.onFailed();
                    AppHelper.print("Task unsuccessful!");
                    throw Objects.requireNonNull(task.getException());
                }

                return storageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        String photoStringLink = task.getResult().toString();
                        AppHelper.print("Uploaded logo Uri " + photoStringLink);

                        clubs.setClubsImgUrl(task.getResult().toString());

                        String clubId;
                        if(isNewRecord) {
                            clubId = clubDbReference.push().getKey();
                        } else {
                            clubId = clubs.getClubsId();
                        }

                        if (clubId != null) {
                            clubDbReference.child(clubId).setValue(clubs)
                                    .addOnSuccessListener(aVoid -> clubsCallback.onSuccess())
                                    .addOnFailureListener(e -> clubsCallback.onFailed());
                        } else {
                            clubsCallback.onFailed();
                        }
                    } else {
                        clubsCallback.onFailed();
                        AppHelper.print("Image uploaded but uri null");
                    }
                } else {
                    AppHelper.print("Task failed: " + task.getException());
                    clubsCallback.onFailed();
                }
            });
        }else {
            String clubId;
            if(isNewRecord) {
                clubId = clubDbReference.push().getKey();
            } else {
                clubId = clubs.getClubsId();
            }

            if (clubId != null) {
                clubDbReference.child(clubId).setValue(clubs)
                        .addOnSuccessListener(aVoid -> clubsCallback.onSuccess())
                        .addOnFailureListener(e -> clubsCallback.onFailed());
            } else {
                clubsCallback.onFailed();
            }
        }
    }

    public interface ClubsCallback {
        void onSuccess();

        void onFailed();
    }

    public interface NewsRetrieveCallback {
        void onSuccess(DataSnapshot dataSnapshot);

        void onFailed();
    }

    public interface UpdateCallback {
        void onSuccess();

        void onFailed();
    }

    public interface TeamMembersCallback {
        void onSuccess(DataSnapshot dataSnapshot);

        void onFail(String error);
    }

    public interface BookmarkCallback {
        void onLoaded(List<String> bookmarkList);

        void onError();
    }

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
}
