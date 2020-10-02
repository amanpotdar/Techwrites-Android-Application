import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
   private FirebaseAuth mAuth;
   Toolbar toolbar;
   NavigationView nav_view;
   DrawerLayout drawer_layout;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       toolbar = (Toolbar)findViewById(R.id.toolbar);
       nav_view = (NavigationView)findViewById(R.id.nav_view);
       drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
       setSupportActionBar(toolbar);

       NavController navController = Navigation.findNavController(this,R.id.fragment);
       NavigationUI.setupWithNavController(nav_view,navController);
       NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout);


       mAuth = FirebaseAuth.getInstance();
   }

   @Override
   public boolean onSupportNavigateUp() {
       return NavigationUI.navigateUp(Navigation.findNavController(this,R.id.fragment),drawer_layout);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.option_menu,menu);
       return true;
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId() == R.id.action_logout){
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setMessage("Are you sure?")
                   .setCancelable(false)
                   .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                          Logout();
                       }
 })
                   .setNegativeButton("No",new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           dialog.cancel();
                       }
                   }).create().show();
       }

       return super.onOptionsItemSelected(item);
   }

   public void Logout() {
       mAuth.signOut();
       Intent loginIntent = new Intent(this, LoginActivity.class);
       startActivity(loginIntent);
   }


}

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainFragment extends Fragment implements Adapter.OnArticleListener {

   NavController navController;
   private DatabaseReference mDatabase;
   Adapter adapter;
   RecyclerView recyclerView;
   ArrayList<Item> mList = new ArrayList<>();
   boolean reload = true;

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_main, container, false);

       if(reload)
       mDatabase = FirebaseDatabase.getInstance().getReference().child("articles");
       mDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               mList.clear();
              for (DataSnapshot  item : dataSnapshot.getChildren())
              {
                  String articleName = item.child("articleName").getValue(String.class);
                  String authorName = item.child("authorUID").getValue(String.class);
                  String articleColor = item.child("articleColor").getValue(String.class);
                  String NumLikes = item.child("NumLikes").getValue(String.class);
                  String [] tags = new String[3];
                  int i=0;
                  for(DataSnapshot tag: item.child("tags").getChildren()){
                      tags[i] = tag.getValue(String.class);
                      i++;
                  }
                  mList.add(new Item(articleColor,articleName,authorName,NumLikes,tags));
              }
              adapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


