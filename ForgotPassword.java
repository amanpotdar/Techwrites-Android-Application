import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

   private FirebaseAuth mAuth;
   EditText forgotemail;
   TextView emailSent;
   Button reset;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_forgotpassword);

       mAuth = FirebaseAuth.getInstance();
       forgotemail = (EditText)findViewById(R.id.forgotemail);
       emailSent = (TextView)findViewById(R.id.emailSent);
       reset = (Button)findViewById(R.id.reset);
   }

   public void ResetPassword(View view) {
       String emailAddress = forgotemail.getText().toString().trim();
       mAuth.sendPasswordResetEmail(emailAddress)
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()) {
                           //Log.d(TAG, "Email sent.");
                           emailSent.setVisibility(View.VISIBLE);
                           reset.setVisibility(View.GONE);
                           forgotemail.setText("");
                       }
                   }
               });
   }
}
