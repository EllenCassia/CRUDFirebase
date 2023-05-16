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

    @PostConstruct
    private void initializeFirebase() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("./serviceAccount.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://bdii-firebase-default-rtdb.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createNewUser(User user) throws InterruptedException, ExecutionException {

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
        } catch (ExecutionException e) {
            // Lida com exceção, se ocorrer algum erro na gravação
            throw e;
        }
    }

    public User returnUser(String key) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(key);
        User user = null;
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Converte o DataSnapshot para o objeto User
                    user = dataSnapshot.getValue(User.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Lida com exceção, se ocorrer algum erro durante a recuperação dos dados
                databaseError.toException().printStackTrace();
            }
        });
        return user;
    }

    public void deleteUser(String key) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(key);

        databaseRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    System.out.println("Dado excluído com sucesso no Firebase");
                } else {
                    System.out.println("Erro ao excluir o dado no Firebase: " + databaseError.getMessage());
                }
            }
        });
    }
}
