import java.io.IOException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws Exception {
        // Inicialize o Firestore usando as credenciais e as opções padrão
        FirestoreOptions options = FirestoreOptions.getDefaultInstance()
                .toBuilder()
                .setProjectId("crud-firebase-f9fbd")
                .build();
        Firestore firestore = options.getService();

        // Exemplo de leitura de um documento
        DocumentReference docRef = firestore.collection("testing").document("1a1pub3pf5lEl2tzp8ZR");
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            System.out.println("Documento encontrado: " + document.getData());
        } else {
            System.out.println("Documento não encontrado.");
        }

        // Feche a conexão com o Firestore quando não for mais necessário
        firestore.close();
    }
}
