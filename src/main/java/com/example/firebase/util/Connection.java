package com.example.firebase.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Repository
public class Connection {

    public Connection(){
        initializeFirebase();
    }

    @PostConstruct
    private void initializeFirebase() {
        try {

            FileInputStream serviceAccount =
                    new FileInputStream("serviceAccount.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://bdii-firebase-45aa0-default-rtdb.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createNewUser(User user)  {

        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getId());
        // Grava os dados do objeto User no Firebase Realtime Database
        CompletableFuture<Void> writeFuture = new CompletableFuture<>();
        databaseRef.setValue(user, (error, ref) -> {
            if (error == null) {
                writeFuture.complete(null); // Marca a gravação como concluída
            } else {
                writeFuture.completeExceptionally(error.toException()); // Marca a gravação como falha
            }
        });
        // Aguarda a conclusão da gravação
        try {
            writeFuture.get(); // Bloqueia a execução até que a gravação seja concluída ou falhe
        } catch (InterruptedException | ExecutionException err) {
            // Lida com exceção, se ocorrer algum erro na gravação
            System.err.println(err.getMessage());
        }
    }

    public User returnUser(String key) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(key);
        CompletableFuture<Void> writeFuture = new CompletableFuture<>();
        final User[] user = {null};
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Converte o DataSnapshot para o objeto User
                    user[0] = dataSnapshot.getValue(User.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Lida com exceção, se ocorrer algum erro durante a recuperação dos dados
                databaseError.toException().printStackTrace();
            }
        });
        return user[0];
    }

    public void deleteUser(String key) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(key);

        CompletableFuture<Void> writeFuture = new CompletableFuture<>();
        databaseRef.removeValue((error, ref) -> {
            if (error == null) {
                writeFuture.complete(null); // Marca a gravação como concluída
            } else {
                writeFuture.completeExceptionally(error.toException()); // Marca a gravação como falha
            }
        });
        // Aguarda a conclusão da gravação
        try {
            writeFuture.get(); // Bloqueia a execução até que a gravação seja concluída ou falhe
        } catch (InterruptedException | ExecutionException err) {
            // Lida com exceção, se ocorrer algum erro na gravação
            System.err.println(err.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection connection = new Connection();
       // User user = new User("1","Ellen","Ellencassiamatos@gmail.com");
        connection.deleteUser("1");
    }
}