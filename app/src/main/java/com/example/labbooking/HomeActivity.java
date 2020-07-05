package com.example.labbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.labbooking.Common.Common;
import com.example.labbooking.Fragments.BookFragment;
import com.example.labbooking.Fragments.HomeFragment;
import com.example.labbooking.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BottomSheetDialog bottomSheetDialog;

    CollectionReference userRef;

    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(HomeActivity.this);

        //Init
        userRef = FirebaseFirestore.getInstance().collection("User");
        dialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .build();

        //Check intent, if is login = true, enable full access
        //If is login = false, just allow user around to view lab seats
        if(getIntent() != null){
            boolean isLogin = getIntent().getBooleanExtra(Common.IS_LOGIN, false);
            if (isLogin){
                dialog.show();

                //Check if user exists
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Save userPhone by Paper
//                Paper.init(HomeActivity.this);
//                Paper.book().write(Common.LOGGED_KEY, user.getPhoneNumber());

                DocumentReference currentUser = userRef.document(user.getPhoneNumber());
                currentUser.get()
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                DocumentSnapshot userSnapShot = task.getResult();
                                if(!userSnapShot.exists()){

                                    showUpdateDialog(user.getPhoneNumber());
                                }
                                else {
                                    //If user already available in our system
                                    Common.currentUser = userSnapShot.toObject(User.class);
                                    bottomNavigationView.setSelectedItemId(R.id.action_home);
                                }
                                if(dialog.isShowing())
                                    dialog.dismiss();
                            }
                        });

            }
        }

        //View
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.action_home)
                    fragment = new HomeFragment();
                else if(menuItem.getItemId() == R.id.action_book)
                    fragment = new BookFragment();

                return loadFragment(fragment);
            }
        });


    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void showUpdateDialog(String phoneNumber) {


        //Init dialog
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setTitle("Update your details");
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_information, null);

        Button btn_update = sheetView.findViewById(R.id.btnUpdate);

        TextInputEditText edit_name = sheetView.findViewById(R.id.edit_name);
        TextInputEditText edit_student_number = sheetView.findViewById(R.id.edit_student_number);
        TextInputEditText edit_phone_number = sheetView.findViewById(R.id.edit_phone_number);
        TextInputEditText edit_degree = sheetView.findViewById(R.id.edit_degree);

        //Figure out how to dispaly STUDENT NUMBER! UNEDITABLE Video3 @16:00
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        DocumentReference currentUser = userRef.document(user.getPhoneNumber());
//        edit_student_number.setText(userRef.document(user.getS));

        btn_update.setOnClickListener(v -> {
            if(!dialog.isShowing())
                dialog.show();
            User user = new User(edit_name.getText().toString(),
                    edit_student_number.getText().toString(),
                    edit_degree.getText().toString(),
                    phoneNumber);
            userRef.document(phoneNumber)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            bottomSheetDialog.dismiss();
                            if(!dialog.isShowing())
                                dialog.dismiss();

                            Common.currentUser = user;
                            bottomNavigationView.setSelectedItemId(R.id.action_home);

                            Toast.makeText
                                    (HomeActivity.this,
                                            "Thank you",
                                            Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener((e) -> {
                bottomSheetDialog.dismiss();
                if(!dialog.isShowing())
                    dialog.show();
                Toast.makeText
                        (HomeActivity.this,
                                "" + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
            });
        });

        bottomSheetDialog.setContentView(sheetView);

        bottomSheetDialog.show();

    }

    }
