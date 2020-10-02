import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity extends AppCompatActivity {
   private FirebaseAuth mAuth;
   EditText textEmail1,textPassword1,textName;



   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_signup);

       mAuth = FirebaseAuth.getInstance();
       textEmail1 = (EditText)findViewById(R.id.textEmail1);
       textPassword1 = (EditText)findViewById(R.id.textPassword1);
       textName = (EditText)findViewById(R.id.textName);

   }

   public void Signup(View view){
       String email = textEmail1.getText().toString().trim();
       String password = textPassword1.getText().toString().trim();
       final String name = textName.getText().toString().trim();

       mAuth.createUserWithEmailAndPassword(email, password)
               .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           //Log.d(TAG, "createUserWithEmail:success");
                           FirebaseUser user = mAuth.getCurrentUser();
                           UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                   .setDisplayName(name)
                                   .build();

                           user.updateProfile(profileUpdates)
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if (task.isSuccessful()) {
                                               //Log.d(TAG, "User profile updated.");
                                               updateUIToMain();
                                           }
                                       }
                                   });
                           updateUIToMain();
                       } else {
                           //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                           Toast.makeText(SignupActivity.this, "Signup failed, " + task.getException(),
                                   Toast.LENGTH_SHORT).show();
                       }

                   }
               });
  }

   public void updateUIToMain(){
       Intent mainIntent = new Intent(this, MainActivity.class);
       startActivity(mainIntent);
   }

   public void ToSignin(View view){
       Intent loginIntent = new Intent(this, LoginActivity.class);
       startActivity(loginIntent);
   }
}
