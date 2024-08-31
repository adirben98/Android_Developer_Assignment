package com.example.android_developer_assignment.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.android_developer_assignment.Model.Retrofit.ReqResResponse;
import com.example.android_developer_assignment.Model.Retrofit.RequestUsers;
import com.example.android_developer_assignment.Model.Retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Model {
    private final static Model model = new Model();
    private final AppDatabase localDb = AppLocalDb.getDb();
    private Executor executor = Executors.newSingleThreadExecutor();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    final public MutableLiveData<LoadingState> EventFeedLoadingState=new MutableLiveData<>(LoadingState.Not_Loading);

    public enum LoadingState{
        Loading,
        Not_Loading
    }
    public static Model instance() {
        return model;
    }

    private Model() {

    }

    public interface Listener<T> {
        void onComplete(T data);
    }

    LiveData<List<User>> users;
    MutableLiveData<String> error = new MutableLiveData<>("");

    void setError(String error) {
        this.error.postValue(error);
    }

    public MutableLiveData<String> getError() {
        return error;
    }
    public void refresh(){
        EventFeedLoadingState.setValue(LoadingState.Loading);

        if (!User.IsUpdated()) {
            fetchUsersFromNetwork();
        }
        else{
            EventFeedLoadingState.setValue(LoadingState.Not_Loading);

        }
    }

    public LiveData<List<User>> getUsers() {
        if (users == null) {
            users = localDb.userDao().getAll();
        }

        refresh();

        return users;
    }

    private void fetchUsersFromNetwork() {
        EventFeedLoadingState.setValue(LoadingState.Loading); // Start loading state
        for (int i = 1; i < 3; i++) {
            RequestUsers request = RetrofitClient.getClient().create(RequestUsers.class);
            int finalI = i;
            request.getUsers(i).enqueue(new Callback<ReqResResponse>() {
                @Override
                public void onResponse(Call<ReqResResponse> call, Response<ReqResResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<User> usersList = response.body().getData();
                        executor.execute(() -> {
                            localDb.userDao().insertAll(usersList.toArray(new User[0]));
                        });
                        User.setIsUpdated(true);
                    }
                    checkIfLoadingComplete(finalI); // Check if the loading should be marked as complete
                }

                @Override
                public void onFailure(Call<ReqResResponse> call, Throwable throwable) {
                    mainHandler.post(() -> {
                        setError("Failed to fetch users");
                        checkIfLoadingComplete(finalI); // Handle error case
                    });
                }
            });
        }
    }

    private void checkIfLoadingComplete(int currentIndex) {
        if (currentIndex == 2) { // Assuming 2 is the last page index
            mainHandler.post(() -> EventFeedLoadingState.setValue(LoadingState.Not_Loading));
        }
    }


    LiveData<User> user;

    public LiveData<User> getUserById(int id) {
        user = localDb.userDao().findById(id);
        return user;
    }

    public void insert(User user, Listener<Void> listener) {
        EventFeedLoadingState.postValue(LoadingState.Loading);
        executor.execute(() -> {
            localDb.userDao().insertAll(user);
            mainHandler.post(() -> {
                listener.onComplete(null);
                EventFeedLoadingState.postValue(LoadingState.Not_Loading);
            });
        });
    }

    public int generateNewId() {
        int randomId = 0;

        try {
            do {
                randomId = ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE);
            } while (localDb.userDao().checkIfIdExists(randomId) > 0);
        } catch (Throwable t) {

        }
        return randomId;
    }

    public void delete(User user) {
        executor.execute(() -> {
            localDb.userDao().delete(user);
        });
    }


    public void uploadPhoto(String id, Bitmap bitmap, Listener<String> listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/" + id + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onComplete("Failed");
                System.out.println(exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        listener.onComplete(uri.toString());
                    }
                });
            }
        });
    }
}
