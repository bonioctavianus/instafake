package com.bonioctavianus.android.instafake.ui.createpost

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bonioctavianus.android.instafake.R
import com.bonioctavianus.android.instafake.base.BaseFragment
import com.bonioctavianus.android.instafake.model.Image
import com.bonioctavianus.android.instafake.service.ImageService
import com.bonioctavianus.android.instafake.utils.CheckPermission
import com.bonioctavianus.android.instafake.utils.PermissionGranted
import com.bonioctavianus.android.instafake.utils.SingleLiveEvent
import com.bonioctavianus.android.instafake.utils.showToast
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_create_post.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class CreatePostFragment : BaseFragment<CreatePostIntent, CreatePostViewState>() {

    companion object {
        private const val REQUEST_IMAGE_CAMERA = 1
        private const val REQUEST_IMAGE_GALLERY = 2
    }

    @Inject
    lateinit var mEventBus: EventBus
    @Inject
    lateinit var mImageService: ImageService
    @Inject
    lateinit var mViewModel: CreatePostViewModel

    private val mSubject: PublishSubject<CreatePostIntent> = PublishSubject.create()

    private var mCurrentImageCameraUri: String? = null

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        create_post_view.mCameraSelectedListener = {
            mEventBus.post(CheckPermission)
        }

        create_post_view.mGallerySelectedListener = {
            if (!mImageService.requestPictureFromGallery(this, REQUEST_IMAGE_GALLERY)) {
                showToast("Cannot perform operation")
            }
        }

        create_post_view.mSaveSuccessListener = {
            showToast("Upload Success")
            findNavController().navigateUp()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this)
        }
    }

    override fun intents(): Observable<CreatePostIntent> {
        return Observable.merge(
            create_post_view.intents(),
            mSubject.hide()
        )
    }

    override fun bindIntent(intent: Observable<CreatePostIntent>) {
        mViewModel.bindIntent(intent)
    }

    override fun observeState(state: MutableLiveData<CreatePostViewState>) {
        state.observe(viewLifecycleOwner, Observer { value ->
            create_post_view.render(value)
        })
    }

    override fun state(): MutableLiveData<CreatePostViewState> = mViewModel.state()

    override fun observeEvent(event: SingleLiveEvent<CreatePostViewState>?) = Unit

    override fun event(): SingleLiveEvent<CreatePostViewState>? = mViewModel.event()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK) {
            mCurrentImageCameraUri?.let {
                mImageService.scanMediaForImage(it)
                mSubject.onNext(
                    CreatePostIntent.SavePictureUri(it, Image.ImageSource.CAMERA)
                )
            }
        }

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            val pictureUri: Uri? = data?.data

            mSubject.onNext(
                CreatePostIntent.SavePictureUri(pictureUri.toString(), Image.ImageSource.GALLERY)
            )
        }
    }

    @Subscribe
    fun onEvent(event: PermissionGranted) {
        mCurrentImageCameraUri =
            mImageService.requestPictureFromCamera(this, REQUEST_IMAGE_CAMERA)
        if (mCurrentImageCameraUri == null) {
            showToast("Cannot perform operation")
        }
    }

    override fun onStop() {
        super.onStop()
        if (mEventBus.isRegistered(this)) {
            mEventBus.unregister(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_create_post_menu, menu)
        create_post_view.mMenuItemSend = menu.findItem(R.id.action_menu_send)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_send) {
            mSubject.onNext(
                CreatePostIntent.Upload(
                    create_post_view.getImageUri(),
                    create_post_view.getImageDescription(),
                    create_post_view.getImageSource()
                )
            )
        }
        return super.onOptionsItemSelected(item)
    }
}