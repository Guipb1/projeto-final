package com.guilherme.contatos.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.guilherme.contatos.database.DataSingleton;

public class ServiceGoogleAuthentication {

    public static GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    private static boolean isSucess=false;
    private static Context context;
    private static ServiceGoogleAuthentication instace;
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private ServiceGoogleAuthentication(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("372500344698-fcr5e3997vp7oepdavk2m7a1amcqd93q.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        this.context = context;
    }
    public static ServiceGoogleAuthentication getInstance(Context context){
        if(instace==null){
            instace = new ServiceGoogleAuthentication(context);
        }
        return instace;
    };

    public  boolean SignIn(int requestCode, Activity context, Intent data){
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);

                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success");
                                    DataSingleton.getInstance().user = mAuth.getCurrentUser();
                                    isSucess = task.isComplete();

                                } else {
                                    isSucess = task.isCanceled();
                                }
                            }
                        });

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
            return isSucess;
        }


        return false;
    }


    public FirebaseUser GetUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return  currentUser;
    }

    public void SignOut(){
        FirebaseAuth.getInstance().signOut();
    }


}
