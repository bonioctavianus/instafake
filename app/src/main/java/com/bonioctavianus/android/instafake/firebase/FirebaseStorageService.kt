package com.bonioctavianus.android.instafake.firebase

import android.net.Uri
import com.bonioctavianus.android.instafake.model.Image
import com.bonioctavianus.android.instafake.service.StorageService
import com.bonioctavianus.android.instafake.usecase.Result
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import timber.log.Timber
import java.io.File
import java.io.FileInputStream

class FirebaseStorageService(
    private val mFirebaseStorage: FirebaseStorage
) : StorageService {

    companion object {
        private const val METADATA_USER_ID = "user_id"
        private const val METADATA_USER_NAME = "user_name"
        private const val METADATA_DESCRIPTION = "description"
    }

    override fun getImages(): Observable<Result> {
        return getDirectories()
            .flatMapIterable { prefix -> prefix }
            .flatMap { item -> getImages(item) }
            .flatMapIterable { image -> image }
            .flatMap { image ->
                Observable.zip(
                    getImageMetadata(image),
                    getImageDownloadUrl(image),
                    BiFunction { metadata: StorageMetadata, uri: Uri ->
                        Image(
                            userId = metadata.getCustomMetadata(METADATA_USER_ID) ?: "",
                            userEmail = metadata.getCustomMetadata(METADATA_USER_NAME) ?: "",
                            description = metadata.getCustomMetadata(METADATA_DESCRIPTION) ?: "",
                            uri = uri.toString(),
                            source = Image.ImageSource.CLOUD,
                            creationTimeInMillis = metadata.creationTimeMillis
                        )
                    }
                )
            }
            .doOnNext {
                Timber.d("Image Uri: ${it.uri}")
            }
            .toList()
            .map { it.sortedByDescending { image -> image.creationTimeInMillis } }
            .toObservable()
            .map { Result.Success(it) }
            .cast(Result::class.java)
            .startWith(Result.InFlight)
            .onErrorReturn { exception ->
                Result.Error(exception)
            }
    }

    private fun getDirectories(): Observable<List<StorageReference>> {
        return Observable.create { emitter ->
            mFirebaseStorage.reference.listAll()
                .addOnSuccessListener { result ->
                    emitter.onNext(result.prefixes)
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    private fun getImages(reference: StorageReference): Observable<List<StorageReference>> {
        return Observable.create { emitter ->
            reference.listAll()
                .addOnSuccessListener { result ->
                    emitter.onNext(result.items)
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    private fun getImageMetadata(reference: StorageReference): Observable<StorageMetadata> {
        return Observable.create { emitter ->
            reference.metadata
                .addOnSuccessListener { result ->
                    emitter.onNext(result)
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    private fun getImageDownloadUrl(reference: StorageReference): Observable<Uri> {
        return Observable.create { emitter ->
            reference.downloadUrl
                .addOnSuccessListener { result ->
                    emitter.onNext(result)
                    emitter.onComplete()
                }
                .addOnFailureListener { exception ->
                    emitter.onError(exception)
                }
        }
    }

    override fun uploadImage(image: Image): Observable<Result> {
        return Observable.create { emitter ->
            emitter.onNext(
                Result.InFlight
            )

            val uploadTask = createUploadTask(image)

            uploadTask.addOnSuccessListener {
                emitter.onNext(
                    Result.Success(Unit)
                )
                emitter.onComplete()
            }

            uploadTask.addOnFailureListener { exception ->
                emitter.onNext(
                    Result.Error(exception)
                )
                emitter.onComplete()
            }
        }
    }

    private fun createUploadTask(image: Image): UploadTask {
        val uri = Uri.parse(image.uri)
        val storageRef = mFirebaseStorage.reference
        val imageRef = storageRef.child(image.userId).child(uri.lastPathSegment!!)
        val metadata = StorageMetadata.Builder()
            .setCustomMetadata(METADATA_USER_ID, image.userId)
            .setCustomMetadata(
                METADATA_USER_NAME,
                image.userEmail.substring(0, image.userEmail.indexOf("@"))
            )
            .setCustomMetadata(METADATA_DESCRIPTION, image.description)
            .build()

        return if (image.source == Image.ImageSource.CAMERA) {
            imageRef.putStream(
                FileInputStream(
                    File(image.uri)
                ), metadata
            )
        } else {
            imageRef.putFile(uri, metadata)
        }
    }
}