package com.bonioctavianus.android.instafake.firebase

import com.bonioctavianus.android.instafake.model.User
import com.bonioctavianus.android.instafake.service.UserService
import com.bonioctavianus.android.instafake.usecase.Result
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable

class FirebaseUserService(
    private val mFirebaseAuth: FirebaseAuth
) : UserService {

    override fun isSignedIn(): Observable<Boolean> {
        return Observable.fromCallable {
            mFirebaseAuth.currentUser != null
        }
    }

    override fun getUser(): Observable<User> {
        return Observable.just(
            User(
                userId = mFirebaseAuth.uid,
                userEmail = mFirebaseAuth.currentUser?.email
            )
        )
    }

    override fun signIn(email: String, password: String): Observable<Result> {
        return Observable.create { emitter ->
            emitter.onNext(
                Result.InFlight
            )
            try {
                mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        emitter.onNext(
                            Result.Success(Unit)
                        )
                        emitter.onComplete()
                    }
                    .addOnFailureListener { exception ->
                        emitter.onNext(
                            Result.Error(exception)
                        )
                        emitter.onComplete()
                    }
            } catch (e: IllegalArgumentException) {
                emitter.onNext(
                    Result.Error(e)
                )
                emitter.onComplete()
            }
        }
    }

    override fun signOut(): Observable<Result> {
        return Observable.fromCallable {
            mFirebaseAuth.signOut()
            Result.Success(Unit)
        }
            .cast(Result::class.java)
            .startWith(Result.InFlight)
            .onErrorReturn { exception ->
                Result.Error(exception)
            }
    }
}