package com.bonioctavianus.android.instafake.ui.createpost

import com.bonioctavianus.android.instafake.model.Image
import com.bonioctavianus.android.instafake.service.StorageService
import com.bonioctavianus.android.instafake.service.UserService
import com.bonioctavianus.android.instafake.usecase.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class CreatePostInteractor @Inject constructor(
    private val mUserService: UserService,
    private val mStorageService: StorageService
) {

    fun compose(): ObservableTransformer<CreatePostIntent, CreatePostPartialState> {
        return ObservableTransformer { intents ->
            intents.publish { intent ->
                Observable.mergeArray(
                    intent.ofType(CreatePostIntent.SelectPicture::class.java)
                        .compose(selectPicture),
                    intent.ofType(CreatePostIntent.SelectCamera::class.java)
                        .compose(selectFromCamera),
                    intent.ofType(CreatePostIntent.SelectGallery::class.java)
                        .compose(selectFromGallery),
                    intent.ofType(CreatePostIntent.SavePictureUri::class.java)
                        .compose(savePictureUri),
                    intent.ofType(CreatePostIntent.ChangeDescription::class.java)
                        .compose(changeMessage),
                    intent.ofType(CreatePostIntent.Upload::class.java)
                        .compose(upload)
                )
            }
        }
    }

    private val selectPicture =
        ObservableTransformer<CreatePostIntent.SelectPicture, CreatePostPartialState> { intents ->
            intents.flatMap {
                Observable.just(
                    CreatePostPartialState.PictureSelected
                )
            }
        }

    private val selectFromCamera =
        ObservableTransformer<CreatePostIntent.SelectCamera, CreatePostPartialState> { intents ->
            intents.flatMap {
                Observable.just(
                    CreatePostPartialState.CameraSelected
                )
            }
        }

    private val selectFromGallery =
        ObservableTransformer<CreatePostIntent.SelectGallery, CreatePostPartialState> { intents ->
            intents.flatMap {
                Observable.just(
                    CreatePostPartialState.GallerySelected
                )
            }
        }

    private val savePictureUri =
        ObservableTransformer<CreatePostIntent.SavePictureUri, CreatePostPartialState> { intents ->
            intents.flatMap { intent ->
                Observable.just(
                    CreatePostPartialState.PictureUriSaved(intent.uri, intent.source)
                )
            }
        }

    private val changeMessage =
        ObservableTransformer<CreatePostIntent.ChangeDescription, CreatePostPartialState> { intents ->
            intents.flatMap { intent ->
                Observable.just(
                    CreatePostPartialState.DescriptionChanged(intent.message)
                )
            }
        }

    private val upload =
        ObservableTransformer<CreatePostIntent.Upload, CreatePostPartialState> { intents ->
            intents.flatMap { intent ->
                mUserService.getUser()
                    .flatMap { user ->
                        val image = Image(
                            userId = user.userId!!,
                            userEmail = user.userEmail!!,
                            description = intent.description ?: "",
                            uri = intent.uri!!,
                            source = intent.source!!
                        )
                        mStorageService.uploadImage(image)
                            .map { result ->
                                when (result) {
                                    is Result.InFlight -> CreatePostPartialState.Upload.InFlight
                                    is Result.Success<*> -> CreatePostPartialState.Upload.Success
                                    is Result.Error -> CreatePostPartialState.Upload.Error(result.throwable)
                                }
                            }
                    }
                    .onErrorReturn { exception ->
                        CreatePostPartialState.Upload.Error(exception)
                    }
            }
        }
}