import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
   private FirebaseAuth mAuth;
   EditText textEmail;
   EditText textPassword;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_login);
       mAuth = FirebaseAuth.getInstance();

       textEmail = (EditText)findViewById(R.id.textEmail);
       textPassword = (EditText)findViewById(R.id.textPassword);
   }

   @Override
   protected void onStart() {
       super.onStart();

       FirebaseUser currentUser = mAuth.getCurrentUser();
       if(currentUser != null){
           Intent intent = new Intent(this, MainActivity.class);
           startActivity(intent);
       }
   }

   public void SignIn(View view) {
       String email = textEmail.getText().toString().trim();
       String password = textPassword.getText().toString().trim();
       mAuth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           FirebaseUser user = mAuth.getCurrentUser();
                           updateUIToMain();
                       } else {
                           //Log.w(TAG, "signInWithEmail:failure", task.getException());
                           Toast.makeText(LoginActivity.this, "Authentication failed.",
                                   Toast.LENGTH_SHORT).show();
                       }
                   }
               });
   }

   public void updateUIToMain(){
       Intent mainIntent = new Intent(this, MainActivity.class);
       startActivity(mainIntent);
   }

 public void ToSignup(View view) {
       Intent signupIntent = new Intent(this, SignupActivity.class);
       startActivity(signupIntent);
   }

   public void Forgot(View view) {
       Intent forgotIntent = new Intent(this, ForgotPassword.class);
       startActivity(forgotIntent);
   }
}
