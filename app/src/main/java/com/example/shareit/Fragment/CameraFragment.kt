package com.example.shareit.Fragment

import android.hardware.Camera
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.shareit.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CameraFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CameraFragment : Fragment(), SurfaceHolder.Callback {
    lateinit var camera: Camera;
    lateinit var mSurfaceView: SurfaceView;
    lateinit var mSurfaceHolder: SurfaceHolder;

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mSurfaceHolder = mSurfaceView.holder;
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        camera = Camera.open();
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        TODO("Not yet implemented")
    }
}